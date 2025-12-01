package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMGitHubAccount;
import com.cassinisys.plm.model.pdm.PDMGitHubRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMGitHubRepositoryRepository extends JpaRepository<PDMGitHubRepository, Integer> {
    PDMGitHubRepository findByAccountAndName(PDMGitHubAccount account, String name);
    List<PDMGitHubRepository> findAllByAccount(PDMGitHubAccount account);
}
