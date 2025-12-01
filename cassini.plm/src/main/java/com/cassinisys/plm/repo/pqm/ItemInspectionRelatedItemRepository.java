package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMItemInspectionRelatedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface ItemInspectionRelatedItemRepository extends JpaRepository<PQMItemInspectionRelatedItem, Integer> {

    List<PQMItemInspectionRelatedItem> findByInspection(Integer id);
}
