package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMInspectionPlanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface InspectionPlanHistoryRepository extends JpaRepository<PQMInspectionPlanHistory, Integer> {

    List<PQMInspectionPlanHistory> findByInspectionPlanOrderByTimestampDesc(Integer id);
}
