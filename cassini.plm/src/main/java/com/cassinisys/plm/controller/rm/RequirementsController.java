package com.cassinisys.plm.controller.rm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.RequirementDeliverableCriteria;
import com.cassinisys.plm.filtering.RequirementSearchCriteria;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.rm.RequirementsService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/rm/requirements")
@Api(tags = "PLM.RM",description = "Requirement Related")
public class RequirementsController extends BaseController {

    @Autowired
    private RequirementsService requirementsService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private PLMWorkflowService workflowService;

    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.GET)
    public List<RmObjectTypeAttribute> getAttributes(@PathVariable("typeId") Integer typeId,
                                                     @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return requirementsService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.POST)
    public RmObjectTypeAttribute createAttribute(@PathVariable("typeId") Integer typeId,
                                                 @RequestBody RmObjectTypeAttribute attribute) {
        return requirementsService.createAttribute(attribute);
    }

    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.GET)
    public RmObjectTypeAttribute getAttribute(@PathVariable("typeId") Integer typeId,
                                              @PathVariable("attributeId") Integer attributeId) {
        return requirementsService.getAttribute(attributeId);
    }

    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.PUT)
    public RmObjectTypeAttribute getAttribute(@PathVariable("typeId") Integer typeId,
                                              @PathVariable("attributeId") Integer attributeId,
                                              @RequestBody RmObjectTypeAttribute attribute) {
        return requirementsService.updateAttribute(attribute);
    }

    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.DELETE)
    public void deleteAttribute(@PathVariable("typeId") Integer typeId,
                                @PathVariable("attributeId") Integer attributeId) {
        requirementsService.deleteAttribute(attributeId);
    }

    @RequestMapping(value = "/attributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getAllItemTypeAttributes(@PathVariable("objectType") String objectType) {
        return requirementsService.getAllItemTypeAttributes(objectType);
    }

    @RequestMapping(value = "/byName/{name}", method = RequestMethod.GET)
    public RmObjectType getByItemTypeName(@PathVariable("name") String name) {
        return requirementsService.getItemTypeName(name);
    }

    @RequestMapping(value = "/type/{type}/attributes", method = RequestMethod.GET)
    public List<RmObjectTypeAttribute> getTypeAttributes(@PathVariable("type") Integer type,
                                                         @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return requirementsService.getSpecificationTypeAttributes(type, hierarchy);
    }

    @RequestMapping(value = "/{reqId}", method = RequestMethod.GET)
    public Requirement findById(@PathVariable("reqId") Integer reqId) {
        return requirementsService.findById(reqId);
    }

    @RequestMapping(value = "/assignedTo/{personId}", method = RequestMethod.GET)
    public Page<Requirement> getRequirementByAssigned(@PathVariable("personId") Integer personId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return requirementsService.findByAssignedTo(personId, pageable);
    }

    @RequestMapping(value = "/edit/{reqId}", method = RequestMethod.PUT)
    public Requirement updateRequirementEdit(@PathVariable("reqId") Integer reqId,
                                             @RequestParam("notes") String notes,
                                             @RequestBody Requirement requirement) {
        return requirementsService.updateReqirementEdit(reqId, requirement, notes);
    }

    @RequestMapping(value = "/{reqId}/edit/lastAccepted", method = RequestMethod.GET)
    public RequirementEdit getLastAcceptedRequirementEdit(@PathVariable("reqId") Integer reqId) {
        return requirementsService.getLastAcceptedRequirementEdit(reqId);
    }

    @RequestMapping(value = "/{reqId}/edits", method = RequestMethod.GET)
    public List<RequirementEdit> getRequirementEdits(@PathVariable("reqId") Integer reqId) {
        return requirementsService.getRequirementEdits(reqId);
    }

    @RequestMapping(value = "/edit/accept/{editId}", method = RequestMethod.PUT)
    public RequirementEdit acceptEntryEditChange(@PathVariable("editId") Integer editId,
                                                 @RequestBody RequirementEdit requirementEdit) {
        return requirementsService.acceptEntryEditChange(editId, requirementEdit);
    }

    @RequestMapping(value = "/edit/reject/{editId}", method = RequestMethod.PUT)
    public RequirementEdit rejectEntryEditChange(@PathVariable("editId") Integer editId,
                                                 @RequestBody RequirementEdit requirementEdit) {
        return requirementsService.rejectEntryEditChange(editId, requirementEdit);
    }

    @RequestMapping(value = "/{specReqId}/edit/approve/{editId}", method = RequestMethod.PUT)
    public RequirementEdit approveEntryEdits(@PathVariable("specReqId") Integer specReqId,
                                             @PathVariable("editId") Integer editId,
                                             @RequestBody RequirementEdit requirementEdit) {
        return requirementsService.approveEntryEdits(specReqId, editId, requirementEdit);
    }

    @RequestMapping(value = "/{reqId}/versionHistory", method = RequestMethod.GET)
    public List<Requirement> getRequirementVersions(@PathVariable("reqId") Integer reqId) {
        return requirementsService.getRequirementVersions(reqId);
    }

    @RequestMapping(value = "/{itemTypeId}/items", method = RequestMethod.GET)
    public Integer getReqTypeItems(@PathVariable("itemTypeId") Integer itemTypeId) {
        return requirementsService.getReqTypeItems(itemTypeId);
    }

    @RequestMapping(value = "/{reqId}/files/{fileId}/renameFile", method = RequestMethod.PUT)
    public RmObjectFile renameFile(@PathVariable("reqId") Integer id,
                                   @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) {
        return requirementsService.updateFileName(fileId, newFileName);
    }

    @RequestMapping(value = "/{reqId}/itemDeliverable", method = RequestMethod.GET)
    public Page<PLMItem> getReqItemDeliverables(@PathVariable("reqId") Integer reqId, PageRequest pageRequest, RequirementDeliverableCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return requirementsService.getReqItemDeliverables(reqId, pageable, criteria);
    }

    @RequestMapping(value = "/{reqId}/deliverables/multiple", method = RequestMethod.POST)
    public List<RequirementDeliverable> createRequirementDeliverables(@PathVariable("reqId") Integer reqId, @RequestBody List<PLMItem> items) {
        return requirementsService.createRequirementDeliverables(reqId, items);
    }

    @RequestMapping(value = "/{reqId}/reqDeliverables", method = RequestMethod.GET)
    public List<PLMItem> getProjectDeliverablesByProjectId(@PathVariable("reqId") Integer reqId) {
        return requirementsService.getRequirementDeliverablesByReqId(reqId);
    }

    @RequestMapping(value = "/{reqId}/deliverable/{itemId}", method = RequestMethod.DELETE)
    public void deleteRequirementDelivarable(@PathVariable("reqId") Integer reqId,
                                             @PathVariable("itemId") Integer itemId) {
        requirementsService.deleteRequirementDelivarable(reqId, itemId);
    }

    @RequestMapping(value = "/items/attributes/{attributeId}", method = RequestMethod.GET)
    public List<RmObjectAttribute> getAttributeUsedItems(@PathVariable("attributeId") Integer attributeId) {
        return requirementsService.getRmObjectUsedAttributes(attributeId);
    }

    @RequestMapping(value = "/{specId}/details", method = RequestMethod.GET)
    public DetailsCount getReqDetails(@PathVariable("specId") Integer specId) {
        return requirementsService.getReqDetails(specId);
    }

    @RequestMapping(value = "{reqId}/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateZip(@PathVariable("reqId") Integer reqId, HttpServletResponse response) throws FileNotFoundException, IOException {
        requirementsService.generateZipFile(reqId, response);
    }

    @RequestMapping(value = "/freeTextSearch", method = RequestMethod.GET)
    public Page<Requirement> freeTextSearch(PageRequest pageRequest, RequirementSearchCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<Requirement> requirements = requirementsService.freeTextSearch(pageable, criteria);
        return requirements;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<Requirement> getRequirements(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return requirementsService.getRequirements(pageable);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return requirementsService.attachReqWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{reqId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("reqId") Integer specId) {
        workflowService.deleteWorkflow(specId);
    }


    @RequestMapping(value = "/workflow/{typeId}/reqType/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getWorkflows(@PathVariable("typeId") Integer typeId,
                                                    @PathVariable("type") String type) {
        return requirementsService.getHierarchyWorkflows(typeId, type);
    }
}