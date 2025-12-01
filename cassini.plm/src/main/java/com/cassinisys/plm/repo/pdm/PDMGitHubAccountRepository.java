package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMGitHubAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDMGitHubAccountRepository extends JpaRepository<PDMGitHubAccount, Integer> {
    PDMGitHubAccount findByName(String name);
}
