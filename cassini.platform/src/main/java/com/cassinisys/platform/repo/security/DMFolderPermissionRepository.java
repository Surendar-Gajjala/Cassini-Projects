package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.security.DMFolderPermission;
import com.cassinisys.platform.model.security.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface DMFolderPermissionRepository extends JpaRepository<DMFolderPermission, Integer> {

    List<DMFolderPermission> findByGroupId(Integer groupId);

    List<DMFolderPermission> findByFolderId(Integer folderId);

    List<DMFolderPermission> findByPermissionId(Integer permissionId);

    @Query(
            "SELECT rp FROM DMFolderPermission rp WHERE rp.groupId IN :groupIds"
    )
    List<DMFolderPermission> getDMFolderPermissionsByGroupIds(@Param("groupIds") List<Integer> groupIds);

}