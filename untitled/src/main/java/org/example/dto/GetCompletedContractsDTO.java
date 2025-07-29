package org.example.dto;

import org.example.enums.PropertyTypeEnum;

import java.math.BigDecimal;

public class GetCompletedContractsDTO {
    public PropertyTypeEnum propertyType;

    public Long contractsQuantity;

    public BigDecimal totalAmount;

    public GetCompletedContractsDTO(PropertyTypeEnum propertyType, Long contractsQuantity, BigDecimal totalAmount) {
        this.propertyType = propertyType;
        this.contractsQuantity = contractsQuantity;
        this.totalAmount = totalAmount;
    }

    public PropertyTypeEnum getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyTypeEnum propertyType) {
        this.propertyType = propertyType;
    }

    public Long getContractsQuantity() {
        return contractsQuantity;
    }

    public void setContractsQuantity(Long contractsQuantity) {
        this.contractsQuantity = contractsQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
