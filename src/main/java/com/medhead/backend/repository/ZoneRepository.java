package com.medhead.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.medhead.backend.model.Zone;

public interface ZoneRepository extends CrudRepository<Zone, String> {

    @Query("select z.code from Zone z order by z.code")
    List<String> findAllCodes();
}
