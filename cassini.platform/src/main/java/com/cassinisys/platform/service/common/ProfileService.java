package com.cassinisys.platform.service.common;

import com.cassinisys.platform.events.ProfileEvents;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Profile;
import com.cassinisys.platform.repo.common.GroupProfileRepository;
import com.cassinisys.platform.repo.common.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nageshreddy on 08-12-2020.
 */
@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private GroupProfileRepository groupProfileRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Profile create(Profile state) {
        checkNotNull(state);
        state.setId(null);
        Profile existingProfile = profileRepository.findByNameEqualsIgnoreCase(state.getName());
        if (existingProfile != null) {
            throw new CassiniException("Profile name already exist");
        }
        state = profileRepository.save(state);
        applicationEventPublisher.publishEvent(new ProfileEvents.ProfileCreatedEvent(state));
        return state;
    }

    @Transactional
    public Profile update(Profile state) {
        checkNotNull(state);
        checkNotNull(state.getId());
        Profile existedProfile = profileRepository.findOne(state.getId());
        Profile existingProfilee = profileRepository.findByNameEqualsIgnoreCase(state.getName());
        if (existingProfilee != null && !existingProfilee.getId().equals(state.getId())) {
            throw new CassiniException("Profile name already exist");
        }
        state.setModifiedDate(new Date());
        applicationEventPublisher.publishEvent(new ProfileEvents.ProfileUpdatedEvent(existedProfile, state));
        state = profileRepository.save(state);
        return state;

    }

    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        profileRepository.delete(id);
    }

    @Transactional
    public Profile get(Integer id) {
        checkNotNull(id);
        Profile state = profileRepository.getOne(id);
        if (state == null) {
            throw new ResourceNotFoundException();
        }
        return state;
    }

    @Transactional(readOnly = true)
    public Page<Profile> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort("name"));
        }
        return profileRepository.findAll(pageable);
    }

    @Transactional
    public List<Profile> getAll() {
        return profileRepository.findAllByOrderByNameAsc();
    }

    public List<PersonGroup> getPersonGroupsByProfile(Integer profileId) {
        return groupProfileRepository.findGroupsByProfile(profileId);
    }

}