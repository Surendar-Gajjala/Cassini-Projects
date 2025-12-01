package com.cassinisys.platform.api.core;

import com.cassinisys.platform.model.core.AttributesDetailsDTO;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.service.core.ObjectTypeAttributeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by GSR on 03-07-2017.
 */
@RestController
@RequestMapping("/core/objects/types/{type}/attributes")
@Api(tags = "PLATFORM.CORE", description = "Core endpoints")
public class ObjectTypeAttributeController extends BaseController {

    @Autowired
    private ObjectTypeAttributeService objectTypeAttributeService;

    @RequestMapping(method = RequestMethod.POST)
    public ObjectTypeAttribute createObjectTypeAttribute(@RequestBody ObjectTypeAttribute objectTypeAttribute) {
        return objectTypeAttributeService.create(objectTypeAttribute);
    }

    @RequestMapping(value = "/{attributeId}", method = RequestMethod.PUT)
    public ObjectTypeAttribute updateObjectTypeAttribute(@PathVariable("attributeId") Integer attributeId,
                                                         @RequestBody ObjectTypeAttribute objectTypeAttribute) {
        return objectTypeAttributeService.update(objectTypeAttribute);
    }

    @RequestMapping(value = "/{attributeId}", method = RequestMethod.DELETE)
    public void deleteObjectTypeAttribute(@PathVariable("attributeId") Integer attributeId) {
        objectTypeAttributeService.delete(attributeId);
    }

    @RequestMapping(value = "/{attributeId}", method = RequestMethod.GET)
    public ObjectTypeAttribute getObjectTypeAttributeDefs(@PathVariable("attributeId") Integer attributeId) {
        return objectTypeAttributeService.get(attributeId);
    }

    @RequestMapping(value = "/objectId/{objectId}", method = RequestMethod.GET)
    public AttributesDetailsDTO getObjectTypeAttributesByObjectType(@PathVariable("type") String type, @PathVariable("objectId") Integer objectId) {
        return objectTypeAttributeService.getObjectTypeAttributesByObjectType(type, objectId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getObjectTypeAttributeByType(@PathVariable("type") String type) {
        return objectTypeAttributeService.getByObjectTypeAttribute(type);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getObjectTypeAttributeByIds(@PathVariable("type") String type, @PathVariable("ids") Integer[] ids) {
        return objectTypeAttributeService.findAllObjectTypeAttributesByIds(Arrays.asList(ids));
    }

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public List<String> getAttributesGroups(@PathVariable("type") String type) {
        return objectTypeAttributeService.getAttributesGroups();
    }

    @RequestMapping(value = "/groups/byName", method = RequestMethod.GET)
    public List<String> getAttributesGroupsByName(@RequestParam("groupName") String groupName) {
        return objectTypeAttributeService.getAttributesGroupsByName(groupName);
    }

    @RequestMapping(value = "/{attributeId}/groups/update", method = RequestMethod.GET)
    public ObjectTypeAttribute updateAttributeGroupName(@PathVariable("attributeId") Integer attributeId, @RequestParam("groupName") String groupName) {
        return objectTypeAttributeService.updateAttributeGroupName(attributeId, groupName);
    }
}
