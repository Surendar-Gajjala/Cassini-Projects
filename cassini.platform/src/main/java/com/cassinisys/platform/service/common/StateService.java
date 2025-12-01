package com.cassinisys.platform.service.common;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.State;
import com.cassinisys.platform.repo.common.StateRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author reddy
 */
@Service
public class StateService implements CrudService<State, Integer>,
		PageableService<State, Integer> {

	@Autowired
	private StateRepository repository;

	@Override
	@Transactional
	public State create(State state) {
		checkNotNull(state);
		state.setId(null);
		return repository.save(state);
	}

	@Override
	@Transactional
	public State update(State state) {
		checkNotNull(state);
		checkNotNull(state.getId());
		state.setModifiedDate(new Date());
		return repository.save(state);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		checkNotNull(id);
		repository.delete(id);
	}

	
	@Override
	@Transactional
	public State get(Integer id) {
		checkNotNull(id);
		State state = repository.getOne(id);
		if (state == null) {
			throw new ResourceNotFoundException();
		}
		return state;
	}

	
	@Override
	@Transactional
	public List<State> getAll() {
		return repository.findAll();
	}

	
	@Override
	@Transactional
	public Page<State> findAll(Pageable pageable) {
		checkNotNull(pageable);
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order("id")));
		}
		return repository.findAll(pageable);
	}

	@Transactional
	public List<State> findByCountry(Integer id) {
		return repository.findByCountry(id);
	}


	@Transactional
	public List<State> findMultiple(List<Integer> ids) {
		return repository.findByIdIn(ids);
	}

}
