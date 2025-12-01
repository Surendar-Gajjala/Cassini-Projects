package com.cassinisys.is.repo.procm;
/**
 * The Class is for RfqSentToRepository
 **/

import com.cassinisys.is.model.procm.ISRfqSentTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RfqSentToRepository extends
        JpaRepository<ISRfqSentTo, Integer> {
    /**
     * The method used to findByRfq of ISRfqSentTo
     **/
    public Page<ISRfqSentTo> findByRfq(Integer rfqId, Pageable pageable);

}
