package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.plm.model.dto.RelatedItemDto;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.PLMRelatedItem;
import com.cassinisys.plm.model.plm.PLMRelatedItemAttribute;
import com.cassinisys.plm.model.plm.dto.RelatedItemsDto;
import com.cassinisys.plm.service.plm.RelatedItemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyamreddy on 011 11-Jan -18.
 */
@RestController
@RequestMapping("/plm/relatedItems")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class RelatedItemController extends BaseController {

    @Autowired
    private RelatedItemService relatedItemService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMRelatedItem create(@RequestBody PLMRelatedItem relatedItem) {
        return relatedItemService.create(relatedItem);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMRelatedItem update(@PathVariable("id") Integer id, @RequestBody PLMRelatedItem relatedItem) {
        return relatedItemService.update(relatedItem);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void update(@PathVariable("id") Integer id) {
        relatedItemService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMRelatedItem get(@PathVariable("id") Integer id) {
        return relatedItemService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMRelatedItem> getAll() {
        return relatedItemService.getAll();
    }

    @RequestMapping(value = "/relationship/{relationshipId}", method = RequestMethod.GET)
    public List<PLMRelatedItem> getRelatedItemsByRelationship(@PathVariable("relationshipId") Integer relationshipId) {
        return relatedItemService.getRelatedItemsByRelationShip(relationshipId);
    }

    @RequestMapping(value = "/{itemId}/items", method = RequestMethod.GET)
    public List<RelatedItemsDto> getRelatedItems(@PathVariable("itemId") Integer itemId) {
        return relatedItemService.getRelatedItems(itemId);
    }

    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.GET)
    public List<PLMRelatedItem> getRelatedItemsByItem(@PathVariable("itemId") Integer itemId) {
        return relatedItemService.getRelatedItemsByItem(itemId);
    }

    @RequestMapping(value = "/multiple", method = RequestMethod.POST)
    public List<PLMRelatedItem> saveMultipleRelatedItems(@RequestBody List<PLMRelatedItem> relatedItems) {
        return relatedItemService.saveMultipleRelatedItems(relatedItems);
    }

    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public List<ObjectAttribute> saveRelatedItemAttributes(@PathVariable("id") Integer id,
                                                           @RequestBody List<PLMRelatedItemAttribute> attributes) {
        return relatedItemService.saveRelatedItemAttributes(attributes);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.GET)
    public List<PLMRelatedItemAttribute> getRelatedItemAttributes(@PathVariable("id") Integer id) {
        return relatedItemService.getRelatedItemAttributes(id);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PLMRelatedItemAttribute createRelatedItemAttribute(@PathVariable("id") Integer id,
                                                              @RequestBody PLMRelatedItemAttribute attributes) {
        return relatedItemService.createRelatedItemAttribute(attributes);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PLMRelatedItemAttribute updateRelatedItemAttribute(@PathVariable("id") Integer id,
                                                              @RequestBody PLMRelatedItemAttribute attribute) {
        return relatedItemService.updateRelatedItemAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/items/object", method = RequestMethod.GET)
    public List<RelatedItemDto> getRelatedItemsByObject(@PathVariable("id") Integer id,
                                                        @RequestParam("objectType") PLMObjectType objectType) {
        return relatedItemService.getRelatedItemsByObject(id,objectType);
    }

    @RequestMapping(value = "/object/{itemId}/item", method = RequestMethod.DELETE)
    public void deleteRelatedItem(@PathVariable("itemId") Integer itemId,
                                  @RequestParam("objectType") PLMObjectType objectType) {
        relatedItemService.deleteRelatedItemByObject(itemId,objectType);
    }

}
