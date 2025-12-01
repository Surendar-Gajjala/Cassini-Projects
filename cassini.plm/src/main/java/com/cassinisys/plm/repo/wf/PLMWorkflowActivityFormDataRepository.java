package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowActivityFormData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 26-10-2021.
 */
@Repository
public interface PLMWorkflowActivityFormDataRepository extends JpaRepository<PLMWorkflowActivityFormData, Integer> {
    @Query(
            "SELECT a FROM PLMWorkflowActivityFormData a WHERE a.id.objectId= :workflowId"
    )
    List<PLMWorkflowActivityFormData> findByWorkflowIdIn(@Param("workflowId") Integer workflowId);

    @Query(
            "SELECT a FROM PLMWorkflowActivityFormData a WHERE a.id.attributeDef= :attributeId"
    )
    List<PLMWorkflowActivityFormData> findByAttributeId(@Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMWorkflowActivityFormData a WHERE a.id.objectId= :workflowId AND a.id.attributeDef= :attributeId"
    )
    PLMWorkflowActivityFormData findByWorkflowActivityAndAttribute(@Param("workflowId") Integer workflowId, @Param("attributeId") Integer attributeId);
}
