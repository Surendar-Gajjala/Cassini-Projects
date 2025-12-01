package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowRevisionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 03-06-2020.
 */
@Repository
public interface WorkflowRevisionHistoryRepository extends JpaRepository<PLMWorkflowRevisionHistory, Integer> {

	List<PLMWorkflowRevisionHistory> findByWorkflowOrderByTimestampDesc(Integer id);

}
