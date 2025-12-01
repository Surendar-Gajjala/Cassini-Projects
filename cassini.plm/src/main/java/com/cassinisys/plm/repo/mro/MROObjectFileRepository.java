package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROObjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROObjectFileRepository extends JpaRepository<MROObjectFile, Integer> {
    MROObjectFile findByObjectAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    MROObjectFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<MROObjectFile> findByObjectAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from MROObjectFile i where i.object= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByObjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<MROObjectFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from MROObjectFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    MROObjectFile findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(String name, Integer parent, Integer qcr);

    MROObjectFile findByObjectAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<MROObjectFile> findByObjectAndFileNo(Integer id, String fileNo);

    List<MROObjectFile> findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<MROObjectFile> findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<MROObjectFile> findByObjectAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from MROObjectFile i where i.object= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByObjectAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<MROObjectFile> findByObjectAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from MROObjectFile i where i.object= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByObjectAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);
}
