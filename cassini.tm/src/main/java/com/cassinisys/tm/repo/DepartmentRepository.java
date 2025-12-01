package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 03-08-2016.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<TMDepartment, Integer>, QueryDslPredicateExecutor<TMDepartment> {
    public List<TMDepartment> findByIdIn(Iterable<Integer> ids);
    TMDepartment findByName(String name);
}
