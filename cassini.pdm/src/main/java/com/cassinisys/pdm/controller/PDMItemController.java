package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.filtering.ItemCriteria;
import com.cassinisys.pdm.filtering.ItemPredicateBuilder;
import com.cassinisys.pdm.filtering.ParameterCriteria;
import com.cassinisys.pdm.model.PDMItem;
import com.cassinisys.pdm.model.PDMItemAttribute;
import com.cassinisys.pdm.model.PDMItemFile;
import com.cassinisys.pdm.model.QPDMItem;
import com.cassinisys.pdm.service.PDMItemFileService;
import com.cassinisys.pdm.service.PDMItemService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.mysema.query.types.Predicate;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Api(name = "Items", description = "Items endpoint", group = "PDM")
@RestController
@RequestMapping("pdm/items")
public class PDMItemController extends BaseController{

    @Autowired
    private PDMItemService itemService;

    @Autowired
    private PDMItemFileService itemFileService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ItemPredicateBuilder predicateBuilder;

    // Item Methods

    @RequestMapping(method = RequestMethod.POST)
    public PDMItem create(@RequestBody PDMItem pdmItem){
        pdmItem.setId(null);
        return itemService.create(pdmItem);
    }

    @RequestMapping(value = "/{itemId}/attributes",method = RequestMethod.POST)
    public PDMItemAttribute create(@PathVariable("itemId") Integer itemId,@RequestBody PDMItemAttribute itemAttribute){
        itemAttribute.setId(null);
        return itemService.createAttribute(itemAttribute);
    }

    @RequestMapping(value = "/{itemId}/attributes/multiple", method = RequestMethod.POST)
    public void saveItemAttributes(@PathVariable("itemId") Integer itemId,
                                   @RequestBody List<PDMItemAttribute> attributes) {
        itemService.saveItemAttributes(attributes);
    }

    @RequestMapping(value = "/{itemId}",method = RequestMethod.PUT)
    public PDMItem update(@PathVariable("itemId") Integer itemId,@RequestBody PDMItem pdmItem){
        pdmItem.setId(itemId);
        return itemService.update(pdmItem);
    }

    @RequestMapping(value = "/{itemId}/attributes",method = RequestMethod.PUT)
    public PDMItemAttribute update(@PathVariable("itemId") Integer itemId,@RequestBody PDMItemAttribute itemAttribute){
        return itemService.updateAttribute(itemAttribute);
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("itemId") Integer itemId){
        itemService.delete(itemId);
    }

    @RequestMapping(value = "/{itemId}",method = RequestMethod.GET)
    public PDMItem get(@PathVariable("itemId") Integer itemId){
        return itemService.get(itemId);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<PDMItem> getItemsByType(@PathVariable("typeId") Integer id,
                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getItemsByType(id, pageable);
    }
    @RequestMapping(method = RequestMethod.GET)
    public List<PDMItem> getAll(){
        return itemService.getAll();
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public Page<PDMItem> findAll(PageRequest pageRequest){
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.findAll(pageable);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.GET)
    public List<PDMItemAttribute> getItemAttributes(@PathVariable("id") Integer id) {
        return itemService.getItemAttributes(id);
    }
    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<PDMItem> freeTextSearch(PageRequest pageRequest, ItemCriteria itemCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PDMItem> pdmItems = itemService.freeTextSearch(pageable, itemCriteria);
        return pdmItems;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<PDMItem> search(ItemCriteria criteria, PageRequest pageRequest) {
        Predicate predicate = predicateBuilder.build(criteria,
                QPDMItem.pDMItem);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PDMItem> pdmItems = itemService.searchItems(predicate, pageable);
        return pdmItems;
    }

    @RequestMapping(value = "/advancedsearch", method = RequestMethod.POST)
    public Page<PDMItem> advancedSearch(@RequestBody ParameterCriteria[] parameterCriterias, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PDMItem> pdmItems = itemService.advancedSearchItem(parameterCriterias, pageable);
        return pdmItems;
    }
}
