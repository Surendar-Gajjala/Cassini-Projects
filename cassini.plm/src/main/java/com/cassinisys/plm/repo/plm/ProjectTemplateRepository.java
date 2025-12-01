package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.pm.ProjectTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 14-03-2018.
 */
@Repository
public interface ProjectTemplateRepository extends JpaRepository<ProjectTemplate, Integer>, QueryDslPredicateExecutor<ProjectTemplate> {

    Page<ProjectTemplate> findAllByOrderByCreatedDateAsc(Pageable pageable);

    ProjectTemplate findByNameEqualsIgnoreCase(String name);

    ProjectTemplate findByNameEqualsIgnoreCaseAndProgramTemplateIsNull(String name);

    ProjectTemplate findByNameEqualsIgnoreCaseAndProgramTemplate(String name, Integer programTemplate);

    List<ProjectTemplate> findByProgramTemplateIsNull();
}
