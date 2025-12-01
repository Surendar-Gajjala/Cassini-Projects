package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.hrm.ERPAllowanceType;
import com.cassinisys.erp.repo.hrm.AllowanceTypeRepository;

@Service
@Transactional
public class AllowanceTypeService {

	@Autowired
	AllowanceTypeRepository allowanceTypeRepository;

	
	public ERPAllowanceType createAllowanceType(ERPAllowanceType allowanceType) {

		return allowanceTypeRepository.save(allowanceType);

	}

		public ERPAllowanceType updateAllowanceType(ERPAllowanceType allowanceType) {

		checkNotNull(allowanceType);
		checkNotNull(allowanceType.getId());
		if (allowanceTypeRepository.findOne(allowanceType.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return allowanceTypeRepository.save(allowanceType);

	}

	
	public ERPAllowanceType getAllowanceTypeById(Integer typeId) {

		return allowanceTypeRepository.findOne(typeId);

	}

	
	public void deleteAllowanceType(Integer typeId) {
		checkNotNull(typeId);
		//allowanceTypeRepository.delete(typeId);
	}

	/**
	 * 
	 * @return
	 */
	public List<ERPAllowanceType> getAllowanceTypes() {

		return allowanceTypeRepository.findAll();

	}

	
}
