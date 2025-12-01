package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.production.ERPMaterialInventory;

import java.util.List;

@Repository
public interface MaterialInventoryRepository extends JpaRepository<ERPMaterialInventory, Integer> {

    @Query(
            "SELECT m FROM ERPMaterialInventory m WHERE m.material.id = :materialId"
    )
    ERPMaterialInventory getMaterialInventoryByMaterialId(@Param("materialId") Integer materialId);

    @Query (
            "SELECT m FROM ERPMaterialInventory m WHERE m.material.id IN (:materialIds)"
    )
    List<ERPMaterialInventory> getMaterialsInventory(@Param("materialIds") List<Integer> materialIds);
}

