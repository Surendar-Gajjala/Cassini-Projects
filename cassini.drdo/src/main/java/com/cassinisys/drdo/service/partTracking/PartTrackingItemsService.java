package com.cassinisys.drdo.service.partTracking;

import com.cassinisys.drdo.model.partTracking.PartTrackingItems;
import com.cassinisys.drdo.repo.bom.BomRepository;
import com.cassinisys.drdo.repo.bom.ItemRepository;
import com.cassinisys.drdo.repo.partTracking.PartTrackingItemsRepository;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Service
public class PartTrackingItemsService implements CrudService<PartTrackingItems, Integer> {


    @Autowired
    private PartTrackingItemsRepository partTrackingItemsRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BomRepository bomRepository;


    @Override
    @Transactional(readOnly = false)
    public PartTrackingItems create(PartTrackingItems imPartTrackingsItem) {
        return partTrackingItemsRepository.save(imPartTrackingsItem);
    }

    @Override
    @Transactional(readOnly = false)
    public PartTrackingItems update(PartTrackingItems imPartTrackingsItem) {
        return partTrackingItemsRepository.save(imPartTrackingsItem);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer integer) {
        partTrackingItemsRepository.delete(integer);
    }

    @Override
    @Transactional(readOnly = true)
    public PartTrackingItems get(Integer integer) {
        return partTrackingItemsRepository.findOne(integer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartTrackingItems> getAll() {
        return partTrackingItemsRepository.findAll();
    }

    @Transactional(readOnly = false)
    public List<PartTrackingItems> saveMultiple(List<PartTrackingItems> imPartTrackingsItems) {

        return partTrackingItemsRepository.save(imPartTrackingsItems);
    }

    @Transactional(readOnly = true)
    public List<PartTrackingItems> getByItem(Integer integer) {
        /*Item imItem = null;
        Bom bom = bomRepository.findOne(integer);

        if (!bom.getParent().getItemType().getName().equals("EBOM"))
            imItem = itemRepository.findOne(bom.getParent().getParentBom());
        else
            imItem = itemRepository.findOne(bom.getParent().getId());*/

        List<PartTrackingItems> iMPartTrackingsItems = partTrackingItemsRepository.findByItemOrderBySerialNo(integer);

       /* for (PartTrackingItems imPartTrackingsItem : iMPartTrackingsItems) {
            imPartTrackingsItem.setSection(bom.getItem().getItemName());
            imPartTrackingsItem.setSecItemNumber(bom.getItem().getItemNumber());
            imPartTrackingsItem.setMissile(bom.getParent().getItemNumber());
            imPartTrackingsItem.setParentOfMissile(imItem.getItemNumber());
        }*/

        return iMPartTrackingsItems;
    }
}
