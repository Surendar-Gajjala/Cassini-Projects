package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMCommit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

public interface CommitRepository extends JpaRepository<PDMCommit, Integer>, QueryDslPredicateExecutor<PDMCommit> {
    Page<PDMCommit> findAllByOrderByCreatedDateDesc(Pageable pageable);
    List<PDMCommit> findAllByOrderByCreatedDateDesc();
}
