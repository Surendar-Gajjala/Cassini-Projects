package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.security.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

    Permission findById(String permissionId);
}
