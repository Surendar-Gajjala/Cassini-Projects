package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomRevisionedObjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomRevisionedObjectFileRepository extends JpaRepository<CustomRevisionedObjectFile, Integer> {
}
