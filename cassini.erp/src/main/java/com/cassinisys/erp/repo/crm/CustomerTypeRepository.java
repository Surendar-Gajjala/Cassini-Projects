package com.cassinisys.erp.repo.crm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.crm.ERPCustomerType;

@Repository
public interface CustomerTypeRepository extends JpaRepository<ERPCustomerType, Integer> {
    ERPCustomerType findByName(String name);
}
