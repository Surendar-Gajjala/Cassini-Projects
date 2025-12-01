package com.cassinisys.is.api.procm;
/**
 * The Class is for ItemTypeController
 **/

import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.service.procm.*;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "Item types", description = "Item types endpoint", group = "IS")
@RestController
@RequestMapping("is/itemtypes")
public class ItemTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private MachineTypeService machineTypeService;

    @Autowired
    private MaterialTypeService materialTypeService;

    @Autowired
    private ManpowerTypeService manpowerTypeService;

    @Autowired
    private MaterialReceiveTypeService materialReceiveTypeService;

    @Autowired
    private MaterialIssueTypeService materialIssueTypeService;

    /**
     * The method used for creating the ISItemType
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISItemType create(@RequestBody ISItemType itemType) {
        return itemTypeService.create(itemType);
    }

    /**
     * The method used for updating the ISItemType
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISItemType update(@PathVariable("id") Integer id,
                             @RequestBody ISItemType itemType) {
        return itemTypeService.update(itemType);
    }

    /**
     * The method used for deleting the ISItemType by typeId
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        itemTypeService.delete(id);
    }

    /**
     * The method used for get the list of ISItemType by resourceType
     **/
    @RequestMapping(value = "/resourceType/{resourceType}", method = RequestMethod.GET)
    public List<ISItemType> getResourceType(@PathVariable("resourceType") ResourceType resourceType) {
        return itemTypeService.getResourceType(resourceType);
    }

    /**
     * The method used for get the values of ISItemType by typeId
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISItemType get(@PathVariable("id") Integer id) {
        return itemTypeService.get(id);
    }

    /**
     * The method used for get the list of ISItemType
     **/
    @RequestMapping(method = RequestMethod.GET)
    public List<ISItemType> getAll() {
        return itemTypeService.getRootTypes();
    }

    /**
     * The method used for get the list of multiple ISItemType through the list of typeIds
     **/
    @RequestMapping(value = {"/multiple/[{ids}]"}, method = {RequestMethod.GET})
    public List<ISItemType> getMultiple(@PathVariable Integer[] ids) {
        return itemTypeService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = {"/materialType/multiple/[{ids}]"}, method = {RequestMethod.GET})
    public List<ISMaterialType> getMultipleMaterialTypes(@PathVariable Integer[] ids) {
        return itemTypeService.getMultipleMaterialTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = {"/machineType/multiple/[{ids}]"}, method = {RequestMethod.GET})
    public List<ISMachineType> getMultipleMachineTypes(@PathVariable Integer[] ids) {
        return itemTypeService.getMultipleMachineTypes(Arrays.asList(ids));
    }

    @RequestMapping(value = {"/manpowerType/multiple/[{ids}]"}, method = {RequestMethod.GET})
    public List<ISManpowerType> getMultipleManpowerTypes(@PathVariable Integer[] ids) {
        return itemTypeService.getMultipleManpowerTypes(Arrays.asList(ids));
    }

    /**
     * The method used for get the childrenList of ISItemType by typeId
     **/
    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    public List<ISItemType> getChildren(@PathVariable("id") Integer id) {
        return itemTypeService.getChildren(id);
    }

    /**
     * The method used for get the ClassificationTree of ISItemType
     **/
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<ISItemType> getClassificationTree() {
        return itemTypeService.getClassificationTree();
    }

    /**
     * The method used for get the attributes of ISItemType by typeId and hierarchy
     **/
    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.GET)
    public List<ISItemTypeAttribute> getAttributes(@PathVariable("typeId") Integer typeId,
                                                   @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return itemTypeService.getAttributes(typeId, hierarchy);
    }

    /**
     * The method used for creating the ISItemTypeAttribute
     **/
    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.POST)
    public ISItemTypeAttribute createAttribute(@PathVariable("typeId") Integer typeId,
                                               @RequestBody ISItemTypeAttribute attribute) {
        return itemTypeService.createAttribute(attribute);
    }

    /**
     * The method used for updating the ISItemTypeAttribute
     **/
    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.PUT)
    public ISItemTypeAttribute updateAttribute(@PathVariable("typeId") Integer typeId,
                                               @PathVariable("attributeId") Integer attributeId,
                                               @RequestBody ISItemTypeAttribute attribute) {
        return itemTypeService.updateAttribute(attribute);
    }

    /**
     * The method used for get the ISItemTypeAttribute by attributeId
     **/
    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.GET)
    public ISItemTypeAttribute getAttribute(@PathVariable("typeId") Integer typeId,
                                            @PathVariable("attributeId") Integer attributeId) {
        return itemTypeService.getAttribute(attributeId);
    }

    /**
     * The method used for deleting the ISItemTypeAttribute by attributeId
     **/
    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.DELETE)
    public void deleteAttribute(@PathVariable("typeId") Integer typeId,
                                @PathVariable("attributeId") Integer attributeId) {
        itemTypeService.deleteAttribute(attributeId);
    }

    /**
     * The method used for get the attributes of ISMachineTypeAttribute by typeId and hierarchy
     **/
    @RequestMapping(value = "/{typeId}/machine/attributes", method = RequestMethod.GET)
    public List<ISMachineTypeAttribute> getMachineAttributes(@PathVariable("typeId") Integer typeId,
                                                             @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return machineTypeService.getAttributes(typeId, hierarchy);
    }

    /**
     * The method used for get the attributes of ISMaterialTypeAttribute by typeId and hierarchy
     **/
    @RequestMapping(value = "/{typeId}/material/attributes", method = RequestMethod.GET)
    public List<ISMaterialTypeAttribute> getMaterialAttributes(@PathVariable("typeId") Integer typeId,
                                                               @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return materialTypeService.getAttributes(typeId, hierarchy);
    }

    /**
     * The method used for get the attributes of ISManpowerTypeAttribute by typeId and hierarchy
     **/
    @RequestMapping(value = "/{typeId}/manpower/attributes", method = RequestMethod.GET)
    public List<ISManpowerTypeAttribute> getManpowerAttributes(@PathVariable("typeId") Integer typeId,
                                                               @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return manpowerTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/{typeId}/receiveType/attributes", method = RequestMethod.GET)
    public List<ISMaterialReceiveTypeAttribute> getMaterialReceiveTypeAttributes(@PathVariable("typeId") Integer typeId,
                                                                                 @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return materialReceiveTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/{typeId}/issueType/attributes", method = RequestMethod.GET)
    public List<ISMaterialIssueTypeAttribute> getMaterialIssueTypeAttributes(@PathVariable("typeId") Integer typeId,
                                                                             @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return materialIssueTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/attributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getObjectTypeAttributes(@PathVariable("objectType") String objectType) {
        return materialTypeService.getObjectTypeAttributes(objectType);
    }

    @RequestMapping(value = "/requiredFalseAttributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getMaterialTypeAttributesRequiredFalse(@PathVariable("objectType") String objectType) {
        return materialTypeService.getMaterialTypeAttributesRequiredFalse(objectType);
    }

    @RequestMapping(value = "/requiredTrueAttributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getMaterialTypeAttributesRequiredTrue(@PathVariable("objectType") String objectType) {
        return materialTypeService.getMaterialTypeAttributesRequiredTrue(objectType);
    }

    @RequestMapping(value = {"/receiveTypes/multiple/[{ids}]"}, method = {RequestMethod.GET})
    public List<ISMaterialReceiveType> getMultipleReceiveTypes(@PathVariable Integer[] ids) {
        return materialReceiveTypeService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = {"/issueTypes/multiple/[{ids}]"}, method = {RequestMethod.GET})
    public List<ISMaterialIssueType> getMultipleIssueTypes(@PathVariable Integer[] ids) {
        return materialIssueTypeService.findMultiple(Arrays.asList(ids));
    }

}

