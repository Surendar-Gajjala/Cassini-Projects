package com.cassinisys.is.repo.dm;
/**
 * The Class is for ISObjectPermissionRepository
 **/

import com.cassinisys.is.model.dm.ISObjectPermission;
import com.cassinisys.is.model.dm.ObjectType;
import com.cassinisys.is.model.dm.PermissionLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISObjectPermissionRepository extends JpaRepository<ISObjectPermission, Integer> {

    List<ISObjectPermission> findByObjectId(Integer folderId);

    ISObjectPermission findByObjectIdAndPermissionAssignedTo(Integer objectId, Integer personId);

    ISObjectPermission findByobjectTypeAndPermissionAssignedToAndPermissionLevel(ObjectType objectType, Integer personId, PermissionLevel permissionLevel);
}
