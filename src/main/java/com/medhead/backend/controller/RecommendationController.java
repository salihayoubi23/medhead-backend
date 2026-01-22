package com.medhead.backend.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.medhead.backend.dto.RecommendationRequest;
import com.medhead.backend.dto.RecommendationResponse;
import com.medhead.backend.model.Hospital;
import com.medhead.backend.service.DistanceMatrixService;
import com.medhead.backend.service.DistanceMatrixService.DistanceEntry;
import com.medhead.backend.service.HospitalService;

import jakarta.validation.Valid;

@RestController
public class RecommendationController {

    private final HospitalService hospitalService;
    private final DistanceMatrixService distanceMatrixService;

    public RecommendationController(HospitalService hospitalService, DistanceMatrixService distanceMatrixService) {
        this.hospitalService = hospitalService;
        this.distanceMatrixService = distanceMatrixService;
    }

    @PostMapping("/recommendations")
    public RecommendationResponse recommend(@Valid @RequestBody RecommendationRequest request) {

        List<Hospital> hospitals = hospitalService.loadHospitals();

        // 1) filtrer spécialité + lits dispo
        List<Hospital> candidates = hospitals.stream()
                .filter(h -> h.getAvailableBeds() > 0)
                .filter(h -> h.getSpecialities() != null && h.getSpecialities().contains(request.getSpeciality()))
                .toList();

        if (candidates.isEmpty()) {
            return new RecommendationResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    "No hospital found for speciality=" + request.getSpeciality() + " with available beds"
            );
        }

        // 2) garder uniquement ceux qui ont une distance définie pour la zone
        List<Hospital> withDistance = candidates.stream()
                .filter(h -> distanceMatrixService.getDistance(request.getOriginZone(), h.getId()).isPresent())
                .toList();

        if (withDistance.isEmpty()) {
            return new RecommendationResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    "No distance data for originZone=" + request.getOriginZone() + " (update distance_matrix.json)"
            );
        }

        // 3) choisir le plus rapide (durationMin)
        Optional<Hospital> best = withDistance.stream()
                .min(Comparator.comparingInt(h -> {
                    DistanceEntry d = distanceMatrixService.getDistance(request.getOriginZone(), h.getId()).orElseThrow();
                    return d.getDurationMin();
                }));

        Hospital chosen = best.orElseThrow();
        DistanceEntry d = distanceMatrixService.getDistance(request.getOriginZone(), chosen.getId()).orElseThrow();

        double distanceKmRounded = Math.round(d.getDistanceKm() * 10.0) / 10.0;

        return new RecommendationResponse(
                chosen.getId(),
                chosen.getName(),
                chosen.getAvailableBeds(),
                distanceKmRounded,
                d.getDurationMin(),
                "Chosen by speciality + beds + routingDistanceMock"
        );
    }
}
