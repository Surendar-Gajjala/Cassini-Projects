package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMGitHubItemRevisionRelease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDMGitHubItemRevisionReleaseRepository extends JpaRepository<PDMGitHubItemRevisionRelease, Integer> {
    PDMGitHubItemRevisionRelease findByItemRevision(Integer revision);
}
