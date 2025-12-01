package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMGitHubItemRevisionReleaseAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDMGitHubItemRevisionReleaseAssetRepository extends JpaRepository<PDMGitHubItemRevisionReleaseAsset, Integer> {
}
