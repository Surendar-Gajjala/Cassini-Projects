package com.cassinisys.erp.repo.security;

import com.cassinisys.erp.model.security.ERPRole;
import com.cassinisys.erp.model.security.ERPRolePermission;
import com.cassinisys.erp.model.security.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<ERPRolePermission, RolePermissionId> {

    @Query (
        "SELECT rp FROM ERPRolePermission rp WHERE rp.id.role = :role"
    )
    List<ERPRolePermission> findRolePermissions(@Param("role") ERPRole role);

    @Query(
        "SELECT DISTINCT(rp.id.role) FROM ERPRolePermission rp WHERE rp.id.permission.id IN :permissions"
    )
    List<ERPRole> getRolesWithPermission(@Param("permissions") List<String> permissions);

    @Modifying
    @Query (
        "DELETE FROM ERPRolePermission rp WHERE rp.id.role = :role"
    )
    void deleteRolePermissions(@Param("role") ERPRole role);
}
