package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMVariance;
import com.cassinisys.plm.model.cm.VarianceType;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface VarianceRepository extends JpaRepository<PLMVariance, Integer>, QueryDslPredicateExecutor<PLMVariance> {
    List<PLMVariance> findByIdIn(List<Integer> ids);

    List<PLMVariance> findByVarianceType(VarianceType type);

    List<PLMVariance> findByVarianceNumber(String id);

    @Query("select count(i) from PLMVariance i where i.varianceType= :varianceType")
    Integer getTotalDeviations(@Param("varianceType") VarianceType varianceType);

    @Query("select count(i) from PLMVariance i where i.varianceType= :varianceType")
    Integer getTotalWaivers(@Param("varianceType") VarianceType varianceType);

    @Query("select count(i) from PLMVariance i where i.varianceType= :varianceType and i.statusType= :statusType")
    Integer getVariancesByTypeAndStatus(@Param("varianceType") VarianceType varianceType, @Param("statusType") WorkflowStatusType statusType);

    @Query("select count(i) from PLMVariance i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and i.varianceType= :varianceType" +
            " and (i.statusType= :statusType or i.statusType= :undefined) and w.started = true and w.onhold = false")
    Integer getVariancesByTypeAndStatusAndStarted(@Param("varianceType") VarianceType varianceType, @Param("statusType") WorkflowStatusType statusType, @Param("undefined") WorkflowStatusType undefined);

    @Query("select count(i) from PLMVariance i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and  w.onhold = true and i.varianceType= :varianceType")
    Integer getOnHoldByVarianceType(@Param("varianceType") VarianceType varianceType);

    @Query("select count(i) from PLMVariance i, com.cassinisys.plm.model.wf.PLMWorkflow w where i.id = w.attachedTo and  w.started = false and i.varianceType= :varianceType")
    Integer getNotStartedByVarianceType(@Param("varianceType") VarianceType varianceType);

    @Query("select count (i) from PLMVariance i where i.varianceType= :varianceType and ((LOWER(CAST(i.varianceNumber as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.title as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%')")
    Integer getVarianceCountBySearchQuery(@Param("varianceType") VarianceType varianceType, @Param("searchText") String searchText);

    @Query("select distinct i.originator from PLMVariance i where i.varianceType= :varianceType")
    List<Integer> getOriginator(@Param("varianceType") VarianceType varianceType);
    
    @Query("select distinct i.status from PLMVariance i where i.varianceType= :varianceType")
    List<String> getStatus(@Param("varianceType") VarianceType varianceType);
}
