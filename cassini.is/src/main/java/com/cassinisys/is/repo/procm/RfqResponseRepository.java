package com.cassinisys.is.repo.procm;
/**
 * The Class is for RfqResponseRepository
 **/

import com.cassinisys.is.model.procm.ISRfqResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RfqResponseRepository extends
        JpaRepository<ISRfqResponse, Integer> {
    /**
     * The method used to findByRfq of ISRfqResponse
     **/
    public Page<ISRfqResponse> findByRfq(Integer rfqId, Pageable pageable);

}
