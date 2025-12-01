package com.cassinisys.erp.repo.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.common.ERPAddressType;

@Repository
public interface AddressTypeRepository extends
		JpaRepository<ERPAddressType, Integer> {
	ERPAddressType findByName(String name);
}
