package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMDrawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMDrawingRepository extends JpaRepository<PDMDrawing, Integer>,
        QueryDslPredicateExecutor<PDMDrawing> {
    List<PDMDrawing> findByIdIn(Iterable<Integer> ids);

    @Query("select count (i) from PDMDrawing i where i.id in :versionIds and ((LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%')")
    Integer getDrawingIdsBySearchQueryAndVersionIds(@Param("searchText") String searchText, @Param("versionIds") Iterable<Integer> versionIds);

    @Query("select count (i) from PDMDrawing i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getDrawingIdsBySearchQuery(@Param("searchText") String searchText);
}
