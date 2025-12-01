package com.cassinisys.erp.repo.hrm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPDeductionType;

@Repository
public interface DeductionTypeRepository extends JpaRepository<ERPDeductionType, Integer> {

}
