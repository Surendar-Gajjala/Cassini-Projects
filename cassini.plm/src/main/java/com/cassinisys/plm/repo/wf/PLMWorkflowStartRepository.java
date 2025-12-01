package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowStart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 5/22/17.
 */
@Repository
public interface PLMWorkflowStartRepository extends JpaRepository<PLMWorkflowStart, Integer> {
}
