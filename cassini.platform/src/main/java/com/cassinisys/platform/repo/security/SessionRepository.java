package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.core.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author reddy
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Integer>,
        QueryDslPredicateExecutor<Session> {

    Session findBySessionId(Integer id);
}
