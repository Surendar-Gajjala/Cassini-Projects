package com.cassinisys.test.repo;

import com.cassinisys.test.model.TestScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 02-07-2018.
 */
@Repository
public interface ScenarioRepository extends JpaRepository<TestScenario, Integer> {

    List<TestScenario> findAllByOrderByCreatedDateAsc();

    TestScenario findByNameEqualsIgnoreCase(String name);
}
