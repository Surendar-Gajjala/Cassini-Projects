package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestInputParamValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by GSR on 03-07-2018.
 */
@Repository
public interface InputParamValueRepository extends JpaRepository<TestInputParamValue, Integer> {
}
