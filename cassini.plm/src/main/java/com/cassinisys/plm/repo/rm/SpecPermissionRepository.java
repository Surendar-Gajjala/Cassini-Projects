package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.SpecPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Gsr on 19-11-2018.
 */
public interface SpecPermissionRepository extends JpaRepository<SpecPermission, Integer> {

    List<SpecPermission> findBySpecification(Integer specId);

    SpecPermission findBySpecificationAndSpecUser(Integer specification, Integer specUser);

}
