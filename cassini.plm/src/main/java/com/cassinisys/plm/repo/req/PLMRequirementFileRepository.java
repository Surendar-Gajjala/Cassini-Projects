package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementFile;
import com.cassinisys.plm.model.req.PLMRequirementVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementFileRepository extends JpaRepository<PLMRequirementFile, Integer> {
    List<PLMRequirementFile> findByIdIn(Iterable<Integer> fileIds);

    List<PLMRequirementFile> findByRequirement(PLMRequirementVersion requirement);

    List<PLMRequirementFile> findByRequirementAndName(PLMRequirementVersion requirement, String name);

    List<PLMRequirementFile> findByRequirementAndLatestTrueOrderByModifiedDateDesc(PLMRequirementVersion requirement);

    PLMRequirementFile findByRequirementAndNameAndLatestTrue(PLMRequirementVersion requirement, String name);

    List<PLMRequirementFile> findAllByRequirementAndNameOrderByCreatedDateDesc(PLMRequirementVersion requirement, String name);

    List<PLMRequirementFile> findByRequirementAndNameContainingIgnoreCase(PLMRequirementVersion requirement, String name);

    List<PLMRequirementFile> findByRequirementAndNameAndLatestFalseOrderByCreatedDateDesc(PLMRequirementVersion requirement, String name);

    List<PLMRequirementFile> findByRequirementAndNameContainingIgnoreCaseAndLatestTrue(PLMRequirementVersion requirement, String name);

    List<PLMRequirementFile> findByRequirementAndFileNoAndLatestFalseOrderByCreatedDateDesc(PLMRequirementVersion requirement, String fileNo);

    List<PLMRequirementFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<PLMRequirementFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMRequirementFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PLMRequirementFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMRequirementFile> findByRequirementAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(PLMRequirementVersion requirement);

    PLMRequirementFile findByRequirementAndNameAndParentFileIsNullAndLatestTrue(PLMRequirementVersion requirement, String name);

    PLMRequirementFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMRequirementFile> findByRequirementAndFileTypeAndLatestTrueOrderByModifiedDateDesc(PLMRequirementVersion requirement, String fileType);

    PLMRequirementFile findByRequirementAndFileNoAndLatestTrue(PLMRequirementVersion requirement, String fileNo);

    List<PLMRequirementFile> findByRequirementAndFileNo(PLMRequirementVersion requirement, String fileNo);

    PLMRequirementFile findByNameEqualsIgnoreCaseAndParentFileAndRequirementAndLatestTrue(String name, Integer parent, PLMRequirementVersion requirement);

    PLMRequirementFile findByNameEqualsIgnoreCaseAndRequirementAndLatestTrue(String name, PLMRequirementVersion requirement);

    @Query("select i.id from PLMRequirementFile i where i.requirement.id= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByRequirementAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMRequirementFile i where i.requirement.id= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByRequirementAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);
}
