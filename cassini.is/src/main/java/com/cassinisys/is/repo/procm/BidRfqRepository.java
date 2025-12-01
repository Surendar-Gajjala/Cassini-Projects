package com.cassinisys.is.repo.procm;
/**
 * The Class is for BidBoqRepository
 **/

import com.cassinisys.is.model.procm.ISBidRfq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRfqRepository extends JpaRepository<ISBidRfq, Integer> {
    /**
     * The method used to findByIdIn of ISBidRfq
     **/
    public List<ISBidRfq> findByIdIn(Iterable<Integer> ids);
}
