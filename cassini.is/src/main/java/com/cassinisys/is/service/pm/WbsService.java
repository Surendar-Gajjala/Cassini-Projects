package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.pm.ISLinks;
import com.cassinisys.is.model.pm.ISWbs;
import com.cassinisys.is.repo.pm.LinksRepository;
import com.cassinisys.is.repo.pm.WbsRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for WbsService
 */
@Service
@Transactional
public class WbsService implements CrudService<ISWbs, Integer>,
        PageableService<ISWbs, Integer> {

    @Autowired
    private WbsRepository wbsRepository;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private LinksRepository linksRepository;

    /**
     * The method used to create ISWbs
     **/
    @Transactional(readOnly = false)
    @Override
    public ISWbs create(ISWbs wbs) {
        checkNotNull(wbs);
        wbs = wbsRepository.save(wbs);
        return wbs;
    }

    /**
     * The method used to update ISWbs
     **/
    @Transactional(readOnly = false)
    @Override
    public ISWbs update(ISWbs wbs) {
        checkNotNull(wbs);
        checkNotNull(wbs.getId());
        wbs = wbsRepository.save(wbs);
        return wbs;
    }

    /**
     * The method used to delete
     **/
    @Transactional(readOnly = false)
    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        ISWbs wbs = wbsRepository.findOne(id);
        if (wbs == null) {
            throw new ResourceNotFoundException();
        }
        wbsRepository.delete(wbs);
    }
    /**
     * The method used to get ISWbs
     **/
    @Transactional(readOnly = true)
    @Override
    public ISWbs get(Integer id) {
        checkNotNull(id);
        ISWbs wbs = wbsRepository.findOne(id);
        if (wbs == null) {
            throw new ResourceNotFoundException();
        }
        return wbs;
    }

    /**
     * The method used to getAll for the list of ISWbs
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISWbs> getAll() {
        return wbsRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISWbs
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISWbs> findAll(Pageable pageable) {
        checkNotNull(pageable);
        return wbsRepository.findAll(pageable);
    }

    /**
     * The method used to findMultiple for the list of ISWbs
     **/
    @Transactional(readOnly = true)
    public List<ISWbs> findMultiple(List<Integer> ids) {
        return wbsRepository.findByIdIn(ids);
    }

    /**
     * The method used to getRootTypes for the list of ISWbs
     **/
    @Transactional(readOnly = true)
    public List<ISWbs> getRootTypes(Integer id) {
        return wbsRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(id);
    }

    /**
     * The method used to getWbsTree for the list of ISWbs
     **/
    @Transactional(readOnly = true)
    public List<ISWbs> getWbsTree(Integer id) {
        List<ISWbs> wbsList = getRootTypes(id);
        for (ISWbs wbs : wbsList) {
            visitChildren(wbs, id);
        }
        return wbsList;
    }

    /**
     * The method used to visitChildren for the list of ISWbs from ISWbs parent
     **/
    private void visitChildren(ISWbs parent, Integer id) {
        List<ISWbs> children = getChildren(parent.getId(), id);
        for (ISWbs child : children) {
            visitChildren(child, id);
        }
        parent.setChildren(children);
    }

    /**
     * The method used to getChildren for the list of ISWbs
     *
     **/
    @Transactional(readOnly = true)
    public List<ISWbs> getChildren(Integer parent, Integer project) {
        return wbsRepository.findByProjectAndParentOrderByCreatedDateAsc(project, parent);
    }

    /**
     * The method used to findByProject for the page of ISWbs
     **/
    @Transactional(readOnly = true)
    public Page<ISWbs> findByProject(Integer id, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return wbsRepository.findByProject(id, pageable);
    }

    @Transactional(readOnly = true)
    public List<ISWbs> findByProjectAndParentOrderByActualStartDateAsc(Integer project, Integer parent) {
        return wbsRepository.findByProjectAndParentOrderByActualStartDateAsc(project, parent);
    }

    @Transactional(readOnly = true)
    public List<ISWbs> findByProjectAndParentOrderByActualFinishDateDesc(Integer project, Integer parent) {
        return wbsRepository.findByProjectAndParentOrderByActualFinishDateDesc(project, parent);
    }

    public List<ISWbs> allWbsByProject(Integer project) {
        return wbsRepository.findByProject(project);
    }
}
