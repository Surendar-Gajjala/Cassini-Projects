package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMGitHubItemRepository;
import com.cassinisys.plm.model.pdm.PDMGitHubRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDMGitHubItemRepositoryRepository extends JpaRepository<PDMGitHubItemRepository, Integer> {
    PDMGitHubItemRepository findByItemAndRepository(Integer itemId, PDMGitHubRepository repo);
    PDMGitHubItemRepository findByItem(Integer item);
}
