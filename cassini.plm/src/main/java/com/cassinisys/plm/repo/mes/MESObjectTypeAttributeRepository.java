package com.cassinisys.plm.repo.mes;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.mes.MESObjectTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface MESObjectTypeAttributeRepository extends JpaRepository<MESObjectTypeAttribute, Integer> {

    List<MESObjectTypeAttribute> findByTypeOrderBySeq(Integer type);

    List<MESObjectTypeAttribute> findByObjectType(ObjectType objectType);

    MESObjectTypeAttribute findByTypeAndName(Integer type, String name);

}
