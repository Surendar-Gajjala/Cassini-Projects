package com.cassinisys.plm.repo.pm;
/**
 * The Class is for ProjectWbsRepository
 **/

import com.cassinisys.plm.model.plm.PLMDocument;
import com.cassinisys.plm.model.plm.PLMFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PLMDocumentRepository extends JpaRepository<PLMDocument, Integer>, QueryDslPredicateExecutor<PLMDocument> {

    List<PLMDocument> findByIdIn(Iterable<Integer> ids);

    List<PLMDocument> findByFileTypeAndParentFileIsNullOrderByModifiedDateDesc(String fileType);

    List<PLMDocument> findByLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc();

    PLMDocument findByNameEqualsIgnoreCaseAndFileTypeAndParentFileIsNull(String name, String fileType);

    PLMDocument findByNameEqualsIgnoreCaseAndFileTypeAndParentFile(String name, String fileType, Integer parent);

    List<PLMDocument> findByParentFileAndFileTypeOrderByModifiedDateDesc(Integer parent, String fileType);

    @Query("select i.id from PLMDocument i where i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByFileTypeOrderByModifiedDateDesc(@Param("fileType") String fileType);

    @Query("select i.id from PLMDocument i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByParentAndFileTypeOrderByModifiedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);

    PLMDocument findByNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(String name);

    PLMDocument findByNameEqualsIgnoreCaseAndParentFileAndLatestTrue(String name, Integer parent);

    PLMDocument findByFileNoAndLatestTrue(String fileNo);

    List<PLMDocument> findByFileNo(String fileNo);

    List<PLMDocument> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    List<PLMDocument> findByParentFileAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer parent, String fileType);

    List<PLMDocument> findByFileNoAndLatestFalseOrderByCreatedDateDesc(String fileNo);

    @Query("select i.id from PLMDocument i where i.fileNo= :fileNo and i.latest = false")
    List<Integer> getFileIdsByFileNoAndLatestFalse(@Param("fileNo") String fileNo);

    List<PLMDocument> findByLatestTrueAndParentFileIsNullOrderByModifiedDateDesc();

    List<PLMDocument> findByNameContainingIgnoreCaseAndLatestTrue(String name);

    @Query("select i.id from PLMDocument i where i.latest = true and i.fileType = 'FILE' and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByNameContainingIgnoreCaseAndLatestTrue(@Param("name") String name);

    Page<PLMDocument> findByNameContainingIgnoreCaseAndLatestTrueAndFileType(String name, String fileType, Pageable pageable);

    @Query("select count (i) from PLMDocument i where i.fileType = 'FILE' and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    Integer getDocumentCountByNameContainingIgnoreCaseAndLatestTrueAndFileType(@Param("name") String name);

    @Query("select i.id from PLMDocument i where i.parentFile= :parent and i.fileType = 'FILE' and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByParentAndNameContainingIgnoreCaseAndLatestTrue(@Param("parent") Integer parent, @Param("name") String name);

    List<PLMDocument> findByFileTypeAndLatestTrueOrderByModifiedDateDesc(String fileType);

    @Query("select count (i) from PLMDocument i where i.fileType= :fileType and i.latest = true")
    Integer getCountByFileTypeAndLatestTrueOrderByModifiedDateDesc(@Param("fileType") String fileType);

    List<PLMDocument> findByFileTypeAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(String fileType);

    @Query("select count (i) from PLMDocument i where i.parentFile= :parent and i.fileType= :fileType and i.latest = true")
    Integer getChildrenCountByParentAndFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select count (i) from PLMDocument i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType and i.id not in :ids")
    Integer getChildrenCountByParentAndFileTypeAndIdsNotIn(@Param("parent") Integer parent, @Param("fileType") String fileType, @Param("ids") Iterable<Integer> ids);

    @Query("select count (i) from PLMDocument i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParent(@Param("parent") Integer parent);

    @Query("select count (i) from PLMDocument i where i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByFileTypeAndLatestTrue(@Param("fileType") String fileType);


    List<PLMDocument> findByIdInAndFileType(Iterable<Integer> ids, String fileType);

    @Query("select i from PLMDocument i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate desc")
    List<PLMDocument> findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select i from PLMDocument i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<PLMDocument> getFilesByParentAndFileTypeOrderByModifiedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select i from PLMDocument i where i.fileNo= :fileNo order by i.id asc")
    List<PLMDocument> findByFileNoOrderByIdAsc(@Param("fileNo") String fileNo);


}
