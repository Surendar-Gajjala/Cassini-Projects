package com.cassinisys.test.repo;

import com.cassinisys.test.model.RunCase;
import com.cassinisys.test.model.RunSuite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RunCaseRepository extends JpaRepository<RunCase, Integer> {
    List<RunCase> findBySuiteOrderByCreatedDateAsc(RunSuite runSuite);
}
