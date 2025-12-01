package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.procm.ISMaterialReceiveType;
import com.cassinisys.is.model.store.ISStockReceive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 01/08/18.
 */
@Repository
public interface ISStockReceiveRepository extends JpaRepository<ISStockReceive, Integer>, QueryDslPredicateExecutor<ISStockReceive> {

    List<ISStockReceive> findByStore(Integer storeId);

    ISStockReceive findByReceiveNumberSource(String number);

    Page<ISStockReceive> findAllByMaterialReceiveTypeAndStore(ISMaterialReceiveType materialReceiveType, Integer storeId, Pageable pageable);

    Page<ISStockReceive> findAllByMaterialReceiveType(ISMaterialReceiveType materialReceiveType, Pageable pageable);

}
