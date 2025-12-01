package com.cassinisys.erp.repo.crm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.crm.ERPSalesRep;

@Repository
public interface SalesRepRepository extends JpaRepository<ERPSalesRep, Integer> {
    ERPSalesRep findByFirstName(String firstName);

    @Modifying
    @Query (
        nativeQuery = true,
        value = "INSERT INTO ERP_SALESREP(SALESREP_ID) VALUES (:salesRepId)"
    )
    void createSalesRep(@Param("salesRepId") Integer salesRepId);
}
