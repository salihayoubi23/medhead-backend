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
import com.medhead.backend.service.HospitalService;
import com.medhead.backend.util.DistanceUtil;

import jakarta.validation.Valid;

@RestController
public class RecommendationController {

    private final HospitalService hospitalService;

    public RecommendationController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping("/recommendations")
    public RecommendationResponse recommend(@Valid @RequestBody RecommendationRequest request) {

        // 1) charger les hôpitaux depuis le JSON
        List<Hospital> hospitals = hospitalService.loadHospitals();

        // 2) filtrer par spécialité + lits disponibles
        List<Hospital> candidates = hospitals.stream()
                .filter(h -> h.getAvailableBeds() > 0)
                .filter(h -> h.getSpecialities() != null
                        && h.getSpecialities().contains(request.getSpeciality()))
                .toList();

        if (candidates.isEmpty()) {
            return new RecommendationResponse(
                    null,
                    "No hospital found for speciality=" + request.getSpeciality() + " with available beds"
            );
        }

        // 3) choisir l’hôpital le plus proche
        Optional<Hospital> best = candidates.stream()
                .min(Comparator.comparingDouble(h ->
                        DistanceUtil.haversineKm(
                                request.getLat(),
                                request.getLon(),
                                h.getLat(),
                                h.getLon()
                        )
                ));

        Hospital chosen = best.get();
        double distanceKm = DistanceUtil.haversineKm(
                request.getLat(),
                request.getLon(),
                chosen.getLat(),
                chosen.getLon()
        );

        // 4) réponse
        return new RecommendationResponse(
                chosen.getName(),
                "Chosen by speciality + beds + nearest. distanceKm="
                        + String.format("%.2f", distanceKm)
        );
    }
}
