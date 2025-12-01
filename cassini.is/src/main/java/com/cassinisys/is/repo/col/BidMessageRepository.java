package com.cassinisys.is.repo.col;
/**
 * The Class is for BidMessageRepository
 **/

import com.cassinisys.is.model.col.ISBidMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidMessageRepository extends
        JpaRepository<ISBidMessage, Integer> {
    /**
     * The method used to findByBid for ISBidMessage
     **/
    public Page<ISBidMessage> findByBid(Integer bidId, Pageable pageable);

    /**
     * The method used to findBySentToId for ISBidMessage
     **/
    public Page<ISBidMessage> findBySentToId(Integer sentToId, Pageable pageable);

    /**
     * The method used to findByIdIn for ISBidMessage
     **/
    public List<ISBidMessage> findByIdIn(Iterable<Integer> ids);

}
