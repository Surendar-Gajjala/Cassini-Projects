package com.cassinisys.is.repo.pm;
/**
 * The Class is for ResourceRepository
 **/

import com.cassinisys.is.model.pm.ISProjectResource;
import com.cassinisys.is.model.pm.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends JpaRepository<ISProjectResource, Integer> {
    /**
     * The method used to findByProjectAndTask of ISProjectResource
     **/
    List<ISProjectResource> findByProjectAndTask(Integer projectId, Integer taskId);

    List<ISProjectResource> findByProjectAndTaskIn(Integer projectId, Iterable<Integer> taskIds);

    /**
     * The method used to findByResourcesIds of ISProjectResource
     **/
    List<ISProjectResource> findByIdIn(Iterable<Integer> resourceIds);

    /**
     * The method used to findByProjectAndReferenceIdIn of ISProjectResource
     **/
    List<ISProjectResource> findByProjectAndReferenceIdAndResourceType(Integer projectId, Integer refId, ResourceType resourceType);

    /**
     * The method used to findByProjectAndTaskAndResourceType of ISProjectResource
     **/
    List<ISProjectResource> findByProjectAndTaskAndResourceType(Integer projectId, Integer taskId, ResourceType resourceType);

    List<ISProjectResource> findByTaskAndResourceType(Integer taskId, ResourceType resourceType);

    List<ISProjectResource> findByReferenceIdIn(Iterable<Integer> refIds);

    List<ISProjectResource> findByProject(Integer project);

    ISProjectResource findByTaskAndReferenceIdAndResourceType(Integer taskId, Integer referenceId, ResourceType resourceType);

    List<ISProjectResource> findByTaskAndReferenceId(Integer taskId, Integer referenceId);

    ISProjectResource findByProjectAndReferenceIdAndTaskAndResourceType(Integer projectId, Integer refId, Integer task, ResourceType resourceType);

    @Query("SELECT DISTINCT(i.referenceId) from ISProjectResource i where i.project= :projectId and i.resourceType= :resourceType")
    List<Integer> findUniqueReferenceIdsByProjectAndResourceType(@Param("projectId") Integer projectId, @Param("resourceType") ResourceType resourceType);

}
