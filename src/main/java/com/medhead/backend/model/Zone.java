package com.medhead.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "zone")
public class Zone {

    @Id
    @Column(length = 50)
    private String code;

    private double lat;
    private double lon;

    public Zone() {}

    public Zone(String code, double lat, double lon) {
        this.code = code;
        this.lat = lat;
        this.lon = lon;
    }

    public String getCode() { return code; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }

    public void setCode(String code) { this.code = code; }
    public void setLat(double lat) { this.lat = lat; }
    public void setLon(double lon) { this.lon = lon; }
}
