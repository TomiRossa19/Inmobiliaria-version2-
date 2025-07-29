import org.example.dto.*;
import org.example.enums.PropertyTypeEnum;
import org.example.enums.StatusEnum;
import org.example.models.RentPayment;
import org.example.models.RentalContract;
import org.example.services.Logica;
import org.example.utils.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogicaTest {
    Logica logica;

    Session session;

    RentalContract rentalContract;

    RentPayment rentPayment;

    RentalContract rentalContract1;

    RentPayment rentPayment1;

    @BeforeAll
    void setUp(){
        logica = Logica.getInstance();

        session = HibernateUtil.getSession();

        rentalContract = new RentalContract("Tomás", PropertyTypeEnum.APARTMENT, BigDecimal.valueOf(20000), LocalDate.of(2025, 7, 7), LocalDate.of(2026,7,7), StatusEnum.ACTIVE);

        rentPayment = new RentPayment(rentalContract, LocalDate.of(2025, 8,7), BigDecimal.valueOf(20000));

        rentalContract1 = new RentalContract("Tomás", PropertyTypeEnum.APARTMENT, BigDecimal.valueOf(20000), LocalDate.of(2025, 7, 7), LocalDate.of(2026,7,7), StatusEnum.COMPLETED);

        rentPayment1 = new RentPayment(rentalContract, LocalDate.of(2025, 8,7), BigDecimal.valueOf(20000));

        session.beginTransaction();
        session.persist(rentalContract);
        session.persist(rentPayment);
        session.persist(rentalContract1);
        session.persist(rentPayment1);
        session.getTransaction().commit();
    }

    @AfterAll
    void tearDown(){
        if (session != null && session.isOpen()){
            session.beginTransaction();
            session.createQuery("delete from RentPayment").executeUpdate();
            session.createQuery("delete from RentalContract").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testGetContracts(){
        List<RentalContractDTO> resultado = logica.getContracts(new GetContractsParamsDTO(PropertyTypeEnum.APARTMENT, LocalDate.of(2020,7,7), "Tomás",LocalDate.of(2027,8,8), BigDecimal.valueOf(20), BigDecimal.valueOf(20000000)));
        assertFalse(resultado.isEmpty());
        assertNotNull(resultado);
        assertEquals(resultado.size(), 2);
    }

    @Test
    void testGetCompletedContracts(){
        List<GetCompletedContractsDTO> resultado = logica.getCompletedContracts(new GetCompletedContractsParamsDTO(LocalDate.of(2020,7,7), LocalDate.of(2027,8,8)));
        assertFalse(resultado.isEmpty());
        assertNotNull(resultado);
        assertEquals(resultado.size(), 1);
    }

    @Test
    void testGetNotFinishedContracts(){
        List<GetNotFinishedContractsDTO> resultado = logica.getNotFinishedContracts();
        assertFalse(resultado.isEmpty());
        assertNotNull(resultado);
        assertEquals(resultado.size(), 1);
    }
}
