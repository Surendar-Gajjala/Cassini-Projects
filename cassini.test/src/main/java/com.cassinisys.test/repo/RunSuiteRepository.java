package com.cassinisys.test.repo;

import com.cassinisys.test.model.RunPlan;
import com.cassinisys.test.model.RunSuite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RunSuiteRepository extends JpaRepository<RunSuite, Integer> {
    List<RunSuite> findByRunPlanOrderByCreatedDateAsc(RunPlan runPlan);

    RunSuite findByName(String name);

    RunSuite findByRunPlanAndName(RunPlan runPlan, String name);
}
