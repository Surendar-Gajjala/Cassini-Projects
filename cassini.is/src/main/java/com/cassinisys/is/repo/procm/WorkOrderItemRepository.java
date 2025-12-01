package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISWorkOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by swapna on 23/01/19.
 */
public interface WorkOrderItemRepository extends JpaRepository<ISWorkOrderItem, Integer>, QueryDslPredicateExecutor<ISWorkOrderItem> {

    List<ISWorkOrderItem> findByWorkOrder(Integer workOrderId);

    Page<ISWorkOrderItem> findByWorkOrder(Integer workOrderId, Pageable pageable);

    List<ISWorkOrderItem> findByTask(Integer taskId);
}
