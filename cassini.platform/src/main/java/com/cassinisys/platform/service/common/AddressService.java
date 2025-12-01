package com.cassinisys.platform.service.common;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Address;
import com.cassinisys.platform.repo.common.AddressRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
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
public class AddressService implements CrudService<Address, Integer>,
		PageableService<Address, Integer> {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private SessionWrapper sessionWrapper;

	@Transactional
	public Address create(Address address) {
		checkNotNull(address);
		address.setId(null);
		address = addressRepository.save(address);

		return address;
	}


	@Transactional
	public Address update(Address address) {
		checkNotNull(address);
		checkNotNull(address.getId());
		address = addressRepository.save(address);

		return address;
	}


	@Transactional
	public void delete(Integer id) {
		checkNotNull(id);
		addressRepository.delete(id);
	}

	@Transactional(readOnly = true)
	public Address get(Integer id) {
		checkNotNull(id);
		Address address = addressRepository.findOne(id);
		if (address == null) {
			throw new ResourceNotFoundException();
		}
		return address;
	}

	@Transactional(readOnly = true)
	public List<Address> getAll() {
		return addressRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Page<Address> findAll(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order("id")));
		}
		return addressRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public List<Address> findMultiple(List<Integer> ids) {
		return addressRepository.findByIdIn(ids);
	}

}
