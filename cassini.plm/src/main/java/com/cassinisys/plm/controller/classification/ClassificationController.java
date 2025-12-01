package com.cassinisys.plm.controller.classification;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.dto.ClassificationTypesDto;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.dto.QualityTypeAttributeDto;
import com.cassinisys.plm.service.classification.ChangeTypeService;
import com.cassinisys.plm.service.classification.ClassificationService;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by reddy on 6/13/17.
 */
@RestController
@RequestMapping("/plm/classification/{type}")
@Api(tags = "PLM.CLASSIFICATION", description = "Classification Related")
public class ClassificationController extends BaseController {
    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private ChangeTypeService changeTypeService;

    /* Types */

    @RequestMapping(method = RequestMethod.GET)
    public List<Object> getClassificationTree(@PathVariable("type") PLMObjectType type) throws InterruptedException {
        return classificationService.getClassificationTree(type);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Object create(@PathVariable("type") PLMObjectType type,
                         @RequestBody ObjectNode json) {
        return classificationService.createType(type, json);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable("type") PLMObjectType type,
                         @RequestBody ObjectNode json) {
        return classificationService.updateType(type, json);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("type") PLMObjectType type,
                       @PathVariable("id") Integer id) {
        classificationService.deleteType(type, id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable("type") PLMObjectType type,
                      @PathVariable("id") Integer id) {
        return classificationService.getType(type, id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Object> getAll(@PathVariable("type") PLMObjectType type) {
        return classificationService.getAllTypes(type);
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    public List<Object> getChildren(@PathVariable("type") PLMObjectType type,
                                    @PathVariable("id") Integer id) {
        return classificationService.getChildren(type, id);
    }


    /* Attributes */

    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.POST)
    public Object createAttribute(@PathVariable("type") PLMObjectType type,
                                  @RequestBody ObjectNode json) {
        return classificationService.createTypeAttribute(type, json);
    }

    @RequestMapping(value = "/{typeId}/attributes/{id}", method = RequestMethod.PUT)
    public Object updateAttribute(@PathVariable("type") PLMObjectType type,
                                  @RequestBody ObjectNode json) {
        return classificationService.updateTypeAttribute(type, json);
    }

    @RequestMapping(value = "/{typeId}/attributes/{id}", method = RequestMethod.DELETE)
    public void updateAttribute(@PathVariable("type") PLMObjectType type,
                                @PathVariable("id") Integer id) {
        classificationService.deleteTypeAttribute(type, id);
    }

    @RequestMapping(value = "/{typeId}/attributes/{id}", method = RequestMethod.GET)
    public Object getAttribute(@PathVariable("type") PLMObjectType type,
                               @PathVariable("id") Integer id) {
        return classificationService.getTypeAttribute(type, id);
    }

    @RequestMapping(value = "/{typeId}/attributes")
    public List<Object> getAttributes(@PathVariable("type") PLMObjectType type,
                                      @PathVariable("typeId") Integer typeId,
                                      @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return classificationService.getTypeAttributes(type, typeId, hierarchy);
    }

    @RequestMapping(value = "/{typeId}/{objectId}/attributes", method = RequestMethod.GET)
    public QualityTypeAttributeDto getObjectTypeAttributes(@PathVariable("type") PLMObjectType type, @PathVariable("typeId") Integer typeId,
                                                           @PathVariable("objectId") Integer objectId, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return classificationService.getObjectTypeAttributes(type, typeId, objectId, hierarchy);
    }

    @RequestMapping(value = "/{typeId}/{objectId}/attributes/values", method = RequestMethod.GET)
    public QualityTypeAttributeDto getObjectAttributeValues(@PathVariable("type") PLMObjectType type, @PathVariable("typeId") Integer typeId,
                                                            @PathVariable("objectId") Integer objectId) {
        return classificationService.getObjectAttributeValues(type, typeId, objectId);
    }


    @RequestMapping(value = "/{objectId}/{attributeId}/images/upload", method = RequestMethod.POST)
    public QualityTypeAttributeDto saveImageAttributeValue(@PathVariable("type") PLMObjectType type, @PathVariable("objectId") Integer objectId,
                                                           @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return classificationService.saveImageAttributeValue(type, objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/{objectId}/{attributeId}/attachments/upload", method = RequestMethod.POST)
    public QualityTypeAttributeDto saveAttachmentAttributeValue(@PathVariable("type") PLMObjectType type, @PathVariable("objectId") Integer objectId,
                                                                @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return classificationService.saveAttachmentAttributeValue(type, objectId, attributeId, request.getFileMap());
    }


    @RequestMapping(value = "/{typeId}/exclusion")
    public PLMItemTypeAttribute getExclusionAttributes(@PathVariable("type") PLMObjectType type,
                                                       @PathVariable("typeId") Integer typeId) {
        return itemTypeService.getExclusionAttributes(typeId);
    }

    @RequestMapping(value = "/{typeId}/configurable/attributes")
    public PLMItemTypeAttribute getConfigurableAttributes(@PathVariable("type") PLMObjectType type,
                                                          @PathVariable("typeId") Integer typeId) {
        return itemTypeService.getConfigurableAttributesBomRules(typeId);
    }

    @RequestMapping(value = "/mco/tree", method = RequestMethod.GET)
    public List<PLMMCOType> getMcoTypeTree() {
        return changeTypeService.getMcoTypeTree();
    }

    @RequestMapping(value = "/itemMco/tree", method = RequestMethod.GET)
    public List<PLMMCOType> getItemMcoTypeTree() {
        return changeTypeService.getItemMcoTypeTree();
    }

    @RequestMapping(value = "/manufacturerMco/tree", method = RequestMethod.GET)
    public List<PLMMCOType> getManufacturerMcoTypeTree() {
        return changeTypeService.getManufacturerMcoTypeTree();
    }

    @RequestMapping(value = "/eco/tree", method = RequestMethod.GET)
    public List<PLMECOType> getEcoTypeTree() {
        return changeTypeService.getEcoTypeTree();
    }

    @RequestMapping(value = "/ecr/tree", method = RequestMethod.GET)
    public List<PLMECRType> getMcrTypeTree() {
        return changeTypeService.getEcrTypeTree();
    }

    @RequestMapping(value = "/dcr/tree", method = RequestMethod.GET)
    public List<PLMDCRType> getDcrTypeTree() {
        return changeTypeService.getDcrTypeTree();
    }

    @RequestMapping(value = "/dco/tree", method = RequestMethod.GET)
    public List<PLMDCOType> getDcoTypeTree() {
        return changeTypeService.getDcoTypeTree();
    }

    @RequestMapping(value = "/tree/all", method = RequestMethod.GET)
    public ClassificationTypesDto getAllClassificationTree() {
        return classificationService.getAllClassificationTree();
    }

    @RequestMapping(value = "/{id}/assigned/{assignedtype}", method = RequestMethod.GET)
    public Object getClassificationType(@PathVariable("id") Integer id, @PathVariable("assignedtype") String type) {
        return classificationService.getClassificationType(id, type);
    }
}
