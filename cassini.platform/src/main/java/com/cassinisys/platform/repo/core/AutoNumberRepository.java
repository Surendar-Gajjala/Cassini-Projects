package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.AutoNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 7/1/15.
 */
@Repository
public interface AutoNumberRepository extends JpaRepository<AutoNumber, Integer>, QueryDslPredicateExecutor<AutoNumber> {

	Page<AutoNumber> findAllByOrderByIdDesc(Pageable pageable);
	
	List<AutoNumber> findAllByOrderByIdDesc();

	AutoNumber findByPrefix(String prefix);

	AutoNumber findByName(String name);

	@Query("SELECT a FROM AutoNumber a WHERE a.id NOT IN :id and a.prefix=:prefix")
	AutoNumber findByPrefixAndNotId(@Param("id") Integer id, @Param("prefix") String prefix);

	@Query("SELECT a FROM AutoNumber a WHERE a.id NOT IN :id and a.name=:name")
	AutoNumber findByNameAndNotId(@Param("id") Integer id, @Param("name") String name);

	List<AutoNumber> findByIdIn(Iterable<Integer> autoNumberIds);
}
