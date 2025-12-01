package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.plm.BOMConfiguration;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.dto.BomDto;
import com.cassinisys.plm.service.plm.BOMConfigurationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Nageshreddy on 16-03-2020.
 */

@RestController
@RequestMapping("/plm/bomConfig")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class BomConfigurationController extends BaseController {

    @Autowired
    private BOMConfigurationService bomConfigurationService;

    @RequestMapping(method = RequestMethod.POST)
    public BOMConfiguration createBomConfiguration(@RequestBody BOMConfiguration bomConfiguration) {
        return bomConfigurationService.create(bomConfiguration);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public BOMConfiguration updateBomConfiguration(@PathVariable("id") Integer id,
                                                   @RequestBody BOMConfiguration bomConfiguration) {
        bomConfiguration.setId(id);
        return bomConfigurationService.update(bomConfiguration);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteConfig(@PathVariable("id") Integer id) {
        bomConfigurationService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BOMConfiguration getBomConfig(@PathVariable("id") Integer id) {
        return bomConfigurationService.get(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<BOMConfiguration> getAll() {
        return bomConfigurationService.getAll();
    }

    @RequestMapping(value = "item/{itemId}", method = RequestMethod.GET)
    public List<BOMConfiguration> getBomConfigsByItem(@PathVariable("itemId") Integer itemId) {
        return bomConfigurationService.getBomConfigsByItem(itemId);
    }

    @RequestMapping(value = "itemDetails/{id}", method = RequestMethod.GET)
    public PLMItemRevision getBomConfigItemWithValues(@PathVariable("id") Integer id) {
        return bomConfigurationService.getBomConfigItemWithValues(id);
    }

    @RequestMapping(value = "resolve/{id}", method = RequestMethod.GET)
    public List<BomDto> resolveBomConfiguration(@PathVariable("id") Integer id) {
        return bomConfigurationService.resolveSelectedBOMConfig(id);
    }

    @RequestMapping(value = "inclusions/{id}", method = RequestMethod.GET)
    public List<String> getBomConfigurationInclusions(@PathVariable("id") Integer id) {
        return bomConfigurationService.getBomConfigurationInclusions(id);
    }

    @RequestMapping(value = "attributeExclusion/{id}", method = RequestMethod.GET)
    public List<String> getBomConfigurationAttributeInclusions(@PathVariable("id") Integer id) {
        return bomConfigurationService.getBomConfigurationAttributeInclusions(id);
    }

    @RequestMapping(value = "/items/{id}/createItemInstances", method = RequestMethod.POST)
    public PLMItem createItemInstances(@PathVariable("id") Integer id, @RequestBody PLMItem item) {
        return bomConfigurationService.createItemInstances(id, item, true);
    }

    @RequestMapping(value = "/items//{id}/createAllInstance", method = RequestMethod.POST)
    public List<PLMItem> createAllInstance(@PathVariable("id") Integer id, @RequestBody List<PLMItem> items) {
        return bomConfigurationService.createItemAllInstances(id, items);
    }

    @RequestMapping(value = "/items/{id}/getAllCombinations", method = RequestMethod.GET)
    public List<PLMItem> getAllCombinations(@PathVariable("id") Integer id) {
        return bomConfigurationService.getAllCombinations(id);
    }

    @RequestMapping(value = "/{itemId}/bomConfiguration", method = RequestMethod.POST)
    public BOMConfiguration createBomConfiguration(@PathVariable("itemId") Integer itemId, @RequestBody BOMConfiguration bomConfiguration) {
        return bomConfigurationService.createBomConfiguration(itemId, bomConfiguration);
    }
}
