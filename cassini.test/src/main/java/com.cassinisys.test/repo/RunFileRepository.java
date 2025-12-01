package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestRunFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 11-09-2018.
 */
@Repository
public interface RunFileRepository extends JpaRepository<TestRunFile, Integer> {

	TestRunFile findByTestRun(Integer fileId);

	TestRunFile findByTestRunAndNameAndLatestTrue(Integer item, String name);

	List<TestRunFile> findByTestRunAndLatestTrueOrderByModifiedDateDesc(Integer runCase);
}
