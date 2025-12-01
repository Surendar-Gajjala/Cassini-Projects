package com.cassinisys.is.api.store;

import com.cassinisys.is.model.store.ISScrapRequestItem;
import com.cassinisys.is.service.store.ISScrapRequestItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Varsha Malgireddy on 9/4/2018.
 */
@RestController
@RequestMapping("is/stores/scrapItem")
public class ISScrapRequestItemController {

    @Autowired
    private ISScrapRequestItemService scrapRequestItemService;

    @RequestMapping(method = RequestMethod.POST)
    private List<ISScrapRequestItem> createScrapRequest(@RequestBody List<ISScrapRequestItem> isScrapRequestItems) {
        return scrapRequestItemService.createScrapItem(isScrapRequestItems);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISScrapRequestItem updateScrapItem(@PathVariable("id") Integer id, @RequestBody ISScrapRequestItem isScrapRequest) {
        isScrapRequest.setId(id);
        return scrapRequestItemService.updateScrapItem(isScrapRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public List<ISScrapRequestItem> getItem(@PathVariable("id") Integer id) {
        return scrapRequestItemService.getScrapItem(id);
    }

}
