package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMChangeRelatedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 022 22-May -17.
 */
@Repository
public interface ChangeRelatedItemRepository extends JpaRepository<PLMChangeRelatedItem, Integer>,QueryDslPredicateExecutor<PLMChangeRelatedItem> {
    List<PLMChangeRelatedItem> findByChange(Integer id);
}
