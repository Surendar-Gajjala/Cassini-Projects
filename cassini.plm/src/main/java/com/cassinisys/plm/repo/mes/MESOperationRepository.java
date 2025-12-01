package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESOperation;
import com.cassinisys.plm.model.mes.MESOperationType;
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
public interface MESOperationRepository extends JpaRepository<MESOperation, Integer>, QueryDslPredicateExecutor<MESOperation> {
    MESOperation findByNameContainingIgnoreCase(String name);

    MESOperation findByName(String name);

    @Query("SELECT i FROM MESOperation i WHERE i.type.id IN :typeIds")
    Page<MESOperation> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<MESOperation> findByType(MESOperationType type);

    MESOperation findByNumber(String number);

    @Query("select count (i) from MESOperation i where (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'")
    Integer getOperationCountBySearchQuery(@Param("searchText") String searchText);
}