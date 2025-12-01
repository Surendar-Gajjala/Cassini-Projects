package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface ManufacturerFileRepository extends JpaRepository<PLMManufacturerFile, Integer> {
    List<PLMManufacturerFile> findByIdIn(Iterable<Integer> ids);

    List<PLMManufacturerFile> findByManufacturer(Integer manufacturer);

    List<PLMManufacturerFile> findByManufacturerAndName(Integer mfrId, String name);

    List<PLMManufacturerFile> findByManufacturerAndLatestTrueOrderByModifiedDateDesc(Integer manufacturer);

    PLMManufacturerFile findByManufacturerAndNameAndLatestTrue(Integer manufacturer, String name);

    List<PLMManufacturerFile> findAllByManufacturerAndNameOrderByCreatedDateDesc(@Param("mfrId") Integer mfrId, @Param("name") String name);

    List<PLMManufacturerFile> findByManufacturerAndNameContainingIgnoreCase(Integer mfrId, String name);

    List<PLMManufacturerFile> findByManufacturerAndNameAndLatestFalseOrderByCreatedDateDesc(Integer manufacturer, String name);

    List<PLMManufacturerFile> findByManufacturerAndNameContainingIgnoreCaseAndLatestTrue(Integer manufacturer, String name);

    List<PLMManufacturerFile> findByManufacturerAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer manufacturer, String fileNo);

    List<PLMManufacturerFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<PLMManufacturerFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMManufacturerFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PLMManufacturerFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMManufacturerFile> findByManufacturerAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer manufacturer);

    PLMManufacturerFile findByManufacturerAndNameAndParentFileIsNullAndLatestTrue(Integer manufacturer, String name);

    PLMManufacturerFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMManufacturerFile> findByManufacturerAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer manufacturer, String fileType);

    PLMManufacturerFile findByManufacturerAndFileNoAndLatestTrue(Integer mfrId, String fileNo);

    List<PLMManufacturerFile> findByManufacturerAndFileNo(Integer mfrId, String fileNo);

    PLMManufacturerFile findByNameEqualsIgnoreCaseAndParentFileAndManufacturerAndLatestTrue(String name, Integer parent, Integer manufacturer);

    PLMManufacturerFile findByNameEqualsIgnoreCaseAndManufacturerAndLatestTrue(String name, Integer manufacturer);

    @Query("select i.id from PLMManufacturerFile i where i.manufacturer= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByMfrAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMManufacturerFile i where i.manufacturer= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByManufacturerAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

}
