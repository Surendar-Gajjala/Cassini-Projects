package com.cassinisys.is.service.store;

import com.cassinisys.is.model.store.ISScrapRequestItem;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.is.repo.store.ISScrapRequestItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varsha Malgireddy on 9/4/2018.
 */
@Service
public class ISScrapRequestItemService {

    @Autowired
    private ISScrapRequestItemRepository isScrapRequestItemRepository;

    @Autowired
    private MaterialItemRepository materialItemRepository;

    @Transactional(readOnly = false)
    public List<ISScrapRequestItem> createScrapItem(List<ISScrapRequestItem> isScrapRequestItems) {
        List<ISScrapRequestItem> savedItems = new ArrayList<>();
        for (ISScrapRequestItem item : isScrapRequestItems) {
            ISScrapRequestItem requestItem = isScrapRequestItemRepository.save(item);
            savedItems.add(requestItem);
        }
        return savedItems;
    }

    @Transactional(readOnly = false)
    public ISScrapRequestItem updateScrapItem(ISScrapRequestItem isScrapRequestItem) {
        return isScrapRequestItemRepository.save(isScrapRequestItem);
    }

    @Transactional(readOnly = true)
    public List<ISScrapRequestItem> getScrapItem(Integer id) {
        return isScrapRequestItemRepository.findByScrapRequest(id);
    }

}
