package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESServiceOrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 19-09-2020.
 */
@Repository
public interface ServiceOrderTypeRepository extends JpaRepository<MESServiceOrderType,Integer>{

    List<MESServiceOrderType> findByIdIn(Iterable<Integer> ids);

    List<MESServiceOrderType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESServiceOrderType> findByParentTypeOrderByCreatedDateAsc(Integer id);
    List<MESServiceOrderType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESServiceOrderType findByName(String name);
}
