package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISWorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 22/01/19.
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<ISWorkOrder, Integer>, QueryDslPredicateExecutor<ISWorkOrder> {

    List<ISWorkOrder> findByContractor(Integer contractorId);
}
