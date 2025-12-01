package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.req.PLMRequirementDocumentChildren;
import com.cassinisys.plm.model.req.PLMRequirementVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 023 23-May -17.
 */
@Repository
public interface RequirementDocumentChildrenRepository extends JpaRepository<PLMRequirementDocumentChildren, Integer>, QueryDslPredicateExecutor<PLMRequirementDocumentChildren> {

    List<PLMRequirementDocumentChildren> findByDocumentAndRequirementVersionLatestTrue(Integer id);

    List<PLMRequirementDocumentChildren> findByDocumentAndParentIsNull(Integer document);

    @Query("SELECT i from PLMRequirementDocumentChildren i where i.document= :docId and i.parent is null and i.requirementVersion.latest = true order by i.requirementVersion.master.createdDate asc")
    List<PLMRequirementDocumentChildren> getByDocumentAndParentIsNullAndLatestTrueOrderByCreatedDateAsc(@Param("docId") Integer docId);

    @Query("SELECT i from PLMRequirementDocumentChildren i where i.document= :docId and i.requirementVersion.latest = true order by i.requirementVersion.master.createdDate asc")
    List<PLMRequirementDocumentChildren> getByDocumentAndLatestTrueOrderByCreatedDateAsc(@Param("docId") Integer docId);

    @Query("SELECT i from PLMRequirementDocumentChildren i where i.document= :docId and i.parent is null order by i.requirementVersion.master.createdDate asc")
    List<PLMRequirementDocumentChildren> getByDocumentAndParentIsNullOrderByCreatedDateAsc(@Param("docId") Integer docId);

    @Query("SELECT i from PLMRequirementDocumentChildren i where i.document= :docId and i.parent is null and i.requirementVersion.latest = true order by i.requirementVersion.master.createdDate asc")
    List<PLMRequirementDocumentChildren> getByDocumentAndParentIsNullAndVersionLatestOrderByCreatedDateAsc(@Param("docId") Integer docId);

    @Query("SELECT i from PLMRequirementDocumentChildren i where i.document= :docId and i.parent is null and i.requirementVersion.latest = false order by i.requirementVersion.master.createdDate asc")
    List<PLMRequirementDocumentChildren> getByDocumentAndParentIsNullAndVersionLatestFalseOrderByCreatedDateAsc(@Param("docId") Integer docId);

    @Query("SELECT count (i) from PLMRequirementDocumentChildren i where i.document= :docId and i.requirementVersion.latest = true")
    Integer getChildrenCountByDocumentAndParentIsNullAndVersionLatestTrue(@Param("docId") Integer docId);

    @Query("SELECT i from PLMRequirementDocumentChildren i where i.document= :docId and i.parent= :parent order by i.requirementVersion.master.createdDate asc")
    List<PLMRequirementDocumentChildren> findByDocumentAndParentOrderByCreatedDateAsc(@Param("docId") Integer docId, @Param("parent") Integer parent);

    @Query("SELECT i from PLMRequirementDocumentChildren i where i.document= :docId and i.parent= :parent and i.requirementVersion.latest = true order by i.requirementVersion.master.createdDate asc")
    List<PLMRequirementDocumentChildren> findByDocumentAndParentAndVersionLatestOrderByCreatedDateAsc(@Param("docId") Integer docId, @Param("parent") Integer parent);

    List<PLMRequirementDocumentChildren> findByDocumentAndParent(Integer document, Integer parent);

    PLMRequirementDocumentChildren findByDocumentAndRequirementVersion(Integer document, PLMRequirementVersion version);

    PLMRequirementDocumentChildren findByRequirementVersion(PLMRequirementVersion version);

    List<PLMRequirementDocumentChildren> findByParent(Integer parent);

    @Query("select i.id from PLMRequirementDocumentChildren i where i.requirementVersion.id in :ids order by i.id desc")
    List<Integer> getChildrenIdsByVersionIds(@Param("ids") List<Integer> ids);

    @Query("select distinct i.requirementVersion.assignedTo from PLMRequirementDocumentChildren i where i.document= :reqId")
    List<Integer> getAssignedTo(@Param("reqId") Integer reqId);

    @Query("select distinct i.requirementVersion.priority from PLMRequirementDocumentChildren i where i.document= :reqId")
    List<String> getUniqueRequirementPriorities(@Param("reqId") Integer reqId);

    @Query("select distinct i.requirementVersion.lifeCyclePhase from PLMRequirementDocumentChildren i where i.document= :reqId and i.requirementVersion.latest = true")
    List<PLMLifeCyclePhase> getLifeCyclePhases(@Param("reqId") Integer reqId);
}

