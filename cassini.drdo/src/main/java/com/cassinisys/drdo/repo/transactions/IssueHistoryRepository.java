package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.IssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 06-08-2019.
 */
@Repository
public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Integer> {

    List<IssueHistory> findByIssueOrderByUpdatedDateDesc(Integer issue);
}
