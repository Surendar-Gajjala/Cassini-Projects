package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMFileRepository extends JpaRepository<PDMFile, Integer> {
    List<PDMFile> findByIdIn(Iterable<Integer> ids);
}
