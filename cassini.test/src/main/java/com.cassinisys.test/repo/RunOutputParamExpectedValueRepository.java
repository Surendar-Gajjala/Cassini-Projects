package com.cassinisys.test.repo;

import com.cassinisys.test.model.RunOutPutParamExpectedValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RunOutputParamExpectedValueRepository extends JpaRepository<RunOutPutParamExpectedValue, Integer> {
    RunOutPutParamExpectedValue findByTCaseAndRunOutPutParam(Integer runcaseId, Integer runOuPutParamId);
}
