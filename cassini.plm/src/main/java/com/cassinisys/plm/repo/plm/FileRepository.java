package com.cassinisys.plm.repo.plm;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.plm.PLMFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Repository
public interface FileRepository extends JpaRepository<PLMFile, Integer> {

    Page<PLMFile> findByLatestTrueAndNameContainsIgnoreCaseAndFileTypeAndObjectTypeNot(String name, String fileType, ObjectType objectType, Pageable pageable);

    @Query("select count (i) from PLMFile i where i.latest = true and i.fileType = 'FILE' and i.objectType!= :objectType and (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getFilesCountByLatestTrueAndNameContainsIgnoreCaseAndObjectTypeNot(@Param("searchText") String searchText, @Param("objectType") ObjectType objectType);

    PLMFile findById(Integer id);

    List<PLMFile> findByIdIn(Iterable<Integer> ids);

    @Query("select i from PLMFile i where i.id in :ids")
    List<PLMFile> getFilesByIds(@Param("ids") List<Integer> ids);

    List<PLMFile> findByIdInOrderByIdAsc(Iterable<Integer> ids);

    List<PLMFile> findByIdInOrderByNameAsc(Iterable<Integer> ids);

    @Query("select count (i) from PLMFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    @Query("select i from PLMFile i where i.parentFile in :parentIds and i.latest = true")
    List<PLMFile> getChildrenCountByParentFileAndLatestTrueByIds(@Param("parentIds") List<Integer> parentIds);

    @Query("select i.id from PLMFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate desc")
    List<Integer> getByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select i.id from PLMFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.name asc")
    List<Integer> getByParentFileAndLatestTrueAndFileTypeOrderByNameAsc(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select i from PLMFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate desc")
    List<PLMFile> findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select count (i) from PLMFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType")
    Integer getChildCountByFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select i from PLMFile i where i.parentFile in :parentIds and i.latest = true and i.fileType= :fileType")
    List<PLMFile> getParentChildrenByFileType(@Param("parentIds") List<Integer> parentIds, @Param("fileType") String fileType);

    @Query("select i from PLMFile i where i.fileNo= :fileNo order by i.id asc")
    List<PLMFile> findByFileNoOrderByIdAsc(@Param("fileNo") String fileNo);

    @Query("select i from PLMFile i where i.fileNo in :fileNos order by i.id asc")
    List<PLMFile> findByFileNosOrderByIdAsc(@Param("fileNos") List<String> fileNos);

    @Query("select i.fileNo from PLMFile i where i.id in :ids")
    List<String> getFileNosByIds(@Param("ids") List<Integer> ids);

    @Query("select distinct i.createdBy from PLMFile i where i.id in :ids")
    List<Integer> getCreatedByIds(@Param("ids") List<Integer> ids);

    @Query("select distinct i.modifiedBy from PLMFile i where i.id in :ids")
    List<Integer> getModifiedByIds(@Param("ids") List<Integer> ids);

    List<PLMFile> findByIdInAndFileType(Iterable<Integer> ids, String fileType);

    List<PLMFile> findByIdInAndFileTypeAndParentFile(Iterable<Integer> ids, String fileType, Integer parentFile);

    List<PLMFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);
}
