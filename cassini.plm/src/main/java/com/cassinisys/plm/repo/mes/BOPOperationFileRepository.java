package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPOperationFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 28-07-2022.
 */
@Repository
public interface BOPOperationFileRepository extends JpaRepository<MESBOPOperationFile, Integer>, QueryDslPredicateExecutor<MESBOPOperationFile> {
    List<MESBOPOperationFile> findByIdIn(Iterable<Integer> fileIds);

    MESBOPOperationFile findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    MESBOPOperationFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<MESBOPOperationFile> findByBopOperationAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from MESBOPOperationFile i where i.bopOperation= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByBopOperationAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<MESBOPOperationFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from MESBOPOperationFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    MESBOPOperationFile findByNameEqualsIgnoreCaseAndParentFileAndBopOperationAndLatestTrue(String name, Integer parent, Integer qcr);

    MESBOPOperationFile findByBopOperationAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<MESBOPOperationFile> findByBopOperationAndFileNo(Integer id, String fileNo);

    List<MESBOPOperationFile> findByBopOperationAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<MESBOPOperationFile> findByBopOperationAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<MESBOPOperationFile> findByBopOperationAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from MESBOPOperationFile i where i.bopOperation= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByBopOperationAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<MESBOPOperationFile> findByBopOperationAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from MESBOPOperationFile i where i.bopOperation= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByBopOperationAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);

    MESBOPOperationFile findByNameEqualsIgnoreCaseAndBopOperationAndLatestTrue(String name, Integer npr);

    List<MESBOPOperationFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<MESBOPOperationFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<MESBOPOperationFile> findByBopOperationAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(Integer revision, String fileNo);
}
