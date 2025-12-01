package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.ProgramTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by smukka on 18-06-2022.
 */
@Repository
public interface ProgramTemplateRepository extends JpaRepository<ProgramTemplate, Integer>, QueryDslPredicateExecutor<ProgramTemplate> {
    ProgramTemplate findByNameEqualsIgnoreCase(String name);
}
