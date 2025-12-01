package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMObjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDMObjectFileRepository extends JpaRepository<PDMObjectFile, Integer> {
}
