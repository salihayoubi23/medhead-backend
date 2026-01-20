package com.medhead.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class RecommendationRequest {

    @NotBlank
    private String speciality;

    private double lat;
    private double lon;

    public RecommendationRequest() {
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
