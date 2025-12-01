package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowFinish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 5/22/17.
 */
@Repository
public interface PLMWorkflowFinishRepository extends JpaRepository<PLMWorkflowFinish, Integer> {
}
