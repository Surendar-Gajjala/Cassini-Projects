package com.cassinisys.platform.repo.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ActivityStreamRepository extends JpaRepository<ActivityStream, Long>,
        QueryDslPredicateExecutor<ActivityStream> {

    @Query("SELECT a FROM ActivityStream a WHERE a.object.object= :objectId")
    Page<ActivityStream> findByObjectId(@Param("objectId") Integer objectId, Pageable pageable);

    @Query("SELECT distinct a.activity FROM ActivityStream a WHERE a.object.object= :objectId and a.converter= :converter")
    List<String> findByObjectTypeActions(@Param("objectId") Integer objectId, @Param("converter") String converter);

    List<ActivityStream> findBySessionOrderByTimestampDesc(Integer session);

    @Query("select distinct i.session from ActivityStream i")
    List<Integer> getUniqueSeesionIds();

    @Query("SELECT distinct a.activity FROM ActivityStream a WHERE a.object.object IN :objectIds and a.converter= :converter")
    List<String> findByObjectTypeActionsIds(@Param("objectIds") List<Integer> objectIds, @Param("converter") String converter);
}
