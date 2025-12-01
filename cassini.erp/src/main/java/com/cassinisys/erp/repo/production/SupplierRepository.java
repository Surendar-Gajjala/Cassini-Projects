package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPMaterial;
import com.cassinisys.erp.model.production.ERPSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<ERPSupplier, Integer> {

    List<ERPSupplier> findByNameAndOfficePhone(String name, String officePhone);

    List<ERPSupplier> findByIdIn(Iterable<Integer> var1);

}
