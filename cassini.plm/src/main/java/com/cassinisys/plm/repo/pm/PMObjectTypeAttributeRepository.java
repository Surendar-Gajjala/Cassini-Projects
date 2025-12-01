package com.cassinisys.plm.repo.pm;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.pm.PMObjectTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PMObjectTypeAttributeRepository extends JpaRepository<PMObjectTypeAttribute, Integer> {

    List<PMObjectTypeAttribute> findByTypeOrderBySequence(Integer type);

    List<PMObjectTypeAttribute> findByObjectType(ObjectType objectType);


}
