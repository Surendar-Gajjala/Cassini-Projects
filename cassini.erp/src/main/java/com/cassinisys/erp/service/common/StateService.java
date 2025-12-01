package com.cassinisys.erp.service.common;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPCountry;
import com.cassinisys.erp.model.common.ERPState;
import com.cassinisys.erp.repo.common.CountryRepository;
import com.cassinisys.erp.repo.common.StateRepository;

@Service
@Transactional
public class StateService {

	@Autowired
	StateRepository stateRepo;

	@Autowired
	CountryRepository countryRepository;

	public ERPState createState(ERPState state) {
		return stateRepo.save(state);
	}

	public List<ERPState> getStatesByCountry(Integer id) {
		ERPCountry country = countryRepository.findOne(id);
		return stateRepo.findByCountry(country);
	}

	public List<ERPState> getAllStates() {
		return stateRepo.findAll();
	}

	public ERPState updateState(ERPState state) {

		checkNotNull(state);
		checkNotNull(state.getId());
		if (stateRepo.findOne(state.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return stateRepo.save(state);

	}

	public ERPState getStateById(Integer stateId) {
		return stateRepo.findOne(stateId);
	}

	public void deleteState(Integer stateId) {
		checkNotNull(stateId);
		stateRepo.delete(stateId);
	}

}
