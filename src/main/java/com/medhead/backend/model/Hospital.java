package com.medhead.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "hospital")
public class Hospital {

    @Id
    private String id;

    private String name;

    @Column(name = "available_beds")
    private int availableBeds;

    private double lat;
    private double lon;

    @ElementCollection
    @CollectionTable(name = "hospital_speciality", joinColumns = @JoinColumn(name = "hospital_id"))
    @Column(name = "speciality")
    private List<String> specialities = new ArrayList<>();

    public Hospital() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAvailableBeds() { return availableBeds; }
    public List<String> getSpecialities() { return specialities; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAvailableBeds(int availableBeds) { this.availableBeds = availableBeds; }
    public void setSpecialities(List<String> specialities) { this.specialities = specialities; }
    public void setLat(double lat) { this.lat = lat; }
    public void setLon(double lon) { this.lon = lon; }
}
