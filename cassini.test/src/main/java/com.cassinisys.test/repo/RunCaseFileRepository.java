package com.cassinisys.test.repo;

import com.cassinisys.test.model.RunCaseFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 20-09-2018.
 */
@Repository
public interface RunCaseFileRepository extends JpaRepository<RunCaseFile, Integer> {

    RunCaseFile findByTestRunCaseAndNameAndLatestTrue(Integer id, String name);

    List<RunCaseFile> findByTestRunCaseAndLatestTrueOrderByModifiedDateDesc(Integer id);
}
