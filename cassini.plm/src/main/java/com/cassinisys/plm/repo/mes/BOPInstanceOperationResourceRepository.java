package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPInstanceOperationResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 28-07-2022.
 */
@Repository
public interface BOPInstanceOperationResourceRepository extends JpaRepository<MESBOPInstanceOperationResource, Integer>, QueryDslPredicateExecutor<MESBOPInstanceOperationResource> {

    @Query("select count (i) from MESBOPInstanceOperationResource i where i.bopOperation= :bopOperation and i.operation= :operation and i.resourceType= :resourceType")
    Integer getBopOperationResourceTypeObjectCount(@Param("bopOperation") Integer bopOperation, @Param("operation") Integer operation, @Param("resourceType") Integer resourceType);

    @Query("select count (i) from MESBOPInstanceOperationResource i where i.bopOperation= :bopOperation and i.operation= :operation")
    Integer getBopOperationResourcesCountByPlanAndOperation(@Param("bopOperation") Integer bopOperation, @Param("operation") Integer operation);

    @Query("select count (i) from MESBOPInstanceOperationResource i where i.bopOperation= :bopOperation")
    Integer getBopOperationResourcesCountByPlan(@Param("bopOperation") Integer bopOperation);

    @Query("select count (i) from MESBOPInstanceOperationResource i where i.bopOperation= :bopOperation and i.operation= :operation and i.type= :type")
    Integer getBopOperationTypeObjectCount(@Param("bopOperation") Integer bopOperation, @Param("operation") Integer operation, @Param("type") String type);

    @Query("select distinct i.type from MESBOPInstanceOperationResource i where i.bopOperation= :planId")
    List<String> getUniqueTypesByBopOperation(@Param("planId") Integer planId);

    List<MESBOPInstanceOperationResource> findByBopOperationOrderByIdAsc(Integer id);

    List<MESBOPInstanceOperationResource> findByBopOperationAndType(Integer planId, String type);

    @Query("select i.resource from MESBOPInstanceOperationResource i where i.bopOperation= :bopOperation and i.operation= :operation and i.resourceType= :resourceType")
    List<Integer> getResourceIdsByPlanAndOperationAndResourceType(@Param("bopOperation") Integer bopOperation, @Param("operation") Integer operation, @Param("resourceType") Integer resourceType);
}
