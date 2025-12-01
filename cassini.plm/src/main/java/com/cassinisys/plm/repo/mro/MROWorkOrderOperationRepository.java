package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROWorkOrderOperation;
import com.cassinisys.plm.model.mro.OperationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROWorkOrderOperationRepository extends JpaRepository<MROWorkOrderOperation, Integer> {
    List<MROWorkOrderOperation> findByWorkOrder(Integer workOrder);

    List<MROWorkOrderOperation> findByWorkOrderAndResult(Integer workOrder, OperationResult result);

    List<MROWorkOrderOperation> findByLovId(Integer autoNumber);

    @Query("select count (i) from MROWorkOrderOperation i where i.lov.id= :lovId")
    Integer getWorkOrderOperationsByLov(@Param("lovId") Integer lovId);

    @Query(value = "SELECT count(i) FROM mro_workorder_operation i WHERE i.lov= :lov AND i.list_value= :lovValue", nativeQuery = true)
    Integer getLovValueCount(@Param("lov") Integer lov, @Param("lovValue") String lovValue);

}
