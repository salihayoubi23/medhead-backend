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

    public List<Hospital> loadHospitals() {
        try (InputStream is = new ClassPathResource("hospitals.json").getInputStream()) {
            return objectMapper.readValue(is, new TypeReference<List<Hospital>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
