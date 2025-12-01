package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.pm.PLMActivityItemReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 20-06-2018.
 */
@Repository
public interface ActivityItemReferenceRepository extends JpaRepository<PLMActivityItemReference, Integer>, QueryDslPredicateExecutor<PLMActivityItemReference> {

    List<PLMActivityItemReference> findByActivity(Integer activity);

    @Query("select i.item.id from PLMActivityItemReference i where i.activity= :activity")
    List<Integer> getItemIdsByActivity(@Param("activity") Integer activity);

    PLMActivityItemReference findByActivityAndItem(Integer activity, PLMItemRevision item);
}
