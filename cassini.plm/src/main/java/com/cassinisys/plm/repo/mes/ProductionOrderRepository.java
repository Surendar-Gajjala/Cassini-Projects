package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESProductionOrder;
import com.cassinisys.plm.model.mes.MESProductionOrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface ProductionOrderRepository extends JpaRepository<MESProductionOrder, Integer>, QueryDslPredicateExecutor<MESProductionOrder> {

    @Query(
            "SELECT i FROM MESProductionOrder i WHERE i.type.id IN :typeIds"
    )
    Page<MESProductionOrder> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<MESProductionOrder> findByType(MESProductionOrderType type);

    @Query("SELECT count (i) FROM MESProductionOrder i")
    Integer getProductionOrdersCount();

    MESProductionOrder findByName(String name);

    MESProductionOrder findByNumber(String number);

    @Query("SELECT MIN(productionOrder.plannedStartDate) FROM MESProductionOrder productionOrder ")
    Date getProductionOrderMinDate();

    @Query("SELECT MAX(productionOrder.plannedFinishDate) FROM MESProductionOrder productionOrder ")
    Date getProductionOrderMaxDate();

    @Query("SELECT count (i) FROM MESProductionOrder i WHERE i.assignedTo= :person")
    Integer getProductionOrdersCountByAssignedTo(@Param("person") Integer person);

    @Query("SELECT count (i) FROM MESProductionOrder i WHERE i.type.lifecycle.id= :lifecycle")
    Integer getProductionOrderByLifeCycle(@Param("lifecycle") Integer lifecycle);

    @Query("select i from MESProductionOrder i where i.plannedStartDate is not null or i.plannedFinishDate is not null")
    List<MESProductionOrder> getPlannedDatesNotNullProductionOrders();
}
