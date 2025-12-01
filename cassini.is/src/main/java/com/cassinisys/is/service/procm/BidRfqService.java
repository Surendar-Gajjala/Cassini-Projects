package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.pm.ISBid;
import com.cassinisys.is.model.procm.ISBidRfq;
import com.cassinisys.is.repo.pm.BidRepository;
import com.cassinisys.is.repo.procm.BidRfqRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
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

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for BidRfqService
 */
@Service
@Transactional
public class BidRfqService extends RfqService implements
        CrudService<ISBidRfq, Integer>, PageableService<ISBidRfq, Integer> {

    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private BidRfqRepository rfqRepository;

    /**
     * The method used to create ISBidRfq
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidRfq create(ISBidRfq rfq) {
        checkNotNull(rfq);
        rfq.setId(null);
        ISBid bid = bidRepository.findOne(rfq.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        rfq = rfqRepository.save(rfq);
        return rfq;
    }

    /**
     * The method used to update ISBidRfq
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidRfq update(ISBidRfq rfq) {
        checkNotNull(rfq);
        checkNotNull(rfq.getId());
        ISBid bid = bidRepository.findOne(rfq.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        rfq = rfqRepository.save(rfq);
        return rfq;
    }

    /**
     * The method used to delete rfq of ISBidRfq
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISBidRfq rfq = rfqRepository.findOne(id);
        if (rfq == null) {
            throw new ResourceNotFoundException();
        }
        ISBid bid = bidRepository.findOne(rfq.getBid());
        rfqRepository.delete(rfq);
    }

    /**
     * The method used to get ISBidRfq
     **/
    @Transactional(readOnly = true)
    @Override
    public ISBidRfq get(Integer id) {
        checkNotNull(id);
        ISBidRfq rfq = rfqRepository.findOne(id);
        if (rfq == null) {
            throw new ResourceNotFoundException();
        }
        return rfq;
    }

    /**
     * The method used to getAll for the list of ISBidRfq
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISBidRfq> getAll() {
        return rfqRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISBidRfq
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISBidRfq> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "createdDate")));
        }
        return rfqRepository.findAll(pageable);
    }

    /**
     * The method used to findMultiple for the list of ISBidRfq
     **/
    @Transactional(readOnly = true)
    public List<ISBidRfq> findMultiple(List<Integer> ids) {
        return rfqRepository.findByIdIn(ids);
    }

}
