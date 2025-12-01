package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 5/30/17.
 */
@Repository
public interface PLMWorkflowStatusHistoryRepository extends JpaRepository<PLMWorkflowStatusHistory, Integer> {
    List<PLMWorkflowStatusHistory> findByWorkflowOrderByTimestampDesc(Integer wfId);
    List<PLMWorkflowStatusHistory> findByWorkflowOrderByTimestampAsc(Integer wfId);
    PLMWorkflowStatusHistory  findByStatus(Integer id);
}
