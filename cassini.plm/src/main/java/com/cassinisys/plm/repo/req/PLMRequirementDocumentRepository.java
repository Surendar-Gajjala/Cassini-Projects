package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementDocument;
import com.cassinisys.plm.model.req.PLMRequirementDocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementDocumentRepository extends JpaRepository<PLMRequirementDocument, Integer>, QueryDslPredicateExecutor<PLMRequirementDocument> {

    List<PLMRequirementDocument> findByIdIn(Iterable<Integer> ids);

    List<PLMRequirementDocument> findByType(PLMRequirementDocumentType type);

    PLMRequirementDocument findByNameContainingIgnoreCase(String name);

    PLMRequirementDocument findByNumber(String number);

    @Query(
            "SELECT i FROM PLMRequirementDocument i WHERE i.type.id IN :typeIds"
    )
    Page<PLMRequirementDocument> getByTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    @Query("SELECT count (i) FROM PLMRequirementDocument i")
    Integer getReqDocumentsCount();

    @Query("select count (i) from PLMRequirementDocument i where (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'")
    Integer getRequirementDocumentsCountBySearchQuery(@Param("searchText") String searchText);

}
