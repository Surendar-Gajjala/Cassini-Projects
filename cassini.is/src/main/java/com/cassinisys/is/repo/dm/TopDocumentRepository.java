package com.cassinisys.is.repo.dm;

import com.cassinisys.is.model.dm.ISTopDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 018 18-Nov -17.
 */
@Repository
public interface TopDocumentRepository extends JpaRepository<ISTopDocument, Integer> {

    List<ISTopDocument> findByFolder(Integer folderId);

    ISTopDocument findByFolderAndIdAndLatestTrueOrderByModifiedDateDesc(Integer folderId, Integer documentId);

    ISTopDocument findByFolderAndNameAndLatestTrueOrderByModifiedDateDesc(Integer folderId, String name);

    List<ISTopDocument> findByFolderAndLatestTrueOrderByModifiedDateDesc(Integer folderId);

    List<ISTopDocument> findByFolderAndNameOrderByVersionDesc(Integer folderId, String documentName);

    List<ISTopDocument> findByFolderAndName(Integer folderId, String documentName);
}
