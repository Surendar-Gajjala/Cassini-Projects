package com.cassinisys.platform.service.common;

import com.cassinisys.platform.model.common.Address;
import com.cassinisys.platform.model.common.AddressType;
import com.cassinisys.platform.repo.common.AddressRepository;
import com.cassinisys.platform.repo.common.AddressTypeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author reddy
 */
@Service
public class AddressTypeService implements CrudService<AddressType, Integer>,
		PageableService<AddressType, Integer> {

	@Autowired
	private AddressTypeRepository addressTypeRository;

	@Autowired
	private AddressRepository addressRepository;
	
	@Override
	@Transactional
	public AddressType create(AddressType addressType) {
		checkNotNull(addressType);
		addressType.setId(null);
		return addressTypeRository.save(addressType);
	}

	@Override
	@Transactional
	public AddressType update(AddressType addressType) {
		checkNotNull(addressType);
		checkNotNull(addressType.getId());
		return addressTypeRository.save(addressType);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		checkNotNull(id);
		addressTypeRository.delete(id);
	}

	
	@Override
	@Transactional(readOnly = true)
	public AddressType get(Integer id) {
		checkNotNull(id);
		AddressType addressType = addressTypeRository.findOne(id);
		if (addressType == null) {
			throw new ResourceNotFoundException();
		}
		return addressType;
	}

	
	@Override
	@Transactional(readOnly = true)
	public List<AddressType> getAll() {
		return addressTypeRository.findAll();
	}

	
	@Override
	@Transactional(readOnly = true)
	public Page<AddressType> findAll(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order("id")));
		}
		return addressTypeRository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Page<Address> getAddresses(Integer id, Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order("id")));
		}
		return addressRepository.findByAddressType(id, pageable);
	}

}
