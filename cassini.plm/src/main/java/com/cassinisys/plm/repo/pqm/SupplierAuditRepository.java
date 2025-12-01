package com.cassinisys.plm.repo.pqm;

import java.util.List;

import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.pqm.PQMSupplierAudit;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;


/**
 * SupplierAuditRepository
 */
@Repository
public interface SupplierAuditRepository extends JpaRepository<PQMSupplierAudit, Integer>, QueryDslPredicateExecutor<PQMSupplierAudit> {
    List<PQMSupplierAudit> findByType(PQMSupplierAuditType type);
    PQMSupplierAudit findByNumber(String number);
    // List<PQMSupplierAudit> findBySupplier(Integer id);
    

    @Query("SELECT count(i) FROM PQMSupplierAudit i where i.status.phaseType= :phaseType")
    Integer getSupplierAuditCounts(@Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("select count (i) from PQMSupplierAudit i")
    Integer getTotalSupplierAudit();

    @Query("SELECT distinct i.plannedYear from PQMSupplierAudit i where i.plannedYear is not null order by i.plannedYear asc")
    List<String> getUniquePlannedYears();

    @Query("SELECT count(i) FROM PQMSupplierAudit i where i.status.phase= :phase and i.plannedYear= :plannedYear")
    Integer getSupplierAuditCountsByYearAndStatus(@Param("phase") String phase,@Param("plannedYear") String plannedYear);
}