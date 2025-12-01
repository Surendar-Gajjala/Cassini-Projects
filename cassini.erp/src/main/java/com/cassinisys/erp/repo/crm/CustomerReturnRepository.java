package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPCustomerReturn;
import com.cassinisys.erp.model.crm.ReturnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerReturnRepository extends JpaRepository<ERPCustomerReturn, Integer>,
        QueryDslPredicateExecutor<ERPCustomerReturn> {

    @Query("SELECT r FROM ERPCustomerReturn r WHERE r.customer.id = :customerId")
    List<ERPCustomerReturn> findByCustomerId(@Param("customerId") Integer customerId);

    @Query("SELECT r FROM ERPCustomerReturn r WHERE r.id = :returnId")
    ERPCustomerReturn findByReturnId(@Param("returnId") Integer returnId);

    @Query(
            "SELECT o FROM ERPCustomerReturn o WHERE o.status= :status"
    )
    List<ERPCustomerReturn> getNewReturns(@Param("status") ReturnStatus status);
}
