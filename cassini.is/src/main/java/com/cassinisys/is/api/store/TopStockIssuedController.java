package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.ISStockIssueCriteria;
import com.cassinisys.is.filtering.ReportCriteria;
import com.cassinisys.is.filtering.StockMovementCriteria;
import com.cassinisys.is.model.procm.ISIssueTypeItemAttribute;
import com.cassinisys.is.model.store.ISStockIssue;
import com.cassinisys.is.model.store.ISTopStockMovement;
import com.cassinisys.is.model.store.StockReceiveItemsDTO;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.store.TopStockIssuedService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.AttributesDetailsDTO;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@RestController
@RequestMapping("is/stores/{storeId}/stockIssues")
@Api(name = "ISTopStockIssue", description = "ISTopStockIssue endpoint", group = "IS")
public class TopStockIssuedController extends BaseController {

    @Autowired
    private TopStockIssuedService topStockIssuedService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ISStockIssue create(@RequestBody ISStockIssue stockIssue) {
        return topStockIssuedService.create(stockIssue);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISStockIssue update(@PathVariable("id") Integer id, @RequestBody ISStockIssue item) {
        item.setId(id);
        return topStockIssuedService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        topStockIssuedService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISStockIssue get(@PathVariable("id") Integer id) {
        return topStockIssuedService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISStockIssue> getStockIssuedByStore(@PathVariable("storeId") Integer storeId) {
        return topStockIssuedService.getStockIssuedByStore(storeId);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<ISStockIssue> stockIssuedFreeTextSearch(PageRequest pageRequest, ISStockIssueCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISStockIssue> stockIssues = topStockIssuedService.stockIssuedFreeTextSearch(pageable, criteria);
        return stockIssues;
    }

    @RequestMapping(value = "pageable", method = RequestMethod.GET)
    public Page<ISStockIssue> getPagedStoreStockIssues(@PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockIssuedService.getPagedStoreStockIssues(storeId, pageable);
    }

    @RequestMapping(value = "/project/{projectId}/itemNumber/{itemNumber}", method = RequestMethod.GET)
    public Double getStockIssuedQtyByItemNumber(@PathVariable("projectId") Integer projectId, @PathVariable("itemNumber") String itemNumber) {
        return topStockIssuedService.getStockIssuedQtyByItemNumber(projectId, itemNumber);
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public List<ISTopStockMovement> getStockReceiveItems(@PathVariable("id") Integer id) {
        return topStockIssuedService.getStockIssueItems(id);
    }

    @RequestMapping(value = "/{id}/items/pageable", method = RequestMethod.GET)
    public Page<ISTopStockMovement> getPagedStockReceiveItems(@PathVariable("id") Integer id, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockIssuedService.getPagedStockIssueItems(id, pageable);
    }

    @RequestMapping(value = "/project/{projectId}/task/{taskId}", method = RequestMethod.GET)
    public List<ItemDTO> getProjectResourcesInventory(@PathVariable("projectId") Integer projectId, @PathVariable("taskId") Integer taskId, @PathVariable("storeId") Integer storeId) {
        return topStockIssuedService.getProjectResourcesInventory(storeId, projectId, taskId);
    }

    //to get project Items or masterdata items based on project id
    @RequestMapping(value = "/project/{projectId}/pageable", method = RequestMethod.GET)
    public Page<ItemDTO> getProjectItems(@PathVariable("storeId") Integer storeId, @PathVariable("projectId") Integer projectId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockIssuedService.getProjectItems(storeId, projectId, pageable);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public List<ObjectAttribute> saveMaterialItemAttributes(@PathVariable("id") Integer id,
                                                            @RequestBody List<ISIssueTypeItemAttribute> attributes) {
        return topStockIssuedService.saveIssueTypeAttributes(attributes);
    }

    @RequestMapping(value = "/type/{typeId}/pageable", method = RequestMethod.GET)
    public Page<ISStockIssue> getStockIssuesByType(@PathVariable("storeId") Integer storeId, @PathVariable("typeId") Integer typeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockIssuedService.getStockIssuesByType(storeId, typeId, pageable);
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public List<StockReceiveItemsDTO> getStoreReceiptItemsReport(@PathVariable("storeId") Integer storeId, ReportCriteria criteria) {
        return topStockIssuedService.getStockIssuedItemsReport(storeId, criteria.getFromDate(), criteria.getToDate());
    }

    @RequestMapping(value = "/export/type/{fileType}", method = RequestMethod.POST, produces = "text/html")
    public String exportIssuedItemsReport(@PathVariable("storeId") Integer storeId, @RequestBody StockMovementCriteria criteria, @PathVariable("fileType") String fileType, HttpServletResponse response) {
        return topStockIssuedService.exportIssuedItemsReport(storeId, criteria, fileType, response);
    }

    @RequestMapping(value = "/file/{fileName}/download", method = RequestMethod.GET)
    public void downloadExportFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        topStockIssuedService.downloadExportFile(fileName, response);

    }

    @RequestMapping(value = "/by/attributes", method = RequestMethod.POST)
    public List<StockReceiveItemsDTO> getStockIssuedItemsByAttributes(@PathVariable("storeId") Integer storeId, @RequestBody StockMovementCriteria criteria) {
        return topStockIssuedService.getStockIssuedItemsByAttributes(storeId, criteria);
    }

    @RequestMapping(value = "/by/attributes/pageable", method = RequestMethod.POST)
    public Page<ISStockIssue> getStockReceivesByAttributes(@PathVariable("storeId") Integer storeId, @RequestBody ISStockIssueCriteria criteria, PageRequest pageRequest) {
        criteria.setAttributeSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockIssuedService.getStockReceivesByAttributes(storeId, pageable, criteria);
    }

    @RequestMapping(value = "/minimumdate", method = RequestMethod.GET)
    public Date getMinimumDate(@PathVariable("storeId") Integer storeId) {
        return topStockIssuedService.getMinimumDate(storeId);
    }

    @RequestMapping(value = "/{issueId}/issueType/{issueTypeId}/attributes", method = RequestMethod.GET)
    public AttributesDetailsDTO getIssueTypeAttributes(@PathVariable("issueId") Integer issueId, @PathVariable("issueTypeId") Integer issueTypeId) {
        return topStockIssuedService.getIssueTypeAttributes(issueId, issueTypeId);
    }

}
