package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.plm.PLMRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 011 11-Jan -18.
 */
@Repository
public interface RelationshipRepository extends JpaRepository<PLMRelationship, Integer>, QueryDslPredicateExecutor<PLMRelationship> {

    List<PLMRelationship> findByFromType(PLMItemType plmItemType);

    List<PLMRelationship> findByToType(PLMItemType plmItemType);

    PLMRelationship findByFromTypeAndToType(PLMItemType fromType, PLMItemType toType);

    PLMRelationship findByToTypeAndFromType(PLMItemType fromType, PLMItemType toType);

    PLMRelationship findByNameEqualsIgnoreCase(String name);
}
