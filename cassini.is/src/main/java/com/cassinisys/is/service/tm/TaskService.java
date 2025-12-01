package com.cassinisys.is.service.tm;

import com.cassinisys.is.filtering.TaskCriteria;
import com.cassinisys.is.filtering.TaskPredicateBuilder;
import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.im.ISIssue;
import com.cassinisys.is.model.tm.*;
import com.cassinisys.is.repo.im.IssueRepository;
import com.cassinisys.is.repo.pm.WbsRepository;
import com.cassinisys.is.repo.tm.*;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Class is for TaskService
 **/
public abstract class TaskService {

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private WbsRepository wbsRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private TaskAssignedToRepository assignedToRepository;
    @Autowired
    private TaskObserverRepository observersRepository;
    @Autowired
    private TaskStatusHistoryRepository statusHistoryRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private TaskPredicateBuilder taskPredicateBuilder;

    /**
     * The method used to addStatusHistoryRecord
     **/
    protected void addStatusHistoryRecord(Integer taskId,
                                          TaskStatus oldStatus, TaskStatus newStatus) {
        ISTaskStatusHistory history = new ISTaskStatusHistory();
        history.setTask(taskId);
        history.setOldStatus(oldStatus.name());
        history.setNewStatus(newStatus.name());
        history.setModifiedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        history.setModifiedDate(new Date());
        history = statusHistoryRepository.save(history);
    }

    /**
     * The method used to getIssues for ISIssuePage
     **/
    @Transactional(readOnly = true)
    public Page<ISIssue> getIssues(Integer taskId, Pageable pageable) {
        checkNotNull(taskId);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("modifiedDate")));
        }
        return issueRepository.findByTargetObjectIdAndTargetObjectType(taskId,
                ObjectType.valueOf(ISObjectType.TASK.name()), pageable);

    }

    /**
     * The method used to getAssignedTo for List of Person
     **/
    @Transactional(readOnly = true)
    public List<Person> getAssignedTo(Integer taskId) {
        checkNotNull(taskId);
        ISTask task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new ResourceNotFoundException();
        }
        List<Integer> peopleId = assignedToRepository.findByTaskId(taskId);
        List<Person> assignedTo = personRepository.findByIdIn(peopleId);
        return assignedTo;
    }

    /**
     * The method used to addAssignedTo for ISTaskAssignedTo
     **/
    public ISTaskAssignedTo addAssignedTo(Integer taskId, Integer personId) {
        checkNotNull(taskId);
        checkNotNull(personId);
        ISTask task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new ResourceNotFoundException();
        }
        Person person = personRepository.findOne(personId);
        if (person == null) {
            throw new ResourceNotFoundException();
        }
        ISTaskAssignedTo assignedTo = new ISTaskAssignedTo(taskId, personId);
        assignedTo = assignedToRepository.save(assignedTo);
        task.setStatus(TaskStatus.ASSIGNED);
        taskRepository.save(task);
        addStatusHistoryRecord(task.getId(), TaskStatus.NEW, TaskStatus.ASSIGNED);
        return assignedTo;
    }

    /**
     * The method used to deleteAssignedTo for ISTaskAssignedToId
     **/
    @Transactional(readOnly = false)
    public void deleteAssignedTo(Integer taskId, Integer personId) {
        checkNotNull(taskId);
        checkNotNull(personId);
        ISTask task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new ResourceNotFoundException();
        }
        Person person = personRepository.findOne(personId);
        if (person == null) {
            throw new ResourceNotFoundException();
        }
        assignedToRepository.delete(new ISTaskAssignedToId(taskId, personId));

    }

    /**
     * The method used to getObservers for Person Page
     **/
    @Transactional(readOnly = true)
    public Page<Person> getObservers(Integer taskId, Pageable pageable) {
        checkNotNull(taskId);
        checkNotNull(pageable);
        ISTask task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new ResourceNotFoundException();
        }
        Page<Integer> peopleIdPage = observersRepository.findByTaskId(taskId,
                pageable);
        List<Person> observers = personRepository.findByIdIn(peopleIdPage.getContent());
        return new PageImpl<Person>(observers, pageable,
                peopleIdPage.getTotalElements());
    }

    /**
     * The method used to addObservers for ISTaskObserver
     **/
    public ISTaskObserver addObservers(Integer taskId, Integer personId) {
        checkNotNull(taskId);
        checkNotNull(personId);
        ISTask task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new ResourceNotFoundException();
        }
        Person person = personRepository.findOne(personId);
        if (person == null) {
            throw new ResourceNotFoundException();
        }
        ISTaskObserver observer = new ISTaskObserver(taskId, personId);
        observer = observersRepository.save(observer);
        return observer;
    }

    /**
     * The method used to deleteObservers
     **/
    @Transactional(readOnly = false)
    public void deleteObservers(Integer taskId, Integer personId) {
        checkNotNull(taskId);
        checkNotNull(personId);
        ISTask task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new ResourceNotFoundException();
        }
        Person person = personRepository.findOne(personId);
        if (person == null) {
            throw new ResourceNotFoundException();
        }
        observersRepository.delete(new ISTaskObserverId(taskId, personId));

    }

    /**
     * The method used to getStatusHistory for ISTaskStatusHistory Page
     **/
    @Transactional(readOnly = true)
    public Page<ISTaskStatusHistory> getStatusHistory(Integer taskId,
                                                      Pageable pageable) {
        checkNotNull(taskId);
        checkNotNull(pageable);
        ISTask task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "modifiedDate")));
        }
        return statusHistoryRepository.findByTask(taskId, pageable);
    }

    /**
     * The method used to getTasks for  the list of ISTask
     **/
    @Transactional(readOnly = true)
    public List<ISTask> getTasks(Integer personId) {
        List<Integer> taskId = assignedToRepository.findByPersonId(personId);
        List<ISTask> tasks = taskRepository.findByIdIn(taskId);
        return tasks;
    }

    /**
     * The method used to getAllTasksForWbsItem for  ISTask Page
     **/
    @Transactional(readOnly = true)
    public Page<ISTask> getAllTasksForWbsItem(Integer wbsItem, Pageable pageable) {
        checkNotNull(wbsItem);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "plannedStartDate")));
        }
        return taskRepository.findByWbsItem(wbsItem, pageable);
    }

    /**
     * The method used to search tasks by name and description
     **/
    @Transactional(readOnly = true)
    public List<ISTask> searchTasks(TaskCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Predicate predicate = taskPredicateBuilder.build(criteria, QISProjectTask.iSProjectTask);
        List<ISTask> isTasks = Lists.newArrayList(projectTaskRepository.findAll(predicate));
        return isTasks;
    }
}
