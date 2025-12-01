package com.cassinisys.erp.service.hrm;

import java.util.ArrayList;
import java.util.List;

import com.cassinisys.erp.model.hrm.ERPEmployeeSalary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.repo.hrm.EmployeeSalaryRepository;

@Service
@Transactional
public class EmployeeSalaryService {

	@Autowired
	EmployeeSalaryRepository employeeSalaryRepository;

	public List<ERPEmployeeSalary> getAllEmployeeSalaries() {
		return employeeSalaryRepository.findAll();
	}

	public ERPEmployeeSalary getEmployeeSalaryById(Integer id) {
		return employeeSalaryRepository.findOne(id);
	}

	public ERPEmployeeSalary create(ERPEmployeeSalary employeeSalary) {
		return employeeSalaryRepository.save(employeeSalary);
	}

	public List<ERPEmployeeSalary> createEmployeeSalaries(
			List<ERPEmployeeSalary> employeeSalaries) {

		List<ERPEmployeeSalary> salaries = new ArrayList<ERPEmployeeSalary>();
		ERPEmployeeSalary erpEmoployeeSalary = null;

		for (ERPEmployeeSalary salary : employeeSalaries) {
			erpEmoployeeSalary = employeeSalaryRepository.save(salary);
			salaries.add(erpEmoployeeSalary);
		}
		return salaries;
	}

}
