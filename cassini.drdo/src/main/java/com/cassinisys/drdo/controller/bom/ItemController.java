package com.cassinisys.drdo.controller.bom;

import com.cassinisys.drdo.filtering.ItemSearchCriteria;
import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.ItemReportDto;
import com.cassinisys.drdo.service.bom.ItemService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by subra on 04-10-2018.
 */
@RestController
@RequestMapping("drdo/items")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public Item create(@RequestBody Item item) {
        return itemService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Item update(@PathVariable("id") Integer id,
                       @RequestBody Item item) {
        return itemService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        itemService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Item get(@PathVariable("id") Integer id) {
        return itemService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Item> getAll() {
        return itemService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<Item> getAllItems(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getAllItems(pageable);
    }

    @RequestMapping(value = {"/revisions/multiple/[{ids}]"}, method = {RequestMethod.GET})
    public List<ItemRevision> getMultipleItemMaster(@PathVariable Integer[] ids) {
        return itemService.getRevisionsByIds(Arrays.asList(ids));
    }

    @RequestMapping(value = "/{id}/uploadImage", method = RequestMethod.POST)
    public Item uploadImage(@PathVariable("id") Integer id, MultipartHttpServletRequest request) {

        Item item1 = itemService.get(id);
        if (item1 != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    item1.setThumbnail(file.getBytes());
                    item1 = itemService.uploadThumbnailImage(item1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return item1;
    }

    @RequestMapping(value = "/updateImageValue/{objectId}/{attributeId}", method = RequestMethod.POST)
    public Item saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                        @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return itemService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/{itemId}/itemImageAttribute/download", method = RequestMethod.GET)
    public void downloadImageItem(@PathVariable("itemId") Integer itemId,
                                  HttpServletResponse response) {
        itemService.getImageItem(itemId, response);
    }

    @RequestMapping(value = "/{id}/attributes/multiple", method = RequestMethod.POST)
    public List<ObjectAttribute> saveItemAttributes(@PathVariable("id") Integer id,
                                                    @RequestBody List<ItemAttributeValue> attributes) {
        return itemService.saveItemAttributes(attributes);
    }

    @RequestMapping(value = "/revision/{revisionId}", method = RequestMethod.GET)
    public ItemRevision getRevision(@PathVariable("revisionId") Integer revisionId) {
        return itemService.getRevision(revisionId);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.GET)
    public List<ItemAttributeValue> getItemAttributes(@PathVariable("id") Integer id) {
        return itemService.getItemAttributes(id);
    }

    @RequestMapping(value = "/{revisionId}/revisionAttributes", method = RequestMethod.GET)
    public List<ItemRevisionAttributeValue> getItemRevisionAttributes(@PathVariable("revisionId") Integer revisionId) {
        return itemService.getItemRevisionAttributes(revisionId);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public ItemAttributeValue createItemAttribute(@PathVariable("id") Integer id,
                                                  @RequestBody ItemAttributeValue attribute) {
        return itemService.createItemAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/revisionAttributes", method = RequestMethod.POST)
    public ItemRevisionAttributeValue createItemRevisionAttribute(@PathVariable("id") Integer id,
                                                                  @RequestBody ItemRevisionAttributeValue attribute) {
        return itemService.createItemRevisionAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public ItemAttributeValue updateItemAttribute(@PathVariable("id") Integer id,
                                                  @RequestBody ItemAttributeValue attribute) {
        return itemService.updateItemAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/revisionAttributes", method = RequestMethod.PUT)
    public ItemRevisionAttributeValue updateItemRevisionAttribute(@PathVariable("id") Integer id,
                                                                  @RequestBody ItemRevisionAttributeValue attribute) {
        return itemService.updateItemRevisionAttribute(attribute);
    }

    @RequestMapping(value = "/byName/{name}", method = RequestMethod.GET)
    public Item getItemByName(@PathVariable("name") String name) {
        return itemService.getItemByName(name);
    }

    @RequestMapping(value = "/newItemSearch", method = RequestMethod.GET)
    public Page<Item> getItemSearchResults(PageRequest pageRequest, ItemSearchCriteria itemSearchCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getItemSearchResults(itemSearchCriteria, pageable);
    }

    @RequestMapping(value = "/objectAttributes", method = RequestMethod.POST)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByItemAndAttributeId(@RequestBody List<Integer[]> ids) {

        Integer[] objectIds = ids.get(0);
        Integer[] attIds = ids.get(1);
        return itemService.getObjectAttributesByItemIdsAndAttributeIds(objectIds, attIds);
    }

    @RequestMapping(value = "/instances/{itemId}", method = RequestMethod.GET)
    public List<ItemInstance> getItemInstances(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemInstances(itemId);
    }

    @RequestMapping(value = "/instances/from20/{itemId}", method = RequestMethod.GET)
    public List<ItemInstance> getItemInstancesFrom20(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemInstancesFrom20(itemId);
    }

    @RequestMapping(value = "/instances/{id}", method = RequestMethod.PUT)
    public ItemInstance updateItemInstance(@PathVariable("id") Integer id, @RequestBody ItemInstance itemInstance) {
        itemInstance.setId(id);
        return itemService.updateItemInstance(itemInstance);
    }

    @RequestMapping(value = "/instances/failure/{id}", method = RequestMethod.PUT)
    public ItemInstance updateFailItemInstance(@PathVariable("id") Integer id, @RequestBody ItemInstance itemInstance) {
        itemInstance.setId(id);
        return itemService.updateFailureItemInstance(itemInstance);
    }

    @RequestMapping(value = "/lotInstances/failure/{id}", method = RequestMethod.PUT)
    public LotInstance updateLotInstance(@PathVariable("id") Integer id, @RequestBody LotInstance lotInstance) {
        lotInstance.setId(id);
        return itemService.updateFailureLotInstance(lotInstance);
    }

    @RequestMapping(value = "/check/{mfr}", method = RequestMethod.GET)
    public Boolean checkWithMfr(@PathVariable("mfr") Integer mfr) {
        return itemService.checkItemInstanceWithMfr(mfr);
    }

    @RequestMapping(value = "/{itemId}/report", method = RequestMethod.GET)
    public List<ItemReportDto> getItemReport(@PathVariable("itemId") Integer itemId) {
        return itemService.getItemReport(itemId);
    }
}
