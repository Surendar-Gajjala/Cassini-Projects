package com.cassinisys.erp.repo.hrm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPDeductionHistory;

@Repository
public interface DeductionHistoryRepository
		extends
		JpaRepository<ERPDeductionHistory, ERPDeductionHistory.DeductionHistoryId> {

	List<ERPDeductionHistory> findByDeductionHistoryIdPayrollIdAndDeductionHistoryIdEmployeeId(
			Integer payrollId, Integer employeeId);
}
