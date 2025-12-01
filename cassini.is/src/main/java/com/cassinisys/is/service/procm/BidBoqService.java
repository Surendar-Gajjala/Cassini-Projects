package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.pm.ISBid;
import com.cassinisys.is.model.procm.ISBidBoq;
import com.cassinisys.is.repo.pm.BidRepository;
import com.cassinisys.is.repo.procm.BidBoqRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
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
 * The class is for BidBoqService
 */
@Service
@Transactional
public class BidBoqService extends BoqService implements
        CrudService<ISBidBoq, Integer>, PageableService<ISBidBoq, Integer> {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private BidBoqRepository bidBoqRepository;

    /**
     * The method used to create ISBidBoq
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidBoq create(ISBidBoq bidBoq) {
        checkNotNull(bidBoq);
        bidBoq.setId(null);
        ISBid bid = bidRepository.findOne(bidBoq.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        bidBoq = bidBoqRepository.save(bidBoq);
        return bidBoq;
    }

    /**
     * The method used to update ISBidBoq
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidBoq update(ISBidBoq bidBoq) {
        checkNotNull(bidBoq);
        checkNotNull(bidBoq.getId());
        ISBid bid = bidRepository.findOne(bidBoq.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        bidBoq = bidBoqRepository.save(bidBoq);
        return bidBoq;
    }

    /**
     * The method used to delete bidBoq ISBidBoq
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISBidBoq bidBoq = bidBoqRepository.findOne(id);
        if (bidBoq == null) {
            throw new ResourceNotFoundException();
        }
        ISBid bid = bidRepository.findOne(bidBoq.getBid());
        bidBoqRepository.delete(bidBoq);

    }

    /**
     * The method used to get ISBidBoq
     **/
    @Transactional(readOnly = true)
    @Override
    public ISBidBoq get(Integer id) {
        checkNotNull(id);
        ISBidBoq bidBoq = bidBoqRepository.findOne(id);
        if (bidBoq == null) {
            throw new ResourceNotFoundException();
        }
        return bidBoq;
    }

    /**
     * The method used to getAll for the list of ISBidBoq
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISBidBoq> getAll() {
        return bidBoqRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISBidBoq
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISBidBoq> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "createdDate")));
        }
        return bidBoqRepository.findAll(pageable);
    }

    /**
     * The method used to findMultiple for the list of ISBidBoq
     **/
    @Transactional(readOnly = true)
    public List<ISBidBoq> findMultiple(List<Integer> ids) {
        return bidBoqRepository.findByIdIn(ids);
    }

}
