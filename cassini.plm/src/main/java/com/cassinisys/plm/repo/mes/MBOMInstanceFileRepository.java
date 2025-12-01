package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMBOMInstanceFile;
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
public interface MBOMInstanceFileRepository extends JpaRepository<MESMBOMInstanceFile, Integer>, QueryDslPredicateExecutor<MESMBOMInstanceFile> {
    List<MESMBOMInstanceFile> findByIdIn(Iterable<Integer> fileIds);

    MESMBOMInstanceFile findByMbomInstanceAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    MESMBOMInstanceFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<MESMBOMInstanceFile> findByMbomInstanceAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from MESMBOMInstanceFile i where i.mbomInstance= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByMbomInstanceAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<MESMBOMInstanceFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from MESMBOMInstanceFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    MESMBOMInstanceFile findByNameEqualsIgnoreCaseAndParentFileAndMbomInstanceAndLatestTrue(String name, Integer parent, Integer qcr);

    MESMBOMInstanceFile findByMbomInstanceAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<MESMBOMInstanceFile> findByMbomInstanceAndFileNo(Integer id, String fileNo);

    List<MESMBOMInstanceFile> findByMbomInstanceAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<MESMBOMInstanceFile> findByMbomInstanceAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<MESMBOMInstanceFile> findByMbomInstanceAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from MESMBOMInstanceFile i where i.mbomInstance= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByMbomInstanceAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<MESMBOMInstanceFile> findByMbomInstanceAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from MESMBOMInstanceFile i where i.mbomInstance= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByMbomInstanceAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);

    MESMBOMInstanceFile findByNameEqualsIgnoreCaseAndMbomInstanceAndLatestTrue(String name, Integer npr);

    List<MESMBOMInstanceFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<MESMBOMInstanceFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<MESMBOMInstanceFile> findByMbomInstanceAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(Integer revision, String fileNo);
}
