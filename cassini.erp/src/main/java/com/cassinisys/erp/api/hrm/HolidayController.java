package com.cassinisys.erp.api.hrm;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.hrm.ERPHoliday;
import com.cassinisys.erp.service.hrm.HolidayService;

@RestController
@RequestMapping("hrm/holidays")
@Api(name = "Holidays", description = "Holidays endpoint", group = "HRM")
public class HolidayController extends BaseController {

	@Autowired
	private HolidayService holidayService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPHoliday createHoliday(@RequestBody @Valid ERPHoliday holiday,
			HttpServletRequest request, HttpServletResponse response) {

		return holidayService.create(holiday);

	}

	@RequestMapping(value = "/{date}", method = RequestMethod.GET)
	public ERPHoliday getHolidayByDate(@PathVariable("date") Date date) {

		return holidayService.getByDate(date);

	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public ERPHoliday update(@PathVariable Integer id,
			@RequestBody ERPHoliday holiday) {
		holiday.setId(id);
		return holidayService.update(holiday);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPHoliday> getAllHolidays() {

		return holidayService.getAll();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer id) {

		holidayService.delete(id);
	}

	@RequestMapping(value = "/month", method = RequestMethod.GET)
	public Integer getHolidaysForMonth() {

		int month=new Date().getMonth();
		return holidayService.getNumOfHolidaysInCurrentMonth(month);

	}

}
