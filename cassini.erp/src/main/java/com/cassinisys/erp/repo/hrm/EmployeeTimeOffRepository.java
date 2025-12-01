package com.cassinisys.erp.repo.hrm;

import java.util.Date;
import java.util.List;

import com.cassinisys.erp.model.hrm.ERPEmployeeAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPEmployeeTimeOff;
import com.cassinisys.erp.model.hrm.TimeOffStatus;

@Repository
public interface EmployeeTimeOffRepository extends
		JpaRepository<ERPEmployeeTimeOff, Integer>,
		QueryDslPredicateExecutor<ERPEmployeeTimeOff> {

	public static final String GET_EMPLOYEE_TIME_OFFS = "SELECT off FROM ERPEmployeeTimeOff off WHERE off.employeeId= :employeeId";

	public static final String GET_EMPLOYEE_TIME_OFFS_BY_ID_AND_DATE_RANGE = "select etf from ERPEmployeeTimeOff etf "
			+ "where etf.startDate between ?1 and ?2 and etf.endDate between ?1 and ?2 and etf.employeeId =?3";

	@Query(GET_EMPLOYEE_TIME_OFFS)
	List<ERPEmployeeTimeOff> getEmplyeeTimeOffs(
			@Param("employeeId") Integer employeeId);

	@Query(GET_EMPLOYEE_TIME_OFFS_BY_ID_AND_DATE_RANGE)
	List<ERPEmployeeTimeOff> getEmplyeeTimeOffsByIdAndDateRange(Date fromDate,
			Date toDate, Integer employeeId);


	@Query("select etf from ERPEmployeeTimeOff etf " +
			"where etf.startDate between ?1 and ?2 and etf.endDate between ?1 and ?2 and etf.employeeId =?3 ")
	List<ERPEmployeeTimeOff> getTimeOffeByMonthWiseforEmp(Date from,Date to,Integer employeeId);

	/*
	 * select sum(etf.num_of_days) from cassini.erp_employeetimeoff etf where
	 * etf.start_date between to_date('01/10/2015', 'dd/MM/YYYY') AND
	 * to_date('31/10/2015', 'dd/MM/YYYY') and etf.end_date between
	 * to_date('01/10/2015', 'dd/MM/YYYY') AND to_date('31/10/2015',
	 * 'dd/MM/YYYY') and etf.employee_id =1 and etf.status= 'PENDING'
	 */

	@Query("select coalesce(sum(etf.numOfDays), 0) from ERPEmployeeTimeOff etf "
			+ "where etf.startDate between ?1 and ?2 and etf.endDate between ?1 and ?2 and etf.employeeId =?3 and etf.status= ?4")
	Integer getTimeOffCountByMonthWiseforEmp(Date from, Date to,
			Integer employeeId, TimeOffStatus status);

}
