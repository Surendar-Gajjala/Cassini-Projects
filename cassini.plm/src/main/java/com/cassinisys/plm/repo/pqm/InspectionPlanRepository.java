package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMInspectionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface InspectionPlanRepository extends JpaRepository<PQMInspectionPlan, Integer>, QueryDslPredicateExecutor<PQMInspectionPlan> {


    List<PQMInspectionPlan> findByIdIn(Iterable<Integer> ids);

    PQMInspectionPlan findByNumber(String number);

    PQMInspectionPlan findByName(String number);

    @Query("select count (i) from PQMInspectionPlan i")
    Integer getAllInspectionPlanCounts();

}
