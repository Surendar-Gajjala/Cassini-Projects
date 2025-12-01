package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.pm.ISProjectResource;
import com.cassinisys.is.repo.pm.TaskResourceRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for TaskResourceService
 */
public class TaskResourceService implements CrudService<ISProjectResource, Integer>,
        PageableService<ISProjectResource, Integer> {

    @Autowired
    private TaskResourceRepository taskResourceRepository;

    /**
     * The method used to create ISProjectResource
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProjectResource create(ISProjectResource isTaskResource) {
        checkNotNull(isTaskResource);
        isTaskResource.setId(null);
        return taskResourceRepository.save(isTaskResource);
    }

    /**
     * The method used to update ISProjectResource
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProjectResource update(ISProjectResource isTaskResource) {
        checkNotNull(isTaskResource);
        checkNotNull(isTaskResource.getId());
        return taskResourceRepository.save(isTaskResource);
    }

    /**
     * The method used to delete
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        taskResourceRepository.delete(id);
    }

    /**
     * The method used to get ISProjectResource
     **/
    @Override
    @Transactional(readOnly = true)
    public ISProjectResource get(Integer id) {
        checkNotNull(id);
        ISProjectResource isTaskResource = taskResourceRepository.findOne(id);
        if (isTaskResource == null) {
            throw new ResourceNotFoundException();
        }
        return isTaskResource;
    }

    /**
     * The method used to getAll  for the list of  ISProjectResource
     **/
    @Override
    @Transactional(readOnly = true)
    public List<ISProjectResource> getAll() {
        return taskResourceRepository.findAll();
    }

    /**
     * The method used to findAll for the page of  ISProjectResource
     **/
    @Override
    @Transactional(readOnly = true)
    public Page<ISProjectResource> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("name")));
        }
        return taskResourceRepository.findAll(pageable);
    }
}
