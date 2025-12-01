package com.cassinisys.is.repo.dm;

import com.cassinisys.is.model.dm.ISTopFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 018 18-Nov -17.
 */
@Repository
public interface TopFolderRepository extends JpaRepository<ISTopFolder, Integer> {

    List<ISTopFolder> findByParentIsNullOrderByCreatedDateAsc();

    List<ISTopFolder> findByParentOrderByCreatedDateAsc(Integer parent);

    List<ISTopFolder> findByParent(Integer id);

    ISTopFolder findByParentIsNullAndNameEqualsIgnoreCase(String name);

    ISTopFolder findByParentAndNameEqualsIgnoreCase(Integer parent, String name);
}
