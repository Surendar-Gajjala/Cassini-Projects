package com.cassinisys.test.repo;

import com.cassinisys.test.model.RunScenario;
import com.cassinisys.test.model.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RunScenarioRepository extends JpaRepository<RunScenario, Integer> {

    RunScenario findByNameEqualsIgnoreCase(String name);

    RunScenario findByTestRun(TestRun testRun);

}
