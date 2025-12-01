package com.cassinisys.plm.controller.pgc;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.service.classification.PGCObjectTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/pgc/objecttypes")
@Api(tags = "PLM.MES", description = "MES Related")
public class PGCObjectTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private PGCObjectTypeService pgcObjectTypeService;


    @RequestMapping(method = RequestMethod.POST)
    public PGCObjectType create(@RequestBody PGCObjectType pgcObjectType) {
        return pgcObjectTypeService.create(pgcObjectType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PGCObjectType update(@PathVariable("id") Integer id,
                                @RequestBody PGCObjectType pgcObjectType) {
        return pgcObjectTypeService.update(pgcObjectType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        pgcObjectTypeService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PGCObjectType get(@PathVariable("id") Integer id) {
        return pgcObjectTypeService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PGCObjectType> getAll() {
        return pgcObjectTypeService.getRootTypes();
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<PGCObjectType> getMesObjectTypeTree() {
        return pgcObjectTypeService.getMesObjectTypeTree();
    }

    @RequestMapping(value = "/{id}/{type}", method = RequestMethod.GET)
    public Object getPGCObjectTypeIdAndType(@PathVariable("id") Integer id, @PathVariable("type") PLMObjectType objectType) {
        return pgcObjectTypeService.getPGCObjectTypeIdAndType(id, objectType);
    }

    @RequestMapping(value = "/{id}/{type}/count", method = RequestMethod.GET)
    public Integer getObjectsByType(@PathVariable("id") Integer id, @PathVariable("type") PLMObjectType objectType) {
        return pgcObjectTypeService.getObjectsByType(id, objectType);
    }

    /*------------------------ substanceType ---------------------------*/
    @RequestMapping(value = "/substancetype", method = RequestMethod.POST)
    public PGCSubstanceType createSubstanceType(@RequestBody PGCSubstanceType substanceType) {
        return pgcObjectTypeService.createSubstanceType(substanceType);
    }

    @RequestMapping(value = "/substancetype/{id}", method = RequestMethod.PUT)
    public PGCSubstanceType updateSubstanceType(@PathVariable("id") Integer id, @RequestBody PGCSubstanceType substanceType) {
        return pgcObjectTypeService.updateSubstanceType(id, substanceType);
    }

    @RequestMapping(value = "/substancetype/{id}", method = RequestMethod.DELETE)
    public void deleteSubstanceType(@PathVariable("id") Integer id) {
        pgcObjectTypeService.deleteSubstanceType(id);
    }

    @RequestMapping(value = "/substancetype/{id}", method = RequestMethod.GET)
    public PGCSubstanceType getSubstanceType(@PathVariable("id") Integer id) {
        return pgcObjectTypeService.getSubstanceType(id);
    }

    @RequestMapping(value = "/substancetype", method = RequestMethod.GET)
    public List<PGCSubstanceType> getAllSubstanceTypes() {
        return pgcObjectTypeService.getAllSubstanceTypes();
    }

    @RequestMapping(value = "/substancetype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PGCSubstanceType> findMultipleSubstanceTypes(@PathVariable Integer[] ids) {
        return pgcObjectTypeService.findMultipleSubstanceTypes(Arrays.asList(ids));
    }


    @RequestMapping(value = "/substancetype/tree", method = RequestMethod.GET)
    public List<PGCSubstanceType> getSubstanceTypeTree() {
        return pgcObjectTypeService.getSubstanceTypeTree();
    }

    /*-------------------------- substanceGroupType -------------------------------- */

    @RequestMapping(value = "/substancegrouptype", method = RequestMethod.POST)
    public PGCSubstanceGroupType createSubstanceGroupType(@RequestBody PGCSubstanceGroupType substanceGroupType) {
        return pgcObjectTypeService.createSubstanceGroupType(substanceGroupType);
    }

    @RequestMapping(value = "/substancegrouptype/{id}", method = RequestMethod.PUT)
    public PGCSubstanceGroupType updateSubstanceGroupTypeType(@PathVariable("id") Integer id, @RequestBody PGCSubstanceGroupType substanceGroupType) {
        return pgcObjectTypeService.updateSubstanceGroupType(id, substanceGroupType);
    }

    @RequestMapping(value = "/substancegrouptype/{id}", method = RequestMethod.DELETE)
    public void deleteSubstanceGroupTypeType(@PathVariable("id") Integer id) {
        pgcObjectTypeService.deleteSubstanceGroupType(id);
    }

    @RequestMapping(value = "/substancegrouptype/{id}", method = RequestMethod.GET)
    public PGCSubstanceGroupType getSubstanceGroupTypeType(@PathVariable("id") Integer id) {
        return pgcObjectTypeService.getSubstanceGroupType(id);
    }

    @RequestMapping(value = "/substancegrouptype", method = RequestMethod.GET)
    public List<PGCSubstanceGroupType> getAllSubstanceGroupTypeTypes() {
        return pgcObjectTypeService.getAllSubstanceGroupTypes();
    }

    @RequestMapping(value = "/substancegrouptype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PGCSubstanceGroupType> getMultipleSubstanceGroupTypeTypes(@PathVariable Integer[] ids) {
        return pgcObjectTypeService.findMultipleSubstanceGroupTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/substancegrouptype/tree", method = RequestMethod.GET)
    public List<PGCSubstanceGroupType> getSubstanceGroupTypeTree() {
        return pgcObjectTypeService.getSubstanceGroupTypeTree();
    }

    /*--------------------- specificationType ------------------------ */

    @RequestMapping(value = "/specificationtype", method = RequestMethod.POST)
    public PGCSpecificationType createSpecificationType(@RequestBody PGCSpecificationType specificationType) {

        return pgcObjectTypeService.createSpecificationType(specificationType);
    }

    @RequestMapping(value = "/specificationtype/{id}", method = RequestMethod.PUT)
    public PGCSpecificationType updateSpecificationType(@PathVariable("id") Integer id, @RequestBody PGCSpecificationType specificationType) {
        return pgcObjectTypeService.updateSpecificationType(id, specificationType);
    }

    @RequestMapping(value = "/specificationtype/{id}", method = RequestMethod.DELETE)
    public void deleteSpecificationType(@PathVariable("id") Integer id) {
        pgcObjectTypeService.deleteSpecificationType(id);
    }

    @RequestMapping(value = "/specificationtype/{id}", method = RequestMethod.GET)
    public PGCSpecificationType getSpecificationType(@PathVariable("id") Integer id) {
        return pgcObjectTypeService.getSpecificationType(id);
    }

    @RequestMapping(value = "/specificationtype", method = RequestMethod.GET)
    public List<PGCSpecificationType> getAllSpecificationTypes() {
        return pgcObjectTypeService.getAllSpecificationTypes();
    }

    @RequestMapping(value = "/specificationtype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PGCSpecificationType> getMultipleSpecificationTypes(@PathVariable Integer[] ids) {
        return pgcObjectTypeService.findMultipleSpecificationTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/specificationtype/tree", method = RequestMethod.GET)
    public List<PGCSpecificationType> getSpecificationTypeTree() {
        return pgcObjectTypeService.getSpecificationTypeTree();
    }


        /*-------------------------- declarationType -------------------------------- */

    @RequestMapping(value = "/declarationtype", method = RequestMethod.POST)
    public PGCDeclarationType createDeclarationType(@RequestBody PGCDeclarationType declarationType) {
        return pgcObjectTypeService.createDeclarationType(declarationType);
    }

    @RequestMapping(value = "/declarationtype/{id}", method = RequestMethod.PUT)
    public PGCDeclarationType updateDeclarationType(@PathVariable("id") Integer id, @RequestBody PGCDeclarationType declarationType) {
        return pgcObjectTypeService.updateDeclarationType(id, declarationType);
    }

    @RequestMapping(value = "/declarationtype/{id}", method = RequestMethod.DELETE)
    public void deleteDeclarationType(@PathVariable("id") Integer id) {
        pgcObjectTypeService.deleteDeclarationType(id);
    }

    @RequestMapping(value = "/declarationtype/{id}", method = RequestMethod.GET)
    public PGCDeclarationType getDeclarationType(@PathVariable("id") Integer id) {
        return pgcObjectTypeService.getDeclarationType(id);
    }

    @RequestMapping(value = "/declarationtype", method = RequestMethod.GET)
    public List<PGCDeclarationType> getAllDeclarationTypes() {
        return pgcObjectTypeService.getAllDeclarationTypes();
    }

    @RequestMapping(value = "/declarationtype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PGCDeclarationType> getMultipleDeclarationTypes(@PathVariable Integer[] ids) {
        return pgcObjectTypeService.findMultipleDeclarationTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/declarationtype/tree", method = RequestMethod.GET)
    public List<PGCDeclarationType> getDeclarationTypeTree() {
        return pgcObjectTypeService.getDeclarationTypeTree();
    }


    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PGCObjectAttribute createPGCObjectAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody PGCObjectAttribute attribute) {
        return pgcObjectTypeService.createPGCObjectAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PGCObjectAttribute updatePGCObjectAttribute(@PathVariable("id") Integer id,
                                                       @RequestBody PGCObjectAttribute attribute) {
        return pgcObjectTypeService.updatePGCObjectAttribute(attribute);
    }


    @RequestMapping(value = "/type/{typeid}/attributes", method = RequestMethod.GET)
    public List<PGCObjectTypeAttribute> getHierarchicalTypeAttributes(@PathVariable("typeid") Integer typeId,
                                                                      @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return pgcObjectTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/all/tree", method = RequestMethod.GET)
    public PGCObjectTypesDto getAllObjectTypesTree() {
        return pgcObjectTypeService.getAllObjectTypesTree();
    }

    @RequestMapping(value = "/attributes/{attributeId}", method = RequestMethod.GET)
    public List<PGCObjectAttribute> getUsedPGCObjectTypeAttributes(@PathVariable("attributeId") Integer attributeId) {
        return pgcObjectTypeService.getUsedPGCObjectAttributes(attributeId);
    }

}
