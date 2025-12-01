package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementDocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 10-11-2020.
 */
@Repository
public interface PLMRequirementDocumentTemplateRepository extends JpaRepository<PLMRequirementDocumentTemplate,Integer>, QueryDslPredicateExecutor<PLMRequirementDocumentTemplate> {

}
