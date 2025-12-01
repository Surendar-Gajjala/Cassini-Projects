package com.cassinisys.plm.controller.mfr;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ItemManufacturerPartCriteria;
import com.cassinisys.plm.filtering.ManufacturerCriteria;
import com.cassinisys.plm.filtering.ManufacturerPartCriteria;
import com.cassinisys.plm.model.cm.PLMMCO;
import com.cassinisys.plm.model.cm.PLMVariance;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.dto.ItemMfrPartsDto;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.PLMSubscribe;
import com.cassinisys.plm.model.pqm.PQMNCR;
import com.cassinisys.plm.model.pqm.PQMQCR;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.classification.ManufacturerPartTypeService;
import com.cassinisys.plm.service.classification.ManufacturerTypeService;
import com.cassinisys.plm.service.cm.MCOService;
import com.cassinisys.plm.service.cm.PLMVarianceService;
import com.cassinisys.plm.service.mfr.ManufacturerFileService;
import com.cassinisys.plm.service.mfr.ManufacturerPartService;
import com.cassinisys.plm.service.mfr.ManufacturerService;
import com.cassinisys.plm.service.pqm.NCRService;
import com.cassinisys.plm.service.pqm.QCRService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home on 4/27/2016.
 */
@RestController
@RequestMapping("/plm/mfr")
@Api(tags = "PLM.MFR", description = "Mfr Related")
public class ManufacturerController extends BaseController {

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ManufacturerPartService manufacturerPartService;

    @Autowired
    private ManufacturerTypeService manufacturerTypeService;

    @Autowired
    private ManufacturerPartTypeService manufacturerPartTypeService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ManufacturerFileService manufacturerFileService;

    @Autowired
    private PLMWorkflowService workflowService;

    @Autowired
    private MCOService mcoService;

    @Autowired
    private QCRService qcrService;

    @Autowired
    private NCRService ncrService;
    @Autowired
    private PLMVarianceService varianceService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMManufacturer createManufacturer(@RequestBody PLMManufacturer manufacturer) {
        return manufacturerService.create(manufacturer);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMManufacturer update(@PathVariable("id") Integer id,
                                  @RequestBody PLMManufacturer plmManufacturer) {
        plmManufacturer.setId(id);
        return manufacturerService.update(plmManufacturer);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteManufacturer(@PathVariable("id") Integer id) {
        manufacturerService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMManufacturer getManufacturer(@PathVariable("id") Integer id) {
        return manufacturerService.get(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<PLMManufacturer> getAllManufacturers() {
        return manufacturerService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMManufacturer> getManufacturers(@PathVariable Integer[] ids) {
        return manufacturerService.getMfrs(Arrays.asList(ids));
    }

    @RequestMapping(value = "/mfrTypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMManufacturerType> getMultipleMfrTypes(@PathVariable Integer[] ids) {
        return manufacturerService.getMultipleMfrTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/mfrPartTypes/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMManufacturerPartType> getMultipleMfrPartTypes(@PathVariable Integer[] ids) {
        return manufacturerService.getMultipleMfrPartTypes(Arrays.asList(ids));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PLMManufacturer> getAllManufactures(PageRequest pageRequest,ManufacturerCriteria manufacturerCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerService.freeTextSearch(pageable, manufacturerCriteria);
    }

    @RequestMapping(value = "/allParts", method = RequestMethod.GET)
    public Page<PLMManufacturerPart> getAllManufacturesPart(PageRequest pageRequest, ItemManufacturerPartCriteria itemManufacturerPartCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerPartService.findByItem(pageable, itemManufacturerPartCriteria);
    }

    @RequestMapping(value = "/{id}/parts", method = RequestMethod.POST)
    public PLMManufacturerPart createPart(@PathVariable("id") Integer id,
                                          @RequestBody PLMManufacturerPart manufacturerPart) {
        return manufacturerPartService.create(manufacturerPart);
    }

    @RequestMapping(value = "/{id}/parts/{partId}", method = RequestMethod.PUT)
    public PLMManufacturerPart updatePart(@PathVariable("id") Integer id,
                                          @PathVariable("partId") Integer partId,
                                          @RequestBody PLMManufacturerPart manufacturerPart) {
        manufacturerPart.setId(partId);
        return manufacturerPartService.update(manufacturerPart);
    }

    @RequestMapping(value = "/{id}/parts/{partId}", method = RequestMethod.DELETE)
    public void deletePart(@PathVariable("id") Integer id,
                           @PathVariable("partId") Integer partId) {
        manufacturerPartService.delete(partId);
    }

    @RequestMapping(value = "/{id}/parts/{partId}/usedCount", method = RequestMethod.GET)
    public Integer getMfrPartUsedCount(@PathVariable("id") Integer id,@PathVariable("partId") Integer partId) {
        return manufacturerPartService.getMfrPartUsedCount(id,partId);
    }

    @RequestMapping(value = "/parts/{partId}", method = RequestMethod.GET)
    public PLMManufacturerPart getPart(@PathVariable("partId") Integer partId) {
        return manufacturerPartService.get(partId);
    }

    @RequestMapping(value = "/{id}/parts", method = RequestMethod.GET)
    public List<PLMManufacturerPart> getAllParts(@PathVariable("id") Integer id) {
        return manufacturerPartService.findAllPartsByMfr(id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<PLMManufacturer> searchManufacturer(ManufacturerCriteria criteria,
                                                    PageRequest pageRequest) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerService.find(criteria, pageable);
    }

    @RequestMapping(value = "/search/parts", method = RequestMethod.GET)
    public Page<PLMManufacturerPart> searchManufacturerPart(ManufacturerPartCriteria criteria,
                                                            PageRequest pageRequest) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerPartService.find(criteria, pageable);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<PLMManufacturer> freeTextSearch(PageRequest pageRequest, ManufacturerCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMManufacturer> pdmMfrs = manufacturerService.freeTextSearch(pageable, criteria);
        return pdmMfrs;
    }

    @RequestMapping(value = "/freesearch/parts", method = RequestMethod.GET)
    public Page<PLMManufacturerPart> freeTextSearch(PageRequest pageRequest, ManufacturerPartCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMManufacturerPart> plmMfrs = manufacturerPartService.freeTextSearch(pageable, criteria);
        return plmMfrs;
    }

    @RequestMapping(value = "/selectionSearch/parts", method = RequestMethod.GET)
    public Page<PLMManufacturerPart> selectionFreeTextSearch(PageRequest pageRequest, ManufacturerPartCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMManufacturerPart> plmMfrs = manufacturerPartService.freeTextSearch(pageable, criteria);
        return plmMfrs;
    }

    @RequestMapping(value = "/parts/type/{typeId}", method = RequestMethod.GET)
    public List<PLMManufacturerPart> getAllMfrPartsByType(@PathVariable("typeId") Integer typeId) {
        return manufacturerPartService.getPartsByType(typeId);
    }

    @RequestMapping(value = "/parts/[{partIds}]", method = RequestMethod.GET)
    public List<PLMManufacturerPart> getMultipleMfrParts(@PathVariable Integer[] partIds) {
        return manufacturerPartService.findMultipleMfrParts(Arrays.asList(partIds));
    }

    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public void saveMfrAttributes(@PathVariable("id") Integer id,
                                  @RequestBody List<PLMManufacturerAttribute> attributes) {
        manufacturerService.saveMfrAttributes(attributes);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PLMManufacturerAttribute createMfrAttributes(@PathVariable("id") Integer id,
                                                        @RequestBody PLMManufacturerAttribute attributes) {
        return manufacturerService.createMfrAttributes(attributes);
    }

    @RequestMapping(value = "/parts/{id}/attributes", method = RequestMethod.POST)
    public PLMManufacturerPartAttribute createMfrPartAttributes(@PathVariable("id") Integer id,
                                                                @RequestBody PLMManufacturerPartAttribute attributes) {
        return manufacturerPartService.createMfrPartAttributes(attributes);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.GET)
    public List<PLMManufacturerAttribute> getMfrAttributes(@PathVariable("id") Integer id) {
        return manufacturerService.getMfrAttributes(id);
    }

    @RequestMapping(value = "/parts/{id}/attributes", method = RequestMethod.GET)
    public List<PLMManufacturerPartAttribute> getMfrPartAttributes(@PathVariable("id") Integer id) {
        return manufacturerPartService.getMfrPartAttributes(id);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PLMManufacturerAttribute updateMfrAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody PLMManufacturerAttribute attribute) {
        return manufacturerService.updateMfrAttribute(attribute);
    }

    @RequestMapping(value = "/parts/{id}/attributes", method = RequestMethod.PUT)
    public PLMManufacturerPartAttribute updateMfrPartAttribute(@PathVariable("id") Integer id,
                                                               @RequestBody PLMManufacturerPartAttribute attribute) {
        return manufacturerPartService.updateMfrPartAttribute(attribute);
    }

    @RequestMapping(value = "/mfrTypes/{type}/attributes", method = RequestMethod.GET)
    public List<PLMManufacturerTypeAttribute> getMfrAttributes(@PathVariable("type") Integer type,
                                                               @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return manufacturerTypeService.getAttributes(type, hierarchy);
    }

    @RequestMapping(value = "/{id}/attributes/multiple/parts", method = RequestMethod.POST)
    public void saveMfrPartAttributes(@PathVariable("id") Integer id,
                                      @RequestBody List<PLMManufacturerPartAttribute> attributes) {
        manufacturerPartService.saveMfrPartAttributes(attributes);
    }

    @RequestMapping(value = "/mfrTypes/{type}/attributes/parts", method = RequestMethod.GET)
    public List<PLMManufacturerPartTypeAttribute> getEcoAttributes(@PathVariable("type") Integer type,
                                                                   @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return manufacturerPartTypeService.getAttributes(type, hierarchy);
    }

    @RequestMapping(value = "/mostused/parts", method = RequestMethod.GET)
    public Page<PLMManufacturerPart> getMostUsedMfrParts(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerPartService.getMostUsedMfrParts(pageable);
    }

    @RequestMapping(value = "/byName/{name}", method = RequestMethod.GET)
    public PLMManufacturer getByName(@PathVariable("name") String name) {
        return manufacturerService.getByName(name);
    }

    @RequestMapping(value = "/byPartNumber/{partNumber}", method = RequestMethod.GET)
    public PLMManufacturerPart getByPartNumber(@PathVariable("partNumber") String partNumber) {
        return manufacturerPartService.getByPartNumber(partNumber);
    }

    @RequestMapping(value = "/partByNumberAndType/{partNumber}/{type}", method = RequestMethod.GET)
    public PLMManufacturerPart partByNumberAndType(@PathVariable("partNumber") String partNumber, @PathVariable("type") Integer type) {
        return manufacturerPartService.partByNumberAndType(partNumber, type);
    }

    @RequestMapping(value = "/{mfrId}/partByMfrAndNumberAndType/{partNumber}/{type}", method = RequestMethod.GET)
    public PLMManufacturerPart getPartByMfrAndNumberAndType(@PathVariable("mfrId") Integer mfrId, @PathVariable("partNumber") String partNumber, @PathVariable("type") Integer type) {
        return manufacturerPartService.getPartByMfrAndNumberAndType(mfrId, partNumber, type);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<PLMManufacturer> getMfrsByType(@PathVariable("typeId") Integer id,
                                               PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerService.getMfrsByType(id, pageable);
    }

    @RequestMapping(value = "/partType/{typeId}", method = RequestMethod.GET)
    public Page<PLMManufacturerPart> getMfrPartsByType(@PathVariable("typeId") Integer id,
                                                       PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerPartService.getMfrPartsByType(id, pageable);
    }

    @RequestMapping(value = "/ByName/{name}", method = RequestMethod.GET)
    public PLMManufacturerType getByMfrName(@PathVariable("name") String name) {
        PLMManufacturerType manufacturerType = manufacturerService.getByMfrTypeName(name);
        return manufacturerType;
    }

    @RequestMapping(value = "/parts/byPartName/{name}", method = RequestMethod.GET)
    public PLMManufacturerPartType getByMfrPartName(@PathVariable("name") String name) {
        return manufacturerPartService.getByPartTypeName(name);
    }

    @RequestMapping(value = "/attributes/{attributeId}", method = RequestMethod.GET)
    public List<PLMManufacturerAttribute> getMfrUsedAttributes(@PathVariable("attributeId") Integer attributeId) {
        return manufacturerService.getMfrUsedAttributes(attributeId);
    }

    @RequestMapping(value = "/mfrParts/attributes/{attributeId}", method = RequestMethod.GET)
    public List<PLMManufacturerPartAttribute> getMfrPartUsedAttributes(@PathVariable("attributeId") Integer attributeId) {
        return manufacturerPartService.getMfrPartUsedAttributes(attributeId);
    }

    @RequestMapping(value = "{mfrId}/files/{fileId}/versionComments/{objectType}", method = RequestMethod.GET)
    private List<PLMManufacturerFile> getAllFileVersionComments(@PathVariable("mfrId") Integer mfrId, @PathVariable("fileId") Integer fileId,
                                                                @PathVariable("objectType") ObjectType objectType) {
        return manufacturerFileService.getAllFileVersionComments(mfrId, fileId, objectType);
    }

    @RequestMapping(value = "/updateImageValue/{objectId}/{attributeId}", method = RequestMethod.POST)
    public PLMManufacturerAttribute saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                                            @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return manufacturerService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/updateMfrPartImageValue/{objectId}/{attributeId}", method = RequestMethod.POST)
    public PLMManufacturerPartAttribute saveMfrPartImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                                                       @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return manufacturerService.saveMfrPartImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/{mfrId}/count", method = RequestMethod.GET)
    public DetailsCount getMfrDetails(@PathVariable("mfrId") Integer mfrId) {
        return manufacturerService.getMfrDetails(mfrId);
    }

    @RequestMapping(value = "/parts/{mfrId}/count", method = RequestMethod.GET)
    public DetailsCount getMfrPartDetails(@PathVariable("mfrId") Integer mfrId) {
        return manufacturerService.getMfrPartDetails(mfrId);
    }

    @RequestMapping(value = "/{id}/promote", method = RequestMethod.PUT)
    public PLMManufacturer promoteManufacturer(@PathVariable("id") Integer id, @RequestBody PLMManufacturer manufacturer) {
        return manufacturerService.promoteManufacturer(id, manufacturer);
    }

    @RequestMapping(value = "/{id}/demote", method = RequestMethod.PUT)
    public PLMManufacturer demoteManufacturer(@PathVariable("id") Integer id, @RequestBody PLMManufacturer manufacturer) {
        return manufacturerService.demoteManufacturer(id, manufacturer);
    }

    @RequestMapping(value = "/parts/{id}/promote", method = RequestMethod.PUT)
    public PLMManufacturerPart promoteManufacturerPart(@PathVariable("id") Integer id, @RequestBody PLMManufacturerPart part) {
        return manufacturerPartService.promoteManufacturerPart(id, part);
    }

    @RequestMapping(value = "/parts/{id}/demote", method = RequestMethod.PUT)
    public PLMManufacturerPart demoteManufacturerPart(@PathVariable("id") Integer id, @RequestBody PLMManufacturerPart part) {
        return manufacturerPartService.demoteManufacturerPart(id, part);
    }

    @RequestMapping(value = "/parts/{id}/mfParts/status/multiple", method = RequestMethod.POST)
    public List<PLMItemManufacturerPart> getMfrPartItems(@PathVariable("id") Integer id, @RequestBody List<PLMItemManufacturerPart> itemManufacturerParts) {
        return manufacturerPartService.getMfrPartItems(id, itemManufacturerParts);
    }

    @RequestMapping(value = "/parts/{id}/items", method = RequestMethod.GET)
    public List<ItemMfrPartsDto> getManufacturerPartItems(@PathVariable("id") Integer id) {
        return manufacturerPartService.getManufacturerPartItems(id);
    }

    @RequestMapping(value = "/parts/items/{id}", method = RequestMethod.POST)
    public List<ItemMfrPartsDto> saveItemMfrParts(@PathVariable("id") Integer id, @RequestBody List<ItemMfrPartsDto> itemMfrPartsDtos) {
        return manufacturerPartService.saveItemMfrParts(id, itemMfrPartsDtos);
    }

    /*  manufacturer workflow methods   */


    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return manufacturerService.attachNewMfrWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{mfrId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("mfrId") Integer mfrId) {
        workflowService.deleteWorkflow(mfrId);
    }

    @RequestMapping(value = "/workflow/{typeId}/mfrType/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getWorkflows(@PathVariable("typeId") Integer typeId,
                                                    @PathVariable("type") String type) {
        return manufacturerService.getHierarchyWorkflows(typeId, type);
    }


    /*  manufacturerParts workflow methods   */


    @RequestMapping(value = "/parts/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachPartsWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return manufacturerPartService.attachNewMfrPartWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/parts/{partId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteMfrPartWfWorkflow(@PathVariable("partId") Integer partId) {
        workflowService.deleteWorkflow(partId);
    }


    @RequestMapping(value = "/parts/workflow/{typeId}/mfrPartType/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getMfrPartWfWorkflows(@PathVariable("typeId") Integer typeId,
                                                             @PathVariable("type") String type) {
        return manufacturerPartService.getHierarchyWorkflows(typeId, type);
    }

    @RequestMapping(value = "/parts/{id}/partItems", method = RequestMethod.PUT)
    public List<PLMItemManufacturerPart> getItemMfrParts(@PathVariable("id") Integer id, @RequestBody PLMManufacturerPart manufacturerPart) {
        return manufacturerPartService.getItemMfrParts(id, manufacturerPart);
    }

    @RequestMapping(value = "/mfrType/{id}", method = RequestMethod.GET)
    public PLMManufacturerType getMfrType(@PathVariable("id") Integer id) {
        return manufacturerTypeService.get(id);
    }

    @RequestMapping(value = "/part/type/{id}", method = RequestMethod.GET)
    public PLMManufacturerPartType getPartType(@PathVariable("id") Integer id) {
        return manufacturerPartTypeService.get(id);
    }

    @RequestMapping(value = "/parts/{id}/changes", method = RequestMethod.GET)
    public List<PLMMCO> getMfrPartChanges(@PathVariable("id") Integer id) {
        return mcoService.findByAffectedItem(id);
    }

    @RequestMapping(value = "/parts/{id}/variances", method = RequestMethod.GET)
    public List<PLMVariance> getMfrPartVariances(@PathVariable("id") Integer id) {
        return varianceService.getVariances(id);
    }


    @RequestMapping(value = "/parts/{id}/qcrs", method = RequestMethod.GET)
    public List<PQMQCR> getMfrPartPRs(@PathVariable("id") Integer id) {
        return qcrService.findByProblemPart(id);
    }

    @RequestMapping(value = "/parts/{id}/ncrs", method = RequestMethod.GET)
    public List<PQMNCR> getMfrPartNCRs(@PathVariable("id") Integer id) {
        return ncrService.findByProblemPart(id);
    }

    @RequestMapping(value = "/parts/all", method = RequestMethod.GET)
    public Page<PLMManufacturerPart> getAllMfrParts(PageRequest pageRequest, ManufacturerPartCriteria partCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerPartService.getAllMfrParts(pageable, partCriteria);
    }

    @RequestMapping(value = "/parts/{partId}/image", method = RequestMethod.POST)
    public PLMManufacturerPart uploadPartImage(@PathVariable("partId") Integer partId, MultipartHttpServletRequest request) {
        return manufacturerPartService.uploadPartImage(partId, request);
    }

    @RequestMapping(value = "/parts/{partId}/image/download", method = RequestMethod.GET)
    public void downloadPartImage(@PathVariable("partId") Integer partId,
                                  HttpServletResponse response) {
        manufacturerPartService.downloadPartImage(partId, response);
    }

    @RequestMapping(value = "/mfrtype", method = RequestMethod.GET)
    public Page<PLMManufacturer> getMfrsByMfrType(PageRequest pageRequest, ManufacturerCriteria manufacturerCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerService.getMfrsByMfrType(pageable, manufacturerCriteria);
    }

    @RequestMapping(value = "/parts/type", method = RequestMethod.GET)
    public Page<PLMManufacturerPart> getMfrPartsByPartType(PageRequest pageRequest, ManufacturerPartCriteria partCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return manufacturerPartService.getMfrPartsByPartType(pageable, partCriteria);
    }

    @RequestMapping(value = "/type/tree", method = RequestMethod.GET)
    public List<PLMManufacturerType> getMfrTypeTree() {
        return manufacturerTypeService.getClassificationTree();
    }

    @RequestMapping(value = "/parts/type/tree", method = RequestMethod.GET)
    public List<PLMManufacturerPartType> getMfrPartTypeTree() {
        return manufacturerPartTypeService.getClassificationTree();
    }
    @RequestMapping(value = "/parts/{partId}/subscribe",method = RequestMethod.POST)
    public PLMSubscribe createSubscribeMfrPart(@PathVariable Integer partId){
        return manufacturerPartService.createSubscribeMfrPart(partId);
    }
}
