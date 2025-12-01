package com.cassinisys.is.api.store;

import com.cassinisys.is.model.store.ISTopSite;
import com.cassinisys.is.service.store.TopSiteService;
import com.cassinisys.platform.api.core.BaseController;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@RestController
@RequestMapping("is/stores/sites")
@Api(name = "ISTopSite", description = "ISTopSite endpoint", group = "IS")
public class TopSiteController extends BaseController {

    @Autowired
    private TopSiteService topSiteService;

    @RequestMapping(method = RequestMethod.POST)
    public ISTopSite create(@RequestBody ISTopSite item) {
        return topSiteService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISTopSite update(@PathVariable("id") Integer id, @RequestBody ISTopSite item) {
        item.setId(id);
        return topSiteService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        topSiteService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISTopSite get(@PathVariable("id") Integer id) {
        return topSiteService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISTopSite> get() {
        return topSiteService.getAll();
    }
}
