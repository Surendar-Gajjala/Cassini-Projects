package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPRouteOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BOPRouteOperationRepository extends JpaRepository<MESBOPRouteOperation, Integer>, QueryDslPredicateExecutor<MESBOPRouteOperation> {
    List<MESBOPRouteOperation> findByBopOrderByIdAsc(Integer id);

    List<MESBOPRouteOperation> findByBopAndParentIsNullOrderByIdAsc(Integer id);

    List<MESBOPRouteOperation> findByParentOrderByIdAsc(Integer id);

    @Query("select i.operation from MESBOPRouteOperation i where i.parent= :parent and i.type = 'OPERATION'")
    List<Integer> getOperationIdsByParent(@Param("parent") Integer parent);

    @Query("select i from MESBOPRouteOperation i where i.bop= :bop and i.type = 'OPERATION'")
    List<MESBOPRouteOperation> getOperationsByBop(@Param("bop") Integer bop);

    @Query("select i.id from MESBOPRouteOperation i where i.bop= :bop and i.type = 'OPERATION'")
    List<Integer> getIdsByBopAndOperation(@Param("bop") Integer bop);

    @Query("select count (i) from MESBOPRouteOperation i where i.parent= :parent")
    Integer getPlanCountByParent(@Param("parent") Integer parent);

    @Query("select count (i) from MESBOPRouteOperation i where i.bop= :bop and i.type = 'OPERATION'")
    Integer getPlanOperationCountByBop(@Param("bop") Integer bop);

    @Query("select i.operation from MESBOPRouteOperation i where i.bop= :bop and i.parent is null and i.type = 'OPERATION'")
    List<Integer> getOperationIdsByBopAndParentIsNull(@Param("bop") Integer bop);

    @Query("select sum (i.setupTime) from MESBOPRouteOperation i where i.bop= :bop and i.setupTime is not null")
    Integer getTotalSetupTimeByBop(@Param("bop") Integer bop);

    @Query("select sum (i.cycleTime) from MESBOPRouteOperation i where i.bop= :bop and i.cycleTime is not null")
    Integer getTotalCycleTimeByBop(@Param("bop") Integer bop);
}
