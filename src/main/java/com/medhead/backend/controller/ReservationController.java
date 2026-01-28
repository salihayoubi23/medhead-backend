package com.medhead.backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medhead.backend.dto.ReservationRequest;
import com.medhead.backend.dto.ReservationResponse;
import com.medhead.backend.model.Hospital;
import com.medhead.backend.service.HospitalService;

import jakarta.validation.Valid;

@RestController
public class ReservationController {

    private final HospitalService hospitalService;

    public ReservationController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> reserve(@Valid @RequestBody ReservationRequest request) {

        Optional<Hospital> existing = hospitalService.findById(request.getHospitalId());
        if (existing.isEmpty()) {
            ReservationResponse body = new ReservationResponse(
                    request.getHospitalId(),
                    null,
                    0,
                    "Hospital not found"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        Hospital before = existing.get();
        if (before.getAvailableBeds() <= 0) {
            ReservationResponse body = new ReservationResponse(
                    before.getId(),
                    before.getName(),
                    0,
                    "No beds available (reservation refused)"
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

        Hospital updated = hospitalService.reserveOneBed(before.getId()).orElse(before);

        ReservationResponse body = new ReservationResponse(
                updated.getId(),
                updated.getName(),
                updated.getAvailableBeds(),
                "Reservation confirmed (1 bed reserved)"
        );
        return ResponseEntity.ok(body);
    }
}
