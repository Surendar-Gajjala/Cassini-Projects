package com.cassinisys.is.service.issue;

import com.cassinisys.is.filtering.IssueCriteria;
import com.cassinisys.is.filtering.IssuePredicateBuilder;
import com.cassinisys.is.model.im.*;
import com.cassinisys.is.model.pm.ProjectProblemDto;
import com.cassinisys.is.model.procm.ISWorkOrder;
import com.cassinisys.is.model.procm.ISWorkOrderItem;
import com.cassinisys.is.model.procm.WorkOrderStatus;
import com.cassinisys.is.model.tm.DetailsCountDto;
import com.cassinisys.is.model.tm.ISProjectTask;
import com.cassinisys.is.model.tm.TaskStatus;
import com.cassinisys.is.repo.im.IssueRepository;
import com.cassinisys.is.repo.im.IssueStatusHistoryRepository;
import com.cassinisys.is.repo.im.IssueTypeRepository;
import com.cassinisys.is.repo.procm.WorkOrderItemRepository;
import com.cassinisys.is.repo.procm.WorkOrderRepository;
import com.cassinisys.is.repo.tm.ProjectTaskRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.MediaRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
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
 * The class is for IssueService
 */
@Service
@Transactional
public class IssueService {

    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private IssueStatusHistoryRepository issueStatusHistoryRepository;
    @Autowired
    private IssuePredicateBuilder predicateBuilder;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private WorkOrderItemRepository workOrderItemRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private IssueTypeRepository issueTypeRepository;

    @Autowired
    private PersonRepository personRepository;

    /**
     * The method used for the  freeTextSearch for the page of  ISIssue
     **/
    public Page<ISIssue> freeTextSearch(Pageable pageable, IssueCriteria criteria) {
        Predicate predicate = predicateBuilder.build(criteria, QISIssue.iSIssue);
        return issueRepository.findAll(predicate, pageable);
    }

    /**
     * The method used to create ISIssue
     **/
    @Transactional(readOnly = false)
    public ISIssue create(ISIssue issue) {
        checkNotNull(issue);
        if (issue.getTargetObjectType().toString().equals("TASK")) {
            ISProjectTask projectTask = projectTaskRepository.findOne(issue.getTargetObjectId());
            if (projectTask.getStatus() == TaskStatus.FINISHED) {
                projectTask.setStatus(TaskStatus.INPROGRESS);
                projectTaskRepository.save(projectTask);
                List<ISWorkOrderItem> workOrderItems = workOrderItemRepository.findByTask(projectTask.getId());
                if (workOrderItems.size() > 0) {
                    ISWorkOrder workOrder = workOrderRepository.findOne(workOrderItems.get(0).getWorkOrder());
                    workOrder.setStatus(WorkOrderStatus.PENDING);
                    workOrderRepository.save(workOrder);
                }
            }
        }
        issue = issueRepository.save(issue);
        return issue;
    }

    /**
     * The method used to update ISIssue
     **/
    @Transactional(readOnly = false)
    public ISIssue update(ISIssue issue) {
        checkNotNull(issue);
        checkNotNull(issue.getId());
        Login login = sessionWrapper.getSession().getLogin();
        ISIssue previous = issueRepository.findOne(issue.getId());
        if (previous == null) {
            throw new ResourceNotFoundException();
        }
        if (!previous.getStatus().equals(issue.getStatus())) {
            ISIssueStatusHistory history = new ISIssueStatusHistory();
            history.setIssue(issue.getId());
            history.setModifiedBy(login.getPerson().getId());
            history.setModifiedDate(new Date());
            history.setNewStatus(issue.getStatus());
            history.setOldStatus(previous.getStatus());
            issueStatusHistoryRepository.save(history);
        }
        issue = issueRepository.save(issue);
        return issue;
    }

    /**
     * The method used to delete  issue for ISIssue
     **/
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISIssue issue = issueRepository.findOne(id);
        if (issue == null) {
            throw new ResourceNotFoundException();
        }
        issueRepository.delete(issue);

    }

    /**
     * The method used to get ISIssue
     **/
    @Transactional(readOnly = true)
    public ISIssue get(Integer id) {
        checkNotNull(id);
        ISIssue issue = issueRepository.findOne(id);
        if (issue == null) {
            throw new ResourceNotFoundException();
        }
        issue.setMedia(mediaRepository.findByObjectIdOrderByCreatedDateDesc(id).size());
        return issue;
    }

    /**
     * The method used to getAll for the list of ISIssue
     **/
    @Transactional(readOnly = true)
    public List<ISIssue> getAll() {
        return issueRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISIssue using  pageable
     **/
    @Transactional
    public Page<ISIssue> getPageableIssues(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("modifiedDate")));

        }
        return issueRepository.findAll(pageable);
    }

    /**
     * The method used to findAll for the page of ISIssue using objectId , objectType and pageable
     **/
    @Transactional(readOnly = true)
    public Page<ISIssue> getPageableIssues(Integer objectId, ObjectType objectType, Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("modifiedDate")));

        }
        return issueRepository.findByTargetObjectIdAndTargetObjectType(objectId, objectType, pageable);
    }

    /**
     * The method used to findAll for the page of ISIssue
     **/
    @Transactional(readOnly = true)
    public Page<ISIssue> findAll(IssueCriteria criteria, Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("modifiedDate")));

        }
        Predicate predicate = predicateBuilder.build(criteria, QISIssue.iSIssue);
        return issueRepository.findAll(predicate, pageable);
    }

    /**
     * The method used to getStatusHistory for the page of ISIssueStatusHistory
     **/
    @Transactional(readOnly = true)
    public Page<ISIssueStatusHistory> getStatusHistory(Integer issueId,
                                                       Pageable pageable) {
        checkNotNull(issueId);
        checkNotNull(pageable);
        ISIssue issue = issueRepository.findOne(issueId);
        if (issue == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "modifiedDate")));

        }
        return issueStatusHistoryRepository.findByIssue(issueId, pageable);
    }

    /**
     * The method used to findMultiple for the list of ISIssue
     **/
    @Transactional(readOnly = true)
    public List<ISIssue> findMultiple(List<Integer> ids) {
        return issueRepository.findByIdIn(ids);
    }

    public List<ISIssue> findAll() {
        return issueRepository.findAll();
    }

    public List<ISIssue> findByObjectType(Integer objectId, ObjectType objectType) {
        return issueRepository.findByTargetObjectIdAndTargetObjectType(objectId, objectType);
    }

    public ProblemPriorityCount getIssuesCountByPriority(Integer objectId) {
        Integer count = 0;
        ProblemPriorityCount priorityCount = new ProblemPriorityCount();
        count = issueRepository.findIssuesCountByPriorityAndObjectId(IssuePriority.HIGH, objectId);
        if (count == null) {
            count = 0;
        }
        priorityCount.setHigh(count);
        count = issueRepository.findIssuesCountByPriorityAndObjectId(IssuePriority.MEDIUM, objectId);
        if (count == null) {
            count = 0;
        }
        priorityCount.setMedium(count);
        count = issueRepository.findIssuesCountByPriorityAndObjectId(IssuePriority.LOW, objectId);
        if (count == null) {
            count = 0;
        }
        priorityCount.setLow(count);
        List<ISProjectTask> projectTasks = projectTaskRepository.findByProject(objectId);
        for (ISProjectTask projectTask : projectTasks) {
            priorityCount.setHigh(priorityCount.getHigh() + issueRepository.findIssuesCountByPriorityAndObjectId(IssuePriority.HIGH, projectTask.getId()));
            priorityCount.setLow(priorityCount.getLow() + issueRepository.findIssuesCountByPriorityAndObjectId(IssuePriority.LOW, projectTask.getId()));
            priorityCount.setMedium(priorityCount.getMedium() + issueRepository.findIssuesCountByPriorityAndObjectId(IssuePriority.MEDIUM, projectTask.getId()));
        }
        return priorityCount;
    }

    public DetailsCountDto getIssueDetailsCount(Integer id) {
        DetailsCountDto detailsCountDto = new DetailsCountDto();
        detailsCountDto.setMedia(mediaRepository.findByObjectIdOrderByCreatedDateDesc(id).size());
        return detailsCountDto;
    }

    @Transactional(readOnly = true)
    public ProjectProblemDto getIssueDetails(Integer id) {
        ProjectProblemDto problemDto = new ProjectProblemDto();
        ISIssue issue = issueRepository.findOne(id);
        problemDto.setId(issue.getId());
        problemDto.setTitle(issue.getTitle());
        problemDto.setDescription(issue.getDescription());
        problemDto.setType(issueTypeRepository.findOne(issue.getType()).getLabel());
        problemDto.setPriority(issue.getPriority());
        problemDto.setStatus(issue.getStatus());
        Person person = personRepository.findOne(issue.getAssignedTo());
        Person createdBy = personRepository.findOne(issue.getCreatedBy());
        problemDto.setAssignedTo(person.getFullName());
        problemDto.setAssignedToNumber(person.getPhoneMobile());
        problemDto.setCreatedBy(createdBy.getFullName());
        problemDto.setCreatedByNumber(createdBy.getPhoneMobile());
        problemDto.setMedia(mediaRepository.findByObjectIdOrderByCreatedDateDesc(id).size());
        return problemDto;
    }
}
