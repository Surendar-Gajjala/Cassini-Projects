package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESAssemblyLineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 09-02-2021.
 */
@Repository
public interface AssemblyLineTypeRepository extends JpaRepository<MESAssemblyLineType, Integer> {

    List<MESAssemblyLineType> findByIdIn(Iterable<Integer> ids);

    List<MESAssemblyLineType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESAssemblyLineType> findByParentTypeIsNullOrderByIdAsc();

    List<MESAssemblyLineType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MESAssemblyLineType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESAssemblyLineType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESAssemblyLineType> findByParentTypeOrderByIdAsc(Integer id);

    List<MESAssemblyLineType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESAssemblyLineType findByName(String name);
}
