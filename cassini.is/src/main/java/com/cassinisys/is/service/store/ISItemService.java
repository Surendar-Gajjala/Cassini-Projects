package com.cassinisys.is.service.store;

import com.cassinisys.is.model.store.ISItem;
import com.cassinisys.is.repo.store.ISItemRepository;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Service
public class ISItemService implements CrudService<ISItem, Integer> {

    @Autowired
    private ISItemRepository itemRepository;

    @Override
    @Transactional(readOnly = false)
    public ISItem create(ISItem item) {
        return itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = false)
    public ISItem update(ISItem item) {
        return itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        itemRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ISItem get(Integer id) {
        return itemRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ISItem> getAll() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ISItem getItemByItemNumber(String itemNumber) {
        return itemRepository.findByItemNumber(itemNumber);
    }
}
