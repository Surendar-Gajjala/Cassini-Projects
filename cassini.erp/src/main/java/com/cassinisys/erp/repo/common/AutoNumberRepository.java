package com.cassinisys.erp.repo.common;

import com.cassinisys.erp.model.common.ERPAutoNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 7/1/15.
 */
@Repository
public interface AutoNumberRepository extends JpaRepository<ERPAutoNumber, Integer> {
    ERPAutoNumber findByName(String name);
}
