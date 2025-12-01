package com.cassinisys.is.service.store;

import com.cassinisys.is.model.store.CustomReceiveChalan;
import com.cassinisys.is.model.store.CustomReceiveItem;
import com.cassinisys.is.repo.store.CustomReceiveChalanRepository;
import com.cassinisys.is.repo.store.CustomReceiveItemRepository;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Service
public class CustomReceiveChalanService implements CrudService<CustomReceiveChalan, Integer> {

    /* adding dependencies */

    @Autowired
    CustomReceiveChalanRepository customReceiveChalanRepository;

    @Autowired
    CustomReceiveItemRepository customReceiveItemRepository;

    /*  methods for CustomReceiveChalan */

    @Transactional(readOnly = false)
    @Override
    public CustomReceiveChalan create(CustomReceiveChalan customReceiveChalan) {
        return customReceiveChalanRepository.save(customReceiveChalan);
    }

    @Override
    @Transactional(readOnly = false)
    public CustomReceiveChalan update(CustomReceiveChalan customReceiveChalan) {
        return customReceiveChalanRepository.save(customReceiveChalan);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer customReceiveChalan) {
        customReceiveChalanRepository.delete(customReceiveChalan);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomReceiveChalan get(Integer customReceiveChalan) {
        return customReceiveChalanRepository.getOne(customReceiveChalan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomReceiveChalan> getAll() {
        return customReceiveChalanRepository.findAll();
    }

     /*  methods for CustomReceiveChalanItem */

    @Transactional(readOnly = false)
    public List<CustomReceiveItem> createReceiveChalanItems(List<CustomReceiveItem> customReceiveChalanItem) {
        return customReceiveItemRepository.save(customReceiveChalanItem);
    }

    @Transactional(readOnly = false)
    public CustomReceiveItem updateReceiveChalanItem(CustomReceiveItem customReceiveChalanItem) {
        return customReceiveItemRepository.save(customReceiveChalanItem);
    }

    @Transactional(readOnly = false)
    public void deleteCustomReceiveItem(Integer customReceiveChalanItemId) {
        customReceiveItemRepository.delete(customReceiveChalanItemId);

    }

    @Transactional(readOnly = true)
    public CustomReceiveItem getCustomReceiveItem(Integer customReceiveChalanItemId) {
        return customReceiveItemRepository.getOne(customReceiveChalanItemId);
    }

    @Transactional(readOnly = true)
    public List<CustomReceiveItem> getAllCustomReceiveItems() {
        return customReceiveItemRepository.findAll();
    }
}
