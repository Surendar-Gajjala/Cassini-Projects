package com.cassinisys.is.service.store;

import com.cassinisys.is.model.store.CustomIssueChalan;
import com.cassinisys.is.model.store.CustomIssueItem;
import com.cassinisys.is.repo.store.CustomIssueChalanRepository;
import com.cassinisys.is.repo.store.CustomIssueItemRepository;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Service
public class CustomIssueChalanService implements CrudService<CustomIssueChalan, Integer> {

    /* adding dependencies */

    @Autowired
    CustomIssueChalanRepository customIssueChalanRepository;

    @Autowired
    CustomIssueItemRepository customIssueItemRepository;

    /*  methods for CustomIssueChalan */

    @Transactional(readOnly = false)
    @Override
    public CustomIssueChalan create(CustomIssueChalan customIssueChalan) {
        return customIssueChalanRepository.save(customIssueChalan);
    }

    @Override
    @Transactional(readOnly = false)
    public CustomIssueChalan update(CustomIssueChalan customIssueChalan) {
        return customIssueChalanRepository.save(customIssueChalan);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer customIssueChalanId) {
        customIssueChalanRepository.delete(customIssueChalanId);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomIssueChalan get(Integer customIssueChalanId) {
        return customIssueChalanRepository.getOne(customIssueChalanId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomIssueChalan> getAll() {
        return customIssueChalanRepository.findAll();
    }
    
     /*  methods for CustomIssueItem */

    @Transactional(readOnly = false)
    public List<CustomIssueItem> createIssueChalanItems(List<CustomIssueItem> CustomIssueItems) {
        return customIssueItemRepository.save(CustomIssueItems);
    }

    @Transactional(readOnly = false)
    public CustomIssueItem updateIssueChalanItem(CustomIssueItem CustomIssueItem) {
        return customIssueItemRepository.save(CustomIssueItem);
    }

    @Transactional(readOnly = false)
    public void deleteIssueChalanItem(Integer customIssueChalanItemId) {
        customIssueItemRepository.delete(customIssueChalanItemId);
    }

    @Transactional(readOnly = true)
    public CustomIssueItem getIssueChalanItem(Integer CustomIssueItemId) {
        return customIssueItemRepository.getOne(CustomIssueItemId);
    }

    @Transactional(readOnly = true)
    public List<CustomIssueItem> getAllIssueChalanItems() {
        return customIssueItemRepository.findAll();
    }
}
