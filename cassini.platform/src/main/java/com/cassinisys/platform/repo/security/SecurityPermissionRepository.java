package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.security.ModuleType;
import com.cassinisys.platform.model.security.SecurityPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface SecurityPermissionRepository extends JpaRepository<SecurityPermission, Integer> , QueryDslPredicateExecutor<SecurityPermission> {

    SecurityPermission findById(Integer permissionId);

    @Query("SELECT DISTINCT s.objectType FROM SecurityPermission s")
    List<String> findByObjectTypeDistinct();

    @Query("SELECT DISTINCT s.module FROM SecurityPermission s")
    List<ModuleType> findByModuleDistinct();

    List<SecurityPermission> findByObjectTypeAndSubType(String objectType, String subType);

    List<SecurityPermission> findByObjectType(String objectType);

    List<SecurityPermission> findByObjectTypeOrderByIdAsc(String objectType);

    @Query("SELECT MAX(s.id) FROM SecurityPermission s")
    Integer getMaxValue();

    List<SecurityPermission> findByObjectTypeAndPrivilege(String objectType, String privilege);
}
