package com.cassinisys.erp.repo.production;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.production.ERPProductType;

@Repository
public interface ProductTypeRepository extends JpaRepository<ERPProductType, Integer> {
    ERPProductType findByName(String name);

}
