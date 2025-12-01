package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESManpower;
import com.cassinisys.plm.model.mes.MESManpowerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface MESManpowerRepository extends JpaRepository<MESManpower, Integer>, QueryDslPredicateExecutor<MESManpower> {
    MESManpower findByNameContainingIgnoreCase(String name);

    @Query(
            "SELECT i FROM MESManpower i WHERE i.type.id IN :typeIds"
    )
    Page<MESManpower> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<MESManpower> findByType(MESManpowerType type);

    MESManpower findByNumber(String number);

    MESManpower findByName(String name);

   /* @Query("select i.person from MESManpower i where i.person not in :ids")
    List<Integer> getPersonsByIdNotIn(@Param("ids") Iterable<Integer> ids);*/

    @Query("select count (i) from MESManpower i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getManpowerCountBySearchQuery(@Param("searchText") String searchText);
}
