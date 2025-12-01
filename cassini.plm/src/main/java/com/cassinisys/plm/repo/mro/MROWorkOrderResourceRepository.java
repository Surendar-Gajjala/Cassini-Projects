package com.cassinisys.plm.repo.mro;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.plm.model.mro.MROWorkOrderResource;
import com.cassinisys.plm.model.plm.PLMObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 10-12-2020.
 */
@Repository
public interface MROWorkOrderResourceRepository extends JpaRepository<MROWorkOrderResource, Integer> {
    @Query("select i.resourceId from MROWorkOrderResource i where i.workOrder= :workOrder")
    List<Integer> getResourceIdsByWorkOrder(@Param("workOrder") Integer workOrder);

    List<MROWorkOrderResource> findByWorkOrderAndResourceType(Integer workOrderId, ObjectType var1);

    List<MROWorkOrderResource> findByWorkOrder(Integer workOrderId);

}
