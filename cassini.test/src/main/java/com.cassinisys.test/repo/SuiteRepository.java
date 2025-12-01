package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestSuite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 02-07-2018.
 */
@Repository
public interface SuiteRepository extends JpaRepository<TestSuite, Integer> {
	List<TestSuite> findByPlanOrderByCreatedDateAsc(Integer testPlan);

	TestSuite findByPlanAndNameEqualsIgnoreCase(Integer testPlan, String name);

	List<TestSuite> findByPlan(Integer id);
}
