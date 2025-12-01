package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMSupplierAuditType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierAuditTypeRepository extends JpaRepository<PQMSupplierAuditType,Integer> {
    List<PQMSupplierAuditType> findByIdIn(Iterable<Integer> ids);

    List<PQMSupplierAuditType> findByParentTypeIsNullOrderByCreatedDateAsc();

    PQMSupplierAuditType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PQMSupplierAuditType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<PQMSupplierAuditType> findByParentTypeOrderByCreatedDateAsc(Integer parent);
}
