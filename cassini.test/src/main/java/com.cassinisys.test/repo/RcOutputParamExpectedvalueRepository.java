package com.cassinisys.test.repo;

import com.cassinisys.test.model.RCOutPutParamExpectedValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RcOutputParamExpectedvalueRepository extends JpaRepository<RCOutPutParamExpectedValue, Integer> {

    RCOutPutParamExpectedValue findByConfigAndTestCaseAndOutputParam(Integer config, Integer testCase, Integer outputParam);
}

