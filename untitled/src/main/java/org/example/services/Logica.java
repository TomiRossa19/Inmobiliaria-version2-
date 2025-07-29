package org.example.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.dto.AddPaymentParamsDTO;
import org.example.enums.StatusEnum;
import org.example.models.RentPayment;
import org.example.models.RentalContract;
import org.example.utils.HibernateUtil;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Logica {
    private static Logica instance;

    private Logica(){

    }

    public Logica getInstance(){
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

    }
}
