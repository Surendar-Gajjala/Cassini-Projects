package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPInstanceRouteOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BOPInstanceRouteOperationRepository extends JpaRepository<MESBOPInstanceRouteOperation, Integer>, QueryDslPredicateExecutor<MESBOPInstanceRouteOperation> {
    List<MESBOPInstanceRouteOperation> findByBopInstanceOrderByIdAsc(Integer id);

    List<MESBOPInstanceRouteOperation> findByBopInstanceAndParentIsNullOrderByIdAsc(Integer id);

    List<MESBOPInstanceRouteOperation> findByParentOrderByIdAsc(Integer id);

    @Query("select i.operation from MESBOPInstanceRouteOperation i where i.parent= :parent and i.type = 'OPERATION'")
    List<Integer> getOperationIdsByParent(@Param("parent") Integer parent);

    @Query("select i from MESBOPInstanceRouteOperation i where i.bopInstance= :bopInstance and i.type = 'OPERATION'")
    List<MESBOPInstanceRouteOperation> getOperationsByBopInstance(@Param("bopInstance") Integer bopInstance);

    @Query("select i.id from MESBOPInstanceRouteOperation i where i.bopInstance= :bopInstance and i.type = 'OPERATION'")
    List<Integer> getIdsByBopInstanceAndOperation(@Param("bopInstance") Integer bopInstance);

    @Query("select count (i) from MESBOPInstanceRouteOperation i where i.parent= :parent")
    Integer getPlanCountByParent(@Param("parent") Integer parent);

    @Query("select count (i) from MESBOPInstanceOperationResource i where i.bopOperation= :bopOperation")
    Integer getBopInstanceOperationResourcesCountByOperation(@Param("bopOperation") Integer bopOperation);

    @Query("select count (i) from MESBOPInstanceOperationPart i where i.bopOperation= :operationId")
    Integer getItemsCountByOperation(@Param("operationId") Integer operationId);

    @Query("select count (i) from MESBOPInstanceRouteOperation i where i.bopInstance= :bopInstance and i.type = 'OPERATION'")
    Integer getPlanOperationCountByBopInstance(@Param("bopInstance") Integer bopInstance);

    @Query("select i.operation from MESBOPInstanceRouteOperation i where i.bopInstance= :bopInstance and i.parent is null and i.type = 'OPERATION'")
    List<Integer> getOperationIdsByBopInstanceAndParentIsNull(@Param("bopInstance") Integer bopInstance);

    @Query("select sum (i.setupTime) from MESBOPInstanceRouteOperation i where i.bopInstance= :bopInstance and i.setupTime is not null")
    Integer getTotalSetupTimeByBopInstance(@Param("bopInstance") Integer bopInstance);

    @Query("select sum (i.cycleTime) from MESBOPInstanceRouteOperation i where i.bopInstance= :bopInstance and i.cycleTime is not null")
    Integer getTotalCycleTimeByBopInstance(@Param("bopInstance") Integer bopInstance);
}
