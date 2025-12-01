package com.cassinisys.erp.repo.hrm;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPEmployeeAttendance;

@Repository
public interface EmployeeAttendanceRepository extends JpaRepository<ERPEmployeeAttendance, Integer> {
	
	public static final String GET_EMPLOYEE_ATTENDANCE =
            "SELECT ea FROM ERPEmployeeAttendance ea WHERE ea.empNumber= :empNumber";
	
	
/*	select count(in_time) from cassini_erp.ERP_EMPLOYEEATTENDANCE where
	 in_time between to_date('01/10/2015', 'dd/MM/YYYY') AND 
	to_date('06/10/2015', 'dd/MM/YYYY') and employee_id=1 group by employee_id*/
	
    @Query(GET_EMPLOYEE_ATTENDANCE)
	List<ERPEmployeeAttendance> getEmployeeAttendance(@Param("empNumber") String empNumber);
    
    @Query("select coalesce(count(ea.inTime), 0) from ERPEmployeeAttendance ea " +
            "where ea.inTime between ?1 and ?2 and ea.empNumber =?3 ")
    Integer getAttendenceCountByMonthWiseforEmp(Date from,Date to,String empNumber);
    
    @Query("select ea from ERPEmployeeAttendance ea " +
            "where ea.date between ?1 and ?2 and ea.empNumber =?3 ")
    List<ERPEmployeeAttendance> getAttendenceByMonthWiseforEmp(Date from,Date to,String empNumber);
 
    
    @Query("SELECT ea FROM ERPEmployeeAttendance ea " +
            "WHERE ea.date BETWEEN ?1 AND ?2 ORDER BY ea.empNumber ASC, ea.date ASC")
    List<ERPEmployeeAttendance> getAttendenceDetailsByDate(Date from,Date to);

    @Query("select coalesce(count(ea.date), 0) from ERPEmployeeAttendance ea " +
            "where ea.inTime between ?1 and ?2")
    Integer getAttendenceCountByMonthWise(Date from,Date to);




}
