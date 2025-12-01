package com.cassinisys.erp.repo.security;

import com.cassinisys.erp.model.security.ERPPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface PermissionRepository extends JpaRepository<ERPPermission, String> {
}
