package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.PortalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortalAccountRepository extends JpaRepository<PortalAccount, Integer> {
}
