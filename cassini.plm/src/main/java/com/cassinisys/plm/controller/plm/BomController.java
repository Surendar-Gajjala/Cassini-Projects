package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.dto.BomComplianceReport;
import com.cassinisys.plm.model.dto.BomModalDto;
import com.cassinisys.plm.model.dto.BomRollUpReportDto;
import com.cassinisys.plm.model.dto.BomWhereUsedReport;
import com.cassinisys.plm.model.plm.BOMConfiguration;
import com.cassinisys.plm.model.plm.PLMBom;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import com.cassinisys.plm.model.plm.dto.BomDto;
import com.cassinisys.plm.service.activitystream.dto.ASBOMConfigItemInclusion;
import com.cassinisys.plm.service.plm.BOMConfigurationService;
import com.cassinisys.plm.service.plm.BomService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by reddy on 22/12/15.
 */
@RestController
@RequestMapping("/plm/items/{itemId}/bom")
@Api(tags = "PLM.ITEMS", description = "Items Related")
public class BomController extends BaseController {

    @Autowired
    private BomService bomService;

    @Autowired
    private BOMConfigurationService bomConfigurationService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMBom creteItemBom(@PathVariable("itemId") Integer itemId,
                               @RequestBody PLMBom bomItem) {
        return bomService.create(itemId, bomItem, true);
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public BomDto creteBomItem(@PathVariable("itemId") Integer itemId,
                               @RequestBody BomDto bomDto) {
        return bomService.createBomItem(itemId, bomDto, true);
    }

    @RequestMapping(value = "/multiple", method = RequestMethod.POST)
    public List<PLMBom> creteBomItems(@PathVariable("itemId") Integer itemId,
                                      @RequestBody List<PLMBom> bomItems) {
        return bomService.create(itemId, bomItems);
    }

    @RequestMapping(value = "/new/multiple", method = RequestMethod.POST)
    public List<BomDto> createBomItems(@PathVariable("itemId") Integer itemId,
                                       @RequestBody List<BomDto> bomDtos) {
        return bomService.createMultipleBomItems(itemId, bomDtos);
    }

    @RequestMapping(value = "/multiple", method = RequestMethod.PUT)
    public List<PLMBom> updateBomItems(@PathVariable("itemId") Integer itemId,
                                       @RequestBody List<PLMBom> bomItems) {
        return bomService.update(itemId, bomItems);
    }

    @RequestMapping(value = "/{bomId}", method = RequestMethod.GET)
    public PLMBom getBomItem(@PathVariable("itemId") Integer itemId,
                             @PathVariable("bomId") Integer bomId) {
        return bomService.get(bomId);
    }

    @RequestMapping(value = "/item/{bomId}", method = RequestMethod.GET)
    public PLMBom getBom(@PathVariable("itemId") Integer itemId,
                         @PathVariable("bomId") Integer bomId) {
        return bomService.getItemBom(bomId);
    }

    @RequestMapping(value = "/item/[{ids}]", method = RequestMethod.GET)
    public List<PLMBom> getBomList(@PathVariable("itemId") Integer itemId,
                                   @PathVariable("bomId") Integer[] bomId) {
        List<PLMBom> plmBoms = bomService.getItemBomList(Arrays.asList(bomId));
        List<Integer> integers = new ArrayList<>();
        for (PLMBom bom : plmBoms) {
        }
        return plmBoms;
    }

    @RequestMapping(value = "/{bomId}", method = RequestMethod.PUT)
    public PLMBom updateBomItem(@PathVariable("itemId") Integer itemId,
                                @PathVariable("bomId") Integer bomId,
                                @RequestBody PLMBom bomItem) {
        return bomService.update(itemId, bomItem);
    }

    @RequestMapping(value = "/{bomId}/update", method = RequestMethod.PUT)
    public BomDto updateItemBom(@PathVariable("itemId") Integer itemId,
                                @PathVariable("bomId") Integer bomId,
                                @RequestBody BomDto bomDto) {
        return bomService.updateItemBom(itemId, bomDto);
    }

    @RequestMapping(value = "/{bomId}", method = RequestMethod.DELETE)
    public void deleteBomItem(@PathVariable("itemId") Integer itemId,
                              @PathVariable("bomId") Integer bomId) {
        bomService.delete(itemId, bomId);
    }

    @RequestMapping(value = {"/{actualId}/change/{targetId}"}, method = RequestMethod.GET)
    public void updateBomItemSeq(@PathVariable("itemId") Integer itemId, @PathVariable("actualId") Integer actualId, @PathVariable("targetId") Integer targetId) {
        bomService.updateBomItemSeq(actualId, targetId);
    }

    @RequestMapping(value = "/paste", method = RequestMethod.PUT)
    public List<BomDto> pasteCopiedItemsToBomItem(@PathVariable("itemId") Integer itemId, @RequestBody List<PLMItem> items) {
        return bomService.pasteCopiedItemsToBomItem(itemId, items);
    }

    @RequestMapping(value = "/undo", method = RequestMethod.PUT)
    public void undoCopiedBomItems(@PathVariable("itemId") Integer itemId, @RequestBody List<BomDto> bomItems) {
        bomService.undoCopiedBomItems(itemId, bomItems);
    }

    @RequestMapping(value = "/modal", method = RequestMethod.GET)
    public BomModalDto getBomModal(@PathVariable("itemId") Integer itemId) {
        return bomService.getBomModal(itemId);
    }

    @RequestMapping(value = "/inclusion/validate", method = RequestMethod.POST)
    public BomModalDto getBomInclusionRules(@PathVariable("itemId") Integer itemId, @RequestBody BomModalDto modalDto) {
        return bomService.getBomInclusionRules(itemId, modalDto);
    }

    @RequestMapping(value = "/itemToItemExclusion/validate", method = RequestMethod.POST)
    public BomModalDto getBomItemToItemExclusionRules(@PathVariable("itemId") Integer itemId, @RequestBody BomModalDto modalDto) {
        return bomService.getBomItemToItemExclusionRules(itemId, modalDto);
    }

    @RequestMapping(value = "/configurations", method = RequestMethod.GET)
    public List<BOMConfiguration> getItemBomConfigurations(@PathVariable("itemId") Integer itemId) {
        return bomService.getItemBomConfigurations(itemId);
    }

    @RequestMapping(value = "/configurations/available", method = RequestMethod.GET)
    public List<BOMConfiguration> getAvailableItemBomConfigurations(@PathVariable("itemId") Integer itemId) {
        return bomService.getAvailableItemBomConfigurations(itemId);
    }

    @RequestMapping(value = "/configurations/{id}/modal", method = RequestMethod.GET)
    public BomModalDto getBomConfigurationModal(@PathVariable("itemId") Integer itemId, @PathVariable("id") Integer id) {
        return bomService.getBomConfigurationModal(itemId, id);
    }

    @RequestMapping(value = "/{configId}/configured/attribute/values", method = RequestMethod.GET)
    public List<PLMItemTypeAttribute> getConfiguredAttributeValues(@PathVariable("itemId") Integer itemId, @PathVariable("configId") Integer configId) {
        return bomService.getConfiguredAttributeValues(itemId, configId);
    }

    @RequestMapping(value = "/configured", method = RequestMethod.GET)
    public List<BomDto> getBomItemsFromConfigured(@PathVariable("itemId") Integer itemId) {
        return bomConfigurationService.resolveSelectedBOMConfigChildren(itemId);
    }

    @RequestMapping(value = "/{id}/resolve", method = RequestMethod.GET)
    public PLMBom getResolvedBomItemInstance(@PathVariable("itemId") Integer itemId, @PathVariable("id") Integer id) {
        return bomService.getResolvedBomItemInstance(itemId, id);
    }

    @RequestMapping(value = "/resolve", method = RequestMethod.GET)
    public List<PLMBom> getResolvedItemBom(@PathVariable("itemId") Integer itemId) {
        return bomService.getResolvedItemBom(itemId);
    }

    @RequestMapping(value = "/rollup/report", method = RequestMethod.POST)
    public BomRollUpReportDto getItemBomRollUpReport(@PathVariable("itemId") Integer itemId, @RequestBody List<PLMItemTypeAttribute> itemTypeAttributes) {
        return bomService.getItemBomRollUpReport(itemId, itemTypeAttributes);
    }

    @RequestMapping(value = "/[{attributeIds}]/rollup/report/children", method = RequestMethod.GET)
    public BomRollUpReportDto getBomItemChildrenReport(@PathVariable("itemId") Integer itemId, @PathVariable("attributeIds") Integer[] attributeIds) {
        return null;
    }

    @RequestMapping(value = "/compliancereport", method = RequestMethod.GET)
    public BomComplianceReport getBomComplianceReport(@PathVariable("itemId") Integer itemId) {
        return bomService.getBomComplianceReport(itemId);
    }

    @RequestMapping(value = "/whereUsedReport", method = RequestMethod.GET)
    public BomWhereUsedReport getBomWhereUsedReport(@PathVariable("itemId") Integer itemId) {
        return bomService.getBomWhereUsedReport(itemId);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<BomDto> getTotalBom(@PathVariable("itemId") Integer itemId, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy,
                                    @RequestParam(value = "bomRule", required = false) String bomRule) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }

        if (bomRule == null) {
            bomRule = "bom.latest";
        }
        return bomService.getTotalBom(itemId, hierarchy, bomRule);
    }

    @RequestMapping(value = "/whereUsedReport/search/[{ids}]", method = RequestMethod.GET)
    public BomWhereUsedReport getBomWhereUsedReportByIds(@PathVariable("itemId") Integer itemId, @PathVariable("ids") Integer[] ids) {
        return bomService.getBomWhereUsedReportByIds(itemId, Arrays.asList(ids));
    }

    @RequestMapping(value = "/attribute/{attributeId}/value", method = RequestMethod.GET)
    public List<String> getAttributeValueUsedInConfigurations(@PathVariable("itemId") Integer itemId, @PathVariable("attributeId") Integer attributeId,
                                                              @RequestParam("value") String value) {
        return bomService.getAttributeValueUsedInConfigurations(itemId, attributeId, value);
    }

    @RequestMapping(value = "/config/items/inclusions", method = RequestMethod.POST)
    private List<ASBOMConfigItemInclusion> createBomConfigItemInclusions(@PathVariable("itemId") Integer itemId,
                                                                         @RequestBody List<ASBOMConfigItemInclusion> configItemInclusions) {
        return bomService.createBomConfigItemInclusions(itemId, configItemInclusions);
    }

    @RequestMapping(value = "/nonconfig/items/inclusions", method = RequestMethod.POST)
    private List<ASBOMConfigItemInclusion> createBomNonConfigItemInclusions(@PathVariable("itemId") Integer itemId,
                                                                            @RequestBody List<ASBOMConfigItemInclusion> configItemInclusions) {
        return bomService.createBomNonConfigItemInclusions(itemId, configItemInclusions);
    }

    @RequestMapping(value = "/attributes/items/exclusions", method = RequestMethod.POST)
    private List<ASBOMConfigItemInclusion> createBomConfigAttributeExclusions(@PathVariable("itemId") Integer itemId,
                                                                              @RequestBody List<ASBOMConfigItemInclusion> configItemInclusions) {
        return bomService.createBomConfigAttributeExclusions(itemId, configItemInclusions);
    }

    @RequestMapping(value = "/substituteBomItem", method = RequestMethod.POST)
    public PLMBom substituteBomItem(@PathVariable("itemId") Integer itemId, @RequestBody PLMBom substituteBomItem) {
        return bomService.substituteBomItem(itemId, substituteBomItem);
    }
}
