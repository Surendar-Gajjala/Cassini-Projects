package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMCommit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMCommitRepository extends JpaRepository<PDMCommit, Integer>,
        QueryDslPredicateExecutor<PDMCommit> {
    List<PDMCommit> findByIdIn(Iterable<Integer> ids);
}
