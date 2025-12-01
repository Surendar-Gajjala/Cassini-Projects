package com.cassinisys.irste.controller;

import com.cassinisys.irste.model.IRSTEGroupLocation;
import com.cassinisys.irste.model.IRSTELocation;
import com.cassinisys.irste.model.IRSTEUtilityLocation;
import com.cassinisys.irste.service.IRSTELocationService;
import com.cassinisys.platform.api.core.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Nageshreddy on 13-11-2018.
 */
@RestController
@RequestMapping(value = "/irste/groups")
public class IRSTELocationController extends BaseController {

    @Autowired
    private IRSTELocationService locationService;

    public IRSTELocationController() {
    }

    @RequestMapping(
            method = {RequestMethod.GET}
    )
    public List<IRSTEGroupLocation> getAll() {
        return locationService.getAll();
    }

    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.GET}
    )
    public IRSTEGroupLocation get(@PathVariable("id") Integer id) {
        return locationService.get(id);
    }

    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.DELETE}
    )
    public void delete(@PathVariable("id") Integer id) {
        locationService.delete(id);
    }

    @RequestMapping(
            method = {RequestMethod.POST}
    )
    public IRSTEGroupLocation save(@RequestBody IRSTEGroupLocation group) {
        return locationService.create(group);
    }

    @RequestMapping(
            value = {"{id}"},
            method = {RequestMethod.PUT}
    )
    public IRSTEGroupLocation update(@PathVariable("id") Integer id, @RequestBody IRSTEGroupLocation group) {
        group.setId(id);
        return locationService.update(group);
    }

    @RequestMapping(
            value = {"/locations/multiple/[{ids}]"},
            method = {RequestMethod.GET}
    )
    public List<IRSTELocation> getLocationsMultiple(@PathVariable("ids") Integer[] ids) {
        return locationService.getLocationsMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/locationUtilities", method = RequestMethod.GET)
    public List<IRSTEUtilityLocation> getUtilityLocations() {
        return locationService.getIRSTEUtilityLocations();
    }

    @RequestMapping(value = "/locationUtilities/{utility}/{location}", method = RequestMethod.POST)
    public IRSTEUtilityLocation addUtilityLocation(@PathVariable("utility") String utility, @PathVariable("location") Integer location) {
        utility = utility.replace("%2F", "/");
        return locationService.addUtilityLocation(utility, location);
    }

    @RequestMapping(value = "/locationUtilities/{utility}/{location}", method = RequestMethod.DELETE)
    public void deleteUtilityLocation(@PathVariable("utility") String utility, @PathVariable("location") Integer location) {
        utility = utility.replace("%2F", "/");
        locationService.deleteUtilityLocation(utility, location);
    }

}
