package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMRelatedItem;
import com.cassinisys.plm.model.plm.PLMRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 011 11-Jan -18.
 */
@Repository
public interface RelatedItemRepository extends JpaRepository<PLMRelatedItem, Integer>, QueryDslPredicateExecutor<PLMRelatedItem> {

    List<PLMRelatedItem> findByRelationship(PLMRelationship relationshipId);

    List<PLMRelatedItem> findByFromItem(Integer itemId);

    List<PLMRelatedItem> findByToItem(PLMItemRevision item);
}
