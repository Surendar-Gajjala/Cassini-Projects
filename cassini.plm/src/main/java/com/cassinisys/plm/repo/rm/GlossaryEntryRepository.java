package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.PLMGlossaryEntry;
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
public interface GlossaryEntryRepository extends JpaRepository<PLMGlossaryEntry, Integer>, QueryDslPredicateExecutor<PLMGlossaryEntry> {

    List<PLMGlossaryEntry> findByLatestFalseAndNameEqualsIgnoreCase(String name);
//    Page<PLMGlossaryEntry> findByLatestTrueOrderByName(Pageable pageable);

    Page<PLMGlossaryEntry> findByLatestTrueOrderByCreatedDateAsc(Pageable pageable);

    List<PLMGlossaryEntry> getByLatestTrueOrderByCreatedDateAsc();

    List<PLMGlossaryEntry> findByDefaultNameIsNullAndDefaultDescriptionIsNullAndLatestTrueOrderByCreatedDateDesc();

    @Query("SELECT i from PLMGlossaryEntry i where i.latest = true and i.defaultDetail.name= :name")
    PLMGlossaryEntry findByLatestTrueAndDefaultDetail(@Param("name") String name);

    PLMGlossaryEntry findByLatestTrueAndDefaultName(String name);

    PLMGlossaryEntry findByLatestTrueAndName(String name);
//    List<PLMGlossaryEntry> findByNameEqualsIgnoreCaseOrderByCreatedDateDesc(String name);
}
