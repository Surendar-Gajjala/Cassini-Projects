package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMBOMFile;
import com.cassinisys.plm.model.mes.MESMBOMRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 17-05-2022.
 */
@Repository
public interface MBOMFileRepository extends JpaRepository<MESMBOMFile, Integer>, QueryDslPredicateExecutor<MESMBOMFile> {
    List<MESMBOMFile> findByIdIn(Iterable<Integer> fileIds);

    MESMBOMFile findByMbomRevisionAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    MESMBOMFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<MESMBOMFile> findByMbomRevisionAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from MESMBOMFile i where i.mbomRevision= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByMbomRevisionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<MESMBOMFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from MESMBOMFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    MESMBOMFile findByNameEqualsIgnoreCaseAndParentFileAndMbomRevisionAndLatestTrue(String name, Integer parent, Integer qcr);

    MESMBOMFile findByMbomRevisionAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<MESMBOMFile> findByMbomRevisionAndFileNo(Integer id, String fileNo);

    List<MESMBOMFile> findByMbomRevisionAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<MESMBOMFile> findByMbomRevisionAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<MESMBOMFile> findByMbomRevisionAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from MESMBOMFile i where i.mbomRevision= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByMbomRevisionAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<MESMBOMFile> findByMbomRevisionAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from MESMBOMFile i where i.mbomRevision= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByMbomRevisionAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);

    MESMBOMFile findByNameEqualsIgnoreCaseAndMbomRevisionAndLatestTrue(String name, Integer npr);

    List<MESMBOMFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<MESMBOMFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<MESMBOMFile> findByMbomRevisionAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(Integer revision, String fileNo);
}
