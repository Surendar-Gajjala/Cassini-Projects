package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.ProgramTemplateFile;
import com.cassinisys.plm.model.pm.ProjectTemplateActivityFile;
import com.cassinisys.plm.model.pm.ProjectTemplateFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 20-07-2022.
 */
@Repository
public interface ProjectTemplateActivityFileRepository extends JpaRepository<ProjectTemplateActivityFile, Integer> {
    List<ProjectTemplateActivityFile> findByIdIn(Iterable<Integer> fileIds);

    ProjectTemplateActivityFile findByActivityAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    ProjectTemplateActivityFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<ProjectTemplateActivityFile> findByActivityAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from ProjectTemplateActivityFile i where i.activity= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from ProjectTemplateActivityFile i where i.activity= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType")
    List<Integer> getFileIdsByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from ProjectTemplateActivityFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType")
    List<Integer> getByParentFileAndLatestTrueAndFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    List<ProjectTemplateActivityFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from ProjectTemplateActivityFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    ProjectTemplateActivityFile findByNameEqualsIgnoreCaseAndParentFileAndActivityAndLatestTrue(String name, Integer parent, Integer qcr);

    ProjectTemplateActivityFile findByActivityAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<ProjectTemplateActivityFile> findByActivityAndFileNo(Integer id, String fileNo);

    List<ProjectTemplateActivityFile> findByActivityAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<ProjectTemplateActivityFile> findByActivityAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<ProjectTemplateActivityFile> findByActivityAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from ProjectTemplateActivityFile i where i.activity= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByActivityAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<ProjectTemplateActivityFile> findByActivityAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from ProjectTemplateActivityFile i where i.activity= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByActivityAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);

    ProjectTemplateActivityFile findByNameEqualsIgnoreCaseAndActivityAndLatestTrue(String name, Integer npr);

    List<ProjectTemplateActivityFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<ProjectTemplateActivityFile> findByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(Integer id, String fileType);

    List<ProjectTemplateActivityFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    @Query("select i from ProjectTemplateActivityFile i where i.parentFile in :parentIds and i.latest = true and i.fileType= :fileType")
    List<ProjectTemplateActivityFile> getParentChildrenByFileType(@Param("parentIds") List<Integer> parentIds, @Param("fileType") String fileType);

    @Query("select i.fileNo from ProjectTemplateActivityFile i where i.id in :ids")
    List<String> getFileNosByIds(@Param("ids") List<Integer> ids);

    @Query("select i from ProjectTemplateActivityFile i where i.fileNo in :fileNos order by i.id asc")
    List<ProjectTemplateActivityFile> findByFileNosOrderByIdAsc(@Param("fileNos") List<String> fileNos);

    @Query("select i from ProjectTemplateActivityFile i where i.parentFile in :parentIds and i.latest = true")
    List<ProjectTemplateActivityFile> getChildrenCountByParentFileAndLatestTrueByIds(@Param("parentIds") List<Integer> parentIds);

    @Query("select i from ProjectTemplateActivityFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate desc")
    List<ProjectTemplateActivityFile> findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);

}
