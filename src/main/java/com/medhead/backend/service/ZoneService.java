package com.medhead.backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.medhead.backend.model.Zone;
import com.medhead.backend.repository.ZoneRepository;

@Service
public class ZoneService {

    public record ZonePoint(String code, double lat, double lon) {}

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public Optional<ZonePoint> findByCode(String code) {
        return zoneRepository.findById(code)
                .map(z -> new ZonePoint(z.getCode(), z.getLat(), z.getLon()));
    }
}
