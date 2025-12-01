package com.cassinisys.is.service.tm;

import com.cassinisys.is.filtering.BidTaskCriteria;
import com.cassinisys.is.filtering.BidTaskPredicateBuilder;
import com.cassinisys.is.model.pm.ISBid;
import com.cassinisys.is.model.tm.ISBidTask;
import com.cassinisys.is.model.tm.ISTaskStatusHistory;
import com.cassinisys.is.model.tm.QISBidTask;
import com.cassinisys.is.repo.pm.BidRepository;
import com.cassinisys.is.repo.tm.BidTaskRepository;
import com.cassinisys.is.repo.tm.TaskStatusHistoryRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
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
 * The Class is for BidTaskService
 **/

@Service
@Transactional
public class BidTaskService extends TaskService implements
        CrudService<ISBidTask, Integer>, PageableService<ISBidTask, Integer> {

    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private BidTaskRepository bidTaskRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private TaskStatusHistoryRepository taskStatusHistoryRepository;

    @Autowired
    private BidTaskPredicateBuilder predicateBuilder;

    /**
     * The Method is used to create ISBidTask
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidTask create(ISBidTask bidTask) {
        checkNotNull(bidTask);
        bidTask.setId(null);
        ISBid bid = bidRepository.findOne(bidTask.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        bidTask = bidTaskRepository.save(bidTask);
        return bidTask;
    }

    /**
     * The Method is used to update ISBidTask
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidTask update(ISBidTask bidTask) {
        checkNotNull(bidTask);
        checkNotNull(bidTask.getId());
        ISBidTask prev = bidTaskRepository.findOne(bidTask.getId());
        if (prev == null) {
            throw new ResourceNotFoundException();
        }
        ISBid bid = bidRepository.findOne(bidTask.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        Login login = sessionWrapper.getSession().getLogin();
        if (!prev.getStatus().equals(bidTask.getStatus())) {
            ISTaskStatusHistory history = new ISTaskStatusHistory();
            history.setModifiedBy(login.getPerson().getId());
            history.setModifiedDate(new Date());
            history.setNewStatus(bidTask.getStatus().name());
            history.setOldStatus(prev.getStatus().name());
            history.setTask(bidTask.getId());
            taskStatusHistoryRepository.save(history);

        }
        bidTask = bidTaskRepository.save(bidTask);
        return bidTask;
    }

    /**
     * The Method is used to delete bidTask
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISBidTask bidTask = bidTaskRepository.findOne(id);
        if (bidTask == null) {
            throw new ResourceNotFoundException();
        }
        ISBid bid = bidRepository.findOne(bidTask.getBid());
        bidTaskRepository.delete(bidTask);

    }

    /**
     * The Method is used to get ISBidTask
     **/
    @Transactional(readOnly = true)
    @Override
    public ISBidTask get(Integer id) {
        checkNotNull(id);
        ISBidTask bidTask = bidTaskRepository.findOne(id);
        if (bidTask == null) {
            throw new ResourceNotFoundException();
        }
        return bidTask;
    }

    /**
     * The Method is used to getAll the list ISBidTask
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISBidTask> getAll() {
        return bidTaskRepository.findAll();
    }

    /**
     * The Method is used to findAll the page ISBidTask for  pageable
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISBidTask> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "plannedStartDate")));
        }
        return bidTaskRepository.findAll(pageable);
    }

    /**
     * The Method is used to findAll the page of ISBidTask for predicate and pageable
     **/
    @Transactional(readOnly = true)
    public Page<ISBidTask> findAll(BidTaskCriteria criteria, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "plannedStartDate")));
        }
        Predicate predicate = predicateBuilder.build(criteria, QISBidTask.iSBidTask);
        return bidTaskRepository.findAll(predicate, pageable);
    }

    /**
     * The Method is used to findMultiple for the list of ISBidTask
     **/
    @Transactional(readOnly = true)
    public List<ISBidTask> findMultiple(List<Integer> ids) {
        return bidTaskRepository.findByIdIn(ids);
    }

}
