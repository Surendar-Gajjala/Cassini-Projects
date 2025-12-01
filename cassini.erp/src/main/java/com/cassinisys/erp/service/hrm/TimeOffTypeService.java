package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.hrm.ERPTimeOffType;
import com.cassinisys.erp.repo.hrm.EmployeeRepository;
import com.cassinisys.erp.repo.hrm.TimeOffTypeRepository;

@Service
@Transactional
public class TimeOffTypeService {

	@Autowired
	TimeOffTypeRepository timeOffTypeRepository;

	@Autowired
	EmployeeRepository employeeRepository;
	

	/**
	 * 
	 * @param timeOffType
	 * @return
	 */
	public ERPTimeOffType createTimeOffType(ERPTimeOffType timeOffType) {

		return timeOffTypeRepository.save(timeOffType);

	}

	/**
	 * 
	 * @param timeOffType
	 * @return
	 */
	public ERPTimeOffType updateTimeOffType(ERPTimeOffType timeOffType) {

		checkNotNull(timeOffType);
		checkNotNull(timeOffType.getId());
		if (timeOffTypeRepository.findOne(timeOffType.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return timeOffTypeRepository.save(timeOffType);

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ERPTimeOffType getTimeOffTypeById(Integer id) {

		return timeOffTypeRepository.findOne(id);

	}

	/**
	 * 
	 * @param id
	 */
	public void deleteTimeOffType(Integer id) {
		checkNotNull(id);
		timeOffTypeRepository.delete(id);
	}

	/**
	 * 
	 * @return
	 */
	public List<ERPTimeOffType> getAllTimeOffTypes() {

		return timeOffTypeRepository.findAll();

	}

}
