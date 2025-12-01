package com.cassinisys.is.repo.procm;
/**
 * The Class is for RequisitionItemListRepository
 **/

import com.cassinisys.is.model.procm.ISRequisitionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequisitionItemListRepository extends
        JpaRepository<ISRequisitionItem, Integer> {
    /**
     * The method used to findByRequisition of ISRequisitionItem
     **/
    public Page<ISRequisitionItem> findByRequisition(Integer requisitionId,
                                                     Pageable pageable);

}
