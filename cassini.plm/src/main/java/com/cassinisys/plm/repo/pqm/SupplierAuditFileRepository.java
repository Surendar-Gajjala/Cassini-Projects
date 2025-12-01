package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMSupplierAuditFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 21-03-2022.
 */
@Repository
public interface SupplierAuditFileRepository extends JpaRepository<PQMSupplierAuditFile, Integer> {

    PQMSupplierAuditFile findBySupplierAuditAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    PQMSupplierAuditFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PQMSupplierAuditFile> findBySupplierAuditAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from PQMSupplierAuditFile i where i.supplierAudit= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsBySupplierAuditAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<PQMSupplierAuditFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from PQMSupplierAuditFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    PQMSupplierAuditFile findByNameEqualsIgnoreCaseAndParentFileAndSupplierAuditAndLatestTrue(String name, Integer parent, Integer qcr);

    PQMSupplierAuditFile findBySupplierAuditAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<PQMSupplierAuditFile> findBySupplierAuditAndFileNo(Integer id, String fileNo);

    List<PQMSupplierAuditFile> findBySupplierAuditAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<PQMSupplierAuditFile> findBySupplierAuditAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<PQMSupplierAuditFile> findBySupplierAuditAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PQMSupplierAuditFile i where i.supplierAudit= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsBySupplierAuditAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<PQMSupplierAuditFile> findBySupplierAuditAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from PQMSupplierAuditFile i where i.supplierAudit= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountBySupplierAuditAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);
}
