package org.example.dto;

import java.time.LocalDate;

public class GetCompletedContractsParamsDTO {
    public LocalDate startDate;

    public LocalDate endDate;

    public GetCompletedContractsParamsDTO(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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
}
