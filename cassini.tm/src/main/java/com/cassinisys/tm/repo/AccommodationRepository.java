package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMAccommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 03-08-2016.
 */
@Repository
public interface AccommodationRepository extends JpaRepository<TMAccommodation, Integer>, QueryDslPredicateExecutor<TMAccommodation> {

    public List<TMAccommodation> findByIdIn(Iterable<Integer> ids);
    TMAccommodation findByName(String name);
}
