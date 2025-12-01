package com.cassinisys.test.repo;

import com.cassinisys.test.model.RunCase;
import com.cassinisys.test.model.RunInputParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 01-08-2018.
 */
@Repository
public interface RunInputparamRepository extends JpaRepository<RunInputParam, Integer> {
    List<RunInputParam> findByTCase(RunCase runCase);
}
