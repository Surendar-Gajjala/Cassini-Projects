package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinitionMaster;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 19-05-2017.
 */
@Repository
public interface PLMWorkflowDefinitionRepository extends JpaRepository<PLMWorkflowDefinition, Integer>, QueryDslPredicateExecutor<PLMWorkflowDefinition> {
    List<PLMWorkflowDefinition> findByName(String name);

    @Query(
            "SELECT i FROM PLMWorkflowDefinition i WHERE i.workflowType.id IN :typeIds"
    )
    Page<PLMWorkflowDefinition> getByWorkflowTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<PLMWorkflowDefinition> findByWorkflowType(PLMWorkflowType type);

    List<PLMWorkflowDefinition> findByWorkflowTypeAndReleasedTrue(PLMWorkflowType type);

    List<PLMWorkflowDefinition> getByMasterOrderByCreatedDateDesc(PLMWorkflowDefinitionMaster master);

    List<PLMWorkflowDefinition> findByMaster(PLMWorkflowDefinitionMaster master);

    PLMWorkflowDefinition findByMasterAndRevision(PLMWorkflowDefinitionMaster master, String revision);

    @Query(
            "SELECT count (i) FROM PLMWorkflowDefinition i WHERE i.workflowType.lifecycle.id= :lifecycle"
    )
    Integer getWorkflowsByLifeCycle(@Param("lifecycle") Integer lifecycle);

    @Query(
            "SELECT count (i) FROM PLMWorkflowDefinition i"
    )
    Integer getWorkflowsCount();

    @Query("select count (i) from PLMWorkflowDefinition i where i.master.latestRevision = i.id and ((LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.master.number as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.workflowType.name as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%')")
    Integer getWorkflowDefinitionCountBySearchQuery(@Param("searchText") String searchText);
}
