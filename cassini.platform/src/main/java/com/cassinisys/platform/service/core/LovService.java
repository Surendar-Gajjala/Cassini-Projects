package com.cassinisys.platform.service.core;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.filtering.LovCriteria;
import com.cassinisys.platform.filtering.LovPredicateBuilder;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.model.core.QLov;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author reddy
 */
@Service
public class LovService implements CrudService<Lov, Integer>,
        PageableService<Lov, Integer> {

    @Autowired
    private LovRepository lovRepository;

    @Autowired
    private LovPredicateBuilder predicateBuilder;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Transactional
    public Lov create(Lov lov) {
        checkNotNull(lov);
        Lov lov1 = lovRepository.findByName(lov.getName());
        if (lov1 != null) {
            throw new RuntimeException("This Name is already exists! please enter another one");
        } else {
            return lovRepository.save(lov);
        }
    }

    @Transactional
    public Lov update(Lov lov) {
        checkNotNull(lov);
        checkNotNull(lov.getId());
        Lov lov1 = lovRepository.findByName(lov.getName());
        if (lov1 != null && lov1.getId() != lov.getId()) {
            throw new RuntimeException("This Name is already exists! please enter another one");
        } else {
            return lovRepository.save(lov);
        }
    }

    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        lovRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Lov get(Integer id) {
        checkNotNull(id);
        Lov lov = lovRepository.findOne(id);
        if (lov == null) {
            throw new ResourceNotFoundException();
        }
        return lov;
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getLovById(Integer id) {
        checkNotNull(id);
        Lov lov = lovRepository.getOne(id);
        List<ObjectTypeAttribute> lovObject = objectTypeAttributeRepository.findByLov(lov);
        if (lov == null) {
            throw new ResourceNotFoundException();
        }
        return lovObject;
    }

    @Transactional(readOnly = true)
    public List<Lov> getAll() {
        return lovRepository.findByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Lov> getAllPersonLovs() {
        return lovRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Map<String, Lov> getLovMapWithNameAsKey() {
        Map<String, Lov> stringLovMap = new HashMap();
        List<Lov> lovs = lovRepository.findAll();
        for (Lov lov : lovs) {
            stringLovMap.put(lov.getName(), lov);
        }
        return stringLovMap;
    }

    @Transactional(readOnly = true)
    public Page<Lov> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("type"),
                    new Order("name")));
        }
        return lovRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Lov> find(LovCriteria criteria, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("type"),
                    new Order("name")));
        }
        Predicate predicate = predicateBuilder.build(criteria, QLov.lov);
        return lovRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public List<Lov> getAllLovsByType(String type) {
        return lovRepository.findByTypeContainingIgnoreCase(type);
    }

    @Transactional(readOnly = true)
    public Lov getLovByName(String name) {
        return lovRepository.findByName(name);
    }

}
