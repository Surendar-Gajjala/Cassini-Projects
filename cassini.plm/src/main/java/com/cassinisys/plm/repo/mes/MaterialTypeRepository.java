package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface MaterialTypeRepository extends JpaRepository<MESMaterialType, Integer> {

    List<MESMaterialType> findByIdIn(Iterable<Integer> ids);

    List<MESMaterialType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESMaterialType> findByParentTypeIsNullOrderByIdAsc();

    List<MESMaterialType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MESMaterialType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESMaterialType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESMaterialType> findByParentTypeOrderByIdAsc(Integer id);

    List<MESMaterialType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESMaterialType findByName(String name);
}
