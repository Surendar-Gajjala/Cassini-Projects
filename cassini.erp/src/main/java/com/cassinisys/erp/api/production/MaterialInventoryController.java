package com.cassinisys.erp.api.production;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.MaterialDailyIssueReportCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.production.ERPMaterial;
import com.cassinisys.erp.model.production.ERPMaterialDailyIssueReport;
import com.cassinisys.erp.model.production.ERPMaterialInventory;
import com.cassinisys.erp.model.production.ERPMaterialInventoryHistory;
import com.cassinisys.erp.service.production.MaterialDailyIssueReportService;
import com.cassinisys.erp.service.production.MaterialInventoryService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("production/materialinventory")
@Api(name = "MaterialInventory", description = "MaterialInventory endpoint", group = "PRODUCTION")
public class MaterialInventoryController extends BaseController {

    @Autowired
    MaterialInventoryService materialInvService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private MaterialDailyIssueReportService dailyIssueReportService;


    @RequestMapping
    public List<ERPMaterialInventory> getMaterialsInventory(@RequestParam(value = "materials") String materials) {
        String[] arr = materials.trim().split(",");

        List<Integer> materialIds = new ArrayList<>();
        for (String s : arr) {
            try {
                materialIds.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException e) {
            }
        }

        return materialInvService.getMaterialsInventory(materialIds);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ERPMaterialInventory createMaterialInv(@RequestBody @Valid ERPMaterialInventory materialInv) {

        return materialInvService.createMaterialInventory(materialInv);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ERPMaterialInventory getMaterialInvById(@PathVariable("id") Integer id) {

        return materialInvService.getMaterialInventoryById(id);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ERPMaterialInventory update(@PathVariable("id") Integer id,
                                       @RequestBody ERPMaterialInventory materialInv) {
        materialInv.setRowId(id);
        return materialInvService.updateMaterialInventory(materialInv);
    }

	/*@RequestMapping(method = RequestMethod.GET)
    public List<ERPMaterialInventory> getAllMaterialInventories() {

		return materialInvService.getAllMaterialInventorys();

	}*/

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {

        materialInvService.deleteMaterialInventory(id);
    }

    @RequestMapping(value = "/stockin", method = RequestMethod.POST)
    public ERPMaterialInventoryHistory updateStockIn(@RequestParam("material") Integer materialId,
                                                     @RequestParam("quantity") Integer quantity, @RequestBody ERPMaterial material) {
        return materialInvService.stockIn(materialId, quantity, material);
    }

    @RequestMapping(value = "/stockout", method = RequestMethod.GET)
    public ERPMaterialInventoryHistory stockOut(@RequestParam("material") Integer materialId,
                                                @RequestParam("quantity") Integer quantity) {
        return materialInvService.stockOut(materialId, quantity);
    }

    @RequestMapping(value = "/{materialId}/history", method = RequestMethod.GET)
    public Page<ERPMaterialInventoryHistory> getMaterialInventoryHistory(@PathVariable("materialId") Integer materialId,
                                                                         ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return materialInvService.getMaterialInventoryHistory(materialId, pageable);
    }

    @RequestMapping(value = "/issueReport", method = RequestMethod.GET)
    public Page<ERPMaterialDailyIssueReport> getIssueReports(MaterialDailyIssueReportCriteria criteria, ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return dailyIssueReportService.getAll(pageable, criteria);
    }

    @RequestMapping(value = "/issueReport", method = RequestMethod.PUT)
    public ERPMaterialDailyIssueReport updateIssueReports(@RequestBody ERPMaterialDailyIssueReport dailyIssueReport) {
        return dailyIssueReportService.update(dailyIssueReport);
    }

}