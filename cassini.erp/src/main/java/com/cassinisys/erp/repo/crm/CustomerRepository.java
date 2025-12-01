package com.cassinisys.erp.repo.crm;

import java.util.List;

import com.cassinisys.erp.model.crm.ERPSalesRegion;
import com.cassinisys.erp.model.crm.ERPSalesRep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.crm.ERPCustomer;
import com.cassinisys.erp.model.crm.ERPCustomerType;


@Repository
public interface CustomerRepository extends JpaRepository<ERPCustomer, Integer>,
		QueryDslPredicateExecutor<ERPCustomer> {
	ERPCustomer findByName(String name);

	@Query (
			"SELECT c FROM ERPCustomer c WHERE c.name= :name AND c.customerType= :customerType"
	)
	ERPCustomer findByNameAndCustomerType(@Param("name") String name, @Param("customerType") ERPCustomerType customerType);

	@Query (
		"SELECT c FROM ERPCustomer c WHERE c.salesRep.id= :salesrep"
	)
	Page<ERPCustomer> findBySalesRep(@Param("salesrep") Integer salesrep, Pageable pageable);

	@Query (
		"SELECT c.salesRep FROM ERPCustomer c WHERE c.salesRegion= :salesRegion"
	)
	List<ERPSalesRep> getSalesRepsByRegion(@Param("salesRegion") ERPSalesRegion salesRegion);

    @Query (
        "SELECT c.salesRegion FROM ERPCustomer c WHERE c.salesRep= :salesRep"
    )
    List<ERPSalesRegion> getSalesRegionsBySalesRep(@Param("salesRep") ERPSalesRep salesRep);


	@Query (
			"SELECT c FROM ERPCustomer c WHERE c.name= :name AND c.salesRegion.name= :region"
	)
	List<ERPCustomer> findByNameAndRegion(@Param("name") String name, @Param("region") String region);
}
