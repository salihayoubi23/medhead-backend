package com.medhead.backend.model;

import java.util.List;

public class Hospital {

    private String id;
    private String name;
    private int availableBeds;
    private List<String> specialities;
    private double lat;
    private double lon;

    public Hospital() {
        // nécessaire pour Jackson
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAvailableBeds() {
        return availableBeds;
    }

    public List<String> getSpecialities() {
        return specialities;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
