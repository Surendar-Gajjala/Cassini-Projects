package com.cassinisys.erp.service.crm;

import com.cassinisys.erp.api.filtering.ConsignmentPredicateBuilder;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.ConsignmentCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.common.ERPAddress;
import com.cassinisys.erp.model.common.ERPAutoNumber;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.repo.crm.ConsignmentRepository;
import com.cassinisys.erp.repo.crm.CustomerOrderDetailsRepository;
import com.cassinisys.erp.repo.crm.CustomerOrderShipmentRepository;
import com.cassinisys.erp.repo.crm.CustomerOrderStatusHistoryRepository;
import com.cassinisys.erp.service.common.AutoNumberService;
import com.cassinisys.erp.service.hrm.EmployeeService;
import com.cassinisys.erp.service.notification.sms.OrderShipmentSms;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import com.cassinisys.erp.service.production.ProductInventoryService;
import com.cassinisys.erp.service.security.SessionWrapper;
import com.cassinisys.erp.util.Utils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by reddy on 10/14/15.
 */
@Service
@Transactional
public class ConsignmentService implements
        CrudService<ERPConsignment, Integer>,
        PageableService<ERPConsignment, Integer> {
    @Autowired
    SessionWrapper sessionWrapper;

    @Autowired
    private ConsignmentRepository consignmentRepository;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ConsignmentPredicateBuilder consignmentPredicateBuilder;

    @Autowired
    private CustomerOrderShipmentRepository orderShipmentRepository;

    @Autowired
    private CustomerOrderStatusHistoryRepository orderStatusHistoryRepository;

    @Autowired
    private ProductInventoryService productInventoryService;

    @Autowired
    private CustomerOrderService orderService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AutoNumberService autoNumberService;

    @Autowired
    private CustomerOrderDetailsRepository orderDetailsRepository;



    @Override
    public ERPConsignment create(ERPConsignment consignment) {
        return consignmentRepository.save(consignment);
    }

    @Override
    public ERPConsignment update(ERPConsignment consignment) {
        consignment = consignmentRepository.save(consignment);

        try {
            if(consignment != null) {
                sendSms(consignment);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        return consignment;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public ERPConsignment get(Integer id) {
        return consignmentRepository.findOne(id);
    }

    @Override
    public List<ERPConsignment> getAll() {
        return consignmentRepository.findAll();
    }

    @Override
    public Page<ERPConsignment> findAll(Pageable pageable) {
        return consignmentRepository.findAll(pageable);
    }

    public Page<ERPConsignment> findAll(ConsignmentCriteria criteria, ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Predicate predicate = consignmentPredicateBuilder.build(criteria, QERPConsignment.eRPConsignment);
        return consignmentRepository.findAll(predicate, pageable);
    }

    public ERPConsignment shipConsignment(ERPConsignment consignment) {
        Set<ERPCustomerOrderShipment> shipments = consignment.getShipments();

        ERPAutoNumber auto = autoNumberService.findByName("Consignment Number");
        String number = autoNumberService.getNextNumber(auto.getId());

        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);

        consignment.setNumber(number);
        consignment.setShipments(null);
        consignment.setStatus(ConsignmentStatus.SHIPPED);
        consignment.setShippedDate(new Date());
        consignment.setShippedBy(employee);

        ERPConsignment savedConsignment = consignmentRepository.save(consignment);

        for(ERPCustomerOrderShipment shipment : shipments) {
            ERPCustomerOrderShipment updatedShipment = shipShipment(shipment);
            updatedShipment.setStatus(ShipmentStatus.SHIPPED);
            updatedShipment.setConsignment(savedConsignment);
            orderShipmentRepository.save(updatedShipment);
        }

        return savedConsignment;
    }

    private ERPCustomerOrderShipment shipShipment(ERPCustomerOrderShipment shipment) {
        ERPCustomerOrderShipment updatedShipment = orderShipmentRepository.findOne(shipment.getId());
        ERPCustomerOrder order = updatedShipment.getOrder();

        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);

        Map<Integer, ERPCustomerOrderShipmentDetails> map = new HashMap<>();
        for (ERPCustomerOrderShipmentDetails item : updatedShipment.getDetails()) {
            if(item.getQuantityShipped() != null && item.getQuantityShipped() > 0) {
                map.put(item.getProduct().getId(), item);
            }
        }

        //Update the quantity shipped on the order items
        Set<ERPCustomerOrderDetails> orderDetails = new HashSet<>(orderService.getAllOrderDetailsByOrderId(order.getId()));
        for (ERPCustomerOrderDetails item : orderDetails) {
            ERPCustomerOrderShipmentDetails shipmentItem = map.get(item.getProduct().getId());
            if(shipmentItem != null) {
                Integer quantityShipped = item.getQuantityShipped();
                if (quantityShipped == null) {
                    quantityShipped = 0;
                }

                if(shipmentItem.getQuantityShipped() != null) {
                    Integer qty = quantityShipped + shipmentItem.getQuantityShipped();
                    if(qty > item.getQuantity()) {
                        quantityShipped = shipmentItem.getQuantityShipped();
                    }
                    else {
                        quantityShipped = qty;
                    }
                }
                else {
                    quantityShipped = item.getQuantity();
                }
                item.setQuantityShipped(quantityShipped);

                if(shipmentItem.getQuantityShipped() > 0) {
                    productInventoryService.stockOut(item.getProduct().getId(), shipmentItem.getQuantityShipped(), shipment);
                }
            }
        }

        orderDetailsRepository.save(orderDetails);
        order = orderService.update(order);

        CustomerOrderStatus oldStatus = order.getStatus();
        //If all the items in the order are shipped, then mark the order as shipped
        if (isOrderShippedInFull(order, orderDetails)) {
            order.setStatus(CustomerOrderStatus.SHIPPED);
        }
        else {
            order.setStatus(CustomerOrderStatus.PARTIALLYSHIPPED);
            order.setStarred(false);
        }

        order = orderService.update(order);

        //save to CustomerOrderStatusHistory
        ERPCustomerOrderStatusHistory orderStatus = new ERPCustomerOrderStatusHistory();
        orderStatus.setModifiedDate(new Date());

        orderStatus.setModifiedBy(employee);
        orderStatus.setNewStatus(order.getStatus());
        orderStatus.setOldStatus(oldStatus);
        orderStatus.setOrder(order);

        orderStatusHistoryRepository.save(orderStatus);

        return updatedShipment;
    }

    private boolean isOrderShippedInFull(ERPCustomerOrder order, Set<ERPCustomerOrderDetails> orderDetails) {
        boolean shippedInFull = true;

        for(ERPCustomerOrderDetails item : orderDetails) {
            Integer quantity = item.getQuantity();
            Integer quantityShipped = item.getQuantityShipped();

            if(quantityShipped == null) {
                shippedInFull = false;
            }
            else if(quantity != null && quantityShipped != null &&
                    quantity > quantityShipped) {
                shippedInFull = false;
            }
        }

        return shippedInFull;
    }

    public List<ERPConsignment> getConsignmentsForShipments(List<Integer> shipmentIds) {
        Predicate predicate = QERPConsignment.eRPConsignment.shipments.any().id.in(shipmentIds);
        Iterable<ERPConsignment> it = consignmentRepository.findAll(predicate);
        return Utils.makeList(it);
    }

    public void deleteShipmentsFromConsignment(Integer shipmentIds) {
        ERPCustomerOrderShipment shipment =  orderShipmentRepository.findOne(shipmentIds);
        shipment.setConsignment(null);
        orderShipmentRepository.save(shipment);
    }

    public List<ERPConsignment> getConsignmentsByOrderId(Integer OrderId) {
         ERPConsignment consignment = consignmentRepository.findOne(OrderId);
          List<ERPConsignment> ListOfConsignments = new ArrayList<>();
          ListOfConsignments.add(consignment);
        return ListOfConsignments;
    }

    private void sendSms(ERPConsignment consignment) {
        if(consignment.getStatus() == ConsignmentStatus.FINISHED && consignment.getConfirmationNumber() != null) {
            List<String> phoneNumbers = new ArrayList<>();

            List<ERPCustomerOrderShipment> shipments = orderShipmentRepository.findByConsignment(consignment);
            List<ERPAddress> addresses = new ArrayList<>();

            String poNumbers = "";
            for(ERPCustomerOrderShipment shipment : shipments) {
                if(shipment.getOrder().getOrderType() == OrderType.PRODUCT) {
                    addresses.add(shipment.getBillingAddress());
                    addresses.add(shipment.getOrder().getBillingAddress());

                    if (shipment.getOrder().getPoNumber() != null) {
                        poNumbers += shipment.getOrder().getPoNumber();
                    }
                }
            }

            for(ERPAddress address : addresses) {
                if (address != null) {
                    List<String> numbers = Utils.extractPhoneNumbers(address.getAddressText());
                    for (String number : numbers) {
                        number = number.trim();
                        if (!phoneNumbers.contains(number)) {
                            phoneNumbers.add(number);
                        }
                    }
                }
            }

            if(phoneNumbers.size() > 0) {
                String shippedDate = new SimpleDateFormat("dd-MM-yyy").format(consignment.getShippedDate());

                OrderShipmentSms sms = new OrderShipmentSms();

                sms.getPlaceholders().put("poNumber", poNumbers);
                sms.getPlaceholders().put("shipper",consignment.getShipper().getName());
                sms.getPlaceholders().put("lrNumber",consignment.getConfirmationNumber());

                ERPCustomerOrderShipment shipment = orderShipmentRepository.findByConsignment(consignment).get(0);
                ERPCustomerOrder order = shipment.getOrder();
                sms.getPlaceholders().put("invoiceTo", order.getCustomer().getName());
                String shipTo = order.getShipTo() != null ? order.getShipTo() :
                        order.getCustomer().getName();
                sms.getPlaceholders().put("shipTo", shipTo);
                sms.getPlaceholders().put("shipDate", shippedDate);
                sms.getPlaceholders().put("contents", consignment.getContents());


                for (String phoneNumber : phoneNumbers) {
                    sms.sendTo("+91" + phoneNumber);
                }
            }
        }
    }
}
