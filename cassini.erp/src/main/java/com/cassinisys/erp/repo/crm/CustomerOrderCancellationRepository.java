package com.cassinisys.erp.repo.crm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.crm.ERPCustomerOrderCancellation;

@Repository
public interface CustomerOrderCancellationRepository extends JpaRepository<ERPCustomerOrderCancellation, Integer> {

}
