package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.CustomRoadChallanCriteria;
import com.cassinisys.is.model.store.CustomRoadChalan;
import com.cassinisys.is.model.store.CustomRoadChalanItem;
import com.cassinisys.is.service.store.CustomRoadChalanService;
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
 * Created by Rajabrahmachary on 14-09-2018.
 */
@RestController
@RequestMapping("is/stores/{storeId}/roadChalans")
@Api(name = "CustomRoadChalan", description = "CustomRoadChalan endpoint", group = "IS")
public class CustomRoadChalanController extends BaseController {

   /*  adding dependencies */

    @Autowired
    private CustomRoadChalanService customRoadChalanService;

    @Autowired
    private PageRequestConverter pageRequestConverter;


   /*  methods for CustomRoadChalan */

    @RequestMapping(method = RequestMethod.POST)
    public CustomRoadChalan create(@RequestBody CustomRoadChalan roadChalan) {
        return customRoadChalanService.create(roadChalan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CustomRoadChalan update(@PathVariable("id") Integer id, @RequestBody CustomRoadChalan roadChalan) {
        roadChalan.setId(id);
        return customRoadChalanService.update(roadChalan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        customRoadChalanService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomRoadChalan get(@PathVariable("id") Integer id) {
        return customRoadChalanService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<CustomRoadChalan> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customRoadChalanService.getAll(pageable);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<CustomRoadChalan> roadChallanFreeTextSearch(PageRequest pageRequest, CustomRoadChallanCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<CustomRoadChalan> roadChalans = customRoadChalanService.roadChallanFreeTextSearch(pageable, criteria);
        return roadChalans;
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<CustomRoadChalan> getPagedRoadChallans(@PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customRoadChalanService.getPagedRoadChallans(storeId, pageable);
    }


     /*  methods for CustomRoadChalanItem */

    @RequestMapping(value = "/{id}/roadChalanItem", method = RequestMethod.POST)
    public List<CustomRoadChalanItem> createRoadChalanItems(@PathVariable("id") Integer id, @RequestBody List<CustomRoadChalanItem> roadChalanItems) {
        return customRoadChalanService.createRoadChalanItems(roadChalanItems);
    }

    @RequestMapping(value = "/{id}/roadChalanItem/{roadChalanItemId}", method = RequestMethod.PUT)
    public CustomRoadChalanItem updateRoadChalanItem(
            @PathVariable("id") Integer id,
            @PathVariable("roadChalanItemId") Integer roadChalanItemId,
            @RequestBody CustomRoadChalanItem roadChalanItem) {
        roadChalanItem.setId(roadChalanItemId);
        return customRoadChalanService.updateRoadChalanItem(roadChalanItem);
    }

    @RequestMapping(value = "/{id}/roadChalanItem/{roadChalanItemId}", method = RequestMethod.DELETE)
    public void deleteRoadChalanItem(@PathVariable("id") Integer id, @PathVariable("roadChalanItemId") Integer roadChalanItemId) {
        customRoadChalanService.deleteRoadChalanItem(roadChalanItemId);
    }

    @RequestMapping(value = "/{id}/roadChalanItem/{roadChalanItemId}", method = RequestMethod.GET)
    public CustomRoadChalanItem getRoadChalanItem(@PathVariable("id") Integer id,
                                                  @PathVariable("roadChalanItemId") Integer roadChalanItemId) {
        return customRoadChalanService.getRoadChalanItem(roadChalanItemId);
    }

    @RequestMapping(value = "{id}/roadChalanitems", method = RequestMethod.GET)
    public List<CustomRoadChalanItem> getRoadChalanItems(@PathVariable("id") Integer id) {
        return customRoadChalanService.getAllRoadChalanItems(id);
    }

    @RequestMapping(value = "{id}/roadChalanitems/pageable", method = RequestMethod.GET)
    public Page<CustomRoadChalanItem> getPagedRoadChalanItems(@PathVariable("id") Integer id, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customRoadChalanService.getPagedRoadChalanItems(id, pageable);
    }
}
