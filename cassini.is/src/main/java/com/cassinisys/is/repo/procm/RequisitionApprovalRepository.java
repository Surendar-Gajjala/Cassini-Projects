package com.cassinisys.is.repo.procm;
/**
 * The Class is for RequisitionApprovalRepository
 **/

import com.cassinisys.is.model.procm.ISRequisitionApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequisitionApprovalRepository extends
        JpaRepository<ISRequisitionApproval, Integer> {
    /**
     * The method used to findByRequisition of ISRequisitionApproval
     **/
    public Page<ISRequisitionApproval> findByRequisition(Integer requisitionId,
                                                         Pageable pageable);

}
