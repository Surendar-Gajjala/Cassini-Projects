package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFileRepository extends JpaRepository<CustomFile, Integer> {

    Page<CustomFile> findByLatestTrueAndNameContainsIgnoreCase(String name,Pageable pageable);
}
