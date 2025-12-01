package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementObjectTypeRepository extends JpaRepository<PLMRequirementObjectType, Integer> {

    List<PLMRequirementObjectType> findByIdIn(Iterable<Integer> ids);

}
