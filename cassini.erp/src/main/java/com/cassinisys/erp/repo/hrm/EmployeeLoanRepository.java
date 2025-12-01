package com.cassinisys.erp.repo.hrm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPEmployeeLoan;
import com.cassinisys.erp.model.hrm.ERPEmployeeTimeOff;
import com.cassinisys.erp.model.hrm.LoanStatus;

@Repository
public interface EmployeeLoanRepository extends JpaRepository<ERPEmployeeLoan,Integer>, QueryDslPredicateExecutor<ERPEmployeeLoan> {
	
	public static final String GET_LOANS_APPROVED_BY =
            "SELECT l FROM ERPEmployeeLoan l WHERE l.approvedBy= :approvedBy";
	
	public static final String GET_LOANS_OF_EMPLOYEE =
            "SELECT l FROM ERPEmployeeLoan l WHERE l.employee= :employee";

    @Query(GET_LOANS_APPROVED_BY)
	List<ERPEmployeeLoan> getLoansApprovedBy(@Param("approvedBy") Integer approvedBy);
    
    @Query(GET_LOANS_OF_EMPLOYEE)
	List<ERPEmployeeLoan> findByEmployee(@Param("employee") Integer employee);
    
    List<ERPEmployeeLoan> findByEmployeeAndStatus(@Param("employee") Integer employee,@Param("status") LoanStatus status);
}
