package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISContractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 21/01/19.
 */
@Repository
public interface ContractorRepository extends JpaRepository<ISContractor, Integer>, QueryDslPredicateExecutor<ISContractor> {

    List<ISContractor> findByContact(Integer contactPerson);

    List<ISContractor> findByIdIn(List<Integer> ids);

    List<ISContractor> findByActive(Boolean flag);
}
