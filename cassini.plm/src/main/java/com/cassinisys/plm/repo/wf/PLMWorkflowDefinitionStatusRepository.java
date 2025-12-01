package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowDefinitionStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 19-05-2017.
 */
@Repository
public interface PLMWorkflowDefinitionStatusRepository extends JpaRepository<PLMWorkflowDefinitionStatus, Integer> {
    List<PLMWorkflowDefinitionStatus> findByWorkflow(Integer id);


    @Query("select i from PLMWorkflowDefinitionStatus i where i.workflow= :workflow and i.type = 'NORMAL' order by i.id asc")
    List<PLMWorkflowDefinitionStatus> getNormalWorkflowStatuses(@Param("workflow") Integer workflow);

    @Query("select i from PLMWorkflowDefinitionStatus i where i.workflow= :workflow order by i.id asc")
    List<PLMWorkflowDefinitionStatus> getWorkflowStatuses(@Param("workflow") Integer workflow);

    @Query("select i from PLMWorkflowDefinitionStatus i where i.workflow= :workflow and i.name= :name and i.type= :type")
    PLMWorkflowDefinitionStatus getWorkflowDefinitionStatusByWorkflowAndNameAndType(@Param("workflow") Integer workflow, @Param("name") String name, @Param("type") WorkflowStatusType type);
}
