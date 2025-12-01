package com.cassinisys.erp.service.print;

import com.cassinisys.erp.config.AutowiredLogger;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.repo.crm.CustomerOrderShipmentRepository;
import com.cassinisys.erp.repo.crm.CustomerOrderVerificationRepository;
import com.cassinisys.erp.service.config.ConfigService;
import com.cassinisys.erp.service.crm.ConsignmentService;
import com.cassinisys.erp.service.crm.CustomerOrderService;
import com.cassinisys.erp.service.crm.ShipmentService;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by reddy on 10/1/15.
 */
@Service
public class PrintService {
    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private ConsignmentService consignmentService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private CustomerOrderVerificationRepository verificationRepository;

    @Autowired
    private CustomerOrderShipmentRepository shipmentRepository;

    @AutowiredLogger
    private Logger LOGGER;

    public String printOrder(Integer orderId) {
        String output = "";

        try {
            TemplateEngine engine = new SimpleTemplateEngine();
            File tplFile = configService.getCurrentTenantConfig().getPrintOrderTemplateFile();
            Template template = engine.createTemplate(new InputStreamReader(FileUtils.openInputStream(tplFile)));

            ERPCustomerOrder order = customerOrderService.get(orderId);
            List<ERPCustomerOrderDetails> details = customerOrderService.getAllOrderDetailsByOrderId(orderId);
            order.setDetails(details);

            Map<String, Object> map = new HashMap<>();
            map.put("order", order);
            output = template.make(map).toString();
        } catch (Exception e) {
            LOGGER.error("Error printing order details", e);
        }

        return output;
    }

    public String printInvoice(Integer id) {
        String output = "";

        try {
            TemplateEngine engine = new SimpleTemplateEngine();
            File tplFile = configService.getCurrentTenantConfig().getPrintInvoiceTemplateFile();
            Template template = engine.createTemplate(new InputStreamReader(FileUtils.openInputStream(tplFile)));

            ERPCustomerOrderShipment shipment = shipmentService.get(id);
            Map<String, Object> map = new HashMap<>();
            map.put("shipment", shipment);
            output = template.make(map).toString();
        } catch (Exception e) {
            LOGGER.error("Error printing invoice details", e);
        }

        return output;
    }

    public String printConsignmentInvoices(Integer id) {
        String output = "";

        try {
            TemplateEngine engine = new SimpleTemplateEngine();
            File tplFile = configService.getCurrentTenantConfig().getPrintInvoiceTemplateFile();
            Template template = engine.createTemplate(new InputStreamReader(FileUtils.openInputStream(tplFile)));

            List<ERPCustomerOrderShipment> shipments = shipmentRepository.findByConsignment(consignmentService.get(id));
            Map<String, Object> map = new HashMap<>();
            map.put("shipments", shipments);
            output = template.make(map).toString();
        } catch (Exception e) {
            LOGGER.error("Error printing invoice details", e);
        }

        return output;
    }

    public String printConsignment(Integer id) {
        String output = "";
        try {
            ERPConsignment consignment = consignmentService.get(id);
            ERPShipper shipper = consignment.getShipper();
            String name = shipper.getName();

            TemplateEngine engine = new SimpleTemplateEngine();
            File tplFile = configService.getCurrentTenantConfig().getShipperTemplate(name);
            if(tplFile == null) {
                tplFile = configService.getCurrentTenantConfig().getShipperTemplate("Default");
            }
            if(tplFile != null) {
                Template template = engine.createTemplate(new InputStreamReader(FileUtils.openInputStream(tplFile)));

                Map<String, Object> map = new HashMap<>();
                map.put("consignment", consignment);
                output = template.make(map).toString();
            }
            else {
                output = "No template found";
            }
        } catch (Exception e) {
            LOGGER.error("Error printing order details", e);
        }
        return output;
    }
}
