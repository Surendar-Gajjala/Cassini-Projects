package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.CustomIndentCriteria;
import com.cassinisys.is.filtering.CustomIndentPredicateBuilder;
import com.cassinisys.is.model.store.CustomIndent;
import com.cassinisys.is.model.store.CustomIndentItem;
import com.cassinisys.is.model.store.QCustomIndent;
import com.cassinisys.is.repo.store.CustomIndentItemRepository;
import com.cassinisys.is.repo.store.CustomIndentRepository;
import com.cassinisys.is.repo.store.ISTopStoreRepository;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Service
public class CustomIndentService implements CrudService<CustomIndent, Integer> {

      /* adding dependencies */

    @Autowired
    CustomIndentRepository customIndentRepository;

    @Autowired
    ISTopStoreRepository isTopStoreRepository;

    @Autowired
    CustomIndentItemRepository customIndentItemRepository;

    @Autowired
    private AutoNumberService autoNumberService;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private CustomIndentPredicateBuilder customIndentPredicateBuilder;

    /*  methods for CustomIndent */

    @Transactional(readOnly = false)
    @Override
    public CustomIndent create(CustomIndent customIndent) {
        List<CustomIndentItem> customIndentItems = new ArrayList<CustomIndentItem>();
        if (customIndent.getIndentNumber() == null) {
            AutoNumber autoNumber = autoNumberService.getByName("Default Indent Number Source");
            String number = autoNumberService.getNextNumber(autoNumber.getId());
            customIndent.setIndentNumber(number);
        }
        CustomIndent customIndent1 = customIndentRepository.save(customIndent);
        if (customIndent.getCustomIndentItems().size() > 0) {
            for (CustomIndentItem customIndentItem : customIndent.getCustomIndentItems()) {
                customIndentItem.setCustomIndent(customIndent1);
                customIndentItems.add(customIndentItem);
            }
            createIndentItems(customIndentItems);
        }
        return customIndent1;
    }

    @Override
    @Transactional(readOnly = false)
    public CustomIndent update(CustomIndent customIndent) {
        List<CustomIndentItem> customIndentItems = new ArrayList<CustomIndentItem>();
        CustomIndent customIndent1 = customIndentRepository.save(customIndent);
        if (customIndent.getCustomIndentItems().size() > 0) {
            for (CustomIndentItem customIndentItem : customIndent.getCustomIndentItems()) {
                customIndentItem.setCustomIndent(customIndent1);
                customIndentItems.add(customIndentItem);
            }
            List<CustomIndentItem> indentItems = updateIndentItems(customIndentItems);
            customIndent1.setCustomIndentItems(indentItems);
        }
        return customIndent1;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer customIndentId) {
        customIndentRepository.delete(customIndentId);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomIndent get(Integer customIndentId) {
        CustomIndent customIndent = customIndentRepository.findOne(customIndentId);
        List<CustomIndentItem> customIndentItems = customIndentItemRepository.findByIndentId(customIndentId);
        customIndent.setCustomIndentItems(customIndentItems);
        return customIndent;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomIndent> getAll() {
        return customIndentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CustomIndent> getPageableIndentsByStore(Pageable pageable, Integer storeId) {
        return customIndentRepository.findByStore(pageable, storeId);
    }

    public Page<CustomIndent> getAllIndents(Pageable pageable) {
        return customIndentRepository.findAll(pageable);
    }

    public Page<CustomIndent> customIndentFreeTextSearch(Pageable pageable, CustomIndentCriteria criteria) {
        Predicate predicate = customIndentPredicateBuilder.build(criteria, QCustomIndent.customIndent);
        return customIndentRepository.findAll(predicate, pageable);
    }

     /*  methods for CustomIndent Attributes */

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getRequiredIndentAttributes(String objectType) {
        List<ObjectTypeAttribute> indentAttributes = objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf(objectType));
        return indentAttributes;
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByIndentIdsAndAttributeIds(Integer[] indentIds, Integer[] objectAttributeIds) {
        Map<Integer, List<ObjectAttribute>> objectAttributesMap = new HashMap();
        List<ObjectAttribute> attributes = objectAttributeRepository.findByObjectIdsInAndAttributeDefIdsIn(indentIds, objectAttributeIds);
        for (ObjectAttribute attribute : attributes) {
            Integer id = attribute.getId().getObjectId();
            List<ObjectAttribute> objectAttributes = objectAttributesMap.get(id);
            if (objectAttributes == null) {
                objectAttributes = new ArrayList<>();
                objectAttributesMap.put(id, objectAttributes);
            }
            objectAttributes.add(attribute);
        }
        return objectAttributesMap;

    }

     /* end methods for CustomIndent Attributes */

      /*  methods for CustomIndentItem */

    @Transactional(readOnly = false)
    public List<CustomIndentItem> createIndentItems(List<CustomIndentItem> CustomIndentItems) {
        return customIndentItemRepository.save(CustomIndentItems);
    }

    @Transactional(readOnly = false)
    public List<CustomIndentItem> updateIndentItems(List<CustomIndentItem> CustomIndentItems) {
        return customIndentItemRepository.save(CustomIndentItems);
    }

    @Transactional(readOnly = false)
    public CustomIndentItem updateIndentItem(CustomIndentItem CustomIndentItem) {
        return customIndentItemRepository.save(CustomIndentItem);
    }

    @Transactional(readOnly = false)
    public void deleteCustomIndentItem(Integer CustomIndentItemId) {
        customIndentItemRepository.delete(CustomIndentItemId);

    }

    @Transactional(readOnly = true)
    public CustomIndentItem getCustomIndentItem(Integer CustomIndentItemId) {
        return customIndentItemRepository.findOne(CustomIndentItemId);
    }

    @Transactional(readOnly = true)
    public List<CustomIndentItem> getAllCustomIndentItems() {
        return customIndentItemRepository.findAll();
    }
}
