package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomRevisionedObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRevisionedObjectRepository extends JpaRepository<CustomRevisionedObject, Integer>,
        QueryDslPredicateExecutor<CustomRevisionedObject> {
    List<CustomRevisionedObject> findByIdIn(Iterable<Integer> ids);
}
