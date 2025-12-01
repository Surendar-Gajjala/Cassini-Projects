package com.cassinisys.is.repo.procm;
/**
 * The Class is for RequisitionRequestRepository
 **/

import com.cassinisys.is.model.procm.ISRequisitionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequisitionRequestRepository extends
        JpaRepository<ISRequisitionRequest, Integer> {
    /**
     * The method used to findByIdIn of ISRequisitionRequest
     **/
    public List<ISRequisitionRequest> findByIdIn(Iterable<Integer> ids);

}
