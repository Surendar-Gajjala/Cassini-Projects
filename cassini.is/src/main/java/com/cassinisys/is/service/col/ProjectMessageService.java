package com.cassinisys.is.service.col;
/**
 * The Class is for ProjectMessageService
 **/

import com.cassinisys.is.model.col.ISProjectMessage;
import com.cassinisys.is.repo.col.ProjectMessageRepository;
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
public class ProjectMessageService implements
        CrudService<ISProjectMessage, Integer>,
        PageableService<ISProjectMessage, Integer> {

    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ProjectMessageRepository messageRepository;

    /**
     * The method used to create ISProjectMessage
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProjectMessage create(ISProjectMessage message) {
        checkNotNull(message);
        message.setId(null);
        Login login = sessionWrapper.getSession().getLogin();
        message.setSentDate(new Date());
        message.setSentBy(login.getPerson().getId());
        message = messageRepository.save(message);
        return message;
    }

    /**
     * The method used to update ISProjectMessage
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProjectMessage update(ISProjectMessage message) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISProjectMessage message = messageRepository.findOne(id);
        messageRepository.delete(id);

    }

    /**
     * The method used to get the value for ISProjectMessage
     **/
    @Transactional(readOnly = true)
    @Override
    public ISProjectMessage get(Integer id) {
        checkNotNull(id);
        ISProjectMessage message = messageRepository.findOne(id);
        if (message == null) {
            throw new ResourceNotFoundException();
        }
        return message;
    }

    /**
     * The method used to getall the values from the list of  ISProjectMessage
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISProjectMessage> getAll() {
        return messageRepository.findAll();
    }

    /**
     * The method used to findAll the values from the page of  ISProjectMessage
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISProjectMessage> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "sentDate")));
        }
        return messageRepository.findAll(pageable);
    }

    /**
     * The method used to findmultiple  values from the List of  ISProjectMessage
     **/
    @Transactional(readOnly = true)
    public List<ISProjectMessage> findMultiple(List<Integer> ids) {
        return messageRepository.findByIdIn(ids);
    }

}
