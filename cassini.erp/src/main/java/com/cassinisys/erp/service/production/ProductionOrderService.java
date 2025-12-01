package com.cassinisys.erp.service.production;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.common.ERPAutoNumber;
import com.cassinisys.erp.model.production.*;
import com.cassinisys.erp.repo.production.*;
import com.cassinisys.erp.service.common.AutoNumberService;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class ProductionOrderService implements CrudService<ERPProductionOrder, Integer>, PageableService<ERPProductionOrder, Integer> {

    @Autowired
    ProductionOrderRepository prodOrderRepository;

    @Autowired
    AutoNumberService autoNumberService;

    @Autowired
    ProductionItemBomRepository productionItemBomRepository;

    @Autowired
    ProcessStepInstanceRepository processStepInstanceRepository;

    @Autowired
    ProcessInstanceRepository processInstanceRepository;


    @Autowired
    ProductionOrderDetailsRepository prodOrderDetailsRepository;

    @Autowired
    ProductOrderHistoryRepository orderStatusHistoryRepository;

    @Autowired
    ProcessStepRepository processStepRepository;

    @Override
    public ERPProductionOrder create(ERPProductionOrder productionOrder) {

        ERPAutoNumber auto = autoNumberService.findByName(productionOrder.getOrderType().toString());
        if (auto == null) {
            auto = autoNumberService.findByName("Order Number");
        }

        String orderNumber = autoNumberService.getNextNumber(auto.getId());

        ERPProductionOrder order = null;
        productionOrder.setOrderNumber(orderNumber);
        productionOrder.setOrderedDate(new Date());
        productionOrder.setStatus(ProductionOrderStatus.CREATED);
        order = prodOrderRepository.save(productionOrder);

        ERPProductionOrderHistory orderStatus = new ERPProductionOrderHistory();

        orderStatus.setTimestamp(new Date());
        orderStatus.setNewStatus(ProductionOrderStatus.CREATED);
        orderStatus.setOldStatus(ProductionOrderStatus.CREATED);//same as new status
        orderStatus.setProductionOrder(order.getId());

        orderStatusHistoryRepository.save(orderStatus);

        List<ERPProductionOrderItem> orderItems = (List<ERPProductionOrderItem>) productionOrder.getDetails();
        for (ERPProductionOrderItem orderItem : orderItems) {
            ERPProcess erpProcess = orderItem.getProcess();

            // copy of ProcessStepInstance From Process Step

            List<ERPProcessStep> processSteps = (List<ERPProcessStep>) processStepRepository.getOne(erpProcess.getId());
            for (ERPProcessStep processStep : processSteps) {

                ERPProcessStepInstance erpProcessStepInstance = new ERPProcessStepInstance();

                erpProcessStepInstance.setDescription(processStep.getDescription());
                erpProcessStepInstance.setName(processStep.getName());
                erpProcessStepInstance.setSequenceNumber(processStep.getSequenceNumber());
                erpProcessStepInstance.setProcess(erpProcess.getId());
                ERPWorkCenter erpWorkCenter = processStep.getWorkcenter();
                erpProcessStepInstance.setWorkCenter(erpWorkCenter.getId());

                processStepInstanceRepository.save(erpProcessStepInstance);


                //Copy of ProcessInstance From Process

                ERPProcessInstance erpProcessInstance = new ERPProcessInstance();

                erpProcessInstance.setItem(orderItem.getItemId());
                erpProcessInstance.setProductionOrder(order.getId());
                erpProcessInstance.setName(erpProcess.getName());
                erpProcessInstance.setDescription(erpProcess.getDescription());

                processInstanceRepository.save(erpProcessInstance);

            }
        }
        return order;

    }

    public Boolean approveOrder(Integer approvedBy, Integer orderId) {
        Boolean isApproved = false;
        ProductionOrderStatus oldStatus = null;


        if (orderId > 0) {

            ERPProductionOrder order = prodOrderRepository.findOne(orderId);
            if (order != null) {
                oldStatus = order.getStatus();
                order.setStatus(ProductionOrderStatus.APPROVED);
                order.setApprovedBy(approvedBy);
                prodOrderRepository.save(order);
                isApproved = true;

                ERPProductionOrderHistory orderStatus = new ERPProductionOrderHistory();

                orderStatus.setTimestamp(new Date());
                orderStatus.setNewStatus(ProductionOrderStatus.APPROVED);
                orderStatus.setOldStatus(oldStatus);
                orderStatus.setProductionOrder(order.getId());

                orderStatusHistoryRepository.save(orderStatus);

            } else {
                throw new ERPException();
            }
        }
        return isApproved;

    }

    @Override
    public Page<ERPProductionOrder> findAll(Pageable pageable) {
        return prodOrderRepository.findAll(pageable);
    }


    @Override
    public ERPProductionOrder update(ERPProductionOrder order) {
        checkNotNull(order);
        checkNotNull(order.getId());
        ERPProductionOrder prodOrder = prodOrderRepository.findOne(order.getId());
        if (prodOrder == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        boolean isExists = false;
        for (ERPProductionOrderItem detail : prodOrder.getDetails()) {
            isExists = false;
            if (detail.getItemId() > 0) {
                for (ERPProductionOrderItem det : order.getDetails()) {

                    if (detail.getItemId() == det.getItemId()) {
                        isExists = true;
                    }
                }
                if (!isExists) {

                    prodOrderDetailsRepository.delete(detail);
                }
            } else {


            }

        }

        for (ERPProductionOrderItem detail : order.getDetails()) {

            if (detail.getBom() != null && detail.getOldBom() != null)
                if (!(detail.getBom().getBomId() == detail.getOldBom().getBomId())) {

                    ERPProductionOrderItem oldDetail = prodOrderDetailsRepository.findOne(detail.getItemId());

                    for (ERPProductionItemBom itemBOM : oldDetail.getItemBoms()) {

                        productionItemBomRepository.delete(itemBOM);
                    }

                }

        }
        //below two fields are not coming from UI and assuming all other ids will come.

        if (order.getStatus().equals(ProductionOrderStatus.CREATED)) {
            order.setOrderedDate(prodOrder.getOrderedDate());
            //order.sssetStatus(prodOrder.getStatus());
        } else if (order.getStatus().equals(ProductionOrderStatus.APPROVED)) {
            order.setOrderedDate(prodOrder.getOrderedDate());
            order.setApprovedDate(new Date());
        } else if (order.getStatus().equals(ProductionOrderStatus.INPRODUCTION)) {
            order.setOrderedDate(prodOrder.getOrderedDate());
            order.setApprovedDate(prodOrder.getApprovedDate());
            order.setStartDate(new Date());
        } else if (order.getStatus().equals(ProductionOrderStatus.COMPLETED)) {
            order.setOrderedDate(prodOrder.getOrderedDate());
            order.setApprovedDate(prodOrder.getApprovedDate());
            order.setStartDate(prodOrder.getStartDate());
            order.setCompletedDate(new Date());
        }

        return prodOrderRepository.save(order);
    }


    @Override
    public void delete(Integer orderId) {
        checkNotNull(orderId);
        prodOrderRepository.delete(orderId);
    }


    @Override
    public ERPProductionOrder get(Integer orderId) {
        return prodOrderRepository.findOne(orderId);

    }


    @Override
    public List<ERPProductionOrder> getAll() {
        return prodOrderRepository.findAll();
    }


    public List<ERPProductionOrderItem> getAllOrderDetailsByOrderId(
            Integer orderId) {

        List<ERPProductionOrderItem> prodOrderDetails = null;

        ERPProductionOrder order = new ERPProductionOrder();
        order.setId(orderId);
        prodOrderDetails = prodOrderDetailsRepository
                .findByProductionOrder(order);

        return prodOrderDetails;
    }

}
