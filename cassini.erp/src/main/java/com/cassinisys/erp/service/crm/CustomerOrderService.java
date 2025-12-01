package com.cassinisys.erp.service.crm;

import com.cassinisys.erp.api.exceptions.InventoryException;
import com.cassinisys.erp.api.exceptions.ResourceNotFoundException;
import com.cassinisys.erp.model.common.ERPAddress;
import com.cassinisys.erp.model.common.ERPAutoNumber;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.repo.common.AddressRepository;
import com.cassinisys.erp.repo.crm.*;
import com.cassinisys.erp.service.common.AutoNumberService;
import com.cassinisys.erp.service.hrm.EmployeeService;
import com.cassinisys.erp.service.notification.push.MessageSender;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class CustomerOrderService implements
        CrudService<ERPCustomerOrder, Integer>,
        PageableService<ERPCustomerOrder, Integer> {

    @Autowired
    SessionWrapper sessionWrapper;

    @Autowired
    CustomerOrderRepository custOrderRepository;

    @Autowired
    CustomerOrderDetailsRepository custOrderDetailsRepository;

    @Autowired
    CustomerOrderStatusHistoryRepository orderStatusHistoryRepository;

    @Autowired
    AutoNumberService autoNumberService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProductInventoryService productInventoryService;

    @Autowired
    private CustomerOrderShipmentRepository customerOrderShipmentRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerOrderVerificationRepository verificationRepository;

    @Autowired
    private MessageSender messageSender;


    public ERPCustomerOrder getOrderByOrderNumber(String orderNumber) {
        return custOrderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public ERPCustomerOrder create(ERPCustomerOrder customerOrder) {
        ERPAutoNumber auto = autoNumberService.findByName(customerOrder.getOrderType().toString());
        if (auto == null) {
            auto = autoNumberService.findByName("Order Number");
        }

        String orderNumber = autoNumberService.getNextNumber(auto.getId());
        customerOrder.setOrderNumber(orderNumber);
        customerOrder.setOrderedDate(new Date());
        customerOrder.setOrderedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        customerOrder.setStatus(CustomerOrderStatus.NEW);

        if (customerOrder.getShippingAddress() != null &&
                customerOrder.getShippingAddress().getId() != null) {
            ERPAddress address = addressRepository.findOne(customerOrder.getShippingAddress().getId());
            if (address != null) {
                customerOrder.setShippingAddress(address);
            }
        }

        if (customerOrder.getBillingAddress() != null &&
                customerOrder.getBillingAddress().getId() != null) {
            ERPAddress address = addressRepository.findOne(customerOrder.getBillingAddress().getId());
            if (address != null) {
                customerOrder.setBillingAddress(address);
            }
        }

        customerOrder = custOrderRepository.save(customerOrder);
        List<ERPCustomerOrderDetails> details = customerOrder.getDetails();
        if (details != null) {
            for (ERPCustomerOrderDetails item : details) {
                item.setOrderId(customerOrder.getId());
            }

            details = custOrderDetailsRepository.save(details);
            customerOrder.setDetails(details);
        }

        //save to CustomerOrderStatusHistory
        ERPCustomerOrderStatusHistory orderStatus = new ERPCustomerOrderStatusHistory();
        orderStatus.setModifiedDate(new Date());
        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);

        orderStatus.setModifiedBy(employee);
        orderStatus.setNewStatus(customerOrder.getStatus());
        orderStatus.setOldStatus(customerOrder.getStatus());//same as new status
        orderStatus.setOrder(customerOrder);

        orderStatusHistoryRepository.save(orderStatus);

        messageSender.sendNewOrderPushNotification(customerOrder);

        return customerOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ERPCustomerOrder> findAll(Pageable pageable) {
        return custOrderRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ERPCustomerOrder> findAll(Predicate predicate, Pageable pageable) {
        return custOrderRepository.findAll(predicate, pageable);
    }

    @Override
    public ERPCustomerOrder update(ERPCustomerOrder order) {
        checkNotNull(order);
        checkNotNull(order.getId());
        ERPCustomerOrder existingOrder = custOrderRepository.findOne(order.getId());
        List<ERPCustomerOrderDetails> details = order.getDetails();
        if (details != null) {
            for (ERPCustomerOrderDetails item : details) {
                item.setOrderId(existingOrder.getId());
            }
            custOrderDetailsRepository.save(details);
        }

        if (order.getShippingAddress() != null) {
            ERPAddress savedAddress = addressRepository.save(order.getShippingAddress());
            order.setShippingAddress(savedAddress);
        }

        if (order.getBillingAddress() != null) {
            ERPAddress savedAddress = addressRepository.save(order.getBillingAddress());
            order.setBillingAddress(savedAddress);
        }

        order = custOrderRepository.save(order);
        order.setDetails(details);
        return order;
    }


    public ERPCustomerOrderShipment updateInvoiceNumber(ERPCustomerOrderShipment shipment) {
        checkNotNull(shipment);
        checkNotNull(shipment.getId());
        shipment = customerOrderShipmentRepository.save(shipment);
        return shipment;
    }

    @Override
    public void delete(Integer orderId) {
        checkNotNull(orderId);
        custOrderRepository.delete(orderId);
    }


    @Override
    public ERPCustomerOrder get(Integer orderId) {
        return custOrderRepository.findOne(orderId);
    }


    @Override
    public List<ERPCustomerOrder> getAll() {
        return custOrderRepository.findAll();
    }


    public List<ERPCustomerOrderDetails> getAllOrderDetailsByOrderId(
            Integer orderId) {
        return custOrderDetailsRepository.findByOrderId(orderId);
    }

    public List<ERPCustomerOrder> getNewOrders() {
        return custOrderRepository.getNewOrders(CustomerOrderStatus.NEW);
    }

    public ERPCustomerOrder approveOrder(Integer orderId) {
        checkNotNull(orderId);

        ERPCustomerOrder order = custOrderRepository.findOne(orderId);
        if (order != null) {
            Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
            ERPEmployee employee = employeeService.getEmployeeById(id);

            CustomerOrderStatus oldStatus = order.getStatus();

            if (oldStatus != CustomerOrderStatus.APPROVED) {
                order.setStatus(CustomerOrderStatus.APPROVED);
                order = custOrderRepository.save(order);

                //save to CustomerOrderStatusHistory
                ERPCustomerOrderStatusHistory orderStatus = new ERPCustomerOrderStatusHistory();
                orderStatus.setModifiedDate(new Date());

                orderStatus.setModifiedBy(employee);
                orderStatus.setNewStatus(order.getStatus());
                orderStatus.setOldStatus(oldStatus);
                orderStatus.setOrder(order);

                orderStatusHistoryRepository.save(orderStatus);
            }
        } else {
            throw new ResourceNotFoundException("Order " + orderId + " does not exist");
        }

        return order;

    }

    public ERPCustomerOrder cancelOrder(Integer orderId) {
        checkNotNull(orderId);

        ERPCustomerOrder order = custOrderRepository.findOne(orderId);
        if (order != null) {
            Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
            ERPEmployee employee = employeeService.getEmployeeById(id);

            if (order.getStatus() == CustomerOrderStatus.PARTIALLYSHIPPED) {
                cancelPendingItems(order);
            }

            CustomerOrderStatus oldStatus = order.getStatus();
            order.setStatus(CustomerOrderStatus.CANCELLED);
            order = custOrderRepository.save(order);

            //save to CustomerOrderStatusHistory
            ERPCustomerOrderStatusHistory orderStatus = new ERPCustomerOrderStatusHistory();
            orderStatus.setModifiedDate(new Date());

            orderStatus.setModifiedBy(employee);
            orderStatus.setNewStatus(order.getStatus());
            orderStatus.setOldStatus(oldStatus);
            orderStatus.setOrder(order);

            orderStatusHistoryRepository.save(orderStatus);

        } else {
            throw new ResourceNotFoundException("Order " + orderId + " does not exist");
        }

        return order;

    }

    private void cancelPendingItems(ERPCustomerOrder order) {
        List<ERPCustomerOrderDetails> details = getAllOrderDetailsByOrderId(order.getId());

        List<ERPCustomerOrderDetails> itemsToRemove = new ArrayList<>();
        Double orderValue = order.getOrderTotal();

        for (ERPCustomerOrderDetails detail : details) {
            Integer orderQty = detail.getQuantity();
            Integer shippedQty = detail.getQuantityShipped();

            if (shippedQty < orderQty) {
                if (shippedQty == 0) {
                    itemsToRemove.add(detail);
                    Double deduct = detail.getUnitPrice() * orderQty;
                    orderValue -= deduct;
                } else {
                    Integer diff = orderQty - shippedQty;

                    detail.setQuantity(diff);
                    custOrderDetailsRepository.save(detail);
                    Double deduct = detail.getUnitPrice() * diff;
                    orderValue -= deduct;
                }
            }
        }

        custOrderDetailsRepository.delete(itemsToRemove);
        order.setOrderTotal(orderValue);
        order.setNetTotal(orderValue);
    }

    private Boolean checkInventory(ERPCustomerOrder order) {
        List<ERPCustomerOrderDetails> details = getAllOrderDetailsByOrderId(order.getId());
        for (ERPCustomerOrderDetails detail : details) {
            Integer inventory = productInventoryService.getInventoryForProduct(detail.getProduct().getId());
            if (inventory < detail.getQuantity()) {
                return false;
            }
        }

        return true;
    }


    public List<ERPCustomerOrder> approveAllNewOrders() {
        List<ERPCustomerOrder> unApprovedOrders = new ArrayList<>();

        List<ERPCustomerOrder> newOrders = getNewOrders();

        for (ERPCustomerOrder order : newOrders) {
            if (checkInventory(order)) {
                approveOrder(order.getId());
            } else {
                unApprovedOrders.add(order);
            }
        }

        return unApprovedOrders;
    }

    public List<ERPCustomerOrderStatusHistory> getOrderHistory(Integer id) {
        return orderStatusHistoryRepository.findOrdersByOrderId(id);
    }

    public List<ERPCustomerOrderShipment> getOrderShipments(Integer id) {
        ERPCustomerOrder order = custOrderRepository.findOne(id);
        return customerOrderShipmentRepository.findByOrder(order);
    }

    public ERPCustomerOrder processOrder(Integer orderId) {
        ERPCustomerOrder order = custOrderRepository.findOne(orderId);

        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);

        if (employee != null) {
            if (!checkInventory(order)) {
                throw new InventoryException("Order cannot be processed. Inventory on some of the items is low");
            }

            //mark the order as shipped
            CustomerOrderStatus oldStatus = order.getStatus();
            order.setStatus(CustomerOrderStatus.PROCESSED);
            order = custOrderRepository.save(order);

            //Create a history object
            ERPCustomerOrderStatusHistory orderStatus = new ERPCustomerOrderStatusHistory();
            orderStatus.setModifiedDate(new Date());
            orderStatus.setModifiedBy(employee);
            orderStatus.setNewStatus(order.getStatus());
            orderStatus.setOldStatus(oldStatus);
            orderStatus.setOrder(order);
            orderStatusHistoryRepository.save(orderStatus);

            List<ERPCustomerOrderDetails> details = getAllOrderDetailsByOrderId(order.getId());
            for (ERPCustomerOrderDetails detail : details) {
                productInventoryService.stockOut(detail.getProduct().getId(), detail.getQuantity(), null);
            }
        }

        return order;
    }

    public ERPCustomerOrder dispatchOrder(Integer orderId) {
        ERPCustomerOrder order = custOrderRepository.findOne(orderId);

        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);

        if (employee != null) {
            //mark the order as shipped
            CustomerOrderStatus oldStatus = order.getStatus();
            order.setStatus(CustomerOrderStatus.SHIPPED);
            order = custOrderRepository.save(order);

            //Create a history object
            ERPCustomerOrderStatusHistory orderStatus = new ERPCustomerOrderStatusHistory();
            orderStatus.setModifiedDate(new Date());
            orderStatus.setModifiedBy(employee);
            orderStatus.setNewStatus(order.getStatus());
            orderStatus.setOldStatus(oldStatus);
            orderStatus.setOrder(order);
            orderStatusHistoryRepository.save(orderStatus);
        }

        return order;
    }

    public List<ERPCustomerOrder> getCustomerOrders(ERPCustomer customer) {
        return custOrderRepository.getCustomerOrders(customer);
    }

    public List<ERPCustomerOrder> getOrdersForShipments(List<Integer> shipmentIds) {
        Predicate predicate = QERPCustomerOrder.eRPCustomerOrder.shipments.any().id.in(shipmentIds);
        return Utils.makeList(custOrderRepository.findAll(predicate));
    }

    public ERPOrderVerification verifyOrder(Integer orderId, ERPOrderVerification verification) {
        ERPCustomerOrder order = get(orderId);
        verification.setOrder(order);
        verification.setAssignedDate(new Date());

        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);
        verification.setAssignedBy(employee);

        //mark the order as shipped
        CustomerOrderStatus oldStatus = order.getStatus();
        order.setStatus(CustomerOrderStatus.PROCESSING);
        order = custOrderRepository.save(order);

        //Create a history object
        ERPCustomerOrderStatusHistory orderStatus = new ERPCustomerOrderStatusHistory();
        orderStatus.setModifiedDate(new Date());
        orderStatus.setModifiedBy(employee);
        orderStatus.setNewStatus(order.getStatus());
        orderStatus.setOldStatus(oldStatus);
        orderStatus.setOrder(order);
        orderStatusHistoryRepository.save(orderStatus);

        return verificationRepository.save(verification);
    }

    public List<ERPOrderVerification> getOrderVerifications(Integer orderId) {
        ERPCustomerOrder order = get(orderId);
        return verificationRepository.findByOrder(order);
    }

    public void deleteItem(Integer itemId) {
        checkNotNull(itemId);

        ERPCustomerOrderDetails item = custOrderDetailsRepository.findOne(itemId);
        if (item != null) {
            custOrderDetailsRepository.delete(itemId);
        }
    }

    public Page<ERPCustomerOrder> getLateApprovedOrders(Pageable pageable) {
        return custOrderRepository.getLateApprovedOrders(pageable);
    }

    public Page<ERPCustomerOrder> getLateProcessedOrders(Pageable pageable) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR,-2);
        Date twoHrsBack = cal.getTime();
        return custOrderRepository.getLateProcessedOrders(twoHrsBack, pageable);
    }

    public Page<ERPCustomerOrder> getLateShippedOrders(Pageable pageable) {
        return custOrderRepository.getLateShippedOrders(pageable);
    }

}