package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPMaterialInventoryHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialInventoryHistoryRepository  extends JpaRepository<ERPMaterialInventoryHistory,Integer>{

    @Query(
            "SELECT h FROM ERPMaterialInventoryHistory h WHERE h.material = :materialId"
    )
    Page<ERPMaterialInventoryHistory> getMaterialInventoryHistory(@Param("materialId") Integer materialId, Pageable pageable);
}

