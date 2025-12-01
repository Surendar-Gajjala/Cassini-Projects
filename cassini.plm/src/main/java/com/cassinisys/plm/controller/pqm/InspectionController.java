package com.cassinisys.plm.controller.pqm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.InspectionPlanCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.InspectionsDto;
import com.cassinisys.plm.model.pqm.dto.PRItemsDto;
import com.cassinisys.plm.service.pqm.InspectionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by subramanyam on 04-06-2020.
 */
@RestController
@RequestMapping("/pqm/inspections")
@Api(tags = "PLM.PQM", description = "Quality Related")
public class InspectionController extends BaseController {

    @Autowired
    private InspectionService inspectionService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PQMInspection create(@RequestBody PQMInspection inspection) {
        return inspectionService.create(inspection);
    }

    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public PQMItemInspection createItemInspection(@RequestBody PQMItemInspection inspection) {
        return inspectionService.createItemInspection(inspection);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PQMInspection update(@PathVariable("id") Integer id,
                                @RequestBody PQMInspection inspection) {
        return inspectionService.update(inspection);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        inspectionService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PQMInspection get(@PathVariable("id") Integer id) {
        return inspectionService.get(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public InspectionsDto getInspectionDetails(@PathVariable("id") Integer id) {
        return inspectionService.getInspectionDetails(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PQMInspection> getAll() {
        return inspectionService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMInspection> getMultiple(@PathVariable Integer[] ids) {
        return inspectionService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.PUT)
    public PQMItemInspection updateItemInspection(@PathVariable("id") Integer id,
                                                  @RequestBody PQMItemInspection inspection) {
        return inspectionService.updateItemInspection(inspection);
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.DELETE)
    public void deleteItemInspection(@PathVariable("id") Integer id) {
        inspectionService.deleteItemInspection(id);
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
    public PQMItemInspection getItemInspection(@PathVariable("id") Integer id) {
        return inspectionService.getItemInspection(id);
    }

    @RequestMapping(value = "/items/all", method = RequestMethod.GET)
    public Page<InspectionsDto> getAllItemInspections(PageRequest pageRequest, InspectionPlanCriteria planCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inspectionService.getAllItemInspections(pageable, planCriteria);
    }

    @RequestMapping(value = "/materials/{id}", method = RequestMethod.PUT)
    public PQMMaterialInspection updateMaterialInspection(@PathVariable("id") Integer id,
                                                          @RequestBody PQMMaterialInspection inspection) {
        return inspectionService.updateMaterialInspection(inspection);
    }

    @RequestMapping(value = "/materials/{id}", method = RequestMethod.DELETE)
    public void deleteMaterialInspection(@PathVariable("id") Integer id) {
        inspectionService.deleteMaterialInspection(id);
    }

    @RequestMapping(value = "/materials/{id}", method = RequestMethod.GET)
    public PQMMaterialInspection getMaterialInspection(@PathVariable("id") Integer id) {
        return inspectionService.getMaterialInspection(id);
    }


    @RequestMapping(value = "/materials/all", method = RequestMethod.GET)
    public Page<InspectionsDto> getAllMaterialInspections(PageRequest pageRequest, InspectionPlanCriteria planCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inspectionService.getAllMaterialInspections(pageable, planCriteria);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PQMInspectionAttribute createInspectionAttribute(@PathVariable("id") Integer id,
                                                            @RequestBody PQMInspectionAttribute attribute) {
        return inspectionService.createInspectionAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PQMInspectionAttribute updateInspectionAttribute(@PathVariable("id") Integer id,
                                                            @RequestBody PQMInspectionAttribute attribute) {
        return inspectionService.updateInspectionAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/checklists", method = RequestMethod.GET)
    public List<PQMInspectionChecklist> getInspectionChecklists(@PathVariable("id") Integer id) {
        return inspectionService.getInspectionChecklists(id);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}", method = RequestMethod.PUT)
    public PQMInspectionChecklist updateInspectionChecklist(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId, @RequestBody PQMInspectionChecklist inspectionChecklist) {
        return inspectionService.updateInspectionChecklist(id, checklistId, inspectionChecklist);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}/children", method = RequestMethod.GET)
    public List<PQMInspectionChecklist> getInspectionChecklistChildren(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId) {
        return inspectionService.getInspectionChecklistChildren(id, checklistId);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}/params", method = RequestMethod.GET)
    public List<PQMParamActualValue> getInspectionChecklistParams(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId) {
        return inspectionService.getInspectionChecklistParams(id, checklistId);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}/params", method = RequestMethod.PUT)
    public PQMParamActualValue updateInspectionChecklistParams(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId,
                                                               @RequestBody PQMParamActualValue paramActualValue) {
        return inspectionService.updateInspectionChecklistParams(id, checklistId, paramActualValue);
    }

    @RequestMapping(value = "{id}/details/count", method = RequestMethod.GET)
    public ItemDetailsDto getDetailsCount(@PathVariable("id") Integer id) {
        return inspectionService.getDetailsCount(id);
    }

    @RequestMapping(value = "{id}/files", method = RequestMethod.GET)
    public List<PQMInspectionFile> getInspectionFiles(@PathVariable("id") Integer id) {
        return inspectionService.getInspectionFiles(id);
    }

    @RequestMapping(value = "/items/{id}/relateditems", method = RequestMethod.POST)
    public List<PQMItemInspectionRelatedItem> createItemRelatedItems(@PathVariable("id") Integer id,
                                                                     @RequestBody List<PQMItemInspectionRelatedItem> relatedItems) {
        return inspectionService.createItemRelatedItems(id, relatedItems);
    }

    @RequestMapping(value = "/items/{id}/relateditems/{itemId}", method = RequestMethod.PUT)
    public PQMItemInspectionRelatedItem updateItemRelatedItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId,
                                                              @RequestBody PQMItemInspectionRelatedItem attribute) {
        return inspectionService.updateItemRelatedItem(id, attribute);
    }

    @RequestMapping(value = "/items/{id}/relateditems", method = RequestMethod.GET)
    public List<PRItemsDto> getItemRelatedItems(@PathVariable("id") Integer id) {
        return inspectionService.getItemRelatedItems(id);
    }

    @RequestMapping(value = "/items/{id}/relateditems/{itemId}", method = RequestMethod.DELETE)
    public void deleteItemRelatedItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        inspectionService.deleteItemRelatedItem(id, itemId);
    }

    @RequestMapping(value = "/materials/{id}/relateditems", method = RequestMethod.POST)
    public List<PQMMaterialInspectionRelatedItem> createMaterialRelatedItems(@PathVariable("id") Integer id,
                                                                             @RequestBody List<PQMMaterialInspectionRelatedItem> relatedItems) {
        return inspectionService.createMaterialRelatedItems(id, relatedItems);
    }

    @RequestMapping(value = "/materials/{id}/relateditems/{itemId}", method = RequestMethod.PUT)
    public PQMMaterialInspectionRelatedItem updateMaterialRelatedItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId,
                                                                      @RequestBody PQMMaterialInspectionRelatedItem attribute) {
        return inspectionService.updateMaterialRelatedItem(id, attribute);
    }

    @RequestMapping(value = "/materials/{id}/relateditems", method = RequestMethod.GET)
    public List<PRItemsDto> getMaterialRelatedItems(@PathVariable("id") Integer id) {
        return inspectionService.getMaterialRelatedItems(id);
    }

    @RequestMapping(value = "/materials/{id}/relateditems/{itemId}", method = RequestMethod.DELETE)
    public void deleteMaterialRelatedItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        inspectionService.deleteMaterialRelatedItem(id, itemId);
    }

    @RequestMapping(value = "/items/{id}/rejected", method = RequestMethod.GET)
    public List<InspectionsDto> getRejectedItemInspectionByProduct(@PathVariable("id") Integer id) {
        return inspectionService.getRejectedItemInspectionByProduct(id);
    }

    @RequestMapping(value = "/materials/rejected", method = RequestMethod.GET)
    public List<InspectionsDto> getRejectedMaterialInspections() {
        return inspectionService.getRejectedMaterialInspections();
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    public List<InspectionsDto> getInspectionsByItem(@PathVariable("id") Integer id) {
        return inspectionService.getInspectionsByItem(id);
    }
}
