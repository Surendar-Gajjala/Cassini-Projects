package com.cassinisys.platform.api.custom;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.filtering.CustomObjectCriteria;
import com.cassinisys.platform.filtering.CustomObjectPredicateBuilder;
import com.cassinisys.platform.filtering.CustomParameterCriteria;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.*;
import com.cassinisys.platform.model.dto.CustomObjectDto;
import com.cassinisys.platform.service.custom.CustomObjectService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.mysema.query.types.Predicate;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.ws.rs.Produces;
import java.util.List;

@RestController
@RequestMapping("/customobjects")
@Api(tags = "PLATFORM.CUSTOM", description = "Custom endpoints")
public class CustomObjectController extends BaseController {

    @Autowired
    private CustomObjectService customObjectService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private CustomObjectPredicateBuilder predicateBuilder;

    @RequestMapping(method = RequestMethod.POST)
    public CustomObjectDto createCustomObject(@RequestBody CustomObjectDto customObjectDto) {
        return customObjectService.createCustomObject(customObjectDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CustomObject update(@RequestBody CustomObject customObject) {
        return customObjectService.updateObject(customObject);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomObject getCustomObject(@PathVariable("id") Integer id) {
        return customObjectService.getCustomObject(id);
    }

    @RequestMapping(value = "/{id}/notification", method = RequestMethod.POST)
    public void sendCustomObjectCreatedNotification(@PathVariable("id") Integer id, @RequestBody CustomObject customObject) {
        customObjectService.sendCustomObjectCreatedNotification(customObject);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) {
        customObjectService.deleteObject(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<CustomObject> getAllCustomObjects() {
        return customObjectService.getAllCustomObjects();
    }

    @RequestMapping(value = "/objects", method = RequestMethod.GET)
    public Page<CustomObject> getCustomObjects(PageRequest pageRequest, CustomObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customObjectService.getCustomObjects(pageable, objectCriteria);
    }

    @RequestMapping(value = "/supplier/{id}", method = RequestMethod.GET)
    public List<CustomObject> getCustomObjectBySupplier(@PathVariable("id") Integer id) {
        return customObjectService.getCustomObjectBySupplier(id);
    }

    @RequestMapping(value = "/type/{typeId}/supplier/{id}", method = RequestMethod.GET)
    public List<CustomObject> getCustomObjectByTypeAndSupplier(@PathVariable("typeId") Integer typeId, @PathVariable("id") Integer id) {
        return customObjectService.getCustomObjectByTypeAndSupplier(typeId, id);
    }

    @RequestMapping(value = "/{objectType}/{objectId}/{attributeId}/images/upload", method = RequestMethod.POST)
    public CustomObjectDto saveImageAttributeValue(@PathVariable("objectType") ObjectType objectType,
                                                   @PathVariable("objectId") Integer objectId,
                                                   @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return customObjectService.saveImageAttributeValue(objectType, objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/{objectType}/{objectId}/{attributeId}/attachments/upload", method = RequestMethod.POST)
    public CustomObjectDto saveAttachmentAttributeValue(@PathVariable("objectType") ObjectType objectType,
                                                        @PathVariable("objectId") Integer objectId,
                                                        @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return customObjectService.saveAttachmentAttributeValue(objectType, objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public CustomObjectAttribute updateCustomAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody CustomObjectAttribute attribute) {
        return customObjectService.updateCustomAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/bom/multiple", method = RequestMethod.POST)
    public List<CustomObjectBomDto> createMultipleCustomObjectBom(@PathVariable("id") Integer id, @RequestBody List<CustomObjectBomDto> customObjectBomDtos) {
        return customObjectService.createMultipleCustomObjectBom(id, customObjectBomDtos);
    }

    @RequestMapping(value = "/{id}/bom", method = RequestMethod.POST)
    public CustomObjectBomDto createCustomObjectBom(@PathVariable("id") Integer id, @RequestBody CustomObjectBomDto customObjectBomDto) {
        return customObjectService.createCustomObjectBom(id, customObjectBomDto);
    }

    @RequestMapping(value = "/{id}/bom/{bomId}", method = RequestMethod.PUT)
    public CustomObjectBomDto updateCustomObjectBom(@PathVariable("id") Integer id, @PathVariable("bomId") Integer bomId,
                                                    @RequestBody CustomObjectBomDto customObjectBomDto) {
        return customObjectService.updateCustomObjectBom(id, customObjectBomDto);
    }

    @RequestMapping(value = "/{id}/bom/{bomId}", method = RequestMethod.GET)
    public CustomObjectBomDto getCustomObjectBom(@PathVariable("id") Integer id, @PathVariable("bomId") Integer bomId) {
        return customObjectService.getCustomObjectBom(id, bomId);
    }

    @RequestMapping(value = "/{id}/bom/{bomId}", method = RequestMethod.DELETE)
    public void deleteCustomObjectBom(@PathVariable("id") Integer id, @PathVariable("bomId") Integer bomId) {
        customObjectService.deleteCustomObjectBom(id, bomId);
    }

    @RequestMapping(value = "/{id}/bom", method = RequestMethod.GET)
    public List<CustomObjectBomDto> getAllCustomObjectBom(@PathVariable("id") Integer id, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return customObjectService.getAllCustomObjectBom(id, hierarchy);
    }

    @RequestMapping(value = "/{id}/relatedobjects", method = RequestMethod.GET)
    public List<CustomObjectRelated> getRelatedCustomObjects(@PathVariable("id") Integer specId) {
        return customObjectService.getRelatedCustomObjects(specId);
    }

    @RequestMapping(value = "/{id}/related/multiple", method = RequestMethod.POST)
    public List<CustomObjectRelated> createMultipleRelatedCustomObject(@PathVariable("id") Integer id, @RequestBody List<CustomObjectRelated> customObjectRelatedList) {
        return customObjectService.createMultipleRelatedCustomObject(id, customObjectRelatedList);
    }

    @RequestMapping(value = "/{id}/related", method = RequestMethod.POST)
    public CustomObjectRelated createRelatedCustomObject(@PathVariable("id") Integer id, @RequestBody CustomObjectRelated customObjectRelated) {
        return customObjectService.createRelatedCustomObject(id, customObjectRelated);
    }


    @RequestMapping(value = "/{id}/related/{relatedId}", method = RequestMethod.PUT)
    public CustomObjectRelated updateRelatedCustomObject(@PathVariable("id") Integer id, @PathVariable("relatedId") Integer relatedId,
                                                         @RequestBody CustomObjectRelated customObjectRelated) {
        return customObjectService.updateRelatedCustomObject(id, customObjectRelated);
    }

    @RequestMapping(value = "/{id}/related/{relatedId}", method = RequestMethod.DELETE)
    public void deleteRelatedCustomObject(@PathVariable("id") Integer id, @PathVariable("relatedId") Integer relatedId) {
        customObjectService.deleteRelatedCustomObject(id, relatedId);
    }

    @RequestMapping(value = "/type/{id}", method = RequestMethod.GET)
    @Produces({"text/plain"})
    public Integer getCustomObjectByType(@PathVariable("id") Integer typeId) {
        return customObjectService.getCustomObjectByType(typeId);
    }

    @RequestMapping(value = "/simpleSearch", method = RequestMethod.GET)
    public Page<CustomObject> search(CustomObjectCriteria criteria, PageRequest pageRequest) {
        Predicate predicate = predicateBuilder.build(criteria,
                QCustomObject.customObject);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<CustomObject> customObjects = customObjectService.searchItems(predicate, pageable);
        return customObjects;
    }

    @RequestMapping(value = "/advancedsearch/{objectTypeId}", method = RequestMethod.POST)
    public Page<CustomObject> advancedSearch(@PathVariable Integer objectTypeId, @RequestBody CustomParameterCriteria[] customParameterCriterias, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<CustomObject> customObjects = customObjectService.advancedSearchItem(objectTypeId, customParameterCriterias, pageable);
        return customObjects;
    }

    @RequestMapping(value = "/{objectId}/whereused", method = RequestMethod.GET)
    public List<CustomObjectBom> getCustomObjectWhereUsed(@PathVariable("objectId") Integer objectId, @RequestParam("hierarchy") Boolean hierarchy) {
        return customObjectService.getCustomObjectWhereUsed(objectId, hierarchy);
    }

}
