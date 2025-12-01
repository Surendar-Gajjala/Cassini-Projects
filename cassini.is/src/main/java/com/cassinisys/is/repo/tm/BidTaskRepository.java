package com.cassinisys.is.repo.tm;
/**
 * The Class is for BidTaskRepository
 **/

import com.cassinisys.is.model.tm.ISBidTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

public interface BidTaskRepository extends JpaRepository<ISBidTask, Integer>, QueryDslPredicateExecutor<ISBidTask> {
    /**
     * The method used to findByBid of ISBidTask
     **/
    Page<ISBidTask> findByBid(Integer bidId, Pageable pageable);

    /**
     * The method used to findByIdIn of ISBidTask
     **/
    public List<ISBidTask> findByIdIn(Iterable<Integer> ids);

}
