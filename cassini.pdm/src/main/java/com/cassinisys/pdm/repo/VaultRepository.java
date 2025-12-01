package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMVault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Repository
public interface VaultRepository extends JpaRepository<PDMVault, Integer>,QueryDslPredicateExecutor<PDMVault>{

}
