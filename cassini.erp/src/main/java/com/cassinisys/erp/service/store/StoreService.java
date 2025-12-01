package com.cassinisys.erp.service.store;

import com.cassinisys.erp.model.store.ERPStore;
import com.cassinisys.erp.repo.store.ERPStoreRepository;
import com.cassinisys.erp.service.paging.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Nageshreddy on 19-08-2018.
 */
@Service
public class StoreService implements CrudService<ERPStore, Integer> {

    @Autowired
    private ERPStoreRepository storeRepository;

    @Override
    public ERPStore create(ERPStore erpStore) {
        return storeRepository.save(erpStore);
    }

    @Override
    public ERPStore update(ERPStore erpStore) {
        return storeRepository.save(erpStore);
    }

    @Override
    public void delete(Integer integer) {
        storeRepository.delete(integer);
    }

    @Override
    public ERPStore get(Integer integer) {
        return storeRepository.findOne(integer);
    }

    @Override
    public List<ERPStore> getAll() {
        return storeRepository.findAll();
    }

    public Page<ERPStore> findAll(Pageable pageable) {
        return storeRepository.findAll(pageable);
    }
}
