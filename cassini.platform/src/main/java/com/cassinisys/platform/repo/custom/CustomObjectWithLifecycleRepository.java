package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomObjectWithLifecycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomObjectWithLifecycleRepository extends JpaRepository<CustomObjectWithLifecycle, Integer>,
        QueryDslPredicateExecutor<CustomObjectWithLifecycle> {
    List<CustomObjectWithLifecycle> findByIdIn(Iterable<Integer> ids);
}
