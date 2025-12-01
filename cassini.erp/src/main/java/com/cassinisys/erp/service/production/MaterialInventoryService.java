package com.cassinisys.erp.service.production;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.model.production.*;
import com.cassinisys.erp.repo.production.MaterialInventoryHistoryRepository;
import com.cassinisys.erp.repo.production.MaterialInventoryRepository;
import com.cassinisys.erp.repo.production.MaterialPurchaseOrderDetailsRepository;
import com.cassinisys.erp.service.hrm.EmployeeService;
import com.cassinisys.erp.service.security.SessionWrapper;
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
public class MaterialInventoryService {

    @Autowired
    MaterialInventoryRepository materialInvRepository;

    @Autowired
    MaterialInventoryHistoryRepository mInvhistoryRepository;

    @Autowired
    MaterialService materialService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    MaterialInventoryHistoryRepository materialInventoryHistoryRepository;

    @Autowired
    MaterialPurchaseOrderDetailsRepository materialPurchaseOrderDetailsRepository;

    @Autowired
    MaterialDailyIssueReportService dailyHistoryService;

    @Autowired
    private SessionWrapper sessionWrapper;

    public List<ERPMaterialInventory> getMaterialsInventory(List<Integer> products) {
        return materialInvRepository.getMaterialsInventory(products);
    }

    public ERPMaterialInventory createMaterialInventory(
            ERPMaterialInventory materialInv) {

	/*	ERPMaterialInventory mInv = null;
        mInv = materialInvRepository.save(materialInv);

		// save to ERPMaterialInventoryHistory

		ERPMaterialInventoryHistory mInvHistory = new ERPMaterialInventoryHistory();
		mInvHistory.setTimestamp(new Date());
		mInvHistory.setMaterial(mInv.getMaterial());
		if (sessionWrapper != null && sessionWrapper.getSession() != null
				&& sessionWrapper.getSession().getLogin() != null)
			mInvHistory.setEmployee(sessionWrapper.getSession().getLogin()
					.getPerson().getId());

		mInvHistory.setQuantity(mInv.getInventory());// check this
		// mInvHistory.setType(InventoryType.STOCKIN);

		mInvhistoryRepository.save(mInvHistory);*/

        return materialInvRepository.save(materialInv);

    }

    public ERPMaterialInventory updateMaterialInventory(
            ERPMaterialInventory materialInv) {

        checkNotNull(materialInv);
        checkNotNull(materialInv.getRowId());
        if (materialInvRepository.findOne(materialInv.getRowId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return materialInvRepository.save(materialInv);

    }


    public ERPMaterialInventory getMaterialInventory(Integer materialId) {
        return materialInvRepository.getMaterialInventoryByMaterialId(materialId);
    }

    public ERPMaterialInventoryHistory stockIn(Integer materialId, Integer quantity, ERPMaterial material) {
        ERPMaterialPurchaseOrder purchaseOrder = material.getMaterialPO();
        ERPMaterialPurchaseOrderDetails details = materialPurchaseOrderDetailsRepository.findByMaterialIdAndMaterialPurchaseOrderId(material.getId(), purchaseOrder.getId());
        details.setIssQuantity(details.getIssQuantity() + quantity);
        if (details.getQuantity() == details.getIssQuantity()) {
            details.setIssued(Boolean.TRUE);
        }
        materialPurchaseOrderDetailsRepository.save(details);
        ERPMaterialInventory inventory = getMaterialInventory(materialId);
        if (inventory == null) {
            inventory = new ERPMaterialInventory();
            inventory.setMaterial(materialService.get(materialId));
        }

        Integer i = inventory.getInventory();
        if (i == null) {
            i = quantity;
        } else {
            i = i + quantity;
        }

        inventory.setInventory(i);
        materialInvRepository.save(inventory);

        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);

        ERPMaterialInventoryHistory history = new ERPMaterialInventoryHistory();
        history.setMaterial(materialId);
        history.setQuantity(quantity);
        history.setMaterialPO(purchaseOrder);
        history.setType(InventoryType.STOCKIN);
        history.setEmployee(employee);
        history.setTimestamp(new Date());


        return materialInventoryHistoryRepository.save(history);
    }

    public ERPMaterialInventoryHistory stockOut(Integer materialId, Integer quantity) {
        ERPMaterialInventory inventory = getMaterialInventory(materialId);
        if (inventory == null) {
            inventory = new ERPMaterialInventory();
            inventory.setMaterial(materialService.get(materialId));
        }

        Integer i = inventory.getInventory();
        if (i == null || i == 0 || i < quantity) {
            throw new RuntimeException("Inventory is too low");
        }
        i = i - quantity;

        inventory.setInventory(i);
        materialInvRepository.save(inventory);

        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);

        //adding Daily report
        dailyHistoryService.addQuantity(materialId, quantity);

        ERPMaterialInventoryHistory history = new ERPMaterialInventoryHistory();
        history.setMaterial(materialId);
        history.setType(InventoryType.STOCKOUT);
        history.setQuantity(quantity);
        history.setEmployee(employee);
        history.setTimestamp(new Date());

        return materialInventoryHistoryRepository.save(history);
    }


    public ERPMaterialInventory getMaterialInventoryById(Integer invId) {
        return materialInvRepository.findOne(invId);
    }

    public void deleteMaterialInventory(Integer invId) {
        checkNotNull(invId);
        materialInvRepository.delete(invId);
    }

    public Page<ERPMaterialInventoryHistory> getMaterialInventoryHistory(Integer productId, Pageable pageable) {
        return materialInventoryHistoryRepository.getMaterialInventoryHistory(productId, pageable);
    }

    public List<ERPMaterialInventory> getAllMaterialInventorys() {
        return materialInvRepository.findAll();
    }
}
