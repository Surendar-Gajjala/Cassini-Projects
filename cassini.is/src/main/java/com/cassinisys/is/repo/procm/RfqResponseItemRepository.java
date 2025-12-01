package com.cassinisys.is.repo.procm;
/**
 * The Class is for RfqResponseItemRepository
 **/

import com.cassinisys.is.model.procm.ISRfqResponseItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RfqResponseItemRepository extends
        JpaRepository<ISRfqResponseItem, Integer> {
    /**
     * The method used to findByResponse of ISRfqResponseItem
     **/
    public Page<ISRfqResponseItem> findByResponse(Integer responseId,
                                                  Pageable pageable);

}
