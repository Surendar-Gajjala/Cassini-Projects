package com.cassinisys.test.repo;

import com.cassinisys.test.model.RunSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RunScheduleRepository extends JpaRepository<RunSchedule, Integer>, QueryDslPredicateExecutor<RunSchedule> {

    @Query("SELECT i FROM RunSchedule i WHERE i.runConfig.id= :runConfig")
    RunSchedule findByRunConfig(@Param("runConfig") Integer runConfig);
}
