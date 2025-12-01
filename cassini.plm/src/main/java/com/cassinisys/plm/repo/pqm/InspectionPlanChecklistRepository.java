package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMInspectionPlanChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface InspectionPlanChecklistRepository extends JpaRepository<PQMInspectionPlanChecklist, Integer> {

    List<PQMInspectionPlanChecklist> findByInspectionPlanAndParentIsNullOrderBySeqAsc(Integer planId);

    List<PQMInspectionPlanChecklist> findByParentOrderBySeqAsc(Integer parent);

    PQMInspectionPlanChecklist findByInspectionPlanAndTitleAndParentIsNull(Integer planId, String title);

    PQMInspectionPlanChecklist findByParentAndTitle(Integer parent, String title);

    List<PQMInspectionPlanChecklist> findByInspectionPlanAndType(Integer id, String checklist);
}
