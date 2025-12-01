package com.cassinisys.irste.repo;

import com.cassinisys.irste.model.IRSTEGroupUtility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Nageshreddy on 21-12-2018.
 */
@Repository
public interface IRSTEGroupUtilityRepository extends JpaRepository<IRSTEGroupUtility, Integer> {

    @Query(
            "SELECT g.id.group FROM IRSTEGroupUtility g WHERE lower(g.id.utility)= lower(:utility)"
    )
    String findGroupByUtility(@Param("utility") String utility);

}
