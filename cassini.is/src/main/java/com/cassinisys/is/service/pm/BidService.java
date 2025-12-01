package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.col.ISBidMeeting;
import com.cassinisys.is.model.col.ISBidMessage;
import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.dm.ISBidFolder;
import com.cassinisys.is.model.im.ISIssue;
import com.cassinisys.is.model.pm.ISBid;
import com.cassinisys.is.model.pm.ISBidStatusHistory;
import com.cassinisys.is.model.procm.ISBidBoq;
import com.cassinisys.is.model.procm.ISBidRfq;
import com.cassinisys.is.model.tm.ISBidTask;
import com.cassinisys.is.repo.col.BidMeetingRepository;
import com.cassinisys.is.repo.col.BidMessageRepository;
import com.cassinisys.is.repo.dm.BidFolderRepository;
import com.cassinisys.is.repo.im.IssueRepository;
import com.cassinisys.is.repo.pm.BidRepository;
import com.cassinisys.is.repo.pm.BidStatusHistoryRepository;
import com.cassinisys.is.repo.procm.BidBoqRepository;
import com.cassinisys.is.repo.tm.BidTaskRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.AttachmentRepository;
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

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for BidService
 */
@Service
@Transactional
public class BidService implements CrudService<ISBid, Integer>,
        PageableService<ISBid, Integer> {

    private SessionWrapper sessionWrapper;

    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private BidTaskRepository bidTaskRepository;
    @Autowired
    private BidStatusHistoryRepository bidStatusHistoryRepository;
    @Autowired
    private BidFolderRepository bidFolderRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private BidMessageRepository messageRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private BidMeetingRepository bidMeetingRepository;
    @Autowired
    private BidBoqRepository bidBoqRepository;

    /**
     * The method used to create  ISBid
     **/
    @Transactional(readOnly = false)
    @Override
    public ISBid create(ISBid bid) {
        checkNotNull(bid);
        bid = bidRepository.save(bid);
        return bid;
    }

    /**
     * The method used to update  ISBid
     **/
    @Transactional(readOnly = false)
    @Override
    public ISBid update(ISBid bid) {
        checkNotNull(bid);
        checkNotNull(bid.getId());
        Login login = sessionWrapper.getSession().getLogin();
        ISBid previous = bidRepository.findOne(bid.getId());
        if (previous == null) {
            throw new ResourceNotFoundException();
        }
        if (!previous.getStatus().equals(bid.getStatus())) {
            ISBidStatusHistory history = new ISBidStatusHistory();
            history.setModifiedBy(login.getPerson().getId());
            history.setModifiedDate(new Date());
            history.setNewStatus(bid.getStatus());
            history.setOldStatus(previous.getStatus());
            history.setBid(bid.getId());
            bidStatusHistoryRepository.save(history);
        }
        bid = bidRepository.save(bid);
        return bid;
    }

    /**
     * The method used to delete  ISBid
     **/
    @Transactional(readOnly = false)
    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        ISBid bid = bidRepository.findOne(id);
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        bidRepository.delete(bid);

    }

    /**
     * The method used to get  ISBid
     **/
    @Transactional(readOnly = true)
    @Override
    public ISBid get(Integer id) {
        checkNotNull(id);
        ISBid bid = bidRepository.findOne(id);
        if (bid == null) {
            throw new ResourceNotFoundException();
        }
        return bid;
    }

    /**
     * The method used to getAll for the list of  ISBid
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISBid> getAll() {
        return bidRepository.findAll();
    }

    /**
     * The method used to findAll for the page of  ISBid
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISBid> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("id")));

        }
        return bidRepository.findAll(pageable);
    }

    /**
     * The method used to getTasks for the page of  ISBidTask
     **/
    @Transactional(readOnly = true)
    public Page<ISBidTask> getTasks(Integer bidId, Pageable pageable) {
        checkNotNull(bidId);
        checkNotNull(pageable);
        if (bidRepository.findOne(bidId) == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "plannedStartDate")));

        }
        return bidTaskRepository.findByBid(bidId, pageable);
    }

    /**
     * The method used to getRootFolders for the page of  ISBidFolder
     **/
    @Transactional(readOnly = true)
    public Page<ISBidFolder> getRootFolders(Integer bidId, Pageable pageable) {
        checkNotNull(bidId);
        checkNotNull(pageable);
        if (bidRepository.findOne(bidId) == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("name")));
        }
        return bidFolderRepository.findByBidAndParentIsNull(bidId, pageable);
    }

    /**
     * The method used to getIssues for the page of  ISIssue
     **/
    @Transactional(readOnly = true)
    public Page<ISIssue> getIssues(Integer bidId, Pageable pageable) {
        checkNotNull(bidId);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("modifiedDate")));
        }
        return issueRepository.findByTargetObjectIdAndTargetObjectType(bidId,
                ObjectType.valueOf(ISObjectType.BID.name()), pageable);
    }

    /**
     * The method used to getMessages for the page of  ISBidMessage
     **/
    @Transactional(readOnly = true)
    public Page<ISBidMessage> getMessages(Integer bidId, Pageable pageable) {
        checkNotNull(bidId);
        checkNotNull(pageable);
        if (bidRepository.findOne(bidId) == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "sentDate")));
        }
        return messageRepository.findByBid(bidId, pageable);
    }

    /**
     * The method used to getMeetings for the page of  ISBidMeeting
     **/
    @Transactional(readOnly = true)
    public Page<ISBidMeeting> getMeetings(Integer bidId, Pageable pageable) {
        checkNotNull(bidId);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "meetingDate")));
        }
        return bidMeetingRepository.findByBid(bidId, pageable);
    }

    /**
     * The method used to getBoqs for the page of ISBidBoq
     **/
    @Transactional(readOnly = true)
    public Page<ISBidBoq> getBoqs(Integer id, Pageable pageable) {
        checkNotNull(id);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "createdDate")));
        }
        return bidBoqRepository.findByBid(id, pageable);
    }

    /**
     * The method used to getRfqs for the page of ISBidRfq
     **/
    @Transactional(readOnly = true)
    public Page<ISBidRfq> getRfqs(Integer id, Pageable pageable) {
        checkNotNull(id);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "createdDate")));
        }
        throw new UnsupportedOperationException();
    }

    /**
     * The method used to findMultiple for the List of ISBid
     **/
    @Transactional(readOnly = true)
    public List<ISBid> findMultiple(List<Integer> ids) {
        return bidRepository.findByIdIn(ids);
    }
}
