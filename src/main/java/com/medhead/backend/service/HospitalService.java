package com.medhead.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medhead.backend.model.Hospital;

@Service
public class HospitalService {

    private final ObjectMapper objectMapper;

    // état PoC en mémoire : hospitalId -> Hospital
    private final Map<String, Hospital> store = new ConcurrentHashMap<>();

    public HospitalService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        // init au démarrage
        List<Hospital> initial = loadHospitalsFrom("hospitals.json");
        for (Hospital h : initial) {
            store.put(h.getId(), h);
        }
    }

    public List<Hospital> loadHospitals() {
        return new ArrayList<>(store.values());
    }

    public Optional<Hospital> findById(String hospitalId) {
        return Optional.ofNullable(store.get(hospitalId));
    }

    public synchronized Optional<Hospital> reserveOneBed(String hospitalId) {
        Hospital h = store.get(hospitalId);
        if (h == null) return Optional.empty();
        if (h.getAvailableBeds() <= 0) return Optional.of(h); // pas de lit
        h.setAvailableBeds(h.getAvailableBeds() - 1);
        return Optional.of(h);
    }

    // utile pour tests/chargement
    public List<Hospital> loadHospitalsFrom(String resourceName) {
        try (InputStream is = new ClassPathResource(resourceName).getInputStream()) {
            return objectMapper.readValue(is, new TypeReference<List<Hospital>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
