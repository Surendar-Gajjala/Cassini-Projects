package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMInspectionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface InspectionFileRepository extends JpaRepository<PQMInspectionFile, Integer> {
    PQMInspectionFile findByInspectionAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    PQMInspectionFile findByNameEqualsIgnoreCaseAndParentFileAndInspectionAndLatestTrue(String name, Integer parent, Integer inspection);

    List<PQMInspectionFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from PQMInspectionFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    PQMInspectionFile findByInspectionAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<PQMInspectionFile> findByInspectionAndFileNo(Integer id, String fileNo);

    PQMInspectionFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PQMInspectionFile> findByInspectionAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<PQMInspectionFile> findByInspectionAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from PQMInspectionFile i where i.inspection= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByInspectionAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<PQMInspectionFile> findByInspectionAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PQMInspectionFile i where i.inspection= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByInspectionAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<PQMInspectionFile> findByInspectionAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String file);
}
