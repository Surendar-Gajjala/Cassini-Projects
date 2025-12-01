package com.cassinisys.plm.repo.pgc;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.pgc.PGCObjectTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface PGCObjectTypeAttributeRepository extends JpaRepository<PGCObjectTypeAttribute, Integer> {

    List<PGCObjectTypeAttribute> findByTypeOrderBySeq(Integer type);

    List<PGCObjectTypeAttribute> findByObjectType(ObjectType objectType);

    PGCObjectTypeAttribute findByTypeAndName(Integer type, String name);

}
