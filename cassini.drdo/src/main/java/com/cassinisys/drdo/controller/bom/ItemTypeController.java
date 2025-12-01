package com.cassinisys.drdo.controller.bom;

import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.TypeCodeDto;
import com.cassinisys.drdo.service.bom.ItemTypeService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by subra on 03-10-2018.
 */
@RestController
@RequestMapping("drdo/itemTypes")
public class ItemTypeController extends BaseController {

    @Autowired
    private ItemTypeService itemTypeService;


    @RequestMapping(method = RequestMethod.POST)
    public ItemType create(@RequestBody ItemType itemType) {
        return itemTypeService.create(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ItemType update(@PathVariable("id") Integer id,
                           @RequestBody ItemType itemType) {
        return itemTypeService.update(itemType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        itemTypeService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ItemType get(@PathVariable("id") Integer id) {
        return itemTypeService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ItemType> getAll() {
        return itemTypeService.getRootTypes();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ItemType> getMultiple(@PathVariable Integer[] ids) {
        return itemTypeService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    public List<ItemType> getChildren(@PathVariable("id") Integer id) {
        return itemTypeService.getChildren(id);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<ItemType> getClassificationTree() {
        return itemTypeService.getClassificationTree();
    }

    @RequestMapping(value = "/{itemTypeId}/items", method = RequestMethod.GET)
    public List<Item> getItemsByTypeId(@PathVariable("itemTypeId") Integer itemTypeId) {
        return itemTypeService.getItemsByTypeId(itemTypeId);
    }

    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.POST)
    public ItemTypeAttribute createAttribute(@PathVariable("typeId") Integer typeId,
                                             @RequestBody ItemTypeAttribute attribute) {
        return itemTypeService.createAttribute(attribute);
    }

    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.GET)
    public ItemTypeAttribute getAttribute(@PathVariable("typeId") Integer typeId,
                                          @PathVariable("attributeId") Integer attributeId) {
        return itemTypeService.getAttribute(attributeId);
    }

    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.PUT)
    public ItemTypeAttribute getAttribute(@PathVariable("typeId") Integer typeId,
                                          @PathVariable("attributeId") Integer attributeId,
                                          @RequestBody ItemTypeAttribute attribute) {
        return itemTypeService.updateAttribute(attribute);
    }

    @RequestMapping(value = "/{typeId}/attributes/{attributeId}", method = RequestMethod.DELETE)
    public void deleteAttribute(@PathVariable("typeId") Integer typeId,
                                @PathVariable("attributeId") Integer attributeId) {
        itemTypeService.deleteAttribute(attributeId);
    }

    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.GET)
    public List<ItemTypeAttribute> getAttributes(@PathVariable("typeId") Integer typeId,
                                                 @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return itemTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/items/attributes/{attributeId}", method = RequestMethod.GET)
    public List<ItemAttributeValue> getAttributeUsedItems(@PathVariable("attributeId") Integer attributeId) {
        return itemTypeService.getItemUsedAttributes(attributeId);
    }

    @RequestMapping(value = "/objectTypeAttributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getAllObjectTypeAttributes(@PathVariable("objectType") String objectType) {
        return itemTypeService.getAllObjectTypeAttributes(objectType);
    }

    @RequestMapping(value = "/autoNumber/{autoNumberId}/items", method = RequestMethod.GET)
    public List<Item> getItemsByAutoNumberId(@PathVariable("autoNumberId") Integer autoNumberId) {
        return itemTypeService.getItemsByAutoNumberId(autoNumberId);
    }

    @RequestMapping(value = "/{typeId}/parentNode", method = RequestMethod.GET)
    public TypeCodeDto getTypeParentNode(@PathVariable("typeId") Integer typeId) {
        return itemTypeService.getTypeParentNode(typeId);
    }

    @RequestMapping(value = "/{typeId}/specs", method = RequestMethod.POST)
    public ItemTypeSpecs createItemTypeSpec(@PathVariable("typeId") Integer typeId, @RequestBody ItemTypeSpecs itemTypeSpec) {
        return itemTypeService.createItemTypeSpec(typeId, itemTypeSpec);
    }

    @RequestMapping(value = "/{typeId}/specs/{specId}", method = RequestMethod.PUT)
    public ItemTypeSpecs updateItemTypeSpec(@PathVariable("typeId") Integer typeId, @PathVariable("specId") Integer specId,
                                            @RequestBody ItemTypeSpecs itemTypeSpec) {
        return itemTypeService.updateItemTypeSpec(typeId, specId, itemTypeSpec);
    }

    @RequestMapping(value = "/{typeId}/specs/{specId}", method = RequestMethod.DELETE)
    public void deleteItemTypeSpec(@PathVariable("typeId") Integer typeId, @PathVariable("specId") Integer specId) {
        itemTypeService.deleteItemTypeSpec(typeId, specId);
    }

    @RequestMapping(value = "/{typeId}/typeSpecs", method = RequestMethod.GET)
    public List<ItemTypeSpecs> getItemTypeSpecs(@PathVariable("typeId") Integer typeId) {
        return itemTypeService.getItemTypeSpecs(typeId);
    }

    @RequestMapping(value = "/{typeId}/specs/{specId}/items", method = RequestMethod.GET)
    public List<Item> getItemsBySpec(@PathVariable("typeId") Integer typeId, @PathVariable("specId") Integer specId) {
        return itemTypeService.getItemsBySpec(typeId, specId);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<ItemType> getAllItemTypes() {
        return itemTypeService.getAllItemTypes();
    }

    @RequestMapping(value = "/all/system", method = RequestMethod.GET)
    public List<ItemType> getAllItemTypesWithoutSystem() {
        return itemTypeService.getAllItemTypesWithoutSystem();
    }

    @RequestMapping(value = "/{parentId}/import", method = RequestMethod.POST)
    public void importClassification(@PathVariable("parentId") Integer parentId, MultipartHttpServletRequest request) throws Exception {
        itemTypeService.importClassification(parentId, request);
    }

}
