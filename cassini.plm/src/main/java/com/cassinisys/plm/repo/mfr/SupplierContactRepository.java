package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMSupplierContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Repository
public interface SupplierContactRepository extends JpaRepository<PLMSupplierContact, Integer>, QueryDslPredicateExecutor<PLMSupplierContact> {
    List<PLMSupplierContact> findBySupplier(Integer supplierId);

    PLMSupplierContact findByContactAndSupplier(Integer personId, Integer supplierId);

    @Query("select i.contact from PLMSupplierContact i where i.contact not in :ids")
    List<Integer> getContactsByIdNotIn(@Param("ids") Iterable<Integer> ids);

    List<PLMSupplierContact> findBySupplierAndActiveTrue(Integer supplierId);

}
