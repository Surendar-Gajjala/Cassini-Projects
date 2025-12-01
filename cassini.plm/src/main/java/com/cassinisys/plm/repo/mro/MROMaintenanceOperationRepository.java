package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROMaintenanceOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROMaintenanceOperationRepository extends JpaRepository<MROMaintenanceOperation, Integer> {
    MROMaintenanceOperation findByMaintenancePlanAndName(Integer maintenanceId, String name);

    List<MROMaintenanceOperation> findByMaintenancePlanOrderByModifiedDateDesc(Integer maintenancePlan);

    List<MROMaintenanceOperation> findByLovId(Integer autoNumber);

    @Query("select count (i) from MROMaintenanceOperation i where i.lov.id= :lovId")
    Integer getMaintenanceOperationsByLov(@Param("lovId") Integer lovId);
}
