package com.cassinisys.erp.repo.hrm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPSalaryHistory;

/**
 * Created by reddy on 8/29/15.
 */
@Repository
public interface SalaryHistoryRepository extends
		JpaRepository<ERPSalaryHistory, ERPSalaryHistory.SalaryHistoryId> {

	/*ERPSalaryHistory findBySalaryHistoryIdPayrollIdAndEmployeeId(
			Integer payrollId, Integer employeeId);
*/
}
