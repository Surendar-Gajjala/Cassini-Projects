package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 06-07-2016.
 */
@Repository
public interface ProjectRepository extends JpaRepository<TMProject, Integer>, QueryDslPredicateExecutor<TMProject> {
    public List<TMProject> findByIdIn(Iterable<Integer> ids);
}

