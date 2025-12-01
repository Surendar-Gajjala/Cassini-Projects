package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMFolderObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Repository
public interface FolderObjectRepository extends JpaRepository<PLMFolderObject, Integer> {
    List<PLMFolderObject> findByFolder(Integer id);
}
