package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementDocumentFile;
import com.cassinisys.plm.model.req.PLMRequirementDocumentRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementDocumentFileRepository extends JpaRepository<PLMRequirementDocumentFile, Integer> {

    List<PLMRequirementDocumentFile> findByIdIn(Iterable<Integer> ids);

    List<PLMRequirementDocumentFile> findByDocumentRevision(PLMRequirementDocumentRevision revision);

    List<PLMRequirementDocumentFile> findByDocumentRevisionAndName(PLMRequirementDocumentRevision revision, String name);

    List<PLMRequirementDocumentFile> findByDocumentRevisionAndLatestTrueOrderByModifiedDateDesc(PLMRequirementDocumentRevision revision);

    PLMRequirementDocumentFile findByDocumentRevisionAndNameAndLatestTrue(PLMRequirementDocumentRevision revision, String name);

    List<PLMRequirementDocumentFile> findAllByDocumentRevisionAndNameOrderByCreatedDateDesc(PLMRequirementDocumentRevision revision, String name);

    List<PLMRequirementDocumentFile> findByDocumentRevisionAndNameContainingIgnoreCase(PLMRequirementDocumentRevision revision, String name);

    List<PLMRequirementDocumentFile> findByDocumentRevisionAndNameAndLatestFalseOrderByCreatedDateDesc(PLMRequirementDocumentRevision revision, String name);

    List<PLMRequirementDocumentFile> findByDocumentRevisionAndNameContainingIgnoreCaseAndLatestTrue(PLMRequirementDocumentRevision revision, String name);

    List<PLMRequirementDocumentFile> findByDocumentRevisionAndFileNoAndLatestFalseOrderByCreatedDateDesc(PLMRequirementDocumentRevision revision, String fileNo);

    List<PLMRequirementDocumentFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<PLMRequirementDocumentFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMRequirementDocumentFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PLMRequirementDocumentFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMRequirementDocumentFile> findByDocumentRevisionAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(PLMRequirementDocumentRevision revision);

    PLMRequirementDocumentFile findByDocumentRevisionAndNameAndParentFileIsNullAndLatestTrue(PLMRequirementDocumentRevision revision, String name);

    PLMRequirementDocumentFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMRequirementDocumentFile> findByDocumentRevisionAndFileTypeAndLatestTrueOrderByModifiedDateDesc(PLMRequirementDocumentRevision revision, String fileType);

    PLMRequirementDocumentFile findByDocumentRevisionAndFileNoAndLatestTrue(PLMRequirementDocumentRevision revision, String fileNo);

    PLMRequirementDocumentFile findByNameEqualsIgnoreCaseAndParentFileAndDocumentRevisionAndLatestTrue(String name, Integer parent, PLMRequirementDocumentRevision revision);

    PLMRequirementDocumentFile findByNameEqualsIgnoreCaseAndDocumentRevisionAndLatestTrue(String name, PLMRequirementDocumentRevision revision);

    List<PLMRequirementDocumentFile> findByDocumentRevisionAndFileNo(PLMRequirementDocumentRevision revision, String fileNo);

    @Query("select i.id from PLMRequirementDocumentFile i where i.documentRevision.id= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByDocumentRevisionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMRequirementDocumentFile i where i.documentRevision.id= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByDocumentRevisionAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);
}
