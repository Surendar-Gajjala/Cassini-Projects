package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.model.PDMItemType;
import com.cassinisys.pdm.model.PDMItemTypeAttribute;
import com.cassinisys.pdm.service.PDMItemTypeService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Api(name = "Item types", description = "Item types endpoint", group = "PDM")
@RestController
@RequestMapping("pdm/itemTypes")
public class PDMItemTypeController extends BaseController {

    @Autowired
    private PDMItemTypeService itemTypeService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PDMItemType create(@RequestBody PDMItemType itemType) {
        itemType.setId(null);
        return itemTypeService.create(itemType);
    }

    @RequestMapping(value = "/{typeId}/attribute", method = RequestMethod.POST)
    public PDMItemTypeAttribute create(@PathVariable("typeId") Integer typeId, @RequestBody PDMItemTypeAttribute typeAttribute) {
        typeAttribute.setId(null);
        return itemTypeService.createType(typeAttribute);
    }

    @RequestMapping(value = "/{typeId}", method = RequestMethod.PUT)
    public PDMItemType update(@PathVariable("typeId") Integer typeId, @RequestBody PDMItemType itemType) {
        itemType.setId(typeId);
        return itemTypeService.update(itemType);
    }

    @RequestMapping(value = "/{typeId}/attribute/{attributeId}", method = RequestMethod.PUT)
    public PDMItemTypeAttribute update(@PathVariable("typeId") Integer typeId, @PathVariable("attributeId") Integer attributeId,
                                       @RequestBody PDMItemTypeAttribute typeAttribute) {
        typeAttribute.setId(attributeId);
        return itemTypeService.updateType(typeAttribute);
    }

    @RequestMapping(value = "/{typeId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("typeId") Integer typeId) {
        itemTypeService.delete(typeId);
    }


    @RequestMapping(value = "/{typeId}", method = RequestMethod.GET)
    public PDMItemType get(@PathVariable("typeId") Integer typeId) {
        return itemTypeService.get(typeId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PDMItemType> getAll() {
        return itemTypeService.getAll();
    }


    @RequestMapping(value = "/{typeId}/attributes", method = RequestMethod.GET)
    public List<PDMItemTypeAttribute> getAttributes(@PathVariable("typeId") Integer typeId,
                                                    @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return itemTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<PDMItemType> findAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemTypeService.findAll(pageable);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<PDMItemType> getClassificationTree() {
        return itemTypeService.getClassificationTree();
    }

}
