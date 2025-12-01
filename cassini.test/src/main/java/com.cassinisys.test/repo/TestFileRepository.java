package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 11-09-2018.
 */
@Repository
public interface TestFileRepository extends JpaRepository<TestFile, Integer> {
}
