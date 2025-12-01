package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMachineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface MachineTypeRepository extends JpaRepository<MESMachineType, Integer> {

    List<MESMachineType> findByIdIn(Iterable<Integer> ids);

    List<MESMachineType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESMachineType> findByParentTypeIsNullOrderByIdAsc();

    List<MESMachineType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MESMachineType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESMachineType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESMachineType> findByParentTypeOrderByIdAsc(Integer id);

    List<MESMachineType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESMachineType findByName(String name);
}
