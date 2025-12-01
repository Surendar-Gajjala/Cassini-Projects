package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMObjectThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDMThumbnailRepository extends JpaRepository<PDMObjectThumbnail, Integer> {
}
