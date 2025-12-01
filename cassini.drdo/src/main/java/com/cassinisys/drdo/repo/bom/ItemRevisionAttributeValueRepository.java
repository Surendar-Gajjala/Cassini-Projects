package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.ItemRevisionAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subra on 04-10-2018.
 */
@Repository
public interface ItemRevisionAttributeValueRepository extends JpaRepository<ItemRevisionAttributeValue, Integer>, QueryDslPredicateExecutor<ItemRevisionAttributeValue> {

    @Query(
            "SELECT a FROM ItemRevisionAttributeValue a WHERE a.id.objectId= :itemId"
    )
    List<ItemRevisionAttributeValue> findByItemId(@Param("itemId") Integer itemId);
}
