package com.cassinisys.pdm.repo;
/**
 * The Class is for ISObjectPermissionRepository
 **/


import com.cassinisys.pdm.model.PDMObjectPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PDMObjectPermissionRepository extends JpaRepository<PDMObjectPermission, Integer> {

    List<PDMObjectPermission> findByObjectId(Integer folderId);
}
