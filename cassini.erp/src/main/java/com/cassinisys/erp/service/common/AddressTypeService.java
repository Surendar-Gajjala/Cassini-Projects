package com.cassinisys.erp.service.common;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPAddressType;
import com.cassinisys.erp.repo.common.AddressTypeRepository;

@Service
@Transactional
public class AddressTypeService {

	private AddressTypeRepository repository;

	@Autowired
	public AddressTypeService(AddressTypeRepository repository) {
		this.repository = repository;
	}

	public ERPAddressType create(ERPAddressType addressType) {
		checkNotNull(addressType);
		addressType.setTypeId(null);
		return repository.save(addressType);
	}

	public ERPAddressType update(ERPAddressType addressType) {
		checkNotNull(addressType);
		checkNotNull(addressType.getTypeId());
		return repository.save(addressType);
	}

	public void delete(Integer id) {
		checkNotNull(id);
		repository.delete(id);
	}

	public ERPAddressType get(Integer id) {
		checkNotNull(id);
		ERPAddressType addressType = repository.findOne(id);
		if (addressType == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return addressType;
	}

	public List<ERPAddressType> getAll() {
		return repository.findAll();
	}

	
}
