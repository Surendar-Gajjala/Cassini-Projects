package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestInputParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 03-07-2018.
 */
@Repository
public interface InputParamRepository extends JpaRepository<TestInputParam, Integer> {

    List<TestInputParam> findByTestCase(Integer testCase);

    TestInputParam findByNameAndTestCase(String name, Integer testCaseId);
}
