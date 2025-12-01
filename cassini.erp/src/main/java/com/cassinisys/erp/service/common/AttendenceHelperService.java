package com.cassinisys.erp.service.common;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.common.AttendenceHelperDTO;
import com.cassinisys.erp.util.DatesHelper;

@Service
public class AttendenceHelperService {

	public AttendenceHelperDTO getMonthDetails() {

		Integer numOfSundays = DatesHelper.getNumberOfSundaysForCurrentMonth();

		Calendar calendar = Calendar.getInstance();
		int month=calendar.get(Calendar.MONTH);
		month=month-1;
		calendar.set(Calendar.MONTH,month);

		Integer numOfDaysInMonth = calendar
				.getActualMaximum(Calendar.DAY_OF_MONTH);

		return new AttendenceHelperDTO(numOfSundays, numOfDaysInMonth);
	}

}
