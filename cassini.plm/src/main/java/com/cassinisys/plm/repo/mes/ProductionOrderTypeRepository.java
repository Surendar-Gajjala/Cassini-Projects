package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESProductionOrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 19-09-2020.
 */
@Repository
public interface ProductionOrderTypeRepository extends JpaRepository<MESProductionOrderType, Integer> {

    List<MESProductionOrderType> findByIdIn(Iterable<Integer> ids);

    List<MESProductionOrderType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESProductionOrderType> findByParentTypeIsNullOrderByIdAsc();

    List<MESProductionOrderType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    List<MESProductionOrderType> findByParentTypeOrderByIdAsc(Integer id);

    MESProductionOrderType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESProductionOrderType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESProductionOrderType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESProductionOrderType findByName(String name);
}
