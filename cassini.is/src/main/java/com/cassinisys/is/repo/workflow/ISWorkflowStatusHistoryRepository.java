package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 5/30/17.
 */
@Repository
public interface ISWorkflowStatusHistoryRepository extends JpaRepository<ISWorkflowStatusHistory, Integer> {
    List<ISWorkflowStatusHistory> findByWorkflowOrderByTimestampDesc(Integer wfId);
}
