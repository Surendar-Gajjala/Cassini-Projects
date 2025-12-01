package com.cassinisys.is.repo.col;
/**
 * The Class is for BidMeetingRepository
 **/

import com.cassinisys.is.model.col.ISBidMeeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidMeetingRepository extends
        JpaRepository<ISBidMeeting, Integer> {
    /**
     * The method used to findByBid for ISBidMeeting
     **/
    public Page<ISBidMeeting> findByBid(Integer bidId, Pageable pageable);

    /**
     * The method used to findByIdIn from the list of ISBidMeeting
     **/
    public List<ISBidMeeting> findByIdIn(Iterable<Integer> ids);
}
