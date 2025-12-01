package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.model.hrm.ERPEmployeeType;
import com.cassinisys.erp.repo.hrm.EmployeeRepository;
import com.cassinisys.erp.repo.hrm.EmployeeTypeRepository;

@Service
@Transactional
public class EmployeeTypeService {

	@Autowired
	EmployeeTypeRepository employeeTypeRepository;

	@Autowired
	EmployeeRepository employeeRepository;
	

	/**
	 * 
	 * @param empType
	 * @return
	 */
	public ERPEmployeeType createEmployeeType(ERPEmployeeType empType) {

		return employeeTypeRepository.save(empType);

	}

	/**
	 * 
	 * @param salesRep
	 * @return
	 */
	public ERPEmployeeType updateEmployeeType(ERPEmployeeType employeeType) {

		checkNotNull(employeeType);
		checkNotNull(employeeType.getId());
		if (employeeTypeRepository.findOne(employeeType.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return employeeTypeRepository.save(employeeType);

	}

	/**
	 * 
	 * @param typeId
	 * @return
	 */
	public ERPEmployeeType getEmployeeTypeById(Integer typeId) {

		return employeeTypeRepository.findOne(typeId);

	}

	/**
	 * 
	 * @param typeId
	 */
	public void deleteEmployeeType(Integer typeId) {
		checkNotNull(typeId);
		//employeeTypeRepository.delete(typeId);
	}

	/**
	 * 
	 * @return
	 */
	public List<ERPEmployeeType> getAllEmployeeTypes() {

		return employeeTypeRepository.findAll();

	}

	/**
	 * 
	 * @param typeId
	 * @return
	 */
	public List<ERPEmployee> getAllEmployeesOfTYpe(Integer typeId) {

		ERPEmployeeType employeeType = employeeTypeRepository.findOne(typeId);
		List<ERPEmployee> employees = null;

		if ( employeeType != null) {

			// employees = employeeRepository.findByEmployeeType(employeeType.getId());

		} else {

			throw new ERPException();

		}

		return employees;

	}

}
