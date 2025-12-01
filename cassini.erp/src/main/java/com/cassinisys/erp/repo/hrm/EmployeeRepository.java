package com.cassinisys.erp.repo.hrm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPEmployee;

@Repository
public interface EmployeeRepository extends JpaRepository<ERPEmployee, Integer>, 
		QueryDslPredicateExecutor<ERPEmployee>{
    ERPEmployee findByFirstName(String firstName);
    ERPEmployee findByEmployeeNumber(String employeeNumber);
}
