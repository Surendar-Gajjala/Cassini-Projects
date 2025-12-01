package com.cassinisys.erp.repo.hrm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPPayrollHistory;

@Repository
public interface PayrollHistoryRepository extends JpaRepository<ERPPayrollHistory, ERPPayrollHistory.PayrollHistoryId> {
	
	

}
