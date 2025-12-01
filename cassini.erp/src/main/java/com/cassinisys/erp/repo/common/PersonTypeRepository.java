package com.cassinisys.erp.repo.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.common.ERPPersonType;

@Repository
public interface PersonTypeRepository extends JpaRepository<ERPPersonType, Integer> {
    ERPPersonType findByName(String name);
}
