package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROSparePart;
import com.cassinisys.plm.model.mro.MROWorkOrderPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROWorkOrderPartRepository extends JpaRepository<MROWorkOrderPart, Integer> {

    @Query("select i.sparePart.id from MROWorkOrderPart i where i.workOrder= :workOrder")
    List<Integer> getSparePartIdsByWorkOrder(@Param("workOrder") Integer workOrder);

    MROWorkOrderPart findByWorkOrderAndSparePart(Integer workOrder, MROSparePart sparePart);

    List<MROWorkOrderPart> findByWorkOrderOrderByModifiedDateDesc(Integer workOrder);

    List<MROWorkOrderPart> findBySparePart(MROSparePart sparePart);
}
