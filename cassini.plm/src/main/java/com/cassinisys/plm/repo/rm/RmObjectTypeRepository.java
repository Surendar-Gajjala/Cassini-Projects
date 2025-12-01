package com.cassinisys.plm.repo.rm;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.rm.RmObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RmObjectTypeRepository extends JpaRepository<RmObjectType, Integer> {

    RmObjectType findByName(String name);

    List<RmObjectType> findByLifecycle(PLMLifeCycle lc);

    List<RmObjectType> findByIdIn(Iterable<Integer> ids);

    List<RmObjectType> findByNumberSourceId(Integer autoNumber);

    List<RmObjectType> findByRevisionSequence(Lov lov);

    @Query("select count (i) from RmObjectType i where i.revisionSequence.id= :lovId")
    Integer getRmObjectTypesByLov(@Param("lovId") Integer lovId);
}