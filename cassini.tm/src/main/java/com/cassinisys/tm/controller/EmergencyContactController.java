package com.cassinisys.tm.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.tm.model.TMEmergencyContact;
import com.cassinisys.tm.service.EmergencyContactService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Rajabrahmachary on 10-08-2016.
 */
@Api(name = "EmergencyContact", description = "EmergencyContact endpoint")
@RestController
@RequestMapping("/emergencyContact")
public class EmergencyContactController extends BaseController{

    @Autowired
    private EmergencyContactService emergencyContactService;


    @RequestMapping(method = RequestMethod.POST)
    public TMEmergencyContact create(@RequestBody TMEmergencyContact tmEmergencyContact) {

        return emergencyContactService.create(tmEmergencyContact);
    }

    @RequestMapping(value = "/{emergencyContactId}", method = RequestMethod.PUT)
    public TMEmergencyContact update(@PathVariable("emergencyContactId") Integer emergencyContactId,
                                    @RequestBody TMEmergencyContact tmEmergencyContact) {
        return emergencyContactService.update(tmEmergencyContact);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TMEmergencyContact> getAll() {
        return emergencyContactService.getAll();
    }

    @RequestMapping(value="/{personId}",method = RequestMethod.GET)
    public TMEmergencyContact getByPersonId(@PathVariable("personId") Integer personId) {
        return emergencyContactService.getEmergencyContactByPersonId(personId);
    }

}
