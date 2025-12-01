package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestRun;
import com.cassinisys.test.model.TestRunConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 02-07-2018.
 */
@Repository
public interface RunRepository extends JpaRepository<TestRun, Integer> {
    List<TestRun> findByTestRunConfiguration(TestRunConfiguration runConfigId);
}
