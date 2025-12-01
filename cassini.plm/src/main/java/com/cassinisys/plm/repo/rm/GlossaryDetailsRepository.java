package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.PLMGlossaryDetails;
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
public interface GlossaryDetailsRepository extends JpaRepository<PLMGlossaryDetails, Integer> {

    PLMGlossaryDetails findByGlossaryAndLanguage(Integer glossary, PLMGlossaryLanguages glossaryLanguage);

    List<PLMGlossaryDetails> findByGlossary(Integer glossary);

    List<PLMGlossaryDetails> findByGlossaryOrderByLanguage(Integer glossary);

    @Query("select i from PLMGlossaryDetails i where i.glossary= :glossary and i.language.id= :languageId")
    List<PLMGlossaryDetails> findByGlossaryByDefaultLanguage(@Param("glossary") Integer glossary, @Param("languageId") Integer languageId);

    @Query("select i from PLMGlossaryDetails i where i.glossary= :glossary and i.language.id!= :languageId order by i.language Asc")
    List<PLMGlossaryDetails> findByGlossaryByNotDefaultLanguage(@Param("glossary") Integer glossary, @Param("languageId") Integer languageId);
}
