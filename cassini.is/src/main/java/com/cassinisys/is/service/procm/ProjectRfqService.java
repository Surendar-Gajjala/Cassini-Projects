package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.procm.ISProjectRfq;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.procm.ProjectRfqRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for ProjectRfqService
 */
@Service
@Transactional
public class ProjectRfqService extends RfqService implements
        CrudService<ISProjectRfq, Integer>,
        PageableService<ISProjectRfq, Integer> {
    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectRfqRepository rfqRepository;

    /**
     * The method used to create ISProjectRfq
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProjectRfq create(ISProjectRfq rfq) {
        checkNotNull(rfq);
        rfq.setId(null);
        ISProject project = projectRepository.findOne(rfq.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        Login login = sessionWrapper.getSession().getLogin();
        rfq.setCreatedBy(login.getPerson().getId());
        rfq.setModifiedBy(login.getPerson().getId());
        rfq = rfqRepository.save(rfq);
        return rfq;
    }

    /**
     * The method used to update ISProjectRfq
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProjectRfq update(ISProjectRfq rfq) {
        checkNotNull(rfq);
        checkNotNull(rfq.getId());
        ISProject project = projectRepository.findOne(rfq.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        Login login = sessionWrapper.getSession().getLogin();
        rfq.setModifiedBy(login.getPerson().getId());
        rfq.setModifiedDate(new Date());
        rfq = rfqRepository.save(rfq);
        return rfq;
    }

    /**
     * The method used to delete
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISProjectRfq rfq = rfqRepository.findOne(id);
        if (rfq == null) {
            throw new ResourceNotFoundException();
        }
        ISProject project = projectRepository.findOne(rfq.getProject());
        rfqRepository.delete(rfq);

    }

    /**
     * The method used to update ISProjectRfq
     **/
    @Transactional(readOnly = true)
    @Override
    public ISProjectRfq get(Integer id) {
        checkNotNull(id);
        ISProjectRfq rfq = rfqRepository.findOne(id);
        if (rfq == null) {
            throw new ResourceNotFoundException();
        }
        return rfq;
    }

    /**
     * The method used to getAll for the list of  ISProjectRfq
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISProjectRfq> getAll() {
        return rfqRepository.findAll();
    }

    /**
     * The method used to findAll for the Page of  ISProjectRfq
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISProjectRfq> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "createdDate")));
        }
        return rfqRepository.findAll(pageable);
    }

    /**
     * The method used to findMultiple for the ll87\]ist of  ISProjectRfq
     **/
    @Transactional(readOnly = true)
    public List<ISProjectRfq> findMultiple(List<Integer> ids) {
        return rfqRepository.findByIdIn(ids);
    }

}
