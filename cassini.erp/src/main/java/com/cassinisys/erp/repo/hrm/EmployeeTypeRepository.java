package com.cassinisys.erp.repo.hrm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPEmployeeType;

@Repository
public interface EmployeeTypeRepository extends JpaRepository<ERPEmployeeType, Integer> {
    ERPEmployeeType findByName(String name);

}
