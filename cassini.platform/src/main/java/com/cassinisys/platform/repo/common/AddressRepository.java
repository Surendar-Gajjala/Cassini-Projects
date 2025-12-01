package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author reddy
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

	public Page<Address> findByAddressType(Integer addressTypeId,
			Pageable pageable);

	public List<Address> findByIdIn(Iterable<Integer> ids);

}
