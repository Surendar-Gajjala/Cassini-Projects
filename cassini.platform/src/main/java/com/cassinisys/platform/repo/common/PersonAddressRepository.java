package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.PersonAddress;
import com.cassinisys.platform.model.common.PersonAddressId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author reddy
 */
@Repository
public interface PersonAddressRepository extends
		JpaRepository<PersonAddress, PersonAddressId> {

	@Query("select pa.id.addressId from PersonAddress pa where pa.id.personId = :personId")
	public Page<Integer> findByPersonId(
			@Param(value = "personId") Integer personId, Pageable pageable);

}
