package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.CustomRoadChalan;
import com.cassinisys.is.model.store.ISTopStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Repository
public interface CustomRoadChalanRepository extends JpaRepository<CustomRoadChalan, Integer>, QueryDslPredicateExecutor<CustomRoadChalan> {

    List<CustomRoadChalan> findByStore(ISTopStore topStore);
}
