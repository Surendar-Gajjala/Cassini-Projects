package com.cassinisys.plm.repo.mes;


import com.cassinisys.plm.model.mes.MESOperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 23-09-2020.
 */
@Repository
public interface OperationTypeRepository extends JpaRepository<MESOperationType, Integer> {
    List<MESOperationType> findByIdIn(Iterable<Integer> ids);

    List<MESOperationType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESOperationType> findByParentTypeIsNullOrderByIdAsc();

    List<MESOperationType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    List<MESOperationType> findByParentTypeOrderByIdAsc(Integer id);

    MESOperationType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESOperationType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESOperationType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESOperationType findByName(String name);
}
