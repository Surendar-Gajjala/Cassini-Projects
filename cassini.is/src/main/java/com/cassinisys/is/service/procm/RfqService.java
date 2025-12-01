package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.procm.dto.RfqRecipientsTO;
import com.cassinisys.is.model.procm.dto.RfqResponseTO;
import com.cassinisys.is.repo.procm.*;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is RfqService of  abstract type
 */
public abstract class RfqService {

    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private RfqRepository rfqRepository;
    @Autowired
    private RfqItemRepository itemRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private RfqSentToRepository rfqSentToRepository;
    @Autowired
    private RfqResponseRepository rfqResponseRepository;
    @Autowired
    private RfqResponseItemRepository rfqResponseItemRepository;

    /**
     * The method is used to getItems of page ISRfqItem
     */
    @Transactional(readOnly = true)
    public Page<ISRfqItem> getItems(Integer rfqId, Pageable pageable) {
        checkNotNull(rfqId);
        checkNotNull(pageable);
        ISRfq rfq = rfqRepository.findOne(rfqId);
        if (rfq == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("name")));
        }
        return itemRepository.findByRfq(rfqId, pageable);
    }

    /**
     * The method is used to addItem of  ISRfqItem
     */
    @Transactional
    public ISRfqItem addItem(ISRfqItem item) {
        checkNotNull(item);
        item.setId(null);
        return itemRepository.save(item);
    }

    /**
     * The method is used to updateItem of  ISRfqItem
     */
    @Transactional(readOnly = false)
    public ISRfqItem updateItem(ISRfqItem item) {
        checkNotNull(item);
        checkNotNull(item.getId());
        return itemRepository.save(item);
    }

    /**
     * The method is used to delete item of  ISRfqItem
     */
    @Transactional(readOnly = false)
    public void deleteItem(Integer itemId) {
        checkNotNull(itemId);
        ISRfqItem item = itemRepository.findOne(itemId);
        if (item == null) {
            throw new ResourceNotFoundException();
        }
        itemRepository.delete(item);
    }

    /**
     * The method is used to sendRfq
     */
    @Transactional
    public void sendRfq(Integer rfqId, RfqRecipientsTO recipients) {
        checkNotNull(rfqId);
        checkNotNull(recipients);
        ISRfq rfq = rfqRepository.findOne(rfqId);
        if (rfq == null) {
            throw new ResourceNotFoundException();
        }
        Login login = sessionWrapper.getSession().getLogin();
        List<ISSupplier> suppliers = supplierRepository.findAll(recipients
                .getSuppliers());
        for (ISSupplier supplier : suppliers) {
            ISRfqSentTo sentTo = new ISRfqSentTo();
            sentTo.setRfq(rfqId);
            sentTo.setSentBy(login.getPerson().getId());
            sentTo.setSentOn(new Date());
           /* sentTo.setSentTo(supplier.getContactPerson());*/
            sentTo.setSupplier(supplier.getId());
            rfqSentToRepository.save(sentTo);
        }
        // TODO sent email
    }

    /**
     * The method is used to getRecipients of ISRfqSentTo Page
     */
    @Transactional(readOnly = true)
    public Page<ISRfqSentTo> getRecipients(Integer rfqId, Pageable pageable) {
        checkNotNull(rfqId);
        ISRfq rfq = rfqRepository.findOne(rfqId);
        if (rfq == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "sentOn")));
        }
        return rfqSentToRepository.findByRfq(rfqId, pageable);
    }

    /**
     * The method is used as responseToRfq of ISRfqResponse
     */
    @Transactional
    public ISRfqResponse responseToRfq(Integer rfqId, RfqResponseTO responseTO) {
        checkNotNull(rfqId);
        checkNotNull(responseTO);
        ISRfq rfq = rfqRepository.findOne(rfqId);
        if (rfq == null) {
            throw new ResourceNotFoundException();
        }
        ISRfqResponse response = new ISRfqResponse();
        response.setRfq(rfqId);
        response.setSupplier(responseTO.getSupplierId());
        response.setResponseDate(new Date());
        response = rfqResponseRepository.save(response);
        for (ISRfqResponseItem item : responseTO.getItems()) {
            item.setResponse(response.getId());
            item.setSupplier(responseTO.getSupplierId());
            rfqResponseItemRepository.save(item);
        }
        return response;
    }

    /**
     * The method is used to getResponses of ISRfqResponse page
     */
    @Transactional(readOnly = true)
    public Page<ISRfqResponse> getResponses(Integer rfqId, Pageable pageable) {
        checkNotNull(rfqId);
        checkNotNull(pageable);
        ISRfq rfq = rfqRepository.findOne(rfqId);
        if (rfq == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "responseDate")));
        }
        return rfqResponseRepository.findByRfq(rfqId, pageable);
    }

    /**
     * The method is used to getResponseItems of ISRfqResponseItem page
     */
    @Transactional(readOnly = true)
    public Page<ISRfqResponseItem> getResponseItems(Integer responseId,
                                                    Pageable pageable) {
        checkNotNull(responseId);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("item")));
        }
        // TODO Auto-generated method stub
        return null;
    }

}
