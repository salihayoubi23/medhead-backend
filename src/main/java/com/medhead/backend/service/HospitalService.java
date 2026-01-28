package com.medhead.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medhead.backend.model.Hospital;
import com.medhead.backend.repository.HospitalRepository;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Transactional(readOnly = true)
    public List<Hospital> findAll() {
        return (List<Hospital>) hospitalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Hospital> findById(String hospitalId) {
        return hospitalRepository.findById(hospitalId);
    }

    @Transactional
    public Optional<Hospital> reserveOneBed(String hospitalId) {
        Optional<Hospital> opt = hospitalRepository.findById(hospitalId);
        if (opt.isEmpty()) return Optional.empty();

        Hospital h = opt.get();
        if (h.getAvailableBeds() <= 0) {
            return Optional.of(h); // rien à réserver
        }

        h.setAvailableBeds(h.getAvailableBeds() - 1);
        Hospital saved = hospitalRepository.save(h);
        return Optional.of(saved);
    }
}
