package com.cassinisys.plm.repo.cm;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.cm.PLMChangeTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 1/3/2016.
 */
@Repository
public interface ChangeTypeAttributeRepository extends JpaRepository<PLMChangeTypeAttribute, Integer> {
    List<PLMChangeTypeAttribute> findByChangeTypeOrderByName(Integer id);

    List<PLMChangeTypeAttribute> findByChangeTypeOrderBySeq(Integer id);

    PLMChangeTypeAttribute findByChangeTypeAndName(Integer id, String name);

    List<PLMChangeTypeAttribute> findByChangeType(Integer id);

    List<PLMChangeTypeAttribute> findByObjectType(ObjectType objectType);
}
