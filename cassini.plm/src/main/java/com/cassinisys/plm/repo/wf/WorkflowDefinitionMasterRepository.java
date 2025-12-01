package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowDefinitionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 15-07-2020.
 */
@Repository
public interface WorkflowDefinitionMasterRepository extends JpaRepository<PLMWorkflowDefinitionMaster, Integer>, QueryDslPredicateExecutor<PLMWorkflowDefinitionMaster> {

	PLMWorkflowDefinitionMaster findByNumber(String number);

	@Query("SELECT count (i) FROM PLMWorkflowDefinitionMaster i")
	Integer getWorkflowsCount();
}
