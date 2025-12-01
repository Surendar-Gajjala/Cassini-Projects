package com.cassinisys.test.repo;

import com.cassinisys.test.model.RCProgramExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RcProgramExectionRepository extends JpaRepository<RCProgramExecution, Integer> {
}
