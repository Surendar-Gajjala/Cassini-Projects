package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISScrapRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by Varsha Malgireddy on 9/4/2018.
 */
public interface ISScrapRequestItemRepository extends JpaRepository<ISScrapRequestItem, Integer>, QueryDslPredicateExecutor<ISScrapRequestItem> {

    List<ISScrapRequestItem> findByScrapRequest(Integer scrapId);

}
