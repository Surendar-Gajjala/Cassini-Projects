package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirement;
import com.cassinisys.plm.model.req.PLMRequirementDocumentRevision;
import com.cassinisys.plm.model.req.PLMRequirementVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementVersionRepository extends JpaRepository<PLMRequirementVersion, Integer>, QueryDslPredicateExecutor<PLMRequirementVersion> {

    @Query("SELECT i from PLMRequirementVersion i where i.requirementDocumentRevision.id= :docId and i.parent is null and i.latest = true order by i.createdDate asc")
    List<PLMRequirementVersion> getVersionByDocumentAndParentIsNullAndLatestTrue(@Param("docId") Integer docId);

    @Query("SELECT i from PLMRequirementVersion i where i.parent= :parentId and i.latest = true")
    List<PLMRequirementVersion> getVersionByParent(@Param("parentId") Integer parentId);

    List<PLMRequirementVersion> getByMasterOrderByCreatedDateDesc(PLMRequirement requirement);

    List<PLMRequirementVersion> getByMasterAndLatestTrueOrderByCreatedDateDesc(PLMRequirement requirement);


    List<PLMRequirementVersion> findByRequirementDocumentRevisionAndParentIsNull(PLMRequirementDocumentRevision revision);

    List<PLMRequirementVersion> findByRequirementDocumentRevisionAndParentIsNullOrderByCreatedDateAsc(PLMRequirementDocumentRevision revision);

    List<PLMRequirementVersion> findByRequirementDocumentRevisionAndParentIsNullAndLatestTrueOrderByCreatedDateAsc(PLMRequirementDocumentRevision revision);

    List<PLMRequirementVersion> findByRequirementDocumentRevision(PLMRequirementDocumentRevision revision);

    List<PLMRequirementVersion> findByRequirementDocumentRevisionAndLatestTrue(PLMRequirementDocumentRevision revision);

    List<PLMRequirementVersion> findByParent(Integer parent);

    @Query("select count (i) from PLMRequirementVersion i where i.latest = true and ((LOWER(CAST(i.master.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.master.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.master.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.master.type.name as text))) LIKE '%' || :searchText || '%')")
    Integer getRequirementsCountBySearchQuery(@Param("searchText") String searchText);

    @Query("select i.id from PLMRequirementVersion i where i.master.id= :reqId order by i.id asc")
     List<Integer> getVersionIdsByReqId(@Param("reqId") Integer reqId);

}
