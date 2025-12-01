package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface MESObjectRepository extends JpaRepository<MESObject, Integer>, QueryDslPredicateExecutor<MESObject> {
}
