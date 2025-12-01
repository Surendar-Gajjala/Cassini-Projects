package com.cassinisys.erp.repo.hrm;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPHoliday;

@Repository
public interface HolidayRepository extends JpaRepository<ERPHoliday, Integer> {
 
	
	  @Query("select coalesce(count(h.date), 0) from ERPHoliday h " +
	            "where h.date between ?1 and ?2 ")
	    Integer getHolidaysForTheMonth(Date from,Date to);
	  
	  ERPHoliday findByDate(@Param("date") Date date);
		
}