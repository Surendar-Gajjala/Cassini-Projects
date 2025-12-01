package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestProgramExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyam on 31-07-2018.
 */
@Repository
public interface TestProgramExecutionRepository extends JpaRepository<TestProgramExecution, Integer> {
}
