package com.cassinisys.test.repo;

import com.cassinisys.test.model.RunPlan;
import com.cassinisys.test.model.RunScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RunPlanRepository extends JpaRepository<RunPlan, Integer> {
    List<RunPlan> findByRunScenarioOrderByCreatedDateAsc(RunScenario runScenario);

    RunPlan findByRunScenarioAndName(RunScenario runScenario, String name);
}
