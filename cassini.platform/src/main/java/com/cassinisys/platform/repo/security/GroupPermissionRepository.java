package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.security.GroupPermission;
import com.cassinisys.platform.model.security.GroupPermissionId;
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
public interface GroupPermissionRepository extends JpaRepository<GroupPermission, GroupPermissionId> {

    @Query(
            "SELECT gp FROM GroupPermission gp WHERE gp.id.group = :group"
    )
    List<GroupPermission> findGroupPermissions(@Param("group") PersonGroup group);

    @Modifying
    @Query(
            "DELETE FROM GroupPermission gp WHERE gp.id.group = :group"
    )
    void deleteGroupPermissions(@Param("group") PersonGroup group);

    @Query(
            "SELECT rp FROM GroupPermission rp WHERE rp.id.group.groupId IN :groupIds"
    )
    List<GroupPermission> findBygroupIds(@Param("groupIds") Set<Integer> groupIds);

    @Query(
            "SELECT rp FROM GroupPermission rp WHERE rp.id.group.groupId IN :groupIds"
    )
    List<GroupPermission> getPermissionsByGroupIds(@Param("groupIds") List<Integer> groupIds);

    @Query("select gp.id.group from GroupPermission gp where gp.id.permission.id= :permissionId")
    List<PersonGroup> getPersonGroupsByPermission(@Param("permissionId") String permissionId);
}
