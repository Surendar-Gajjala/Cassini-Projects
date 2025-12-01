package com.cassinisys.irste.controller;

import com.cassinisys.irste.filtering.ComplaintCriteria;
import com.cassinisys.irste.filtering.UserCriteria;
import com.cassinisys.irste.model.IRSTEComplaint;
import com.cassinisys.irste.model.IRSTEUser;
import com.cassinisys.irste.model.IRSTEUserUtilities;
import com.cassinisys.irste.service.IRSTEUserService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Nageshreddy on 19-11-2018.
 */
@RestController
@RequestMapping("/irste/users")
public class IRSTEUserController extends BaseController {

    @Autowired
    private IRSTEUserService userService;


    @Autowired
    private PageRequestConverter pageRequestConverter;


    @RequestMapping(method = RequestMethod.POST)
    public IRSTEUser createResponder(@RequestBody IRSTEUser responder) {
        return userService.createResponder(responder);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteResponder(@PathVariable("id") Integer id) {
        userService.deleteResponder(id);
    }

    @RequestMapping(value = "/complainant", method = RequestMethod.POST)
    public IRSTEUser createComplainant(@RequestBody IRSTEUser responder) {
        return userService.createComplainant(responder);
    }

    @RequestMapping(value = "{responderId}/utilities", method = RequestMethod.POST)
    public List<IRSTEUserUtilities> createResponderUtilities(@PathVariable("responderId") Integer responderId,
                                                             @RequestBody String[] utilities) {
        return userService.createResponderUtilities(responderId, utilities);
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<IRSTEUser> getPageableResponders(PageRequest pageRequest) {
        return userService.getResponders(pageRequest);
    }

    @RequestMapping(value = "/all/{personType}", method = RequestMethod.GET)
    public List<IRSTEUser> getAllResponders(@PathVariable("personType") String personType) {
        return userService.getAllResponders(personType);
    }
/*
    @RequestMapping(value ="/all/facilitators", method = {RequestMethod.GET})
    public List<Person> getAllFacilitators() {
        return userService.getAllFacilitators();
    }

    @RequestMapping(value = "/all/assistors",
            method = {RequestMethod.GET}
    )
    public List<Person>  getAllAssistors() {
        return userService.getAllAssistors();
    }*/

    @RequestMapping(value = "/responderUtilities/{utility}/{responder}/{personType}", method = RequestMethod.POST)
    public IRSTEUserUtilities addResponderUtility(@PathVariable("utility") String utility, @PathVariable("responder") Integer responder,
                                                  @PathVariable("personType") String personType) {
        utility = utility.replace("%2F", "/");
        return userService.addResponderUtility(utility, responder, Integer.parseInt(personType));
    }

    @RequestMapping(value = "/responderUtilities/{utility}/{responder}", method = RequestMethod.DELETE)
    public void deleteResponderUtility(@PathVariable("utility") String utility, @PathVariable("responder") Integer responder) {
        utility = utility.replace("%2F", "/");
        userService.deleteResponderUtility(utility, responder);
    }

    @RequestMapping(value = "/users/pageable/filters", method = RequestMethod.GET)
    public Page<IRSTEUser> getFilterUsers(PageRequest pageRequest, UserCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return userService.getFilterUsers(pageRequest, criteria);
    }

    @RequestMapping(value = "/responderUtilities/{personType}/{utility}/byUtility", method = RequestMethod.GET)
    public List<Integer> getByUtilityAndPersonType(@PathVariable("personType") Integer personType, @PathVariable("utility") String utility) {
        utility = utility.replace("%2F", "/");
        return userService.getByUtilityAndPersonType(utility, personType);
    }
    @RequestMapping(value = "/freeTextSearch", method = RequestMethod.GET)
    public Page<IRSTEUser> freeTextSearch(PageRequest pageRequest, UserCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        criteria.setFreeTextSearch(true);
        return userService.freeTextSearch(criteria, pageable);
    }

   /* @RequestMapping(value = "/filters", method = RequestMethod.GET)
    public Page<IRSTEUser> getFilterUsers(PageRequest pageRequest, UserCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return userService.getFilterUsers(criteria, pageable);
    }*/

}
