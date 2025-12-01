package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.Lov;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LovRepository extends JpaRepository<Lov, Integer>,
		QueryDslPredicateExecutor<Lov> {

	Lov findByName(String name);

	List<Lov> findByTypeContainingIgnoreCase(String type);

	List<Lov> findByTypeAndName(String type, String name);

	List<Lov> findByOrderByIdDesc();

}
