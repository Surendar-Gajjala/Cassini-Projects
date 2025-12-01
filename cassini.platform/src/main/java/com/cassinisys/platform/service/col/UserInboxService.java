package com.cassinisys.platform.service.col;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.UserInbox;
import com.cassinisys.platform.repo.col.UserInboxRepository;
import com.cassinisys.platform.service.core.CrudService;
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
 * Created by Nageshreddy on 04-05-2016.
 */
@Service
public class UserInboxService implements CrudService<UserInbox, Integer> {

    @Autowired
    private UserInboxRepository userInboxRepository;

    @Override
    @Transactional
    public UserInbox create(UserInbox inbox) {
        checkNotNull(inbox);
        inbox.setId(null);
        inbox = userInboxRepository.save(inbox);

        return inbox;
    }

    @Override
    @Transactional
    public UserInbox update(UserInbox inbox) {
        checkNotNull(inbox);
        checkNotNull(inbox.getId());
        inbox = userInboxRepository.save(inbox);

        return inbox;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        UserInbox inbox = userInboxRepository.findOne(id);
        if (inbox == null) {
            throw new ResourceNotFoundException();
        }
        userInboxRepository.delete(inbox);

    }


    @Override
    @Transactional(readOnly = true)
    public UserInbox get(Integer id) {
        checkNotNull(id);
        UserInbox inbox = userInboxRepository.findOne(id);
        if (inbox == null) {
            throw new ResourceNotFoundException();
        }
        return inbox;
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserInbox> getAll() {
        return userInboxRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<UserInbox> findAll(Integer id, Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("createdDate")));
        }
        return userInboxRepository.findByUserId(id, pageable);
    }

    @Transactional(readOnly = true)
    public List<UserInbox> getUnread(Integer userId) {
        return userInboxRepository.findByUserIdAndMessageRead(userId, false);
    }
}

