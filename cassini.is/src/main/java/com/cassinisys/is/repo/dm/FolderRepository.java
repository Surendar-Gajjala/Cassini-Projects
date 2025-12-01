package com.cassinisys.is.repo.dm;
/**
 * The Class is for FolderRepository
 **/

import com.cassinisys.is.model.dm.ISFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<ISFolder, Integer> {
}
