package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.PLMGlossaryLanguages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 14-09-2018.
 */
@Repository
public interface GlossaryLanguagesRepository extends JpaRepository<PLMGlossaryLanguages, Integer> {

    PLMGlossaryLanguages findByLanguage(String language);

    PLMGlossaryLanguages findByDefaultLanguageTrue();

    List<PLMGlossaryLanguages> findAllByOrderByCreatedDateAsc();
}
