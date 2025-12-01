package com.cassinisys.erp.api.crm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.api.filtering.CustomerOrderPredicateBuilder;
import com.cassinisys.erp.api.filtering.OrderVerificationPredicateBuilder;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.CustomerOrderCriteria;
import com.cassinisys.erp.model.api.criteria.OrderVerificationCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.model.crm.QERPCustomerOrder;
import com.cassinisys.erp.model.crm.QERPOrderVerification;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.repo.crm.CustomerOrderVerificationRepository;
import com.cassinisys.erp.service.crm.CustomerOrderService;
import com.cassinisys.erp.service.hrm.EmployeeService;
import com.cassinisys.erp.service.print.PrintService;
import com.cassinisys.erp.service.security.PermissionCheck;
import com.cassinisys.erp.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("crm/customerorders")
@Api(name = "CustomerOrders", description = "CustomerOrders endpoint", group = "CRM")
public class CustomerOrderController extends BaseController {
    private SessionWrapper sessionWrapper;
    private CustomerOrderPredicateBuilder predicateBuilder;
    private OrderVerificationPredicateBuilder verificationPredicateBuilder;
    private CustomerOrderService customerOrderService;
    private ERPCustomerOrderShipment customerOrderShipment;
    private PageRequestConverter pageRequestConverter;
    private EmployeeService employeeService;
    private PrintService printService;
    private CustomerOrderVerificationRepository verificationRepository;

    @Autowired
    public CustomerOrderController(SessionWrapper sessionWrapper,
                                   CustomerOrderService customerOrderService,
                                   PageRequestConverter pageRequestConverter,
                                   CustomerOrderPredicateBuilder predicateBuilder,
                                   EmployeeService employeeService,
                                   PrintService printService,
                                   OrderVerificationPredicateBuilder verificationPredicateBuilder,
                                   CustomerOrderVerificationRepository verificationRepository) {
        this.sessionWrapper = sessionWrapper;
        this.pageRequestConverter = pageRequestConverter;
        this.customerOrderService = customerOrderService;
        this.predicateBuilder = predicateBuilder;
        this.employeeService = employeeService;
        this.printService = printService;
        this.verificationPredicateBuilder = verificationPredicateBuilder;
        this.verificationRepository = verificationRepository;
    }

    @PermissionCheck(permissions = {"permission.crm.order.create"})
    @RequestMapping(method = RequestMethod.POST)
    public ERPCustomerOrder createCustomerOrder(
            @RequestBody @Valid ERPCustomerOrder customerOrder,
            HttpServletRequest request, HttpServletResponse response) {
        return customerOrderService.create(customerOrder);
    }

    @PermissionCheck(permissions = {"permission.crm.order.view"})
    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public ERPCustomerOrder getCustomerOrderById(
            @PathVariable("orderId") Integer orderId) {
        return customerOrderService.get(orderId);
    }

    @PermissionCheck(permissions = {"permission.crm.order.view"})
    @RequestMapping(value = "/{orderId}/history", method = RequestMethod.GET)
    public List<ERPCustomerOrderStatusHistory> getOrderHistory(
            @PathVariable("orderId") Integer orderId) {
        return customerOrderService.getOrderHistory(orderId);
    }

    @PermissionCheck(permissions = {"permission.crm.order.view"})
    @RequestMapping(value = "/{orderId}/shipments", method = RequestMethod.GET)
    public List<ERPCustomerOrderShipment> getOrderShipments(
            @PathVariable("orderId") Integer orderId) {
        return customerOrderService.getOrderShipments(orderId);
    }

    //@PermissionCheck(permissions = {"permission.crm.order.update"})
    @RequestMapping(value = "/{orderId}", method = RequestMethod.PUT)
    public ERPCustomerOrder update(@PathVariable("orderId") Integer orderId,
                                   @RequestBody ERPCustomerOrder customerOrder) {
        return customerOrderService.update(customerOrder);
    }


    @RequestMapping(value = "/shipment/{shipmentId}", method = RequestMethod.PUT)
    public ERPCustomerOrderShipment updateInvoiceNumber(@PathVariable("shipmentId") Integer shipmentId,
                                                        @RequestBody ERPCustomerOrderShipment shipment) {
        return customerOrderService.updateInvoiceNumber(shipment);
    }


    @PermissionCheck(permissions = {"permission.crm.order.view"})
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public List<ERPCustomerOrder> getNewOrders() {
        return customerOrderService.getNewOrders();
    }


    @PermissionCheck(permissions = {"permission.crm.order.approve"})
    @RequestMapping(value = "/{orderId}/approve", method = RequestMethod.GET)
    public ERPCustomerOrder approveOrder(
            @PathVariable("orderId") Integer orderId) {
        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return customerOrderService.approveOrder(orderId);
        } else {
            throw new ERPException(HttpStatus.UNAUTHORIZED, ErrorCodes.GENERAL,
                    "You are not authorized to approve orders");
        }
    }

    @PermissionCheck(permissions = {"permission.crm.order.approve"})
    @RequestMapping(value = "/{orderId}/cancel", method = RequestMethod.GET)
    public ERPCustomerOrder cancelOrder(
            @PathVariable("orderId") Integer orderId) {
        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return customerOrderService.cancelOrder(orderId);
        } else {
            throw new ERPException(HttpStatus.UNAUTHORIZED, ErrorCodes.GENERAL,
                    "You are not authorized to cancel orders");
        }
    }

    @RequestMapping(value = "/{orderId}/verify", method = RequestMethod.POST)
    public ERPOrderVerification beginOrderProcessing(@PathVariable("orderId") Integer orderId,
                                                     @RequestBody @Valid ERPOrderVerification verificationInfo) {
        return customerOrderService.verifyOrder(orderId, verificationInfo);
    }

    @RequestMapping(value = "/{orderId}/verifications", method = RequestMethod.GET)
    public List<ERPOrderVerification> getVerifications(@PathVariable("orderId") Integer orderId) {
        return customerOrderService.getOrderVerifications(orderId);
    }


    @PermissionCheck(permissions = {"permission.crm.order.approve"})
    @RequestMapping(value = "/new/approveall", method = RequestMethod.GET)
    public List<ERPCustomerOrder> approveAllNewOrders() {
        return customerOrderService.approveAllNewOrders();
    }


    @PermissionCheck(permissions = {"permission.crm.order.view"})
    @RequestMapping(method = RequestMethod.GET)
    public Page<ERPCustomerOrder> find(CustomerOrderCriteria criteria,
                                       ERPPageRequest pageRequest) {
        Predicate predicate = predicateBuilder.build(criteria,
                QERPCustomerOrder.eRPCustomerOrder);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customerOrderService.findAll(predicate, pageable);
    }

    @PermissionCheck(permissions = {"permission.crm.order.view"})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<ERPCustomerOrder> search(CustomerOrderCriteria criteria,
                                         ERPPageRequest pageRequest) {
        criteria.setFreeTextSearch(true);
        Predicate predicate = predicateBuilder.build(criteria,
                QERPCustomerOrder.eRPCustomerOrder);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customerOrderService.findAll(predicate, pageable);
    }

    @PermissionCheck(permissions = {"permission.crm.order.view"})
    @RequestMapping(value = "/{orderId}/print", method = RequestMethod.GET)
    public void printConsignment(@PathVariable("orderId") Integer orderId, HttpServletResponse response) {
        try {
            String html = printService.printOrder(orderId);
            response.setContentType("text/html");
            response.getWriter().write(html);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PermissionCheck(permissions = {"permission.crm.order.delete"})
    @RequestMapping(value = "/{orderId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("orderId") Integer orderId) {
        customerOrderService.delete(orderId);
    }

    @PermissionCheck(permissions = {"permission.crm.order.dispatch"})
    @RequestMapping(value = "/{orderId}/dispatch", method = RequestMethod.GET)
    public ERPCustomerOrder dispatchOrder(@PathVariable("orderId") Integer orderId) {
        return customerOrderService.dispatchOrder(orderId);
    }

    @PermissionCheck(permissions = {"permission.crm.order.dispatch"})
    @RequestMapping(value = "/{orderId}/process", method = RequestMethod.GET)
    public ERPCustomerOrder processOrder(@PathVariable("orderId") Integer orderId) {
        return customerOrderService.processOrder(orderId);
    }

    @PermissionCheck(permissions = {"permission.crm.order.view"})
    @RequestMapping(value = "/{orderId}/details", method = RequestMethod.GET)
    public List<ERPCustomerOrderDetails> getAllOrderDetailsForOrder(
            @PathVariable("orderId") Integer orderId) {
        return customerOrderService.getAllOrderDetailsByOrderId(orderId);
    }

    @PermissionCheck(permissions = {"permission.crm.order.view"})
    @RequestMapping(value = "/{orderId}/items", method = RequestMethod.GET)
    public List<ERPCustomerOrderDetails> getOrderItems(
            @PathVariable("orderId") Integer orderId) {
        return customerOrderService.getAllOrderDetailsByOrderId(orderId);
    }

    @RequestMapping(value = "/shipments/[{shipmentIds}]", method = RequestMethod.GET)
    public List<ERPCustomerOrder> getOrdersForShipments(@PathVariable Integer[] shipmentIds) {
        return customerOrderService.getOrdersForShipments(Arrays.asList(shipmentIds));
    }

    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable("itemId") Integer itemId) {
        customerOrderService.deleteItem(itemId);
    }

    @RequestMapping(value = "/verifications", method = RequestMethod.GET)
    public Page<ERPOrderVerification> getOrderVerifications(OrderVerificationCriteria criteria, ERPPageRequest pageRequest) {
        Predicate predicate = verificationPredicateBuilder.build(criteria, QERPOrderVerification.eRPOrderVerification);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return verificationRepository.findAll(predicate, pageable);
    }

    @RequestMapping(value = "/lateapprovedorders", method = RequestMethod.GET)
    public Page<ERPCustomerOrder> getLateApprovedOrders(ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customerOrderService.getLateApprovedOrders(pageable);
    }

    @RequestMapping(value = "/lateprocessedorders", method = RequestMethod.GET)
    public Page<ERPCustomerOrder> getLateProcessedOrders(ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customerOrderService.getLateProcessedOrders(pageable);
    }


    @RequestMapping(value = "/lateshippedorders", method = RequestMethod.GET)
    public Page<ERPCustomerOrder> getLateShippedOrders(ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customerOrderService.getLateShippedOrders(pageable);
    }
}
