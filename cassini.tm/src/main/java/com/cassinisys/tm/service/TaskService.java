package com.cassinisys.tm.service;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activity.ActivityStreamWriter;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.tm.filtering.ProjectTaskCriteria;
import com.cassinisys.tm.filtering.ProjectTaskPredicateBuilder;
import com.cassinisys.tm.model.*;
import com.cassinisys.tm.repo.*;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by CassiniSystems on 06-07-2016.
 */
@Service
public class TaskService {


    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private ActivityStreamWriter activityWriter;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PendingReasonRepository pendingReasonRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private TaskHistoryRepository historyRepository;

    @Autowired
    private EmergencyContactRepository contactRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProjectTaskPredicateBuilder taskPredicateBuilder;

    @Autowired
    private TaskImageRepository taskImageRepository;



    public TMProjectTask createTask(TMProjectTask projectTask) {
        checkNotNull(projectTask);
        projectTask = projectTaskRepository.save(projectTask);
        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        Person person = personRepository.findOne(id);
        TMTaskHistory tmTaskHistory = new TMTaskHistory();
        tmTaskHistory.setNewStatus(projectTask.getStatus());
        tmTaskHistory.setTask(projectTask.getId());
        tmTaskHistory.setTimeStamp(new Date());
        tmTaskHistory.setUpdatedBy(person.getId());
        tmTaskHistory.setOldStatus(projectTask.getStatus());
        historyRepository.save(tmTaskHistory);
        return projectTask;
    }

    public TMProjectTask updateTask(TMProjectTask projectTask) {
//        checkNotNull(projectTask);
        checkNotNull(projectTask.getId());

        Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        TaskStatus newStatus = projectTask.getStatus();
        Person person = personRepository.findOne(id);

        TMProjectTask existingTask = projectTaskRepository.findOne(projectTask.getId());
        TaskStatus oldStatus = existingTask.getStatus();
        if (newStatus != oldStatus) {

            TMTaskHistory tmTaskHistory = new TMTaskHistory();
            tmTaskHistory.setNewStatus(newStatus);
            tmTaskHistory.setTask(projectTask.getId());
            tmTaskHistory.setTimeStamp(new Date());
            tmTaskHistory.setUpdatedBy(person.getId());
            tmTaskHistory.setOldStatus(oldStatus);
            historyRepository.save(tmTaskHistory);
        }

        projectTask = projectTaskRepository.save(projectTask);

        return projectTask;
    }

    public void deleteTask(Integer id) {
        checkNotNull(id);
        TMProjectTask projectTask = projectTaskRepository.findOne(id);
        if (projectTask == null) {
            throw new ResourceNotFoundException();
        }
        projectTaskRepository.delete(projectTask);
    }

    public TMProjectTask finishTask(TMProjectTask projectTask) {
        checkNotNull(projectTask);
        checkNotNull(projectTask.getId());
        TMProjectTask prev = projectTaskRepository.findOne(projectTask.getId());
        if (prev == null) {
            throw new ResourceNotFoundException();
        }
        projectTask.setStatus(TaskStatus.FINISHED);
        projectTask = projectTaskRepository.save(projectTask);

        Login login = sessionWrapper.getSession().getLogin();

        if (!prev.getStatus().equals(projectTask.getStatus())) {
            TMTaskHistory history = new TMTaskHistory();
            history.setUpdatedBy(login.getPerson().getId());
            history.setTimeStamp(new Date());
            history.setOldStatus(TaskStatus.FINISHEDPENDING);
            history.setNewStatus(TaskStatus.FINISHED);
            history.setTask(projectTask.getId());
            historyRepository.save(history);
        }

        return projectTask;
    }

    public TMProjectTask verifyTask(TMProjectTask projectTask) {
        checkNotNull(projectTask);
        checkNotNull(projectTask.getId());
        TMProjectTask prev = projectTaskRepository.findOne(projectTask.getId());
        if (prev == null) {
            throw new ResourceNotFoundException();
        }
        TMProject project = projectRepository.findOne(projectTask.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        projectTask.setStatus(TaskStatus.VERIFIED);
        projectTask = projectTaskRepository.save(projectTask);

        Login login = sessionWrapper.getSession().getLogin();
        if (!prev.getStatus().equals(projectTask.getStatus())) {
            TMTaskHistory history = new TMTaskHistory();
            history.setUpdatedBy(login.getPerson().getId());
            history.setTimeStamp(new Date());
            history.setOldStatus(TaskStatus.FINISHED);
            history.setNewStatus(TaskStatus.VERIFIED);
            history.setTask(projectTask.getId());
            historyRepository.save(history);
        }

        return projectTask;
    }

    public TMProjectTask approveTask(TMProjectTask projectTask) {
        checkNotNull(projectTask);
        checkNotNull(projectTask.getId());
        TMProjectTask prev = projectTaskRepository.findOne(projectTask.getId());
        if (prev == null) {
            throw new ResourceNotFoundException();
        }
        TMProject project = projectRepository.findOne(projectTask.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        projectTask.setStatus(TaskStatus.APPROVED);
        projectTask = projectTaskRepository.save(projectTask);

        Login login = sessionWrapper.getSession().getLogin();
        if (!prev.getStatus().equals(projectTask.getStatus())) {
            TMTaskHistory history = new TMTaskHistory();
            history.setUpdatedBy(login.getPerson().getId());
            history.setTimeStamp(new Date());
            history.setOldStatus(TaskStatus.VERIFIED);
            history.setNewStatus(TaskStatus.APPROVED);
            history.setTask(projectTask.getId());
            historyRepository.save(history);
        }

        return projectTask;
    }

    public TMProjectTask rejectTask(TMProjectTask projectTask) {
        checkNotNull(projectTask);
        checkNotNull(projectTask.getId());
        TMProjectTask prev = projectTaskRepository.findOne(projectTask.getId());
        if (prev == null) {
            throw new ResourceNotFoundException();
        }
        TMProject project = projectRepository.findOne(projectTask.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        projectTask.setStatus(TaskStatus.REJECTED);
        projectTask = projectTaskRepository.save(projectTask);

        Login login = sessionWrapper.getSession().getLogin();
        if (!prev.getStatus().equals(projectTask.getStatus())) {
            TMTaskHistory history = new TMTaskHistory();
            history.setUpdatedBy(login.getPerson().getId());
            history.setTimeStamp(new Date());
            history.setOldStatus(TaskStatus.APPROVED);
            history.setNewStatus(TaskStatus.REJECTED);
            history.setTask(projectTask.getId());
            historyRepository.save(history);
        }

        return projectTask;
    }

    @Transactional(readOnly = true)
    public TMProjectTask getTask(Integer id) {
        checkNotNull(id);
        TMProjectTask projectTask = projectTaskRepository.findOne(id);
        if (projectTask == null) {
            throw new ResourceNotFoundException();
        }
        return projectTask;
    }

    @Transactional(readOnly = true)
    public List<TMProjectTask> getAllTasks() {
        return projectTaskRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Page<TMProjectTask> findAllTasks(Pageable pageable) {
        checkNotNull(pageable);
        return projectTaskRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<TMProjectTask> findTasksByPerson(Integer PersonId) {
        checkNotNull(PersonId);
        return projectTaskRepository.findByAssignedTo(PersonId);
    }

    @Transactional(readOnly = true)
    public List<TMProjectTask> findMultipleTasks(List<Integer> ids) {
        return projectTaskRepository.findByIdIn(ids);
    }


    public Page<TMProjectTask> findAll(ProjectTaskCriteria criteria, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    new Sort(new Sort.Order(Sort.Direction.DESC, "name")));
        }
        Predicate predicate = taskPredicateBuilder.build(criteria, QTMProjectTask.tMProjectTask);
        Page<TMProjectTask> tasks = projectTaskRepository.findAll(predicate, pageable);
        return tasks;
    }

    public List<TMProjectTask> findTasks(ProjectTaskCriteria projectTaskCriteria) {
        Predicate predicate = taskPredicateBuilder.build(projectTaskCriteria, QTMProjectTask.tMProjectTask);
        Iterable<TMProjectTask> tasks = projectTaskRepository.findAll(predicate);
        List<TMProjectTask> listTasks = new ArrayList<>();
        for (TMProjectTask tmProjectTask : tasks) {
            listTasks.add(tmProjectTask);
        }
        return listTasks;
    }

    @Transactional(readOnly = true)
    public Page<TMProjectTask> getTasks(Integer projectId, Pageable pageable) {
        checkNotNull(projectId);
        checkNotNull(pageable);
        /*if (projectTaskRepository.findOne(projectId) == null) {
            throw new ResourceNotFoundException();
        }*/
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC,
                    "name")));
        }
        return projectTaskRepository.findByProject(projectId, pageable);

    }

    @Transactional(readOnly = true)
    public List<TMTaskHistory> getHistory(Integer taskId) {
        checkNotNull(taskId);
        TMProjectTask task = projectTaskRepository.findOne(taskId);
        if (task == null) {
            throw new ResourceNotFoundException();
        }
        return historyRepository.findByTask(taskId);
    }

    @Transactional
    public List<TMPendingReason> getReasons() {
        return pendingReasonRepository.findAll();
    }

    @Transactional
    public TMPendingReason getReason(Integer id) {
        return pendingReasonRepository.findOne(id);
    }

    public TMPendingReason createPendingReason(TMPendingReason pendingReason) {
        return pendingReasonRepository.save(pendingReason);
    }

    @Transactional
    public List<String> getLocations() {

        return projectTaskRepository.getLocations();
    }

    @Transactional(readOnly = true)
    public List<TMProjectTask> getDepartmentTasksByPersonIds(List<Integer> ids) {
        return projectTaskRepository.findByAssignedToIn(ids);
    }

    @Transactional(readOnly = true)
    public List<TMProjectTask> getTasksByPersonIdAndDate(Integer assignedTo,Date assignedDate) {
        return projectTaskRepository.findByAssignedToAndAssignedDate(assignedTo ,assignedDate);
    }

    @Transactional(readOnly = true)
    public List<TMTaskImage> getTaskImages(Integer taskId) {
        return taskImageRepository.findByTask(taskId);
    }

    public TMTaskImage addTaskImage(TMTaskImage image) {
        return taskImageRepository.save(image);
    }

    public List<Object[]> getAllTaskStats() {
        return projectTaskRepository.getAllTaskStats();
    }

    public List<TMProjectTask> getTasksByLocationAndStatus(String location, Iterable<TaskStatus> statuses) {
        return projectTaskRepository.findByLocationAndStatusIn(location, statuses);
    }

}

