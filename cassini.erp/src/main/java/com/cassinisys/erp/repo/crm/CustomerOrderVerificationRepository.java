package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPCustomerOrder;
import com.cassinisys.erp.model.crm.ERPOrderVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 11/3/15.
 */
@Repository
public interface CustomerOrderVerificationRepository extends JpaRepository<ERPOrderVerification, Integer>,
        QueryDslPredicateExecutor<ERPOrderVerification> {
    List<ERPOrderVerification> findByOrder(ERPCustomerOrder order);
}
