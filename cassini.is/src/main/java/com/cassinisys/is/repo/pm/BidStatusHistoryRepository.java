package com.cassinisys.is.repo.pm;
/**
 * The Class is for BidStatusHistoryRepository
 **/

import com.cassinisys.is.model.pm.ISBidStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidStatusHistoryRepository extends JpaRepository<ISBidStatusHistory, Integer> {

}
