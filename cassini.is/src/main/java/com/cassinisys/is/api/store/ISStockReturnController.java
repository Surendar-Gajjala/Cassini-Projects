package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.StockReturnCriteria;
import com.cassinisys.is.model.store.ISStockReturn;
import com.cassinisys.is.model.store.ISStockReturnItem;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.store.ISStockReturnService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by swapna on 05/12/18.
 */
@RestController
@RequestMapping("is/stores/{storeId}/stockReturn")
public class ISStockReturnController extends BaseController {

    @Autowired
    private ISStockReturnService stockReturnService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ISStockReturn createStockReturn(@PathVariable("storeId") Integer storeId, @RequestBody ISStockReturn stockReturn) {
        return stockReturnService.createStockReturn(storeId, stockReturn);
    }

    @RequestMapping(value = "/{stockReturnId}", method = RequestMethod.GET)
    public ISStockReturn getStockReturn(@PathVariable("stockReturnId") Integer stockReturnId) {
        return stockReturnService.getStockReturn(stockReturnId);
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISStockReturn> getStoreStockReturns(@PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return stockReturnService.getStoreStockReturns(storeId, pageable);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<ISStockReturn> getStockReturns(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return stockReturnService.getStockReturns(pageable);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ISStockReturn updateStockReturn(@RequestBody ISStockReturn stockReturn) {
        return stockReturnService.updateStockReturn(stockReturn);
    }

    @RequestMapping(value = "/{stockReturnId}", method = RequestMethod.DELETE)
    public void deleteStockReturn(@PathVariable("stockReturnId") Integer stockReturnId) {
        stockReturnService.deleteStockReturn(stockReturnId);
    }

    @RequestMapping(value = "/{stockReturnId}/stockReturnItems", method = RequestMethod.POST)
    public List<ISStockReturnItem> creteStockReturnItems(@PathVariable("storeId") Integer storeId, @PathVariable("stockReturnId") Integer stockReturnId, @RequestBody List<ISStockReturnItem> stockReturnItemList) {
        return stockReturnService.creteStockReturnItems(storeId, stockReturnId, stockReturnItemList);
    }

    @RequestMapping(value = "/{stockReturnId}/stockReturnItems/pageable", method = RequestMethod.GET)
    public Page<ItemDTO> getStockReturnItems(@PathVariable("stockReturnId") Integer stockReturnId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return stockReturnService.getStockReturnItems(stockReturnId, pageable);
    }

    @RequestMapping(value = "/{stockReturnId}/printStockReturnChallan", method = RequestMethod.GET, produces = "text/html")
    public String printStockReturnChallan(@PathVariable("stockReturnId") Integer stockReturnId, HttpServletRequest request, HttpServletResponse response) {
        String fileName = stockReturnService.printStockReturnChallan(stockReturnId, request, response);
        return fileName;
    }

    @RequestMapping(value = "/{stockReturnId}/file/{fileName}/download", method = RequestMethod.GET)
    public void downloadExportFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        stockReturnService.downloadExportFile(fileName, response);

    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<ISStockReturn> freeTextSearch(PageRequest pageRequest, StockReturnCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISStockReturn> stockReturns = stockReturnService.freeTextSearch(pageable, criteria);
        return stockReturns;
    }
}
