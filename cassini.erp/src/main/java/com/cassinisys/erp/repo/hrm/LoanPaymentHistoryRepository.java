package com.cassinisys.erp.repo.hrm;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPLoanPaymentHistory;

@Repository
public interface LoanPaymentHistoryRepository extends JpaRepository<ERPLoanPaymentHistory, ERPLoanPaymentHistory.LoanPaymentHistoryId> {
	
	/*public static final String GET_LOAN_PAYMENT_ID =
            "SELECT lp FROM ERPLoanPaymentHistory lp WHERE lp.loanId= :loanId";
*/	
	public static final String GET_LOAN_PAYMENTS_BETWEEN =
            "SELECT lp FROM ERPLoanPaymentHistory lp WHERE lp.paymentDate BETWEEN  :fromDate AND :toDate";
	
	public static final String GET_LOAN_PAYMENTS_PAYROLLANDEMPID =
            "SELECT lp FROM ERPLoanPaymentHistory lp WHERE lp.payrollId=? AND lp.employeeId=?";
	
	
	/*public static final String GET_LOAN_PAYMENTS_BY_ID_BETWEEN =
            "SELECT lp FROM ERPLoanPaymentHistory lp WHERE lp.loanId= :loanId AND lp.paymentDate BETWEEN  :fromDate AND :toDate";
*/
   // @Query(GET_LOAN_PAYMENT_ID)
	List<ERPLoanPaymentHistory> findByLoanPaymentHistoryIdLoanId( Integer loanId);
    
    @Query(GET_LOAN_PAYMENTS_BETWEEN)
   	List<ERPLoanPaymentHistory> getLoanPaymentsByDateRange(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
    
   /* @Query(GET_LOAN_PAYMENTS_BY_ID_BETWEEN)
   	List<ERPLoanPaymentHistory> getLoanPaymentsByIdAndDateRange(@Param("loanId") Integer loanId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
*/
    List<ERPLoanPaymentHistory> findByLoanPaymentHistoryIdPayrollIdAndLoanPaymentHistoryIdEmployeeId(
			Integer payrollId, Integer employeeId);

    
}
	