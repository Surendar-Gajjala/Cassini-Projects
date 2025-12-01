package com.cassinisys.documents.repo;
/**
 * The Class is for DMObjectPermissionRepository
 **/

import com.cassinisys.documents.model.dm.DMObjectPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DMObjectPermissionRepository extends JpaRepository<DMObjectPermission, Integer> {

    List<DMObjectPermission> findByObjectId(Integer folderId);

}
