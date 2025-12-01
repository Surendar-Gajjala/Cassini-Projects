package com.cassinisys.tms.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.tms.model.TMSTrip;
import com.cassinisys.tms.service.TripService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by CassiniSystems on 31-10-2016.
 */

@Api(name = "Trip",
		description = "Trip endpoint")
@RestController
@RequestMapping("/trips")
public class TripController extends BaseController {

	@Autowired
	private TripService tripService;
	@Autowired
	PageRequestConverter pageRequestConverter;

	@RequestMapping(method = RequestMethod.POST)
	public TMSTrip create(@RequestBody TMSTrip trip){
		return  tripService.create(trip);
	}

	@RequestMapping(value = "/{tripId}",method = RequestMethod.PUT)
	public  TMSTrip update(@PathVariable("tripId") Integer tripId, @RequestBody TMSTrip trip){
		trip.setId(tripId);
		return tripService.update(trip);
	}

	@RequestMapping(value = "/{tripId}", method = RequestMethod.GET)
	public  TMSTrip get(@PathVariable("tripId") Integer tripId){
		return tripService.get(tripId);
	}

	@RequestMapping(value = "/{tripId}", method = RequestMethod.DELETE)
	public  void delete(@PathVariable("tripId") Integer tripId){
		tripService.delete(tripId);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<TMSTrip> getAll(){
		return  tripService.getAll();
	}

	@RequestMapping(value = "/pageable", method = RequestMethod.GET)
	public Page<TMSTrip> getAll(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return tripService.findAll(pageable);
	}

}
