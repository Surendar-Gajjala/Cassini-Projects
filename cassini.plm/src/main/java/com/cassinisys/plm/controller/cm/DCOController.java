package com.cassinisys.plm.controller.cm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.DCOChangeRequestCriteria;
import com.cassinisys.plm.filtering.DCOCriteria;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.ChangeTypeAttributeDto;
import com.cassinisys.plm.model.cm.dto.DCODto;
import com.cassinisys.plm.model.cm.dto.DCRAffecteditemsDto;
import com.cassinisys.plm.model.cm.dto.DCRDto;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.mobile.DCODetails;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.dto.QualityTypeAttributeDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by CassiniSystems on 09-06-2020.
 */

@RestController
@RequestMapping("/cms/dcos")
@Api(tags = "PLM.CM", description = "Changes Related")
public class DCOController extends BaseController {

    @Autowired
    private DCOService dcoService;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private PLMWorkflowService workflowService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMDCO create(@RequestBody PLMDCO dco) {
        return dcoService.create(dco);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMDCO update(@PathVariable("id") Integer id,
                         @RequestBody PLMDCO dco) {
        return dcoService.update(dco);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        dcoService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMDCO get(@PathVariable("id") Integer id) {
        return dcoService.get(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public DCODetails getDCODetails(@PathVariable("id") Integer id) {
        return dcoService.getDCODetails(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMDCO> getAll() {
        return dcoService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMDCO> getMultiple(@PathVariable Integer[] ids) {
        return dcoService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/changeTypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMChangeType> getMultipleChangeTypes(@PathVariable Integer[] ids) {
        return dcoService.getMultipleChangeTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<DCODto> getAllDCOS(PageRequest pageRequest, DCOCriteria dcoCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return dcoService.getAllDCOS(pageable, dcoCriteria);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return dcoService.attachDCOWorkflow(id, wfDefId, null);
    }

    @RequestMapping(value = "/{id}/attachnewworkflow/{wfDefId}")
    public PLMWorkflow attachNewWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId,
                                         @RequestParam("rule") RevisionCreationType rule, @RequestParam("status") Integer status) {
        return dcoService.attachNewDCOWorkflow(id, wfDefId, rule, status);
    }

    @RequestMapping(value = "/{mfrId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("mfrId") Integer mfrId) {
        workflowService.deleteWorkflow(mfrId);
    }

    @RequestMapping(value = "{id}/relatedItem/delete/{item}", method = RequestMethod.DELETE)
    public void deleteDcoRelatedItem(@PathVariable("id") Integer id,
                                     @PathVariable("item") Integer item) {
        dcoService.deleteDcoRelatedItem(id, item);
    }

    @RequestMapping(value = "/filtered/dcr/items", method = RequestMethod.GET)
    public Page<PLMDCR> getFilteredDcrs(PageRequest pageRequest, DCOChangeRequestCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return dcoService.getFilteredDcrs(pageable, criteria);
    }


    @RequestMapping(value = "/{dcoId}/changeRequest/multiple", method = RequestMethod.POST)
    public List<PLMDCR> createChangeReqItems(@PathVariable("dcoId") Integer dcoId,
                                             @RequestBody List<PLMDCR> items) {
        return dcoService.createChangeReqItems(dcoId, items);
    }

    @RequestMapping(value = "/{dcoId}/changeRequestItems", method = RequestMethod.GET)
    public List<DCRDto> getChangeReqItems(@PathVariable("dcoId") Integer dcoId) {
        return dcoService.getChangeReqItems(dcoId);
    }

    @RequestMapping(value = "{id}/affectedItem", method = RequestMethod.POST)
    public PLMDCOAffectedItem createDcoAffectedItem(@RequestBody PLMDCOAffectedItem item) {
        return dcoService.createAffectedItem(item);
    }

    @RequestMapping(value = "/affectedItems/{id}", method = RequestMethod.GET)
    public List<DCRAffecteditemsDto> getDcoAffectedItem(@PathVariable("id") Integer id) {
        return dcoService.getDcoAffectedItem(id);
    }

    @RequestMapping(value = "{id}/affectedItem/delete/{item}", method = RequestMethod.DELETE)
    public void deleteDcoAffectedItem(@PathVariable("id") Integer id,
                                      @PathVariable("item") Integer item) {
        dcoService.deleteDcoAffectedItem(id, item);
    }

    @RequestMapping(value = "/{objectType}/object", method = RequestMethod.POST)
    public ChangeTypeAttributeDto crateChangeObject(@PathVariable("objectType") PLMObjectType objectType, @RequestBody ChangeTypeAttributeDto changeTypeAttributeDto) {
        return dcoService.crateChangeObject(objectType, changeTypeAttributeDto);
    }

    @RequestMapping(value = "/{objectType}/{objectId}/{attributeId}/images/upload", method = RequestMethod.POST)
    public QualityTypeAttributeDto saveImageAttributeValue(@PathVariable("objectType") PLMObjectType objectType, @PathVariable("objectId") Integer objectId,
                                                           @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return dcoService.saveImageAttributeValue(objectType, objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/{objectType}/{objectId}/{attributeId}/attachments/upload", method = RequestMethod.POST)
    public QualityTypeAttributeDto saveAttachmentAttributeValue(@PathVariable("objectType") PLMObjectType objectType, @PathVariable("objectId") Integer objectId,
                                                                @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return dcoService.saveAttachmentAttributeValue(objectType, objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/{dcoId}/details", method = RequestMethod.GET)
    public DetailsCount getDCRDetailsCount(@PathVariable("dcoId") Integer dcoId) {
        return dcoService.getDCRDetailsCount(dcoId);
    }

    @RequestMapping(value = "/{dcoId}/changeRequestItems/{changeId}", method = RequestMethod.DELETE)
    public void deleteEcoChangeRequest(@PathVariable("dcoId") Integer dcoId, @PathVariable("changeId") Integer changeId) {
        dcoService.deleteDcoChangeRequest(dcoId, changeId);
    }

    @RequestMapping(value = "/changeAnalysts", method = RequestMethod.GET)
    public List<Person> getChangeAnalysts() {
    return dcoService.getChangeAnalysts();
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public List<String> getStatus() {
    return dcoService.getStatus();

    }


}
