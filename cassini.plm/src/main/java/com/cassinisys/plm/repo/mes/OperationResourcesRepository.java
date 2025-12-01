package com.cassinisys.plm.repo.mes;


import com.cassinisys.plm.model.mes.MESOperationResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OperationResourcesRepository extends JpaRepository<MESOperationResources, Integer> {

    List<MESOperationResources> findByOperation(Integer id);

    MESOperationResources findByOperationAndResourceAndResourceType(Integer operation, String resource, Integer resourceType);

    MESOperationResources findByOperationAndResourceType(Integer operation, Integer resourceType);

    List<MESOperationResources> findByOperationAndResource(Integer operation, String resource);

    @Query("select distinct i.resource from MESOperationResources i where i.operation= :operation")
    List<String> getUniqueTypesByOperation(@Param("operation") Integer operation);

    @Query("select sum (i.quantity) from MESOperationResources i where i.operation= :operation")
    Integer getResourcesQuantityCountByOperation(@Param("operation") Integer operation);
}
