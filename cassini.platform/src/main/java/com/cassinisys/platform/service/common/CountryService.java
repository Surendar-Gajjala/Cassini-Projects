package com.cassinisys.platform.service.common;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Country;
import com.cassinisys.platform.model.common.State;
import com.cassinisys.platform.repo.common.CountryRepository;
import com.cassinisys.platform.repo.common.StateRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author reddy
 */
@Service
public class CountryService implements CrudService<Country, Integer>,
		PageableService<Country, Integer> {

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private StateRepository stateRepository;
	
	@Override
	@Transactional
	public Country create(Country country) {
		checkNotNull(country);
		country.setId(null);
		return countryRepository.save(country);
	}

	@Override
	@Transactional
	public Country update(Country country) {
		checkNotNull(country);
		checkNotNull(country.getId());
		country.setModifiedDate(new Date());
		return countryRepository.save(country);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		checkNotNull(id);
		countryRepository.delete(id);
	}

	
	@Override
	@Transactional(readOnly = true)
	public Country get(Integer id) {
		checkNotNull(id);
		Country country = countryRepository.findOne(id);
		if (country == null) {
			throw new ResourceNotFoundException();
		}
		return country;
	}

	
	@Override
	@Transactional(readOnly = true)
	public List<Country> getAll() {
		return countryRepository.findAll();
	}

	
	@Override
	@Transactional(readOnly = true)
	public Page<Country> findAll(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort("name"));
		}
		return countryRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Page<State> getStates(Integer id, Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort("name"));
		}
		return stateRepository.findByCountry(id, pageable);
	}

	@Transactional(readOnly = true)
	public List<Country> findMultiple(List<Integer> ids) {
		return countryRepository.findByIdIn(ids);
	}
}
