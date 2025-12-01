package com.cassinisys.erp.repo.hrm;

import com.cassinisys.erp.model.hrm.ERPEmployeeSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSalaryRepository extends JpaRepository<ERPEmployeeSalary,Integer> {


}
