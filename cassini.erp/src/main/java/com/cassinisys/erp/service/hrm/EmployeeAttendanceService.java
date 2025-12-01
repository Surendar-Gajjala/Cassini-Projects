package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cassinisys.erp.model.hrm.AttendanceDTO;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.model.hrm.ERPEmployeeAttendance;
import com.cassinisys.erp.model.hrm.ERPEmployeeAttendanceDTO;
import com.cassinisys.erp.repo.hrm.EmployeeAttendanceRepository;
import com.cassinisys.erp.repo.hrm.EmployeeRepository;
import com.cassinisys.erp.util.AttendenceImportHelper;

@Service
@Transactional
public class EmployeeAttendanceService {

	@Autowired
	EmployeeAttendanceRepository employeeAttendanceRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	private AttendenceImportHelper attendenceImportHelper;

	/**
	 * 
	 * @param empAttendance
	 * @return
	 */
	public ERPEmployeeAttendance saveEmployeeAttendance(
			ERPEmployeeAttendance empAttendance) {

		return employeeAttendanceRepository.save(empAttendance);

	}

	/**
	 * 
	 * @param empAttendance
	 * @return
	 */
	public ERPEmployeeAttendance update(ERPEmployeeAttendance empAttendance) {

		return null;

	}


	public Integer getAttendenceCountByMonth(Integer month,Integer year){

		int attendedDays=0;

		Date fromDate;
		Date toDate;

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);

		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.AM_PM, Calendar.AM);


		fromDate = calendar.getTime();
		//add one month
		calendar.add(Calendar.MONTH, 1);


		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		toDate = calendar.getTime();


		attendedDays=employeeAttendanceRepository.getAttendenceCountByMonthWise(fromDate,toDate);

		return attendedDays;

	}
	/**
	 * 
	 * @param employeeNum
	 * @return
	 */
	public ERPEmployeeAttendanceDTO getEmployeeAttendance(Integer employeeNum,int month,int year) {

		int day = 1;
	
		Date fromDate;
		Date toDate;

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		fromDate = cal.getTime();

		cal.add(Calendar.MONTH, 1);

		toDate = cal.getTime();
		ERPEmployee emp = employeeRepository.findByEmployeeNumber(employeeNum+"");
		
		List<ERPEmployeeAttendance> list = employeeAttendanceRepository.getAttendenceByMonthWiseforEmp(fromDate, toDate, employeeNum+"");
		
		ERPEmployeeAttendanceDTO dto = new ERPEmployeeAttendanceDTO();		
		dto.setEmpNumber(employeeNum+"");
		if(emp!=null)
		dto.setEmpName(emp.getFirstName() + " " + emp.getLastName());
		List<AttendanceDTO> l = new ArrayList<AttendanceDTO>();
		for (ERPEmployeeAttendance att: list) {
			AttendanceDTO attendance = new AttendanceDTO();
			attendance.setDate(att.getDate());
			attendance.setInTime(att.getInTime());
			attendance.setOutTime(att.getOutTime());
			attendance.setStatus(att.getStatus());			
			l.add(attendance);
		}
				
		dto.setAttendance(l);
			
			
		
		return dto;

	}

	public List<ERPEmployeeAttendance> getAttendenceDetailsByDate(String date) {

		// date with dd-mm-yyyy

		String[] datee = date.split("-");
		int day = Integer.parseInt(datee[0]);
		int month = Integer.parseInt(datee[1]);
		int year = Integer.parseInt(datee[2]);

		Date fromDate;
		Date toDate;

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		fromDate = cal.getTime();

		cal.add(Calendar.DATE, 1);

		toDate = cal.getTime();

		return employeeAttendanceRepository.getAttendenceDetailsByDate(
				fromDate, toDate);

	}

	/**
	 * 
	 * @param employeeId
	 */
	public void deleteEmployeeAttendace(Integer employeeId) {
		checkNotNull(employeeId);
		// employeeAttendanceRepository.delete(employeeId);
	}

	public String uploadAttendence(Map<String, MultipartFile> fileMap) {

		String uploaded="error";
		try {

			if(fileMap!=null && fileMap.size()>0 ){

				for (MultipartFile mFile : fileMap.values()) {

					List<ERPEmployeeAttendance> erpEmployeeAttendances=	attendenceImportHelper.init(mFile.getInputStream());

					if(erpEmployeeAttendances!=null && erpEmployeeAttendances.size()>0){
						ERPEmployee empp=employeeRepository.findOne(1);
						//logic of for loop to remove after testing in local

						for(ERPEmployeeAttendance empAtt:erpEmployeeAttendances){

							if(empAtt.getEmpNumber()!=null){
								ERPEmployee empDB=employeeRepository.findByEmployeeNumber(empAtt.getEmpNumber());
								if(!(empDB!=null && empDB.getEmployeeNumber()!=null && empDB.getEmployeeNumber().length()>0)){

										ERPEmployee emp = new ERPEmployee();
										emp.setEmployeeNumber(empAtt.getEmpNumber());
										emp.setFirstName("abc"+empAtt.getEmpNumber());
										emp.setLastName(" reddy"+empAtt.getEmpNumber());
										emp.setDateOfBirth(new Date());
										emp.setDateOfHire(new Date());
										emp.setDepartment(empp.getDepartment());
										emp.setEmployeeType(empp.getEmployeeType());
										emp.setPersonType(empp.getPersonType());
										employeeRepository.save(emp);

									}

								}
							}

						try{
						//save all records at once
						employeeAttendanceRepository.save(erpEmployeeAttendances);
						uploaded="Attendence Uploaded Successfully";
						}catch (Exception exp){

						}
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return uploaded;
	}



	public List<ERPEmployeeAttendanceDTO> getEmployeesAttendance(int month,int year) {

		int day = 1;
	
		Date fromDate;
		Date toDate;

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		fromDate = cal.getTime();

		cal.add(Calendar.MONTH, 1);

		toDate = cal.getTime();

		List<ERPEmployeeAttendance> list =  employeeAttendanceRepository.getAttendenceDetailsByDate(fromDate, toDate);
		List<ERPEmployee> empList = employeeRepository.findAll();
		
		HashMap<String, ERPEmployeeAttendanceDTO> map = new HashMap<String, ERPEmployeeAttendanceDTO>();
		for (ERPEmployeeAttendance att: list) {
			ERPEmployeeAttendanceDTO dto;
			if (map.get(att.getEmpNumber()) == null ) {
				dto = new ERPEmployeeAttendanceDTO();
				dto.setAttendance(new ArrayList<AttendanceDTO>());
				dto.setEmpNumber(att.getEmpNumber());
				for (ERPEmployee emp : empList) {
					if (att.getEmpNumber().equalsIgnoreCase(emp.getEmployeeNumber())) {
						dto.setEmpName(emp.getFirstName() + " " + emp.getLastName());
						break;
					}
					
				}
				map.put(att.getEmpNumber(), dto);
			} else {
				dto = map.get(att.getEmpNumber());
			}
			
			AttendanceDTO attendance = new AttendanceDTO();
			attendance.setDate(att.getDate());
			attendance.setInTime(att.getInTime());
			attendance.setOutTime(att.getOutTime());
			attendance.setStatus(att.getStatus());
			List<AttendanceDTO> l = new ArrayList<AttendanceDTO>();
			l.add(attendance);		
			l.addAll(dto.getAttendance());
			dto.setAttendance(l);
			
		}
		 
		ERPEmployeeAttendanceDTO[] a = new ERPEmployeeAttendanceDTO[map.keySet().size()];
		map.values().toArray(a);
		List<ERPEmployeeAttendanceDTO> lst=Arrays.asList(a);
		Collections.sort(lst);
		return lst;

	}


}
