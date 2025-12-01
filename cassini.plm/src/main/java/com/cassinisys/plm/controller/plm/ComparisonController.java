package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.dto.ItemToItemCompareDTO;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.service.plm.ComparisonService;
import com.cassinisys.plm.service.plm.ItemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by subramanyam on 25-08-2020.
 */
@RestController
@RequestMapping("/plm/comparisons")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class ComparisonController extends BaseController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ComparisonService comparisonService;

    @RequestMapping(value = "/item/to/item/comparision/{id}/{itemId}", method = RequestMethod.GET)
    public ItemToItemCompareDTO getComparedItems(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) throws InterruptedException {
        PLMItemRevision fromItem = itemService.getRevision(id);
        PLMItemRevision toItem = itemService.getRevision(itemId);
        return comparisonService.getObjectsForSameRevisionItemsCompare(fromItem, toItem);
    }
}
