package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.BDLReceive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 17-07-2019.
 */
@Repository
public interface BDLReceiveRepository extends JpaRepository<BDLReceive, Integer> {

    List<BDLReceive> findByIssue(Integer issue);
}
