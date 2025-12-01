package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyam on 31-07-2018.
 */
@Repository
public interface TestExecutionRepository extends JpaRepository<TestExecution, Integer> {
    TestExecution findById(Integer executionId);
}
