package com.medhead.backend.dto;

public class RecommendationResponse {

    private String hospitalName;
    private String reason;

    public RecommendationResponse(String hospitalName, String reason) {
        this.hospitalName = hospitalName;
        this.reason = reason;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getReason() {
        return reason;
    }
}
