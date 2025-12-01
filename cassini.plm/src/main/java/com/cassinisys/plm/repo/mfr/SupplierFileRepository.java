package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMSupplierFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Repository
public interface SupplierFileRepository extends JpaRepository<PLMSupplierFile, Integer> {
    List<PLMSupplierFile> findByIdIn(Iterable<Integer> ids);

    List<PLMSupplierFile> findBySupplier(Integer supplier);

    List<PLMSupplierFile> findBySupplierAndName(Integer mfrId, String name);

    List<PLMSupplierFile> findBySupplierAndLatestTrueOrderByModifiedDateDesc(Integer supplier);

    PLMSupplierFile findBySupplierAndNameAndLatestTrue(Integer supplier, String name);

    List<PLMSupplierFile> findAllBySupplierAndNameOrderByCreatedDateDesc(@Param("supplierId") Integer mfrId, @Param("name") String name);

    List<PLMSupplierFile> findBySupplierAndNameContainingIgnoreCase(Integer supplierId, String name);

    List<PLMSupplierFile> findBySupplierAndNameAndLatestFalseOrderByCreatedDateDesc(Integer supplier, String name);

    List<PLMSupplierFile> findBySupplierAndNameContainingIgnoreCaseAndLatestTrue(Integer supplier, String name);

    List<PLMSupplierFile> findBySupplierAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer supplier, String fileNo);

    List<PLMSupplierFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<PLMSupplierFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMSupplierFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PLMSupplierFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMSupplierFile> findBySupplierAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer supplier);

    PLMSupplierFile findBySupplierAndNameAndParentFileIsNullAndLatestTrue(Integer supplier, String name);

    PLMSupplierFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMSupplierFile> findBySupplierAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer supplier, String fileType);

    PLMSupplierFile findBySupplierAndFileNoAndLatestTrue(Integer mfrId, String fileNo);

    List<PLMSupplierFile> findBySupplierAndFileNo(Integer mfrId, String fileNo);

    PLMSupplierFile findByNameEqualsIgnoreCaseAndParentFileAndSupplierAndLatestTrue(String name, Integer parent, Integer supplier);

    PLMSupplierFile findByNameEqualsIgnoreCaseAndSupplierAndLatestTrue(String name, Integer supplier);

    @Query("select i.id from PLMSupplierFile i where i.supplier= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsBySupplierAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMSupplierFile i where i.supplier= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsBySupplierAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);
}
