package com.cassinisys.erp.repo.hrm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPEmployeeAllowance;

@Repository
public interface EmployeeAllowanceRepository
		extends
		JpaRepository<ERPEmployeeAllowance, ERPEmployeeAllowance.EmployeeAllowanceId> {

	List<ERPEmployeeAllowance> findByEmployeeAllowanceIdEmployee(
			Integer employee);
}
