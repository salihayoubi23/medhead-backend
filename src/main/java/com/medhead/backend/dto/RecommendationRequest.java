package com.medhead.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class RecommendationRequest {

    @NotBlank
    private String speciality;

    @NotBlank
    private String location;

    public RecommendationRequest() {
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
