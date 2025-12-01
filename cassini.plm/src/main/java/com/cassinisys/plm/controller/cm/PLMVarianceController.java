package com.cassinisys.plm.controller.cm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.VarianceCriteria;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.VarianceAffectedItemDto;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.dto.VarianceDto;
import com.cassinisys.plm.model.mobile.VarianceDetails;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.classification.ChangeTypeService;
import com.cassinisys.plm.service.cm.ECOService;
import com.cassinisys.plm.service.cm.PLMVarianceService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/plm/variance")
@Api(tags = "PLM.CM", description = "Changes Related")
public class PLMVarianceController extends BaseController {

    @Autowired
    private PLMVarianceService varianceService;
    @Autowired
    private FileDownloadService fileDownloadService;
    @Autowired
    private ECOService ecoService;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private ChangeTypeService changeTypeService;
    @Autowired
    private PLMWorkflowService workFlowService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMVariance create(@RequestBody PLMVariance variance) {
        return varianceService.create(variance);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMVariance update(@PathVariable("id") Integer id,
                              @RequestBody PLMVariance variance) {
        return varianceService.update(variance);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        varianceService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMVariance get(@PathVariable("id") Integer id) {
        return varianceService.get(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public VarianceDetails getVarianceDetails(@PathVariable("id") Integer id) {
        return varianceService.getVarianceDetails(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMVariance> getAll() {
        return varianceService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMVariance> getMultiple(@PathVariable Integer[] ids) {
        return varianceService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<VarianceDto> getAllByType(PageRequest pageRequest, VarianceCriteria varianceCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return varianceService.getVarianceByType(pageable, varianceCriteria);
    }

    @RequestMapping(value = "/byItem/{item}", method = RequestMethod.GET)
    public List<PLMVariance> getAllByItem(@PathVariable("item") Integer integer) {
        return varianceService.findByAffectedItem(integer);
    }

    @RequestMapping(value = "/byPart/{item}", method = RequestMethod.GET)
    public List<PLMVariance> getAllByPart(@PathVariable("item") Integer integer) {
        return varianceService.findByAffectedPart(integer);
    }


    @RequestMapping(value = "/byNumber/{number}", method = RequestMethod.GET)
    public Integer getByNumber(@PathVariable("number") String number) {
        return varianceService.getByNumber(number);
    }

    // ---------------- Affected Items -------------------------- //

    @RequestMapping(value = "/{id}/varianceAffectedItems", method = RequestMethod.POST)
    public PLMVarianceAffectedItem createVarianceAffectedItem(@PathVariable("id") Integer varianceId,
                                                              @RequestBody PLMVarianceAffectedItem item) {
        return varianceService.createVarianceAffectedItem(varianceId, item);
    }

    @RequestMapping(value = "/{id}/varianceAffectedItems/all", method = RequestMethod.POST)
    public List<PLMVarianceAffectedItem> createAllVarianceAffectedItems(@PathVariable("id") Integer varianceId,
                                                                        @RequestBody List<PLMVarianceAffectedItem> item) {
        return varianceService.createAllVarianceAffectedItems(varianceId, item);
    }

    @RequestMapping(value = "/{id}/varianceAffectedItems/{itemId}", method = RequestMethod.PUT)
    public PLMVarianceAffectedItem updateVarianceAffectedItem(@PathVariable("id") Integer varianceId,
                                                              @PathVariable("itemId") Integer itemId,
                                                              @RequestBody PLMVarianceAffectedItem item) {
        return varianceService.updateVarianceAffectedItem(varianceId, item);
    }

    @RequestMapping(value = "/{id}/varianceAffectedItems", method = RequestMethod.GET)
    public List<VarianceAffectedItemDto> getAffectedItems(@PathVariable("id") Integer id) {
        return varianceService.getAffectedItems(id);
    }

    @RequestMapping(value = "/{id}/varianceAffectedItems/{itemId}", method = RequestMethod.DELETE)
    public void deleteVarianceAffectedItem(@PathVariable("id") Integer id,
                                           @PathVariable("itemId") Integer itemId) {
        varianceService.deleteVarianceAffectedItem(id, itemId);
    }

    @RequestMapping(value = "/{id}/varianceAffectedParts", method = RequestMethod.POST)
    public PLMVarianceAffectedMaterial createVarianceAffectedPart(@PathVariable("id") Integer varianceId,
                                                                  @RequestBody PLMVarianceAffectedMaterial item) {
        return varianceService.createVarianceAffectedPart(varianceId, item);
    }

    @RequestMapping(value = "/{id}/varianceAffectedParts/all", method = RequestMethod.POST)
    public List<PLMVarianceAffectedMaterial> createAllVarianceAffectedParts(@PathVariable("id") Integer varianceId,
                                                                            @RequestBody List<PLMVarianceAffectedMaterial> item) {
        return varianceService.createAllVarianceAffectedParts(varianceId, item);
    }

    @RequestMapping(value = "/{id}/varianceAffectedParts/{itemId}", method = RequestMethod.PUT)
    public PLMVarianceAffectedMaterial updateVarianceAffectedPart(@PathVariable("id") Integer varianceId,
                                                                  @PathVariable("itemId") Integer itemId,
                                                                  @RequestBody PLMVarianceAffectedMaterial item) {
        return varianceService.updateVarianceAffectedPart(varianceId, item);
    }

    @RequestMapping(value = "/{id}/varianceAffectedParts", method = RequestMethod.GET)
    public List<VarianceAffectedItemDto> getAffectedParts(@PathVariable("id") Integer id) {
        return varianceService.getAffectedParts(id);
    }

    @RequestMapping(value = "/{id}/varianceAffectedParts/{itemId}", method = RequestMethod.DELETE)
    public void deleteVarianceAffectedPart(@PathVariable("id") Integer id,
                                           @PathVariable("itemId") Integer itemId) {
        varianceService.deleteVarianceAffectedPart(id, itemId);
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public DetailsCount getVarianceDetailsCount(@PathVariable("id") Integer id) {
        return varianceService.getVarianceDetailsCount(id);
    }

    @RequestMapping(value = "/checkIsRecurring/{vairanceId}", method = RequestMethod.GET)
    public void checkIsRecurring(@PathVariable("vairanceId") Integer varianceId) {
        varianceService.checkIsRecurring(varianceId);
    }

    @RequestMapping(value = "/checkIsRecurringParts/{vairanceId}", method = RequestMethod.GET)
    public void checkIsRecurringForParts(@PathVariable("vairanceId") Integer varianceId) {
        varianceService.checkIsRecurringForParts(varianceId);
    }

    @RequestMapping(value = "{id}/checkIsRecurringAfterDelete/{itemId}", method = RequestMethod.GET)
    public void checkIsRecurringAfterDelete(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        varianceService.checkIsRecurringAfterDelete(id, itemId);
    }

    @RequestMapping(value = "{id}/checkIsRecurringAfterDeleteParts/{itemId}", method = RequestMethod.GET)
    public void checkIsRecurringAfterDeleteForParts(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        varianceService.checkIsRecurringAfterDeleteForParts(id, itemId);
    }

    // ------------------- Variance Types ------------------- //

    @RequestMapping(value = "/deviationTypes", method = RequestMethod.POST)
    public PLMDeviationType createDeviationType(@RequestBody PLMDeviationType deviationType) {
        return changeTypeService.createDeviationType(deviationType);
    }

    @RequestMapping(value = "/deviationTypes/{id}", method = RequestMethod.PUT)
    public PLMDeviationType updateDeviationType(@PathVariable("id") Integer id, @RequestBody PLMDeviationType deviationType) {
        return changeTypeService.updateDeviationType(id, deviationType);
    }

    @RequestMapping(value = "/deviationTypes/{id}", method = RequestMethod.DELETE)
    public void deleteDeviationType(@PathVariable("id") Integer id) {
        changeTypeService.deleteDeviationType(id);
    }

    @RequestMapping(value = "/deviationTypes/{id}", method = RequestMethod.GET)
    public PLMDeviationType getDeviationType(@PathVariable("id") Integer id) {
        return changeTypeService.getDeviationType(id);
    }

    @RequestMapping(value = "/deviationTypes", method = RequestMethod.GET)
    public List<PLMDeviationType> getAllDeviationTypes() {
        return changeTypeService.getAllDeviationTypes();
    }

    @RequestMapping(value = "/deviationTypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMDeviationType> getMultipleDeviationTypes(@PathVariable Integer[] ids) {
        return changeTypeService.findMultipleDeviationTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/devitionTypeTree/tree", method = RequestMethod.GET)
    public List<PLMDeviationType> getDevitionTypeTree() {
        return changeTypeService.getDevitionTypeTree();
    }

    @RequestMapping(value = "/waiverTypes", method = RequestMethod.POST)
    public PLMWaiverType createWaiverType(@RequestBody PLMWaiverType deviationType) {
        return changeTypeService.createWaiverType(deviationType);
    }

    @RequestMapping(value = "/waiverTypes/{id}", method = RequestMethod.PUT)
    public PLMWaiverType updateWaiverType(@PathVariable("id") Integer id, @RequestBody PLMWaiverType deviationType) {
        return changeTypeService.updateWaiverType(id, deviationType);
    }

    @RequestMapping(value = "/waiverTypes/{id}", method = RequestMethod.DELETE)
    public void deleteWaiverType(@PathVariable("id") Integer id) {
        changeTypeService.deleteWaiverType(id);
    }

    @RequestMapping(value = "/waiverTypes/{id}", method = RequestMethod.GET)
    public PLMWaiverType getWaiverType(@PathVariable("id") Integer id) {
        return changeTypeService.getWaiverType(id);
    }

    @RequestMapping(value = "/waiverTypes", method = RequestMethod.GET)
    public List<PLMWaiverType> getAllWaiverTypes() {
        return changeTypeService.getAllWaiverTypes();
    }

    @RequestMapping(value = "/waiverTypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMWaiverType> getMultipleWaiverTypes(@PathVariable Integer[] ids) {
        return changeTypeService.findMultipleWaiverTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/waiverTypeTree/tree", method = RequestMethod.GET)
    public List<PLMWaiverType> getWaiverTypeTree() {
        return changeTypeService.getWaiverTypeTree();
    }

    @RequestMapping(value = "/updateImageValue/{objectId}/{attributeId}", method = RequestMethod.POST)
    public PLMVariance saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                               @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return varianceService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }


    // -------------- Workflows -------------------- //

    @RequestMapping(value = "/{varianceId}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("varianceId") Integer varianceId, @PathVariable("wfDefId") Integer wfDefId) {
        return varianceService.attachWorkflow(varianceId, wfDefId);
    }

    @RequestMapping(value = "/{varianceId}/workflow")
    public PLMWorkflow getWorkflow(@PathVariable("varianceId") Integer varianceId) {
        return varianceService.getAttachedWorkflow(varianceId);
    }


    @RequestMapping(value = "/{mfrId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("mfrId") Integer mfrId) {
        workFlowService.deleteWorkflow(mfrId);
    }

    @RequestMapping(value = "/changeRelatedItem/{changeId}", method = RequestMethod.POST)
    public List<PLMChangeRelatedItem> createChangeRelatedItem(@PathVariable("changeId") Integer changeId,
                                                              @RequestBody List<PLMChangeRelatedItem> plmChangeRelatedItem) {
        return varianceService.createVarianceRelatedItems(changeId, plmChangeRelatedItem);
    }

    @RequestMapping(value = "{id}/relatedItem/delete/{item}", method = RequestMethod.DELETE)
    public void deleteVarianceRelatedItem(@PathVariable("id") Integer id,
                                          @PathVariable("item") Integer item) {
        varianceService.deleteVarianceRelatedItem(id, item);
    }

    @RequestMapping(value = "/originator/{type}", method = RequestMethod.GET)
    public List<Person> getOriginator(@PathVariable("type") VarianceType type) {
        return varianceService.getOriginator(type);
    }

    @RequestMapping(value = "/status/{type}", method = RequestMethod.GET)
    public List<String> getStatus(@PathVariable("type") VarianceType type) {
        return varianceService.getStatus(type);
    }
}
