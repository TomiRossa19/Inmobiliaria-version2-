package org.example.dto;

import java.math.BigDecimal;

public class GetNotFinishedContractsDTO {
    public BigDecimal expectedAmount;

    public BigDecimal payedAmount;

    public GetNotFinishedContractsDTO(BigDecimal expectedAmount, BigDecimal payedAmount) {
        this.expectedAmount = expectedAmount;
        this.payedAmount = payedAmount;
    }

    public BigDecimal getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(BigDecimal expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public BigDecimal getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(BigDecimal payedAmount) {
        this.payedAmount = payedAmount;
    }
}
