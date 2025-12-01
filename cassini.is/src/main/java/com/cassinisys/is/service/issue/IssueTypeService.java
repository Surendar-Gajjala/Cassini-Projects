package com.cassinisys.is.service.issue;

import com.cassinisys.is.model.im.ISIssueType;
import com.cassinisys.is.repo.im.IssueTypeRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for IssueTypeService
 */
@Service
@Transactional
public class IssueTypeService implements CrudService<ISIssueType, Integer>,
        PageableService<ISIssueType, Integer> {

    @Autowired
    private IssueTypeRepository issueTypeRepository;

    /**
     * The method used create  ISIssueType
     **/
    @Override
    @Transactional(readOnly = false)
    public ISIssueType create(ISIssueType issueType) {
        checkNotNull(issueType);
        issueType.setId(null);
        return issueTypeRepository.save(issueType);
    }

    /**
     * The method used update  ISIssueType
     **/
    @Override
    @Transactional(readOnly = false)
    public ISIssueType update(ISIssueType issueType) {
        checkNotNull(issueType);
        checkNotNull(issueType.getId());
        return issueTypeRepository.save(issueType);
    }

    /**
     * The method used delete
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        issueTypeRepository.delete(id);
    }

    /**
     * The method used to get  ISIssueType
     **/
    @Transactional(readOnly = true)
    @Override
    public ISIssueType get(Integer id) {
        checkNotNull(id);
        ISIssueType issueType = issueTypeRepository.findOne(id);
        if (issueType == null) {
            throw new ResourceNotFoundException();
        }
        return issueTypeRepository.save(issueType);
    }

    /**
     * The method used to getAll for the  List of  ISIssueType
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISIssueType> getAll() {
        return issueTypeRepository.findAll();
    }

    /**
     * The method used to findAll for the  page of  ISIssueType
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISIssueType> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("typeCode")));
        }
        return issueTypeRepository.findAll(pageable);
    }
}
