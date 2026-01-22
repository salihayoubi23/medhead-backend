package com.medhead.backend.dto;

public record RecommendationResponse(
        String hospitalId,
        String hospitalName,
        Integer availableBeds,
        Double distanceKm,
        Integer durationMin,
        String reason
) {}
