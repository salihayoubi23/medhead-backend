package com.medhead.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class RecommendationRequest {

    @NotBlank
    private String speciality;

    @NotBlank
    private String originZone; // ex: LONDON_CENTRAL

    public RecommendationRequest() {}

    public String getSpeciality() { return speciality; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }

    public String getOriginZone() { return originZone; }
    public void setOriginZone(String originZone) { this.originZone = originZone; }
}
