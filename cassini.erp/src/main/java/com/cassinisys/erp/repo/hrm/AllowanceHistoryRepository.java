package com.cassinisys.erp.repo.hrm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPAllowanceHistory;

@Repository
public interface AllowanceHistoryRepository
		extends
		JpaRepository<ERPAllowanceHistory, ERPAllowanceHistory.AllowanceHistoryId> {

	List<ERPAllowanceHistory> findByAllowanceHistoryIdPayrollIdAndAllowanceHistoryIdEmployeeId(
			Integer payrollId, Integer employeeId);

}
