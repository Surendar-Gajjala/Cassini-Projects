package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMNpr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NprRepository extends JpaRepository<PLMNpr, Integer>, QueryDslPredicateExecutor<PLMNpr> {

    List<PLMNpr> findByIdIn(Iterable<Integer> ids);

    PLMNpr findByNumber(String number);

    @Query("select count (i) from PLMNpr i where (LOWER(CAST(i.reasonForRequest as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.notes as text))) LIKE '%' || :searchText || '%'")
    Integer getNPRCountBySearchQuery(@Param("searchText") String searchText);
}
