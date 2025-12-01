package com.cassinisys.erp.api.crm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.api.criteria.ConsignmentCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.crm.ERPConsignment;
import com.cassinisys.erp.service.crm.ConsignmentService;
import com.cassinisys.erp.service.print.PrintService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by reddy on 10/14/15.
 */
@RestController
@RequestMapping("crm/consignments")
@Api(name = "Consignment", description = "Consignment endpoint", group = "CRM")
public class ConsignmentController extends BaseController {

    @Autowired
    private ConsignmentService consignmentService;

    @Autowired
    private PrintService printService;

    @RequestMapping (method = RequestMethod.GET)
    public Page<ERPConsignment> getConsignments(ConsignmentCriteria criteria,
                                                ERPPageRequest pageRequest) {
        return consignmentService.findAll(criteria, pageRequest);
    }

    @RequestMapping (method = RequestMethod.POST)
    public ERPConsignment createConsignment(@RequestBody ERPConsignment consignment) {
        return consignmentService.shipConsignment(consignment);
    }

    @RequestMapping(value="/shipments/{ids}", method = RequestMethod.GET)
    public List<ERPConsignment> getConsignmentsForShipments(@PathVariable List<Integer> ids) {
        return consignmentService.getConsignmentsForShipments(ids);
    }

    @RequestMapping(value="/shipments/{shipmentId}", method = RequestMethod.DELETE)
    public void deleteShipmentsFromConsignment(@PathVariable Integer shipmentId) {
        consignmentService.deleteShipmentsFromConsignment(shipmentId);
    }

    @RequestMapping(value="/consignments/{orderid}", method = RequestMethod.GET)
    public List<ERPConsignment> getConsignmentsForShipments(Integer orderid) {
        return consignmentService.getConsignmentsByOrderId(orderid);
    }

    @RequestMapping (value = "/{id}", method = RequestMethod.PUT)
    public ERPConsignment createConsignment(@PathVariable Integer id,
                                            @RequestBody ERPConsignment consignment) {
        return consignmentService.update(consignment);
    }


    @RequestMapping(value = "/{id}/print", method = RequestMethod.GET)
    public void printConsignment(@PathVariable("id") Integer id, HttpServletResponse response) {
        try {
            String html = printService.printConsignment(id);
            response.setContentType("text/html");
            response.getWriter().write(html);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value="/{id}/shipments/print", method = RequestMethod.GET)
    public void printConsignmentShipments(@PathVariable Integer id, HttpServletResponse response) {
        try {
            String html = printService.printConsignmentInvoices(id);
            response.setContentType("text/html");
            response.getWriter().write(html);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping (value = "/{id}", method = RequestMethod.GET)
    public ERPConsignment createConsignment(@PathVariable Integer id) {
        return consignmentService.get(id);
    }
}
