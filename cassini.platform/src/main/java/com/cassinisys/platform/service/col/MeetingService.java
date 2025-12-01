package com.cassinisys.platform.service.col;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Meeting;
import com.cassinisys.platform.repo.col.MeetingRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
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
 * Created by Cassinisys on 20-10-2017.
 */
@Service
public class MeetingService implements CrudService<Meeting, Integer>,
        PageableService<Meeting, Integer> {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private SessionWrapper sessionWrapper;


    @Override
    @Transactional
    public Meeting create(Meeting meeting) {
        checkNotNull(meeting);
        return meetingRepository.save(meeting);
    }

    @Override
    @Transactional
    public Meeting update(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        meetingRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Meeting get(Integer id) {
        checkNotNull(id);
        Meeting meeting = meetingRepository.findOne(id);
        if (meeting == null) {
            throw new ResourceNotFoundException();
        }
        return meeting;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Meeting> getAll() {
        return meetingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Meeting> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC,
                    "modifiedDate")));
        }
        return meetingRepository.findAll(pageable);
    }

}
