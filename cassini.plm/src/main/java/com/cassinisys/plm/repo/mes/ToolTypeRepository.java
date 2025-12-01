package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESToolType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 19-09-2020.
 */
@Repository
public interface ToolTypeRepository extends JpaRepository<MESToolType, Integer> {

    List<MESToolType> findByIdIn(Iterable<Integer> ids);

    List<MESToolType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESToolType> findByParentTypeIsNullOrderByIdAsc();

    List<MESToolType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MESToolType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESToolType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESToolType> findByParentTypeOrderByIdAsc(Integer id);

    List<MESToolType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESToolType findByName(String name);
}
