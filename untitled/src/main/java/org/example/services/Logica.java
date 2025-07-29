package org.example.services;

import jakarta.persistence.Table;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.example.dto.*;
import org.example.enums.PropertyTypeEnum;
import org.example.enums.StatusEnum;
import org.example.models.RentPayment;
import org.example.models.RentalContract;
import org.example.utils.HibernateUtil;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Logica {
    private static Logica instance;

    private Logica(){

    }

    public static Logica getInstance(){
        if (instance == null){
            instance = new Logica();
        }
        return instance;
    }

    public boolean addPayment(AddPaymentParamsDTO addPaymentParamsDTO){
        try (Session session = HibernateUtil.getSession()){
            RentalContract rentalContract = session.get(RentalContract.class, addPaymentParamsDTO.getContractID());
            if (rentalContract == null){
                System.out.println("El contrato no existe");
                return false;
            }else {
                RentPayment rentPayment = new RentPayment(rentalContract, LocalDate.now(), addPaymentParamsDTO.getAmount());

                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<RentPayment> cq = cb.createQuery(RentPayment.class);
                Root<RentPayment> root = cq.from(RentPayment.class);
                Predicate coincidence = cb.equal(root.get("contractID"), rentalContract);
                cq.where(coincidence);

                List<RentPayment> payments = session.createQuery(cq).getResultList();

                BigDecimal paymentTotal = BigDecimal.ZERO;

                for (RentPayment rentPayment1 : payments){
                    paymentTotal = paymentTotal.add(rentPayment1.getAmount());
                }

                Long months = ChronoUnit.MONTHS.between(rentalContract.getStartDate(), rentalContract.getEndDate());

                BigDecimal contractTotal = rentalContract.getMonthlyRent().multiply(BigDecimal.valueOf(months));

                if (paymentTotal.compareTo(contractTotal) >= 0){
                    rentalContract.setStatus(StatusEnum.COMPLETED);
                } else if (rentalContract.getEndDate().isBefore(LocalDate.now())) {
                    rentalContract.setStatus(StatusEnum.OVERDUE);
                }

                session.beginTransaction();
                session.persist(rentPayment);
                session.merge(rentalContract);
                session.getTransaction().commit();

                return true;
            }
        }
    }

    public List<RentalContractDTO> getContracts(GetContractsParamsDTO getContractsParamsDTO){
        try (Session session = HibernateUtil.getSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<RentalContract> cq = cb.createQuery(RentalContract.class);
            Root<RentalContract> root = cq.from(RentalContract.class);

            Predicate tenantName = cb.equal(root.get("tenantName"), getContractsParamsDTO.tenantName);

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(tenantName);

            if (getContractsParamsDTO.getPropertyType() != null){
                Predicate propertyType = cb.equal(root.get("propertyType"), getContractsParamsDTO.getPropertyType());
                predicates.add(propertyType);
            }

            if(getContractsParamsDTO.getStartDate() != null && getContractsParamsDTO.getEndDate() != null){
                Predicate dates = cb.between(root.get("startDate"), getContractsParamsDTO.getStartDate(), getContractsParamsDTO.getEndDate());
                predicates.add(dates);
            }

            if (getContractsParamsDTO.getStartMonthlyRent() != null && getContractsParamsDTO.getEndMonthlyRent() != null){
                Predicate rents = cb.between(root.get("monthlyRent"), getContractsParamsDTO.getStartMonthlyRent(), getContractsParamsDTO.getEndMonthlyRent());
                predicates.add(rents);
            }

            cq.where(cb.and(predicates.toArray(new Predicate[0])));

            List<RentalContract> contracts = session.createQuery(cq).getResultList();

            return contracts.stream()
                    .map(RentalContractDTO :: fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<GetCompletedContractsDTO> getCompletedContracts(GetCompletedContractsParamsDTO getCompletedContractsParamsDTO){
        try (Session session = HibernateUtil.getSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Tuple> cq = cb.createTupleQuery();
            Root<RentalContract> root = cq.from(RentalContract.class);

            Predicate completed = cb.equal(root.get("status"), StatusEnum.COMPLETED);
            Predicate dates = cb.between(root.get("startDate"), getCompletedContractsParamsDTO.getStartDate(), getCompletedContractsParamsDTO.getEndDate());

            cq.where(cb.and(completed, dates));

            cq.groupBy(root.get("propertyType"));

            Expression<Long> contractQuantity = cb.count(root);
            Expression<Integer> startYear = cb.function("YEAR", Integer.class, root.get("startDate"));
            Expression<Integer> endYear = cb.function("YEAR", Integer.class, root.get("endDate"));

            Expression<Integer> startMonth = cb.function("MONTH", Integer.class, root.get("startDate"));
            Expression<Integer> endMonth = cb.function("MONTH", Integer.class, root.get("endDate"));

// months = (endYear * 12 + endMonth) - (startYear * 12 + startMonth)
            Expression<Integer> months = cb.diff(
                    cb.sum(cb.prod(endYear, cb.literal(12)), endMonth),
                    cb.sum(cb.prod(startYear, cb.literal(12)), startMonth)
            );

            Expression<BigDecimal> totalAmount = cb.sum(cb.prod(root.get("monthlyRent"), cb.toBigDecimal(months)));

            cq.multiselect(root.get("propertyType").alias("propertyType"), contractQuantity.alias("contractQuantity"), totalAmount.alias("totalAmount"));

            List<Tuple> contracts = session.createQuery(cq).getResultList();

            List<GetCompletedContractsDTO> getCompletedContractsDTOS = new ArrayList<>();
            for (Tuple contract : contracts){
                GetCompletedContractsDTO getCompletedContractsDTO = new GetCompletedContractsDTO((PropertyTypeEnum) contract.get("propertyType"), (Long) contract.get("contractQuantity"), (BigDecimal) contract.get("totalAmount"));
                getCompletedContractsDTOS.add(getCompletedContractsDTO);
            }

            return getCompletedContractsDTOS;
        }
    }

    public List<GetNotFinishedContractsDTO> getNotFinishedContracts(){
        try (Session session = HibernateUtil.getSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<RentalContract> cq = cb.createQuery(RentalContract.class);
            Root<RentalContract> root = cq.from(RentalContract.class);

            Predicate notActiveOrOverdue = cb.or(cb.equal(root.get("status"), StatusEnum.ACTIVE), cb.equal(root.get("status"), StatusEnum.OVERDUE));

            cq.where(notActiveOrOverdue);

            List<RentalContract> rentalContracts = session.createQuery(cq).getResultList();

            List<GetNotFinishedContractsDTO> getNotFinishedContractsDTOS = new ArrayList<>();

            for (RentalContract rentalContract : rentalContracts){
                CriteriaQuery<RentPayment> cq1 = cb.createQuery(RentPayment.class);
                Root<RentPayment> root1 = cq1.from(RentPayment.class);
                Predicate coincidence = cb.equal(root1.get("contractID"), rentalContract);
                cq1.where(coincidence);

                List<RentPayment> rentPayments = session.createQuery(cq1).getResultList();

                BigDecimal paymentsTotal = BigDecimal.ZERO;
                for (RentPayment rentPayment : rentPayments){
                    paymentsTotal = paymentsTotal.add(rentPayment.getAmount());
                }

                Long months = ChronoUnit.MONTHS.between(rentalContract.getStartDate(), rentalContract.getEndDate());

                BigDecimal contractTotal = rentalContract.getMonthlyRent().multiply(BigDecimal.valueOf(months));

                GetNotFinishedContractsDTO getNotFinishedContractsDTO = new GetNotFinishedContractsDTO(contractTotal, paymentsTotal);
                getNotFinishedContractsDTOS.add(getNotFinishedContractsDTO);
            }

            return getNotFinishedContractsDTOS;
        }
    }
}
