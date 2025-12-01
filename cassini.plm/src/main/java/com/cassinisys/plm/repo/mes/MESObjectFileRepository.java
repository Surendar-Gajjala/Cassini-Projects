package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESObjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface MESObjectFileRepository extends JpaRepository<MESObjectFile, Integer> {

    MESObjectFile findByObjectAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    MESObjectFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<MESObjectFile> findByObjectAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from MESObjectFile i where i.object= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<MESObjectFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from MESObjectFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    MESObjectFile findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(String name, Integer parent, Integer qcr);

    MESObjectFile findByObjectAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<MESObjectFile> findByObjectAndFileNo(Integer id, String fileNo);

    List<MESObjectFile> findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<MESObjectFile> findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<MESObjectFile> findByObjectAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from MESObjectFile i where i.object= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByObjectAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<MESObjectFile> findByObjectAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from MESObjectFile i where i.object= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByObjectAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);
}
