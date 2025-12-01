package com.cassinisys.tm.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.tm.model.TMPersonOtherInfo;
import com.cassinisys.tm.service.EmergencyContactService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Rajabrahmachary on 10-08-2016.
 */
@Api(name = "PersonOtherInfo", description = "PersonOtherInfo endpoint")
@RestController
@RequestMapping("/personOtherInfo")
public class PersonOtherInfoController extends BaseController {

    @Autowired
    private EmergencyContactService emergencyContactService;

    @RequestMapping(method = RequestMethod.POST)
    public TMPersonOtherInfo create(@RequestBody TMPersonOtherInfo tmPersonOtherInfo) {

        return emergencyContactService.create(tmPersonOtherInfo);
    }

    @RequestMapping(value = "/{personOtherInfoId}", method = RequestMethod.PUT)
    public TMPersonOtherInfo update(@PathVariable("personOtherInfoId") Integer personOtherInfoId,
                                    @RequestBody TMPersonOtherInfo tmPersonOtherInfo) {
        return emergencyContactService.updateOtherInfo(tmPersonOtherInfo);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TMPersonOtherInfo> getAll() {
        return emergencyContactService.getAllPersonOtherInfos();
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    public TMPersonOtherInfo getByPersonId(@PathVariable("personId") Integer personId) {
        return emergencyContactService.getPersonOtherInfoByPersonId(personId);
    }

    @RequestMapping(value = "/{personId}/role", method = RequestMethod.GET)
    public TMPersonOtherInfo getRoleByPersonId(@PathVariable("personId") Integer personId) {
        return emergencyContactService.getPersonRoleByPersonId(personId);
    }

    @RequestMapping(value = "/role/{personRole}", method = RequestMethod.GET)
    public List<TMPersonOtherInfo> getPersonsByRole(@PathVariable("personRole") String personRole) {
        return emergencyContactService.getPersonsByRole(personRole);
    }

}
