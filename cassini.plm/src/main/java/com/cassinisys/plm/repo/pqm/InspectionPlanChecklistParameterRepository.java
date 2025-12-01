package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMInspectionPlanChecklistParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 10-06-2020.
 */
@Repository
public interface InspectionPlanChecklistParameterRepository extends JpaRepository<PQMInspectionPlanChecklistParameter, Integer> {

    List<PQMInspectionPlanChecklistParameter> findByChecklistOrderByIdAsc(Integer checklist);
}
