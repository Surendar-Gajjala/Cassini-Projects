package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISMachineTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineTypeAttributeRepository extends JpaRepository<ISMachineTypeAttribute, Integer> {
    List<ISMachineTypeAttribute> findByItemTypeOrderByIdAsc(Integer typeId);
}
