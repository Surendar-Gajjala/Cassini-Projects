package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMMfrPartInspectionReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 10-03-2022.
 */
@Repository
public interface MfrPartInspectionReportRepository extends JpaRepository<PLMMfrPartInspectionReport, Integer> {

    List<PLMMfrPartInspectionReport> findByIdIn(Iterable<Integer> ids);

    List<PLMMfrPartInspectionReport> findByManufacturerPartAndFileTypeAndParentFileIsNullOrderByModifiedDateDesc(Integer mfrPart, String fileType);

    List<PLMMfrPartInspectionReport> findByManufacturerPartAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer mfrPart);

    PLMMfrPartInspectionReport findByManufacturerPartAndNameEqualsIgnoreCaseAndFileTypeAndParentFileIsNull(Integer mfrPart, String name, String fileType);

    PLMMfrPartInspectionReport findByManufacturerPartAndNameEqualsIgnoreCaseAndFileTypeAndParentFile(Integer mfrPart, String name, String fileType, Integer parent);

    List<PLMMfrPartInspectionReport> findByManufacturerPartAndParentFileAndFileTypeOrderByModifiedDateDesc(Integer mfrPart, Integer parent, String fileType);

    @Query("select i.id from PLMMfrPartInspectionReport i where i.manufacturerPart= :mfrPart and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByManufacturerPartAndFileTypeOrderByModifiedDateDesc(@Param("mfrPart") Integer mfrPart, @Param("fileType") String fileType);

    @Query("select i.id from PLMMfrPartInspectionReport i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByParentAndFileTypeOrderByModifiedDateDesc(@Param("parent") Integer parent, @Param("fileType") String fileType);

    PLMMfrPartInspectionReport findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileIsNullAndLatestTrue(Integer mfrPart, String name);

    PLMMfrPartInspectionReport findByManufacturerPartAndNameEqualsIgnoreCaseAndParentFileAndLatestTrue(Integer mfrPart, String name, Integer parent);

    PLMMfrPartInspectionReport findByFileNoAndLatestTrue(String fileNo);

    List<PLMMfrPartInspectionReport> findByFileNo(String fileNo);

    List<PLMMfrPartInspectionReport> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    List<PLMMfrPartInspectionReport> findByParentFileAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer parent, String fileType);

    List<PLMMfrPartInspectionReport> findByFileNoAndLatestFalseOrderByCreatedDateDesc(String fileNo);

    @Query("select i.id from PLMMfrPartInspectionReport i where i.fileNo= :fileNo and i.latest = false")
    List<Integer> getFileIdsByFileNoAndLatestFalse(@Param("fileNo") String fileNo);

    List<PLMMfrPartInspectionReport> findByManufacturerPartAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer mfrPart);

    List<PLMMfrPartInspectionReport> findByNameContainingIgnoreCaseAndLatestTrue(String name);

    @Query("select i.id from PLMMfrPartInspectionReport i where i.manufacturerPart= :mfrPart and i.latest = true and i.fileType = 'FILE' and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByManufacturerPartAndNameContainingIgnoreCaseAndLatestTrue(@Param("mfrPart") Integer mfrPart, @Param("name") String name);

    Page<PLMMfrPartInspectionReport> findByManufacturerPartAndNameContainingIgnoreCaseAndLatestTrueAndFileType(Integer mfrPart, String name, String fileType, Pageable pageable);

    @Query("select count (i) from PLMMfrPartInspectionReport i where i.manufacturerPart= :mfrPart and i.fileType = 'FILE' and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    Integer getDocumentCountByManufacturerPartAndNameContainingIgnoreCaseAndLatestTrueAndFileType(@Param("mfrPart") Integer mfrPart, @Param("name") String name);

    @Query("select i.id from PLMMfrPartInspectionReport i where i.parentFile= :parent and i.fileType = 'FILE' and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByParentAndNameContainingIgnoreCaseAndLatestTrue(@Param("parent") Integer parent, @Param("name") String name);

    List<PLMMfrPartInspectionReport> findByManufacturerPartAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer mfrPart, String fileType);

    @Query("select count (i) from PLMMfrPartInspectionReport i where i.manufacturerPart= :mfrPart and i.fileType= :fileType and i.latest = true")
    Integer getCountByManufacturerPartAndFileTypeAndLatestTrueOrderByModifiedDateDesc(@Param("mfrPart") Integer mfrPart, @Param("fileType") String fileType);

    List<PLMMfrPartInspectionReport> findByManufacturerPartAndFileTypeAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer mfrPart, String fileType);

    @Query("select count (i) from PLMMfrPartInspectionReport i where i.parentFile= :parent and i.fileType= :fileType and i.latest = true")
    Integer getChildrenCountByParentAndFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select count (i) from PLMMfrPartInspectionReport i where i.parentFile= :parent and i.latest = true and i.fileType= :fileType and i.id not in :ids")
    Integer getChildrenCountByParentAndFileTypeAndIdsNotIn(@Param("parent") Integer parent, @Param("fileType") String fileType, @Param("ids") Iterable<Integer> ids);

    @Query("select count (i) from PLMMfrPartInspectionReport i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParent(@Param("parent") Integer parent);

    @Query("select count (i) from PLMMfrPartInspectionReport i where i.manufacturerPart= :mfrPart and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByManufacturerPartAndFileTypeAndLatestTrue(@Param("fileType") String fileType);

    @Query("select count (i) from PLMMfrPartInspectionReport i where i.parentFile= :parent and i.fileType = 'FILE' and i.lifeCyclePhase.phaseType = 'RELEASED'")
    Integer getReleasedReportCountByParentFile(@Param("parent") Integer parent);

    @Query("select distinct i.createdBy from PLMMfrPartInspectionReport i where i.manufacturerPart= :mfrPart and i.parentFile is null and i.revision = 'A' and i.version = 1")
    List<Integer> getCreatedByIds(@Param("mfrPart") Integer mfrPart);

    List<PLMMfrPartInspectionReport> findByLatestTrueAndFileTypeOrderByLifeCyclePhaseIdAsc(String fileType);

    @Query("select distinct i.lifeCyclePhase.lifeCycle from PLMMfrPartInspectionReport i")
    List<Integer> getInspectionReportLifecycleIds();
}
