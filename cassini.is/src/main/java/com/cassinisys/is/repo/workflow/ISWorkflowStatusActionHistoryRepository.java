package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowStatusActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by CassiniSystems on 21-01-2020.
 */
public interface ISWorkflowStatusActionHistoryRepository extends JpaRepository<ISWorkflowStatusActionHistory, Integer> {
    List<ISWorkflowStatusActionHistory> findByWorkflowAndStatusOrderByTimestampDesc(Integer wfId, Integer statusId);
}
