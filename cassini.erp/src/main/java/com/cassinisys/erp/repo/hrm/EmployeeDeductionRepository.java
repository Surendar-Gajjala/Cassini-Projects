package com.cassinisys.erp.repo.hrm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPEmployeeDeduction;

@Repository
public interface EmployeeDeductionRepository
		extends
		JpaRepository<ERPEmployeeDeduction, ERPEmployeeDeduction.EmployeeDeductionId> {

	List<ERPEmployeeDeduction> findByEmployeeDeductionIdEmployee(
			Integer employee);

}
