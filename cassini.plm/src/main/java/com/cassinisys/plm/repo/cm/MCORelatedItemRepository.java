package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMMCORelatedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by subramanyam on 15-06-2020.
 */
public interface MCORelatedItemRepository extends JpaRepository<PLMMCORelatedItem, Integer> {

    List<PLMMCORelatedItem> findByMco(Integer mco);
}
