package com.cassinisys.erp.repo.crm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.crm.ERPCustomerAddress;

@Repository
public interface CustomerAddressRepository extends JpaRepository<ERPCustomerAddress, Integer> {

}
