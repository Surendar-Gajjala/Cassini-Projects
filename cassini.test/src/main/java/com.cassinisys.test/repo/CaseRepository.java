package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 02-07-2018.
 */
@Repository
public interface CaseRepository extends JpaRepository<TestCase, Integer> {
	List<TestCase> findBySuiteOrderByCreatedDateAsc(Integer testSuite);

	TestCase findBySuiteAndNameEqualsIgnoreCase(Integer testSuite, String name);

	List<TestCase> findBySuite(Integer id);
}
