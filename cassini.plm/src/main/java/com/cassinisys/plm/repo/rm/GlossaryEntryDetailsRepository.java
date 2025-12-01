package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.PLMGlossaryEntryDetails;
import com.cassinisys.plm.model.rm.PLMGlossaryLanguages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 14-09-2018.
 */
@Repository
public interface GlossaryEntryDetailsRepository extends JpaRepository<PLMGlossaryEntryDetails, Integer> {

    PLMGlossaryEntryDetails findByGlossaryEntryAndLanguage(Integer glossaryEntry, PLMGlossaryLanguages glossaryLanguage);

    PLMGlossaryEntryDetails findByGlossaryEntryAndLanguageAndNameEqualsIgnoreCase(Integer glossaryEntry, PLMGlossaryLanguages plmGlossaryLanguage, String name);

    List<PLMGlossaryEntryDetails> findByGlossaryEntry(Integer glossaryEntry);

    List<PLMGlossaryEntryDetails> findByName(String name);

    @Query("select i from PLMGlossaryEntryDetails i where i.name= :name order by i.id desc")
    List<PLMGlossaryEntryDetails> findByNameOrderById(@Param("name") String name);

    List<PLMGlossaryEntryDetails> findByGlossaryEntryOrderByLanguage(Integer glossaryEntry);

    @Query("select i from PLMGlossaryEntryDetails i where i.glossaryEntry= :glossaryEntry and i.language.id= :languageId")
    List<PLMGlossaryEntryDetails> findByGlossaryEntryAndDefaultLanguage(@Param("glossaryEntry") Integer glossaryEntry, @Param("languageId") Integer languageId);

    @Query("select i from PLMGlossaryEntryDetails i where i.glossaryEntry= :glossaryEntry and i.language.id!= :languageId order by i.language Asc")
    List<PLMGlossaryEntryDetails> findByGlossaryEntryAndNotDefaultLanguage(@Param("glossaryEntry") Integer glossaryEntry, @Param("languageId") Integer languageId);
}
