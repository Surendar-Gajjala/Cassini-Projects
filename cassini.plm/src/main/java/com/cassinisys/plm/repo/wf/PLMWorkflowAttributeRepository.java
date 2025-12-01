package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 006 6-Sep -17.
 */
@Repository
public interface PLMWorkflowAttributeRepository extends JpaRepository<PLMWorkflowAttribute, Integer> {
    @Query(
            "SELECT a FROM PLMWorkflowAttribute a WHERE a.id.objectId= :workflowId"
    )
    List<PLMWorkflowAttribute> findByWorkflowIdIn(@Param("workflowId") Integer workflowId);

    @Query(
            "SELECT a FROM PLMWorkflowAttribute a WHERE a.id.attributeDef= :attributeId"
    )
    List<PLMWorkflowAttribute> findByAttributeId(@Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM PLMWorkflowAttribute a WHERE a.id.objectId= :workflowId AND a.id.attributeDef= :attributeId"
    )
    PLMWorkflowAttribute findByWorkflowAndAttribute(@Param("workflowId") Integer workflowId, @Param("attributeId") Integer attributeId);
}
