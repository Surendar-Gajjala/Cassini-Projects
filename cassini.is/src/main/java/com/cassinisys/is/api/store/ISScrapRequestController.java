package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.ScrapRequestCriteria;
import com.cassinisys.is.model.store.ISScrapRequest;
import com.cassinisys.is.service.store.ISScrapRequestService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Varsha Malgireddy on 8/28/2018.
 */
@RestController
@RequestMapping("is/stores/scraps")
public class ISScrapRequestController extends BaseController {

    @Autowired
    private ISScrapRequestService isScrapRequestService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    private ISScrapRequest createScrapRequest(@RequestBody ISScrapRequest isScrapRequest) {
        return isScrapRequestService.createScrapRequest(isScrapRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISScrapRequest updateScrap(@PathVariable("id") Integer id, @RequestBody ISScrapRequest isScrapRequest) {
        isScrapRequest.setId(id);
        return isScrapRequestService.updateScrap(isScrapRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISScrapRequest get(@PathVariable("id") Integer id) {
        return isScrapRequestService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<ISScrapRequest> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return isScrapRequestService.getAll(pageable);
    }

    @RequestMapping(value = "/scrapAttributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getScrapAttributes(@PathVariable("objectType") String objectType) {
        return isScrapRequestService.getScrapAttributes(objectType);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<ISScrapRequest> freeTextSearch(PageRequest pageRequest, ScrapRequestCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISScrapRequest> scrapRequests = isScrapRequestService.freeTextSearch(pageable, criteria);
        return scrapRequests;
    }

}
