package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMQCRCAPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface QCRCAPARepository extends JpaRepository<PQMQCRCAPA, Integer> {

    List<PQMQCRCAPA> findByQcrOrderByLatestDescModifiedDateDesc(Integer id);
}
