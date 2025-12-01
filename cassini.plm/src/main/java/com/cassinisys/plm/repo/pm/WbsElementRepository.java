package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMWbsElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */

@Repository
public interface WbsElementRepository extends JpaRepository<PLMWbsElement, Integer> {

    List<PLMWbsElement> findByProject(PLMProject project);

    List<PLMWbsElement> findByParent(Integer parent);

    List<PLMWbsElement> findByIdIn(Iterable<Integer> ids);

    List<PLMWbsElement> findByProjectAndParentIsNull(PLMProject projectId);

    PLMWbsElement findByProjectAndNameEqualsIgnoreCaseAndParent(PLMProject project, String wbsName, Integer parent);

    List<PLMWbsElement> findByProjectAndParentIsNullOrderByCreatedDateAsc(PLMProject projectId);

    List<PLMWbsElement> findByProjectAndParentIsNullOrderBySequenceNumberAsc(PLMProject projectId);

    List<PLMWbsElement> findByProjectIdAndParentIsNullOrderBySequenceNumberAsc(Integer projectId);

    List<PLMWbsElement> findByProjectIdInAndParentIsNullOrderBySequenceNumberAsc(Iterable<Integer> projectIds);

    List<PLMWbsElement> findByProjectAndParentIsNullOrderByIdAsc(PLMProject projectId);

    List<PLMWbsElement> findByParentOrderByCreatedDateAsc(Integer wbsId);

    PLMWbsElement findByProjectAndNameEqualsIgnoreCase(PLMProject project, String name);

    List<PLMWbsElement> findByProjectAndSequenceNumberIsNullOrderByCreatedDateAsc(PLMProject project);

    List<PLMWbsElement> findByProjectOrderBySequenceNumberAsc(PLMProject project);

    @Query("select count (i) from PLMWbsElement i where i.project.id= :project")
    Integer getWbsCountByProject(@Param("project") Integer project);
}
