package com.cassinisys.plm.repo.plm;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.plm.PLMSharedObject;
import com.cassinisys.plm.model.plm.SharePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 20-10-2017.
 */
@Repository
public interface SharedObjectRepository extends JpaRepository<PLMSharedObject, Integer>, QueryDslPredicateExecutor<PLMSharedObject> {
    List<PLMSharedObject> findByObjectIdAndSharedTo(Integer objectId, Integer sharedTo);

    List<PLMSharedObject> findByObjectIdAndSharedToAndPermission(Integer objectId, Integer sharedTo, SharePermission sharePermission);

    List<PLMSharedObject> findByObjectId(Integer objectId);

    @Query("select count (i) from PLMSharedObject i where i.objectId= :objectId")
    Integer getSharedCountByObjectId(@Param("objectId") Integer objectId);

    PLMSharedObject findByObjectIdAndPermission(Integer objectId, SharePermission sharePermission);

    List<PLMSharedObject> findBySharedTo(Integer personId);

    Page<PLMSharedObject> findBySharedObjectTypeAndSharedBy(ObjectType value, Integer personId, Pageable pageable);

    Page<PLMSharedObject> findBySharedObjectTypeAndSharedTo(ObjectType value, Integer groupId, Pageable pageable);

    Page<PLMSharedObject> findBySharedBy(Integer personId, Pageable pageable);

    Page<PLMSharedObject> findBySharedByAndSharedTo(Integer personId, Integer sharedTo, Pageable pageable);

    Page<PLMSharedObject> findBySharedObjectTypeAndSharedByAndSharedTo(ObjectType value, Integer personId, Integer sharedTo, Pageable pageable);

    Page<PLMSharedObject> findBySharedToIn(Iterable<Integer> sharedTo, Pageable pageable);

    Page<PLMSharedObject> findBySharedObjectTypeAndSharedToIn(ObjectType value, Iterable<Integer> sharedTo, Pageable pageable);

    Page<PLMSharedObject> findBySharedObjectType(ObjectType value, Pageable pageable);

    @Query("select count(i) from PLMSharedObject i where i.sharedObjectType= :sharedObjectType")
    long getObjectTypeCount(@Param("sharedObjectType") ObjectType sharedObjectType);

    List<PLMSharedObject> findBySharedObjectTypeAndSharedToIn(ObjectType value, Iterable<Integer> sharedIds);

    @Query("select i from PLMSharedObject i where i.sharedObjectType= :objectType and i.sharedTo= :sharedTo")
    List<PLMSharedObject> getObjectIdsByObjectTypeAndSharedTo(@Param("objectType") ObjectType objectType, @Param("sharedTo") Integer sharedTo);

    @Query(" select i.objectId from PLMSharedObject i where i.sharedObjectType= :objectType")
    List<Integer> getObjectIdsByObjectType(@Param("objectType") ObjectType objectType);

    @Query("select count(i) from PLMSharedObject i where i.sharedObjectType= :objectType and i.sharedTo= :sharedTo")
    Integer findByObjectTypeAndSharedTo(@Param("objectType") ObjectType objectType, @Param("sharedTo") Integer sharedTo);

    @Query("select i from PLMSharedObject i where i.objectId in :objectIds and i.sharedTo= :sharedTo")
    List<PLMSharedObject> getSharedObjectByObjectIdsInAndPerson(@Param("objectIds") List<Integer> objectIds, @Param("sharedTo") Integer sharedTo);

    @Query("select distinct i.sharedTo from PLMSharedObject i")
    List<Integer> getSharedToIds();

    @Query("select count(i) from PLMSharedObject i where i.sharedObjectType= :sharedType and i.sharedTo= :sharedTo")
    Integer getCountByShareTypeAndSharedTo(@Param("sharedType") ObjectType sharedType, @Param("sharedTo") Integer sharedTo);

}
