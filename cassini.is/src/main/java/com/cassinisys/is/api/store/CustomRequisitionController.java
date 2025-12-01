package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.CustomRequisitionCriteria;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.model.store.CustomRequisition;
import com.cassinisys.is.model.store.CustomRequisitionItem;
import com.cassinisys.is.service.store.CustomRequisitionService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@RestController
@RequestMapping("is/stores/{storeId}/requisitions")
@Api(name = "CustomRequisition", description = "CustomRequisition endpoint", group = "IS")
public class CustomRequisitionController extends BaseController {

    @Autowired
    private CustomRequisitionService customRequisitionService;

    @Autowired
    private PageRequestConverter pageRequestConverter;


   /*  methods for CustomRequisitionChalan */

    @RequestMapping(method = RequestMethod.POST)
    public CustomRequisition create(@RequestBody CustomRequisition customRequisition) {
        return customRequisitionService.create(customRequisition);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CustomRequisition update(@PathVariable("id") Integer id, @RequestBody CustomRequisition customRequisition) {
        customRequisition.setId(id);
        return customRequisitionService.update(customRequisition);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        customRequisitionService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomRequisition get(@PathVariable("id") Integer id) {
        return customRequisitionService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomRequisition> getAll(@PathVariable("storeId") Integer storeId) {
        return customRequisitionService.getAllStoreRequisitions(storeId);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<CustomRequisition> requisitionFreeTextSearch(PageRequest pageRequest, CustomRequisitionCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<CustomRequisition> requisitions = customRequisitionService.requisitionFreeTextSearch(pageable, criteria);
        return requisitions;
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<CustomRequisition> getPageableRequisitions(@PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customRequisitionService.getPageableRequisitions(storeId, pageable);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<CustomRequisition> getAllPageableRequisitions(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customRequisitionService.getAllPageableRequisitions(pageable);
    }

    @RequestMapping(value = "/{projectId}/pageable", method = RequestMethod.GET)
    public Page<CustomRequisition> findPageableRequisitionsByProject(@PathVariable("projectId") Integer projectId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customRequisitionService.findPageableRequisitionsByProject(projectId, pageable);
    }

       /*  methods for to get requisition attributes */

    @RequestMapping(value = "/requiredRequisitionAttributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getRequiredRequisitionAttributes(@PathVariable("objectType") String objectType) {
        return customRequisitionService.getRequiredRequisitionAttributes(objectType);
    }

    @RequestMapping(value = "/objectAttributes", method = RequestMethod.POST)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByRequisitionAndAttributeId(@RequestBody List<Integer[]> ids) {
        Integer[] requisitionIds = ids.get(0);
        Integer[] attIds = ids.get(1);
        return customRequisitionService.getObjectAttributesByRequisitionIdsAndAttributeIds(requisitionIds, attIds);
    }

     /*  methods for CustomRequisitionItem */

    @RequestMapping(value = "/{id}/customRequisitionItem", method = RequestMethod.POST)
    public List<CustomRequisitionItem> createCustomRequisitionItem(@PathVariable("id") Integer id, @RequestBody List<CustomRequisitionItem> customRequisitionItems) {
        return customRequisitionService.createCustomRequisitionItem(customRequisitionItems);
    }

    @RequestMapping(value = "/{id}/customRequisitionItem/{customRequisitionItemId}", method = RequestMethod.PUT)
    public CustomRequisitionItem updateCustomRequisitionItem(
            @PathVariable("id") Integer id,
            @PathVariable("customRequisitionItemId") Integer customRequisitionItemId,
            @RequestBody CustomRequisitionItem customRequisitionItem) {
        customRequisitionItem.setId(customRequisitionItemId);
        return customRequisitionService.updateCustomRequisitionItem(customRequisitionItem);
    }

    @RequestMapping(value = "/{id}/customRequisitionItem/{customRequisitionItemId}", method = RequestMethod.DELETE)
    public void deleteCustomRequisitionItem(@PathVariable("id") Integer id, @PathVariable("customRequisitionItemId") Integer customRequisitionItemId) {
        customRequisitionService.deleteCustomRequisitionItem(customRequisitionItemId);
    }

    @RequestMapping(value = "/{id}/customRequisitionItem/{customRequisitionItemId}", method = RequestMethod.GET)
    public CustomRequisitionItem getCustomRequisitionItem(@PathVariable("id") Integer id,
                                                          @PathVariable("customRequisitionItemId") Integer customRequisitionItemId) {
        return customRequisitionService.getCustomRequisitionItem(customRequisitionItemId);
    }

    @RequestMapping(value = "/{requisitionId}/customRequisitionItems", method = RequestMethod.GET)
    public List<CustomRequisitionItem> getCustomRequisitionItems(@PathVariable("requisitionId") Integer requisitionId) {
        return customRequisitionService.getAllRequisitionItems(requisitionId);
    }

    @RequestMapping(value = "/{requisitionId}/customRequisitionItems/pageable", method = RequestMethod.GET)
    public Page<CustomRequisitionItem> getPagedCustomRequisitionItems(@PathVariable("requisitionId") Integer requisitionId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customRequisitionService.getPagedCustomRequisitionItems(requisitionId, pageable);
    }

    @RequestMapping(value = "/{requisitionId}/printRequisitionChallan", method = RequestMethod.GET, produces = "text/html")
    public String printRequisitionChallan(@PathVariable("requisitionId") Integer requisitionId,
                                          @RequestParam("customer") String customer, HttpServletRequest request, HttpServletResponse response) {
        String fileName = customRequisitionService.printRequisitionChallan(requisitionId, request, response);
        return fileName;
    }

    @RequestMapping(value = "/file/{fileName}/download", method = RequestMethod.GET)
    public void downloadExportFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        customRequisitionService.downloadExportFile(fileName, response);

    }

    @RequestMapping(value = "/{requisitionId}/project/{projectId}", method = RequestMethod.GET)
    public List<ISMaterialItem> findNonRequisitionItems(@PathVariable("requisitionId") Integer requisitionId, @PathVariable("projectId") Integer projectId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customRequisitionService.findNonRequisitionItems(requisitionId, projectId, pageable);
    }

}
