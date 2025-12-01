package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMPendingReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 30-08-2016.
 */
@Repository
public interface PendingReasonRepository extends JpaRepository<TMPendingReason, Integer>, QueryDslPredicateExecutor<TMPendingReason> {

}
