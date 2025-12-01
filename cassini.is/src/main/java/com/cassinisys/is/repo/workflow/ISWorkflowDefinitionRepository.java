package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 19-05-2017.
 */
@Repository
public interface ISWorkflowDefinitionRepository extends JpaRepository<ISWorkflowDefinition, Integer>, QueryDslPredicateExecutor<ISWorkflowDefinition> {

    ISWorkflowDefinition findByName(String name);

}
