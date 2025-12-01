package com.cassinisys.erp.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatesHelper {

	public static int getNumberOfSundaysForCurrentMonth() {

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int count = 0;
		int month = calendar.get(Calendar.MONTH);

		Calendar cal = new GregorianCalendar(year, month, 1);
		do {
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day == Calendar.SUNDAY) {
				count++;
				// System.out.println(cal.get(Calendar.DAY_OF_MONTH));
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
		} while (cal.get(Calendar.MONTH) == month);

		return count;
	}

	public static int getNumberOfSundaysForGivenYearAndMonth(int year, int month) {

		int count = 0;
		Calendar cal = new GregorianCalendar(year, month, 1);
		do {
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day == Calendar.SUNDAY) {
				count++;
				// System.out.println(cal.get(Calendar.DAY_OF_MONTH));
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
		} while (cal.get(Calendar.MONTH) == month);

		return count;
	}

}
