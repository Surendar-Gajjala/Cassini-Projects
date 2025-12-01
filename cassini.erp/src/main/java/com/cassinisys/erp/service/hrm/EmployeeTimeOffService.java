package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.model.hrm.ERPEmployeeTimeOff;
import com.cassinisys.erp.model.hrm.ERPHoliday;
import com.cassinisys.erp.model.hrm.ERPTimeOffType;
import com.cassinisys.erp.model.hrm.TimeOffStatus;
import com.cassinisys.erp.repo.hrm.EmployeeRepository;
import com.cassinisys.erp.repo.hrm.EmployeeTimeOffRepository;
import com.cassinisys.erp.repo.hrm.HolidayRepository;
import com.cassinisys.erp.repo.hrm.TimeOffTypeRepository;
import com.mysema.query.types.Predicate;

@Service
@Transactional
public class EmployeeTimeOffService {

	@Autowired
	EmployeeTimeOffRepository employeeTimeOffRepository;

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	HolidayRepository holidayRepository;
	
	@Autowired
	TimeOffTypeRepository timeOffTypeRepository;
	

	/**
	 * 
	 * @param empTimeOff
	 * @return
	 */
	public ERPEmployeeTimeOff saveEmployeeTimeOff(ERPEmployeeTimeOff empTimeOff) {
		
			return 	employeeTimeOffRepository.save(empTimeOff);

	}

	/**
	 * 
	 * @param empTimeOff
	 * @return
	 */
	/*public ERPEmployeeTimeOff updateEmployeeTimeOff(ERPEmployeeTimeOff empTimeOff) {

		checkNotNull(empTimeOff);
		checkNotNull(empTimeOff.getLoanId());
		if (loanPaymentsRepository.getLoanPaymentById(empTimeOff.getLoanId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return employeeTimeOffRepository.save(empTimeOff);

	}*/

	/**
	 * 
	 * @param employeeId
	 * @return
	 */
	public List<ERPEmployeeTimeOff> getEmployeeTimeOff(Integer employeeId) {
		checkNotNull(employeeId);
		return employeeTimeOffRepository.getEmplyeeTimeOffs(employeeId);

	}

	/**
	 * 
	 * @param employeeId
	 */
	public void deleteEmployeeTimeOff(Integer employeeId) {
		checkNotNull(employeeId);
		//employeeTimeOffRepository.delete(typeId);
	}

	public List<ERPEmployeeTimeOff> getEmployeeTimeOffsByMonthAndYear(Integer employeeId, Integer month, Integer year) {

		int day=1;
		Date fromDate;
		Date toDate;

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		fromDate = cal.getTime();

		cal.add(Calendar.MONTH, 1);

		toDate = cal.getTime();

		return employeeTimeOffRepository.getTimeOffeByMonthWiseforEmp(fromDate, toDate, employeeId);

	}


	/**
	 * @param employeeId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<ERPEmployeeTimeOff> getEmployeeTimeOffsIdAndDateRange(Integer employeeId, Date fromDate, Date toDate) {
				
		checkNotNull(fromDate);
		checkNotNull(toDate);
		return employeeTimeOffRepository.getEmplyeeTimeOffsByIdAndDateRange(fromDate, toDate,employeeId);		 

	}
	
	public ERPEmployeeTimeOff approveTimeOff(Integer id){
		
		ERPEmployeeTimeOff timeOff=employeeTimeOffRepository.findOne(id);
		
		if(timeOff!=null){
			timeOff.setStatus(TimeOffStatus.APPROVED);
			timeOff= employeeTimeOffRepository.save(timeOff);
		}
		return timeOff;
	}
	
	public Integer getValidTimeOffCountDaysBetweenDates(Date from,Date to){
		
		int validOffDays=0;
		
		int numOfDays=(int)( (to.getTime() - from.getTime()) / (1000 * 60 * 60 * 24));
		
		Calendar cal =Calendar.getInstance();
		
		cal.setTime(from);

		//below condition is keeping <= because numOfDys not considering to Date in count;
		for(int i=0;i<=numOfDays;i++){
			//TODO
		
			//calculate date is a sunday or holiday
			if(i!=0)
			cal.add(Calendar.DATE, 1);
					
			if(cal.get(Calendar.DAY_OF_WEEK)==1){
				continue;
			}
		
			String DATEFORMAT = "dd/MM/yyyy";
			SimpleDateFormat format = new SimpleDateFormat(DATEFORMAT);
			Date date = cal.getTime();
			try {
				String d=format.format(date);
				
				date= format.parse(d);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		
			ERPHoliday holiday=holidayRepository.findByDate(date);
			if(holiday!=null && holiday.getId()>0){
				continue;
			}
			validOffDays++;
			
			
		}
		
		return validOffDays;
		
	}
	
	 public Page<ERPEmployeeTimeOff> findAll(Predicate predicate, Pageable pageable) {
		 	Page<ERPEmployeeTimeOff> l = employeeTimeOffRepository.findAll(predicate, pageable);
		 	List<ERPEmployee> empList = employeeRepository.findAll();
		 	Map<Integer, String> empMap = new HashMap<Integer, String>();
		 	Map<Integer, String> typesMap = new HashMap<Integer, String>();
		 	List<ERPTimeOffType> types = timeOffTypeRepository.findAll();
		 	for (ERPEmployee emp:empList) {
		 		empMap.put(emp.getId(), emp.getFirstName() + emp.getLastName() != null ? " " + emp.getLastName() : "");
		 	}
		 	for (ERPTimeOffType type: types) {
		 		typesMap.put(type.getId(), type.getName());
		 	}
		 	for (ERPEmployeeTimeOff obj: l) {
		 		obj.setEmpName(empMap.get(obj.getEmployeeId()));
		 		obj.setLeaveType(typesMap.get(obj.getTimeOffType()));
		 	}
	        return l;
	    }
	

	
	
}
