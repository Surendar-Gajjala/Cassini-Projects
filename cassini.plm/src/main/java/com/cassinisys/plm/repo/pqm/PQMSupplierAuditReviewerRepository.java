package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMSupplierAuditReviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PQMSupplierAuditReviewerRepository extends JpaRepository<PQMSupplierAuditReviewer, Integer>, QueryDslPredicateExecutor<PQMSupplierAuditReviewer> {
    List<PQMSupplierAuditReviewer> findByPlan(Integer plan);

    @Query("select count (i) from PQMSupplierAuditReviewer i where i.plan= :plan and i.approver = true and (i.status = 'NONE' or i.status = 'REJECTED')")
    Integer getNotApprovedCountByPlan(@Param("plan") Integer plan);

    @Query("select count (i) from PQMSupplierAuditReviewer i where i.plan= :plan and i.approver = true and (i.status = 'APPROVED' or i.status = 'REJECTED')")
    Integer getApprovedCountByPlan(@Param("plan") Integer plan);

    @Query("select count (i) from PQMSupplierAuditReviewer i where i.plan= :plan and i.approver = true")
    Integer getApproverCountByPlan(@Param("plan") Integer plan);
}
