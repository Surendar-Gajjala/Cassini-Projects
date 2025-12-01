package com.cassinisys.erp.repo.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.security.ERPSession;

/**
 * Created by reddy on 7/25/15.
 */
@Repository
public interface SessionRepository extends JpaRepository<ERPSession, Integer>,
        QueryDslPredicateExecutor<ERPSession>{
}
