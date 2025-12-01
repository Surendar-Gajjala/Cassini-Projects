package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.security.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

    Privilege findById(String permissionId);
}
