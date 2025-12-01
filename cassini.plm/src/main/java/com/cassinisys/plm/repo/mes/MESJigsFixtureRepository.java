package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.JigsFixtureType;
import com.cassinisys.plm.model.mes.MESJigsFixture;
import com.cassinisys.plm.model.mes.MESJigsFixtureType;
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
public interface MESJigsFixtureRepository extends JpaRepository<MESJigsFixture, Integer>, QueryDslPredicateExecutor<MESJigsFixture> {

    List<MESJigsFixture> findByIdIn(Iterable<Integer> ids);

    List<MESJigsFixture> findByType(MESJigsFixtureType type);

    MESJigsFixture findByNameContainingIgnoreCase(String name);

    MESJigsFixture findByJigTypeAndName(JigsFixtureType type, String name);

    MESJigsFixture findByNumber(String number);

    @Query(
            "SELECT i FROM MESJigsFixture i WHERE i.type.id IN :typeIds"
    )
    Page<MESJigsFixture> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query(
            "SELECT i FROM MESJigsFixture i WHERE i.type.id IN :typeIds and i.requiresMaintenance=true"
    )
    List<MESJigsFixture> getByTypeIds(@Param("typeIds") List<Integer> typeId);

    @Query("select count (i) from MESJigsFixture i where i.jigType= :jigType and ((LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%')")
    Integer getJigAndFixtureCountBySearchQuery(@Param("jigType") JigsFixtureType jigType, @Param("searchText") String searchText);
}
