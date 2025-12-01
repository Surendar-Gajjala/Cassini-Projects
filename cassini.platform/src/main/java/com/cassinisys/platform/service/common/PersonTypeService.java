package com.cassinisys.platform.service.common;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.PersonType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PersonTypeRepository;
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

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author reddy
 */
@Service
public class PersonTypeService implements CrudService<PersonType, Integer>,
		PageableService<PersonType, Integer> {

	@Autowired
	private PersonTypeRepository repository;

	@Autowired
	private PersonRepository personRepository;

	@Override
	@Transactional
	public PersonType create(PersonType personType) {
		checkNotNull(personType);
		personType.setId(null);
		return repository.save(personType);
	}

	@Override
	@Transactional
	public PersonType update(PersonType personType) {
		checkNotNull(personType);
		checkNotNull(personType.getId());
		return repository.save(personType);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		checkNotNull(id);
		repository.delete(id);
	}

	
	@Override
	@Transactional(readOnly = true)
	public PersonType get(Integer id) {
		checkNotNull(id);
		PersonType personType = repository.findOne(id);
		if (personType == null) {
			throw new ResourceNotFoundException();
		}
		return personType;
	}


	@Override
	@Transactional(readOnly = true)
	public List<PersonType> getAll() {
		return repository.findAll();
	}

	
	@Override
	@Transactional(readOnly = true)
	public Page<PersonType> findAll(Pageable pageable) {
		checkNotNull(pageable);
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order("id")));
		}
		return repository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public List<PersonType> findMultiple(List<Integer> ids) {
		return repository.findByIdIn(ids);
	}
}
