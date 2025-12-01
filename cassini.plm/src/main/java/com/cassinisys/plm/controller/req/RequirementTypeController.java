package com.cassinisys.plm.controller.req;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PMObjectType;
import com.cassinisys.plm.model.pm.PMObjectTypeAttribute;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.service.pm.PMObjectTypeService;
import com.cassinisys.plm.service.req.RequirementTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/plm/requirementtypes")
@Api(tags = "PLM.PGC", description = "PGC Related")
public class RequirementTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private RequirementTypeService requirementTypeService;
    @Autowired
    private PMObjectTypeService pmObjectTypeService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMRequirementObjectType create(@RequestBody PLMRequirementObjectType itemType) {
        return requirementTypeService.create(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMRequirementObjectType update(@PathVariable("id") Integer id,
                                           @RequestBody PLMRequirementObjectType itemType) {
        return requirementTypeService.update(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        requirementTypeService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMRequirementObjectType get(@PathVariable("id") Integer id) {
        return requirementTypeService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMRequirementObjectType> getAll() {
        return requirementTypeService.getRootTypes();
    }

    @RequestMapping(value = "/all/tree", method = RequestMethod.GET)
    public PMObjectTypesDto getAllObjectTypesTree() {
        return pmObjectTypeService.getAllPMObjectTypesTree();
    }

    @RequestMapping(value = "/{id}/{type}", method = RequestMethod.GET)
    public Object getPMObjectTypeIdAndType(@PathVariable("id") Integer id, @PathVariable("type") PLMObjectType objectType) {
        return requirementTypeService.getPMObjectTypeIdAndType(id, objectType);
    }
    
    /*-------------------------- Requirement Type -------------------------------- */

    @RequestMapping(value = "/reqtype", method = RequestMethod.POST)
    public PLMRequirementType createRequirementType(@RequestBody PLMRequirementType machineType) {
        return requirementTypeService.createRequirementType(machineType);
    }

    @RequestMapping(value = "/reqtype/{id}", method = RequestMethod.PUT)
    public PLMRequirementType updateRequirementType(@PathVariable("id") Integer id, @RequestBody PLMRequirementType machineType) {
        return requirementTypeService.updateRequirementType(id, machineType);
    }

    @RequestMapping(value = "/reqtype/{id}", method = RequestMethod.DELETE)
    public void deleteRequirementType(@PathVariable("id") Integer id) {
        requirementTypeService.deleteRequirementType(id);
    }

    @RequestMapping(value = "/reqtype/{id}", method = RequestMethod.GET)
    public PLMRequirementType getRequirementType(@PathVariable("id") Integer id) {
        return requirementTypeService.getRequirementType(id);
    }

    @RequestMapping(value = "/reqtype", method = RequestMethod.GET)
    public List<PLMRequirementType> getAllRequirementTypes() {
        return requirementTypeService.getAllRequirementTypes();
    }

    @RequestMapping(value = "/reqtype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMRequirementType> getMultipleRequirementTypes(@PathVariable Integer[] ids) {
        return requirementTypeService.findMultipleRequirementTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/reqtype/tree", method = RequestMethod.GET)
    public List<PLMRequirementType> getRequirementTypeTree() {
        return requirementTypeService.getRequirementTypeTree();
    }
    
      /*---------------------- RequirementDocument Type --------------------------*/

    @RequestMapping(value = "/documenttype", method = RequestMethod.POST)
    public PLMRequirementDocumentType createReqDocumentType(@RequestBody PLMRequirementDocumentType materialType) {
        return requirementTypeService.createReqDocumentType(materialType);
    }

    @RequestMapping(value = "/documenttype/{id}", method = RequestMethod.PUT)
    public PLMRequirementDocumentType updateReqDocumentType(@PathVariable("id") Integer id, @RequestBody PLMRequirementDocumentType materialType) {
        return requirementTypeService.updateReqDocumentType(id, materialType);
    }

    @RequestMapping(value = "/documenttype/{id}", method = RequestMethod.DELETE)
    public void deleteReqDocumentType(@PathVariable("id") Integer id) {
        requirementTypeService.deleteReqDocumentType(id);
    }

    @RequestMapping(value = "/documenttype/{id}", method = RequestMethod.GET)
    public PLMRequirementDocumentType getReqDocumentType(@PathVariable("id") Integer id) {
        return requirementTypeService.getReqDocumentType(id);
    }

    @RequestMapping(value = "/documenttype", method = RequestMethod.GET)
    public List<PLMRequirementDocumentType> getAllReqDocumentTypes() {
        return requirementTypeService.getAllReqDocumentTypes();
    }

    @RequestMapping(value = "/documenttype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMRequirementDocumentType> getMultiplePrTypes(@PathVariable Integer[] ids) {
        return requirementTypeService.findMultipleReqDocumentTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/documenttype/tree", method = RequestMethod.GET)
    public List<PLMRequirementDocumentType> getReqDocumentTypeTree() {
        return requirementTypeService.getReqDocumentTypeTree();
    }

    @RequestMapping(value = "/type/{typeid}/attributes", method = RequestMethod.GET)
    public List<PLMRequirementObjectTypeAttribute> getHierarchicalTypeAttributes(@PathVariable("typeid") Integer typeId,
                                                                                 @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return requirementTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PLMRequirementObjectAttribute createReqAttribute(@PathVariable("id") Integer id,
                                                            @RequestBody PLMRequirementObjectAttribute attribute) {
        return requirementTypeService.createReqObjectAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PLMRequirementObjectAttribute updateReqObjectAttribute(@PathVariable("id") Integer id,
                                                                  @RequestBody PLMRequirementObjectAttribute attribute) {
        return requirementTypeService.updateReqObjectAttribute(attribute);
    }

        /*---------------------- PMObject Type --------------------------*/

    @RequestMapping(value = "/pmtype", method = RequestMethod.POST)
    public PMObjectType createProjectType(@RequestBody PMObjectType objectType) {
        return pmObjectTypeService.createProjectType(objectType);
    }

    @RequestMapping(value = "/pmtype/{id}", method = RequestMethod.PUT)
    public PMObjectType updateProjectType(@PathVariable("id") Integer id, @RequestBody PMObjectType pmObjectType) {
        return pmObjectTypeService.updateProjectType(id, pmObjectType);
    }

    @RequestMapping(value = "/pmtype/{id}", method = RequestMethod.DELETE)
    public void deleteProjectType(@PathVariable("id") Integer id) {
        pmObjectTypeService.deleteProjectType(id);
    }

    @RequestMapping(value = "/pmtype/{id}", method = RequestMethod.GET)
    public PMObjectType getProjectType(@PathVariable("id") Integer id) {
        return pmObjectTypeService.getProjectType(id);
    }

    @RequestMapping(value = "/pmtype", method = RequestMethod.GET)
    public List<PMObjectType> getAllProjectTypes() {
        return pmObjectTypeService.getAllProjectTypes();
    }

    @RequestMapping(value = "/pmtype/{id}/count", method = RequestMethod.GET)
    public Integer getObjectCountByType(@PathVariable("id") Integer id) {
        return pmObjectTypeService.getObjectCountByType(id);
    }

    @RequestMapping(value = "/pmtype/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PMObjectType> getMultipleProjectTypes(@PathVariable Integer[] ids) {
        return pmObjectTypeService.getMultipleProjectTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = "/pmtype/tree", method = RequestMethod.GET)
    public List<PMObjectType> getProjectTypeTree() {
        return pmObjectTypeService.getClassificationTree();
    }

    @RequestMapping(value = "/pmtype/type/{typeid}/attributes", method = RequestMethod.GET)
    public List<PMObjectTypeAttribute> getHierarchicalPMTypeAttributes(@PathVariable("typeid") Integer typeId,
                                                                       @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return pmObjectTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/pmtype/object/{objectid}/attributes", method = RequestMethod.PUT)
    public ObjectAttribute updateAttribute(@PathVariable("objectid") Integer objectId, @RequestBody ObjectAttribute objectAttribute) {
        return pmObjectTypeService.updateAttribute(objectAttribute);
    }
}
