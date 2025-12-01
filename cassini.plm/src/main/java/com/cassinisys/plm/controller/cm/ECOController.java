package com.cassinisys.plm.controller.cm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.BomItemFilterCriteria;
import com.cassinisys.plm.filtering.ECOCriteria;
import com.cassinisys.plm.filtering.ParameterCriteria;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.AffectedItemDto;
import com.cassinisys.plm.model.cm.dto.ChangeObjectsFilterDto;
import com.cassinisys.plm.model.cm.dto.VarianceRelatedItemDto;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.mobile.ECODetails;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.classification.ChangeTypeService;
import com.cassinisys.plm.service.cm.ECOService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.ws.rs.Produces;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lakshmi on 1/3/2016.
 */

@RestController
@RequestMapping("/cm/ecos")
@Api(tags = "PLM.CM", description = "Changes Related")
public class ECOController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private ECOService ecoService;
    @Autowired
    private PLMWorkflowService workFlowService;
    @Autowired
    private ChangeTypeService changeTypeService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMECO create(@RequestBody PLMECO eco) {
        eco.setId(null);
        return ecoService.create(eco);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMECO update(@PathVariable("id") Integer id,
                         @RequestBody PLMECO eco) {
        eco.setId(id);
        return ecoService.update(eco);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long getEcoCount() {
        return ecoService.getEcoCount();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        ecoService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMECO get(@PathVariable("id") Integer id) {
        return ecoService.get(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public ECODetails getEcoDetails(@PathVariable("id") Integer id) {
        return ecoService.getEcoDetails(id);
    }

    @RequestMapping(value = "/byNumber/{number}", method = RequestMethod.GET)
    public Integer getByNumber(@PathVariable("number") String number) {
        return ecoService.getByNumber(number);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PLMECO> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecoService.findAll(pageable);
    }

    @RequestMapping(value = "/item/all", method = RequestMethod.GET)
    public Page<PLMECO> getAllItemECOs(PageRequest pageRequest, ECOCriteria ecoCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecoService.getAllItemECOs(pageable, ecoCriteria);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public List<String> getStatus() {

        return ecoService.getStatus();
    }

    @RequestMapping(value = "/changeAnalysts", method = RequestMethod.GET)
    public List<Person> getChangeAnalysts() {

        return ecoService.getChangeAnalysts();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<PLMECO> getAll(ECOCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecoService.findAll(criteria, pageable);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMECO> getMultiple(@PathVariable Integer[] ids) {
        return ecoService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/changeTypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMChangeType> getMultipleChangeTypes(@PathVariable Integer[] ids) {
        return ecoService.getMultipleChangeTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/{ecoId}/ecoAffectedItems", method = RequestMethod.POST)
    public PLMAffectedItem createEcoAffectedItem(@PathVariable("ecoId") Integer ecoId,
                                                 @RequestBody PLMAffectedItem item) {
        return ecoService.createEcoAffectedItem(item);
    }

    @RequestMapping(value = "/{ecoId}/affectedItems", method = RequestMethod.POST)
    public PLMAffectedItem createAffectedItem(@PathVariable("ecoId") Integer ecoId,
                                              @RequestBody PLMAffectedItem item) {
        return ecoService.createAffectedItem(item);
    }

    @RequestMapping(value = "/{ecoId}/affectedItems/multiple", method = RequestMethod.POST)
    public List<PLMAffectedItem> createAffectedItem(@PathVariable("ecoId") Integer ecoId,
                                                    @RequestBody List<PLMAffectedItem> items) {
        return ecoService.createAffectedItems(ecoId, items);
    }

    @RequestMapping(value = "/{ecoId}/affectedItems/configurable", method = RequestMethod.GET)
    public List<PLMAffectedItem> getConfigurableAffectedItems(@PathVariable("ecoId") Integer ecoId) {
        return ecoService.getConfigurableAffectedItems(ecoId);
    }

    @RequestMapping(value = "/{ecoId}/ecoAffectedItems/{itemId}", method = RequestMethod.PUT)
    public PLMAffectedItem updateEcoAffectedItem(@PathVariable("ecoId") Integer ecoId,
                                                 @PathVariable("itemId") Integer itemId,
                                                 @RequestBody PLMAffectedItem item) {
        return ecoService.updateEcoAffectedItem(item);
    }

    @RequestMapping(value = "/{ecoId}/ecoAffectedItems", method = RequestMethod.GET)
    public List<PLMAffectedItem> getEcoAffectedItems(@PathVariable("ecoId") Integer ecoId) {
        return ecoService.getEcoAffectedItems(ecoId);
    }

    @RequestMapping(value = "/{ecoId}/ecoAffectedItems/{itemId}", method = RequestMethod.DELETE)
    public void deleteEcoAffectedItem(@PathVariable("ecoId") Integer ecoId,
                                      @PathVariable("itemId") Integer itemId) {
        ecoService.deleteEcoAffectedItem(itemId);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<PLMECO> searchECO(ECOCriteria criteria,
                                  PageRequest pageRequest) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecoService.find(criteria, pageable);
    }

    @RequestMapping(value = "/advancedsearch", method = RequestMethod.POST)
    public Page<PLMECO> advancedSearch(@RequestBody ParameterCriteria[] parameterCriterias, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMECO> pdmecos = ecoService.advancedSearchECR(parameterCriterias, pageable);
        return pdmecos;
    }

    @RequestMapping(value = "/{ecoId}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("ecoId") Integer ecoId, @PathVariable("wfDefId") Integer wfDefId) {
        return ecoService.attachWorkflow(ecoId, wfDefId, null);
    }

    @RequestMapping(value = "/{ecoId}/attachnewworkflow/{wfDefId}")
    public PLMWorkflow attachNewWorkflow(@PathVariable("ecoId") Integer ecoId, @PathVariable("wfDefId") Integer wfDefId,
                                         @RequestParam("rule") RevisionCreationType rule, @RequestParam("status") Integer status) {
        return ecoService.attachNewWorkflow(ecoId, wfDefId, rule, status);
    }

    @RequestMapping(value = "/{ecoId}/workflow")
    public PLMWorkflow getWorkflow(@PathVariable("ecoId") Integer ecoId) {
        return ecoService.getAttachedWorkflow(ecoId);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PLMChangeAttribute createEcoAttribute(@PathVariable("id") Integer id,
                                                 @RequestBody PLMChangeAttribute attribute) {
        return ecoService.createEcoAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PLMChangeAttribute updateEcoAttribute(@PathVariable("id") Integer id,
                                                 @RequestBody PLMChangeAttribute attribute) {
        return ecoService.updateEcoAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.GET)
    public List<PLMChangeAttribute> getEcoAttributes(@PathVariable("id") Integer id) {
        return ecoService.getEcoAttributes(id);
    }

    @RequestMapping(value = "/changeTypes/{type}", method = RequestMethod.GET)
    public PLMChangeType getChangeType(@PathVariable("type") String type) {
        return changeTypeService.getChangeType(type);
    }

    @RequestMapping(value = "/changeType/{type}", method = RequestMethod.GET)
    public PLMChangeType getChangeType(@PathVariable("type") Integer type) {
        return changeTypeService.get(type);
    }

    @RequestMapping(value = "/byName/{name}", method = RequestMethod.GET)
    public PLMChangeType getChangeTypeByName(@PathVariable("name") String name) {
        return changeTypeService.getChangeType(name);
    }

    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public void saveEcoAttributes(@PathVariable("id") Integer id,
                                  @RequestBody List<PLMChangeAttribute> attributes) {
        ecoService.saveEcoAttributes(attributes);
    }

    @RequestMapping(value = "/ecoTypes/{type}/attributes", method = RequestMethod.GET)
    public List<PLMChangeTypeAttribute> getEcoAttributes(@PathVariable("type") Integer type,
                                                         @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return ecoService.getEcoTypeAttributes(type, hierarchy);
    }

    @RequestMapping(value = "/latestReleasedRevs", method = RequestMethod.GET)
    public Page<PLMItemRevision> getLatestReleasedRevisions(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecoService.getLatestReleasedRevisions(pageable);
    }

    @RequestMapping(value = "/releasedEcos", method = RequestMethod.GET)
    public Page<PLMECO> findByReleasedFalse(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecoService.findByReleasedFalse(pageable);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<PLMECO> getItemsByType(@PathVariable("typeId") Integer id,
                                       PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecoService.getECOsByType(id, pageable);
    }

    @RequestMapping(value = "/type/{id}/count", method = RequestMethod.GET)
    @Produces({"text/plain"})
    public Integer getObjectsByType(@PathVariable("id") Integer id) {
        return ecoService.getObjectsByType(id);
    }

    @RequestMapping(value = "/attributes/{attributeId}", method = RequestMethod.GET)
    public List<PLMChangeAttribute> getEcoUsedAttributes(@PathVariable("attributeId") Integer attributeId) {
        return ecoService.getEcoUsedAttributes(attributeId);
    }

    @RequestMapping(value = "/updateImageValue/{objectId}/{attributeId}", method = RequestMethod.POST)
    public PLMECO saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                          @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return ecoService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/{ecoId}/details", method = RequestMethod.GET)
    public DetailsCount getECODetails(@PathVariable("ecoId") Integer ecoId) {
        return ecoService.getECODetails(ecoId);
    }

    @RequestMapping(value = "/{ecoId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("ecoId") Integer ecoId) {
        workFlowService.deleteWorkflow(ecoId);
    }

    @RequestMapping(value = "/workflow/{typeId}/changeType/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getWorkflows(@PathVariable("typeId") Integer typeId,
                                                    @PathVariable("type") String type) {
        return ecoService.getHierarchyWorkflows(typeId, type);
    }

    @RequestMapping(value = "/filteredItems", method = RequestMethod.GET)
    public Page<PLMItem> getFilterRelatedItems(PageRequest pageRequest, BomItemFilterCriteria bomItemFilterCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecoService.getFilterRelatedItems(pageable, bomItemFilterCriteria);
    }

    @RequestMapping(value = "/changeRelatedItem/{changeId}", method = RequestMethod.POST)
    public PLMChangeRelatedItem createChangeRelatedItem(@PathVariable("changeId") Integer changeId,
                                                        @RequestBody PLMChangeRelatedItem plmChangeRelatedItem) {
        return ecoService.createChangeRelatedItem(plmChangeRelatedItem);
    }

    @RequestMapping(value = "/changeRelatedItem/{rowId}", method = RequestMethod.DELETE)
    public void deleteChangeRelatedItem(@PathVariable("rowId") Integer changeId) {
        ecoService.deleteChangeRelatedItem(changeId);
    }

    @RequestMapping(value = "/changeRelatedItem/{changeId}", method = RequestMethod.GET)
    public List<VarianceRelatedItemDto> getAllChangeRelatedItems(@PathVariable("changeId") Integer changeId) {
        return ecoService.getAllChangeRelatedItems(changeId);
    }

    @RequestMapping(value = "/change/all/objects", method = RequestMethod.GET)
    public Page<PLMChange> getChangeObjects(ECOCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return ecoService.getChangeObjects(criteria, pageable);
    }

    @RequestMapping(value = "/change/{changeId}", method = RequestMethod.GET)
    public PLMChange getChangeObject(@PathVariable("changeId") Integer changeId) {
        return ecoService.getChangeObject(changeId);
    }

    @RequestMapping(value = "/change/type/tree", method = RequestMethod.GET)
    public List<PLMChangeType> getChangeTypeTree() {
        return changeTypeService.getClassificationTree();
    }

    @RequestMapping(value = "/{id}/change/items", method = RequestMethod.GET)
    public List<AffectedItemDto> getChangeAffectedItems(@PathVariable("id") Integer id) {
        return ecoService.getChangeAffectedItems(id);
    }

    @RequestMapping(value = "/change/filter/objects/{type}", method = RequestMethod.GET)
    public ChangeObjectsFilterDto getChangeAnalysts(@PathVariable("type") String type) {
        return ecoService.getChangeTypeFilterObjects(type);
    }
}
