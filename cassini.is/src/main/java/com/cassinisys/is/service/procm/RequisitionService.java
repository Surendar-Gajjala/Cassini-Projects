package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.procm.ISRequisitionApproval;
import com.cassinisys.is.model.procm.ISRequisitionItem;
import com.cassinisys.is.model.procm.ISRequisitionRequest;
import com.cassinisys.is.repo.procm.RequisitionApprovalRepository;
import com.cassinisys.is.repo.procm.RequisitionItemListRepository;
import com.cassinisys.is.repo.procm.RequisitionRequestRepository;
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
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for RequisitionService
 */
@Service
@Transactional
public class RequisitionService implements
        CrudService<ISRequisitionRequest, Integer>,
        PageableService<ISRequisitionRequest, Integer> {

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private RequisitionRequestRepository requisitionRepository;
    @Autowired
    private RequisitionItemListRepository itemRepository;
    @Autowired
    private RequisitionApprovalRepository approvalRepository;

    /**
     * The method is used to create ISRequisitionRequest
     */
    @Override
    @Transactional(readOnly = false)
    public ISRequisitionRequest create(ISRequisitionRequest requisition) {
        checkNotNull(requisition);
        requisition.setId(null);
        requisition = requisitionRepository.save(requisition);
        return requisition;
    }

    /**
     * The method is used to update ISRequisitionRequest
     */
    @Override
    @Transactional(readOnly = false)
    public ISRequisitionRequest update(ISRequisitionRequest requisition) {
        checkNotNull(requisition);
        checkNotNull(requisition.getId());
        requisition = requisitionRepository.save(requisition);
        return requisition;
    }

    /**
     * The method is used to delete requisition of ISRequisitionRequest
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISRequisitionRequest requisition = requisitionRepository.findOne(id);
        if (requisition == null) {
            throw new ResourceNotFoundException();
        }
        requisitionRepository.delete(requisition);

    }

    /**
     * The method is used to get ISRequisitionRequest
     */
    @Transactional(readOnly = true)
    @Override
    public ISRequisitionRequest get(Integer id) {
        checkNotNull(id);
        ISRequisitionRequest requisition = requisitionRepository.findOne(id);
        if (requisition == null) {
            throw new ResourceNotFoundException();
        }
        return requisition;
    }

    /**
     * The method is used to getAll the list of ISRequisitionRequest
     */
    @Transactional(readOnly = true)
    @Override
    public List<ISRequisitionRequest> getAll() {
        return requisitionRepository.findAll();
    }

    /**
     * The method is used to findAll the page of ISRequisitionRequest
     */
    @Transactional(readOnly = true)
    @Override
    public Page<ISRequisitionRequest> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("id")));
        }
        return requisitionRepository.findAll(pageable);
    }

    /**
     * The method is used to addItem to ISRequisitionItem
     */
    public ISRequisitionItem addItem(ISRequisitionItem item) {
        checkNotNull(item);
        item.setId(null);
        return itemRepository.save(item);
    }

    /**
     * The method is used to updateItem of ISRequisitionItem
     */
    @Transactional(readOnly = false)
    public ISRequisitionItem updateItem(ISRequisitionItem item) {
        checkNotNull(item);
        checkNotNull(item.getId());
        return itemRepository.save(item);
    }

    /**
     * The method is used to deleteItem of ISRequisitionItem
     */
    @Transactional(readOnly = false)
    public void deleteItem(Integer itemId) {
        checkNotNull(itemId);
        ISRequisitionItem item = itemRepository.findOne(itemId);
        if (item == null) {
            throw new ResourceNotFoundException();
        }
        itemRepository.delete(item);
    }

    /**
     * The method is used to getItems for the page of ISRequisitionItem
     */
    @Transactional(readOnly = true)
    public Page<ISRequisitionItem> getItems(Integer reqId, Pageable pageable) {
        checkNotNull(reqId);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("item")));
        }
        return itemRepository.findByRequisition(reqId, pageable);
    }

    /**
     * The method is used to approve ISRequisitionApproval
     */
    public void approve(Integer reqId, ISRequisitionApproval approval) {
        checkNotNull(reqId);
        checkNotNull(approval);
        ISRequisitionRequest requisition = requisitionRepository.findOne(reqId);
        if (requisition == null) {
            throw new ResourceNotFoundException();
        }
        Login login = sessionWrapper.getSession().getLogin();
        approval.setRequisition(requisition.getId());
        approval.setPerson(login.getPerson().getId());
        approvalRepository.save(approval);
    }

    /**
     * The method is used to getApprovals for the page of ISRequisitionApproval
     */
    @Transactional(readOnly = true)
    public Page<ISRequisitionApproval> getApprovals(Integer reqId,
                                                    Pageable pageable) {
        checkNotNull(reqId);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("id")));
        }
        return approvalRepository.findByRequisition(reqId, pageable);
    }

    /**
     * The method is used to findMultiple for the list of ISRequisitionRequest
     */
    @Transactional(readOnly = true)
    public List<ISRequisitionRequest> findMultiple(List<Integer> ids) {
        return requisitionRepository.findByIdIn(ids);
    }
}
