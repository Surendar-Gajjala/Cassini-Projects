package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.cm.PLMChange;
import com.cassinisys.plm.model.cm.PLMDCR;
import com.cassinisys.plm.model.cm.PLMECR;
import com.cassinisys.plm.model.dto.*;
import com.cassinisys.plm.model.mfr.PLMItemManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.pgc.PGCItemSpecification;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.AlternatePartDto;
import com.cassinisys.plm.model.plm.dto.BomDto;
import com.cassinisys.plm.model.plm.dto.SubstitutePartDto;
import com.cassinisys.plm.model.pqm.PQMProblemReport;
import com.cassinisys.plm.model.pqm.PQMQCR;
import com.cassinisys.plm.model.rm.RequirementDeliverable;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.cm.DCRService;
import com.cassinisys.plm.service.cm.ECOService;
import com.cassinisys.plm.service.cm.ECRService;
import com.cassinisys.plm.service.exim.importer.Importer;
import com.cassinisys.plm.service.exim.importer.ItemImporter;
import com.cassinisys.plm.service.mfr.ManufacturerPartService;
import com.cassinisys.plm.service.plm.BomService;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.cassinisys.plm.service.pqm.ProblemReportService;
import com.cassinisys.plm.service.pqm.QCRService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.Predicate;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plm/items")
@Api(tags = "PLM.ITEMS", description = "Items Related")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ItemPredicateBuilder predicateBuilder;

    @Autowired
    private BomService bomService;

    @Autowired
    private ECOService ecoService;

    @Autowired
    private DCOService dcoService;

    @Autowired
    private ECRService ecrService;

    @Autowired
    private DCRService dcrService;

    @Autowired
    private ManufacturerPartService manufacturerPartService;

    @Autowired
    private PLMWorkflowService workflowService;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private ProblemReportService problemReportService;

    @Autowired
    private QCRService qcrService;

    @Autowired
    private Importer importer;

    @Autowired
    private ItemImporter itemImporter;

    // Item Methods

    // To create Item
    @RequestMapping(method = RequestMethod.POST)
    public PLMItem create(@RequestBody @Valid PLMItem item) {
        return itemService.create(item);
    }

    @RequestMapping(value = "/revisions/copy", method = RequestMethod.POST)
    public PLMItem createCopyItem(@RequestBody @Valid PLMItem item) {
        return itemService.createCopyItem(item);
    }

    // To Update Item
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMItem update(@PathVariable("id") Integer id,
                          @RequestBody PLMItem item) {
        item.setId(id);
        return itemService.update(item);
    }

    // To Delete Item
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        itemService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMItem get(@PathVariable("id") Integer id) {
        return itemService.get(id);
    }

    @RequestMapping(value = "/export/all", method = RequestMethod.GET)
    public List<PLMItem> getAll() {
        return itemService.getAll();
    }

    // To Get multiple Items by multiple Ids
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMItem> getMultiple(@PathVariable Integer[] ids) {
        return itemService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long getItemMasterCount() {
        return itemService.getItemMasterCount();
    }

    @RequestMapping(value = "/revisions/count", method = RequestMethod.GET)
    public Long getItemRevisionCount() {
        return itemService.getItemRevisionCount();
    }

    // To Get Specific Item by RevisionId
    @RequestMapping(value = "/revisions/{id}/item", method = RequestMethod.GET)
    public PLMItem getItemByRevision(@PathVariable("id") Integer id) {
        return itemService.getItemByRevision(id);
    }

    // To Get All Items
    @RequestMapping(method = RequestMethod.GET)
    public Page<PLMItem> getPagebleItems(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.findAll(pageable);
    }

    // To Get All Items
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<ItemsDto> getAllItems(PageRequest pageRequest, ItemCriteria itemCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getAllItems(pageable, itemCriteria);
    }

    @RequestMapping(value = "/class/{itemClass}", method = RequestMethod.GET)
    public Page<PLMItem> getItemsByClass(@PathVariable("itemClass") ItemClass itemClass, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getItemsByClass(itemClass, pageable);
    }

    @RequestMapping(value = "/filteredItems", method = RequestMethod.GET)
    public Page<PLMItem> getFilteredItems(PageRequest pageRequest, BomItemFilterCriteria bomItemFilterCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getFilterBomItems(pageable, bomItemFilterCriteria);
    }

    @RequestMapping(value = {"/{itemId}/latest"}, method = {RequestMethod.GET})
    public PLMItemRevision getLatestRevision(@PathVariable("itemId") Integer itemId) {
        return itemService.getLatestRevision(itemId);
    }

    @RequestMapping(value = {"/multiple/[{itemIds}]/latest"}, method = {RequestMethod.GET})
    public List<PLMItemRevision> getItemRevisionsByItemIds(@PathVariable("itemIds") Integer[] itemIds) {
        return itemService.getItemRevisionsByItemIds(Arrays.asList(itemIds));
    }

    // To Create ItemRevision
    @RequestMapping(value = "{itemId}/revisions", method = RequestMethod.POST)
    public PLMItemRevision createRevision(@PathVariable("itemId") Integer itemId, @RequestBody @Valid PLMItemRevision itemRevision) {
        return itemService.createRevision(itemRevision);
    }

    // To Get All Item revisions
    @RequestMapping(value = "/revisions", method = RequestMethod.GET)
    public Page<PLMItemRevision> getAllLatestItemRevisions(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getAllLatestItemRevisions(pageable);
    }

    // To Get All Item revisions
    @RequestMapping(value = "/revisions/search", method = RequestMethod.GET)
    public Page<PLMItemRevision> searchItemRevisions(PageRequest pageRequest, ItemCriteria itemCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.searchItemRevisions(pageable, itemCriteria);
    }

    //To update Item By RevisionId
    @RequestMapping(value = "/revisions/{itemRevisionId}", method = RequestMethod.PUT)
    public PLMItemRevision updateRevision(@PathVariable("itemRevisionId") Integer itemRevisionId,
                                          @RequestBody PLMItemRevision itemRevision) {
        itemRevision.setId(itemRevisionId);
        return itemService.updateRevision(itemRevision);
    }

    @RequestMapping(value = "/revisions/{itemRevisionId}", method = RequestMethod.DELETE)
    public void deleteItemRevision(@PathVariable("itemRevisionId") Integer itemRevisionId) {
        itemService.deleteItemRevision(itemRevisionId);
    }

    @RequestMapping(value = "/revisions/{itemRevisionId}", method = RequestMethod.GET)
    public PLMItemRevision getItemRevision(@PathVariable("itemRevisionId") Integer itemRevisionId) {
        return itemService.getItemRevision(itemRevisionId);
    }

    //To Get Item Revisions by Passing ItemId
    @RequestMapping(value = "/{itemId}/revisions", method = RequestMethod.GET)
    public List<PLMItemRevision> getItemRevisions(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemRevisions(itemId);
    }

    @RequestMapping(value = "/{itemId}/revisions/ids", method = RequestMethod.GET)
    public List<Integer> getItemRevisionIds(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemRevisionIds(itemId);
    }

    @RequestMapping(value = "/{itemId}/revisions/history", method = RequestMethod.GET)
    public List<PLMItemRevision> getItemRevisionStatusHistory(@PathVariable("itemId") Integer item) {
        return itemService.getItemRevisionStatusHistory(item);
    }

    @RequestMapping(value = {"/revisions/multiple"}, method = {RequestMethod.POST})
    public List<PLMItemRevision> getRevisionsByIds(@RequestBody Integer[] itemRevisionIds) {
        return itemService.getRevisionsByIds(Arrays.asList(itemRevisionIds));
    }

    @RequestMapping(value = "/pageable/[{ids}]", method = RequestMethod.GET)
    public List<PLMItem> getPageableItemsByIds(@PathVariable Integer[] ids, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getPageableItemsByIds(Arrays.asList(ids), pageable);
    }

    @RequestMapping(value = "/objectAttributes", method = RequestMethod.POST)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByItemAndAttributeId(@RequestBody List<Integer[]> ids) {
        Integer[] itemsIds = ids.get(0);
        Integer[] attIds = ids.get(1);
        return itemService.getObjectAttributesByItemIdsAndAttributeIds(itemsIds, attIds);
    }

    // To Get Item by ItemNumber
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public List<PLMItem> find(@RequestParam("itemNumber") String itemNumber) {
        return itemService.findByItemNumber(itemNumber);
    }

    // To Get Items by ItemType
    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<PLMItem> getItemsByType(@PathVariable("typeId") Integer id,
                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getItemsByType(id, pageable);
    }

    @RequestMapping(value = "/itemType/{typeId}", method = RequestMethod.GET)
    public List<PLMItem> getItemByItemType(@PathVariable("typeId") Integer id) {
        return itemService.getItemByItemType(id);
    }

    @RequestMapping(value = "/itemType/{typeId}/normalconfigurable", method = RequestMethod.GET)
    public List<PLMItem> getNormalAndConfigurableItemByItemType(@PathVariable("typeId") Integer id) {
        return itemService.getNormalAndConfigurableItemByItemType(id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<PLMItem> search(ItemCriteria criteria, PageRequest pageRequest) {
        Predicate predicate = predicateBuilder.build(criteria,
                QPLMItem.pLMItem);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMItem> pdmItems = itemService.searchItems(predicate, pageable);
        return pdmItems;
    }

    @RequestMapping(value = "/advancedsearch", method = RequestMethod.POST)
    public Page<ItemsDto> advancedSearch(@RequestBody ParameterCriteria[] parameterCriterias, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.advancedSearchItem(parameterCriterias, pageable);
    }

    @RequestMapping(value = "/freetextsearch", method = RequestMethod.GET)
    public Page<PLMItem> freeTextSearch(PageRequest pageRequest, ItemCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMItem> pdmItems = itemService.freeTextSearch(pageable, criteria);
        return pdmItems;
    }

    @RequestMapping(value = "/search/mobile", method = RequestMethod.GET)
    public Page<ItemDto> searchItems(PageRequest pageRequest, ItemCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ItemDto> pdmItems = itemService.searchItemsForMobile(pageable, criteria);
        return pdmItems;
    }

    @RequestMapping(value = "/search/all", method = RequestMethod.GET)
    public List<PLMItem> searchAll(ItemCriteria criteria, PageRequest pageRequest) {
        Predicate predicate = predicateBuilder.build(criteria,
                QPLMItem.pLMItem);
        List<PLMItem> pdmItems = itemService.searchItemsAll(predicate);
        return pdmItems;
    }

    @RequestMapping(value = "/advancedsearch/all", method = RequestMethod.POST)
    public List<PLMItem> advancedSearchAll(@RequestBody ParameterCriteria[] parameterCriterias) {
        List<PLMItem> pdmItems = itemService.advancedSearchItemAll(parameterCriterias);
        return pdmItems;
    }

    @RequestMapping(value = "/freetextsearch/all", method = RequestMethod.GET)
    public List<PLMItem> freeTextSearchAll(PageRequest pageRequest, ItemCriteria criteria) {
        List<PLMItem> pdmItems = itemService.freeTextSearchAll(criteria);
        return pdmItems;
    }

    // To Get Specific Revision By Id
    @RequestMapping(value = "/revision/{revisionId}", method = RequestMethod.GET)
    public PLMItemRevision getRevision(@PathVariable("revisionId") Integer revisionId) {
        return itemService.getRevision(revisionId);
    }

    @RequestMapping(value = "/{itemId}/multiple/mpns", method = RequestMethod.POST)
    public List<PLMItemManufacturerPart> createItemMfrParts(@PathVariable("itemId") Integer itemId,
                                                            @RequestBody List<PLMItemManufacturerPart> itemManufacturerPart) {
        return itemService.createManufacturerParts(itemId, itemManufacturerPart);
    }

    @RequestMapping(value = "/mpns/{mfrPart}", method = RequestMethod.DELETE)
    public void deleteItemMfrPart(@PathVariable("mfrPart") Integer mfrPart) {
        itemService.deleteItemMfrPart(mfrPart);
    }

    @RequestMapping(value = "/{itemId}/mfrPart/{mfrPart}", method = RequestMethod.DELETE)
    public void deleteMfrPartByItem(@PathVariable("itemId") Integer itemId, @PathVariable("mfrPart") Integer mfrPart) {
        itemService.deleteMfrPartByItem(itemId, mfrPart);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PLMItemAttribute createItemAttribute(@PathVariable("id") Integer id,
                                                @RequestBody PLMItemAttribute attribute) {
        return itemService.createItemAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PLMItemAttribute updateItemAttribute(@PathVariable("id") Integer id,
                                                @RequestBody PLMItemAttribute attribute) {
        return itemService.updateItemAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.GET)
    public List<PLMItemAttribute> getItemAttributes(@PathVariable("id") Integer id) {
        return itemService.getItemAttributes(id);
    }

    @RequestMapping(value = "/revisions/{itemRevisionId}/attributes", method = RequestMethod.POST)
    public PLMItemRevisionAttribute createItemRevisionAttribute(@PathVariable("itemRevisionId") Integer itemRevisionId,
                                                                @RequestBody PLMItemRevisionAttribute attribute) {
        return itemService.createItemRevisionAttribute(attribute);
    }

    @RequestMapping(value = "/revisions/{itemRevisionId}/attributes", method = RequestMethod.PUT)
    public PLMItemRevisionAttribute updateItemRevisionAttribute(@PathVariable("itemRevisionId") Integer itemRevisionId,
                                                                @RequestBody PLMItemRevisionAttribute attribute) {
        return itemService.updateItemRevisionAttribute(attribute);
    }

    @RequestMapping(value = "/revisions/{itemRevisionId}/attributes", method = RequestMethod.GET)
    public List<PLMItemRevisionAttribute> getItemRevisionAttributes(@PathVariable("itemRevisionId") Integer itemRevisionId) {
        return itemService.getItemRevisionAttributes(itemRevisionId);
    }

    @RequestMapping(value = "/revisions/unique", method = RequestMethod.GET)
    public List<String> getUniqueRevisions() {
        return itemService.getUniqueRevisions();
    }

    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public List<ObjectAttribute> saveItemAttributes(@PathVariable("id") Integer id,
                                                    @RequestBody List<PLMItemAttribute> attributes) {
        return itemService.saveItemAttributes(attributes);
    }

    @RequestMapping(value = "/{id}/imageAttribute", method = RequestMethod.POST)
    public ObjectAttribute saveImageAttribute(@PathVariable("id") Integer id,
                                              @RequestBody PLMItemAttribute attribute, MultipartHttpServletRequest request) {
        Map<String, MultipartFile> filesMap = request.getFileMap();
        List<MultipartFile> files = new ArrayList<>(filesMap.values());
        if (files.size() > 0) {
            MultipartFile file = files.get(0);
            try {
                attribute.setImageValue(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return itemService.createAttribute(attribute);
        }
        return null;
    }

    @RequestMapping(value = "/{itemId}/bom", method = RequestMethod.GET)
    public List<PLMBom> getItemBom(@PathVariable("itemId") Integer itemId, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        PLMItemRevision item = itemService.getRevision(itemId);
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return bomService.getBom(item, hierarchy);
    }

    @RequestMapping(value = "/{itemId}/bom/released", method = RequestMethod.GET)
    public List<PLMItem> getReleasedItemsByItem(@PathVariable("itemId") Integer itemId, @RequestParam("problemReport") Integer problemReport) {
        return itemService.getReleasedItemsByItem(itemId, problemReport);
    }

    @RequestMapping(value = "/released", method = RequestMethod.GET)
    public List<ItemDto> getReleasedItems(@RequestParam("hasBom") Boolean hasBom) {
        if (hasBom == null) {
            hasBom = true;
        }
        return itemService.getReleasedItems(hasBom);
    }

    @RequestMapping(value = "{itemId}/bom/search", method = RequestMethod.GET)
    public List<BomDto> getBomItemsByFreeTextSearch(@PathVariable("itemId") Integer itemId, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy,
                                                    BomItemSearchCriteria bomItemSearchCriteria) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return bomService.getBomItemsByFreeTextSearch(itemId, bomItemSearchCriteria, hierarchy);
    }

    @RequestMapping(value = "/{itemId}/whereused", method = RequestMethod.GET)
    public List<PLMBom> getItemWhereUsed(@PathVariable("itemId") Integer itemId, @RequestParam("hierarchy") Boolean hierarchy) {
        PLMItem item = itemService.get(itemId);
        return bomService.getWhereUsed(item, hierarchy);
    }

    @RequestMapping(value = "/{itemId}/whereused/items", method = RequestMethod.GET)
    public List<PLMBom> getItemWhereUsedItem(@PathVariable("itemId") Integer itemId) {
        return bomService.getWhereUsedItems(itemId);
    }

    @RequestMapping(value = "/{id}/changes", method = RequestMethod.GET)
    public List<PLMChange> getItemChanges(@PathVariable("id") Integer id) {
        List<PLMChange> changes = new ArrayList<>();
        changes.addAll(ecoService.findByAffectedItem(id));
        return changes;
    }

    @RequestMapping(value = "/{id}/ecrs", method = RequestMethod.GET)
    public List<PLMECR> getItemECRs(@PathVariable("id") Integer id) {
        return ecrService.findByAffectedItem(id);
    }

    @RequestMapping(value = "/{id}/dcos", method = RequestMethod.GET)
    public List<PLMChange> getItemDCOs(@PathVariable("id") Integer id) {
        List<PLMChange> changes = new ArrayList<>();
        changes.addAll(dcoService.findByAffectedItem(id));
        return changes;
    }

    @RequestMapping(value = "/{id}/dcrs", method = RequestMethod.GET)
    public List<PLMDCR> getItemDCRs(@PathVariable("id") Integer id) {
        return dcrService.findByAffectedItem(id);
    }

    @RequestMapping(value = "/{id}/prs", method = RequestMethod.GET)
    public List<PQMProblemReport> getItemPRs(@PathVariable("id") Integer id) {
        return problemReportService.findByProblemItem(id);
    }

    @RequestMapping(value = "/{id}/product/prs", method = RequestMethod.GET)
    public List<PQMProblemReport> getProductItemPRs(@PathVariable("id") Integer id) {
        return problemReportService.getProductItemPRs(id);
    }

    @RequestMapping(value = "/{id}/qcrs", method = RequestMethod.GET)
    public List<PQMQCR> getItemQCRs(@PathVariable("id") Integer id) {
        return qcrService.findByProblemItem(id);
    }

    /*
        Begin item references
     */
    @RequestMapping(value = "/{itemId}/references", method = RequestMethod.GET)
    public List<PLMItemReference> getItemReferences(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemReferences(itemId);
    }

    @RequestMapping(value = "/{itemId}/references/{refId}", method = RequestMethod.GET)
    public PLMItemReference getItemReferenceById(@PathVariable("itemId") Integer itemId,
                                                 @PathVariable("refId") Integer refId) {
        return itemService.getItemReference(refId);
    }

    @RequestMapping(value = "/{itemId}/references", method = RequestMethod.POST)
    public PLMItemReference createItemReference(@PathVariable("itemId") Integer itemId,
                                                @RequestBody PLMItemReference reference) {
        return itemService.createItemReference(reference);
    }

    @RequestMapping(value = "/{itemId}/references/{refId}", method = RequestMethod.PUT)
    public PLMItemReference updateItemReference(@PathVariable("itemId") Integer itemId,
                                                @PathVariable("refId") Integer refId,
                                                @RequestBody PLMItemReference reference) {
        return itemService.updateItemReference(reference);

    }

    @RequestMapping(value = "/{itemId}/references/{refId}", method = RequestMethod.DELETE)
    public void deleteItemReference(@PathVariable("itemId") Integer itemId,
                                    @PathVariable("refId") Integer refId) {
        itemService.deleteItemReference(refId);

    }

    @RequestMapping(value = "/{itemId}/mpns", method = RequestMethod.POST)
    public PLMItemManufacturerPart create(@PathVariable("itemId") Integer itemId,
                                          @RequestBody PLMItemManufacturerPart itemManufacturerPart) {
        return itemService.createManufacturerPart(itemId, itemManufacturerPart);
    }

    @RequestMapping(value = "/{itemId}/mpns/{mpnId}", method = RequestMethod.PUT)
    public PLMItemManufacturerPart update(@PathVariable("itemId") Integer itemId,
                                          @PathVariable("mpnId") Integer mpnId,
                                          @RequestBody PLMItemManufacturerPart itemManufacturerPart) {
        return itemService.updateManufacturerPart(itemManufacturerPart);
    }

    @RequestMapping(value = "/{itemId}/mpns/{mpnId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("itemId") Integer itemId,
                       @PathVariable("mpnId") Integer mpnId) {
        itemService.deleteManufacturerPart(itemId);
    }

    @RequestMapping(value = "/{itemId}/mpns/{mpnId}", method = RequestMethod.GET)
    public PLMItemManufacturerPart get(@PathVariable("itemId") Integer itemId,
                                       @PathVariable("mpnId") Integer mpnId) {
        return itemService.getManufacturerPartById(mpnId);
    }

    @RequestMapping(value = "/{itemId}/mpns", method = RequestMethod.GET)
    public List<PLMItemManufacturerPart> getAllManufacturerParts(@PathVariable("itemId") Integer itemId) {
        return itemService.getManufacturerParts(itemId);
    }

    @RequestMapping(value = "/{itemId}/byMfrPart/mpns", method = RequestMethod.GET)
    public List<PLMItemManufacturerPart> getPartsByMfrPart(@PathVariable("itemId") Integer itemId) {
        return itemService.getPartsByMfrPart(itemId);
    }

    @RequestMapping(value = "/search/parts", method = RequestMethod.GET)
    public Page<PLMManufacturerPart> searchManufacturerPart(ManufacturerPartCriteria criteria,
                                                            PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerPartService.find(criteria, pageable);
    }

    @RequestMapping(value = "/{itemId}/revise", method = RequestMethod.POST)
    public PLMItemRevision reviseItem(@PathVariable("itemId") Integer itemId) {
        return itemService.reviseItem(itemId);
    }


   /* Saved Search Methods*/

    @RequestMapping(value = "/savedsearches", method = RequestMethod.POST)
    public PLMSavedSearch saveSearches(PLMSavedSearch PLMSavedSearch) {
        return itemService.saveSearches(PLMSavedSearch);
    }

    @RequestMapping(value = "/savedsearches", method = RequestMethod.GET)
    public List<PLMSavedSearch> getAllSavedSearches() {
        return itemService.getAllSavedSearches();
    }

    @RequestMapping(value = "/savedsearches/{searchId}", method = RequestMethod.GET)
    public PLMSavedSearch getBySavedSearchId(@PathVariable Integer searchId) {
        return itemService.getSavedSearchBySearchId(searchId);
    }

    @RequestMapping(value = "/savedsearches/{searchId}", method = RequestMethod.PUT)
    public PLMSavedSearch updateSavedSearches(@PathVariable Integer searchId, @RequestBody PLMSavedSearch PLMSavedSearch) {
        return itemService.updateSavedSearches(searchId, PLMSavedSearch);
    }

    @RequestMapping(value = "/savedsearches/{searchId}", method = RequestMethod.DELETE)
    public void deleteSavedSearch(@PathVariable Integer searchId) {
        itemService.deleteSavedSearch(searchId);
    }

    @RequestMapping(value = "/export/{file}", method = RequestMethod.GET)
    public void exportItems(@PathVariable String file) {
        itemService.exportItems(file);
    }

    @RequestMapping(value = "/{id}/uploadImage", method = RequestMethod.POST)
    public PLMItem uploadImage(@PathVariable("id") Integer id, MultipartHttpServletRequest request) {
        PLMItem item1 = itemService.get(id);
        if (item1 != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    item1.setThumbnail(file.getBytes());
                    item1 = itemService.uploadThumbnailImage(item1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return item1;

    }

    @RequestMapping(value = "/{itemId}/itemImageAttribute/download", method = RequestMethod.GET)
    public void downloadImageItem(@PathVariable("itemId") Integer itemId,
                                  HttpServletResponse response) {
        itemService.getImageItem(itemId, response);
    }

    @RequestMapping(value = "/updateImageValue/{objectId}/{attributeId}", method = RequestMethod.POST)
    public PLMItem saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                           @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return itemService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/lockItem/{itemId}", method = RequestMethod.PUT)
    public PLMItem lockItem(@PathVariable("itemId") Integer itemId, @RequestBody ItemsDto item) {
        return itemService.lockItem(itemId, item);
    }

    @RequestMapping(value = "/subscribe/{itemId}", method = RequestMethod.POST)
    public PLMSubscribe subscribeItem(@PathVariable("itemId") Integer itemId) {
        return itemService.subscribeItem(itemId);
    }

    @RequestMapping(value = "/subscribe/{itemId}/{personId}", method = RequestMethod.GET)
    public PLMSubscribe getSubscribeItemByPerson(@PathVariable("itemId") Integer itemId, @PathVariable("personId") Integer personId) {
        return itemService.getSubscribeItemByPerson(itemId, personId);
    }

    @RequestMapping(value = "/{id}/attribute", method = RequestMethod.POST)
    public ObjectAttribute saveItemAttributes(@PathVariable("id") Integer id,
                                              @RequestBody PLMItemAttribute attribute) {
        return itemService.saveItemAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/projectItems", method = RequestMethod.GET)
    public List<ProjectItemsDto> projectItems(@PathVariable("id") Integer id) {
        return itemService.getProjectItems(id);
    }

    @RequestMapping(value = "/{id}/itemRequirements", method = RequestMethod.GET)
    public List<RequirementDeliverable> itemRequirements(@PathVariable("id") Integer id) {
        return itemService.getItemRequirements(id);
    }

    @RequestMapping(value = "/byType", method = RequestMethod.GET)
    public List<PLMItem> getItemsByType(@RequestParam("name") String name) {
        return itemService.getItemsByType(name);
    }

    @RequestMapping(value = "/{id}/importBom", method = RequestMethod.POST)
    public PLMBom importBom(@PathVariable("id") Integer id,
                            MultipartHttpServletRequest request) throws Exception {
        return itemService.importBom(id, request.getFileMap());
    }

    @RequestMapping(value = "/import/file", method = RequestMethod.POST)
    public void importUploadedFile(MultipartHttpServletRequest request) throws Exception {
        itemImporter.importItemDataFromFile(request.getFileMap());
    }

    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    public ItemDetailsDto itemDetails(@PathVariable("id") Integer id) {
        return itemService.itemDetails(id);
    }

    @RequestMapping(value = "/{itemId}/exportBom", method = RequestMethod.GET)
    public List<BomDto> itemBomExport(@PathVariable("itemId") Integer itemId, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy,
                                      @RequestParam(value = "bomRule", required = false) String bomRule) {
        return bomService.getTotalBom(itemId, hierarchy, bomRule);
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    public Comment createComment(@RequestBody Comment comment) {
        comment.setId(null);
        return itemService.createComment(comment);
    }

    @RequestMapping(value = "/systemInfo", method = RequestMethod.GET)
    public SystemInfoDto getSystemInfo() {
        return itemService.getSystemInfo();
    }

    @RequestMapping(value = "/setFileAsItemImage/{id}/{fileId}", method = RequestMethod.PUT)
    public void setImageAsDefaultForItem(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId) {
        itemService.setImageAsDefaultForItem(id, fileId);
    }

    @RequestMapping(value = "/update/bom/sequence", method = RequestMethod.GET)
    public PLMItem updateBomSequence() {
        return bomService.updateBomSequence();
    }

    @RequestMapping(value = "/update/bom/{itemId}/sequence", method = RequestMethod.GET)
    public PLMItemRevision updateBomItemSequence(@PathVariable("itemId") Integer itemId) {
        return bomService.updateBomItemSequence(itemId);
    }

    @RequestMapping(value = "/update/email/template", method = RequestMethod.PUT)
    public void updateEmailTemplate(@RequestBody EmailTemplateConfiguration emailTemplateConfiguration) {
        itemService.updateEmailTemplate(emailTemplateConfiguration);
    }

    @RequestMapping(value = "/email/template", method = RequestMethod.POST)
    public EmailTemplateConfiguration getEmailTemplate(@RequestBody EmailTemplateConfiguration emailTemplateConfiguration) throws IOException {
        return itemService.findTemplate(emailTemplateConfiguration);
    }

    @RequestMapping(value = "/hasBom/items/{id}", method = RequestMethod.GET)
    public List<PLMItem> getAllHasBomItems(@PathVariable("id") Integer id) {
        return itemService.getAllHasBomItems(id);
    }

    @RequestMapping(value = "/hasBom/items/latestFalse/{id}", method = RequestMethod.GET)
    public List<PLMItem> getAllHasBomItem(@PathVariable("id") Integer id) {
        return itemService.getAllHasBomItemsLatestFalse(id);
    }


    @RequestMapping(value = "/item/to/items/{id}", method = RequestMethod.GET)
    public List<PLMItem> getAllItemsToCompare(@PathVariable("id") Integer id) {
        return itemService.getAllItemsToCompare(id);
    }

    @RequestMapping(value = "/item/compareitems", method = RequestMethod.GET)
    public Page<PLMItem> getItemsToCompare(PageRequest pageRequest, ItemCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getItemsToCompare(pageable, criteria);
    }

    @RequestMapping(value = "/item/to/items/latestFalse/{id}", method = RequestMethod.GET)
    public List<PLMItem> getAllLatestFalseItemsToCompare(@PathVariable("id") Integer id) {
        return itemService.getAllItemsToCompareLatestFalse(id);
    }

    @RequestMapping(value = "/itemRevisions/bom/compare/{id}", method = RequestMethod.GET)
    public List<PLMItemRevision> getAllItemRevisions(@PathVariable("id") Integer id) {
        return itemService.getAllItemRevisionsByMaster(id);
    }

    @RequestMapping(value = "/bom/item/comparision/{id}/{itemId}/{latest}", method = RequestMethod.GET)
    public BomItemComparisionDTO getComparedBomItemsByItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId, @PathVariable("latest") Boolean latest) {
        PLMItemRevision fromItem = itemService.getRevision(id);
        PLMItemRevision toItem = itemService.getRevision(itemId);
        return bomService.getCompareBomItemsByIndividualItems(fromItem, toItem, latest);
    }

    @RequestMapping(value = "/bom/revision/comparision/{id}/{itemId}/{latest}", method = RequestMethod.GET)
    public BomItemComparisionDTO getComparedBomItemsByRevision(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId, @PathVariable("latest") Boolean latest) {
        PLMItemRevision fromItem = itemService.getRevision(id);
        PLMItemRevision toItem = itemService.getRevision(itemId);
        return bomService.getCompareBomItemsByIndividualRevisions(fromItem, toItem, latest);
    }

    /**
     * Item To Item Comparision
     */

    @RequestMapping(value = "/modal/bom/items/{id}", method = RequestMethod.GET)
    public List<PLMItemType> getModalBomItems(@PathVariable("id") Integer id) {
        return itemTypeService.getAllSubTypesForModalBom(id);
    }

    @RequestMapping(value = "/modal/bom/attributes/{id}", method = RequestMethod.GET)
    public List<PLMItemTypeAttribute> getModalBomItemAttributes(@PathVariable("id") Integer id) {
        return itemTypeService.getTypeAttributesFroModalBom(id);
    }

    @RequestMapping(value = "/instances/{id}", method = RequestMethod.GET)
    public List<PLMItem> getItemInstances(@PathVariable("id") Integer id) {
        return itemService.getItemInstances(id);
    }

    @RequestMapping(value = "/{id}/itemInstances", method = RequestMethod.GET)
    public List<PLMItem> getInstances(@PathVariable("id") Integer id) {
        return itemService.getInstances(id);
    }

    @RequestMapping(value = "/revisions/{id}/promote", method = RequestMethod.PUT)
    public PLMItemRevision promoteItem(@PathVariable("id") Integer id, @RequestBody PLMItemRevision itemRevision) {
        return itemService.promoteItem(id, itemRevision);
    }

    @RequestMapping(value = "/revisions/{id}/demote", method = RequestMethod.PUT)
    public PLMItemRevision demoteItem(@PathVariable("id") Integer id, @RequestBody PLMItemRevision itemRevision) {
        return itemService.demoteItem(id, itemRevision);
    }

    @RequestMapping(value = "/bom/inclusion", method = RequestMethod.PUT)
    public PLMItemRevision updateBomInclusionRules(
            @RequestBody PLMItemRevision itemRevision) {
        return itemService.updateRevisionBomInclusionRules(itemRevision);
    }

    @RequestMapping(value = "/workflow/{typeId}/itemType/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getWorkflows(@PathVariable("typeId") Integer typeId,
                                                    @PathVariable("type") String type) {
        return itemService.getHierarchyWorkflows(typeId, type);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return itemService.attachItemWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{itemId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("itemId") Integer itemId) {
        workflowService.deleteWorkflow(itemId);
    }

    @RequestMapping(value = "/{itemId}/update/configurableAttributes", method = RequestMethod.GET)
    public List<ItemConfigurableAttributes> updateConfigurableAttributes(@PathVariable("itemId") Integer itemId) {
        return itemService.updateConfigurableAttributes(itemId);
    }

    @RequestMapping(value = "/{itemId}/configurableAttributes", method = RequestMethod.GET)
    public Map<String, ItemConfigurableAttributes> getConfigurableAttributes(@PathVariable("itemId") Integer itemId) {
        return itemService.getConfigurableAttributes(itemId);
    }

    @RequestMapping(value = "/{itemId}/itemConfigurableAttributes", method = RequestMethod.GET)
    public List<ItemConfigurableAttributes> getItemConfigurableAttributes(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemConfigurableAttributes(itemId);
    }

    @RequestMapping(value = "/{itemId}/itemConfigurableAttributes", method = RequestMethod.PUT)
    public ItemConfigurableAttributes updateItemConfigurableAttributes(@PathVariable("itemId") Integer itemId, @RequestBody ItemConfigurableAttributes itemConfigurableAttribute) {
        return itemService.updateItemConfigurableAttributes(itemConfigurableAttribute);
    }

    @RequestMapping(value = "/{itemId}/mfrPart", method = RequestMethod.POST)
    public PLMItemManufacturerPart createItemPart(@PathVariable("itemId") Integer itemId,
                                                  @RequestBody PLMItemManufacturerPart itemManufacturerPart) {
        return itemService.createItemPart(itemId, itemManufacturerPart);
    }

    @RequestMapping(value = "/{itemId}/mfrPart/multiple", method = RequestMethod.POST)
    public List<PLMItemManufacturerPart> createItemParts(@PathVariable("itemId") Integer itemId,
                                                         @RequestBody List<PLMItemManufacturerPart> itemManufacturerParts) {
        return itemService.createItemParts(itemId, itemManufacturerParts);
    }

    @RequestMapping(value = "/{itemId}/mfrPart/{partId}", method = RequestMethod.PUT)
    public PLMItemManufacturerPart updateItemPart(@PathVariable("itemId") Integer itemId, @PathVariable("partId") Integer partId,
                                                  @RequestBody PLMItemManufacturerPart itemManufacturerPart) {
        return itemService.updateItemPart(itemId, itemManufacturerPart);
    }

    @RequestMapping(value = "/{itemId}/mfParts", method = RequestMethod.GET)
    public List<PLMItemManufacturerPart> getItemMfrParts(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemMfrParts(itemId);
    }

    @RequestMapping(value = "/update/configurableAttributes", method = RequestMethod.GET)
    public List<PLMItemType> updateAllConfigurableAttributes() {
        return itemService.updateAllConfigurableAttributes();
    }

    @RequestMapping(value = "/itemClass/{itemClass}", method = RequestMethod.GET)
    public List<PLMItem> getItemsByItemClass(@PathVariable("itemClass") ItemClass itemClass) {
        return itemService.getItemsByItemClass(itemClass);
    }

    @RequestMapping(value = "/itemClass/{itemClass}/normal", method = RequestMethod.GET)
    public List<PLMItem> getNormalItemsByItemClass(@PathVariable("itemClass") ItemClass itemClass) {
        return itemService.getNormalItemsByItemClass(itemClass);
    }

    @RequestMapping(value = "/itemClass/{itemClass}/normalconfigurable", method = RequestMethod.GET)
    public List<PLMItem> getNormalAndConfigurab1eItemsByItemClass(@PathVariable("itemClass") ItemClass itemClass) {
        return itemService.getNormalAndConfigurab1eItemsByItemClass(itemClass);
    }

    @RequestMapping(value = "/itemClass/{itemClass}/released", method = RequestMethod.GET)
    public List<PLMItem> getReleasedItemsByItemClass(@PathVariable("itemClass") ItemClass itemClass) {
        return itemService.getReleasedItemsByItemClass(itemClass);
    }

    @RequestMapping(value = "/{itemid}/specifications/multiple", method = RequestMethod.POST)
    public List<PGCItemSpecification> createItemSpecifications(@PathVariable("itemid") Integer decId,
                                                               @RequestBody List<PGCItemSpecification> itemSpecifications) {
        return itemService.createItemSpecifications(decId, itemSpecifications);
    }

    @RequestMapping(value = "/{itemid}/specifications", method = RequestMethod.GET)
    public List<PGCItemSpecification> getItemSpecifications(@PathVariable("itemid") Integer decId) {
        return itemService.getItemSpecifications(decId);
    }

    @RequestMapping(value = "/{itemid}/specifications/{specid}", method = RequestMethod.DELETE)
    public void deleteItemSpecification(@PathVariable("itemid") Integer itemId, @PathVariable("specid") Integer specId) {
        itemService.deleteItemSpecification(itemId, specId);
    }

    @RequestMapping(value = "/bom/{bomId}/substituteparts/multiple", method = RequestMethod.POST)
    public List<PLMSubstitutePart> createSubstituteParts(@PathVariable("bomId") Integer bomId, @RequestBody List<PLMSubstitutePart> substituteParts) {
        return itemService.createSubstituteParts(bomId, substituteParts);
    }

    @RequestMapping(value = "/{itemId}/substituteparts", method = RequestMethod.GET)
    public List<SubstitutePartDto> getSubstituteParts(@PathVariable("itemId") Integer itemId) {
        return itemService.getSubstituteParts(itemId);
    }

    @RequestMapping(value = "/{itemId}/substituteparts/{substitutePartId}", method = RequestMethod.DELETE)
    public void deleteSubstitutePart(@PathVariable("itemId") Integer itemId, @PathVariable("substitutePartId") Integer substitutePartId) {
        itemService.deleteSubstitutePart(itemId, substitutePartId);
    }

    @RequestMapping(value = "/{itemId}/alternateparts/multiple", method = RequestMethod.POST)
    public List<PLMAlternatePart> createAlternateParts(@PathVariable("itemId") Integer itemId, @RequestBody List<PLMAlternatePart> alternateParts) {
        return itemService.createAlternateParts(itemId, alternateParts);
    }

    @RequestMapping(value = "/{itemId}/alternateparts", method = RequestMethod.POST)
    public PLMAlternatePart createAlternatePart(@PathVariable("itemId") Integer itemId, @RequestBody PLMAlternatePart alternatePart) {
        return itemService.createAlternatePart(itemId, alternatePart);
    }

    @RequestMapping(value = "/{itemId}/alternateparts/{partId}", method = RequestMethod.PUT)
    public PLMAlternatePart updateAlternatePart(@PathVariable("itemId") Integer itemId, @PathVariable("partId") Integer partId,
                                                @RequestBody PLMAlternatePart alternatePart) {
        return itemService.updateAlternatePart(itemId, alternatePart);
    }

    @RequestMapping(value = "/{itemId}/alternateparts", method = RequestMethod.GET)
    public List<AlternatePartDto> getAlternateParts(@PathVariable("itemId") Integer itemId) {
        return itemService.getAlternateParts(itemId);
    }

    @RequestMapping(value = "/{itemId}/alternateparts/{alternatePartId}", method = RequestMethod.DELETE)
    public void deleteAlternatePart(@PathVariable("itemId") Integer itemId, @PathVariable("alternatePartId") Integer alternatePartId) {
        itemService.deleteAlternatePart(itemId, alternatePartId);
    }

    @RequestMapping(value = "/itemtype", method = RequestMethod.GET)
    public Page<PLMItem> getItemsByItemType(PageRequest pageRequest, ItemCriteria itemCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getItemsByItemType(pageable, itemCriteria);
    }

}