package com.cassinisys.test.repo;

import com.cassinisys.test.model.RunCase;
import com.cassinisys.test.model.RunOutPutParam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
public interface RunOutputParamRepository extends JpaRepository<RunOutPutParam, Integer> {
    List<RunOutPutParam> findByTCase(RunCase runCase);
}
