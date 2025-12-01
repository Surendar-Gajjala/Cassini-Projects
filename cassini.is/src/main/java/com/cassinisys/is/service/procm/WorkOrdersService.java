package com.cassinisys.is.service.procm;

import com.cassinisys.is.filtering.WorkOrderCriteria;
import com.cassinisys.is.filtering.WorkOrderPredicateBuilder;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.procm.ISContractor;
import com.cassinisys.is.model.procm.ISWorkOrder;
import com.cassinisys.is.model.procm.ISWorkOrderItem;
import com.cassinisys.is.model.procm.QISWorkOrder;
import com.cassinisys.is.model.tm.ISProjectTask;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.procm.ContractorRepository;
import com.cassinisys.is.repo.procm.WorkOrderItemRepository;
import com.cassinisys.is.repo.procm.WorkOrderRepository;
import com.cassinisys.is.repo.tm.ProjectTaskRepository;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 22/01/19.
 */
@Service
public class WorkOrdersService {

    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private WorkOrderItemRepository workOrderItemRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private WorkOrderPredicateBuilder workOrderPredicateBuilder;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PersonRepository personRepository;

    @Transactional(readOnly = false)
    public ISWorkOrder create(ISWorkOrder workOrder) {
        if (workOrder.getNumber() == null) {
            AutoNumber autoNumber = autoNumberService.getByName("Default Work Order Number Source");
            String number = autoNumberService.getNextNumber(autoNumber.getId());
            workOrder.setNumber(number);
        }
        return workOrderRepository.save(workOrder);
    }

    public List<ISWorkOrder> getAll() {
        return workOrderRepository.findAll();
    }

    public Page<ISWorkOrder> getPageableWorkOrders(Pageable pageable) {
        return workOrderRepository.findAll(pageable);
    }

    public ISWorkOrder get(Integer workOrderId) {
        ISWorkOrder workOrder = workOrderRepository.findOne(workOrderId);
        ISProject project = projectRepository.findOne(workOrder.getProject());
        ISContractor contractor = contractorRepository.findOne(workOrder.getContractor());
        Person person = personRepository.findOne(workOrder.getCreatedBy());
        workOrder.setIsContractor(contractor);
        workOrder.setIsProject(project);
        workOrder.setPerson(person);
        return workOrder;
    }

    @Transactional(readOnly = false)
    public ISWorkOrder update(ISWorkOrder workOrder) {
        return workOrderRepository.save(workOrder);
    }

    public void delete(Integer workOrderId) {
        workOrderRepository.delete(workOrderId);
    }

    public List<ISWorkOrder> findByContractor(Integer contractorID) {
        return workOrderRepository.findByContractor(contractorID);
    }

    @Transactional(readOnly = false)
    public ISWorkOrderItem createWorkOrderItem(ISWorkOrderItem workOrderItem) {
        ISWorkOrder workOrder = workOrderRepository.findOne(workOrderItem.getWorkOrder());
        ISContractor contractor = contractorRepository.findOne(workOrder.getContractor());
        ISProjectTask projectTask = projectTaskRepository.findOne(workOrderItem.getTask());
        projectTask.setPerson(contractor.getContact());
        projectTaskRepository.save(projectTask);
        return workOrderItemRepository.save(workOrderItem);
    }

    @Transactional(readOnly = false)
    public List<ISWorkOrderItem> createWorkOrderItems(List<ISWorkOrderItem> workOrderItems) {
        List<ISProjectTask> projectTasks = new ArrayList<>();
        ISWorkOrder workOrder = workOrderRepository.findOne(workOrderItems.get(0).getWorkOrder());
        ISContractor contractor = contractorRepository.findOne(workOrder.getContractor());
        for (ISWorkOrderItem workOrderItem : workOrderItems) {
            ISProjectTask projectTask = projectTaskRepository.findOne(workOrderItem.getTask());
            projectTask.setPerson(contractor.getContact());
            projectTasks.add(projectTask);
        }
        projectTaskRepository.save(projectTasks);
        return workOrderItemRepository.save(workOrderItems);
    }

    public List<ISWorkOrderItem> getWorkOrderItems(Integer workOrderId) {
        return workOrderItemRepository.findByWorkOrder(workOrderId);
    }

    public Page<ISWorkOrderItem> getPageableWorkOrderItems(Integer workOrderId, Pageable pageable) {
        return workOrderItemRepository.findByWorkOrder(workOrderId, pageable);
    }

    public ISWorkOrderItem getWorkOrderItem(Integer workOrderItemId) {
        return workOrderItemRepository.findOne(workOrderItemId);
    }

    public ISWorkOrderItem updateWorkOrderItem(ISWorkOrderItem workOrderItem) {
        return workOrderItemRepository.save(workOrderItem);
    }

    public void deleteWorkOrderItem(Integer workOrderItemId) {
        workOrderItemRepository.delete(workOrderItemId);
    }

    public Page<ISWorkOrder> contractorsFreeTextSearch(Pageable pageable, WorkOrderCriteria criteria) {
        com.mysema.query.types.Predicate predicate = workOrderPredicateBuilder.build(criteria, QISWorkOrder.iSWorkOrder);
        return workOrderRepository.findAll(predicate, pageable);
    }
}
