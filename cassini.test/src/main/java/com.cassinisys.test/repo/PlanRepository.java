package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 02-07-2018.
 */
@Repository
public interface PlanRepository extends JpaRepository<TestPlan, Integer> {

    List<TestPlan> findByScenario(Integer scenarioId);

    List<TestPlan> findByScenarioOrderByCreatedDateAsc(Integer scenario);

    TestPlan findByScenarioAndNameEqualsIgnoreCase(Integer scenario, String name);
}
