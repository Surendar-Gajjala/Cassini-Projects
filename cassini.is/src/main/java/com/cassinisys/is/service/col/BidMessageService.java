package com.cassinisys.is.service.col;
/**
 * The class  is for ISBidMessageService
 **/

import com.cassinisys.is.model.col.ISBidMessage;
import com.cassinisys.is.repo.col.BidMessageRepository;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class BidMessageService implements CrudService<ISBidMessage, Integer>,
        PageableService<ISBidMessage, Integer> {

    @Autowired
    private BidMessageRepository messageRepository;
    @Autowired
    private SessionWrapper sessionWrapper;

    /**
     * The method used to create ISBidMessage
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidMessage create(ISBidMessage message) {
        checkNotNull(message);
        message.setId(null);
        Login login = sessionWrapper.getSession().getLogin();
        message.setSentDate(new Date());
        message.setSentBy(login.getPerson().getId());
        message = messageRepository.save(message);
        return message;
    }

    /**
     * The method used to update ISBidMessage
     **/
    @Override
    @Transactional(readOnly = false)
    public ISBidMessage update(ISBidMessage message) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        messageRepository.delete(id);

    }

    /**
     * The method used to get the value of ISBidMessage
     **/
    @Transactional(readOnly = true)
    @Override
    public ISBidMessage get(Integer id) {
        checkNotNull(id);
        ISBidMessage message = messageRepository.findOne(id);
        if (message == null) {
            throw new ResourceNotFoundException();
        }
        return message;
    }

    /**
     * The method used to get all the values from the list of ISBidMessage
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISBidMessage> getAll() {
        return messageRepository.findAll();
    }

    /**
     * The method used to findall the values in the page ISBidMessage
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISBidMessage> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "sentDate")));
        }
        return messageRepository.findAll(pageable);
    }

    /**
     * The method used to findmultiples from the list of ISBidMessage
     **/
    @Transactional(readOnly = true)
    public List<ISBidMessage> findMultiple(List<Integer> ids) {
        return messageRepository.findByIdIn(ids);
    }
}
