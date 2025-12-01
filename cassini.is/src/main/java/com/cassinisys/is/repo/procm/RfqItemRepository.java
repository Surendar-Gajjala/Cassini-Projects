package com.cassinisys.is.repo.procm;
/**
 * The Class is for RfqItemRepository
 **/

import com.cassinisys.is.model.procm.ISRfqItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RfqItemRepository extends JpaRepository<ISRfqItem, Integer> {
    /**
     * The method used to findByRfq of ISRfqItem
     **/
    public Page<ISRfqItem> findByRfq(Integer rfqId, Pageable pageable);

}
