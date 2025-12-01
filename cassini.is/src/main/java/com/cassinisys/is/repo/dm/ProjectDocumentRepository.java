package com.cassinisys.is.repo.dm;
/**
 * The Class is for ProjectDocumentRepository
 **/

import com.cassinisys.is.model.dm.ISProjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectDocumentRepository extends
        JpaRepository<ISProjectDocument, Integer> {
    /**
     * The method used to findByProjectAndLatestTrueOrderByModifiedDateDesc from the list of ISProjectDocument
     **/
    public List<ISProjectDocument> findByProjectAndLatestTrueOrderByModifiedDateDesc(Integer projectId);

    /**
     * The method used to findByFolderAndIdAndLatestTrueOrderByModifiedDateDesc  of ISProjectDocument
     **/
    public ISProjectDocument findByFolderAndIdAndLatestTrueOrderByModifiedDateDesc(Integer folderId, Integer documentId);

    /**
     * The method used to findByFolderAndLatestTrueOrderByModifiedDateDesc from the list of ISProjectDocument
     **/
    public List<ISProjectDocument> findByFolderAndLatestTrueOrderByModifiedDateDesc(Integer folderId);

    /**
     * The method used to findByFolderAndNameAndLatestTrueOrderByModifiedDateDesc  of ISProjectDocument
     **/
    public ISProjectDocument findByFolderAndNameAndLatestTrueOrderByModifiedDateDesc(Integer folderId, String name);

    /**
     * The method used to findByFolderAndNameOrderByVersionDesc from the list of ISProjectDocument
     **/
    public List<ISProjectDocument> findByFolderAndNameOrderByVersionDesc(Integer folderId, String name);

    /**
     * The method used to findByIdIn from the list of ISProjectDocument
     **/
    public List<ISProjectDocument> findByIdIn(Iterable<Integer> ids);

    List<ISProjectDocument> findByFolderAndName(Integer folderId, String name);

    @Query(
            "SELECT count(i) FROM ISProjectDocument i WHERE i.project= :projectId"
    )
    Integer findCountByProject(@Param("projectId") Integer projectId);

}
