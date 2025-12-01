package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestRunConfiguration;
import com.cassinisys.test.model.TestScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 03-07-2018.
 */
@Repository
public interface RunConfigurationRepository extends JpaRepository<TestRunConfiguration, Integer> {

	List<TestRunConfiguration> findAllByOrderByCreatedDateAsc();

	List<TestRunConfiguration> findByScenario(TestScenario scenarioId);

	List<TestRunConfiguration> findByScenarioOrderByCreatedDateAsc(TestScenario scenarioId);

	TestRunConfiguration findByScenarioAndNameIgnoreCase(TestScenario testScenario, String name);

	TestRunConfiguration findByScenarioAndName(TestScenario testScenario, String name);
}
