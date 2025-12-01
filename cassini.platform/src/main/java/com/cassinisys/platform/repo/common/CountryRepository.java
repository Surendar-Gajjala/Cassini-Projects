package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author reddy
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
	List<Country> findByIdIn(Iterable<Integer> ids);
}
