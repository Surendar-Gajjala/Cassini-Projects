package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.security.Role;
import com.cassinisys.platform.model.security.RolePermission;
import com.cassinisys.platform.model.security.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    @Query (
        "SELECT rp FROM RolePermission rp WHERE rp.id.role = :role"
    )
    List<RolePermission> findRolePermissions(@Param("role") Role role);

    @Modifying
    @Query (
        "DELETE FROM RolePermission rp WHERE rp.id.role = :role"
    )
    void deleteRolePermissions(@Param("role") Role role);

    @Query(
            "SELECT rp FROM RolePermission rp WHERE rp.id.role.id IN :roleIds"
    )
    List<RolePermission> findByRoleIds(@Param("roleIds") Set<Integer> roleIds);
}
