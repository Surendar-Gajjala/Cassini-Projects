package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.CustomIndentCriteria;
import com.cassinisys.is.model.store.CustomIndent;
import com.cassinisys.is.model.store.CustomIndentItem;
import com.cassinisys.is.model.store.IndentStatus;
import com.cassinisys.is.service.store.CustomIndentService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@RestController
@RequestMapping("is/stores/indents")
@Api(name = "CustomIndent", description = "CustomIndent endpoint", group = "IS")
public class CustomIndentController extends BaseController {

    @Autowired
    private CustomIndentService customIndentService;

    @Autowired
    private PageRequestConverter pageRequestConverter;


   /*  methods for CustomIndent */

    @RequestMapping(method = RequestMethod.POST)
    public CustomIndent create(@RequestBody CustomIndent customIndent) {
        customIndent.setStatus(IndentStatus.NEW);
        return customIndentService.create(customIndent);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CustomIndent update(@PathVariable("id") Integer id, @RequestBody CustomIndent customIndent) {
        customIndent.setId(id);
        return customIndentService.update(customIndent);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        customIndentService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomIndent get(@PathVariable("id") Integer id) {
        return customIndentService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomIndent> getAll() {
        return customIndentService.getAll();
    }

    @RequestMapping(value = "/byStore/{storeId}/pageable", method = RequestMethod.GET)
    public Page<CustomIndent> getPageableIndents(@PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customIndentService.getPageableIndentsByStore(pageable, storeId);
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<CustomIndent> getAllIndents(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customIndentService.getAllIndents(pageable);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<CustomIndent> customIndentFreeTextSearch(PageRequest pageRequest, CustomIndentCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<CustomIndent> items = customIndentService.customIndentFreeTextSearch(pageable, criteria);
        return items;
    }

   /*  methods for to get indent attributes */

    @RequestMapping(value = "/requiredIndentAttributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getRequiredIndentAttributes(@PathVariable("objectType") String objectType) {
        return customIndentService.getRequiredIndentAttributes(objectType);
    }

    @RequestMapping(value = "/objectAttributes", method = RequestMethod.POST)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByIndentAndAttributeId(@RequestBody List<Integer[]> ids) {
        Integer[] indentIds = ids.get(0);
        Integer[] attIds = ids.get(1);
        return customIndentService.getObjectAttributesByIndentIdsAndAttributeIds(indentIds, attIds);
    }

     /*  methods for CustomIndentItem */

    @RequestMapping(value = "/{id}/customIndentItems", method = RequestMethod.POST)
    public List<CustomIndentItem> createIndentChalanItem(@PathVariable("id") Integer id, @RequestBody List<CustomIndentItem> customIndentItems) {
        return customIndentService.createIndentItems(customIndentItems);
    }

    @RequestMapping(value = "/{id}/customIndentItem/{customIndentItemId}", method = RequestMethod.DELETE)
    public void deleteCustomIndentItem(@PathVariable("id") Integer id, @PathVariable("customIndentItemId") Integer customIndentItemId) {
        customIndentService.deleteCustomIndentItem(customIndentItemId);
    }

    @RequestMapping(value = "/{id}/customIndentItem/{customIndentItemId}", method = RequestMethod.PUT)
    public CustomIndentItem updateCustomIndentItem(@PathVariable("id") Integer id,
                                                   @PathVariable("customIndentItemId") Integer customIndentItemId,
                                                   @RequestBody CustomIndentItem customIndentItem) {
        customIndentItem.setId(customIndentItemId);
        return customIndentService.updateIndentItem(customIndentItem);
    }

    @RequestMapping(value = "/{id}/customIndentItem/{customIndentItemId}", method = RequestMethod.GET)
    public CustomIndentItem getCustomIndentItem(@PathVariable("id") Integer id,
                                                @PathVariable("customIndentItemId") Integer customIndentItemId) {
        return customIndentService.getCustomIndentItem(customIndentItemId);
    }

    @RequestMapping(value = "/{id}/customIndentItems", method = RequestMethod.GET)
    public List<CustomIndentItem> getAllCustomIndentItems() {
        return customIndentService.getAllCustomIndentItems();
    }
}
