package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestOutputParamExpectedValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by GSR on 03-07-2018.
 */
@Repository
public interface OutputParamExpectedValueRepository extends JpaRepository<TestOutputParamExpectedValue, Integer> {
}
