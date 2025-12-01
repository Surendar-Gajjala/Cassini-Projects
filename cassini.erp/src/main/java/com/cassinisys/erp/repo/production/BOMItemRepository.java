package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPBom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.production.ERPBomItem;

import java.util.List;

@Repository
public interface BOMItemRepository extends JpaRepository<ERPBomItem, Integer> {

 List<ERPBomItem> findByBom(ERPBom bom);

}
