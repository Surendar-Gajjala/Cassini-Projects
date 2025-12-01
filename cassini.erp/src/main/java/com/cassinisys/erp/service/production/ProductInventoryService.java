package com.cassinisys.erp.service.production;

import com.cassinisys.erp.model.crm.ERPCustomerOrderShipment;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.model.production.ERPProductInventory;
import com.cassinisys.erp.model.production.ERPProductInventoryHistory;
import com.cassinisys.erp.model.production.InventoryType;
import com.cassinisys.erp.repo.production.ProductInventoryHistoryRepository;
import com.cassinisys.erp.repo.production.ProductInventoryRepository;
import com.cassinisys.erp.service.hrm.EmployeeService;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import com.cassinisys.erp.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by reddy on 9/7/15.
 */
@Service
@Transactional
public class ProductInventoryService implements CrudService<ERPProductInventory, Integer>,
        PageableService<ERPProductInventory, Integer> {
    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private ProductInventoryHistoryRepository productInventoryHistoryRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmployeeService employeeService;


    @Override
    public ERPProductInventory create(ERPProductInventory erpProductInventory) {
        return productInventoryRepository.save(erpProductInventory);
    }

    @Override
    public ERPProductInventory update(ERPProductInventory erpProductInventory) {
        return productInventoryRepository.save(erpProductInventory);
    }

    public ERPProductInventory updateProductRestock(ERPProductInventory productRestock) {
        checkNotNull(productRestock);
        checkNotNull(productRestock.getRowId());
        productRestock = productInventoryRepository.save(productRestock);
        return productRestock;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public ERPProductInventory get(Integer id) {
        return productInventoryRepository.findOne(id);
    }

    @Override
    public List<ERPProductInventory> getAll() {
        return productInventoryRepository.findAll();
    }

    @Override
    public Page<ERPProductInventory> findAll(Pageable pageable) {
        return productInventoryRepository.findAll(pageable);
    }

    public ERPProductInventory getProductInventory(Integer productId) {
        return productInventoryRepository.getProductInventoryByProductId(productId);
    }

    public List<ERPProductInventory> getProductsInventory(List<Integer> products) {
        return productInventoryRepository.getProductsInventory(products);
    }


    public ERPProductInventoryHistory stockIn(Integer productId, Integer quantity) {
        ERPProductInventory inventory = getProductInventory(productId);
        if (inventory == null) {
            inventory = new ERPProductInventory();
            inventory.setProduct(productService.get(productId));
        }

        Integer i = inventory.getInventory();
        if (i == null) {
            i = quantity;
        } else {
            i = i + quantity;
        }

        inventory.setInventory(i);
        productInventoryRepository.save(inventory);

        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);

        ERPProductInventoryHistory history = new ERPProductInventoryHistory();
        history.setProduct(productId);
        history.setQuantity(quantity);
        history.setType(InventoryType.STOCKIN);
        history.setEmployee(employee);
        history.setTimestamp(new Date());

        return productInventoryHistoryRepository.save(history);
    }

    public ERPProductInventoryHistory stockOut(Integer productId, Integer quantity, ERPCustomerOrderShipment shipment) {
        ERPProductInventory inventory = getProductInventory(productId);
        if (inventory != null && quantity > 0) {
            Integer i = inventory.getInventory();
            if (i == null || i == 0 || i < quantity) {
                System.out.println("Inventory is too low product:" + productId + " inventory: " + i + " quantity:" + quantity);
                throw new RuntimeException("Inventory is too low");
            }
            i = i - quantity;

            inventory.setInventory(i);
            productInventoryRepository.save(inventory);

            Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
            ERPEmployee employee = employeeService.getEmployeeById(id);

            ERPProductInventoryHistory history = new ERPProductInventoryHistory();
            history.setProduct(productId);
            history.setType(InventoryType.STOCKOUT);
            history.setQuantity(quantity);
            history.setEmployee(employee);
            history.setTimestamp(new Date());
            if (shipment != null) {
                history.setReference(shipment.getId());
            }

            return productInventoryHistoryRepository.save(history);
        }

        return null;
    }

    public Integer getInventoryForProduct(Integer productId) {
        Integer count = 0;
        ERPProductInventory inventory = getProductInventory(productId);
        if (inventory != null) {
            count = inventory.getInventory();
        }

        return count;
    }

    public Page<ERPProductInventoryHistory> getProductInventoryHistory(Integer productId, Pageable pageable) {
        return productInventoryHistoryRepository.getProductInventoryHistory(productId, pageable);
    }

    public Page<ERPProductInventory> getLowInventoryItems(Pageable pageable) {
        return productInventoryRepository.getLowInventoryItems(pageable);
    }

    public Page<ERPProductInventoryHistory> getProductInventoryHistoryType(Pageable pageable, Predicate predicate) {
        return productInventoryHistoryRepository.findAll(predicate, pageable);

    }

}
