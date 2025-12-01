package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCDeclaration;
import com.cassinisys.plm.model.pgc.PGCDeclarationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 25-11-2020.
 */
@Repository
public interface PGCDeclarationRepository extends JpaRepository<PGCDeclaration, Integer>, QueryDslPredicateExecutor<PGCDeclaration> {

    List<PGCDeclaration> findByIdIn(Iterable<Integer> ids);

    List<PGCDeclaration> findByType(PGCDeclarationType type);

    PGCDeclaration findByNameContainingIgnoreCase(String name);

    PGCDeclaration findByNumber(String number);

    @Query(
            "SELECT i FROM PGCDeclaration i WHERE i.type.id IN :typeIds"
    )
    Page<PGCDeclaration> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<PGCDeclaration> findBySupplier(Integer supplierId);

    List<PGCDeclaration> findBySupplierAndContact(Integer supplierId, Integer contactId);

    @Query("SELECT count (i) FROM PGCDeclaration i")
    Integer getDeclarationsCount();

    @Query("select count (i) from PGCDeclaration i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getDeclarationCountBySearchQuery(@Param("searchText") String searchText);
}
