package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISMachineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineTypeRepository extends JpaRepository<ISMachineType, Integer> {
    @Query("SELECT m FROM ISMachineType m where m.parentType = null ORDER BY lower(m.name)")
    List<ISMachineType> findByParentTypeIsNullOrderByName();

    @Query("SELECT m FROM ISMachineType m where m.parentType = :parent ORDER BY lower(m.name)")
    List<ISMachineType> findByParentTypeOrderByName(@Param("parent") Integer parent);

    ISMachineType findByName(String itemType);

    List<ISMachineType> findByIdIn(Iterable<Integer> ids);

    List<ISMachineType> findByMachineNumberSourceId(Integer autoNumber);

    List<ISMachineType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<ISMachineType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);
}
