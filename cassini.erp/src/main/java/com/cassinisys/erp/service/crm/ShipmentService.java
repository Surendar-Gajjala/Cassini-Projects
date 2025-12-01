package com.cassinisys.erp.service.crm;

import com.cassinisys.erp.api.exceptions.ResourceNotFoundException;
import com.cassinisys.erp.api.filtering.ShipmentPredicateBuilder;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.ShipmentCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.common.ERPAddress;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.model.crm.QERPCustomerOrderShipment;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.repo.crm.CustomerOrderShipmentDetailsRepository;
import com.cassinisys.erp.repo.crm.CustomerOrderShipmentRepository;
import com.cassinisys.erp.repo.crm.CustomerOrderStatusHistoryRepository;
import com.cassinisys.erp.repo.crm.ShipperRepository;
import com.cassinisys.erp.service.hrm.EmployeeService;
import com.cassinisys.erp.service.notification.sms.InvoiceOrderSms;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.production.ProductInventoryService;
import com.cassinisys.erp.service.security.SessionWrapper;
import com.cassinisys.erp.util.Utils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by reddy on 10/5/15.
 */
@Service
@Transactional
public class ShipmentService implements CrudService<ERPCustomerOrderShipment, Integer> {
    @Autowired
    SessionWrapper sessionWrapper;

    @Autowired
    private CustomerOrderShipmentRepository orderShipmentRepository;

    @Autowired
    private CustomerOrderShipmentDetailsRepository shipmentDetailsRepository;

    @Autowired
    private CustomerOrderService orderService;

    @Autowired
    private CustomerOrderStatusHistoryRepository orderStatusHistoryRepository;

    @Autowired
    private ProductInventoryService productInventoryService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ShipperRepository shipperRepository;

    @Autowired
    private ShipmentPredicateBuilder shipmentPredicateBuilder;

    @Autowired
    private PageRequestConverter pageRequestConverter;


    @Override
    public ERPCustomerOrderShipment create(ERPCustomerOrderShipment shipment) {
        return orderShipmentRepository.save(shipment);
    }

    @Override
    public ERPCustomerOrderShipment update(ERPCustomerOrderShipment shipment) {
        return orderShipmentRepository.save(shipment);
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public ERPCustomerOrderShipment get(Integer id) {
        return orderShipmentRepository.findOne(id);
    }

    @Override
    public List<ERPCustomerOrderShipment> getAll() {
        return orderShipmentRepository.findAll();
    }

    public Page<ERPCustomerOrderShipment> findAll(ShipmentCriteria criteria, ERPPageRequest pageRequest) {
        Predicate predicate = shipmentPredicateBuilder.build(criteria, QERPCustomerOrderShipment.eRPCustomerOrderShipment);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return orderShipmentRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ERPCustomerOrderShipment> findAll(Predicate predicate, Pageable pageable) {
        return orderShipmentRepository.findAll(predicate, pageable);
    }

    public void deleteShipmentItem(Integer itemId) {
        shipmentDetailsRepository.deleteItem(itemId);
    }

    public void deleteShipment(Integer shipmentId) {
        orderShipmentRepository.deleteItem(shipmentId);
    }


    public List<ERPCustomerOrderShipment> getOrderShipments(Integer orderId) {
        return orderShipmentRepository.findByOrder(orderService.get(orderId));
    }

    public ERPCustomerOrderShipment processShipment(Integer orderId,
                                                    ERPCustomerOrderShipment shipment) {
        ERPCustomerOrder order = orderService.get(orderId);
        shipment.setStatus(ShipmentStatus.PENDING);

        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);
        //Create a status item
        CustomerOrderStatus oldStatus = order.getStatus();
        if(oldStatus != CustomerOrderStatus.PARTIALLYSHIPPED) {
            order.setStatus(CustomerOrderStatus.PROCESSED);
            order = orderService.update(order);
        }

        //save to CustomerOrderStatusHistory
        ERPCustomerOrderStatusHistory orderStatus = new ERPCustomerOrderStatusHistory();
        orderStatus.setModifiedDate(new Date());

        orderStatus.setModifiedBy(employee);
        orderStatus.setNewStatus(CustomerOrderStatus.PROCESSED);
        orderStatus.setOldStatus(oldStatus);
        orderStatus.setOrder(order);

        orderStatusHistoryRepository.save(orderStatus);

        shipment.setOrder(order);
        shipment = orderShipmentRepository.save(shipment);

        /*
        if(order.getOrderType() == OrderType.PRODUCT) {
            try {
                InvoiceOrderSms sms = new InvoiceOrderSms();
                sms.getPlaceholders().put("poNumber", shipment.getOrder().getPoNumber());
                sms.getPlaceholders().put("invoiceTo", shipment.getOrder().getCustomer().getName());
                String shipTo = shipment.getOrder().getShipTo() != null ? shipment.getOrder().getShipTo() :
                        shipment.getOrder().getCustomer().getName();
                sms.getPlaceholders().put("shipTo", shipTo);
                sms.getPlaceholders().put("invoiceNumber", shipment.getInvoiceNumber());
                sms.getPlaceholders().put("invoiceAmount", ""+shipment.getInvoiceAmount());

                List<String> phoneNumbers = getPhoneNumbers(order, shipment);
                for (String phoneNumber : phoneNumbers) {
                    sms.sendTo("+91" + phoneNumber);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */

        return shipment;
    }

    private List<String> getPhoneNumbers(ERPCustomerOrder order, ERPCustomerOrderShipment shipment) {
        List<String> phoneNumbers = new ArrayList<>();

        List<ERPAddress> addresses = Arrays.asList(order.getBillingAddress(), shipment.getBillingAddress());

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

        return phoneNumbers;
    }

    private boolean isOrderShippedInFull(ERPCustomerOrder order) {
        boolean shippedInFull = true;

        Set<ERPCustomerOrderDetails> orderDetails = new HashSet<>(orderService.getAllOrderDetailsByOrderId(order.getId()));
        for(ERPCustomerOrderDetails item : orderDetails) {
            Integer quantity = item.getQuantity();
            Integer quantityShipped = item.getQuantityShipped();

            if(quantity != null && quantityShipped != null &&
                    quantity.intValue() != quantityShipped.intValue()) {
                shippedInFull = false;
            }
        }

        return shippedInFull;
    }

    public ERPCustomerOrderShipment getShipmentByInvoiceNumber(String invoiceNumber) {
        return orderShipmentRepository.findFirstByInvoiceNumber(invoiceNumber);
    }

    public List<ERPShipper> getShippers() {
        return shipperRepository.findAll();
    }

    public ERPShipper createShipper(ERPShipper shipper) {
        return shipperRepository.save(shipper);
    }

    public ERPShipper updateShipper(ERPShipper shipper) {
        return shipperRepository.save(shipper);
    }

    public List<ERPCustomerOrderShipment> getShipmentsByIds(List<Integer> ids) {
        Predicate predicate = QERPCustomerOrderShipment.eRPCustomerOrderShipment.id.in(ids);
        Iterable<ERPCustomerOrderShipment> it = orderShipmentRepository.findAll(predicate);
        return Utils.makeList(it);
    }

    public ERPCustomerOrderShipment cancelShipment(Integer shipmentId) {
        checkNotNull(shipmentId);
        ERPCustomerOrderShipment shipment = orderShipmentRepository.findOne(shipmentId);
        if (shipment != null) {
            if (shipment.getStatus() == ShipmentStatus.PENDING) {
                cancelPendingItems(shipment);
            }
            shipment.setStatus(ShipmentStatus.CANCELLED);
            shipment = orderShipmentRepository.save(shipment);

        } else {
            throw new ResourceNotFoundException("Shipment " + shipmentId + " does not exist");
        }

        return shipment;

    }
    private void cancelPendingItems(ERPCustomerOrderShipment shipment) {
        List<ERPCustomerOrderShipmentDetails> details = getAllOrderShipmentDetailsByShipmentId(shipment.getId());
        List<ERPCustomerOrderShipmentDetails> itemsToRemove = new ArrayList<>();
        for (ERPCustomerOrderShipmentDetails detail : details) {
            detail.setQuantityShipped(0);
            if (detail.getQuantityShipped()== 0) {
                    itemsToRemove.add(detail);
                }
            else {
                shipmentDetailsRepository.save(detail);
                }
        }
        shipmentDetailsRepository.delete(itemsToRemove);
    }

    public List<ERPCustomerOrderShipmentDetails> getAllOrderShipmentDetailsByShipmentId(
            Integer shipmentId) {
        return shipmentDetailsRepository.findByShipmentId(shipmentId);
    }
}
