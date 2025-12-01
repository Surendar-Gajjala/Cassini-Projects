package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMSupplierAuditAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PQMSupplierAuditAttributeRepository extends JpaRepository<PQMSupplierAuditAttribute,Integer> {
    @Query(
            "SELECT a FROM PQMSupplierAuditAttribute a WHERE a.id.objectId= :planId"
    )
    List<PQMSupplierAuditAttribute> findBySupplierAuditIdIn(@Param("planId") Integer planId);

    @Query(
            "SELECT a FROM PQMSupplierAuditAttribute a WHERE a.id.objectId= :itemId AND a.id.attributeDef= :attributeId"
    )
    PQMSupplierAuditAttribute findBySupplierAuditAndAttribute(@Param("itemId") Integer itemId, @Param("attributeId") Integer attributeId);

}
