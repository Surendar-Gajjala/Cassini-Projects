package com.cassinisys.plm.controller.cm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.DCOCriteria;
import com.cassinisys.plm.filtering.DCRItemsCriteria;
import com.cassinisys.plm.filtering.ECOChangeRequestCriteria;
import com.cassinisys.plm.model.cm.PLMChangeRelatedItem;
import com.cassinisys.plm.model.cm.PLMECR;
import com.cassinisys.plm.model.cm.PLMECRAffectedItem;
import com.cassinisys.plm.model.cm.PLMECRPR;
import com.cassinisys.plm.model.cm.dto.ECRAffecteditemsDto;
import com.cassinisys.plm.model.cm.dto.ECRDto;
import com.cassinisys.plm.model.cm.dto.ECRRelatedItemDto;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mobile.ECRDetails;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.pqm.dto.ProblemReportsDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.cm.ECRService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by CassiniSystems on 09-06-2020.
 */

@RestController
@RequestMapping("/cms/ecrs")
@Api(tags = "PLM.CM", description = "Changes Related")
public class ECRController extends BaseController {

    @Autowired
    private ECRService ecrService;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private PLMWorkflowService workflowService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMECR create(@RequestBody PLMECR ecr) {
        return ecrService.create(ecr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMECR update(@PathVariable("id") Integer id,
                         @RequestBody PLMECR ecr) {
        return ecrService.update(ecr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        ecrService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMECR get(@PathVariable("id") Integer id) {
        return ecrService.get(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public ECRDetails getEcrDetails(@PathVariable("id") Integer id) {
        return ecrService.getEcrDetails(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMECR> getAll() {
        return ecrService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMECR> getMultiple(@PathVariable Integer[] ids) {
        return ecrService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<ECRDto> getAllECRs(PageRequest pageRequest, DCOCriteria dcoCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecrService.getAllECRs(pageable, dcoCriteria);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return ecrService.attachDCRWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{mfrId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("mfrId") Integer mfrId) {
        workflowService.deleteWorkflow(mfrId);
    }

    @RequestMapping(value = "{id}/affectedItem", method = RequestMethod.POST)
    public PLMECRAffectedItem createEcrItem(@RequestBody PLMECRAffectedItem item) {
        return ecrService.createAffectedItem(item);
    }

    @RequestMapping(value = "{id}/affectedItem/{itemId}", method = RequestMethod.PUT)
    public PLMECRAffectedItem updateEcrItem(@RequestBody PLMECRAffectedItem item) {
        return ecrService.updateAffectedItem(item);
    }

    @RequestMapping(value = "{id}/affectedItem/multiple", method = RequestMethod.POST)
    public List<PLMECRAffectedItem> createEcrItems(@PathVariable("id") Integer id, @RequestBody List<PLMECRAffectedItem> affectedItems) {
        return ecrService.createAffectedItems(id, affectedItems);
    }

    @RequestMapping(value = "/affectedItems/{id}", method = RequestMethod.GET)
    public List<ECRAffecteditemsDto> getAffectedItem(@PathVariable("id") Integer id) {
        return ecrService.getAffectedItem(id);
    }

    @RequestMapping(value = "/filteredItems", method = RequestMethod.GET)
    public Page<PLMItem> getFilteredItems(PageRequest pageRequest, DCRItemsCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecrService.getFilterBomItems(pageable, criteria);
    }

    @RequestMapping(value = "/{ecrId}/relatedItems/multiple", method = RequestMethod.POST)
    public List<PLMChangeRelatedItem> createEcrRelatedItems(@PathVariable("ecrId") Integer ecrId,
                                                            @RequestBody List<PLMChangeRelatedItem> items) {
        return ecrService.createEcrRelatedItems(ecrId, items);
    }

    @RequestMapping(value = "/relatedItems/{id}", method = RequestMethod.GET)
    public List<ECRRelatedItemDto> getEcrRelatedItem(@PathVariable("id") Integer id) {
        return ecrService.getEcrRelatedItem(id);
    }

    @RequestMapping(value = "{id}/affectedItem/delete/{item}", method = RequestMethod.DELETE)
    public void deleteEcrAffectedItem(@PathVariable("item") Integer item) {
        ecrService.deleteEcrAffectedItem(item);
    }

    @RequestMapping(value = "{id}/relatedItem/delete/{item}", method = RequestMethod.DELETE)
    public void deleteEcrRelatedItem(@PathVariable("item") Integer item) {
        ecrService.deleteEcrRelatedItem(item);
    }

    @RequestMapping(value = "{id}/details/count", method = RequestMethod.GET)
    public ItemDetailsDto getEcrDetailsCount(@PathVariable("id") Integer id) {
        return ecrService.getEcrDetailsCount(id);
    }

    @RequestMapping(value = "/filtered/ecr/items", method = RequestMethod.GET)
    public Page<PLMECR> getFilteredEcrs(PageRequest pageRequest, ECOChangeRequestCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecrService.getFilteredEcrs(pageable, criteria);
    }

    @RequestMapping(value = "/{ecrId}/changeRequest/multiple", method = RequestMethod.POST)
    public List<PLMECR> createChangeReqItems(@PathVariable("ecrId") Integer ecrId,
                                             @RequestBody List<PLMECR> items) {
        return ecrService.createChangeReqItems(ecrId, items);
    }

    @RequestMapping(value = "/{ecrId}/changeRequestItems", method = RequestMethod.GET)
    public List<ECRDto> getChangeReqItems(@PathVariable("ecrId") Integer ecrId) {
        return ecrService.getChangeReqItems(ecrId);
    }

    @RequestMapping(value = "/{ecrId}/changeRequestItems/{changeId}", method = RequestMethod.DELETE)
    public void deleteEcoChangeRequest(@PathVariable("ecrId") Integer ecrId, @PathVariable("changeId") Integer changeId) {
        ecrService.deleteEcoChangeRequest(ecrId, changeId);
    }

    @RequestMapping(value = "/{ecrId}/problemreports/multiple", method = RequestMethod.POST)
    public List<PLMECRPR> createECRProblemReports(@PathVariable("ecrId") Integer ecrId,
                                                  @RequestBody List<PLMECRPR> ecrprs) {
        return ecrService.createECRProblemReports(ecrId, ecrprs);
    }

    @RequestMapping(value = "/{ecrId}/problemreports", method = RequestMethod.POST)
    public PLMECRPR createECRProblemReport(@PathVariable("ecrId") Integer ecrId,
                                           @RequestBody PLMECRPR ecrpr) {
        return ecrService.createECRProblemReport(ecrId, ecrpr);
    }

    @RequestMapping(value = "/{ecrId}/problemreports", method = RequestMethod.GET)
    public List<ProblemReportsDto> getECRProblemReports(@PathVariable("ecrId") Integer ecrId) {
        return ecrService.getECRProblemReports(ecrId);
    }

    @RequestMapping(value = "/{ecrId}/problemreports/{prId}", method = RequestMethod.DELETE)
    public void deleteECRProblemReport(@PathVariable("ecrId") Integer ecrId, @PathVariable("prId") Integer prId) {
        ecrService.deleteECRProblemReport(ecrId, prId);
    }

    @RequestMapping(value = "/changeAnalysts", method = RequestMethod.GET)
    public List<Person> getChangeAnalysts() {
    return ecrService.getChangeAnalysts();
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public List<String> getStatus() {
    return ecrService.getStatus();
    }

    @RequestMapping(value = "/urgency", method = RequestMethod.GET)
    public List<String> getUrgency() {
    return ecrService.getUrgency();
    }

    @RequestMapping(value = "/originators", method = RequestMethod.GET)
    public List<Person> getOriginators() {
    return ecrService.getOriginators();
    }

    @RequestMapping(value = "/requesters", method = RequestMethod.GET)
    public List<Person> getRequesters() {
    return ecrService.getRequesters();
    }

    @RequestMapping(value = "/changeReasonTypes", method = RequestMethod.GET)
    public List<String> getChangeReasonType() {
    return ecrService.getChangeReasonType();
    }
}
