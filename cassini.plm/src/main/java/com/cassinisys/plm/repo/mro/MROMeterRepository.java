package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROMeter;
import com.cassinisys.plm.model.mro.MROMeterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROMeterRepository extends JpaRepository<MROMeter, Integer>, QueryDslPredicateExecutor<MROMeter> {
    MROMeter findByName(String name);

    MROMeter findByNumber(String number);


    @Query(
            "SELECT i FROM MROMeter i WHERE i.type.id IN :typeIds"
    )
    Page<MROMeter> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<MROMeter> findByType(MROMeterType type);

    @Query("select count (i) from MROMeter i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'")
    Integer getMetersCountBySearchQuery(@Param("searchText") String searchText);

}
