package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RmObjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 20-10-2018.
 */
@Repository
public interface RmObjectFileRepository extends JpaRepository<RmObjectFile, Integer> {

    RmObjectFile findByObjectAndNameAndLatestTrue(Integer objectId, String name);

    List<RmObjectFile> findAllByObjectAndName(Integer objectId, String name);

    List<RmObjectFile> findByObjectAndNameAndLatestFalseOrderByCreatedDateDesc(Integer objectId, String name);

    List<RmObjectFile> findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer objectId, String fileNo);

    List<RmObjectFile> findByObjectAndLatestTrueOrderByModifiedDateDesc(Integer rmObject);

    List<RmObjectFile> findByObjectAndNameContainingIgnoreCaseAndLatestTrue(Integer rmObject, String name);

    List<RmObjectFile> findByObject(Integer objectId);

    List<RmObjectFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<RmObjectFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<RmObjectFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<RmObjectFile> findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer objectId);

    RmObjectFile findByObjectAndNameAndParentFileIsNullAndLatestTrue(Integer objectId, String name);

    RmObjectFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<RmObjectFile> findByObjectAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer objectId, String fileType);

    RmObjectFile findByObjectAndFileNoAndLatestTrue(Integer objectId, String fileNo);

    RmObjectFile findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(String name, Integer parent, Integer objectId);

    RmObjectFile findByNameEqualsIgnoreCaseAndObjectAndLatestTrue(String name, Integer objectId);

    List<RmObjectFile> findByObjectAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(Integer object, String fileNo);
}
