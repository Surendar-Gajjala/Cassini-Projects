package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMSuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 03-08-2016.
 */
@Repository
public interface SuitRepository extends JpaRepository<TMSuit, Integer>, QueryDslPredicateExecutor<TMSuit> {
    List<TMSuit> findBySuite(Integer suitId);
}
