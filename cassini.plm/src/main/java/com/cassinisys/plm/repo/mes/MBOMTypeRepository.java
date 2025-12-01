package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMBOMType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 09-02-2021.
 */
@Repository
public interface MBOMTypeRepository extends JpaRepository<MESMBOMType, Integer> {

    List<MESMBOMType> findByIdIn(Iterable<Integer> ids);

    List<MESMBOMType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESMBOMType> findByParentTypeIsNullOrderByIdAsc();

    List<MESMBOMType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MESMBOMType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESMBOMType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESMBOMType> findByParentTypeOrderByIdAsc(Integer id);

    List<MESMBOMType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESMBOMType findByName(String name);

    @Query("select distinct i.lifecycle from MESMBOMType i")
    List<PLMLifeCycle> getUniqueItemTypeLifeCycles();
}
