package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Repository
public interface ItemRepository extends JpaRepository<PDMItem, Integer>,QueryDslPredicateExecutor<PDMItem>{
    @Query(
            "SELECT i FROM PDMItem i WHERE i.itemType.id IN :typeIds"
    )
    Page<PDMItem> getByItemTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);
}
