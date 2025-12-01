package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.hrm.ERPHoliday;
import com.cassinisys.erp.repo.hrm.HolidayRepository;

@Service
@Transactional
public class HolidayService {

	@Autowired
	HolidayRepository holidayRepository;

	/**
	 * 
	 * @param holiday
	 * @return
	 */
	public ERPHoliday create(ERPHoliday holiday) {

		if (holiday != null) {

			holiday = holidayRepository.save(holiday);
		}
		return holiday;

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ERPHoliday getByDate(Date date) {

		return holidayRepository.findByDate(date);

	}

	/**
	 * 
	 * @param holiday
	 * @return
	 */
	public ERPHoliday update(ERPHoliday holiday) {
		checkNotNull(holiday);
		checkNotNull(holiday.getId());
		if (holidayRepository.findOne(holiday.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return holidayRepository.save(holiday);
	}

	public int getNumOfHolidaysInCurrentMonth(Integer month) {

		int holidays = 0;

		Date fromDate;
		Date toDate;

		Calendar calendar = Calendar.getInstance();
		month=month-1;
		calendar.set(Calendar.MONTH,month);

		calendar.set(Calendar.DATE,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		fromDate = calendar.getTime();
		// add one month
		calendar.add(Calendar.MONTH, 1);

		calendar.set(Calendar.DATE,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		toDate = calendar.getTime();

		holidays = holidayRepository.getHolidaysForTheMonth(fromDate, toDate);

		return holidays;
	}

	/**
	 * 
	 * @return List<ERPHoliday>
	 */
	public List<ERPHoliday> getAll() {

		return holidayRepository.findAll();

	}

	/**
	 * 
	 * @param id
	 */
	public void delete(Integer id) {
		checkNotNull(id);
		holidayRepository.delete(id);
	}

}
