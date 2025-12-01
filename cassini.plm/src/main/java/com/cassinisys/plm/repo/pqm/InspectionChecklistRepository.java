package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.ChecklistResult;
import com.cassinisys.plm.model.pqm.ChecklistStatus;
import com.cassinisys.plm.model.pqm.PQMInspectionChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface InspectionChecklistRepository extends JpaRepository<PQMInspectionChecklist, Integer> {

    @Query("select i from PQMInspectionChecklist i where i.inspection = :id and i.planChecklist.parent is null order by i.planChecklist.seq asc")
    List<PQMInspectionChecklist> findByInspectionAndParentIsNullOrderBySeqAsc(@Param("id") Integer id);

    @Query("select i from PQMInspectionChecklist i where i.inspection= :inspection and i.planChecklist.parent= :parent order by i.planChecklist.seq asc")
    List<PQMInspectionChecklist> findByParentOrderBySeqAsc(@Param("inspection") Integer inspection, @Param("parent") Integer parent);

    @Query("select i from PQMInspectionChecklist i where i.inspection= :inspection and i.planChecklist.type= :checklistType order by i.planChecklist.seq asc")
    List<PQMInspectionChecklist> findByInspectionAndType(@Param("inspection") Integer inspection, @Param("checklistType") String checklistType);

    @Query("select i from PQMInspectionChecklist i where i.inspection= :inspection and i.status= :status and i.planChecklist.type= :checklistType order by i.planChecklist.seq asc")
    List<PQMInspectionChecklist> findByInspectionAndStatusAndType(@Param("inspection") Integer inspection, @Param("status") ChecklistStatus status, @Param("checklistType") String checklistType);

    @Query("select i from PQMInspectionChecklist i where i.inspection= :inspection and i.result= :result and i.planChecklist.type= :checklistType order by i.planChecklist.seq asc")
    List<PQMInspectionChecklist> findByInspectionAndResultAndType(@Param("inspection") Integer inspection, @Param("result") ChecklistResult result, @Param("checklistType") String checklistType);

    List<PQMInspectionChecklist> findByAssignedToAndResult(Integer id, ChecklistResult checklistResult);
}
