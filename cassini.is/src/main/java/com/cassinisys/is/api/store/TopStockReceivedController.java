package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.ISStockReceiveCriteria;
import com.cassinisys.is.filtering.StockMovementCriteria;
import com.cassinisys.is.model.procm.ISReceiveTypeItemAttribute;
import com.cassinisys.is.model.store.ISStockReceive;
import com.cassinisys.is.model.store.ISTopStockMovement;
import com.cassinisys.is.model.store.StockReceiveItemsDTO;
import com.cassinisys.is.service.store.TopStockReceivedService;
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
@RequestMapping("is/stores/{storeId}/stockReceive")
@Api(name = "ISStockReceiveItem", description = "ISStockReceiveItem endpoint", group = "IS")
public class TopStockReceivedController extends BaseController {

    @Autowired
    private TopStockReceivedService topStockReceivedService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ISStockReceive create(@RequestBody ISStockReceive stockReceive) {
        return topStockReceivedService.create(stockReceive);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISStockReceive update(@PathVariable("id") Integer id, @RequestBody ISStockReceive item) {
        item.setId(id);
        return topStockReceivedService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        topStockReceivedService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISStockReceive get(@PathVariable("id") Integer id) {
        return topStockReceivedService.get(id);
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public List<ISTopStockMovement> getStockReceiveItems(@PathVariable("id") Integer id) {
        return topStockReceivedService.getStockReceiveItems(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISStockReceive> getStockReceivedByStoreId(@PathVariable("storeId") Integer storeId) {
        return topStockReceivedService.getStockReceivedByStoreId(storeId);
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISStockReceive> getPagedStockReceives(@PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockReceivedService.getPagedStockReceives(storeId, pageable);
    }

    @RequestMapping(value = "/itemNumber/{itemNumber}/boq/{boqId}", method = RequestMethod.GET)
    public Double getStockReceivedQtyByItemNumber(@PathVariable("itemNumber") String itemNumber, @PathVariable("boqId") Integer boqId) {
        return topStockReceivedService.getStockReceivedByItemNumber(itemNumber, boqId);
    }

    @RequestMapping(value = "/{id}/items/pageable", method = RequestMethod.GET)
    public Page<ISTopStockMovement> getPagedStockReceiveItems(@PathVariable("id") Integer id, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockReceivedService.getPagedStockReceiveItems(id, pageable);
    }

    @RequestMapping(value = "/freeSearch", method = RequestMethod.GET)
    public Page<ISStockReceive> freeTextSearch(PageRequest pageRequest, ISStockReceiveCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISStockReceive> projects = topStockReceivedService.freeTextSearch(pageable, criteria);
        return projects;
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public List<ObjectAttribute> saveMaterialItemAttributes(@PathVariable("id") Integer id,
                                                            @RequestBody List<ISReceiveTypeItemAttribute> attributes) {
        return topStockReceivedService.saveReceiveTypeAttributes(attributes);
    }

    @RequestMapping(value = "/type/{receiveTypeId}/pageable", method = RequestMethod.GET)
    public Page<ISStockReceive> getStockReceivesByType(@PathVariable("storeId") Integer storeId, @PathVariable("receiveTypeId") Integer receiveTypeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockReceivedService.getStockReceivesByType(receiveTypeId, storeId, pageable);
    }

    @RequestMapping(value = "/export/type/{fileType}", method = RequestMethod.POST, produces = "text/html")
    public String exportReceiveItemsReport(@PathVariable("storeId") Integer storeId, @RequestBody StockMovementCriteria criteria, @PathVariable("fileType") String fileType, HttpServletResponse response) {
        return topStockReceivedService.exportStockReceiveItems(storeId, criteria, fileType, response);
    }

    @RequestMapping(value = "/file/{fileName}/download", method = RequestMethod.GET)
    public void downloadExportFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        topStockReceivedService.downloadExportFile(fileName, response);

    }

    @RequestMapping(value = "/by/attributes/pageable", method = RequestMethod.POST)
    public Page<ISStockReceive> getStockReceivesByAttributes(@PathVariable("storeId") Integer storeId, @RequestBody ISStockReceiveCriteria criteria, PageRequest pageRequest) {
        criteria.setAttributeSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockReceivedService.getStockReceivesByAttributes(storeId, pageable, criteria);
    }

    @RequestMapping(value = "/by/attributes", method = RequestMethod.POST)
    public List<StockReceiveItemsDTO> getStockReceivedItemsByAttributes(@PathVariable("storeId") Integer storeId, @RequestBody StockMovementCriteria criteria) {
        return topStockReceivedService.getStockReceivedItemsByAttributes(storeId, criteria);
    }

    @RequestMapping(value = "/minimumdate", method = RequestMethod.GET)
    public Date getMinimumDate(@PathVariable("storeId") Integer storeId) {
        return topStockReceivedService.getMinimumDate(storeId);
    }

    @RequestMapping(value = "/{receiveId}/receiveType/{receiveTypeId}/attributes", method = RequestMethod.GET)
    public AttributesDetailsDTO getReceiveTypeAttributes(@PathVariable("receiveId") Integer receiveId, @PathVariable("receiveTypeId") Integer receiveTypeId) {
        return topStockReceivedService.getReceiveTypeAttributes(receiveId, receiveTypeId);
    }

}
