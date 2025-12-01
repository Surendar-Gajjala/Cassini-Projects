package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface MESShiftRepository extends JpaRepository<MESShift, Integer>, QueryDslPredicateExecutor<MESShift> {
    MESShift findByNameContainingIgnoreCase(String name);
    MESShift findByName(String name);

    MESShift findBystartTimeAndEndTime(LocalTime startTime, LocalTime endTime);

    MESShift findByNumber(String number);

    @Query("select count (i) from MESShift i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getShiftCountBySearchQuery(@Param("searchText") String searchText);
}
