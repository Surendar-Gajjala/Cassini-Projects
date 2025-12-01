package com.cassinisys.is.repo.procm;
/**
 * The Class is for BidBoqRepository
 **/

import com.cassinisys.is.model.procm.ISBidBoq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidBoqRepository extends JpaRepository<ISBidBoq, Integer> {
    /**
     * The method used to findByBid of ISBidBoq
     **/
    public Page<ISBidBoq> findByBid(Integer bidId, Pageable pageable);

    /**
     * The method used to findByIdIn of ISBidBoq
     **/
    public List<ISBidBoq> findByIdIn(Iterable<Integer> ids);

}
