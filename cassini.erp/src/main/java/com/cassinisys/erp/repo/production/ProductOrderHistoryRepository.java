package com.cassinisys.erp.repo.production;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.production.ERPProductionOrderHistory;

@Repository
public interface ProductOrderHistoryRepository extends JpaRepository<ERPProductionOrderHistory, Integer> {

}
