package com.cassinisys.platform.repo.wfm;

import com.cassinisys.platform.model.wfm.WorkflowHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 2/1/2016.
 */
@Repository
public interface WorkFlowHistoryRepository extends JpaRepository<WorkflowHistory, Integer> {
    List<WorkflowHistory> findByWorkflow(Integer id);
}