package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMProgramFile;
import com.cassinisys.plm.model.pm.ProgramTemplateFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 20-07-2022.
 */
@Repository
public interface ProgramTemplateFileRepository extends JpaRepository<ProgramTemplateFile, Integer> {
    List<ProgramTemplateFile> findByIdIn(Iterable<Integer> fileIds);

    ProgramTemplateFile findByTemplateAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    ProgramTemplateFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<ProgramTemplateFile> findByTemplateAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from ProgramTemplateFile i where i.template= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByTemplateAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from ProgramTemplateFile i where i.template= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType")
    List<Integer> getFileIdsByTemplateAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from ProgramTemplateFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType")
    List<Integer> getByParentFileAndLatestTrueAndFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    List<ProgramTemplateFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from ProgramTemplateFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    ProgramTemplateFile findByNameEqualsIgnoreCaseAndParentFileAndTemplateAndLatestTrue(String name, Integer parent, Integer qcr);

    ProgramTemplateFile findByTemplateAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<ProgramTemplateFile> findByTemplateAndFileNo(Integer id, String fileNo);

    List<ProgramTemplateFile> findByTemplateAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<ProgramTemplateFile> findByTemplateAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<ProgramTemplateFile> findByTemplateAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from ProgramTemplateFile i where i.template= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByTemplateAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<ProgramTemplateFile> findByTemplateAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from ProgramTemplateFile i where i.template= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByTemplateAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);

    ProgramTemplateFile findByNameEqualsIgnoreCaseAndTemplateAndLatestTrue(String name, Integer npr);

    ProgramTemplateFile findByNameEqualsIgnoreCaseAndTemplateAndLatestTrueAndParentFileIsNull(String name, Integer npr);

    List<ProgramTemplateFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<ProgramTemplateFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<ProgramTemplateFile> findByTemplateAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select i from ProgramTemplateFile i where i.parentFile in :parentIds and i.latest = true and i.fileType= :fileType")
    List<ProgramTemplateFile> getParentChildrenByFileType(@Param("parentIds") List<Integer> parentIds, @Param("fileType") String fileType);

    @Query("select i.fileNo from ProgramTemplateFile i where i.id in :ids")
    List<String> getFileNosByIds(@Param("ids") List<Integer> ids);

    @Query("select i from ProgramTemplateFile i where i.fileNo in :fileNos order by i.id asc")
    List<ProgramTemplateFile> findByFileNosOrderByIdAsc(@Param("fileNos") List<String> fileNos);

    @Query("select i from ProgramTemplateFile i where i.parentFile in :parentIds and i.latest = true")
    List<ProgramTemplateFile> getChildrenCountByParentFileAndLatestTrueByIds(@Param("parentIds") List<Integer> parentIds);

    @Query("select i from ProgramTemplateFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate desc")
    List<ProgramTemplateFile> findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);
}
