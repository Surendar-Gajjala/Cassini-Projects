package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCObjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface PGCObjectFileRepository extends JpaRepository<PGCObjectFile, Integer> {

    PGCObjectFile findByObjectAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    PGCObjectFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PGCObjectFile> findByObjectAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from PGCObjectFile i where i.object= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<PGCObjectFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from PGCObjectFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    PGCObjectFile findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(String name, Integer parent, Integer qcr);

    PGCObjectFile findByObjectAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<PGCObjectFile> findByObjectAndFileNo(Integer id, String fileNo);

    List<PGCObjectFile> findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<PGCObjectFile> findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<PGCObjectFile> findByObjectAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PGCObjectFile i where i.object= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByObjectAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<PGCObjectFile> findByObjectAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from PGCObjectFile i where i.object= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByObjectAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);
}
