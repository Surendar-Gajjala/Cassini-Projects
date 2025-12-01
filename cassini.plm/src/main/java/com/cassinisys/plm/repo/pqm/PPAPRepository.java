package com.cassinisys.plm.repo.pqm;

import java.util.List;

import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.pqm.PQMPPAP;
import com.cassinisys.plm.model.pqm.PQMPPAPType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PPAPRepository extends JpaRepository<PQMPPAP, Integer>, QueryDslPredicateExecutor<PQMPPAP>{
    List<PQMPPAP> findByType(PQMPPAPType type);
    PQMPPAP findByNumber(String number);
    List<PQMPPAP> findBySupplier(Integer supplierId);
    List<PQMPPAP> findBySupplierPart(Integer supplierPartId);

    @Query("SELECT count(i) FROM PQMPPAP i where i.status.phaseType= :phaseType")
    Integer getPpapCountsByStatus(@Param("phaseType") LifeCyclePhaseType phaseType);

    

    @Query("select count (i) from PQMPPAP i")
    Integer getAllPpapCounts();


}
