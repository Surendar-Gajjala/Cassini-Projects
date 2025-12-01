package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPInstanceOperationFile;
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
public interface BOPInstanceOperationFileRepository extends JpaRepository<MESBOPInstanceOperationFile, Integer>, QueryDslPredicateExecutor<MESBOPInstanceOperationFile> {
    List<MESBOPInstanceOperationFile> findByIdIn(Iterable<Integer> fileIds);

    MESBOPInstanceOperationFile findByBopOperationAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    MESBOPInstanceOperationFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<MESBOPInstanceOperationFile> findByBopOperationAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from MESBOPInstanceOperationFile i where i.bopOperation= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByBopOperationAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<MESBOPInstanceOperationFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from MESBOPInstanceOperationFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    MESBOPInstanceOperationFile findByNameEqualsIgnoreCaseAndParentFileAndBopOperationAndLatestTrue(String name, Integer parent, Integer qcr);

    MESBOPInstanceOperationFile findByBopOperationAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<MESBOPInstanceOperationFile> findByBopOperationAndFileNo(Integer id, String fileNo);

    List<MESBOPInstanceOperationFile> findByBopOperationAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<MESBOPInstanceOperationFile> findByBopOperationAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<MESBOPInstanceOperationFile> findByBopOperationAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from MESBOPInstanceOperationFile i where i.bopOperation= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByBopOperationAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<MESBOPInstanceOperationFile> findByBopOperationAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from MESBOPInstanceOperationFile i where i.bopOperation= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByBopOperationAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);

    MESBOPInstanceOperationFile findByNameEqualsIgnoreCaseAndBopOperationAndLatestTrue(String name, Integer npr);

    List<MESBOPInstanceOperationFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<MESBOPInstanceOperationFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<MESBOPInstanceOperationFile> findByBopOperationAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(Integer revision, String fileNo);
}
