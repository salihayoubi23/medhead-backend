package com.medhead.backend;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.medhead.backend.service.DistanceMatrixService;

@SpringBootTest
class DistanceMatrixServiceTest {

    @Autowired
    private DistanceMatrixService distanceMatrixService;

    @Test
    void shouldReturnDistanceForKnownZoneAndHospital() {
        var result = distanceMatrixService.getDistance("LONDON_CENTRAL", "HOSP-001");
        assertTrue(result.isPresent(), "Distance should be present for known zone/hospital");
        assertTrue(result.get().getDurationMin() > 0, "durationMin should be > 0");
    }

    @Test
    void shouldReturnEmptyForUnknownZone() {
        var result = distanceMatrixService.getDistance("UNKNOWN_ZONE", "HOSP-001");
        assertTrue(result.isEmpty(), "Distance should be empty for unknown zone");
    }
}
