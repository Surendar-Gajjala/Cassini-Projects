package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.model.plm.PLMSavedSearch;
import com.cassinisys.plm.service.plm.SavedSearchService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@RestController
@RequestMapping("/plm/savedsearches")
@Api(tags = "PLM.ITEMS", description = "Items Related")
public class SavedSearchController extends BaseController {

    @Autowired
    private SavedSearchService savedSearchService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PLMSavedSearch creteSavedSearch(@RequestBody PLMSavedSearch PLMSavedSearch) {
        return savedSearchService.create(PLMSavedSearch);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public PLMSavedSearch updateSavedSearch(@RequestBody PLMSavedSearch PLMSavedSearch) {
        return savedSearchService.update(PLMSavedSearch);
    }

    @RequestMapping(value = "/{savedSearchId}", method = RequestMethod.DELETE)
    public void deleteSavedSearch(@PathVariable("savedSearchId") Integer savedSearchId) {
        savedSearchService.delete(savedSearchId);
    }

    @RequestMapping(value = "/{savedSearchId}", method = RequestMethod.GET)
    public PLMSavedSearch getSavedSearch(@PathVariable("savedSearchId") Integer savedSearchId) {
        return savedSearchService.get(savedSearchId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMSavedSearch> getSavedSearches() {
        return savedSearchService.getAll();
    }

    @RequestMapping(value = "/byType/{objectType}", method = RequestMethod.GET)
    public List<PLMSavedSearch> getSavedSearchesByType(@PathVariable("objectType") String objectType) {
        return savedSearchService.findByType(objectType);
    }

    @RequestMapping(value = "/byowner/{owner}", method = RequestMethod.GET)
    public Page<PLMSavedSearch> getSavedSearchesByOwer(@PathVariable("owner") Integer owner, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return savedSearchService.findByOwner(owner, pageable);
    }

    @RequestMapping(value = "/all/count", method = RequestMethod.GET)
    public Long getMessageCount() {
        return new Long(savedSearchService.getSavedSearchesCount().size());
    }

}
