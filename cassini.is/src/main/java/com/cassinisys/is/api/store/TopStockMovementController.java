package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.ReportCriteria;
import com.cassinisys.is.filtering.StockMovementCriteria;
import com.cassinisys.is.model.procm.dto.MaterialTypeStoresReportDTO;
import com.cassinisys.is.model.store.ISTopStockMovement;
import com.cassinisys.is.model.store.StockReceiveItemsDTO;
import com.cassinisys.is.service.store.TopStockMovementService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@RestController
@RequestMapping("is/stores/stockMovements")
@Api(name = "ISTopStockMovement", description = "ISTopStockMovement endpoint", group = "IS")
public class TopStockMovementController extends BaseController {

    @Autowired
    private TopStockMovementService topStockMovementService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ISTopStockMovement create(@RequestBody ISTopStockMovement item) {
        return topStockMovementService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISTopStockMovement update(@PathVariable("id") Integer id, @RequestBody ISTopStockMovement item) {
        item.setId(id);
        return topStockMovementService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        topStockMovementService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISTopStockMovement get(@PathVariable("id") Integer id) {
        return topStockMovementService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISTopStockMovement> get() {
        return topStockMovementService.getAll();
    }

    @RequestMapping(value = "store/{storeId}/byItem/{itemId}", method = RequestMethod.GET)
    public List<ISTopStockMovement> getStockMovementByItemAndStore(@PathVariable("storeId") Integer storeId, @PathVariable("itemId") Integer itemId) {
        return topStockMovementService.getStockMovementByItemAndStore(itemId, storeId);
    }

    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.GET)
    public List<ISTopStockMovement> getStockMovementByItem(@PathVariable("itemId") Integer itemId) {
        return topStockMovementService.getStockMovementByItem(itemId);
    }

    @RequestMapping(value = "/boqItemNumber /{itemNumber}/project/{projectId}", method = RequestMethod.GET)
    public List<ISTopStockMovement> getStockMovementByProjectItemNum(@PathVariable("itemNumber") String itemNumber, @PathVariable("projectId") Integer projectId) {
        return topStockMovementService.getStockMovementByProjectItemNum(itemNumber, projectId);
    }

    @RequestMapping(value = "/store/{storeId}/filters/pageable", method = RequestMethod.GET)
    public Page<ISTopStockMovement> getPageableStockMovementByFilter(@PathVariable("storeId") Integer storeId, PageRequest pageRequest, StockMovementCriteria stockMovementCriteria) {
        stockMovementCriteria.setStoreId(storeId);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockMovementService.getPageableStockMovementByFilter(stockMovementCriteria, pageable);
    }

    @RequestMapping(value = "/store/{storeId}/filters", method = RequestMethod.GET)
    public List<ISTopStockMovement> getStockMovementByFilter(@PathVariable("storeId") Integer storeId, StockMovementCriteria stockMovementCriteria) {
        stockMovementCriteria.setStoreId(storeId);
        return topStockMovementService.getStockMovementByFilter(stockMovementCriteria);
    }

    @RequestMapping(value = "/itemNumber/{itemNumber}/bomItem/{bomItem}", method = RequestMethod.GET)
    public List<ISTopStockMovement> getStockMovementByBomItemAndItemNum(@PathVariable("itemNumber") String itemNumber, @PathVariable("bomItem") Integer bomItem) {
        return topStockMovementService.getStockMovementByBomItemAndItemNum(itemNumber, bomItem);
    }

    @RequestMapping(value = "/store/{storeId}", method = RequestMethod.GET)
    public Page<ISTopStockMovement> getStockMovementByStore(@PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return topStockMovementService.getStockMovementByStore(storeId, pageable);
    }

    @RequestMapping(value = "/receive/{receiveId}", method = RequestMethod.GET)
    public List<ISTopStockMovement> getStockMovementsByReceiveId(@PathVariable("receiveId") Integer receiveId) {
        return topStockMovementService.getStockMovementsByReceiveId(receiveId);
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public MaterialTypeStoresReportDTO getTaskReportReports(ReportCriteria criteria) {
        return topStockMovementService.getReportByDates(criteria.getFromDate(), criteria.getToDate());
    }

    @RequestMapping(value = "/{fileType}/report", method = RequestMethod.GET,
            produces = "text/html")
    public String getTaskReport(@PathVariable("fileType") String fileType, HttpServletResponse response, ReportCriteria criteria) {
        return topStockMovementService.exportTaskReport(fileType, response, criteria);
    }

    @RequestMapping(value = "/balancereport", method = RequestMethod.GET)
    public List<StockReceiveItemsDTO> balancereport(StockMovementCriteria stockMovementCriteria) {
        return topStockMovementService.getOpeningAndClosingBalReport(stockMovementCriteria);
    }

    @RequestMapping(value = "/export/type/{fileType}", method = RequestMethod.POST, produces = "text/html")
    public String exportStockMovements(@RequestBody StockMovementCriteria criteria, @PathVariable("fileType") String fileType, HttpServletResponse response) {
        return topStockMovementService.exportStockMovements(criteria, fileType, response);
    }

    @RequestMapping(value = "/file/{fileName}/download", method = RequestMethod.GET)
    public void downloadExportFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        topStockMovementService.downloadExportFile(fileName, response);

    }
}
