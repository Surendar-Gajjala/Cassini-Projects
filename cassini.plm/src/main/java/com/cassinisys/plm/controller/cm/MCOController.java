package com.cassinisys.plm.controller.cm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.DCRItemsCriteria;
import com.cassinisys.plm.filtering.ItemMCOCriteria;
import com.cassinisys.plm.filtering.MCOCriteria;
import com.cassinisys.plm.filtering.ManufacturerMCOCriteria;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.MCOAffecteditemsDto;
import com.cassinisys.plm.model.cm.dto.MCOProductAffectedItemDto;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mobile.MCODetails;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.cm.MCOService;
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
@RequestMapping("/cms/mcos")
@Api(tags = "PLM.CM", description = "Changes Related")
public class MCOController extends BaseController {

    @Autowired
    private MCOService mcoService;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private PLMWorkflowService workflowService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMMCO create(@RequestBody PLMMCO mco) {
        return mcoService.create(mco);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMMCO update(@PathVariable("id") Integer id,
                         @RequestBody PLMMCO mco) {
        return mcoService.update(mco);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        mcoService.delete(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public MCODetails getMCODetails(@PathVariable("id") Integer id) {
        return mcoService.getMCODetails(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMMCO get(@PathVariable("id") Integer id) {
        return mcoService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMMCO> getAll() {
        return mcoService.getAll();
    }

    @RequestMapping(value = "/changeAnalysts/{type}", method = RequestMethod.GET)
    public List<Person> getChangeAnalysts(@PathVariable("type") String type) {
      return mcoService.getChangeAnalysts(type);
    }

    @RequestMapping(value = "/status/{type}", method = RequestMethod.GET)
    public List<String> getStatus(@PathVariable("type") String type) {
        return mcoService.getStatus(type);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMMCO> getMultiple(@PathVariable Integer[] ids) {
        return mcoService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<PLMMCO> getAllMCOs(PageRequest pageRequest, MCOCriteria mcoCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return mcoService.getAllMCOs(pageable, mcoCriteria);
    }

    @RequestMapping(value = "/item/all", method = RequestMethod.GET)
    public Page<PLMItemMCO> getAllItemMCOs(PageRequest pageRequest, ItemMCOCriteria mcoCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return mcoService.getAllItemMCOs(pageable, mcoCriteria);
    }

    @RequestMapping(value = "/manufacturer/all", method = RequestMethod.GET)
    public Page<PLMManufacturerMCO> getAllManufacturerMCOs(PageRequest pageRequest, ManufacturerMCOCriteria mcoCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return mcoService.getAllManufacturerMCOs(pageable, mcoCriteria);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return mcoService.attachDCRWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{mfrId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("mfrId") Integer mfrId) {
        workflowService.deleteWorkflow(mfrId);
    }

    @RequestMapping(value = "{id}/affectedItem", method = RequestMethod.POST)
    public PLMMCOAffectedItem createMcoItem(@PathVariable("id") Integer id, @RequestBody PLMMCOAffectedItem item) {
        return mcoService.createAffectedItem(id, item);
    }

    @RequestMapping(value = "{id}/productAffectedItem", method = RequestMethod.POST)
    public PLMMCOProductAffectedItem createMcoMbom(@PathVariable("id") Integer id, @RequestBody PLMMCOProductAffectedItem mbom) {
        return mcoService.createProductAffectedItem(id, mbom);
    }

    @RequestMapping(value = "{id}/productAffectedItem/multiple", method = RequestMethod.POST)
    public List<PLMMCOProductAffectedItem> createMcoMboms(@PathVariable("id") Integer id, @RequestBody List<PLMMCOProductAffectedItem> mboms) {
        return mcoService.createProductAffectedItems(id, mboms);
    }

    @RequestMapping(value = "{id}/productAffectedItem/{mbomId}", method = RequestMethod.PUT)
    public PLMMCOProductAffectedItem updateMcoMbom(@PathVariable("id") Integer id, @RequestBody PLMMCOProductAffectedItem mbom) {
        return mcoService.updateProductAffectedItem(id, mbom);
    }

    @RequestMapping(value = "{id}/productAffectedItem/delete/{item}", method = RequestMethod.DELETE)
    public void deleteMcoProductAffectedItem(@PathVariable("item") Integer mbom) {
        mcoService.deleteMcoProductAffectedItem(mbom);
    }

    @RequestMapping(value = "/productAffectedItems/{id}", method = RequestMethod.GET)
    public List<MCOProductAffectedItemDto> getProductAffectedItem(@PathVariable("id") Integer id) {
        return mcoService.getProductAffectedItem(id);
    }

    @RequestMapping(value = "{id}/affectedItem/multiple", method = RequestMethod.POST)
    public List<PLMMCOAffectedItem> createMcoItems(@PathVariable("id") Integer id, @RequestBody List<PLMMCOAffectedItem> items) {
        return mcoService.createAffectedItems(id, items);
    }

    @RequestMapping(value = "{id}/affectedItem/{itemId}", method = RequestMethod.PUT)
    public PLMMCOAffectedItem updateMcoItem(@PathVariable("id") Integer id, @RequestBody PLMMCOAffectedItem item) {
        return mcoService.updateAffectedItem(id, item);
    }

    @RequestMapping(value = "/affectedItems/{id}", method = RequestMethod.GET)
    public List<MCOAffecteditemsDto> getAffectedItem(@PathVariable("id") Integer id) {
        return mcoService.getAffectedItem(id);
    }

    @RequestMapping(value = "/filteredItems", method = RequestMethod.GET)
    public Page<PLMItem> getFilteredItems(PageRequest pageRequest, DCRItemsCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return mcoService.getFilterBomItems(pageable, criteria);
    }

    @RequestMapping(value = "/{mcoId}/relatedItems", method = RequestMethod.POST)
    public PLMMCORelatedItem createMcoRelatedItem(@PathVariable("mcoId") Integer mcoId,
                                                  @RequestBody PLMMCORelatedItem relatedItem) {
        return mcoService.createMcoRelatedItem(relatedItem);
    }

    @RequestMapping(value = "/{mcoId}/relatedItems/{id}", method = RequestMethod.PUT)
    public PLMMCORelatedItem updateMcoRelatedItem(@PathVariable("mcoId") Integer mcoId, @PathVariable("id") Integer id,
                                                  @RequestBody PLMMCORelatedItem relatedItem) {
        return mcoService.updateMcoRelatedItem(relatedItem);
    }

    @RequestMapping(value = "/{mcoId}/relatedItems/multiple", method = RequestMethod.POST)
    public List<PLMMCORelatedItem> createMcoRelatedItems(@PathVariable("mcoId") Integer mcoId,
                                                         @RequestBody List<PLMMCORelatedItem> items) {
        return mcoService.createMcoRelatedItems(mcoId, items);
    }

    @RequestMapping(value = "/relatedItems/{id}", method = RequestMethod.GET)
    public List<PLMMCORelatedItem> getMcoRelatedItems(@PathVariable("id") Integer id) {
        return mcoService.getMcoRelatedItems(id);
    }

    @RequestMapping(value = "{id}/affectedItem/delete/{item}", method = RequestMethod.DELETE)
    public void deleteMcoAffectedItem(@PathVariable("item") Integer item) {
        mcoService.deleteMcoAffectedItem(item);
    }

    @RequestMapping(value = "{id}/relatedItem/{item}", method = RequestMethod.DELETE)
    public void deleteMcoRelatedItem(@PathVariable("item") Integer item) {
        mcoService.deleteMcoRelatedItem(item);
    }

    @RequestMapping(value = "{id}/details/count", method = RequestMethod.GET)
    public ItemDetailsDto getMcoDetailsCount(@PathVariable("id") Integer id) {
        return mcoService.getMcoDetailsCount(id);
    }

    @RequestMapping(value = "{id}/aml/parts", method = RequestMethod.GET)
    public List<MCOAffecteditemsDto> getAmlParts(@PathVariable("id") Integer id) {
        return mcoService.getAmlParts(id);
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.PUT)
    public PLMItemMCO updateItemMco(@PathVariable("id") Integer id,
                                    @RequestBody PLMItemMCO itemMCO) {
        return mcoService.updateItemMco(itemMCO);
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
    public PLMItemMCO getItemMco(@PathVariable("id") Integer id) {
        return mcoService.getItemMco(id);
    }

    @RequestMapping(value = "/materials/{id}", method = RequestMethod.PUT)
    public PLMManufacturerMCO updateMaterialMco(@PathVariable("id") Integer id,
                                                @RequestBody PLMManufacturerMCO manufacturerMCO) {
        return mcoService.updateMaterialMco(manufacturerMCO);
    }

    @RequestMapping(value = "/materials/{id}", method = RequestMethod.GET)
    public PLMManufacturerMCO getMaterialMco(@PathVariable("id") Integer id) {
        return mcoService.getMaterialMco(id);
    }

}
