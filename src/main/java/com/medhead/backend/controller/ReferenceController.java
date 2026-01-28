package com.medhead.backend.controller;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medhead.backend.repository.ZoneRepository;

@RestController
public class ReferenceController {

    private final JdbcTemplate jdbcTemplate;
    private final ZoneRepository zoneRepository;

    public ReferenceController(JdbcTemplate jdbcTemplate, ZoneRepository zoneRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.zoneRepository = zoneRepository;
    }

    // ✅ Toutes les spécialités depuis hospital_speciality
    @GetMapping("/specialities")
    public List<NameDto> specialities() {
        String sql = """
            SELECT DISTINCT speciality
            FROM hospital_speciality
            ORDER BY speciality
        """;
        return jdbcTemplate.query(sql, (rs, i) -> new NameDto(rs.getString("speciality")));
    }

    // ✅ Toutes les zones depuis la table zone
    @GetMapping("/zones")
    public List<ZoneDto> zones() {
        // CrudRepository renvoie Iterable -> on convertit en List
        return ((List<com.medhead.backend.model.Zone>) zoneRepository.findAll())
                .stream()
                .sorted((a,b) -> a.getCode().compareTo(b.getCode()))
                .map(z -> new ZoneDto(z.getCode(), z.getLat(), z.getLon()))
                .toList();
    }

    public record NameDto(String name) {}
    public record ZoneDto(String code, double lat, double lon) {}
}
