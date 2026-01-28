package com.medhead.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class OrsDistanceService {

    public record OrsResult(double distanceMeters, double durationSeconds) {}

    private final RestTemplate restTemplate;

    @Value("${ors.api.key}")
    private String apiKey;

    @Value("${ors.profile:driving-car}")
    private String profile;

    public OrsDistanceService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public OrsResult route(double originLon, double originLat, double destLon, double destLat) {

        String url = "https://api.openrouteservice.org/v2/directions/" + profile;

        Map<String, Object> body = Map.of(
                "coordinates", List.of(
                        List.of(originLon, originLat),
                        List.of(destLon, destLat)
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(
                MediaType.valueOf("application/geo+json"),
                MediaType.APPLICATION_JSON
        ));
        headers.set("Authorization", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {

            // Appel ORS
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            Map<?, ?> root = response.getBody();
            if (root == null) {
                throw new IllegalStateException("ORS error: empty body");
            }

            // Parsing format "routes"
            Object routesObj = root.get("routes");
            if (!(routesObj instanceof List<?> routes) || routes.isEmpty()) {
                Object err = root.get("error");
                Object msg = root.get("message");
                throw new IllegalStateException(
                        "ORS: no routes in response. error=" + err + " message=" + msg + " body=" + root
                );
            }

            Map<?, ?> route0 = (Map<?, ?>) routes.get(0);
            Map<?, ?> summary = (Map<?, ?>) route0.get("summary");

            if (summary == null || summary.get("distance") == null || summary.get("duration") == null) {
                throw new IllegalStateException("ORS: missing summary fields in response: " + root);
            }

            double distance = ((Number) summary.get("distance")).doubleValue();
            double duration = ((Number) summary.get("duration")).doubleValue();

            return new OrsResult(distance, duration);

        } catch (HttpStatusCodeException e) {
            throw new IllegalStateException(
                    "ORS HTTP " + e.getStatusCode() + " body=" + e.getResponseBodyAsString(),
                    e
            );
        }
    }
}
