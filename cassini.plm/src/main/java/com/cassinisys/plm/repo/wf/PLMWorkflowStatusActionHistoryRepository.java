package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowStatusActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by reddy on 5/30/17.
 */
public interface PLMWorkflowStatusActionHistoryRepository extends JpaRepository<PLMWorkflowStatusActionHistory, Integer> {
    List<PLMWorkflowStatusActionHistory> findByWorkflowAndStatusOrderByTimestampDesc(Integer wfId, Integer statusId);
}
