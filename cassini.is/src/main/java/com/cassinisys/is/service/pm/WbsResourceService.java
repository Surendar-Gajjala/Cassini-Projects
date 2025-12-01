package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.pm.ISWbsResource;
import com.cassinisys.is.repo.pm.WbsResourceRepository;
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
 * The class is for WbsResourceService
 */
public class WbsResourceService implements CrudService<ISWbsResource, Integer>,
        PageableService<ISWbsResource, Integer> {

    @Autowired
    private WbsResourceRepository wbsResourceRepository;

    /**
     * The method used to create ISWbsResource
     **/
    @Override
    @Transactional(readOnly = false)
    public ISWbsResource create(ISWbsResource isWbsResource) {
        checkNotNull(isWbsResource);
        isWbsResource.setId(null);
        return wbsResourceRepository.save(isWbsResource);
    }

    /**
     * The method used to update ISWbsResource
     **/
    @Override
    @Transactional(readOnly = false)
    public ISWbsResource update(ISWbsResource isWbsResource) {
        checkNotNull(isWbsResource);
        checkNotNull(isWbsResource.getId());
        return wbsResourceRepository.save(isWbsResource);
    }

    /**
     * The method used to delete
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        wbsResourceRepository.delete(id);
    }

    /**
     * The method used to get ISWbsResource
     **/
    @Override
    @Transactional(readOnly = true)
    public ISWbsResource get(Integer id) {
        checkNotNull(id);
        ISWbsResource isWbsResource = wbsResourceRepository.findOne(id);
        if (isWbsResource == null) {
            throw new ResourceNotFoundException();
        }
        return isWbsResource;
    }

    /**
     * The method used to getAll for the list of ISWbsResource
     **/
    @Override
    @Transactional(readOnly = true)
    public List<ISWbsResource> getAll() {
        return wbsResourceRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISWbsResource
     **/
    @Override
    @Transactional(readOnly = true)
    public Page<ISWbsResource> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("name")));
        }
        return wbsResourceRepository.findAll(pageable);
    }
}
