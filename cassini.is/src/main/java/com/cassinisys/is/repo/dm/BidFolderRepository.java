package com.cassinisys.is.repo.dm;
/**
 * The Class is for BidFolderRepository
 **/

import com.cassinisys.is.model.dm.ISBidFolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidFolderRepository extends
        JpaRepository<ISBidFolder, Integer> {
    /**
     * The method used to findByParent of ISBidFolder
     **/
    Page<ISBidFolder> findByParent(Integer projectFolderId, Pageable pageable);

    /**
     * The method used to findByBidAndParentIsNull of ISBidFolder
     **/
    Page<ISBidFolder> findByBidAndParentIsNull(Integer bidId, Pageable pageable);

    /**
     * The method used to findByIdIn of ISBidFolder
     **/
    public List<ISBidFolder> findByIdIn(Iterable<Integer> ids);
}
