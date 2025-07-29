package org.example.dto;

import org.example.enums.PropertyTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GetContractsParamsDTO {
    public String tenantName;

    public PropertyTypeEnum propertyType;

    public LocalDate startDate;

    public LocalDate endDate;

    public BigDecimal startMonthlyRent;

    public BigDecimal endMonthlyRent;

    public GetContractsParamsDTO(PropertyTypeEnum propertyType, LocalDate startDate, String tenantName, LocalDate endDate, BigDecimal startMonthlyRent, BigDecimal endMonthlyRent) {
        this.propertyType = propertyType;
        this.startDate = startDate;
        this.tenantName = tenantName;
        this.endDate = endDate;
        this.startMonthlyRent = startMonthlyRent;
        this.endMonthlyRent = endMonthlyRent;
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

    public BigDecimal getStartMonthlyRent() {
        return startMonthlyRent;
    }

    public void setStartMonthlyRent(BigDecimal startMonthlyRent) {
        this.startMonthlyRent = startMonthlyRent;
    }

    public BigDecimal getEndMonthlyRent() {
        return endMonthlyRent;
    }

    public void setEndMonthlyRent(BigDecimal endMonthlyRent) {
        this.endMonthlyRent = endMonthlyRent;
    }
}
