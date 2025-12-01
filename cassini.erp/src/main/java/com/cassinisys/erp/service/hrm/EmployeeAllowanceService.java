package com.cassinisys.erp.service.hrm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.model.hrm.ERPEmployeeAllowance;
import com.cassinisys.erp.repo.hrm.EmployeeAllowanceRepository;

@Service
@Transactional
public class EmployeeAllowanceService {

	@Autowired
	EmployeeAllowanceRepository employeeallowanceRepository;

	public Page<ERPEmployeeAllowance> getAllEmployeeAllowances(Pageable pageable) {
		return employeeallowanceRepository.findAll(pageable);
	}


	public ERPEmployeeAllowance create(ERPEmployeeAllowance employeeAllowance) {
		return employeeallowanceRepository.save(employeeAllowance);
	}


	public List<ERPEmployeeAllowance> createEmployeeAllowances(
			List<ERPEmployeeAllowance> employeeAllowances) {

		List<ERPEmployeeAllowance> allowances = new ArrayList<ERPEmployeeAllowance>();
		ERPEmployeeAllowance erpEmployeeAllowance = null;

		for (ERPEmployeeAllowance allowance : employeeAllowances) {
			erpEmployeeAllowance = employeeallowanceRepository.save(allowance);
			allowances.add(erpEmployeeAllowance);
		}
		return allowances;
	}

}
