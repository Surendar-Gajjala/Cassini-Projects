package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 04-09-2017.
 */
@Repository
public interface WorkflowTypeRepository extends JpaRepository<PLMWorkflowType, Integer>, QueryDslPredicateExecutor<PLMWorkflowType> {
    @PostFilter("hasPermission(filterObject,'view')")
    List<PLMWorkflowType> findByParentTypeIsNullOrderByCreatedDateAsc();

    @PostFilter("hasPermission(filterObject,'view')")
    List<PLMWorkflowType> findByParentTypeIsNullOrderByIdAsc();

    List<PLMWorkflowType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    List<PLMWorkflowType> findByParentTypeOrderByIdAsc(Integer parent);

    PLMWorkflowType findByName(String name);

    PLMWorkflowType findByAssignable(String name);

    PLMWorkflowType findByAssignableAndAssignedType(String name, Integer type);

    List<PLMWorkflowType> findByIdIn(List<Integer> ids);

    List<PLMWorkflowType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<PLMWorkflowType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

    List<PLMWorkflowType> findByRevisionSequenceId(Integer lovId);

    @Query("select count (i) from PLMWorkflowType i where i.revisionSequence.id= :lovId")
    Integer getWorkflowTypesByLov(@Param("lovId") Integer lovId);

    @Query(value = "select count(i) FROM plm_workflowdefinition i JOIN plm_workflowtype p ON p.type_id = i.workflow_type WHERE i.revision= :lovValue AND p.revision_sequence= :lov", nativeQuery = true)
    Integer getTypeLovValueCount(@Param("lov") Integer lov, @Param("lovValue") String lovValue);
    @Query("select count (i) from PLMWorkflowType i where i.numberSource.id= :autoNumber")
    Integer getWorkflowTypeAutoNumberValueCount(@Param("autoNumber") Integer autoNumber);
}
