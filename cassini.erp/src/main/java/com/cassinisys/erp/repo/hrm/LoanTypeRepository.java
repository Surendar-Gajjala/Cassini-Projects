package com.cassinisys.erp.repo.hrm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPLoanType;

@Repository
public interface LoanTypeRepository extends JpaRepository<ERPLoanType, Integer> {

}
