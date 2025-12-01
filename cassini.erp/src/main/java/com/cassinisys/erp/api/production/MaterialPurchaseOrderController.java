package com.cassinisys.erp.api.production;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrder;
import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrderDetails;
import com.cassinisys.erp.service.production.MaterialPurchaseOrderService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("production/materialpurchaseorders")
@Api(name = "MaterialPurchaseOrders", description = "MaterialPurchaseOrders endpoint", group = "PRODUCTION")
public class MaterialPurchaseOrderController extends BaseController {

    private MaterialPurchaseOrderService mPurchaseOrderService;

    private PageRequestConverter pageRequestConverter;

    @Autowired
    public MaterialPurchaseOrderController(
            MaterialPurchaseOrderService mPurchaseOrderService,
            PageRequestConverter pageRequestConverter) {
        this.pageRequestConverter = pageRequestConverter;
        this.mPurchaseOrderService = mPurchaseOrderService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ERPMaterialPurchaseOrder createMaterialPurchaseOrder(
            @RequestBody @Valid ERPMaterialPurchaseOrder mPurchaseOrder,
            HttpServletRequest request, HttpServletResponse response) {

        return mPurchaseOrderService.create(mPurchaseOrder);

    }

    @RequestMapping(value = "/multiple", method = RequestMethod.POST)
    public List<ERPMaterialPurchaseOrder> createMaterialPurchaseOrders(
            @RequestBody List<ERPMaterialPurchaseOrder> mPurchaseOrders,
            HttpServletRequest request, HttpServletResponse response) {

        return mPurchaseOrderService.createPOs(mPurchaseOrders);

    }

    @RequestMapping(value = "/{orderId}/approvedby/{empId}", method = RequestMethod.GET)
    public Boolean approveOrder(@PathVariable("orderId") Integer orderId,
                                @PathVariable("empId") Integer empId) {

        return mPurchaseOrderService.approveOrder(empId, orderId);

    }

    @RequestMapping(value = "/{orderId}/deliveredby/{empId}", method = RequestMethod.GET)
    public Boolean updateDeliverOrder(@PathVariable("orderId") Integer orderId,
                                      @PathVariable("empId") Integer empId) {

        return mPurchaseOrderService.deliverOrder(empId, orderId);

    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public ERPMaterialPurchaseOrder getMaterialPurchaseOrderById(
            @PathVariable("orderId") Integer orderId) {

        return mPurchaseOrderService.get(orderId);

    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.PUT)
    public ERPMaterialPurchaseOrder update(
            @PathVariable("orderId") Integer orderId,
            @RequestBody ERPMaterialPurchaseOrder mPurchaseOrder) {

        mPurchaseOrder.setId(orderId);
        return mPurchaseOrderService.update(mPurchaseOrder);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ERPMaterialPurchaseOrder> getAllMaterialPurchaseOrders() {

        return mPurchaseOrderService.getAll();

    }

    @RequestMapping(value = "/pagable", method = RequestMethod.GET)
    public Page<ERPMaterialPurchaseOrder> getAllMaterialPurchaseOrders(
            ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return mPurchaseOrderService.findAll(pageable);

    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("orderId") Integer orderId) {

        mPurchaseOrderService.delete(orderId);
    }

    @RequestMapping(value = "/{orderId}/details", method = RequestMethod.GET)
    public List<ERPMaterialPurchaseOrderDetails> getAllOrderDetailsForOrder(
            @PathVariable("orderId") Integer orderId) {

        return mPurchaseOrderService
                .getAllMaterialPurchaseOrderDetailsByOrderId(orderId);
    }

    @RequestMapping(value = "byMaterial/{materialId}/{poId}/details", method = RequestMethod.GET)
    public ERPMaterialPurchaseOrderDetails getMaterialPODetailsByMaterialAndMaterialPurchaseOrder(@PathVariable("materialId") Integer materialId,
                                                                                                  @PathVariable("poId") Integer poId) {
        return mPurchaseOrderService.getMaterialPODetailsByMaterialAndMaterialPurchaseOrder(materialId, poId);
    }

    @RequestMapping(value = "/{materialId}/{issued}/details", method = RequestMethod.GET)
    public List<ERPMaterialPurchaseOrder> getByMaterialIdAndIssuedFalse(@PathVariable("materialId") Integer materialId,
                                                                               @PathVariable("issued") Boolean issued) {
        return mPurchaseOrderService.getByMaterialIdAndIssuedFalse(materialId, issued);
    }

}
