package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMAssembly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMAssemblyRepository extends JpaRepository<PDMAssembly, Integer>,
        QueryDslPredicateExecutor<PDMAssembly> {
    List<PDMAssembly> findByIdIn(Iterable<Integer> ids);

    @Query("select count (i) from PDMAssembly i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getAssemblyIdsBySearchQuery(@Param("searchText") String searchText);

    @Query("select count (i) from PDMAssembly i where i.id in :versionIds and ((LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%')")
    Integer getAssemblyIdsBySearchQueryAndVersionIds(@Param("searchText") String searchText, @Param("versionIds") Iterable<Integer> versionIds);
}
