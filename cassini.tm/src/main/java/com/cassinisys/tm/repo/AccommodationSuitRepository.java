package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMAccommodationSuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 03-08-2016.
 */
@Repository
public interface AccommodationSuitRepository extends JpaRepository<TMAccommodationSuit, Integer>, QueryDslPredicateExecutor<TMAccommodationSuit> {
    List<TMAccommodationSuit> findByAccommodation(Integer accomId);
    TMAccommodationSuit findByAccommodationAndName(Integer accommId, String name);
}
