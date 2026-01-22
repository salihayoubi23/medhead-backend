package com.medhead.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ReservationRequest {

    @NotBlank
    private String hospitalId;

    public ReservationRequest() {}

    public ReservationRequest(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}
