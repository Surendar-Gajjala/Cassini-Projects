package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPWorkShiftEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkShiftEmployeeRepository extends JpaRepository<ERPWorkShiftEmployee, Integer>, QueryDslPredicateExecutor<ERPWorkShiftEmployee> {


    List<ERPWorkShiftEmployee> findByShiftId(Integer shift);

}
