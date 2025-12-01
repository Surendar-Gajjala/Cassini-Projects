package com.cassinisys.is.repo.dm;
/**
 * The Class is for ProjectFolderRepository
 **/

import com.cassinisys.is.model.dm.DocType;
import com.cassinisys.is.model.dm.ISProjectFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectFolderRepository extends JpaRepository<ISProjectFolder, Integer> {

    /**
     * The method used to findByProjectAndParentIsNullOrderByCreatedDateAsc of ISProjectFolder
     **/
    List<ISProjectFolder> findByProjectAndParentIsNullOrderByCreatedDateAsc(Integer projectId);

    /**
     * The method used to findByParentOrderByCreatedDateAsc of ISProjectFolder
     **/
    List<ISProjectFolder> findByParentOrderByCreatedDateAsc(Integer parentId);

    List<ISProjectFolder> findByParentAndFolderType(Integer id, DocType type);

    ISProjectFolder findByProjectAndParentIsNullAndNameEqualsIgnoreCaseAndFolderType(Integer parentId, String name, DocType type);

    ISProjectFolder findByProjectAndParentAndNameEqualsIgnoreCaseAndFolderType(Integer parentId, Integer parent, String name, DocType type);

    @Query(
            "SELECT f FROM ISProjectFolder f WHERE f.project=:projectId AND f.parent IS NULL AND " +
                    "f.folderType=:folderType ORDER BY f.createdDate"
    )
    /**
     * The method used to findRootFolders of ISProjectFolder
     **/
    List<ISProjectFolder> findRootFolders(@Param("projectId") Integer projectId,
                                          @Param("folderType") DocType folderType);

    /**
     * The method used to findByIdIn of ISProjectFolder
     **/
    public List<ISProjectFolder> findByIdIn(Iterable<Integer> ids);
}
