package com.cassinisys.erp.service.production;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPAutoNumber;
import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrder;
import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrderDetails;
import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrderHistory;
import com.cassinisys.erp.model.production.MaterialPurchaseOrderStatus;
import com.cassinisys.erp.repo.production.MaterialPurchaseOrderDetailsRepository;
import com.cassinisys.erp.repo.production.MaterialPurchaseOrderHistoryRepository;
import com.cassinisys.erp.repo.production.MaterialPurchaseOrderRepository;
import com.cassinisys.erp.service.common.AutoNumberService;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import com.cassinisys.erp.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class MaterialPurchaseOrderService implements
        CrudService<ERPMaterialPurchaseOrder, Integer>,
        PageableService<ERPMaterialPurchaseOrder, Integer> {

    @Autowired
    MaterialPurchaseOrderRepository matPurchaseOrderRepository;

    @Autowired
    MaterialPurchaseOrderDetailsRepository matPurchOrderDetailsRepository;

    @Autowired
    MaterialPurchaseOrderHistoryRepository orderStatusHistoryRepository;

    @Autowired
    AutoNumberService autoNumberService;

    @Autowired
    SessionWrapper sessionWrapper;

    @Override
    public ERPMaterialPurchaseOrder create(
            ERPMaterialPurchaseOrder matPurchaseOrder) {

        ERPAutoNumber auto = autoNumberService.findByName(matPurchaseOrder.getOrderType().toString());
        if (auto == null) {
            auto = autoNumberService.findByName("Order Number");
        }

        String orderNumber = autoNumberService.getNextNumber(auto.getId());

        ERPMaterialPurchaseOrder order = null;
        matPurchaseOrder.setOrderNumber(orderNumber);
        matPurchaseOrder.setOrderedDate(new Date());
        matPurchaseOrder.setStatus(MaterialPurchaseOrderStatus.CREATED);
        matPurchaseOrder.setOrderedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        order = matPurchaseOrderRepository.save(matPurchaseOrder);

        // save to CustomerOrderStatusHistory

        ERPMaterialPurchaseOrderHistory orderStatus = new ERPMaterialPurchaseOrderHistory();
        orderStatus.setNotes("");// later get this by session
        orderStatus.setTimestamp(new Date());
        orderStatus.setNewStatus(order.getStatus());
        orderStatus.setOldStatus(order.getStatus());// same as new status
        orderStatus.setPurchaseOrder(order.getId());

        orderStatusHistoryRepository.save(orderStatus);

        return order;
    }

    public List<ERPMaterialPurchaseOrder> createPOs(
            List<ERPMaterialPurchaseOrder> matPurchaseOrders) {
        List<ERPMaterialPurchaseOrder> matPOs = new ArrayList();
        for (int i = 0; i < matPurchaseOrders.size(); i++) {
            ERPMaterialPurchaseOrder matPurchaseOrder = matPurchaseOrders.get(i);

            ERPAutoNumber auto = autoNumberService.findByName(matPurchaseOrder.getOrderType().toString());
            if (auto == null) {
                auto = autoNumberService.findByName("Order Number");
            }

            String orderNumber = autoNumberService.getNextNumber(auto.getId());

            ERPMaterialPurchaseOrder order = null;
            matPurchaseOrder.setOrderNumber(orderNumber);
            matPurchaseOrder.setOrderedDate(new Date());
            matPurchaseOrder.setStatus(MaterialPurchaseOrderStatus.CREATED);
            matPurchaseOrder.setOrderedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
            order = matPurchaseOrderRepository.save(matPurchaseOrder);

            // save to CustomerOrderStatusHistory

            ERPMaterialPurchaseOrderHistory orderStatus = new ERPMaterialPurchaseOrderHistory();
            orderStatus.setNotes("");// later get this by session
            orderStatus.setTimestamp(new Date());
            orderStatus.setNewStatus(order.getStatus());
            orderStatus.setOldStatus(order.getStatus());// same as new status
            orderStatus.setPurchaseOrder(order.getId());

            orderStatusHistoryRepository.save(orderStatus);
            matPOs.add(order);
        }


        return matPOs;
    }

    public Boolean deliverOrder(Integer approvedByEmp, Integer orderId) {

        Boolean isDelivered = false;
        MaterialPurchaseOrderStatus oldStatus;

        if (orderId > 0) {

            ERPMaterialPurchaseOrder order = matPurchaseOrderRepository
                    .findOne(orderId);
            if (order != null) {
                oldStatus = order.getStatus();
                order.setStatus(MaterialPurchaseOrderStatus.DELIVERED);
                order.setApprovedBy(approvedByEmp);
                matPurchaseOrderRepository.save(order);
                isDelivered = true;

                // save to ERPMaterialPurchaseOrderHistory
                ERPMaterialPurchaseOrderHistory orderStatus = new ERPMaterialPurchaseOrderHistory();
                orderStatus.setNotes("delivered today");
                orderStatus.setTimestamp(new Date());
                orderStatus.setNewStatus(order.getStatus());
                orderStatus.setOldStatus(oldStatus);
                orderStatus.setPurchaseOrder(order.getId());

                orderStatusHistoryRepository.save(orderStatus);

            } else {
                throw new ERPException();
            }
        }
        return isDelivered;

    }


    public Boolean approveOrder(Integer approvedByEmp, Integer orderId) {

        Boolean isApproved = false;
        MaterialPurchaseOrderStatus oldStatus;

        if (orderId > 0) {

            ERPMaterialPurchaseOrder order = matPurchaseOrderRepository
                    .findOne(orderId);
            if (order != null) {
                oldStatus = order.getStatus();
                order.setStatus(MaterialPurchaseOrderStatus.APPROVED);
                order.setApprovedBy(approvedByEmp);
                matPurchaseOrderRepository.save(order);
                isApproved = true;

                // save to ERPMaterialPurchaseOrderHistory
                ERPMaterialPurchaseOrderHistory orderStatus = new ERPMaterialPurchaseOrderHistory();
                orderStatus.setNotes("Approved today");
                orderStatus.setTimestamp(new Date());
                orderStatus.setNewStatus(order.getStatus());
                orderStatus.setOldStatus(oldStatus);
                orderStatus.setPurchaseOrder(order.getId());

                orderStatusHistoryRepository.save(orderStatus);

            } else {
                throw new ERPException();
            }
        }
        return isApproved;

    }


    @Override
    public Page<ERPMaterialPurchaseOrder> findAll(Pageable pageable) {

        return matPurchaseOrderRepository.findAll(pageable);
    }

    @Override
    public ERPMaterialPurchaseOrder update(ERPMaterialPurchaseOrder order) {
        checkNotNull(order);
        checkNotNull(order.getId());
        ERPMaterialPurchaseOrder purchaseOrder = matPurchaseOrderRepository
                .findOne(order.getId());

        Set<ERPMaterialPurchaseOrderDetails> details = purchaseOrder.getDetails();

        if (details != null) {
            for (ERPMaterialPurchaseOrderDetails details1 : details) {
                matPurchOrderDetailsRepository.delete(details1.getRowId());
            }
        }

        if (purchaseOrder == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        // below two fields are not coming from UI and assuming all other ids
        // will come.
        order.setOrderedDate(purchaseOrder.getOrderedDate());
        order.setStatus(purchaseOrder.getStatus());
        return matPurchaseOrderRepository.save(order);
    }

    @Override
    public void delete(Integer orderId) {
        checkNotNull(orderId);
        matPurchaseOrderRepository.delete(orderId);
    }


    @Override
    public ERPMaterialPurchaseOrder get(Integer orderId) {
        return matPurchaseOrderRepository.findOne(orderId);

    }

    @Override
    public List<ERPMaterialPurchaseOrder> getAll() {
        return matPurchaseOrderRepository.findAll();
    }


    public List<ERPMaterialPurchaseOrderDetails> getAllMaterialPurchaseOrderDetailsByOrderId(
            Integer orderId) {

        List<ERPMaterialPurchaseOrderDetails> materialPurchaseOrderDetails = null;

        ERPMaterialPurchaseOrder order = new ERPMaterialPurchaseOrder();
        order.setId(orderId);
        materialPurchaseOrderDetails = matPurchOrderDetailsRepository
                .findBymaterialPurchaseOrder(order);

        return materialPurchaseOrderDetails;
    }

    public List<ERPMaterialPurchaseOrder> getByMaterialIdAndIssuedFalse(Integer integer, Boolean issued) {
        List<ERPMaterialPurchaseOrderDetails> orderDetailses = null;
        List<ERPMaterialPurchaseOrder> orders = new ArrayList();
        if (issued) {
            orderDetailses = matPurchOrderDetailsRepository.findByMaterialIdAndIssuedTrue(integer);
        } else if (!issued) {
            orderDetailses = matPurchOrderDetailsRepository.findByMaterialIdAndIssuedFalse(integer);
        }

        for (ERPMaterialPurchaseOrderDetails details : orderDetailses) {
            orders.add(details.getMaterialPurchaseOrder());
        }
        return orders;
    }

    public ERPMaterialPurchaseOrderDetails getMaterialPODetailsByMaterialAndMaterialPurchaseOrder(Integer materialId, Integer poId){
        return matPurchOrderDetailsRepository.findByMaterialIdAndMaterialPurchaseOrderId(materialId, poId);
    }


}
