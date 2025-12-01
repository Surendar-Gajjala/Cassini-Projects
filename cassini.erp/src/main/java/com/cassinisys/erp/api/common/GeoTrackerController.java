package com.cassinisys.erp.api.common;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.common.ERPGeoTracker;
import com.cassinisys.erp.service.common.GeoTrackerService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by reddy on 10/10/15.
 */
@RestController
@RequestMapping("common/geotracker")
@Api(name="Geo Tracker",description="Geo tracker endpoint",group="COMMON")
public class GeoTrackerController extends BaseController {

    @Autowired
    private GeoTrackerService geoTrackerService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public List<ERPGeoTracker> getEmployeeGeoLocationsByDate(@PathVariable("id") Integer employeeId,
                      @RequestParam("date") @DateTimeFormat(pattern="dd-MM-yyyy") Date date) {
        return geoTrackerService.getEmployeeGeoLocationsByDate(employeeId, date);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ERPGeoTracker addGeoLocation(@PathVariable("id") Integer employeeId,
                @RequestBody ERPGeoTracker geoTracker) {
        geoTracker.setEmployee(employeeId);
        if(geoTracker.getTimestamp() != null) {
            geoTracker.setTimestamp(new Date());
        }
        return geoTrackerService.addGeoLocation(geoTracker);
    }

    @RequestMapping(value = "/{id}/batch", method = RequestMethod.POST)
    public List<ERPGeoTracker> addGeoLocations(@PathVariable("id") Integer employeeId,
                @RequestBody List<ERPGeoTracker> geoTrackers) {
        for(ERPGeoTracker geoTracker : geoTrackers) {
            geoTracker.setEmployee(employeeId);
            if (geoTracker.getTimestamp() == null) {
                geoTracker.setTimestamp(new Date());
            }
        }
        return geoTrackerService.addGeoLocations(geoTrackers);
    }
}
