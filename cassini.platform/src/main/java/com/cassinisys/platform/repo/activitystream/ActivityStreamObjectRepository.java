package com.cassinisys.platform.repo.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ActivityStreamObjectRepository extends JpaRepository<ActivityStreamObject, Long>,
        QueryDslPredicateExecutor<ActivityStreamObject> {

    @Query("SELECT DISTINCT s.type FROM ActivityStreamObject s")
    List<String> findObjectTypesDistinct();

    @Modifying
    @Query(
            value = "DELETE FROM ActivityStreamObject c WHERE c.object= :objectId"
    )
    @Transactional
    void deleteActivityStreamObjectByObjectId(@Param("objectId") Integer objectId);
}
