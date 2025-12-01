package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPSalesRepFieldReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface SalesRepFieldReportRepository extends JpaRepository<ERPSalesRepFieldReport, Integer>,
        QueryDslPredicateExecutor<ERPSalesRepFieldReport> {

    @Query (
        "SELECT r FROM ERPSalesRepFieldReport r WHERE r.salesRep.id = :salesRep"
    )
    Page<ERPSalesRepFieldReport> findBySalesRepId(@Param("salesRep") Integer salesRep, Pageable pageable);

    @Query (
        "SELECT r FROM ERPSalesRepFieldReport r WHERE r.customer.id = :customerId"
    )
    Page<ERPSalesRepFieldReport> findByCustomerId(@Param("customerId") Integer customerId, Pageable pageable);
}
