package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROObjectRepository extends JpaRepository<MROObject, Integer>, QueryDslPredicateExecutor<MROObject> {

}
