package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomObjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomObjectFileRepository extends JpaRepository<CustomObjectFile, Integer> {

    List<CustomObjectFile> findByObjectAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);


    @Query("select i from CustomObjectFile i where i.object= :id  and i.latest = true and i.parentFile is null order by  i.fileType desc , i.modifiedDate desc")
    List<CustomObjectFile> getCustomObjectFiles(@Param("id") Integer id);


    @Query("select i from CustomObjectFile i where i.object IN :ids  and i.latest = true and i.parentFile is null order by  i.fileType desc , i.modifiedDate desc")
    List<CustomObjectFile> getCustomObjectFilesByIdsIn(@Param("ids") List<Integer> ids);

    List<CustomObjectFile> findAllByObjectAndNameOrderByCreatedDateDesc(@Param("objectId") Integer objectId, @Param("name") String name);

    List<CustomObjectFile> findByObjectAndNameContainingIgnoreCaseAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer objectId, String name);

    List<CustomObjectFile> findByObjectAndNameAndLatestFalseOrderByCreatedDateDesc(Integer objectId, String name);

    List<CustomObjectFile> findByNumberIsNullAndLatestTrueOrderByIdAsc();

    List<CustomObjectFile> findByObjectAndNumberAndLatestFalseOrderByCreatedDateDesc(Integer objectId, String number);

    List<CustomObjectFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select i from CustomObjectFile i where i.parentFile IN :parentIds  and i.latest = true")
    List<CustomObjectFile> findByParentFileInAndLatestTrueOrderByCreatedDateDesc(@Param("parentIds") List<Integer> parentIds);

    List<CustomObjectFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<CustomObjectFile> findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer objectId);

    CustomObjectFile findByObjectAndNameAndParentFileIsNullAndLatestTrue(Integer objectId, String name);

    CustomObjectFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<CustomObjectFile> findByObjectAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer objectId, String fileType);

    @Query("select count (i) from CustomObjectFile i where i.object= :objectId and i.fileType= :fileType and i.latest = true")
    Integer getByObjectAndFileTypeAndLatestTrue(@Param("objectId") Integer objectId, @Param("fileType") String fileType);

    CustomObjectFile findByObjectAndNumberAndLatestTrue(Integer objectId, String number);

    CustomObjectFile findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(String name, Integer parent, Integer objectId);

    List<CustomObjectFile> findByParentFileOrderByFileTypeDescModifiedDateDesc(Integer parent);

    List<CustomObjectFile> findByObjectAndNumber(Integer object, String number);

    List<CustomObjectFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer folder);

    List<CustomObjectFile> findByLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc();
}

