package com.cassinisys.tm.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.tm.filtering.ProjectTaskCriteria;
import com.cassinisys.tm.filtering.ShiftCriteria;
import com.cassinisys.tm.filtering.ShiftPersonCriteria;
import com.cassinisys.tm.model.TMProjectTask;
import com.cassinisys.tm.model.TMShift;
import com.cassinisys.tm.model.TMShiftPerson;
import com.cassinisys.tm.service.ShiftService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Rajabrahmachary on 21-07-2016.
 */
@Api(name = "Shift",
        description = "Shift endpoint")
@RestController
@RequestMapping("/shifts")
public class ShiftController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ShiftService shiftService;

    @RequestMapping(method = RequestMethod.POST)
    public TMShift create(@RequestBody TMShift tmShift) {
        return shiftService.create(tmShift);
    }

    @RequestMapping(value = "/{shiftId}", method = RequestMethod.PUT)
    public TMShift update(@PathVariable("shiftId") Integer shiftId,
                          @RequestBody TMShift tmShift) {
        return shiftService.update(tmShift);
    }

    @RequestMapping(value = "/{shiftId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("shiftId") Integer shiftId) {
        shiftService.delete(shiftId);
    }

    @RequestMapping(value = "/{shiftId}", method = RequestMethod.GET)
    public TMShift get(@PathVariable("shiftId") Integer shiftId) {
        return shiftService.get(shiftId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TMShift> getAllShifts() {
        return shiftService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<TMShift> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return shiftService.findAll(pageable);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<TMShift> getMultiple(@PathVariable Integer[] ids) {
        return shiftService.findMultipleShifts(Arrays.asList(ids));
    }

    @RequestMapping( value = "/{shiftId}/persons",method = RequestMethod.POST)
    public TMShiftPerson create(@RequestBody TMShiftPerson tmShiftPerson) {
        return shiftService.create(tmShiftPerson);
    }

    @RequestMapping(value = "/{shiftId}/persons/{personId}", method = RequestMethod.PUT)
    public TMShiftPerson update(@PathVariable("personId") Integer personId,
                                @RequestBody TMShiftPerson tmShiftPerson) {
        return shiftService.update(tmShiftPerson);
    }


    @RequestMapping(value = "/{shiftId}/persons/{personId}", method = RequestMethod.DELETE)
    public void deleteShiftPerson(@PathVariable("shiftId") Integer shiftId,
                                  @PathVariable("personId") Integer personId) {
        shiftService.deleteShiftPerson(personId, shiftId);
    }

    @RequestMapping(value = "/{shiftId}/persons/{personId}", method = RequestMethod.GET)
    public TMShiftPerson getShift(@PathVariable("personId") Integer personId) {
        return shiftService.getShift(personId);
    }

    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public List<TMShiftPerson> getAllShiftPersons() {
        return shiftService.getAllShifts();
    }

    @RequestMapping(value = "/persons/all", method = RequestMethod.GET)
    public Page<TMShiftPerson> getAllShiftPersons(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return shiftService.findAllShifts(pageable);
    }

    @RequestMapping(value = "/{shiftId}/persons", method = RequestMethod.GET)
    public List<TMShiftPerson> getPersonsBasedOnShift(@PathVariable("shiftId") Integer shiftId) {
        return shiftService.getPersonsBasedOnShift(shiftId);
    }

    @RequestMapping(value = "/{shiftId}/persons", method = RequestMethod.PUT)
    public List<TMShiftPerson> savePersonsBasedOnShift(@PathVariable("shiftId") Integer shiftId,
                                                       @RequestBody TMShiftPerson[] persons) {
        return shiftService.savePersonsBasedOnShift(Arrays.asList(persons));
    }

    @RequestMapping(value = "/persons/pageable", method = RequestMethod.GET)
    public Page<TMShiftPerson> getAllShiftPersons(ShiftPersonCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);

        return shiftService.findAll(criteria, pageable);
    }

}
