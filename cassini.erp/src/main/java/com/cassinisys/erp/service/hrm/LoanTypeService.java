package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.hrm.ERPLoanType;
import com.cassinisys.erp.repo.hrm.LoanTypeRepository;

@Service
@Transactional
public class LoanTypeService {

	@Autowired
	LoanTypeRepository loanTypeRepository;

	public ERPLoanType createLoanType(ERPLoanType loanType) {

		return loanTypeRepository.save(loanType);

	}

	public ERPLoanType updateLoanType(ERPLoanType loanType) {

		checkNotNull(loanType);
		checkNotNull(loanType.getId());
		if (loanTypeRepository.findOne(loanType.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return loanTypeRepository.save(loanType);

	}

	public ERPLoanType getLoanTypeById(Integer typeId) {

		return loanTypeRepository.findOne(typeId);

	}

	public void deleteLoanType(Integer typeId) {
		checkNotNull(typeId);
		loanTypeRepository.delete(typeId);
	}

	/**
	 * 
	 * @return
	 */
	public List<ERPLoanType> getAllLoanTypes() {

		return loanTypeRepository.findAll();

	}

}
