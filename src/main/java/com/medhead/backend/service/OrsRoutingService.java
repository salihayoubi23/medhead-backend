package com.medhead.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrsRoutingService {

    public record RouteResult(double distanceKm, int durationMin) {}

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ors.api.key}")
    private String apiKey;

    @Value("${ors.profile:driving-car}")
    private String profile;

    public RouteResult route(double originLat, double originLon, double destLat, double destLon) {

        String url = "https://api.openrouteservice.org/v2/directions/" + profile;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", apiKey);

        // ORS attend [lon, lat]
        Map<String, Object> body = Map.of(
                "coordinates", List.of(
                        List.of(originLon, originLat),
                        List.of(destLon, destLat)
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

        // Parse minimal : features[0].properties.summary.distance (m), duration (s)
        List<Map<String, Object>> features = (List<Map<String, Object>>) response.get("features");
        Map<String, Object> properties = (Map<String, Object>) features.get(0).get("properties");
        Map<String, Object> summary = (Map<String, Object>) properties.get("summary");

        double distanceMeters = ((Number) summary.get("distance")).doubleValue();
        double durationSeconds = ((Number) summary.get("duration")).doubleValue();

        double km = distanceMeters / 1000.0;
        int min = (int) Math.round(durationSeconds / 60.0);

        return new RouteResult(km, min);
    }
}
