package com.cassinisys.erp.repo.common;

import com.cassinisys.erp.model.common.ERPGeoTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 10/10/15.
 */
@Repository
public interface GeoTrackerRepository extends JpaRepository<ERPGeoTracker, Integer>,
        QueryDslPredicateExecutor<ERPGeoTracker> {
}
