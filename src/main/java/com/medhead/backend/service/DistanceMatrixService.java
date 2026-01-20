package com.medhead.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DistanceMatrixService {

    public static class DistanceEntry {
        public double distanceKm;
        public int durationMin;

        public DistanceEntry() {}
        public double getDistanceKm() { return distanceKm; }
        public int getDurationMin() { return durationMin; }
    }

    private final ObjectMapper objectMapper;

    public DistanceMatrixService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // zone -> (hospitalId -> entry)
    public Map<String, Map<String, DistanceEntry>> loadMatrix() {
        try (InputStream is = new ClassPathResource("distance_matrix.json").getInputStream()) {
            return objectMapper.readValue(is, new TypeReference<Map<String, Map<String, DistanceEntry>>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public Optional<DistanceEntry> getDistance(String originZone, String hospitalId) {
        Map<String, Map<String, DistanceEntry>> matrix = loadMatrix();
        if (!matrix.containsKey(originZone)) return Optional.empty();
        return Optional.ofNullable(matrix.get(originZone).get(hospitalId));
    }
}
