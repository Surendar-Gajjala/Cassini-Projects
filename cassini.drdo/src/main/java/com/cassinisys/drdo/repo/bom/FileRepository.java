package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by subra on 07-10-2018.
 */
@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
}
