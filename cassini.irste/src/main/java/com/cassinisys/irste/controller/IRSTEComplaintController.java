package com.cassinisys.irste.controller;

import com.cassinisys.irste.filtering.ComplaintCriteria;
import com.cassinisys.irste.model.IRSTEComplaint;
import com.cassinisys.irste.model.IRSTEComplaintHistory;
import com.cassinisys.irste.service.IRSTEComplaintHistoryService;
import com.cassinisys.irste.service.IRSTEComplaintService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Nageshreddy on 06-11-2018.
 */
@RestController
@RequestMapping("/irste/complaints")
public class IRSTEComplaintController extends BaseController {

    @Autowired
    private IRSTEComplaintService complaintService;

    @Autowired
    private IRSTEComplaintHistoryService complaintHistoryService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public IRSTEComplaint create(@RequestBody IRSTEComplaint complaint) {
        return complaintService.create(complaint);
    }


    @RequestMapping(value = "/{id}/finishComplaint", method = RequestMethod.PUT)
    public IRSTEComplaint finishComplaint(@PathVariable("id") Integer id, @RequestBody IRSTEComplaint complaint) {
         complaint.setId(id);
        return complaintService.finishComplaint(complaint);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public IRSTEComplaint update(@PathVariable("id") Integer id, @RequestBody IRSTEComplaint complaint) {
        complaint.setId(id);
        return complaintService.update(complaint);
    }

    @RequestMapping(value = "/status/{id}", method = RequestMethod.PUT)
    public IRSTEComplaint updateStatus(@PathVariable("id") Integer id, @RequestBody IRSTEComplaint complaint) {
        complaint.setId(id);
        return complaintService.updateStatus(complaint);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void create(@PathVariable("id") Integer id) {
        complaintService.delete(id);
    }

    @RequestMapping(value ="/all/facilitators", method = {RequestMethod.GET})
    public List<Person> getAllFacilitators() {
        return complaintService.getAllFacilitators();
    }

    @RequestMapping(value = "/all/assistors",
            method = {RequestMethod.GET}
    )
    public List<Person>  getAllAssistors() {
        return complaintService.getAllAssistors();
    }

    @RequestMapping(value = "/freeTextSearch", method = RequestMethod.GET)
    public Page<IRSTEComplaint> freeTextSearch(PageRequest pageRequest, ComplaintCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        criteria.setFreeTextSearch(true);
        return complaintService.freeTextSearch(criteria, pageable);
    }

    @RequestMapping(value = "/filters", method = RequestMethod.GET)
    public Page<IRSTEComplaint> findComplaintsByFilter(PageRequest pageRequest, ComplaintCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return complaintService.getComplaintsByFilter(criteria, pageable);
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<IRSTEComplaint> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return complaintService.findAll(pageable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        complaintService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public IRSTEComplaint get(@PathVariable("id") Integer id) {
        return complaintService.get(id);
    }


    @RequestMapping(value = "/number/{number}", method = RequestMethod.GET)
    public IRSTEComplaint getByComplaintNumber(@PathVariable("number") String number) {
        return complaintService.getByComplaintNumber(number);
    }

    @RequestMapping(value = "/responder/{person}/pageable", method = RequestMethod.GET)
    public Page<IRSTEComplaint> getByPerson(PageRequest pageRequest, @PathVariable("person") Integer person) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return complaintService.getByResponder(person, pageable);
    }

    @RequestMapping(value = "/history/complaint/{id}", method = RequestMethod.GET)
    public List<IRSTEComplaintHistory> getHistoryByComplaint(@PathVariable("id") Integer id) {
        return complaintHistoryService.getByComplaintId(id);
    }

    @RequestMapping(value = "/all/responder/{responder}", method = RequestMethod.GET)
    public List<IRSTEComplaint> getAllComplaintsByResponder(@PathVariable("responder") Integer responder) {
        return complaintService.getAllComplaintsByResponder(responder);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<IRSTEComplaint> getAllComplaints() {
        return complaintService.getAll();
    }

    @RequestMapping(value = "/{fileType}/report", method = RequestMethod.GET,
            produces = "text/html")
    public String getTaskReport(@PathVariable("fileType") String fileType, HttpServletResponse response) {
        return complaintService.exportTaskReport(fileType, response);
    }
}
