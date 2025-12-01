package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */
@Repository
public interface ProjectFileRepository extends JpaRepository<PLMProjectFile, Integer> {

    PLMProjectFile findById(Integer id);

    List<PLMProjectFile> findByProject(Integer project);

    List<PLMProjectFile> findByIdIn(Iterable<Integer> ids);

    PLMProjectFile findByProjectAndNameAndLatestTrue(Integer projectId, String fileName);

    List<PLMProjectFile> findByProjectAndLatestTrue(Integer projectId);

    PLMProjectFile findByProjectAndIdAndLatestTrue(Integer projectId, Integer fileName);

    PLMProjectFile findByProjectAndId(Integer projectId, Integer fileName);

    List<PLMProjectFile> findByProjectAndName(Integer projectId, String fileName);

    List<PLMProjectFile> findByProjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer project, String fileNo);

    List<PLMProjectFile> findByProjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(Integer project, String fileType);

    List<PLMProjectFile> findByProjectAndNameAndLatestFalseOrderByCreatedDateDesc(Integer project, String fileName);

    List<PLMProjectFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<PLMProjectFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMProjectFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PLMProjectFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMProjectFile> findByProjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer project);

    PLMProjectFile findByProjectAndNameAndParentFileIsNullAndLatestTrue(Integer project, String name);

    PLMProjectFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMProjectFile> findByProjectAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer project, String fileType);

    PLMProjectFile findByNameEqualsIgnoreCaseAndParentFileAndProjectAndLatestTrue(String name, Integer parent, Integer project);

    PLMProjectFile findByNameEqualsIgnoreCaseAndProjectAndLatestTrue(String name, Integer project);

    PLMProjectFile findByProjectAndFileNoAndLatestTrue(Integer projectId, String fileNo);

    List<PLMProjectFile> findByProjectAndFileNo(Integer projectId, String fileNo);

    List<PLMProjectFile> findByProjectAndNameContainingIgnoreCaseAndLatestTrue(Integer project, String name);

    @Query("select i.id from PLMProjectFile i where i.project= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByProjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMProjectFile i where i.project= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType")
    List<Integer> getFileIdsByProjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMProjectFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType")
    List<Integer> getByParentFileAndLatestTrueAndFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select i from PLMProjectFile i where i.project= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType")
    List<PLMProjectFile> getFilesByProjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i from PLMProjectFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType")
    List<PLMProjectFile> getFilesByParentAndLatestTrueAndFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select i.id from PLMProjectFile i where i.project= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByProjectAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    @Query("select count (i) from PLMProjectFile i where i.project in :projectIds")
    Integer getProjectFileCount(@Param("projectIds") List<Integer> projectIds);

    @Query("select i from PLMProjectFile i where i.parentFile in :parentIds and i.latest = true and i.fileType= :fileType")
    List<PLMProjectFile> getParentChildrenByFileType(@Param("parentIds") List<Integer> parentIds, @Param("fileType") String fileType);

    @Query("select i.fileNo from PLMProjectFile i where i.id in :ids")
    List<String> getFileNosByIds(@Param("ids") List<Integer> ids);

    @Query("select i from PLMProjectFile i where i.fileNo in :fileNos order by i.id asc")
    List<PLMProjectFile> findByFileNosOrderByIdAsc(@Param("fileNos") List<String> fileNos);

    @Query("select i from PLMProjectFile i where i.parentFile in :parentIds and i.latest = true")
    List<PLMProjectFile> getChildrenCountByParentFileAndLatestTrueByIds(@Param("parentIds") List<Integer> parentIds);

    @Query("select i from PLMProjectFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate desc")
    List<PLMProjectFile> findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);

}
