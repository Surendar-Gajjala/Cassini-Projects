package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.PLMGlossary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 19-06-2018.
 */
@Repository
public interface GlossaryRepository extends JpaRepository<PLMGlossary, Integer>, QueryDslPredicateExecutor<PLMGlossary> {

    List<PLMGlossary> findByLatestFalseAndNameEqualsIgnoreCase(String name);
//    List<PLMGlossary> findByLatestTrueAndNameEqualsIgnoreCase(String name);

    List<PLMGlossary> findByLatestTrue();

    List<PLMGlossary> findByLatestTrueAndIsReleasedFalse();

    Page<PLMGlossary> findByLatestTrueOrderByModifiedDateDesc(Pageable pageable);

    List<PLMGlossary> findByNumberOrderByCreatedDateDesc(String number);
//    List<PLMGlossary> findByNameEqualsIgnoreCaseOrderByCreatedDateDesc(String name);

    @Query("SELECT i from PLMGlossary i where i.latest = true and i.defaultDetail.name= :name")
    PLMGlossary findByLatestTrueAndDefaultDetail(@Param("name") String name);

    @Query(
            "SELECT count (i)FROM PLMGlossary i WHERE i.lifeCyclePhase.lifeCycle= :lifecycle"
    )
    Integer getGlossariesByLifeCycle(@Param("lifecycle") Integer lifecycle);
}

