package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestOutputParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 03-07-2018.
 */
@Repository
public interface OutputParamRepository extends JpaRepository<TestOutputParam, Integer> {

    List<TestOutputParam> findByTestCase(Integer testCase);

    TestOutputParam findByNameAndTestCase(String name, Integer testCaseId);
}
