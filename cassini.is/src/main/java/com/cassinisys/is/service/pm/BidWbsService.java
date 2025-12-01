package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.pm.ISBid;
import com.cassinisys.is.model.pm.ISBidWbs;
import com.cassinisys.is.repo.pm.BidRepository;
import com.cassinisys.is.repo.pm.BidWbsRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for BidWbsService
 */
@Service
@Transactional
public class BidWbsService implements CrudService<ISBidWbs, Integer>, PageableService<ISBidWbs, Integer> {

    @Autowired
    private BidWbsRepository bidWbsRepository;
    @Autowired
    private BidRepository bidRepository;

    /**
     * The method used to create  ISBidWbs
     **/
    @Transactional(readOnly = false)
    @Override
    public ISBidWbs create(ISBidWbs bidWbs) {
        checkNotNull(bidWbs);
        bidWbs.setId(null);
        ISBid bid = bidRepository.findOne(bidWbs.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        bidWbs = bidWbsRepository.save(bidWbs);
        return bidWbs;
    }

    /**
     * The method used to update ISBidWbs
     **/
    @Transactional(readOnly = false)
    @Override
    public ISBidWbs update(ISBidWbs bidWbs) {
        checkNotNull(bidWbs);
        checkNotNull(bidWbs.getId());
        ISBid bid = bidRepository.findOne(bidWbs.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        bidWbs = bidWbsRepository.save(bidWbs);
        return bidWbs;
    }

    /**
     * The method used to delete bid of ISBid
     **/
    @Transactional(readOnly = false)
    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        ISBidWbs bidWbs = bidWbsRepository.findOne(id);
        if (bidWbs == null) {
            throw new ResourceNotFoundException();
        }
        ISBid bid = bidRepository.findOne(bidWbs.getBid());
        bidWbsRepository.delete(bidWbs);

    }

    /**
     * The method used to get ISBidWbs
     **/
    @Transactional(readOnly = true)
    @Override
    public ISBidWbs get(Integer id) {
        checkNotNull(id);
        ISBidWbs bidWbs = bidWbsRepository.findOne(id);
        if (bidWbs == null) {
            throw new ResourceNotFoundException();
        }
        return bidWbs;
    }

    /**
     * The method used to getAll for the List of ISBidWbs
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISBidWbs> getAll() {
        return bidWbsRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISBidWbs
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISBidWbs> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        return bidWbsRepository.findAll(pageable);
    }

    /**
     * The method used to findMultiple for the List of ISBidWbs
     **/
    @Transactional(readOnly = true)
    public List<ISBidWbs> findMultiple(List<Integer> ids) {
        return bidWbsRepository.findByIdIn(ids);
    }
}
