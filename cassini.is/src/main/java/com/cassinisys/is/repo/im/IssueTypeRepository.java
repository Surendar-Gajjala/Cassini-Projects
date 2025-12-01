package com.cassinisys.is.repo.im;
/**
 * The Class is for IssueTypeRepository
 **/

import com.cassinisys.is.model.im.ISIssueType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueTypeRepository extends
        JpaRepository<ISIssueType, Integer> {
}
