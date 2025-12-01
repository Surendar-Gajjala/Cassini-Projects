package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMActivityFile;
import com.cassinisys.plm.model.pm.PLMTaskFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */
@Repository
public interface TaskFileRepository extends JpaRepository<PLMTaskFile, Integer> {

    PLMTaskFile findById(Integer id);

    List<PLMTaskFile> findByTask(Integer task);

    List<PLMTaskFile> findByIdIn(Iterable<Integer> ids);

    PLMTaskFile findByTaskAndNameAndLatestTrue(Integer taskId, String name);

    List<PLMTaskFile> findByTaskAndName(Integer activityId, String fileName);

    PLMTaskFile findByTaskAndIdAndLatestTrue(Integer activityId, Integer fileId);

    PLMTaskFile findByTaskAndId(Integer taskId, Integer fileId);

    List<PLMTaskFile> findByTaskAndLatestTrue(Integer taskId);

    List<PLMTaskFile> findByTaskAndNameAndLatestFalseOrderByCreatedDateDesc(Integer task, String name);

    List<PLMTaskFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<PLMTaskFile> findByTaskAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer task, String fileNo);

    List<PLMTaskFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMTaskFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PLMTaskFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMTaskFile> findByTaskAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer task);

    PLMTaskFile findByTaskAndNameAndParentFileIsNullAndLatestTrue(Integer task, String name);

    PLMTaskFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMTaskFile> findByTaskAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer task, String fileType);

    PLMTaskFile findByTaskAndFileNoAndLatestTrue(Integer task, String fileNo);

    PLMTaskFile findByNameEqualsIgnoreCaseAndParentFileAndTaskAndLatestTrue(String name, Integer parent, Integer task);

    PLMTaskFile findByNameEqualsIgnoreCaseAndTaskAndLatestTrue(String name, Integer task);

    List<PLMTaskFile> findByTaskAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PLMTaskFile i where i.task= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMTaskFile i where i.task= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType")
    List<Integer> getFileIdsByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMTaskFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType")
    List<Integer> getByParentFileAndLatestTrueAndFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select i.id from PLMTaskFile i where i.task= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByTaskAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<PLMTaskFile> findByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from PLMTaskFile i where i.task in :taskIds")
    Integer getTasksFileCount(@Param("taskIds") List<Integer> taskIds);

    @Query("select count (i) from PLMTaskFile i where i.task= :taskId and i.fileType= :fileType and i.latest = true")
    Integer getTaskFileCount(@Param("taskId") Integer taskId, @Param("fileType") String fileType);

    @Query("select i from PLMTaskFile i where i.parentFile in :parentIds and i.latest = true and i.fileType= :fileType")
    List<PLMTaskFile> getParentChildrenByFileType(@Param("parentIds") List<Integer> parentIds, @Param("fileType") String fileType);

    @Query("select i.fileNo from PLMTaskFile i where i.id in :ids")
    List<String> getFileNosByIds(@Param("ids") List<Integer> ids);

    @Query("select i from PLMTaskFile i where i.fileNo in :fileNos order by i.id asc")
    List<PLMTaskFile> findByFileNosOrderByIdAsc(@Param("fileNos") List<String> fileNos);

    @Query("select i from PLMTaskFile i where i.parentFile in :parentIds and i.latest = true")
    List<PLMTaskFile> getChildrenCountByParentFileAndLatestTrueByIds(@Param("parentIds") List<Integer> parentIds);

    @Query("select i from PLMTaskFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate desc")
    List<PLMTaskFile> findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);
}
