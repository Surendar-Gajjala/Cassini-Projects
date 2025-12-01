package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMRelationshipAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 022 22-Jan -18.
 */
@Repository
public interface RelationshipAttributeRepository extends JpaRepository<PLMRelationshipAttribute, Integer>, QueryDslPredicateExecutor<PLMRelationshipAttribute> {

    List<PLMRelationshipAttribute> findByRelationship(Integer relationshipId);
}
