package com.cassinisys.drdo.repo.failureList;

import com.cassinisys.drdo.model.failureList.FailureList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Nageshreddy on 02-01-2019.
 */
@Repository
public interface FailureListRepository extends
        JpaRepository<FailureList, Integer> {

}