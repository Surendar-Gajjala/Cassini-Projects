package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESEquipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface EquipmentTypeRepository extends JpaRepository<MESEquipmentType, Integer> {

    List<MESEquipmentType> findByIdIn(Iterable<Integer> ids);

    List<MESEquipmentType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESEquipmentType> findByParentTypeIsNullOrderByIdAsc();

    MESEquipmentType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESEquipmentType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESEquipmentType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    List<MESEquipmentType> findByParentTypeOrderByIdAsc(Integer id);
    List<MESEquipmentType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESEquipmentType findByName(String name);
}
