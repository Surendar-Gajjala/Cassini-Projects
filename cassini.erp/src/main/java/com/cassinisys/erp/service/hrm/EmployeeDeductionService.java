package com.cassinisys.erp.service.hrm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.model.hrm.ERPEmployeeDeduction;
import com.cassinisys.erp.repo.hrm.EmployeeDeductionRepository;

@Service
@Transactional
public class EmployeeDeductionService {

	@Autowired
	EmployeeDeductionRepository employeeDeductionRepository;

	/**
	 * 
	 * @return List<ERPEmployeeDeduction>
	 */
	public List<ERPEmployeeDeduction> getAllEmployeeDeductions() {
		return employeeDeductionRepository.findAll();
	}

	public ERPEmployeeDeduction create(ERPEmployeeDeduction employeeDeduction) {
		return employeeDeductionRepository.save(employeeDeduction);
	}

	public List<ERPEmployeeDeduction> createEmployeeDeductions(
			List<ERPEmployeeDeduction> employeeDeduction) {

		List<ERPEmployeeDeduction> deductions = new ArrayList<ERPEmployeeDeduction>();
		ERPEmployeeDeduction erpEmployeeDeductions = null;

		for (ERPEmployeeDeduction deduction : employeeDeduction) {
			erpEmployeeDeductions = employeeDeductionRepository.save(deduction);
			deductions.add(erpEmployeeDeductions);
		}
		return deductions;
	}

	/**
	 * 
	 * @param employeededuction
	 * @return
	 *//*
	public ERPEmployeeDeduction update(ERPEmployeeDeduction employeededuction) {
		
		if(employeededuction.)
		return employeeDeductionRepository.save(employeededuction);
	}
*/
}
