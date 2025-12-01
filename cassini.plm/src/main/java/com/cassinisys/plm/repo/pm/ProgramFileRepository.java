package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMActivityFile;
import com.cassinisys.plm.model.pm.PLMProgramFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 02-06-2022.
 */
@Repository
public interface ProgramFileRepository extends JpaRepository<PLMProgramFile, Integer> {
    List<PLMProgramFile> findByIdIn(Iterable<Integer> fileIds);

    PLMProgramFile findByProgramAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    PLMProgramFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMProgramFile> findByProgramAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    List<PLMProgramFile> findByProgramAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select i.id from PLMProgramFile i where i.program= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByProgramAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMProgramFile i where i.program= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType")
    List<Integer> getFileIdsByProgramAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMProgramFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType")
    List<Integer> getByParentFileAndLatestTrueAndFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    List<PLMProgramFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from PLMProgramFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    PLMProgramFile findByNameEqualsIgnoreCaseAndParentFileAndProgramAndLatestTrue(String name, Integer parent, Integer qcr);

    PLMProgramFile findByProgramAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<PLMProgramFile> findByProgramAndFileNo(Integer id, String fileNo);

    List<PLMProgramFile> findByProgramAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<PLMProgramFile> findByProgramAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<PLMProgramFile> findByProgramAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PLMProgramFile i where i.program= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByProgramAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<PLMProgramFile> findByProgramAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from PLMProgramFile i where i.program= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByProgramAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);

    PLMProgramFile findByNameEqualsIgnoreCaseAndProgramAndLatestTrue(String name, Integer npr);

    List<PLMProgramFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<PLMProgramFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMProgramFile i where i.program in :programIds")
    Integer getProgramFileCount(@Param("programIds") List<Integer> programIds);

    @Query("select i from PLMProgramFile i where i.parentFile in :parentIds and i.latest = true and i.fileType= :fileType")
    List<PLMProgramFile> getParentChildrenByFileType(@Param("parentIds") List<Integer> parentIds, @Param("fileType") String fileType);

    @Query("select i.fileNo from PLMProgramFile i where i.id in :ids")
    List<String> getFileNosByIds(@Param("ids") List<Integer> ids);

    @Query("select i from PLMProgramFile i where i.fileNo in :fileNos order by i.id asc")
    List<PLMProgramFile> findByFileNosOrderByIdAsc(@Param("fileNos") List<String> fileNos);

    @Query("select i from PLMProgramFile i where i.parentFile in :parentIds and i.latest = true")
    List<PLMProgramFile> getChildrenCountByParentFileAndLatestTrueByIds(@Param("parentIds") List<Integer> parentIds);

    @Query("select i from PLMProgramFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate desc")
    List<PLMProgramFile> findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);

}
