package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author reddy
 */
@Repository
public interface ObjectRepository extends JpaRepository<CassiniObject, Integer> {

    public CassiniObject findByIdAndObjectType(Integer id, Enum objectType);

    List<CassiniObject> findByObjectType(ObjectType objectType);

    List<CassiniObject> findByIdInAndObjectType(List<Integer> objectIds, ObjectType objectType);

    CassiniObject findById(Integer id);

    List<CassiniObject> findByIdInOrderByModifiedDateDesc(Iterable<Integer> ids);

    @Modifying
    @Query(
            "DELETE FROM CassiniObject c WHERE c.id= :id"
    )
    void deleteById(@Param("id") Integer id);

    @Query(
            "SELECT i.objectType FROM CassiniObject i WHERE i.id= :id"
    )
    Enum getObjectTypeById(@Param("id") Integer id);

}
