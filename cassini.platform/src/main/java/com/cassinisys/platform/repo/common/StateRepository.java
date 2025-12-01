package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author reddy
 */
@Repository
public interface StateRepository extends JpaRepository<State, Integer> {

	List<State> findByCountry(Integer countryId);

	Page<State> findByCountry(Integer countryId, Pageable pageable);
	
	List<State> findByIdIn(Iterable<Integer> ids);

}
