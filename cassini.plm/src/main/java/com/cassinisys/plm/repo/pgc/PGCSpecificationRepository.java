package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCSpecification;
import com.cassinisys.plm.model.pgc.PGCSpecificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCSpecificationRepository extends JpaRepository<PGCSpecification, Integer>, QueryDslPredicateExecutor<PGCSpecification> {

    List<PGCSpecification> findByIdIn(Iterable<Integer> ids);

    List<PGCSpecification> findByType(PGCSpecificationType type);

    PGCSpecification findByName(String name);

    PGCSpecification findByNumber(String number);

    @Query(
            "SELECT i FROM PGCSpecification i WHERE i.type.id IN :typeIds"
    )
    Page<PGCSpecification> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("SELECT count (i) FROM PGCSpecification i")
    Integer getSpecificationsCount();

    @Query("select count (i) from PGCSpecification i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getSpecificationCountBySearchQuery(@Param("searchText") String searchText);
}
