package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPInstanceOperationPart;
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
public interface BOPInstanceOperationPartRepository extends JpaRepository<MESBOPInstanceOperationPart, Integer>, QueryDslPredicateExecutor<MESBOPInstanceOperationPart> {

    List<MESBOPInstanceOperationPart> findByBopOperation(Integer id);

    MESBOPInstanceOperationPart findByBopOperationAndMbomInstanceItem(Integer id, Integer mbomInstanceItem);

    @Query("select sum(i.quantity) from MESBOPInstanceOperationPart i where i.bopOperation in :operationIds and i.mbomInstanceItem= :mbomInstanceItem")
    Integer getTotalQuantityByMbomInstanceItemAndIds(@Param("operationIds") List<Integer> operationIds, @Param("mbomInstanceItem") Integer mbomInstanceItem);

    @Query("select sum(i.quantity) from MESBOPInstanceOperationPart i where i.bopOperation= :operationId and i.mbomInstanceItem= :mbomInstanceItem")
    Integer getTotalQuantityByMbomInstanceItem(@Param("operationId") Integer operationId, @Param("mbomInstanceItem") Integer mbomInstanceItem);

    @Query("select sum(i.quantity) from MESBOPInstanceOperationPart i where i.bopOperation= :operationId and i.mbomInstanceItem= :mbomInstanceItem and i.id!= :id")
    Integer getTotalQuantityByMbomInstanceItemWithoutUpdateItem(@Param("operationId") Integer operationId, @Param("mbomInstanceItem") Integer mbomInstanceItem, @Param("id") Integer id);

    @Query("select sum(i.quantity) from MESBOPInstanceOperationPart i where i.bopOperation in :operationIds")
    Integer getTotalQuantityByBopOperationIds(@Param("operationIds") List<Integer> operationIds);

    @Query("select count (i) from MESBOPInstanceOperationPart i where i.bopOperation= :operationId")
    Integer getItemsCountByPlan(@Param("operationId") Integer operationId);

    List<MESBOPInstanceOperationPart> findByBopOperationAndType(Integer id, OperationPartType type);

    MESBOPInstanceOperationPart findByBopOperationAndMbomInstanceItemAndType(Integer id, Integer mbomInstanceItem, OperationPartType type);

    @Query("select sum(i.quantity) from MESBOPInstanceOperationPart i where i.bopOperation in :operationIds and i.mbomInstanceItem= :mbomInstanceItem and i.type= :partType")
    Integer getTotalQuantityByMbomInstanceItemAndIdsAndType(@Param("operationIds") List<Integer> operationIds, @Param("mbomInstanceItem") Integer mbomInstanceItem, @Param("partType") OperationPartType partType);

    @Query("select sum(i.quantity) from MESBOPInstanceOperationPart i where i.bopOperation in :operationIds and i.mbomInstanceItem= :mbomInstanceItem and i.type= :partType")
    Integer getTotalQuantityByMbomInstanceItemAndIdsByType(@Param("operationIds") List<Integer> operationIds, @Param("mbomInstanceItem") Integer mbomInstanceItem, @Param("partType") OperationPartType partType);

    @Query("select sum(i.quantity) from MESBOPInstanceOperationPart i where i.bopOperation= :operationId and i.mbomInstanceItem= :mbomInstanceItem and i.type= :partType")
    Integer getTotalQuantityByMbomInstanceItemAndType(@Param("operationId") Integer operationId, @Param("mbomInstanceItem") Integer mbomInstanceItem, @Param("partType") OperationPartType partType);

    @Query("select sum(i.quantity) from MESBOPInstanceOperationPart i where i.bopOperation= :operationId and i.mbomInstanceItem= :mbomInstanceItem and i.type= :partType and i.id!= :id")
    Integer getTotalQuantityByMbomInstanceItemAndTypeWithoutUpdateItem(@Param("operationId") Integer operationId, @Param("mbomInstanceItem") Integer mbomInstanceItem, @Param("partType") OperationPartType partType, @Param("id") Integer id);

}
