package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author reddy
 */
@Repository
public interface AddressTypeRepository extends
		JpaRepository<AddressType, Integer> {

}
