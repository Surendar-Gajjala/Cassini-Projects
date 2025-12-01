package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.PLMGlossaryEntry;
import com.cassinisys.plm.model.rm.PLMGlossaryEntryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 19-06-2018.
 */
@Repository
public interface GlossaryEntryItemRepository extends JpaRepository<PLMGlossaryEntryItem, Integer> {

    List<PLMGlossaryEntryItem> findByGlossary(Integer glossaryId);

    @Query("SELECT i from PLMGlossaryEntryItem i where i.glossary= :glossary order by i.entry.defaultDetail.name")
    List<PLMGlossaryEntryItem> getGlossaryEntryItemsOrderByName(@Param("glossary") Integer glossary);

    PLMGlossaryEntryItem findByGlossaryAndEntry(Integer glossary, PLMGlossaryEntry glossaryEntry);

    @Query("SELECT i from PLMGlossaryEntryItem i where (LOWER(i.entry.defaultDetail.name)  LIKE LOWER('%' || :search || '%')" +
            "or LOWER(i.entry.defaultDetail.description)  LIKE LOWER('%' || :search || '%'))" +
            "and i.glossary= :glossary")
    List<PLMGlossaryEntryItem> getGlossaryEntrySearchItems(@Param("glossary") Integer glossary, @Param("search") String search);

}
