package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.CustomIndentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Repository
public interface CustomIndentItemRepository extends JpaRepository<CustomIndentItem, Integer>, QueryDslPredicateExecutor<CustomIndentItem> {

    @Query("SELECT i FROM CustomIndentItem i WHERE i.customIndent.id =:indentId ORDER BY i.id ASC")
    List<CustomIndentItem> findByIndentId(@Param("indentId") Integer indentId);
}
