package com.cassinisys.is.repo.col;
/**
 * The Class is for ProjectUserInboxRepository
 **/

import com.cassinisys.is.model.col.ISProjectUserInbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUserInboxRepository extends JpaRepository<ISProjectUserInbox, Integer> {
}
