package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMSuit;
import com.cassinisys.tm.model.TMTaskImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 03-08-2016.
 */
@Repository
public interface TaskImageRepository extends JpaRepository<TMTaskImage, Integer> {
    List<TMTaskImage> findByTask(Integer taskId);
}
