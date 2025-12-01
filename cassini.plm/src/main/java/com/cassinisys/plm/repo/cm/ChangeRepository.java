package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.ChangeType;
import com.cassinisys.plm.model.cm.PLMChange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 1/3/2016.
 */
@Repository
public interface ChangeRepository extends JpaRepository<PLMChange, Integer>, QueryDslPredicateExecutor<PLMChange> {

    @Query("select i from PLMChange i where i.changeClass.id= :type")
    List<PLMChange> findByChangeClass(@Param("type") Integer type);

    Page<PLMChange> findByIdIn(Iterable<Integer> ids, Pageable pageable);

    @Query("select i from PLMChange i where i.changeType= :type")
    List<PLMChange> findByChangeType(@Param("type") Enum type);

    @Query("select i.id from PLMChange i where i.id in :ids and i.changeType= :changeType")
    List<Integer> getIdsByIdsAndType(@Param("ids") List<Integer> ids, @Param("changeType") ChangeType changeType);


}
