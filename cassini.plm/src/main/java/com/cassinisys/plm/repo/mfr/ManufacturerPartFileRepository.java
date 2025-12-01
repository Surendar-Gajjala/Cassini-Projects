package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerPartFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface ManufacturerPartFileRepository extends JpaRepository<PLMManufacturerPartFile, Integer> {
    List<PLMManufacturerPartFile> findByIdIn(Iterable<Integer> ids);

    List<PLMManufacturerPartFile> findByManufacturerPart(Integer mfrPart);

    List<PLMManufacturerPartFile> findByManufacturerPartAndLatestTrueOrderByModifiedDateDesc(Integer mfrPart);

    PLMManufacturerPartFile findByManufacturerPartAndNameAndLatestTrue(Integer mfrPart, String name);

    List<PLMManufacturerPartFile> findAllByManufacturerPartAndNameOrderByCreatedDateDesc(@Param("partId") Integer partId, @Param("name") String name);

    List<PLMManufacturerPartFile> findByNameContainingIgnoreCase(String name);

    List<PLMManufacturerPartFile> findByManufacturerPartAndNameContainingIgnoreCaseAndLatestTrue(Integer part, String name);

    List<PLMManufacturerPartFile> findByManufacturerPartAndNameAndLatestFalseOrderByCreatedDateDesc(Integer partId, String name);

    List<PLMManufacturerPartFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<PLMManufacturerPartFile> findByManufacturerPartAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer partId, String fileNo);

    List<PLMManufacturerPartFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMManufacturerPartFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PLMManufacturerPartFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMManufacturerPartFile> findByManufacturerPartAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer partId);

    PLMManufacturerPartFile findByManufacturerPartAndNameAndParentFileIsNullAndLatestTrue(Integer partId, String name);

    PLMManufacturerPartFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMManufacturerPartFile> findByManufacturerPartAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer partId, String fileType);

    PLMManufacturerPartFile findByManufacturerPartAndFileNoAndLatestTrue(Integer partId, String fileNo);

    List<PLMManufacturerPartFile> findByManufacturerPartAndFileNo(Integer partId, String fileNo);

    PLMManufacturerPartFile findByNameEqualsIgnoreCaseAndParentFileAndManufacturerPartAndLatestTrue(String name, Integer parent, Integer partId);

    PLMManufacturerPartFile findByNameEqualsIgnoreCaseAndManufacturerPartAndLatestTrue(String name, Integer partId);

    @Query("select i.id from PLMManufacturerPartFile i where i.manufacturerPart= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByMfrAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMManufacturerPartFile i where i.manufacturerPart= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByManufacturerPartAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

}
