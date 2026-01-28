package com.medhead.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medhead.backend.model.Hospital;
import com.medhead.backend.service.HospitalService;

@RestController
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping("/hospitals")
    public List<Hospital> hospitals() {
        return hospitalService.findAll();
    }
}
