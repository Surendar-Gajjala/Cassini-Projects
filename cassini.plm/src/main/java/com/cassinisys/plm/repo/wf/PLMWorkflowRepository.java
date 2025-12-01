package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Repository
public interface PLMWorkflowRepository extends JpaRepository<PLMWorkflow, Integer>, QueryDslPredicateExecutor<PLMWorkflow> {
    PLMWorkflow findByAttachedTo(Integer id);

    List<PLMWorkflow> findByIdIn(Iterable<Integer> varl);

    List<PLMWorkflow> findByName(String name);

    List<PLMWorkflow> findByWorkflowRevision(Integer id);

    @Modifying
    @Query(
            "DELETE FROM PLMWorkflow w WHERE w.id= :id"
    )
    void deleteById(@Param("id") Integer id);

    @Query("SELECT count(i) FROM PLMWorkflow i where i.started = false and i.finished = false and i.cancelled = false")
    Integer getNotStartedWfs();

    @Query("SELECT count(i) FROM PLMWorkflow i where i.finished = false and i.started = true and i.cancelled = false")
    Integer getNormalWfs();

    @Query("SELECT count(i) FROM PLMWorkflow i where i.finished = true and i.started = true and i.cancelled = false")
    Integer getReleasedWfs();

    @Query("SELECT count(i) FROM PLMWorkflow i where i.cancelled = true and i.started = true and i.finished = false")
    Integer getRejectedWfs();

   /* @Query(value = "SELECT count(i) FROM plm_workflow i join cassini_object o on i.attached_to = o.object_id where  cast(o.object_type as text) = :objectType", nativeQuery = true)
    Integer getTypeWorkflows(@Param("objectType")String objectType);*/

    @Query("SELECT count(i) FROM PLMWorkflow i where i.attachedToType = :objectType")
    Integer getTypeWorkflows(@Param("objectType") PLMObjectType objectType);

    @Query("SELECT count(i) FROM PLMWorkflow i where i.started = false and i.finished = false and i.cancelled = false and  i.attachedToType = :objectType")
    Integer getTypeNotStartedWfs(@Param("objectType") PLMObjectType objectType);

    @Query("SELECT count(i) FROM PLMWorkflow i where i.finished = false and i.started = true and i.cancelled = false and i.attachedToType = :objectType")
    Integer getTypeNormalWfs(@Param("objectType") PLMObjectType objectType);

    @Query("SELECT count(i) FROM PLMWorkflow i where i.finished = true and i.started = true and i.cancelled = false and i.attachedToType = :objectType")
    Integer getTypeReleasedWfs(@Param("objectType") PLMObjectType objectType);

    @Query("SELECT count(i) FROM PLMWorkflow i where i.cancelled = true and i.started = true and i.finished = false and  i.attachedToType = :objectType")
    Integer getTypeRejectedWfs(@Param("objectType") PLMObjectType objectType);

    @Query("SELECT i FROM PLMWorkflow i where i.started = true and i.finished = false and i.cancelled = false")
    List<PLMWorkflow> findByStartedWorkflows();

    @Query("SELECT i.id FROM PLMWorkflow i where i.attachedToType = :objectType")
    List<Integer> getObjectTypeWorkflows(@Param("objectType") PLMObjectType objectType);

    @Query("SELECT i.id FROM PLMWorkflow i where i.attachedTo in :attachedToIds")
    List<Integer> getIdsByAttachedToIds(@Param("attachedToIds") List<Integer> attachedToIds);

    @Query("SELECT count (i.currentStatus) FROM PLMWorkflow i where i.id in :ids and i.start.id = i.currentStatus")
    Integer getCurrentStatusCountByIdsAndStartEqualToCurrentStatus(@Param("ids") List<Integer> ids);

    @Query("SELECT i.currentStatus FROM PLMWorkflow i where i.id in :ids and i.start.id != i.currentStatus")
    List<Integer> getCurrentStatusIdsByIdsAndStartNotEqualToCurrentStatus(@Param("ids") List<Integer> ids);

    @Query("SELECT i.attachedTo FROM PLMWorkflow i where i.attachedToType = :objectType and i.id in :ids")
    List<Integer> getAttachedToIdsByIdsAndType(@Param("objectType") PLMObjectType objectType, @Param("ids") List<Integer> ids);

    @Query("SELECT i.attachedTo FROM PLMWorkflow i where i.attachedToType = :objectType and i.start.id = i.currentStatus")
    List<Integer> getStartWorkflowIds(@Param("objectType") PLMObjectType objectType);

    @Query("SELECT i.id FROM PLMWorkflow i where i.id in :ids and i.start.id != i.currentStatus")
    List<Integer> getIdsByIdsAndStartNotEqualCurrentStatus(@Param("ids") List<Integer> ids);

    @Query("SELECT i.id FROM PLMWorkflow i where i.id in :ids and i.start.id = i.currentStatus")
    List<Integer> getIdsByIdsAndStartEqualCurrentStatus(@Param("ids") List<Integer> ids);

    @Query("SELECT i.attachedTo FROM PLMWorkflow i where i.id in :ids")
    List<Integer> getAttachedToIdsByIds(@Param("ids") List<Integer> ids);

    @Query("SELECT i.attachedTo FROM PLMWorkflow i where i.attachedToType = :objectType and i.currentStatus in :ids")
    List<Integer> getCurrentStatusIdsByTypeAndStatus(@Param("objectType") PLMObjectType objectType, @Param("ids") List<Integer> ids);

    @Query("SELECT i.attachedTo FROM PLMWorkflow i where i.attachedToType = :objectType")
    List<Integer> getAttachedToIdsByType(@Param("objectType") PLMObjectType objectType);
}
