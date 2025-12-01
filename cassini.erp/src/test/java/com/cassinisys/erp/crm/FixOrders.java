package com.cassinisys.erp.crm;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.model.production.ERPProduct;
import com.cassinisys.erp.repo.crm.*;
import com.cassinisys.erp.service.crm.CustomerOrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.util.*;

/**
 * Created by reddy on 4/15/16.
 */
public class FixOrders extends BaseTest {

    @Autowired
    private CustomerOrderRepository orderRepository;

    @Autowired
    private CustomerOrderService orderService;

    @Autowired
    private CustomerOrderDetailsRepository orderDetailsRepository;

    @Autowired
    private CustomerOrderShipmentRepository shipmentRepository;

    @Autowired
    private CustomerOrderShipmentDetailsRepository shipmentDetailsRepository;

    @Autowired
    private CustomerOrderStatusHistoryRepository orderStatusHistoryRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Test
    public void findProblemOrders() throws Exception {
        String jpql = "SELECT o.id, o.orderNumber FROM ERPCustomerOrder o WHERE o.status = 'SHIPPED' ORDER BY o.orderedDate DESC";
        List<Object[]> orders = entityManager.createQuery(jpql).getResultList();

        List<String> problemOrders = new ArrayList<>();
        for(Object[] order : orders) {
            try {
                if (checkOrder((Integer) order[0])) {
                    System.out.println(order[1]);
                    problemOrders.add((String)order[1]);
                }
            } catch(Exception e) {

            }
        }

        System.out.println("\n\n PROBLEM ORDERS:");
        for(String problemOrder : problemOrders) {
            System.out.println(problemOrder);
        }
    }

    @Test
    @Rollback(false)
    public void fixProblemOrders() throws Exception {
        String jpql = "SELECT o.id, o.orderNumber FROM ERPCustomerOrder o WHERE o.status = 'SHIPPED' ORDER BY o.orderedDate DESC";
        List<Object[]> results = entityManager.createQuery(jpql).getResultList();

        String fileName = "/Users/reddy/Downloads/problem-orders.txt";
        BufferedReader bis = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

        String strLine;

        while ((strLine = bis.readLine()) != null)   {
            String orderNumber = strLine.trim();
        //for(Object[] result : results) {
            try {
                ERPCustomerOrder order = orderRepository.findByOrderNumber(orderNumber);
                if (order != null && checkOrder(order.getId()) && order.getBillingAddress() != null) {
                    System.out.println(orderNumber);

                    jpql = "SELECT h FROM ERPCustomerOrderStatusHistory h WHERE h.order.id = " + order.getId() +
                            " AND h.oldStatus = 'PROCESSED' AND h.newStatus = 'SHIPPED'";

                    List<Object> hResults = entityManager.createQuery(jpql).getResultList();
                    if (hResults.size() > 0) {
                        ERPCustomerOrderStatusHistory history = (ERPCustomerOrderStatusHistory) hResults.get(hResults.size() - 1);

                        if (history != null) {
                            history.setNewStatus(CustomerOrderStatus.PARTIALLYSHIPPED);
                            orderStatusHistoryRepository.save(history);
                        }
                    }

                    order.setStatus(CustomerOrderStatus.PARTIALLYSHIPPED);
                    orderRepository.save(order);
                }
            }catch(Exception e){}
        }

        bis.close();
    }


    private Boolean checkOrder(Integer orderId) throws Exception {
        Boolean problem = Boolean.FALSE;

        List<ERPCustomerOrderDetails> items = orderDetailsRepository.findByOrderId(orderId);
        Map<Integer, ERPCustomerOrderDetails> map = new HashMap<>();

        /*
        for(ERPCustomerOrderDetails item : items) {
            map.put(item.getProduct().getId(), item);
        }

        String jpql = "SELECT s FROM ERPCustomerOrderShipment s WHERE s.order.id = " + orderId;
        List<ERPCustomerOrderShipment> shipments = entityManager.createQuery(jpql).getResultList();
        Map<Integer, Integer> anotherMap = new HashMap<>();

        for(ERPCustomerOrderShipment shipment : shipments) {
            Set<ERPCustomerOrderShipmentDetails> shippedItems = shipment.getDetails();
            for(ERPCustomerOrderShipmentDetails shippedItem : shippedItems) {
                Integer itemId = shippedItem.getProduct().getId();
                Integer shippedQty = shippedItem.getQuantityShipped();
                if(shippedQty == null) {
                    shippedQty = 0;
                }

                Integer totalQty = anotherMap.get(itemId);
                if(totalQty == null) {
                    totalQty = 0;
                }

                totalQty += shippedQty;
                anotherMap.put(itemId, totalQty);
            }
        }
        */

        for(ERPCustomerOrderDetails item : items) {
            Integer shippedQty = item.getQuantityShipped();
            //Integer qtyFromShipments = anotherMap.get(item.getProduct().getId());
            if(shippedQty == null || shippedQty == 0) {
                problem = Boolean.TRUE;
            }
            else if(shippedQty < item.getQuantity()) {
                problem = Boolean.TRUE;
            }
        }

        return problem;
    }


    @Test
    public void findItemQuantityInPartiallyShippedOrders() throws Exception {
        String jpql = "SELECT o.id, o.orderNumber FROM ERPCustomerOrder o WHERE o.status = 'PARTIALLYSHIPPED' ORDER BY o.orderedDate DESC";
        List<Object[]> orders = entityManager.createQuery(jpql).getResultList();

        Map<Integer, ItemQuantity> map = new HashMap<>();
        for(Object[] order : orders) {
            System.out.println(order[1]);

            Integer orderId = (Integer) order[0];
            List<ERPCustomerOrderDetails> details = null;
            try {
                details = orderService.getAllOrderDetailsByOrderId(orderId);
            } catch(Exception e){}

            if(details != null) {
                for (ERPCustomerOrderDetails detail : details) {
                    if (detail.getQuantityShipped() == null) {
                        detail.setQuantityShipped(0);
                    }
                    if (detail.getQuantityShipped() < detail.getQuantity()) {
                        ItemQuantity iq = map.get(detail.getProduct().getId());
                        if (iq == null) {
                            iq = new ItemQuantity();
                            iq.product = detail.getProduct();
                            iq.quantity = 0;
                            map.put(detail.getProduct().getId(), iq);
                        }

                        iq.quantity += (detail.getQuantity() - detail.getQuantityShipped());
                    }
                }
            }
        }

        Collection<ItemQuantity> list = map.values();
        for(ItemQuantity it : list) {
            System.out.println(it.product.getName() + " : " + it.quantity);
        }

    }


    public class ItemQuantity {
        public ERPProduct product;
        public Integer quantity = 0;
    }

}
