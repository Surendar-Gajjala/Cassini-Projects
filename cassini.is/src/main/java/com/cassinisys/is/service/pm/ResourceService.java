package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.pm.ISProjectResource;
import com.cassinisys.is.repo.pm.ResourceRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for ResourceService
 */
@Service
@Transactional
public class ResourceService implements CrudService<ISProjectResource, Integer>,
        PageableService<ISProjectResource, Integer> {

    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * The method used to create ISProjectResource
     **/
    @Transactional(readOnly = false)
    @Override
    public ISProjectResource create(ISProjectResource isProjectResource) {
        checkNotNull(isProjectResource);
        isProjectResource.setId(null);
        return resourceRepository.save(isProjectResource);
    }

    /**
     * The method used to update ISProjectResource
     **/
    @Transactional(readOnly = false)
    @Override
    public ISProjectResource update(ISProjectResource isProjectResource) {
        checkNotNull(isProjectResource);
        checkNotNull(isProjectResource.getId());
        return resourceRepository.save(isProjectResource);
    }

    /**
     * The method used to delete
     **/
    @Transactional(readOnly = false)
    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        resourceRepository.delete(id);
    }

    /**
     * The method used to get ISProjectResource
     **/
    @Transactional(readOnly = true)
    @Override
    public ISProjectResource get(Integer id) {
        checkNotNull(id);
        ISProjectResource isProjectResource = resourceRepository.findOne(id);
        if (isProjectResource == null) {
            throw new ResourceNotFoundException();
        }
        return isProjectResource;
    }

    /**
     * The method used to getAll for the list of ISProjectResource
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISProjectResource> getAll() {
        return resourceRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISProjectResource
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISProjectResource> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("name")));
        }
        return resourceRepository.findAll(pageable);
    }

}

