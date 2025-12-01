package com.cassinisys.plm.repo.pm;
/**
 * The Class is for ProjectWbsRepository
 **/

import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMObjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PLMObjectDocumentRepository extends JpaRepository<PLMObjectDocument, Integer> {

    @Query("select i.document.id from PLMObjectDocument i where i.object= :objectId and i.folder= :folderId")
    List<Integer> getDocumentIdsByObjectIdAndFolder(@Param("objectId") Integer objectId, @Param("folderId") Integer folderId);

    @Query("select i.document.id from PLMObjectDocument i where i.object= :objectId and i.folder= :folderId and i.documentType= :type")
    List<Integer> getDocumentIdsByObjectIdAndFolderAndDocumentType(@Param("objectId") Integer objectId, @Param("folderId") Integer folderId, @Param("type") String type);

    @Query("select i.document.id from PLMObjectDocument i where i.object= :objectId and i.folder is null")
    List<Integer> getDocumentIdsByObjectIdAndFolderIsNull(@Param("objectId") Integer objectId);

    @Query("select i.document.id from PLMObjectDocument i where i.object= :objectId and i.folder is null and i.documentType= :type")
    List<Integer> getDocumentIdsByObjectIdAndFolderIsNullAndDocumentType(@Param("objectId") Integer objectId, @Param("type") String type);

    PLMObjectDocument findByDocumentIdAndObjectAndFolderIsNull(Integer document, Integer object);

    PLMObjectDocument findByDocumentIdAndObjectAndFolder(Integer document, Integer object, Integer folder);

    List<PLMObjectDocument> findByObjectAndFolderIsNullAndDocumentType(Integer object, String type);

    List<PLMObjectDocument> findByObjectAndFolderAndDocumentType(Integer object, Integer folder, String type);

    List<PLMObjectDocument> findByObject(Integer object);

    List<PLMObjectDocument> findByFolder(Integer object);

    @Query("select count (i) from PLMObjectDocument i where i.object= :objectId and i.folder= :folderId")
    Integer getDocumentsCountByObjectIdAndFolder(@Param("objectId") Integer objectId, @Param("folderId") Integer folderId);

    @Query("select i from PLMObjectDocument i where i.object= :objectId and i.folder in :folderIds")
    List<PLMObjectDocument> getDocumentsCountByObjectIdAndFolderIds(@Param("objectId") Integer objectId, @Param("folderIds") List<Integer> folderIds);

    @Query("select count (i) from PLMObjectDocument i where i.object= :objectId and i.folder is null")
    Integer getDocumentsCountByObjectIdAndFolderIsNull(@Param("objectId") Integer objectId);

    @Query("select count (i) from PLMObjectDocument i where i.object= :objectId")
    Integer getDocumentsCountByObjectId(@Param("objectId") Integer objectId);

    @Query("select count (i) from PLMObjectDocument i where i.object= :objectId and i.document.lifeCyclePhase.phaseType= :phaseType")
    Integer getReleasedDocumentsByObjectAndStatus(@Param("objectId") Integer objectId, @Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("select count (i) from PLMObjectDocument i where i.object= :objectId and i.documentType= :type")
    Integer getDocumentsCountByObjectIdAndDocumentType(@Param("objectId") Integer objectId, @Param("type") String type);

    @Query("select count (i) from PLMObjectDocument i where i.document.id= :document")
    Integer getObjectDocumentCountByDocument(@Param("document") Integer document);

    @Query("select count (i) from PLMObjectDocument i where i.document.id in :documents")
    Integer getObjectDocumentCountByDocumentIds(@Param("documents") List<Integer> documents);

    @Query("select i from PLMObjectDocument i where i.object= :objectId and i.document.latest = true and i.document.fileType = 'FILE' and (LOWER(CAST(i.document.name as text))) LIKE '%' || :name || '%'")
    List<PLMObjectDocument> getFileIdsByObjectAndNameContainingIgnoreCaseAndLatestTrue(@Param("objectId") Integer objectId, @Param("name") String name);

    @Query("select count (i) from PLMObjectDocument i where i.object= :objectId and i.document.lifeCyclePhase.phaseType != 'RELEASED'")
    Integer getNotReleasedDocumentsCount(@Param("objectId") Integer objectId);
}
