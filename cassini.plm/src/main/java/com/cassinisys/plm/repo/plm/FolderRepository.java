package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.FolderType;
import com.cassinisys.plm.model.plm.PLMFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Repository
public interface FolderRepository extends JpaRepository<PLMFolder, Integer> {
    List<PLMFolder> findByParent(Integer id);

    PLMFolder findByParentIsNullAndTypeAndNameEqualsIgnoreCase(FolderType folderType, String name);

    PLMFolder findByParentIsNullAndOwnerAndTypeAndNameEqualsIgnoreCase(Integer personId, FolderType folderType,
            String name);

    PLMFolder findByParentAndTypeAndNameEqualsIgnoreCase(Integer parent, FolderType folderType, String name);

    PLMFolder findByParentAndOwnerAndTypeAndNameEqualsIgnoreCase(Integer parent, Integer personId,
            FolderType folderType, String name);

    List<PLMFolder> findByParentIsNullAndType(FolderType folderType);

    List<PLMFolder> findByParentIsNullAndTypeAndOwner(FolderType folderType, Integer person);

    List<PLMFolder> findByIdIn(Iterable<Integer> ids);
}
