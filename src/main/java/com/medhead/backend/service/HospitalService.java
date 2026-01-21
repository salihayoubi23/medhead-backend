package com.medhead.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medhead.backend.model.Hospital;

@Service
public class HospitalService {

    private final ObjectMapper objectMapper;

    public HospitalService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // comportement normal de l'app : on charge hospitals.json
    public List<Hospital> loadHospitals() {
        return loadHospitalsFrom("hospitals.json");
    }

    // utile pour les tests : charger un autre fichier (ex: hospitals_no_beds.json)
    public List<Hospital> loadHospitalsFrom(String resourceName) {
        try (InputStream is = new ClassPathResource(resourceName).getInputStream()) {
            return objectMapper.readValue(is, new TypeReference<List<Hospital>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
