package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMSupplier;
import com.cassinisys.plm.model.mfr.PLMSupplierType;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface SupplierRepository extends JpaRepository<PLMSupplier, Integer>, QueryDslPredicateExecutor<PLMSupplier> {

    List<PLMSupplier> findByIdIn(List<Integer> ids);

    PLMSupplier findByName(String name);

    PLMSupplier findByNameEqualsIgnoreCase(String name);

    PLMSupplier findByNumber(String number);

    @Query(
            "SELECT i FROM PLMSupplier i WHERE i.supplierType.id IN :typeIds"
    )
    Page<PLMSupplier> getBySupplierTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<PLMSupplier> findBySupplierType(PLMSupplierType supplierType);

    @Query(
            "SELECT i FROM PLMSupplier i WHERE i.supplierType.id IN :typeIds"
    )
    List<PLMSupplier> getByTypeIds(@Param("typeIds") List<Integer> typeId);

    @Query("select i from PLMSupplier i where i.lifeCyclePhase.phaseType= :phaseType")
    List<PLMSupplier> getSuppliersByLifecyclePhase(@Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("SELECT count (i) FROM PLMSupplier i")
    Integer getSuppliersCount();

    @Query("select count (i) from PLMSupplier i where (LOWER(CAST(i.supplierType.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.address as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.city as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.phoneNumber as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.mobileNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.faxNumber as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.email as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.webSite as text))) LIKE '%' || :searchText || '%'")
    Integer getSuppliersCountBySearchQuery(@Param("searchText") String searchText);
}
