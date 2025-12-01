package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESPlantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface PlantTypeRepository extends JpaRepository<MESPlantType, Integer> {

    List<MESPlantType> findByIdIn(Iterable<Integer> ids);

    List<MESPlantType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESPlantType> findByParentTypeIsNullOrderByIdAsc();

    MESPlantType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESPlantType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESPlantType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    List<MESPlantType> findByParentTypeOrderByIdAsc(Integer id);
    List<MESPlantType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);
    MESPlantType findByName(String name);
}
