package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPFile;
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
public interface BOPFileRepository extends JpaRepository<MESBOPFile, Integer>, QueryDslPredicateExecutor<MESBOPFile> {
    List<MESBOPFile> findByIdIn(Iterable<Integer> fileIds);

    MESBOPFile findByBopAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    MESBOPFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<MESBOPFile> findByBopAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from MESBOPFile i where i.bop= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByBopAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<MESBOPFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from MESBOPFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    MESBOPFile findByNameEqualsIgnoreCaseAndParentFileAndBopAndLatestTrue(String name, Integer parent, Integer qcr);

    MESBOPFile findByBopAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<MESBOPFile> findByBopAndFileNo(Integer id, String fileNo);

    List<MESBOPFile> findByBopAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<MESBOPFile> findByBopAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<MESBOPFile> findByBopAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from MESBOPFile i where i.bop= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByBopAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<MESBOPFile> findByBopAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from MESBOPFile i where i.bop= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByBopAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);

    MESBOPFile findByNameEqualsIgnoreCaseAndBopAndLatestTrue(String name, Integer npr);

    List<MESBOPFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<MESBOPFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<MESBOPFile> findByBopAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(Integer revision, String fileNo);
}
