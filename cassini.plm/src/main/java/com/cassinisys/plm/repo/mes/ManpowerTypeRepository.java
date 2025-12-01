package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESManpowerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 12-10-2020.
 */
@Repository
public interface ManpowerTypeRepository extends JpaRepository<MESManpowerType, Integer> {

    List<MESManpowerType> findByIdIn(Iterable<Integer> ids);

    List<MESManpowerType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESManpowerType> findByParentTypeIsNullOrderByIdAsc();

    MESManpowerType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESManpowerType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESManpowerType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    List<MESManpowerType> findByParentTypeOrderByIdAsc(Integer id);
    List<MESManpowerType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESManpowerType findByName(String name);
}
