package com.cassinisys.documents.repo;

import com.cassinisys.documents.model.dm.DMDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 11/12/18.
 */
@Repository
public interface DMDocumentRepository extends JpaRepository<DMDocument, Integer>, QueryDslPredicateExecutor<DMDocument> {

    DMDocument findByFolderAndIdAndLatestTrueOrderByModifiedDateDesc(Integer folderId, Integer documentId);

    DMDocument findByFolderAndNameAndLatestTrueOrderByModifiedDateDesc(Integer folderId, String name);

    List<DMDocument> findByFolderAndLatestTrueOrderByModifiedDateDesc(Integer folderId);

    List<DMDocument> findByFolderAndNameOrderByVersionDesc(Integer folderId, String documentName);

    List<DMDocument> findByFolderAndName(Integer folderId, String documentName);
}
