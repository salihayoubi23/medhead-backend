package com.medhead.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.medhead.backend.dto.RecommendationRequest;
import com.medhead.backend.dto.RecommendationResponse;

import jakarta.validation.Valid;

@RestController
public class RecommendationController {

    @PostMapping("/recommendations")
    public RecommendationResponse recommend(@Valid @RequestBody RecommendationRequest request) {

        // Version 1 (mock) : réponse fixe pour valider le flux
        return new RecommendationResponse(
                "Hôpital Fred Brooks",
                "Mock response: speciality=" + request.getSpeciality() + ", location=" + request.getLocation()
        );
    }
}
