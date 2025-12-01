package com.cassinisys.irste.repo;

import com.cassinisys.irste.model.IRSTEUtilityLocation;
import com.cassinisys.platform.model.core.ObjectAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 13-11-2018.
 */
@Repository
public interface IRSTEUtilityLocationRepository extends JpaRepository<IRSTEUtilityLocation, Integer> {

    @Query(
            "SELECT a FROM IRSTEUtilityLocation a WHERE a.id.locationId= :locationId"
    )
    List<IRSTEUtilityLocation> findByLocationId(@Param("locationId") Integer locationId);

    @Query(
            "SELECT a.id.utility FROM IRSTEUtilityLocation a WHERE a.id.locationId= :locationId"
    )
    List<String> findUtilitiesByLocationId(@Param("locationId") Integer locationId);

    @Query(
            "SELECT a FROM IRSTEUtilityLocation a WHERE lower(a.id.utility)= lower(:utility)"
    )
    List<IRSTEUtilityLocation> findByUtility(@Param("utility") String utility);
}
