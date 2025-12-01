package com.cassinisys.erp.crm;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.config.TenantManager;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.ConsignmentCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.crm.ERPConsignment;
import com.cassinisys.erp.model.crm.ERPCustomerOrder;
import com.cassinisys.erp.model.crm.ERPCustomerOrderShipment;
import com.cassinisys.erp.model.production.ERPProductInventory;
import com.cassinisys.erp.repo.crm.CustomerOrderRepository;
import com.cassinisys.erp.repo.crm.CustomerOrderShipmentRepository;
import com.cassinisys.erp.repo.production.ProductInventoryRepository;
import com.cassinisys.erp.service.crm.ConsignmentService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

/**
 * Created by reddy on 10/2/15.
 */
public class TestOrders extends BaseTest {
    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private CustomerOrderShipmentRepository shipmentRepository;

    @Autowired
    private ConsignmentService consignmentService;

    @Autowired
    private PageRequestConverter pageRequestConverter;


    @BeforeClass
    public static void init() {
        TenantManager.get().setTenantId("cassini");
    }


    @Test
    @Rollback(false)
    public void testOrderUpdate() throws Exception {
    }

    @Test
    public void testShipmentByInvoiceNumber() throws Exception {
        ERPCustomerOrderShipment shipment = shipmentRepository.findFirstByInvoiceNumber("INV-00001");
        System.out.println(shipment.getInvoiceAmount());
    }

    @Test
    public void testConsignment() throws Exception {
        ConsignmentCriteria criteria = new ConsignmentCriteria();
        Page<ERPConsignment> page = consignmentService.findAll(criteria, new ERPPageRequest());

        System.out.println(page);
    }

    @Test
    public void testOrders() throws Exception {
        ERPCustomerOrder order = customerOrderRepository.findOne(2266);
        System.out.println(order);
    }

    @Test
    public void testLateApprovedOrders() throws Exception {
        Pageable pageable = pageRequestConverter.convert(new ERPPageRequest());
        Page<ERPCustomerOrder> list = customerOrderRepository.getLateApprovedOrders(pageable);
        System.out.println("\nFound " + list.getContent().size() + " items\n");
    }

    @Test
    public void testLowinventory() throws Exception {
        Pageable pageable = pageRequestConverter.convert(new ERPPageRequest());
        Page<ERPProductInventory> list = productInventoryRepository.getLowInventoryItems(pageable);
        System.out.println("\nFound " + list.getContent().size() + " items\n");
    }


   /* @Test
    public void testLateProcessedOrders() throws Exception {
        Pageable pageable = pageRequestConverter.convert(new ERPPageRequest());
        Page<ERPCustomerOrder> list = customerOrderRepository.getLateProcessedOrders(new Date(),pageable);
        System.out.println(list);
    }*/
}
