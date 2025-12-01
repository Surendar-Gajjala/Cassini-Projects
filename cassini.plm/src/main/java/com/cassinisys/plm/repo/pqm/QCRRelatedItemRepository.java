package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMQCRRelatedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 17-06-2020.
 */
@Repository
public interface QCRRelatedItemRepository extends JpaRepository<PQMQCRRelatedItem, Integer> {

    List<PQMQCRRelatedItem> findByQcr(Integer id);
}
