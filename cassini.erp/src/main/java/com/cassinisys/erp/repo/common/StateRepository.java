package com.cassinisys.erp.repo.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.common.ERPCountry;
import com.cassinisys.erp.model.common.ERPState;

@Repository
public interface StateRepository  extends JpaRepository<ERPState, Integer>{
    List<ERPState> findByCountry(ERPCountry country);
    ERPState findByNameAndCountry(String name, ERPCountry country);

}
