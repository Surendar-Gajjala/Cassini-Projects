package com.cassinisys.is.repo.im;
/**
 * The Class is for IssueStatusHistoryRepository
 **/

import com.cassinisys.is.model.im.ISIssueStatusHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueStatusHistoryRepository extends JpaRepository<ISIssueStatusHistory, Integer> {

    /**
     * The method used to findByIssue of ISIssueStatusHistory
     **/
    public Page<ISIssueStatusHistory> findByIssue(Integer issueId,
                                                  Pageable pageable);

}
