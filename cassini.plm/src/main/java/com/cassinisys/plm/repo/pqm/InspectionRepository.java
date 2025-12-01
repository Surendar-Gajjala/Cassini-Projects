package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMInspection;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface InspectionRepository extends JpaRepository<PQMInspection, Integer>, QueryDslPredicateExecutor<PQMInspection> {

    List<PQMInspection> findByIdIn(Iterable<Integer> ids);

    PQMInspection findByInspectionNumber(String number);

    List<PQMInspection> findByInspectionPlan(Integer id);

    List<PQMInspection> findByReleasedTrue();

    @Query("SELECT count(i) FROM PQMInspection i where i.released = true")
    Integer getReleasedInspections();

    @Query("SELECT count(i) FROM PQMInspection i where i.statusType= :statusType")
    Integer getRejectedInspections(@Param("statusType") WorkflowStatusType statusType);

    @Query("SELECT count(i) FROM PQMInspection i where i.released = false and i.statusType != 'REJECTED'")
    Integer getPendingInspections();

    @Query("select count (i) from PQMInspection i")
    Integer getAllInspectionCounts();

}
