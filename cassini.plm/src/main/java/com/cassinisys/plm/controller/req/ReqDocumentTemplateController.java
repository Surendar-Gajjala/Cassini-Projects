package com.cassinisys.plm.controller.req;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ReqDocTemplateCriteria;
import com.cassinisys.plm.model.req.PLMRequirementDocumentTemplate;
import com.cassinisys.plm.model.req.PLMRequirementDocumentTemplateReviewer;
import com.cassinisys.plm.model.req.PLMRequirementTemplate;
import com.cassinisys.plm.model.req.PLMRequirementTemplateReviewer;
import com.cassinisys.plm.service.req.ReqDocTemplateService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Lenovo on 25-06-2021.
 */
@RestController
@RequestMapping("/req/documents/template")
@Api(tags = "PLM.PGC", description = "PGC Related")
public class ReqDocumentTemplateController extends BaseController {

    @Autowired
    private ReqDocTemplateService reqDocTemplateService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    //-------------------ReqDocTemplate------------------------------------
    @RequestMapping(method = RequestMethod.POST)
    public PLMRequirementDocumentTemplate createReqDocTemplate(@RequestBody PLMRequirementDocumentTemplate plmRequirementDocumentTemplate) {
        return reqDocTemplateService.create(plmRequirementDocumentTemplate);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMRequirementDocumentTemplate updateReqDocTemplate(@PathVariable("id") Integer id,
                                                               @RequestBody PLMRequirementDocumentTemplate plmRequirementDocumentTemplate) {
        return reqDocTemplateService.update(plmRequirementDocumentTemplate);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteReqDocTemplate(@PathVariable("id") Integer id) {
        reqDocTemplateService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMRequirementDocumentTemplate getReqDocTemplate(@PathVariable("id") Integer id) {
        return reqDocTemplateService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMRequirementDocumentTemplate> getAllReqDocTemplates() {
        return reqDocTemplateService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<PLMRequirementDocumentTemplate> getAllDocumentTemplates(PageRequest pageRequest, ReqDocTemplateCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return reqDocTemplateService.getAllDocumentTemplates(pageable, criteria);
    }


//---------------------------------PLMRequirementTemplate --------------------------------

    @RequestMapping(value = "/requirement", method = RequestMethod.POST)
    public PLMRequirementTemplate createRequirementTemplate(@RequestBody PLMRequirementTemplate plmRequirementTemplate) {
        return reqDocTemplateService.createRequirementTemplate(plmRequirementTemplate);
    }

    @RequestMapping(value = "/requirement/{id}", method = RequestMethod.PUT)
    public PLMRequirementTemplate updateReqTemplate(@PathVariable("id") Integer id,
                                                    @RequestBody PLMRequirementTemplate plmRequirementTemplate) {
        return reqDocTemplateService.updateReqTemplate(plmRequirementTemplate);
    }

    @RequestMapping(value = "/requirement/{id}", method = RequestMethod.DELETE)
    public void deleteReqTemplate(@PathVariable("id") Integer id) {
        reqDocTemplateService.deleteReqTemplate(id);
    }

    @RequestMapping(value = "/requirement/{id}", method = RequestMethod.GET)
    public PLMRequirementTemplate getReqTemplate(@PathVariable("id") Integer id) {
        return reqDocTemplateService.getReqTemplate(id);
    }

    @RequestMapping(value = "/requirement", method = RequestMethod.GET)
    public List<PLMRequirementTemplate> getAllReqTemplates() {
        return reqDocTemplateService.getAllReqTemplates();
    }

    //---------------------------------PLMRequirementTemplateReviewer--------------------------------
    @RequestMapping(value = "/requirement/{id}/reviewer", method = RequestMethod.POST)
    public PLMRequirementTemplateReviewer createRequirementTemplateReviewer(@PathVariable("id") Integer id, @RequestBody PLMRequirementTemplateReviewer plmRequirementDocumentTemplateReviewer) {
        return reqDocTemplateService.createRequirementTemplateReviewer(id, plmRequirementDocumentTemplateReviewer);
    }

    @RequestMapping(value = "/requirement/{id}/reviewer/{reviewerId}", method = RequestMethod.PUT)
    public PLMRequirementTemplateReviewer updateRequirementTemplateReviewer(@PathVariable("id") Integer id,
                                                                            @RequestBody PLMRequirementTemplateReviewer requirementTemplateReviewer) {
        return reqDocTemplateService.updateRequirementTemplateReviewer(id, requirementTemplateReviewer);
    }

    @RequestMapping(value = "/requirement/{id}/reviewer/{reviewerId}", method = RequestMethod.DELETE)
    public void deleteRequirementTemplateReviewer(@PathVariable("id") Integer id, @PathVariable("reviewerId") Integer reviewerId) {
        reqDocTemplateService.deleteRequirementTemplateReviewer(reviewerId);
    }

    @RequestMapping(value = "/requirement/{id}/reviewers", method = RequestMethod.GET)
    public List<PLMRequirementTemplateReviewer> getAllRequirementTemplateReviewers(@PathVariable("id") Integer id) {
        return reqDocTemplateService.getAllRequirementTemplateReviewers(id);
    }


    //-------------PLMRequirementDocumentTemplateReviewer--------------------------
    @RequestMapping(value = "/{id}/reviewer", method = RequestMethod.POST)
    public PLMRequirementDocumentTemplateReviewer createReqDocTemplateReviewer(@PathVariable("id") Integer id, @RequestBody PLMRequirementDocumentTemplateReviewer plmRequirementDocumentTemplateReviewer) {
        return reqDocTemplateService.addReqDocTemplateReviewer(id, plmRequirementDocumentTemplateReviewer);
    }

    @RequestMapping(value = "/{id}/reviewer/{reviewerId}", method = RequestMethod.PUT)
    public PLMRequirementDocumentTemplateReviewer updateReqDocTemplateReviewer(@PathVariable("id") Integer id,
                                                                               @RequestBody PLMRequirementDocumentTemplateReviewer plmRequirementDocumentTemplateReviewer) {
        return reqDocTemplateService.updateReqDocTemplateReviewer(id, plmRequirementDocumentTemplateReviewer);
    }

    @RequestMapping(value = "/{id}/reviewer/{reviewerId}", method = RequestMethod.DELETE)
    public void deleteReqDocTemplateReviewer(@PathVariable("id") Integer id, @PathVariable("reviewerId") Integer reviewerId) {
        reqDocTemplateService.deleteReqDocTemplateReviewer(reviewerId);
    }

    @RequestMapping(value = "/{id}/reqDocTemReviewer", method = RequestMethod.GET)
    public PLMRequirementDocumentTemplateReviewer getReqDocTemplateReviewer(@PathVariable("id") Integer id) {
        return reqDocTemplateService.getReqDocTemplateReviewer(id);
    }

    @RequestMapping(value = "/{id}/reqDocTemReviewer/all", method = RequestMethod.GET)
    public List<PLMRequirementDocumentTemplateReviewer> getAllReqDocTemplateReviewers(@PathVariable("id") Integer id) {
        return reqDocTemplateService.getAllReqDocTemplateReviewers(id);
    }

    @RequestMapping(value = "/{reqDocId}/requirement/template/tree", method = RequestMethod.GET)
    public List<PLMRequirementTemplate> getRequirementTree(@PathVariable("reqDocId") Integer reqDocId) {
        return reqDocTemplateService.getRequirementTemplateTree(reqDocId);
    }


}
