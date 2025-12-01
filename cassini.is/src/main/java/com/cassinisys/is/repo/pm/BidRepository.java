package com.cassinisys.is.repo.pm;
/**
 * The Class is for BidRepository
 **/

import com.cassinisys.is.model.pm.ISBid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<ISBid, Integer> {
    /**
     * The method used to findByIdIn of ISBid
     **/
    public List<ISBid> findByIdIn(Iterable<Integer> ids);

}
