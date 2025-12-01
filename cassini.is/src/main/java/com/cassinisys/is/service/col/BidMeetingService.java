package com.cassinisys.is.service.col;
/**
 * The Class is for BidMeetingService
 **/

import com.cassinisys.is.model.col.ISBidMeeting;
import com.cassinisys.is.model.pm.ISBid;
import com.cassinisys.is.repo.col.BidMeetingRepository;
import com.cassinisys.is.repo.pm.BidRepository;
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

@Service
@Transactional
public class BidMeetingService implements
        CrudService<ISBidMeeting, Integer>,
        PageableService<ISBidMeeting, Integer> {

    @Autowired
    private BidMeetingRepository bidMeetingRepository;
    @Autowired
    private BidRepository bidRepository;

    /**
     * The method used to create ISBidMeeting
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidMeeting create(ISBidMeeting meeting) {
        checkNotNull(meeting);
        meeting.setId(null);
        ISBid bid = bidRepository.findOne(meeting.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        meeting = bidMeetingRepository.save(meeting);
        return meeting;
    }

    /**
     * The method used to update ISBidMeeting
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidMeeting update(ISBidMeeting meeting) {
        checkNotNull(meeting);
        checkNotNull(meeting.getId());
        ISBid bid = bidRepository.findOne(meeting.getBid());
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        meeting = bidMeetingRepository.save(meeting);
        return meeting;
    }

    /**
     * The method used to delete ISBidMeeting
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISBidMeeting meeting = bidMeetingRepository.findOne(id);
        if (meeting == null) {
            throw new ResourceNotFoundException();
        }
        ISBid bid = bidRepository.findOne(meeting.getBid());
        bidMeetingRepository.delete(meeting);
    }

    /**
     * The method used to get the value for ISBidMeeting
     **/
    @Transactional(readOnly = true)
    @Override
    public ISBidMeeting get(Integer id) {
        checkNotNull(id);
        ISBidMeeting meeting = bidMeetingRepository.findOne(id);
        if (meeting == null) {
            throw new ResourceNotFoundException();
        }
        return meeting;
    }

    /**
     * The method used to getall values from the list of ISBidMeeting
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISBidMeeting> getAll() {
        return bidMeetingRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ISBidMeeting> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "meetingDate")));
        }
        return bidMeetingRepository.findAll(pageable);
    }

    /**
     * The method used to find multiples from the list of ISBidMeeting
     **/
    @Transactional(readOnly = true)
    public List<ISBidMeeting> findMultiple(List<Integer> ids) {
        return bidMeetingRepository.findByIdIn(ids);
    }

}
