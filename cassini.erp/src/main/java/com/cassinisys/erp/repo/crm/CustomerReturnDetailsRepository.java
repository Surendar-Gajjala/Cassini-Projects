package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPCustomerReturnDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerReturnDetailsRepository extends JpaRepository<ERPCustomerReturnDetails, Integer> {

    @Query("SELECT r FROM ERPCustomerReturnDetails r WHERE r.customerReturn.id = :returnId")
    List<ERPCustomerReturnDetails> findByReturnId(@Param("returnId") Integer returnId);

}
