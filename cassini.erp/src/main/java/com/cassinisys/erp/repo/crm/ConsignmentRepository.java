package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPConsignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 10/14/15.
 */
@Repository
public interface ConsignmentRepository extends JpaRepository<ERPConsignment, Integer>,
        QueryDslPredicateExecutor<ERPConsignment> {

    ERPConsignment findByNumber(String number);


}
