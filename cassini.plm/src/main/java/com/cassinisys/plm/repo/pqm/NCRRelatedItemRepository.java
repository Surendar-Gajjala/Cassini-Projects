package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMNCRRelatedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 17-06-2020.
 */
@Repository
public interface NCRRelatedItemRepository extends JpaRepository<PQMNCRRelatedItem, Integer> {
    List<PQMNCRRelatedItem> findByNcr(Integer ncr);
}
