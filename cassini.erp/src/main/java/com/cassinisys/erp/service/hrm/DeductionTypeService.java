package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.hrm.ERPDeductionType;
import com.cassinisys.erp.repo.hrm.DeductionTypeRepository;

@Service
@Transactional
public class DeductionTypeService {

	@Autowired
	DeductionTypeRepository deductionTypeRepository;

	
	public ERPDeductionType createDeductionType(ERPDeductionType deductionType) {

		return deductionTypeRepository.save(deductionType);

	}

		public ERPDeductionType updateDeductionType(ERPDeductionType deductionType) {

		checkNotNull(deductionType);
		checkNotNull(deductionType.getId());
		if (deductionTypeRepository.findOne(deductionType.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return deductionTypeRepository.save(deductionType);

	}

	
	public ERPDeductionType getDeductionTypeById(Integer typeId) {

		return deductionTypeRepository.findOne(typeId);

	}

	
	public void deleteDeductionType(Integer typeId) {
		checkNotNull(typeId);
		//deductionTypeRepository.delete(typeId);
	}

	/**
	 * 
	 * @return
	 */
	public List<ERPDeductionType> getDeductionTypes() {

		return deductionTypeRepository.findAll();

	}

	
}
