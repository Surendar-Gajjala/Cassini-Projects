package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESProductionOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 04-10-2022.
 */
@Repository
public interface ProductionOrderItemRepository extends JpaRepository<MESProductionOrderItem, Integer> {
    List<MESProductionOrderItem> findByProductionOrderOrderByIdAsc(Integer id);

    MESProductionOrderItem findByProductionOrderAndMbomRevision(Integer productionOrder, Integer mbomRevision);

    @Query("select count (i) from MESProductionOrderItem i where i.productionOrder= :id")
    Integer getItemCountByProductionOrder(@Param("id") Integer id);

    @Query("select sum (i.quantityProduced) from MESProductionOrderItem i where i.productionOrder= :id and i.quantityProduced is not null")
    Integer getItemsSumByProductionOrder(@Param("id") Integer id);
}
