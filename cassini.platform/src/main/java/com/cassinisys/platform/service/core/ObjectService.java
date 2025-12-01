package com.cassinisys.platform.service.core;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.core.ObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@Service
public class ObjectService implements CrudService<CassiniObject, Integer>,
        PageableService<CassiniObject, Integer> {

    @Autowired
    ObjectRepository objectRepository;


    @Override
    @Transactional
    public CassiniObject create(CassiniObject cassiniObject) {
        return objectRepository.save(cassiniObject);
    }

    @Override
    @Transactional
    public CassiniObject update(CassiniObject cassiniObject) {
        return objectRepository.save(cassiniObject);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        objectRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CassiniObject get(Integer id) {
        return objectRepository.findOne(id);
    }


    @Transactional(readOnly = true)
    public List<CassiniObject> getByObjectType(ObjectType type) {
        return objectRepository.findByObjectType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CassiniObject> getAll() {
        return objectRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CassiniObject> findAll(Pageable pageable) {
        return objectRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<CassiniObject> findMultipleByType(List<Integer> ids, String type) {
        return objectRepository.findByIdInAndObjectType(ids, ObjectType.valueOf(type));
    }

    @Transactional (readOnly = true)
    public CassiniObject findByObjectTypeAndId(String type, Integer id) {
        return objectRepository.findByIdAndObjectType(id, ObjectType.valueOf(type));
    }
}
