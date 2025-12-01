package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 04-09-2017.
 */
@Repository
public interface WorkflowTypeAttributeRepository extends JpaRepository<PLMWorkflowTypeAttribute, Integer> {

    List<PLMWorkflowTypeAttribute> findByWorkflowTypeOrderByName(Integer id);

    List<PLMWorkflowTypeAttribute> findByWorkflowTypeOrderBySeq(Integer id);

    PLMWorkflowTypeAttribute findByWorkflowTypeAndName(Integer id, String name);

    List<PLMWorkflowTypeAttribute> findByWorkflowType(Integer id);

}
