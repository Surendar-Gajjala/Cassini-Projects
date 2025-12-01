package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.ItemAttributeValue;
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
public interface ItemAttributeValueRepository extends JpaRepository<ItemAttributeValue, Integer>, QueryDslPredicateExecutor<ItemAttributeValue> {

    @Query(
            "SELECT a FROM ItemAttributeValue a WHERE a.id.attributeDef= :attributeId"
    )
    List<ItemAttributeValue> findByAttributeId(@Param("attributeId") Integer attributeId);

    @Query(
            "SELECT a FROM ItemAttributeValue a WHERE a.id.objectId= :itemId"
    )
    List<ItemAttributeValue> findByItemId(@Param("itemId") Integer itemId);
}
