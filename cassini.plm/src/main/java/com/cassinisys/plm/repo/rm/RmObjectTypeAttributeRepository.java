package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RmObjectTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RmObjectTypeAttributeRepository extends JpaRepository<RmObjectTypeAttribute, Integer> {
    List<RmObjectTypeAttribute> findByRmObjectTypeOrderByName(Integer typeId);

    List<RmObjectTypeAttribute> findByRmObjectTypeOrderBySeq(Integer typeId);

    RmObjectTypeAttribute findByRmObjectTypeAndName(Integer typeId, String name);

    List<RmObjectTypeAttribute> findByRmObjectType(Integer typeId);
}