package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPOperationPart;
import com.cassinisys.plm.model.mes.OperationPartType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 04-08-2022.
 */
@Repository
public interface BOPOperationPartRepository extends JpaRepository<MESBOPOperationPart, Integer>, QueryDslPredicateExecutor<MESBOPOperationPart> {

    List<MESBOPOperationPart> findByBopOperation(Integer id);

    MESBOPOperationPart findByBopOperationAndMbomItem(Integer id, Integer mbomItem);

    @Query("select sum(i.quantity) from MESBOPOperationPart i where i.bopOperation in :operationIds and i.mbomItem= :mbomItem")
    Integer getTotalQuantityByMBomItemAndIds(@Param("operationIds") List<Integer> operationIds, @Param("mbomItem") Integer mbomItem);

    @Query("select sum(i.quantity) from MESBOPOperationPart i where i.bopOperation= :operationId and i.mbomItem= :mbomItem")
    Integer getTotalQuantityByMBomItem(@Param("operationId") Integer operationId, @Param("mbomItem") Integer mbomItem);

    @Query("select sum(i.quantity) from MESBOPOperationPart i where i.bopOperation= :operationId and i.mbomItem= :mbomItem and i.id!= :id")
    Integer getTotalQuantityByMBomItemWithoutUpdateItem(@Param("operationId") Integer operationId, @Param("mbomItem") Integer mbomItem, @Param("id") Integer id);

    @Query("select sum(i.quantity) from MESBOPOperationPart i where i.bopOperation in :operationIds")
    Integer getTotalQuantityByBopOperationIds(@Param("operationIds") List<Integer> operationIds);

    @Query("select count (i) from MESBOPOperationPart i where i.bopOperation= :operationId")
    Integer getItemsCountByPlan(@Param("operationId") Integer operationId);

    List<MESBOPOperationPart> findByBopOperationAndType(Integer id, OperationPartType type);

    MESBOPOperationPart findByBopOperationAndMbomItemAndType(Integer id, Integer mbomItem, OperationPartType type);

    @Query("select sum(i.quantity) from MESBOPOperationPart i where i.bopOperation in :operationIds and i.mbomItem= :mbomItem and i.type= :partType")
    Integer getTotalQuantityByMBomItemAndIdsAndType(@Param("operationIds") List<Integer> operationIds, @Param("mbomItem") Integer mbomItem, @Param("partType") OperationPartType partType);

    @Query("select sum(i.quantity) from MESBOPOperationPart i where i.bopOperation in :operationIds and i.mbomItem= :mbomItem and i.type= :partType")
    Integer getTotalQuantityByMBomItemAndIdsByType(@Param("operationIds") List<Integer> operationIds, @Param("mbomItem") Integer mbomItem, @Param("partType") OperationPartType partType);

    @Query("select sum(i.quantity) from MESBOPOperationPart i where i.bopOperation= :operationId and i.mbomItem= :mbomItem and i.type= :partType")
    Integer getTotalQuantityByMBomItemAndType(@Param("operationId") Integer operationId, @Param("mbomItem") Integer mbomItem, @Param("partType") OperationPartType partType);

    @Query("select sum(i.quantity) from MESBOPOperationPart i where i.bopOperation= :operationId and i.mbomItem= :mbomItem and i.type= :partType and i.id!= :id")
    Integer getTotalQuantityByMBomItemAndTypeWithoutUpdateItem(@Param("operationId") Integer operationId, @Param("mbomItem") Integer mbomItem, @Param("partType") OperationPartType partType, @Param("id") Integer id);

}
