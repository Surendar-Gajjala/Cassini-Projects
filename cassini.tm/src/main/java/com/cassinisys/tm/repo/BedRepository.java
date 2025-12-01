package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMBed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<TMBed, Integer> {
    List<TMBed> findBySuite(Integer suiteId);
    TMBed findBySuiteAndName(Integer suiteId, String name);

    List<TMBed> findByAssignedToIsNull();
    List<TMBed> findByAssignedToIsNotNull();

    List<TMBed> findBySuiteAndAssignedToIsNull(Integer suiteId);
    List<TMBed> findBySuiteAndAssignedToIsNotNull(Integer suiteId);

    TMBed findByAssignedTo(Integer personId);
}
