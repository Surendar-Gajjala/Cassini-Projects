package com.cassinisys.erp.api.production;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.api.filtering.ProductInventoryHistoryPredicateBuilder;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.ProductInventoryHistoryCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.crm.ERPCustomerOrderShipment;
import com.cassinisys.erp.model.production.ERPProductInventory;
import com.cassinisys.erp.model.production.ERPProductInventoryHistory;
import com.cassinisys.erp.model.production.QERPProductInventoryHistory;
import com.cassinisys.erp.service.production.ProductInventoryService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 9/7/15.
 */
@RestController
@RequestMapping("production/productinventory")
public class ProductInventoryController extends BaseController {

    @Autowired
    private ProductInventoryService productInventoryService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ProductInventoryHistoryPredicateBuilder predicateBuilder;

    @RequestMapping
    public List<ERPProductInventory> getProductsInventory(@RequestParam(value = "products") String products) {
        String[] arr = products.trim().split(",");

        List<Integer> productIds = new ArrayList<>();
        for (String s : arr) {
            try {
                productIds.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException e) {
            }
        }

        return productInventoryService.getProductsInventory(productIds);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ERPProductInventory createProductInventory(@RequestBody @Valid ERPProductInventory inventory) {
        return productInventoryService.create(inventory);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ERPProductInventory updateProductInventory(@RequestBody @Valid ERPProductInventory inventory) {
        return productInventoryService.update(inventory);
    }

    @RequestMapping(value = "/productRestock/{productId}", method = RequestMethod.PUT)
    public ERPProductInventory updateProductRestock(@PathVariable("productId") Integer productId,
                                                    @RequestBody ERPProductInventory productRestock) {
        return productInventoryService.updateProductRestock(productRestock);
    }

    @RequestMapping(value = "/stockin", method = RequestMethod.GET)
    public ERPProductInventoryHistory stockIn(@RequestParam("product") Integer productId,
                                              @RequestParam("quantity") Integer quantity) {
        return productInventoryService.stockIn(productId, quantity);
    }

    @RequestMapping(value = "/stockout", method = RequestMethod.GET)
    public ERPProductInventoryHistory stockOut(@RequestParam("product") Integer productId,
                                               @RequestParam("quantity") Integer quantity) {
        return productInventoryService.stockOut(productId, quantity, null);
    }

    @RequestMapping(value = "/{productId}/history/stocktype", method = RequestMethod.GET)
    public Page<ERPProductInventoryHistory> getProductInventoryHistoryByStockType(ERPPageRequest pageRequest, ProductInventoryHistoryCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);

        Predicate predicate = predicateBuilder.build(criteria,
                QERPProductInventoryHistory.eRPProductInventoryHistory);
        return productInventoryService.getProductInventoryHistoryType(pageable, predicate);
    }

    @RequestMapping(value = "/{productId}/history", method = RequestMethod.GET)
    public Page<ERPProductInventoryHistory> getProductInventoryHistory(@PathVariable("productId") int productId, ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return productInventoryService.getProductInventoryHistory(productId, pageable);
    }

    @RequestMapping(value = "/lowinventory", method = RequestMethod.GET)
    public Page<ERPProductInventory> getLowInventoryItems(ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return productInventoryService.getLowInventoryItems(pageable);
    }
}
