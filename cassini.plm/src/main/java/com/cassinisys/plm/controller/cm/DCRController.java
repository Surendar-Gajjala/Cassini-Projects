package com.cassinisys.plm.controller.cm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.DCRCriteria;
import com.cassinisys.plm.filtering.DCRItemsCriteria;
import com.cassinisys.plm.model.cm.PLMChangeRelatedItem;
import com.cassinisys.plm.model.cm.PLMDCR;
import com.cassinisys.plm.model.cm.PLMDCRAffectedItem;
import com.cassinisys.plm.model.cm.dto.DCRAffecteditemsDto;
import com.cassinisys.plm.model.cm.dto.DCRDto;
import com.cassinisys.plm.model.cm.dto.DCRRelatedItemDto;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.mobile.DCRDetails;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.cm.DCRService;
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
@RequestMapping("/cms/dcrs")
@Api(tags = "PLM.CM", description = "Changes Related")
public class DCRController extends BaseController {

    @Autowired
    private DCRService dcrService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private PLMWorkflowService workflowService;

    @Autowired
    private FileDownloadService fileDownloadService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMDCR create(@RequestBody PLMDCR dcr) {
        return dcrService.create(dcr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMDCR update(@PathVariable("id") Integer id,
                         @RequestBody PLMDCR dcr) {
        return dcrService.update(dcr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        dcrService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMDCR get(@PathVariable("id") Integer id) {
        return dcrService.get(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public DCRDetails getDCRDetails(@PathVariable("id") Integer id) {
        return dcrService.getDCRDetails(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMDCR> getAll() {
        return dcrService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMDCR> getMultiple(@PathVariable Integer[] ids) {
        return dcrService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<DCRDto> getAllDCRS(PageRequest pageRequest, DCRCriteria dcrCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return dcrService.getAllDCRS(pageable, dcrCriteria);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return dcrService.attachNewWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{dcrId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("dcrId") Integer dcrId) {
        workflowService.deleteWorkflow(dcrId);
    }

    @RequestMapping(value = "{id}/affectedItem", method = RequestMethod.POST)
    public PLMDCRAffectedItem createDcrItem(@RequestBody PLMDCRAffectedItem item) {
        return dcrService.createAffectedItem(item);
    }

    @RequestMapping(value = "{id}/affectedItem/multiple", method = RequestMethod.POST)
    public List<PLMDCRAffectedItem> createDcrItems(@PathVariable("id") Integer id, @RequestBody List<PLMDCRAffectedItem> item) {
        return dcrService.createAffectedItems(id, item);
    }

    @RequestMapping(value = "/affectedItems/{id}", method = RequestMethod.GET)
    public List<DCRAffecteditemsDto> getAffectedItem(@PathVariable("id") Integer id) {
        return dcrService.getAffectedItem(id);
    }

    @RequestMapping(value = "/filteredItems", method = RequestMethod.GET)
    public Page<PLMItem> getFilteredItems(PageRequest pageRequest, DCRItemsCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return dcrService.getFilterBomItems(pageable, criteria);
    }

    @RequestMapping(value = "/{dcrId}/relatedItems/multiple", method = RequestMethod.POST)
    public List<PLMChangeRelatedItem> createDcrRelatedItems(@PathVariable("dcrId") Integer dcrId,
                                                            @RequestBody List<PLMChangeRelatedItem> items) {
        return dcrService.createDcrRelatedItems(dcrId, items);
    }

    @RequestMapping(value = "/relatedItems/{id}", method = RequestMethod.GET)
    public List<DCRRelatedItemDto> getDcrRelatedItem(@PathVariable("id") Integer id) {
        return dcrService.getDcrRelatedItem(id);
    }

    @RequestMapping(value = "{id}/affectedItem/delete/{item}", method = RequestMethod.DELETE)
    public void deleteDcrAffectedItem(@PathVariable("id") Integer id, @PathVariable("item") Integer item) {
        dcrService.deleteDcrAffectedItem(id, item);
    }

    @RequestMapping(value = "{id}/relatedItem/delete/{item}", method = RequestMethod.DELETE)
    public void deleteDcrRelatedItem(@PathVariable("id") Integer id,
                                     @PathVariable("item") Integer item) {
        dcrService.deleteDcrRelatedItem(id, item);
    }

    @RequestMapping(value = "/{dcrId}/details", method = RequestMethod.GET)
    public DetailsCount getDCRDetailsCount(@PathVariable("dcrId") Integer dcrId) {
        return dcrService.getDCRDetailsCount(dcrId);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public List<String> getStatus() {
        return dcrService.getStatus();
    }
    
    @RequestMapping(value = "/changeReasonType", method = RequestMethod.GET)
    public List<String> getChangeReasonType() {
        return dcrService.getChangeReasonType();
    }

    @RequestMapping(value = "/urgency", method = RequestMethod.GET)
    public List<String> getUrgency() {
        return dcrService.getUrgency();
    }

    @RequestMapping(value = "/changeAnalysts", method = RequestMethod.GET)
    public List<Person> getChangeAnalysts() {
      return dcrService.getChangeAnalysts();
    }

    @RequestMapping(value = "/originators", method = RequestMethod.GET)
    public List<Person> getOriginators() {
      return dcrService.getOriginators();
    }

    @RequestMapping(value = "/requestedBy", method = RequestMethod.GET)
    public List<Person> getRequestedBy() {
      return dcrService.getRequestedBy();
    }

}
