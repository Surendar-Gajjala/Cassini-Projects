package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPSalesRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRegionRepository extends JpaRepository<ERPSalesRegion, Integer>,
        QueryDslPredicateExecutor<ERPSalesRegion> {
    ERPSalesRegion findByNameAndDistrict(String name, String district);
}

