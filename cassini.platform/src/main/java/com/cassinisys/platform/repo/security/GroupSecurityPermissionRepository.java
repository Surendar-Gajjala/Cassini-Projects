package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.security.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by lakshmi on 10/20/2016.
 */
@Repository
public interface GroupSecurityPermissionRepository extends JpaRepository<GroupSecurityPermission, GroupSecurityPermissionId> {

    @Query(
            "SELECT gp FROM GroupSecurityPermission gp WHERE gp.id.group = :group"
    )
    List<GroupSecurityPermission> findGroupSecurityPermissions(@Param("group") PersonGroup group);

    @Modifying
    @Query(
            "DELETE FROM GroupSecurityPermission gp WHERE gp.id.group = :group AND gp.id.securityPermission = :securityPermission"
    )
    void deleteGroupSecurityPermissions(@Param("group") PersonGroup group, @Param("securityPermission") SecurityPermission securityPermission);

    @Query(
            "SELECT rp FROM GroupSecurityPermission rp WHERE rp.id.group.groupId IN :groupIds"
    )
    List<GroupSecurityPermission> findBygroupIds(@Param("groupIds") Set<Integer> groupIds);

    @Query(
            "SELECT rp.id.securityPermission FROM GroupSecurityPermission rp WHERE rp.id.group.groupId = :groupId"
    )
    List<SecurityPermission> getSecurityPermissionsByGroupId(@Param("groupId") Integer groupId);


    @Query(
            "SELECT rp FROM GroupSecurityPermission rp WHERE rp.id.group.groupId IN :groupIds"
    )
    List<GroupSecurityPermission> getSecurityPermissionsByGroupIds(@Param("groupIds") List<Integer> groupIds);

    @Query("select gp.id.group from GroupSecurityPermission gp where gp.id.securityPermission.id IN :permissionId")
    List<PersonGroup> getPersonGroupsByPermission(@Param("permissionId") Integer permissionId);

    @Query(
            "SELECT DISTINCT rp.id.securityPermission.objectType FROM GroupSecurityPermission rp WHERE rp.id.group.groupId = :groupId"
    )
    List<String> getSecurityPermissionsObjectTypeByGroupId(@Param("groupId") Integer groupId);

    @Query(
            "SELECT DISTINCT rp.id.securityPermission.subType FROM GroupSecurityPermission rp WHERE rp.id.group.groupId = :groupId"
    )
    List<String> getSecurityPermissionsSubTypeByGroupId(@Param("groupId") Integer groupId);
}
