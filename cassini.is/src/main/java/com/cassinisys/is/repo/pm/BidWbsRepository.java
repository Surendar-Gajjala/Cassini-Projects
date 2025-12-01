package com.cassinisys.is.repo.pm;
/**
 * The Class is for BidWbsRepository
 **/

import com.cassinisys.is.model.pm.ISBidWbs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidWbsRepository extends JpaRepository<ISBidWbs, Integer> {
    /**
     * The method used to findByIdIn of ISBidWbs
     **/
    public List<ISBidWbs> findByIdIn(Iterable<Integer> ids);
}
