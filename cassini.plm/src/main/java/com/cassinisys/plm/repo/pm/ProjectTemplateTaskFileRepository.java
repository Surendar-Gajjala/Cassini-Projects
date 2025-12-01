package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.ProjectTemplateTaskFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 20-07-2022.
 */
@Repository
public interface ProjectTemplateTaskFileRepository extends JpaRepository<ProjectTemplateTaskFile, Integer> {
    List<ProjectTemplateTaskFile> findByIdIn(Iterable<Integer> fileIds);

    ProjectTemplateTaskFile findByTaskAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    ProjectTemplateTaskFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<ProjectTemplateTaskFile> findByTaskAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from ProjectTemplateTaskFile i where i.task= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from ProjectTemplateTaskFile i where i.task= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType")
    List<Integer> getFileIdsByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from ProjectTemplateTaskFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType")
    List<Integer> getByParentFileAndLatestTrueAndFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    List<ProjectTemplateTaskFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from ProjectTemplateTaskFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    ProjectTemplateTaskFile findByNameEqualsIgnoreCaseAndParentFileAndTaskAndLatestTrue(String name, Integer parent, Integer qcr);

    ProjectTemplateTaskFile findByTaskAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<ProjectTemplateTaskFile> findByTaskAndFileNo(Integer id, String fileNo);

    List<ProjectTemplateTaskFile> findByTaskAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<ProjectTemplateTaskFile> findByTaskAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<ProjectTemplateTaskFile> findByTaskAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from ProjectTemplateTaskFile i where i.task= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByTaskAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<ProjectTemplateTaskFile> findByTaskAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from ProjectTemplateTaskFile i where i.task= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByTaskAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);

    ProjectTemplateTaskFile findByNameEqualsIgnoreCaseAndTaskAndLatestTrue(String name, Integer npr);

    List<ProjectTemplateTaskFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<ProjectTemplateTaskFile> findByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(Integer id, String fileType);

    List<ProjectTemplateTaskFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    @Query("select i from ProjectTemplateTaskFile i where i.parentFile in :parentIds and i.latest = true and i.fileType= :fileType")
    List<ProjectTemplateTaskFile> getParentChildrenByFileType(@Param("parentIds") List<Integer> parentIds, @Param("fileType") String fileType);

    @Query("select i.fileNo from ProjectTemplateTaskFile i where i.id in :ids")
    List<String> getFileNosByIds(@Param("ids") List<Integer> ids);

    @Query("select i from ProjectTemplateTaskFile i where i.fileNo in :fileNos order by i.id asc")
    List<ProjectTemplateTaskFile> findByFileNosOrderByIdAsc(@Param("fileNos") List<String> fileNos);

    @Query("select i from ProjectTemplateTaskFile i where i.parentFile in :parentIds and i.latest = true")
    List<ProjectTemplateTaskFile> getChildrenCountByParentFileAndLatestTrueByIds(@Param("parentIds") List<Integer> parentIds);

    @Query("select i from ProjectTemplateTaskFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate desc")
    List<ProjectTemplateTaskFile> findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);
}
