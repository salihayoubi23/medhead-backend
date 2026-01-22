package com.medhead.backend.dto;

public class ReservationResponse {

    private String hospitalId;
    private String hospitalName;
    private int remainingBeds;
    private String message;

    public ReservationResponse() {}

    public ReservationResponse(String hospitalId, String hospitalName, int remainingBeds, String message) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.remainingBeds = remainingBeds;
        this.message = message;
    }

    public String getHospitalId() { return hospitalId; }
    public String getHospitalName() { return hospitalName; }
    public int getRemainingBeds() { return remainingBeds; }
    public String getMessage() { return message; }
}
