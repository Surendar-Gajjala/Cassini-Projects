package com.cassinisys.drdo.controller.bom;

import com.cassinisys.drdo.filtering.BomSearchCriteria;
import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.*;
import com.cassinisys.drdo.service.bom.BomService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by subramanyam reddy on 08-10-2018.
 */
@RestController
@RequestMapping("drdo/bom")
public class BomController extends BaseController {

    @Autowired
    private BomService bomService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public Bom create(@RequestBody Bom bom) {
        return bomService.create(bom);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Bom update(@PathVariable("id") Integer id,
                      @RequestBody Bom itemType) {
        return bomService.update(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        bomService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Bom get(@PathVariable("id") Integer id) {
        return bomService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Bom> getAll() {
        return bomService.getAll();
    }

    @RequestMapping(value = "/bomTree", method = RequestMethod.GET)
    public List<Bom> getBomTree() {
        return bomService.getBomTree();
    }

    @RequestMapping(value = "/structure/tree/{bomId}", method = RequestMethod.GET)
    public List<BomItem> getBomStructure(@PathVariable("bomId") Integer bomId) {
        return bomService.getBomStructure(bomId);
    }

    @RequestMapping(value = "/update/uniqueCode", method = RequestMethod.GET)
    public void updateUniqueCodes() {
        bomService.updateUniqueCodes();
    }

    @RequestMapping(value = "/{bomItemId}/parts", method = RequestMethod.GET)
    public StoragePartsDto getBomPartsByBomType(@PathVariable("bomItemId") Integer bomItemId, @RequestParam("storageId") Integer storageId) {
        return bomService.getBomPartsByBomType(bomItemId, storageId);
    }

    @RequestMapping(value = "/{bomItemId}/storage/{storageId}/parts", method = RequestMethod.GET)
    public StoragePartsDto getBomPartsByBomTypeAndText(@PathVariable("bomItemId") Integer bomItemId, @PathVariable("storageId") Integer storageId, @RequestParam("searchText") String searchText) {
        return bomService.getBomPartsByBomTypeAndText(bomItemId, storageId, searchText);
    }

    /*@RequestMapping(value = "/{bomItemId}/storage/{storageId}/items", method = RequestMethod.GET)
    public StoragePartsDto searchBomItems(@PathVariable("bomItemId") Integer bomItemId, @PathVariable("storageId") Integer storageId, @RequestParam("searchText") String searchText) {
        return bomService.searchBomItems(bomItemId, storageId, searchText);
    }*/

    @RequestMapping(value = "/storage/{storageId}/searchItems", method = RequestMethod.GET)
    public Page<BomItem> searchBomItems(@PathVariable("storageId") Integer storageId, BomSearchCriteria bomSearchCriteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bomService.getInwardSearchResults(bomSearchCriteria, pageable);
    }

    @RequestMapping(value = "/{itemId}/item", method = RequestMethod.POST)
    public BomItem createBomItem(@PathVariable("itemId") Integer itemId, @RequestBody BomItem bomItem) {
        return bomService.createBomItem(itemId, bomItem);
    }

    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.PUT)
    public BomItem updateBomItem(@PathVariable("itemId") Integer itemId, @RequestBody BomItem bomItem) {
        return bomService.updateBomItem(itemId, bomItem);
    }

    @RequestMapping(value = "/item/delete/{bomItemId}", method = RequestMethod.DELETE)
    public void deleteBomItem(@PathVariable("bomItemId") Integer bomItemId) {
        bomService.deleteBomItem(bomItemId);
    }

    @RequestMapping(value = "/items/{itemId}", method = RequestMethod.GET)
    public List<BomItem> getItemBom(@PathVariable("itemId") Integer itemId, @RequestParam("versity") Boolean versity) {
        return bomService.getItemBom(itemId, versity);
    }

    @RequestMapping(value = "/{bomId}/synchronize", method = RequestMethod.GET)
    public Bom synchronizeBom(@PathVariable("bomId") Integer bomId) {
        return bomService.synchronizeBom(bomId);
    }


    @RequestMapping(value = "/{bomId}/synchronize/{sectionId}", method = RequestMethod.GET)
    public BomItem synchronizeBomSection(@PathVariable("bomId") Integer bomId, @PathVariable("sectionId") Integer sectionId) {
        return bomService.synchronizeBomSection(bomId, sectionId);
    }

    @RequestMapping(value = "/{bomId}/synchronize/byunit/{unitId}", method = RequestMethod.GET)
    public BomItem synchronizeBomUnit(@PathVariable("bomId") Integer bomId, @PathVariable("unitId") Integer unitId) {
        return bomService.synchronizeBomUnit(bomId, unitId);
    }

    @RequestMapping(value = "/items/{itemId}/children", method = RequestMethod.GET)
    public List<BomItem> getBomItemChildren(@PathVariable("itemId") Integer itemId) {
        return bomService.getBomItemChildren(itemId);
    }

    @RequestMapping(value = "/bomItem/{itemId}/children", method = RequestMethod.GET)
    public List<BomItem> getChildrenByBomItem(@PathVariable("itemId") Integer itemId) {
        return bomService.getChildrenByBomItem(itemId);
    }

    @RequestMapping(value = "/items/instance/{itemId}", method = RequestMethod.GET)
    public List<BomInstanceItem> getItemInstanceBom(@PathVariable("itemId") Integer itemId, @RequestParam("versity") Boolean versity) {
        return bomService.getItemInstanceBom(itemId, versity);
    }

    @RequestMapping(value = "/{itemId}/item/new", method = RequestMethod.POST)
    public BomItem createNewBomItem(@PathVariable("itemId") Integer itemId, @RequestBody BomItem bomItem) {
        return bomService.createNewBomItem(itemId, bomItem);
    }


    @RequestMapping(value = "/instance/{itemId}", method = RequestMethod.PUT)
    public BomInstanceItem updateItemInstance(@PathVariable("itemId") Integer itemId, @RequestBody BomInstanceItem bomInstanceItem) {
        bomInstanceItem.setId(itemId);
        return bomService.updateBomInstanceItem(bomInstanceItem);
    }

    @RequestMapping(value = "/instance/{itemId}", method = RequestMethod.POST)
    public List<BomInstance> createBomInstance(@PathVariable("itemId") Integer itemId, @RequestBody List<String> instances) {
        return bomService.createBomInstance(itemId, instances);
    }

    @RequestMapping(value = "/instance/{itemId}", method = RequestMethod.DELETE)
    public void deleteBomInstance(@PathVariable("itemId") Integer itemId) {
        bomService.deleteBomInstance(itemId);
    }

    @RequestMapping(value = "/instance/{itemId}", method = RequestMethod.GET)
    public List<ItemInstance> getBomInstance(@PathVariable("itemId") Integer itemId) {
        return bomService.getBomInstance(itemId);
    }

    @RequestMapping(value = "/instances/{itemId}", method = RequestMethod.GET)
    public List<BomInstance> getBomInstances(@PathVariable("itemId") Integer itemId) {
        return bomService.getBomInstances(itemId);
    }

    @RequestMapping(value = "/instances/byId/{itemId}", method = RequestMethod.GET)
    public BomInstanceItem getBomInstancesById(@PathVariable("itemId") Integer itemId) {
        return bomService.getBomInstanceById(itemId);
    }

    @RequestMapping(value = "items/instance/{itemId}/toggle", method = RequestMethod.GET)
    public List<BomInstanceItem> getBomInstanceToggle(@PathVariable("itemId") Integer itemId) {
        return bomService.getItemInstanceToggle(itemId);
    }

    @RequestMapping(value = "/inwardSearchResults", method = RequestMethod.GET)
    public Page<BomItem> getInwardSearchResults(BomSearchCriteria bomSearchCriteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bomService.getInwardSearchResults(bomSearchCriteria, pageable);
    }


    @RequestMapping(value = "/{bomId}/search", method = RequestMethod.GET)
    public List<BomItem> searchBom(@PathVariable("bomId") Integer bomId, @RequestParam("searchText") String searchText,
                                   @RequestParam("versity") Boolean versity) {
        return bomService.searchBom(bomId, searchText, versity);
    }


    @RequestMapping(value = "/requestSearchResults", method = RequestMethod.GET)
    public Page<BomInstanceItem> getRequestSearchResults(BomSearchCriteria bomSearchCriteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bomService.getRequestSearchResults(bomSearchCriteria, pageable);
    }

    @RequestMapping(value = "/requestSearch/section", method = RequestMethod.GET)
    public Page<BomInstanceItem> getRequestSearchBySection(BomSearchCriteria bomSearchCriteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bomService.getRequestSearchBySection(bomSearchCriteria, pageable);
    }

    @RequestMapping(value = "/by/section/{path}", method = RequestMethod.GET)
    public List<BomInstanceItem> getPartsBySection(@PathVariable("path") String path) {
        path = path.replace("%2F", "/");
        return bomService.getPartsBySection(path);
    }

    @RequestMapping(value = "/drdologo", method = RequestMethod.GET, produces = "text/html")
    public String getDrdoLogo() {
        return bomService.getDrdoLogoImage();
    }

    @RequestMapping(value = "/{bomItemId}/structure", method = RequestMethod.GET)
    public List<BomItem> getBomItemStructure(@PathVariable("bomItemId") Integer bomItemId) {
        return bomService.getBomItemStructure(bomItemId);
    }

    @RequestMapping(value = "/sections/structure/[{sectionIds}]", method = RequestMethod.GET)
    public List<BomItem> getBomSectionsStructure(@PathVariable("sectionIds") Integer[] sectionIds) {
        return bomService.getBomSectionsStructure(Arrays.asList(sectionIds));
    }

    @RequestMapping(value = "/instance/requestReport/{instanceId}", method = RequestMethod.GET)
    public List<BomInstanceInventoryDto> getRequestReportByInstance(@PathVariable("instanceId") Integer instanceId) {
        return bomService.getRequestReportByInstance(instanceId);
    }

    @RequestMapping(value = "/instance/requestReport/{instanceId}/section/{sectionId}", method = RequestMethod.GET)
    public List<BomInstanceInventoryDto> getRequestReportBySection(@PathVariable("instanceId") Integer instanceId, @PathVariable("sectionId") Integer sectionId) {
        return bomService.getRequestReportBySection(instanceId, sectionId);
    }

    @RequestMapping(value = "/{bomId}/sections", method = RequestMethod.GET)
    public List<BomItem> getSectionsByBom(@PathVariable("bomId") Integer bomId) {
        return bomService.getSectionsByBom(bomId);
    }

    @RequestMapping(value = "/instance/{instanceId}/sections", method = RequestMethod.GET)
    public List<BomInstanceItem> getSectionsByInstance(@PathVariable("instanceId") Integer instanceId, @RequestParam("admin") Boolean admin,
                                                       @RequestParam("versity") Boolean versity) {
        return bomService.getSectionsByInstance(instanceId, admin, versity);
    }

    @RequestMapping(value = "/instance/{itemId}/children", method = RequestMethod.GET)
    public List<BomInstanceItem> getChildrenByItem(@PathVariable("itemId") Integer itemId) {
        return bomService.getChildrenByItem(itemId);
    }

    @RequestMapping(value = "/instance/{instanceId}/reqItems", method = RequestMethod.POST)
    public RequestSectionDto getSectionItemsByInstanceAndSection(@PathVariable("instanceId") Integer instanceId, @RequestBody BomInstanceItem bomInstanceItem) {
        return bomService.getSectionBomInstances(instanceId, bomInstanceItem);
    }

    @RequestMapping(value = "/instance/{instanceId}/report", method = RequestMethod.POST)
    public RequestSectionDto getReportByInstanceChildren(@PathVariable("instanceId") Integer instanceId, @RequestBody BomInstanceItem bomInstanceItem) {
        return bomService.getReportByInstanceChildren(instanceId, bomInstanceItem);
    }

    @RequestMapping(value = "/items/report", method = RequestMethod.POST)
    public RequestSectionDto searchItemReport(@RequestBody Integer[] selectedMissileIds, BomSearchCriteria bomSearchCriteria) {
        return bomService.searchItemReport(bomSearchCriteria, selectedMissileIds);
    }

    @RequestMapping(value = "/instance/{instanceId}/reqItems/[{unitIds}]", method = RequestMethod.GET)
    public RequestSectionDto getBomItemsByUnits(@PathVariable("instanceId") Integer instanceId, @PathVariable Integer[] unitIds) {
        return bomService.getBomItemsByUnits(instanceId, Arrays.asList(unitIds));
    }

    @RequestMapping(value = "/bomInstance/{itemId}", method = RequestMethod.PUT)
    public BomInstance updateBomInstance(@PathVariable("itemId") Integer itemId, @RequestBody BomInstance bomInstance) {
        bomInstance.setId(itemId);
        return bomService.updateBomInstance(bomInstance);
    }

    @RequestMapping(value = "/bomInstance/byId/{itemId}", method = RequestMethod.GET)
    public BomInstance findBomInstance(@PathVariable("itemId") Integer itemId) {
        return bomService.findBomInstance(itemId);
    }

    @RequestMapping(value = "/{bomId}/validate/storage", method = RequestMethod.GET)
    public List<StorageBomItemDto> validateStorage(@PathVariable("bomId") Integer bomId) {
        return bomService.validateStorage(bomId);
    }

    @RequestMapping(value = "/instance/{instanceId}/document", method = RequestMethod.GET)
    public BomInstanceTarbDto getBomInstanceTarbDocument(@PathVariable("instanceId") Integer instanceId) {
        return bomService.getBomInstanceTarbDocument(instanceId);
    }

    @RequestMapping(value = "/instance/{instanceId}/section/{sectionId}/document", method = RequestMethod.GET)
    public BomInstanceTarbDto getBomInstanceSectionTarbDocument(@PathVariable("instanceId") Integer instanceId, @PathVariable("sectionId") Integer sectionId) {
        return bomService.getBomInstanceSectionTarbDocument(instanceId, sectionId);
    }

    @RequestMapping(value = "{instanceId}/sections/requestItems", method = RequestMethod.GET)
    public List<SectionRequestedItemsDto> getInstanceRequestedItems(@PathVariable("instanceId") Integer instanceId) {
        return bomService.getInstanceRequestedItems(instanceId);
    }


    @RequestMapping(value = "{instanceId}/section/{sectionId}/requestItems", method = RequestMethod.GET)
    public List<RequestedItemsDto> getInstanceSectionRequestedItems(@PathVariable("instanceId") Integer instanceId, @PathVariable("sectionId") Integer sectionId,
                                                                    @RequestParam("searchText") String searchText) {
        return bomService.getInstanceSectionRequestedItems(instanceId, sectionId, searchText);
    }

    @RequestMapping(value = "bomInstanceItem/byInstance/{instanceId}", method = RequestMethod.GET)
    public BomInstanceItem getBomInstanceItemByItemInstance(@PathVariable("instanceId") Integer instanceId) {
        return bomService.findBomInstanceItemByItemInstance(instanceId);
    }

    @RequestMapping(value = "/{parentId}/import", method = RequestMethod.POST)
    public void importBOM(@PathVariable("parentId") Integer parentId, MultipartHttpServletRequest request) throws Exception {
        bomService.importBOM(parentId, request);
    }

    @RequestMapping(value = "/workCenter/items", method = RequestMethod.GET)
    public List<SectionBomItemsDto> getItemsByBomAndWorkCenter(PageRequest pageRequest, BomSearchCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bomService.getItemsByBomAndWorkCenter(criteria, pageable);
    }

    @RequestMapping(value = "/sections/byBom/{itemId}", method = RequestMethod.GET)
    public List<BomItem> getSectionByBomId(@PathVariable("itemId") Integer itemId) {
        return bomService.findSectionByBomId(itemId);
    }
}