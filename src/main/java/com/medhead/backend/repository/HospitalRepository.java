package com.medhead.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.medhead.backend.model.Hospital;

public interface HospitalRepository extends CrudRepository<Hospital, String> {

    @Query("select distinct h from Hospital h left join fetch h.specialities")
    List<Hospital> findAllWithSpecialities();

    @Query("""
        select distinct h
        from Hospital h
        join h.specialities s
        where s = :speciality
          and h.availableBeds > 0
    """)
    List<Hospital> findCandidates(@Param("speciality") String speciality);
}
