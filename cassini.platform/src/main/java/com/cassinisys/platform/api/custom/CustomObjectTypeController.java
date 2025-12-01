package com.cassinisys.platform.api.custom;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomObjectType;
import com.cassinisys.platform.model.custom.CustomObjectTypeAttribute;
import com.cassinisys.platform.model.dto.CustomObjectDto;
import com.cassinisys.platform.model.dto.CustomObjectTypeAttributeDto;
import com.cassinisys.platform.service.custom.CustomObjectTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/customobjecttypes")
@Api(tags = "PLATFORM.CUSTOM", description = "Custom endpoints")
public class CustomObjectTypeController extends BaseController {
    @Autowired
    private CustomObjectTypeService customObjectTypeService;

    @RequestMapping(method = RequestMethod.POST)
    public CustomObjectType create(@RequestBody CustomObjectType customObjectType) {
        return customObjectTypeService.create(customObjectType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CustomObjectType update(@PathVariable("id") Integer id, @RequestBody CustomObjectType customObjectType) {
        return customObjectTypeService.update(id, customObjectType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomObjectType get(@PathVariable Integer id) {
        return customObjectTypeService.get(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<CustomObjectType> getAll() {
        return customObjectTypeService.getAll();
    }

    @RequestMapping(value = "/roots", method = RequestMethod.GET)
    public List<CustomObjectType> getRoots() {
        return customObjectTypeService.getRootTypes();
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<CustomObjectType> getTree() {
        return customObjectTypeService.getClassificationTree();
    }

    @RequestMapping(value = "/{id}/tree", method = RequestMethod.GET)
    public CustomObjectType getCustomObjectTypeTree(@PathVariable Integer id) {
        return customObjectTypeService.getCustomObjectTypeTree(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) {
        customObjectTypeService.delete(id);
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    public List<CustomObjectType> getChildren(@PathVariable Integer id) {
        return customObjectTypeService.getChildren(id);
    }

    @RequestMapping(value = "/[{ids}]", method = RequestMethod.GET)
    public List<CustomObjectType> getMultiple(@PathVariable Integer[] ids) {
        return customObjectTypeService.getMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.GET)
    public List<CustomObjectTypeAttribute> getAttributes(@PathVariable Integer id,
                                                         @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return customObjectTypeService.getAttributes(id, hierarchy);
    }

    @RequestMapping(value = "/{typeid}/attributes/{attid}", method = RequestMethod.GET)
    public CustomObjectTypeAttribute getAttribute(@PathVariable Integer typeid,
                                                  @PathVariable Integer attid) {
        return customObjectTypeService.getAttribute(attid);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public CustomObjectTypeAttribute createAttribute(@PathVariable Integer id,
                                                     @RequestBody CustomObjectTypeAttribute customObjectTypeAttribute) {
        return customObjectTypeService.createAttribute(customObjectTypeAttribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public CustomObjectTypeAttribute updateAttribute(@PathVariable Integer id,
                                                     @RequestBody CustomObjectTypeAttribute customObjectTypeAttribute) {
        return customObjectTypeService.updateAttribute(customObjectTypeAttribute);
    }

    @RequestMapping(value = "/{typeid}/attributes/{attid}", method = RequestMethod.DELETE)
    public void deleteAttribute(@PathVariable Integer typeid,
                                @PathVariable Integer attid) {
        customObjectTypeService.deleteAttribute(attid);
    }


    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public List<CustomObjectType> getAllObjectTypes() {
        return customObjectTypeService.getAllObjectTypes();
    }

    @RequestMapping(value = "/{objectType}/{typeId}/attributes", method = RequestMethod.GET)
    public CustomObjectTypeAttributeDto getCustomObjectTypeAttributes(@PathVariable("objectType") ObjectType objectType, @PathVariable("typeId") Integer typeId, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return customObjectTypeService.getCustomObjectTypeAttributes(objectType, typeId, hierarchy);
    }


    @RequestMapping(value = "/{objectType}/{typeId}/{objectId}/attributes/values", method = RequestMethod.GET)
    public CustomObjectTypeAttributeDto getQualityTypeAttributeValues(@PathVariable("objectType") ObjectType objectType, @PathVariable("typeId") Integer typeId,
                                                                      @PathVariable("objectId") Integer objectId) {
        return customObjectTypeService.getCustomObjectTypeAttributeValues(objectType, typeId, objectId);
    }

    @RequestMapping(value = "/navigation/types", method = RequestMethod.GET)
    public List<CustomObjectType> getNavigationCustomTypes() {
        return customObjectTypeService.getNavigationCustomTypes();
    }

    @RequestMapping(value = "/byname/{name}", method = RequestMethod.GET)
    public CustomObjectDto getCustomObjectTypeName(@PathVariable String name) {
        return customObjectTypeService.getCustomObjectTypeByName(name);
    }
}
