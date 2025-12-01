package com.cassinisys.irste.repo;

import com.cassinisys.irste.model.IRSTEUserUtilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 19-11-2018.
 */
@Repository
public interface IRSTEUserUtilityRepository extends JpaRepository<IRSTEUserUtilities, Integer> {

    @Query(
            "SELECT a.id.utility FROM IRSTEUserUtilities a WHERE a.id.personId= :personId"
    )
    List<String> findByResponder(@Param("personId") Integer personId);

    @Query(
            "SELECT a.id.personId FROM IRSTEUserUtilities a WHERE lower(a.id.utility)= lower(:utility)"
    )
    List<Integer> findByUtility(@Param("utility") String utility);

    @Query(
            "SELECT a.id.personId FROM IRSTEUserUtilities a WHERE lower(a.id.utility)= lower(:utility) AND a.personType = :personType"
    )
    List<Integer> findByUtilityAndPersonType(@Param("utility") String utility, @Param("personType") Integer personType);

}
