package com.cassinisys.plm.repo.req;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.req.PLMRequirementObjectTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementObjectTypeAttributeRepository extends JpaRepository<PLMRequirementObjectTypeAttribute, Integer> {

    List<PLMRequirementObjectTypeAttribute> findByTypeOrderBySequence(Integer type);

    List<PLMRequirementObjectTypeAttribute> findByObjectType(ObjectType objectType);

    PLMRequirementObjectTypeAttribute findByTypeAndName(Integer typeId, String name);
}
