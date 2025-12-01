package com.cassinisys.plm.repo.mro;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.mro.MROObjectTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROObjectTypeAttributeRepository extends JpaRepository<MROObjectTypeAttribute, Integer> {

    List<MROObjectTypeAttribute> findByTypeOrderBySeq(Integer type);

    List<MROObjectTypeAttribute> findByObjectType(ObjectType objectType);

    MROObjectTypeAttribute findByTypeAndName(Integer id, String name);

}
