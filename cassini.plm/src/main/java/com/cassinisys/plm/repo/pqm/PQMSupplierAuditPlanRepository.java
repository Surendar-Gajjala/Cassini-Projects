package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.AuditPlanStatus;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface PQMSupplierAuditPlanRepository extends JpaRepository<PQMSupplierAuditPlan, Integer>, QueryDslPredicateExecutor<PQMSupplierAuditPlan> {

    List<PQMSupplierAuditPlan> findBySupplier(Integer supplierId);

    List<PQMSupplierAuditPlan> findBySupplierAuditOrderByModifiedDateDesc(Integer audit);

    PQMSupplierAuditPlan findBySupplierAuditAndSupplier(Integer audit, Integer supplier);

    @Query("select i.supplier from PQMSupplierAuditPlan i where i.supplierAudit= :audit")
    List<Integer> getSupplierIdsByAudit(@Param("audit") Integer audit);

    @Query("select count (i) from PQMSupplierAuditPlan i where i.supplierAudit= :audit")
    Integer getPlanCountByAudit(@Param("audit") Integer audit);

    @Query("select count (i) from PQMSupplierAuditPlan i where i.supplier= :supplier")
    Integer getPlanCountBySupplier(@Param("supplier") Integer supplier);

    @Query("select count (i) from PQMSupplierAuditPlan i where i.supplierAudit= :audit and (i.plannedStartDate is null or i.plannedStartDate is empty)")
    Integer getNoPlannedDatesCountByAudit(@Param("audit") Integer audit);

    @Query("select count (i) from PQMSupplierAuditPlan i where i.supplierAudit= :audit and (i.plannedStartDate is not null and i.plannedStartDate is not empty)")
    Integer getPlannedDatesCountByAudit(@Param("audit") Integer audit);

    @Query("select count (i) from PQMSupplierAuditPlan i where i.supplierAudit= :audit and (i.finishedDate is not null and i.finishedDate is not empty)")
    Integer getFinishedDatesCountByAudit(@Param("audit") Integer audit);

    @Query("select count (i) from PQMSupplierAuditPlan i where i.supplierAudit= :audit and (i.finishedDate is null or i.finishedDate is empty)")
    Integer getNoFinishedDatesCountByAudit(@Param("audit") Integer audit);

    @Query("select count (i) from PQMSupplierAuditPlan i where i.supplierAudit= :audit and i.status= :status")
    Integer getAuditPlanCountByAuditAndStatus(@Param("audit") Integer audit, @Param("status") AuditPlanStatus status);

    @Query("select count (i) from PQMSupplierAuditPlan i where i.supplierAudit= :audit and i.status != 'APPROVED' ")
    Integer getNonApprovedAuditPlanCount(@Param("audit") Integer audit);

    @Query("select i from PQMSupplierAuditPlan i where i.status= :status")
    List<PQMSupplierAuditPlan> getSupplierAuditPlansByStatus(@Param("status") AuditPlanStatus status);

    @Query("select count(i) from PQMSupplierAuditPlan i where i.supplierAudit= :audit and (i.plannedStartDate is not null and i.plannedStartDate < :data) and ( i.status != 'APPROVED' and i.status !='COMPLETED') ")
    Integer getOverdueSupplerAudits(@Param("audit") Integer audit, @Param("data") Date data);

    @Query("select count (i) from PQMSupplierAuditPlan i where i.status= :auditStatus")
    Integer getSupplierAuditStatusCounts(@Param("auditStatus") AuditPlanStatus auditStatus);

    
}
