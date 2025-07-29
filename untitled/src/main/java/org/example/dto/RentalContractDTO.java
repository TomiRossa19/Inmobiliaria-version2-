package org.example.dto;

import org.example.enums.PropertyTypeEnum;
import org.example.enums.StatusEnum;
import org.example.models.RentalContract;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalContractDTO {
    public String tenantName;

    public PropertyTypeEnum propertyType;

    public BigDecimal monthlyRent;

    public LocalDate startDate;

    public LocalDate endDate;

    public StatusEnum status;

    public RentalContractDTO(String tenantName, PropertyTypeEnum propertyType, BigDecimal monthlyRent, LocalDate startDate, LocalDate endDate, StatusEnum status) {
        this.tenantName = tenantName;
        this.propertyType = propertyType;
        this.monthlyRent = monthlyRent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public PropertyTypeEnum getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyTypeEnum propertyType) {
        this.propertyType = propertyType;
    }

    public BigDecimal getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(BigDecimal monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public static RentalContractDTO fromEntity(RentalContract rentalContract){
        if(rentalContract == null){
            return null;
        }
        return new RentalContractDTO(rentalContract.getTenantName(), rentalContract.getPropertyType(), rentalContract.getMonthlyRent(), rentalContract.getStartDate(), rentalContract.getEndDate(), rentalContract.getStatus());
    }
}
