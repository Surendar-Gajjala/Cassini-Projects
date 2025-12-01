package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMInspectionPlanFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface InspectionPlanFileRepository extends JpaRepository<PQMInspectionPlanFile, Integer> {

    List<PQMInspectionPlanFile> findByInspectionPlanAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from PQMInspectionPlanFile i where i.inspectionPlan= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByInspectionPlanAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    PQMInspectionPlanFile findByInspectionPlanAndNameAndParentFileIsNullAndLatestTrue(Integer planId, String name);

    PQMInspectionPlanFile findByNameEqualsIgnoreCaseAndParentFileAndInspectionPlanAndLatestTrue(String name, Integer parent, Integer planId);

    List<PQMInspectionPlanFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count(i) from PQMInspectionPlanFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    PQMInspectionPlanFile findByParentFileAndNameAndLatestTrue(Integer id, String name);

    PQMInspectionPlanFile findByInspectionPlanAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<PQMInspectionPlanFile> findByInspectionPlanAndFileNo(Integer id, String fileNo);

    List<PQMInspectionPlanFile> findByInspectionPlanAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<PQMInspectionPlanFile> findByInspectionPlanAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<PQMInspectionPlanFile> findByInspectionPlanAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PQMInspectionPlanFile i where i.inspectionPlan= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByInspectionPlanAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<PQMInspectionPlanFile> findByInspectionPlanAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String file);

    List<PQMInspectionPlanFile> findByInspectionPlanAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(Integer id, String fileNo);

    List<PQMInspectionPlanFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);
}
