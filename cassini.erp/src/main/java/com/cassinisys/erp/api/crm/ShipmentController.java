package com.cassinisys.erp.api.crm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.api.filtering.CustomerOrderPredicateBuilder;
import com.cassinisys.erp.api.filtering.ShipmentPredicateBuilder;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.CustomerOrderCriteria;
import com.cassinisys.erp.model.api.criteria.ShipmentCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.service.crm.ShipmentService;
import com.cassinisys.erp.service.print.PrintService;
import com.mysema.query.types.Predicate;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * Created by reddy on 10/4/15.
 */

@RestController
@RequestMapping("crm/shipments")
@Api(name = "Shipment", description = "Shipment endpoint", group = "CRM")
public class ShipmentController extends BaseController{

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShipmentPredicateBuilder predicateBuilder;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private PrintService printService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ERPCustomerOrderShipment> getShipments(ShipmentCriteria criteria,
                                                       ERPPageRequest pageRequest) {
        return shipmentService.findAll(criteria, pageRequest);
    }

    @RequestMapping(value="/{shipmentId}", method = RequestMethod.POST)
    public ERPCustomerOrderShipment processShipment(@PathVariable Integer shipmentId,
            @RequestBody @Valid ERPCustomerOrderShipment shipment) {
        return shipmentService.processShipment(shipmentId, shipment);
    }

    @RequestMapping(value="/{shipmentId}", method = RequestMethod.PUT)
    public ERPCustomerOrderShipment updateShipment(@RequestBody @Valid ERPCustomerOrderShipment shipment) {
        return shipmentService.update(shipment);
    }

    @RequestMapping(value="/{shipmentId}/details", method = RequestMethod.GET)
    public SortedSet<ERPCustomerOrderShipmentDetails> getShipmentDetails(@PathVariable Integer shipmentId) {
        ERPCustomerOrderShipment shipment = shipmentService.get(shipmentId);
        return shipment.getDetails();
    }

    @RequestMapping(value="/{shipmentId}/details/{itemId}", method = RequestMethod.DELETE)
    public void deleteShipmentItem(@PathVariable Integer itemId) {
        shipmentService.deleteShipmentItem(itemId);
    }


    @RequestMapping(value="/{shipmentId}", method = RequestMethod.DELETE)
    public void deleteShipment(@PathVariable Integer shipmentId) {
        shipmentService.deleteShipment(shipmentId);
    }

    @RequestMapping(value = "/{shipmentId}/cancel", method = RequestMethod.GET)
    public ERPCustomerOrderShipment cancelShipment(
            @PathVariable("shipmentId") Integer shipmentId) {{
            return shipmentService.cancelShipment(shipmentId);
        }
    }

    @RequestMapping(value="/invoice/{invoiceNumber}", method = RequestMethod.GET)
    public ERPCustomerOrderShipment getShipmentByInvoiceNumber(@PathVariable String invoiceNumber) {
        return shipmentService.getShipmentByInvoiceNumber(invoiceNumber);
    }

    @RequestMapping(value="/{shipmentId}/print", method = RequestMethod.GET)
    public void printShipment(@PathVariable Integer shipmentId, HttpServletResponse response) {
        try {
            String html = printService.printInvoice(shipmentId);
            response.setContentType("text/html");
            response.getWriter().write(html);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value="/[{ids}]", method = RequestMethod.GET)
    public List<ERPCustomerOrderShipment> getShipmentsByIds(@PathVariable Integer[] ids) {
        return shipmentService.getShipmentsByIds(Arrays.asList(ids));
    }

    @RequestMapping(value="/{shipmentId}", method = RequestMethod.GET)
    public List<ERPCustomerOrderShipment> getShipments(@PathVariable Integer shipmentId) {
        return shipmentService.getOrderShipments(shipmentId);
    }

    @RequestMapping(value="/shippers", method = RequestMethod.GET)
    public List<ERPShipper> getShippers() {
        return shipmentService.getShippers();
    }

    @RequestMapping(value="/shippers", method = RequestMethod.POST)
    public ERPShipper createShipper(@RequestBody @Valid ERPShipper shipper) {
        return shipmentService.createShipper(shipper);
    }

    @RequestMapping(value="/shippers/{shipperId}", method = RequestMethod.PUT)
    public ERPShipper updateShipper(@PathVariable String shipperId,
                                    @RequestBody @Valid ERPShipper shipper) {
        return shipmentService.createShipper(shipper);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<ERPCustomerOrderShipment> search(ShipmentCriteria criteria,
                                         ERPPageRequest pageRequest) {
        criteria.setFreeTextSearch(true);
        Predicate predicate = predicateBuilder.build(criteria,
                QERPCustomerOrderShipment.eRPCustomerOrderShipment);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return shipmentService.findAll(predicate, pageable);
    }

   /* @RequestMapping(value = "/{shipmentId}/cancel", method = RequestMethod.GET)
    public ERPCustomerOrderShipment cancelOrderShipment(@PathVariable("shipmentId") Integer shipmentId) {
            return shipmentService.cancelShipment(shipmentId);
    }*/
}
