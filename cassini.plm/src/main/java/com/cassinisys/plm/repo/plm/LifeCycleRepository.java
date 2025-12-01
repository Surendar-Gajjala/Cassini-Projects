package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMLifeCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 18-05-2017.
 */
@Repository
public interface LifeCycleRepository extends JpaRepository<PLMLifeCycle, Integer>,
        QueryDslPredicateExecutor<PLMLifeCycle> {
    List<PLMLifeCycle> findAllByOrderByIdDesc();

    PLMLifeCycle findByName(String name);

    List<PLMLifeCycle> findByIdIn(Iterable<Integer> ids);

}
