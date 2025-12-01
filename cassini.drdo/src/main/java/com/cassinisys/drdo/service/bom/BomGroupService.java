package com.cassinisys.drdo.service.bom;

import com.cassinisys.drdo.filtering.BomGroupCriteria;
import com.cassinisys.drdo.filtering.BomGroupPredicateBuilder;
import com.cassinisys.drdo.model.bom.BomGroup;
import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.QBomGroup;
import com.cassinisys.drdo.repo.bom.BomGroupRepository;
import com.cassinisys.drdo.repo.bom.BomItemRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.core.CrudService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by subramanyam reddy on 08-11-2018.
 */
@Service
public class BomGroupService implements CrudService<BomGroup, Integer> {

    @Autowired
    private BomGroupRepository bomGroupRepository;

    @Autowired
    private BomGroupPredicateBuilder bomGroupPredicateBuilder;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Override
    @Transactional(readOnly = false)
    public BomGroup create(BomGroup bomGroup) {

        BomGroup existGroupTypeCode = bomGroupRepository.findByTypeAndCodeAndVersity(bomGroup.getType(), bomGroup.getCode(), bomGroup.getVersity());
        BomGroup existGroupTypeName = bomGroupRepository.findByTypeAndNameAndVersity(bomGroup.getType(), bomGroup.getName(), bomGroup.getVersity());

        if (existGroupTypeCode != null) {
            throw new CassiniException(bomGroup.getCode() + " : Code already exist on " + bomGroup.getType());
        }

        if ((existGroupTypeName != null)) {
            throw new CassiniException(bomGroup.getName() + " " + bomGroup.getType() + " already exist");
        }


        return bomGroupRepository.save(bomGroup);
    }

    @Override
    @Transactional(readOnly = false)
    public BomGroup update(BomGroup bomGroup) {
        BomGroup existGroupTypeCode = bomGroupRepository.findByTypeAndCodeAndVersity(bomGroup.getType(), bomGroup.getCode(), bomGroup.getVersity());
        BomGroup existGroupTypeName = bomGroupRepository.findByTypeAndNameAndVersity(bomGroup.getType(), bomGroup.getName(), bomGroup.getVersity());

        if (existGroupTypeCode != null && !existGroupTypeCode.getId().equals(bomGroup.getId())) {
            throw new CassiniException(bomGroup.getCode() + " : Code already exist on " + bomGroup.getType());
        }

        if (existGroupTypeName != null && !existGroupTypeName.getId().equals(bomGroup.getId())) {
            throw new CassiniException(bomGroup.getCode() + " " + bomGroup.getType() + " : Name already exist");
        }
        return bomGroupRepository.save(bomGroup);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        bomGroupRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BomGroup get(Integer id) {
        return bomGroupRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BomGroup> getAll() {
        return bomGroupRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<BomGroup> getAllBomGroups(Pageable pageable) {
        return bomGroupRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<BomGroup> getFilteredBomGroups(Pageable pageable, BomGroupCriteria bomGroupCriteria) {
        Predicate predicate = bomGroupPredicateBuilder.build(bomGroupCriteria, QBomGroup.bomGroup);

        return bomGroupRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<BomGroup> getBomGroupTypesByBom(Pageable pageable, BomGroupCriteria bomGroupCriteria) {
        Predicate predicate = bomGroupPredicateBuilder.build(bomGroupCriteria, QBomGroup.bomGroup);

        return bomGroupRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public List<BomItem> getBomItemsByGroupType(Integer groupTypeId) {
        BomGroup bomGroup = bomGroupRepository.findOne(groupTypeId);
        List<BomItem> bomItems = bomItemRepository.findByTypeRef(bomGroup);
        return bomItems;
    }
}
