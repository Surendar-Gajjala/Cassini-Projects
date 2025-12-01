package com.cassinisys.test.repo;

import com.cassinisys.test.model.RunExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RunExectionRepository extends JpaRepository<RunExecution, Integer> {
}
