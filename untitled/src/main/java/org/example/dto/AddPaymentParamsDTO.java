package org.example.dto;

import java.math.BigDecimal;

public class AddPaymentParamsDTO {
    public Long contractID;

    public BigDecimal amount;

    public AddPaymentParamsDTO(Long contractID, BigDecimal amount) {
        this.contractID = contractID;
        this.amount = amount;
    }

    public Long getContractID() {
        return contractID;
    }

    public void setContractID(Long contractID) {
        this.contractID = contractID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
