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
import com.medhead.backend.repository.HospitalRepository;
import com.medhead.backend.service.OrsDistanceService;
import com.medhead.backend.service.OrsDistanceService.OrsResult;
import com.medhead.backend.service.ZoneService;
import com.medhead.backend.service.ZoneService.ZonePoint;

import jakarta.validation.Valid;

@RestController
public class RecommendationController {

    private final HospitalRepository hospitalRepository;
    private final OrsDistanceService orsDistanceService;
    private final ZoneService zoneService;

    public RecommendationController(HospitalRepository hospitalRepository,
                                    OrsDistanceService orsDistanceService,
                                    ZoneService zoneService) {
        this.hospitalRepository = hospitalRepository;
        this.orsDistanceService = orsDistanceService;
        this.zoneService = zoneService;
    }

    @PostMapping("/recommendations")
    public RecommendationResponse recommend(@Valid @RequestBody RecommendationRequest request) {

        Optional<ZonePoint> originOpt = zoneService.findByCode(request.getOriginZone());
        if (originOpt.isEmpty()) {
            return new RecommendationResponse(
                    null, null, null, null, null,
                    "Zone inconnue : " + request.getOriginZone() + " (table zone)"
            );
        }
        ZonePoint origin = originOpt.get();

        List<Hospital> candidates = hospitalRepository.findCandidates(request.getSpeciality());
        if (candidates.isEmpty()) {
            return new RecommendationResponse(
                    null, null, null, null, null,
                    "Aucun hôpital pour speciality=" + request.getSpeciality() + " avec lits disponibles"
            );
        }

        int totalCandidates = candidates.size();

        Optional<HospitalScore> best = candidates.stream()
                // évite le cas distance=0/durée=0 si origine==destination
                .filter(h -> !(Double.compare(origin.lon(), h.getLon()) == 0
                        && Double.compare(origin.lat(), h.getLat()) == 0))
                // calcule score ORS, ignore si ORS échoue
                .map(h -> {
                    try {
                        OrsResult r = orsDistanceService.route(origin.lon(), origin.lat(), h.getLon(), h.getLat());
                        int durationMin = (int) Math.round(r.durationSeconds() / 60.0);
                        double distanceKm = r.distanceMeters() / 1000.0;
                        return new HospitalScore(h, distanceKm, durationMin);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(score -> score != null)
                .min(Comparator.comparingInt(HospitalScore::durationMin));

        if (best.isEmpty()) {
            return new RecommendationResponse(
                    null, null, null, null, null,
                    "Impossible de calculer une route ORS (candidats=" + totalCandidates + ")"
            );
        }

        HospitalScore chosen = best.get();
        double distanceKmRounded = Math.round(chosen.distanceKm * 10.0) / 10.0;

        return new RecommendationResponse(
                chosen.h.getId(),
                chosen.h.getName(),
                chosen.h.getAvailableBeds(),
                distanceKmRounded,
                chosen.durationMin,
                "Choisi via ORS (distance réelle) + spécialité + lits"
        );
    }

    private static class HospitalScore {
        final Hospital h;
        final double distanceKm;
        final int durationMin;

        HospitalScore(Hospital h, double distanceKm, int durationMin) {
            this.h = h;
            this.distanceKm = distanceKm;
            this.durationMin = durationMin;
        }

        int durationMin() { return durationMin; }
    }
}
