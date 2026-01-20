package com.medhead.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medhead.backend.model.Speciality;

@RestController
public class SpecialityController {

    @GetMapping("/specialities")
    public List<Speciality> getSpecialities() {
        return List.of(
            new Speciality("Cardiologie"),
            new Speciality("Médecine d'urgence"),
            new Speciality("Pédiatrie")
        );
    }
}
