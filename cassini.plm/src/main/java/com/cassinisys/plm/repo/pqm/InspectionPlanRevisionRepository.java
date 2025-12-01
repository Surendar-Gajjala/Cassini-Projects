package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMInspectionPlan;
import com.cassinisys.plm.model.pqm.PQMInspectionPlanRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface InspectionPlanRevisionRepository extends JpaRepository<PQMInspectionPlanRevision, Integer> {
    List<PQMInspectionPlanRevision> findByPlan(PQMInspectionPlan plan);

    List<PQMInspectionPlanRevision> findByReleasedTrue();

    @Query("SELECT count(i) FROM PQMInspectionPlanRevision i where i.released = true and i.plan.latestRevision = i.id")
    Integer getReleasedInspectionsPlans();

    @Query("SELECT count(i) FROM PQMInspectionPlanRevision i where i.rejected = true and i.plan.latestRevision = i.id")
    Integer getRejectedInspectionsPlans();

    @Query("SELECT count(i) FROM PQMInspectionPlanRevision i where i.released = false and i.rejected = false and i.plan.latestRevision = i.id")
    Integer getPendingInspectionPlans();

    List<PQMInspectionPlanRevision> getByPlanOrderByCreatedDateDesc(PQMInspectionPlan id);

    @Query("select i.id from PQMInspectionPlanRevision i where i.lifeCyclePhase.phase= :phase")
    List<Integer> getRevisionIdsByPhase(@Param("phase") String phase);

    @Query("select i.id from PQMInspectionPlanRevision i where i.status= :status")
    List<Integer> getRevisionIdsByStatus(@Param("status") String status);

    @Query("select distinct i.status from PQMInspectionPlanRevision i")
    List<String> getStatus();
}
