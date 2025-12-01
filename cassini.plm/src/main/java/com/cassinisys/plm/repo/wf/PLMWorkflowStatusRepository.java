package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Repository
public interface PLMWorkflowStatusRepository extends JpaRepository<PLMWorkflowStatus, Integer> {
    List<PLMWorkflowStatus> findByWorkflow(Integer id);

    List<PLMWorkflowStatus> findByIdIn(Iterable<Integer> varl);

    PLMWorkflowStatus findByWorkflowAndNameAndType(Integer workflow, String name, WorkflowStatusType statusType);

    List<PLMWorkflowStatus> findByWorkflowAndType(Integer workflow, WorkflowStatusType statusType);

    @Query("select i from PLMWorkflowStatus i where i.workflow= :workflow order by i.id asc")
    List<PLMWorkflowStatus> getWorkflowStatuses(@Param("workflow") Integer workflow);

    @Query("select distinct i.name from PLMWorkflowStatus i where i.id in :ids")
    List<String> getWorkflowStatusesNames(@Param("ids") List<Integer> ids);

    @Query("select distinct i.name from PLMWorkflowStatus i where i.id in :ids and i.flag != 'TERMINATED'")
    List<String> getWorkflowStatusesNamesNotTerminated(@Param("ids") List<Integer> ids);

    @Query("select distinct i.transitionedFrom from PLMWorkflowStatus i where i.id in :ids and i.transitionedFrom is not null and i.flag = 'TERMINATED'")
    List<Integer> getWorkflowTransitionFromIdsNamesByIdsAndTerminated(@Param("ids") List<Integer> ids);

    @Query("select distinct i.workflow from PLMWorkflowStatus i where i.transitionedFrom in :ids and i.transitionedFrom is not null and i.flag = 'TERMINATED'")
    List<Integer> getWorkflowIdsNamesByIdsAndTerminated(@Param("ids") List<Integer> ids);

    @Query("select i.transitionedFrom from PLMWorkflowStatus i where i.workflow in :ids and i.transitionedFrom is not null and i.flag = 'INPROCESS'")
    List<Integer> getTransitionedFromIdsByWorkflow(@Param("ids") List<Integer> ids);

    @Query("select count (i) from PLMWorkflowStatus i where i.workflow in :ids and i.transitionedFrom is null and i.flag = 'INPROCESS'")
    Integer getTransitionedFromIsNullCountByWorkflow(@Param("ids") List<Integer> ids);

    @Query("select i.workflow from PLMWorkflowStatus i where i.workflow in :ids and i.transitionedFrom is null and i.flag = 'INPROCESS'")
    List<Integer> getTransitionedFromIsNullByWorkflow(@Param("ids") List<Integer> ids);

    @Query("select distinct i.workflow from PLMWorkflowStatus i where i.name= :name")
    List<Integer> getWorkflowIdsByName(@Param("name") String name);

    @Query("select i.id from PLMWorkflowStatus i where i.name= :name")
    List<Integer> getIdsByName(@Param("name") String name);

    @Query("select i.workflow from PLMWorkflowStatus i where i.transitionedFrom in :ids and i.flag = 'INPROCESS'")
    List<Integer> getWorflowsByIds(@Param("ids") List<Integer> ids);

    @Query("select distinct i.name from PLMWorkflowStatus i where i.workflow in :ids and (i.type = 'RELEASED' or i.type = 'REJECTED') and i.flag = 'COMPLETED'")
    List<String> getNamesByTypeAndFlag(@Param("ids") List<Integer> ids);

    @Query("select i.workflow from PLMWorkflowStatus i where i.id in :ids and i.flag = 'COMPLETED' and (i.type = 'RELEASED' or i.type = 'REJECTED')")
    List<Integer> getWorkflowIdsByFlagAndStatus(@Param("ids") List<Integer> ids);
}
