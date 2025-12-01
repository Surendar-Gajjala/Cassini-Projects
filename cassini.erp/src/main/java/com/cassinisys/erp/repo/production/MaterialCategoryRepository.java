package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPMaterialCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialCategoryRepository extends JpaRepository<ERPMaterialCategory, Integer> {


    ERPMaterialCategory findByName(String name);
    List<ERPMaterialCategory> findByParent(Integer parent);

}
