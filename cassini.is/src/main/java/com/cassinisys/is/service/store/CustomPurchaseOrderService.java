package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.CustomPurchaseOrderCriteria;
import com.cassinisys.is.filtering.CustomPurchaseOrderPredicateBuilder;
import com.cassinisys.is.model.procm.ISSupplier;
import com.cassinisys.is.model.store.CustomPurchaseOrder;
import com.cassinisys.is.model.store.CustomPurchaseOrderItem;
import com.cassinisys.is.model.store.QCustomPurchaseOrder;
import com.cassinisys.is.repo.procm.SupplierRepository;
import com.cassinisys.is.repo.store.CustomPurchaseOrderItemRepository;
import com.cassinisys.is.repo.store.CustomPurchaseOrderRepository;
import com.cassinisys.is.repo.store.ISTopStoreRepository;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Service
public class CustomPurchaseOrderService implements CrudService<CustomPurchaseOrder, Integer> {

    /*  adding dependencies */

    @Autowired
    CustomPurchaseOrderRepository customPurchaseOrderRepository;

    @Autowired
    CustomPurchaseOrderItemRepository customPurchaseOrderItemRepository;
    @Autowired
    ISTopStoreRepository isTopStoreRepository;
    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    CustomPurchaseOrderPredicateBuilder purchaseOrderPredicateBuilder;
    @Autowired
    private AutoNumberService autoNumberService;

    /*  methods for CustomPurchaseOrder */

    @Transactional(readOnly = false)
    @Override
    public CustomPurchaseOrder create(CustomPurchaseOrder customPurchaseOrder) {
        List<CustomPurchaseOrderItem> customPurchaseOrderItems = new ArrayList<>();
        if (customPurchaseOrder.getPoNumber() == null) {
            AutoNumber autoNumber = autoNumberService.getByName("Default Purchase Order Number Source");
            String number = autoNumberService.getNextNumber(autoNumber.getId());
            customPurchaseOrder.setPoNumber(number);
        }
        CustomPurchaseOrder customPurchaseOrder1 = customPurchaseOrderRepository.save(customPurchaseOrder);
        if (customPurchaseOrder.getCustomPurchaseOrdersItems().size() > 0) {
            for (CustomPurchaseOrderItem customPurchaseOrderItem : customPurchaseOrder.getCustomPurchaseOrdersItems()) {
                customPurchaseOrderItem.setCustomPurchaseOrder(customPurchaseOrder1);
                customPurchaseOrderItems.add(customPurchaseOrderItem);
            }
            createPurchaseOrderItems(customPurchaseOrderItems);
        }
        return customPurchaseOrder1;
    }

    @Override
    @Transactional(readOnly = false)
    public CustomPurchaseOrder update(CustomPurchaseOrder customPurchaseOrder) {
        List<CustomPurchaseOrderItem> customPurchaseOrderItems = new ArrayList<>();
        CustomPurchaseOrder customPurchaseOrder1 = customPurchaseOrderRepository.save(customPurchaseOrder);
        if (customPurchaseOrder.getCustomPurchaseOrdersItems().size() > 0) {
            for (CustomPurchaseOrderItem customPurchaseOrderItem : customPurchaseOrder.getCustomPurchaseOrdersItems()) {
                customPurchaseOrderItem.setCustomPurchaseOrder(customPurchaseOrder1);
                customPurchaseOrderItems.add(customPurchaseOrderItem);
            }
            List<CustomPurchaseOrderItem> purchaseOrderItems = updatePurchaseOrderItems(customPurchaseOrderItems);
            customPurchaseOrder1.setCustomPurchaseOrdersItems(purchaseOrderItems);
        }
        return customPurchaseOrder1;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer customPurchaseOrderId) {
        customPurchaseOrderRepository.delete(customPurchaseOrderId);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPurchaseOrder get(Integer customPurchaseOrderId) {
        List<CustomPurchaseOrderItem> purchaseOrderItems = new ArrayList<>();
        CustomPurchaseOrder customPurchaseOrder = customPurchaseOrderRepository.findOne(customPurchaseOrderId);
        purchaseOrderItems = customPurchaseOrderItemRepository.findByCustomPurchaseOrderId(customPurchaseOrderId);
        if (purchaseOrderItems.size() > 0) {
            customPurchaseOrder.setCustomPurchaseOrdersItems(purchaseOrderItems);
        }
        return customPurchaseOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomPurchaseOrder> getAll() {
        return customPurchaseOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CustomPurchaseOrder> getPageablePurchaseOrders(Integer storeId, Pageable pageable) {
        return customPurchaseOrderRepository.findByStore(pageable, storeId);
    }

    @Transactional(readOnly = true)
    public Page<CustomPurchaseOrder> getAllPurchaseOrders(Pageable pageable) {
        return customPurchaseOrderRepository.findAll(pageable);
    }

    public Page<CustomPurchaseOrder> customIndentFreeTextSearch(Pageable pageable, CustomPurchaseOrderCriteria criteria) {
        Predicate predicate = purchaseOrderPredicateBuilder.build(criteria, QCustomPurchaseOrder.customPurchaseOrder);
        return customPurchaseOrderRepository.findAll(predicate, pageable);
    }

     /*  methods for CustomPurchaseOrderItem */

    @Transactional(readOnly = false)
    public List<CustomPurchaseOrderItem> createPurchaseOrderItems(List<CustomPurchaseOrderItem> purchaseOrderItems) {
        return customPurchaseOrderItemRepository.save(purchaseOrderItems);
    }

    @Transactional(readOnly = false)
    public List<CustomPurchaseOrderItem> updatePurchaseOrderItems(List<CustomPurchaseOrderItem> purchaseOrderItems) {
        return customPurchaseOrderItemRepository.save(purchaseOrderItems);
    }

    @Transactional(readOnly = false)
    public CustomPurchaseOrderItem updatePurchaseOrderItem(Integer poId, CustomPurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItem.setCustomPurchaseOrder(get(poId));
        return customPurchaseOrderItemRepository.save(purchaseOrderItem);
    }

    @Transactional(readOnly = false)
    public void deletePurchaseOrderItem(Integer purchaseOrderItemId) {
        customPurchaseOrderItemRepository.delete(purchaseOrderItemId);
    }

    @Transactional(readOnly = true)
    public CustomPurchaseOrderItem getPurchaseOrderItem(Integer purchaseOrderItemId) {
        return customPurchaseOrderItemRepository.findOne(purchaseOrderItemId);
    }

    @Transactional(readOnly = true)
    public List<CustomPurchaseOrderItem> getAllPurchaseOrderItems() {
        return customPurchaseOrderItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ISSupplier getSupplierByPurchaseOrderId(Integer poId) {
        CustomPurchaseOrder purchaseOrder = customPurchaseOrderRepository.findOne(poId);
        ISSupplier supplier = supplierRepository.findByName(purchaseOrder.getSupplier());
        return supplier;
    }

    @Transactional(readOnly = true)
    public List<CustomPurchaseOrder> getPurchaseOrdersByIds(List<Integer> poIds) {
        return customPurchaseOrderRepository.findAll(poIds);
    }
}
