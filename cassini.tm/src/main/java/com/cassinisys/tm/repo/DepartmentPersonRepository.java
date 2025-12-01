package com.cassinisys.tm.repo;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.tm.model.TMDepartmentPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 03-08-2016.
 */
@Repository
public interface DepartmentPersonRepository extends JpaRepository<TMDepartmentPerson, Integer>, QueryDslPredicateExecutor<TMDepartmentPerson> {
    TMDepartmentPerson findByPersonAndDepartment(Integer personId, Integer deptId);
    List<TMDepartmentPerson> findByDepartment(Integer deptId);
    List<TMDepartmentPerson> findByPersonIn(Iterable<Integer> ids);
}
