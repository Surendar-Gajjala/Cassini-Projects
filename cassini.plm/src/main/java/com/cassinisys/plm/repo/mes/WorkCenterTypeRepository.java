package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESWorkCenterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Hello on 9/18/2020.
 */
@Repository
public interface WorkCenterTypeRepository extends JpaRepository<MESWorkCenterType, Integer> {

    List<MESWorkCenterType> findByIdIn(Iterable<Integer> ids);

    List<MESWorkCenterType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESWorkCenterType> findByParentTypeIsNullOrderByIdAsc();

    List<MESWorkCenterType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MESWorkCenterType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESWorkCenterType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESWorkCenterType> findByParentTypeOrderByIdAsc(Integer id);

    List<MESWorkCenterType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESWorkCenterType findByName(String name);
}
