package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPProcessInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 02-02-2017.
 */
@Repository
public interface ProcessInstanceRepository extends JpaRepository<ERPProcessInstance, Integer>{
}
