package com.cassinisys.erp.repo.production;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.production.ERPProductBom;

@Repository
public interface ProductBomRepository extends JpaRepository<ERPProductBom, Integer> {

	List<ERPProductBom> findByProduct(Integer product);
	List<ERPProductBom> findByMaterial(Integer material);
}
