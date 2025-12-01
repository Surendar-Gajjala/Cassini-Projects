package com.cassinisys.erp.repo.hrm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPDepartment;

@Repository
public interface DepartmentRepository extends JpaRepository<ERPDepartment, Integer> {
    ERPDepartment findByName(String name);
}
