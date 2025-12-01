package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.model.plm.PLMSavedSearch;
import com.cassinisys.plm.repo.plm.SavedSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Service
public class SavedSearchService implements CrudService<PLMSavedSearch, Integer>,
        PageableService<PLMSavedSearch, Integer> {

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SessionWrapper sessionWrapper;


    @Override
    @Transactional
    @PreAuthorize("hasPermission(#savedSearch,'create')")
    public PLMSavedSearch create(PLMSavedSearch savedSearch) {
        if (savedSearch.getType().equals("PUBLIC")) {
            PLMSavedSearch existSearch = savedSearchRepository.findBySearchObjectTypeAndTypeAndNameEqualsIgnoreCase(savedSearch.getSearchObjectType(), savedSearch.getType(), savedSearch.getName());
            if (existSearch != null) {
                throw new CassiniException(messageSource.getMessage("name_already_exists", null, "Name already exist", LocaleContextHolder.getLocale()));
            }
        } else {
            PLMSavedSearch existSearch1 = savedSearchRepository.findByOwnerAndSearchObjectTypeAndTypeAndNameEqualsIgnoreCase(savedSearch.getOwner(), savedSearch.getSearchObjectType(), savedSearch.getType(), savedSearch.getName());
            if (existSearch1 != null) {
                throw new CassiniException(messageSource.getMessage("name_already_exists", null, "Name already exist", LocaleContextHolder.getLocale()));
            }
        }
        if (savedSearch.getType().equals("PRIVATE")) {
            savedSearch.setType("PRIVATE");
        }
        return savedSearchRepository.save(savedSearch);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#PLMSavedSearch.id ,'edit')")
    public PLMSavedSearch update(PLMSavedSearch PLMSavedSearch) {
        return savedSearchRepository.save(PLMSavedSearch);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        savedSearchRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMSavedSearch get(Integer id) {
        return savedSearchRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMSavedSearch> getAll() {
        return savedSearchRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMSavedSearch> findAll(Pageable pageable) {
        return savedSearchRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMSavedSearch> findByType(String objectType) {
        List<PLMSavedSearch> savedSearches = new ArrayList<>();
        List<PLMSavedSearch> savedSearches1;
        if (objectType.equalsIgnoreCase("ALL")) {
            savedSearches1 = savedSearchRepository.findAll();
        } else {
            savedSearches1 = savedSearchRepository.findBySearchObjectType(ObjectType.valueOf(objectType));
        }

        for (PLMSavedSearch savedSearch : savedSearches1) {

            if (savedSearch.getOwner().equals(sessionWrapper.getSession().getLogin().getPerson().getId())) {
                savedSearches.add(savedSearch);
            }
            if (!savedSearch.getOwner().equals(sessionWrapper.getSession().getLogin().getPerson().getId())) {
                if (savedSearch.getType().equals("PUBLIC")) {
                    savedSearches.add(savedSearch);
                }
            }
        }
        return savedSearches;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    private List<PLMSavedSearch> getCurrentUserSavedSearches(List<PLMSavedSearch> savedSearches) {
        List<PLMSavedSearch> plmSavedSearches = new ArrayList<>();
        for (PLMSavedSearch savedSearch : savedSearches) {
            if (savedSearch.getOwner().equals(sessionWrapper.getSession().getLogin().getPerson().getId())) {
                plmSavedSearches.add(savedSearch);
            }
            if (!savedSearch.getOwner().equals(sessionWrapper.getSession().getLogin().getPerson().getId())) {
                if (savedSearch.getType().equals("PUBLIC")) {
                    plmSavedSearches.add(savedSearch);
                }
            }
        }

        return plmSavedSearches;
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMSavedSearch> findByOwner(Integer owner, Pageable pageable) {
        Page<PLMSavedSearch> savedSearches = savedSearchRepository.findAll(pageable);
        List<PLMSavedSearch> plmSavedSearches = getCurrentUserSavedSearches(savedSearches.getContent());
        return new PageImpl<PLMSavedSearch>(plmSavedSearches, pageable, savedSearches.getTotalElements());
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMSavedSearch> getSavedSearchesCount() {
        List<PLMSavedSearch> savedSearches = savedSearchRepository.findAll();
        List<PLMSavedSearch> plmSavedSearches = getCurrentUserSavedSearches(savedSearches);
        return plmSavedSearches;
    }
}

