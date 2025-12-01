package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.MBOMCriteria;
import com.cassinisys.plm.model.cm.PLMItemMCO;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mes.MESBOMItem;
import com.cassinisys.plm.model.mes.MESMBOM;
import com.cassinisys.plm.model.mes.MESMBOMRevision;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.dto.BOPDto;
import com.cassinisys.plm.model.mes.dto.EBOMValidate;
import com.cassinisys.plm.model.mes.dto.MBOMDto;
import com.cassinisys.plm.model.mes.dto.MBOMItemDto;
import com.cassinisys.plm.model.plm.dto.BomDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.cm.MCOService;
import com.cassinisys.plm.service.mes.MBOMService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/mes/mboms")
@Api(tags = "PLM.MES", description = "MES Related")
public class MBOMController extends BaseController {

    @Autowired
    private MBOMService mbomService;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private MCOService mcoService;

    @RequestMapping(method = RequestMethod.POST)
    public MESMBOM create(@RequestBody MESMBOM mbom) {
        return mbomService.create(mbom);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESMBOM update(@PathVariable("id") Integer id,
                          @RequestBody MESMBOM mbom) {
        return mbomService.update(mbom);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        mbomService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESMBOM get(@PathVariable("id") Integer id) {
        return mbomService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESMBOM> getAll() {
        return mbomService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MBOMDto> getAllMBOMS(PageRequest pageRequest, MBOMCriteria mbomCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return mbomService.getAllMbomsByPageable(pageable, mbomCriteria);
    }

    @RequestMapping(value = "/revisions/{id}", method = RequestMethod.GET)
    public MESMBOMRevision getMBOMRevision(@PathVariable("id") Integer id) {
        return mbomService.getMBOMRevision(id);
    }

    @RequestMapping(value = "/{id}/revisions/history", method = RequestMethod.GET)
    public List<MESMBOMRevision> getMBOMRevisionHistory(@PathVariable("id") Integer id) {
        return mbomService.getMBOMRevisionHistory(id);
    }

    @RequestMapping(value = "/revisions/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachMBOMWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return mbomService.attachMBOMWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/create/attributes/multiple", method = RequestMethod.POST)
    public void saveMbomAttributes(@RequestBody List<MESObjectAttribute> attributes) {
        mbomService.saveMbomAttributes(attributes);
    }

    @RequestMapping(value = "/update/attributes", method = RequestMethod.PUT)
    public MESObjectAttribute updateMbomAttribute(@PathVariable("id") Integer id,
                                                  @RequestBody MESObjectAttribute attribute) {
        return mbomService.updateMbomAttribute(attribute);
    }

    @RequestMapping(value = "/uploadimageattribute/{objectid}/{attributeid}", method = RequestMethod.POST)
    public MESMBOM saveImageAttributeValue(@PathVariable("objectid") Integer objectId,
                                           @PathVariable("attributeid") Integer attributeId, MultipartHttpServletRequest request) {
        return mbomService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESMBOM> getMBOMsByType(@PathVariable("typeId") Integer id,
                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return mbomService.getMBOMsByType(id, pageable);
    }


    @RequestMapping(value = "/{id}/{objectType}/workflows", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(@PathVariable("id") Integer id, @PathVariable("objectType") String type) {
        return mbomService.getHierarchyWorkflows(id, type);
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.POST)
    public MBOMItemDto createMBOMItem(@PathVariable("id") Integer id, @RequestBody MESBOMItem mesbomItem) {
        return mbomService.createMBOMItem(id, mesbomItem, true);
    }

    @RequestMapping(value = "/{id}/items/multiple", method = RequestMethod.POST)
    public List<MESBOMItem> createMultipleMBOMItems(@PathVariable("id") Integer id, @RequestBody List<MESBOMItem> mesbomItems) {
        return mbomService.createMultipleMBOMItems(id, mesbomItems);
    }

    @RequestMapping(value = "/{id}/items/{itemId}", method = RequestMethod.PUT)
    public MESBOMItem updateMBOMItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId,
                                     @RequestBody MESBOMItem mesbomItem) {
        return mbomService.updateMBOMItem(id, mesbomItem);
    }

    @RequestMapping(value = "/{id}/items/{itemId}", method = RequestMethod.DELETE)
    public void deleteMBOMItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        mbomService.deleteMBOMItem(id, itemId);
    }

    @RequestMapping(value = "/{id}/items/{itemId}", method = RequestMethod.GET)
    public MESBOMItem getMBOMItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        return mbomService.getMBOMItem(id, itemId);
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public List<MBOMItemDto> getMBOMItems(@PathVariable("id") Integer id, @RequestParam("hierarchy") Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = false;
        }
        return mbomService.getMBOMItems(id, hierarchy);
    }

    @RequestMapping(value = "/{id}/items/{itemId}/children", method = RequestMethod.GET)
    public List<MBOMItemDto> getMBOMItemChildren(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        return mbomService.getMBOMItemChildren(id, itemId);
    }

    @RequestMapping(value = "/{id}/item/{itemId}/bom/released", method = RequestMethod.GET)
    public List<BomDto> getReleasedBom(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return mbomService.getReleasedBom(id, itemId, hierarchy);
    }

    @RequestMapping(value = "/{id}/item/{itemId}/bom/released/validate", method = RequestMethod.GET)
    public List<EBOMValidate> getValidatedReleasedBom(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return mbomService.getValidatedReleasedBom(id, itemId, hierarchy);
    }

    @RequestMapping(value = "/released", method = RequestMethod.GET)
    public List<MBOMDto> getReleasedMboms() {
        return mbomService.getReleasedMboms();
    }

    @RequestMapping(value = "/revisions/{mbomRevision}/bops/released", method = RequestMethod.GET)
    public List<BOPDto> getMBOMReleasedBOPs(@PathVariable("mbomRevision") Integer mbomRevision) {
        return mbomService.getMBOMReleasedBOPs(mbomRevision);
    }

    @RequestMapping(value = "/{id}/changes", method = RequestMethod.GET)
    public List<PLMItemMCO> getMBOMChanges(@PathVariable("id") Integer id) {
        return mcoService.findByMCOsByMbomRevision(id);
    }

    @RequestMapping(value = "/{id}/whereused", method = RequestMethod.GET)
    public List<BOPDto> getMBOMWhereUsed(@PathVariable("id") Integer id) {
        return mbomService.getMBOMWhereUsed(id);
    }

    @RequestMapping(value = "/{id}/counts", method = RequestMethod.GET)
    public ItemDetailsDto getMBOMTabCounts(@PathVariable("id") Integer id) {
        return mbomService.getMBOMTabCounts(id);
    }
}

