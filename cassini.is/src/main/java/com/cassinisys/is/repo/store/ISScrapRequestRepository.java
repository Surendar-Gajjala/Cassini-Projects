package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISScrapRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Varsha Malgireddy on 8/28/2018.
 */
@Repository
public interface ISScrapRequestRepository extends JpaRepository<ISScrapRequest, Integer>, QueryDslPredicateExecutor<ISScrapRequest> {

}