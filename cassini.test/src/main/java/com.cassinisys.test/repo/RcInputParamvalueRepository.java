package com.cassinisys.test.repo;

import com.cassinisys.test.model.RCInputParamValue;
import com.cassinisys.test.model.TestInputParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RcInputParamvalueRepository extends JpaRepository<RCInputParamValue, Integer> {

    RCInputParamValue findByConfigAndTestCaseAndInputParam(Integer config, Integer testCase, Integer inputParam);
}
