package com.cassinisys.tms.repo;

import com.cassinisys.tms.model.TMSTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 20-10-2016.
 */
@Repository
public interface TripRepository extends JpaRepository<TMSTrip, Integer>, QueryDslPredicateExecutor<TMSTrip> {
}
