package com.cassinisys.erp.repo.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.common.ERPCountry;

@Repository
public interface CountryRepository extends JpaRepository<ERPCountry, Integer>{
    ERPCountry findByName(String name);
}
