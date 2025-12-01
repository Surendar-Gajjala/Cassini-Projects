package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.GlossaryEntryEditStatus;
import com.cassinisys.plm.model.rm.PLMGlossaryEntryEdit;
import com.cassinisys.plm.model.rm.PLMGlossaryLanguages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 01-10-2018.
 */
@Repository
public interface GlossaryEntryEditRepository extends JpaRepository<PLMGlossaryEntryEdit, Integer>, QueryDslPredicateExecutor<PLMGlossaryEntryEdit> {

    @Query("SELECT i FROM PLMGlossaryEntryEdit i WHERE i.entry= :entryId and i.language.id= :languageId order by i.updatedDate DESC")
    List<PLMGlossaryEntryEdit> findByEntryAndLanguage(@Param("entryId") Integer entryId, @Param("languageId") Integer languageId);

    List<PLMGlossaryEntryEdit> findByEntryAndEditVersionOrderByUpdatedDateAsc(Integer entryId, Integer version);

    PLMGlossaryEntryEdit findByEntryAndLanguageAndLatestTrue(Integer entry, PLMGlossaryLanguages language);

    List<PLMGlossaryEntryEdit> findByEntryAndLanguageAndStatusOrderByAcceptedDateDesc(Integer entry, PLMGlossaryLanguages glossaryLanguage, GlossaryEntryEditStatus status);

    List<PLMGlossaryEntryEdit> findByEntryAndLanguageAndStatus(Integer entry, PLMGlossaryLanguages glossaryLanguage, GlossaryEntryEditStatus status);

    List<PLMGlossaryEntryEdit> findByEntryAndEditVersion(Integer entry, Integer version);
}