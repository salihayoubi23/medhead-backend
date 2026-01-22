package com.medhead.backend.controller;

import java.util.Optional;

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
    public ReservationResponse reserve(@Valid @RequestBody ReservationRequest request) {

        Optional<Hospital> existing = hospitalService.findById(request.getHospitalId());
        if (existing.isEmpty()) {
            return new ReservationResponse(request.getHospitalId(), null, 0,
                    "Hospital not found");
        }

        Hospital before = existing.get();
        if (before.getAvailableBeds() <= 0) {
            return new ReservationResponse(before.getId(), before.getName(), 0,
                    "No beds available (reservation refused)");
        }

        Hospital updated = hospitalService.reserveOneBed(before.getId()).get();

        return new ReservationResponse(
                updated.getId(),
                updated.getName(),
                updated.getAvailableBeds(),
                "Reservation confirmed (1 bed reserved)"
        );
    }
}
