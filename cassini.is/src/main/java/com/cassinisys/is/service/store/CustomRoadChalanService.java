package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.CustomRoadChallanCriteria;
import com.cassinisys.is.filtering.CustomRoadChallanPredicateBuilder;
import com.cassinisys.is.model.store.CustomRoadChalan;
import com.cassinisys.is.model.store.CustomRoadChalanItem;
import com.cassinisys.is.model.store.ISTopStore;
import com.cassinisys.is.model.store.QCustomRoadChalan;
import com.cassinisys.is.repo.store.CustomRoadChalanItemRepository;
import com.cassinisys.is.repo.store.CustomRoadChalanRepository;
import com.cassinisys.is.repo.store.ISTopStoreRepository;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Service
public class CustomRoadChalanService {

    /* adding dependencies */

    @Autowired
    private CustomRoadChalanRepository customRoadChalanRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private CustomRoadChalanItemRepository customRoadChalanItemRepository;

    @Autowired
    private AutoNumberService autoNumberService;

    @Autowired
    private ISTopStoreRepository topStoreRepository;

    @Autowired
    private CustomRoadChallanPredicateBuilder roadChallanPredicateBuilder;

    /*  methods for CustomRoadChalan */

    @Transactional(readOnly = false)
    public CustomRoadChalan create(CustomRoadChalan customRoadChalan) {
        if (customRoadChalan.getChalanNumber() == null) {
            AutoNumber autoNumber = autoNumberService.getByName("Default Road Challan Number Source");
            String number = autoNumberService.getNextNumber(autoNumber.getId());
            customRoadChalan.setChalanNumber(number);
        }
        CustomRoadChalan roadChalan = customRoadChalanRepository.save(customRoadChalan);
        for (CustomRoadChalanItem roadChalanItem : customRoadChalan.getCustomRoadChalanItems()) {
            roadChalanItem.setRoadChalan(roadChalan);
        }
        customRoadChalanItemRepository.save(customRoadChalan.getCustomRoadChalanItems());
        return customRoadChalan;
    }

    @Transactional(readOnly = false)
    public CustomRoadChalan update(CustomRoadChalan customRoadChalan) {
        return customRoadChalanRepository.save(customRoadChalan);
    }

    @Transactional(readOnly = false)
    public void delete(Integer customRoadChalanId) {
        customRoadChalanRepository.delete(customRoadChalanId);
    }

    @Transactional(readOnly = true)
    public CustomRoadChalan get(Integer customRoadChalanId) {
        return customRoadChalanRepository.findOne(customRoadChalanId);
    }

    @Transactional(readOnly = true)
    public Page<CustomRoadChalan> getAll(Pageable pageable) {
        return customRoadChalanRepository.findAll(pageable);
    }

    public Page<CustomRoadChalan> roadChallanFreeTextSearch(Pageable pageable, CustomRoadChallanCriteria criteria) {
        Predicate predicate = roadChallanPredicateBuilder.build(criteria, QCustomRoadChalan.customRoadChalan);
        return customRoadChalanRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CustomRoadChalan> getPagedRoadChallans(Integer storeId, Pageable pageable) {
        ISTopStore topStore = topStoreRepository.findOne(storeId);
        List<CustomRoadChalan> customRoadChalans = customRoadChalanRepository.findByStore(topStore);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > customRoadChalans.size() ? customRoadChalans.size() : (start + pageable.getPageSize());
        return new PageImpl<CustomRoadChalan>(customRoadChalans.subList(start, end), pageable, customRoadChalans.size());
    }

    /*  methods for CustomRoadChalanItem */
    @Transactional(readOnly = false)
    public List<CustomRoadChalanItem> createRoadChalanItems(List<CustomRoadChalanItem> customRoadChalanItems) {
        return customRoadChalanItemRepository.save(customRoadChalanItems);
    }

    @Transactional(readOnly = false)
    public CustomRoadChalanItem updateRoadChalanItem(CustomRoadChalanItem customRoadChalanItem) {
        return customRoadChalanItemRepository.save(customRoadChalanItem);
    }

    @Transactional(readOnly = false)
    public void deleteRoadChalanItem(Integer roadChalanItemId) {
        customRoadChalanItemRepository.delete(roadChalanItemId);
    }

    @Transactional(readOnly = true)
    public CustomRoadChalanItem getRoadChalanItem(Integer customRoadChalanItemId) {
        return customRoadChalanItemRepository.findOne(customRoadChalanItemId);
    }

    @Transactional(readOnly = true)
    public List<CustomRoadChalanItem> getAllRoadChalanItems(Integer roadChallan) {
        CustomRoadChalan customRoadChalan = customRoadChalanRepository.findOne(roadChallan);
        return customRoadChalanItemRepository.findByRoadChalan(customRoadChalan);
    }

    @Transactional(readOnly = true)
    public Page<CustomRoadChalanItem> getPagedRoadChalanItems(Integer roadChallan, Pageable pageable) {
        List<CustomRoadChalanItem> customRoadChalanItems = getAllRoadChalanItems(roadChallan);
        for (CustomRoadChalanItem customRoadChalanItem : customRoadChalanItems) {
            List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectId(customRoadChalanItem.getId());
            if (objectAttributes != null) {
                if (objectAttributes.size() > 0) {
                    customRoadChalanItem.getItemDTO().setEditAttribute(true);
                }
            }
        }
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > customRoadChalanItems.size() ? customRoadChalanItems.size() : (start + pageable.getPageSize());
        return new PageImpl<CustomRoadChalanItem>(customRoadChalanItems.subList(start, end), pageable, customRoadChalanItems.size());
    }
}
