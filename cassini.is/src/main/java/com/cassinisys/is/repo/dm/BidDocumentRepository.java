package com.cassinisys.is.repo.dm;
/**
 * The Class is for BidDocumentRepository
 **/

import com.cassinisys.is.model.dm.ISBidDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidDocumentRepository extends JpaRepository<ISBidDocument, Integer> {
    /**
     * The method used to findByFolder of ISBidDocument
     **/
    Page<ISBidDocument> findByFolder(Integer bidFolderId, Pageable pageable);

    /**
     * The method used to findByFolderAndLatestTrue of ISBidDocument
     **/
    Page<ISBidDocument> findByFolderAndLatestTrue(Integer bidFolderId,
                                                  Pageable pageable);

    /**
     * The method used to findByFolderAndNameAndLatestTrue of ISBidDocument
     **/
    ISBidDocument findByFolderAndNameAndLatestTrue(Integer folderId,
                                                   String originalFilename);

    /**
     * The method used to findByFolderAndIdAndLatestTrue of ISBidDocument
     **/
    ISBidDocument findByFolderAndIdAndLatestTrue(Integer folderId,
                                                 Integer documentId);

    /**
     * The method used to findByFolderAndNameOrderByVersionDesc from the list of ISBidDocument
     **/
    List<ISBidDocument> findByFolderAndNameOrderByVersionDesc(Integer folderId,
                                                              String name);

    /**
     * The method used to findByIdIn from the list of ISBidDocument
     **/
    public List<ISBidDocument> findByIdIn(Iterable<Integer> ids);
}
