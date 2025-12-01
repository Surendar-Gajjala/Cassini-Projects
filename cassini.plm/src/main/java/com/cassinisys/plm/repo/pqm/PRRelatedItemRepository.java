package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMPRRelatedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface PRRelatedItemRepository extends JpaRepository<PQMPRRelatedItem, Integer> {

    List<PQMPRRelatedItem> findByProblemReport(Integer id);
}
