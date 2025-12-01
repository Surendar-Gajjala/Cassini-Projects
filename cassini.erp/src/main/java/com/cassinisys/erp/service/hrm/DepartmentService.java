package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.hrm.ERPDepartment;
import com.cassinisys.erp.repo.hrm.DepartmentRepository;

@Service
@Transactional
public class DepartmentService {

	@Autowired
	DepartmentRepository departmentRepository;

	/**
	 * 
	 * @param department
	 * @return
	 */
	public ERPDepartment create(ERPDepartment department) {

		if (department != null) {

			department = departmentRepository.save(department);
		}
		return department;

	}

	/**
	 * 
	 * @param departId
	 * @return
	 */
	public ERPDepartment get(Integer departId) {

		ERPDepartment depart = null;

		if (departId > 0) {
			depart = departmentRepository.findOne(departId);
		}
		return depart;
	}

	/**
	 * 
	 * @param department
	 * @return
	 */
	public ERPDepartment update(ERPDepartment department) {
		checkNotNull(department);
		checkNotNull(department.getId());
		if (departmentRepository.findOne(department.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return departmentRepository.save(department);
	}

	/**
	 * 
	 * @return
	 */
	public List<ERPDepartment> getAll() {

		List<ERPDepartment> departments = null;

		departments = departmentRepository.findAll();

		return departments;
	}

	/**
	 * 
	 * @param deptId
	 */
	public void delete(Integer deptId) {
		checkNotNull(deptId);
		departmentRepository.delete(deptId);
	}

}
