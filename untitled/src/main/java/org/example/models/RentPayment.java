package org.example.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "rent_payment")
public class RentPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private RentalContract contractID;

    @Column(name = "pay_date")
    private LocalDate payDate;

    @Column(name = "amount")
    private BigDecimal amount;

    public RentPayment(){

    }

    public RentPayment(RentalContract contractID, LocalDate payDate, BigDecimal amount) {
        this.contractID = contractID;
        this.payDate = payDate;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RentalContract getContractID() {
        return contractID;
    }

    public void setContractID(RentalContract contractID) {
        this.contractID = contractID;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
