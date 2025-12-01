package com.cassinisys.drdo.repo;

import com.cassinisys.drdo.model.DRDOUpdates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 28-07-2019.
 */
@Repository
public interface DRDOUpdateRepository extends JpaRepository<DRDOUpdates, Integer> {

    List<DRDOUpdates> findByPersonOrderByDateDesc(Integer person);

    List<DRDOUpdates> findByPersonAndReadFalseOrderByDateDesc(Integer person);
}
