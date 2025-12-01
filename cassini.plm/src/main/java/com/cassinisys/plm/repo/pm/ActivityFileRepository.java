package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pm.PLMActivityFile;
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
public interface ActivityFileRepository extends JpaRepository<PLMActivityFile, Integer> {

    PLMActivityFile findById(Integer id);

    List<PLMActivityFile> findByActivity(Integer activity);

    List<PLMActivityFile> findByIdIn(Iterable<Integer> ids);

    PLMActivityFile findByActivityAndNameAndLatestTrue(Integer activity, String name);

    PLMActivityFile findByActivityAndIdAndLatestTrue(Integer activityId, Integer fileId);

    PLMActivityFile findByActivityAndId(Integer activityId, Integer fileId);

    List<PLMActivityFile> findByActivityAndName(Integer activityId, String fileName);

    List<PLMActivityFile> findByActivityAndLatestTrue(Integer activityId);

    List<PLMActivityFile> findByActivityAndNameAndLatestFalseOrderByCreatedDateDesc(Integer activity, String name);

    List<PLMActivityFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<PLMActivityFile> findByActivityAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer activity, String fileNo);

    List<PLMActivityFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMActivityFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PLMActivityFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMActivityFile> findByActivityAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer activity);

    @Query("select i.id from PLMActivityFile i where i.activity= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMActivityFile i where i.activity= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType")
    List<Integer> getFileIdsByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMActivityFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType")
    List<Integer> getByParentFileAndLatestTrueAndFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select i.id from PLMActivityFile i where i.activity= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByActivityAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    PLMActivityFile findByActivityAndNameAndParentFileIsNullAndLatestTrue(Integer activity, String name);

    PLMActivityFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMActivityFile> findByActivityAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer activity, String fileType);

    PLMActivityFile findByActivityAndFileNoAndLatestTrue(Integer activityId, String fileNo);

    PLMActivityFile findByNameEqualsIgnoreCaseAndParentFileAndActivityAndLatestTrue(String name, Integer parent, Integer activity);

    PLMActivityFile findByNameEqualsIgnoreCaseAndActivityAndLatestTrue(String name, Integer activity);

    List<PLMActivityFile> findByActivityAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select count (i) from PLMActivityFile i where i.activity in :activityIds")
    Integer getActivitiesFileCount(@Param("activityIds") List<Integer> activityIds);

    @Query("select count (i) from PLMActivityFile i where i.activity= :activityId and i.fileType= :fileType and i.latest = true")
    Integer getActivityFileCount(@Param("activityId") Integer activityId, @Param("fileType") String fileType);

    @Query("select i from PLMActivityFile i where i.parentFile in :parentIds and i.latest = true and i.fileType= :fileType")
    List<PLMActivityFile> getParentChildrenByFileType(@Param("parentIds") List<Integer> parentIds, @Param("fileType") String fileType);

    @Query("select i.fileNo from PLMActivityFile i where i.id in :ids")
    List<String> getFileNosByIds(@Param("ids") List<Integer> ids);

    @Query("select i from PLMActivityFile i where i.fileNo in :fileNos order by i.id asc")
    List<PLMActivityFile> findByFileNosOrderByIdAsc(@Param("fileNos") List<String> fileNos);

    @Query("select i from PLMActivityFile i where i.parentFile in :parentIds and i.latest = true")
    List<PLMActivityFile> getChildrenCountByParentFileAndLatestTrueByIds(@Param("parentIds") List<Integer> parentIds);

    List<PLMActivityFile> findByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(Integer activity, String fileType);

    @Query("select i from PLMActivityFile i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate desc")
    List<PLMActivityFile> findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);
}
