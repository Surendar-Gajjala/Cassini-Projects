package com.cassinisys.plm.event;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ItemEventListener {
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Async
    @EventListener
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void itemModified(ItemEvents.ItemBaseEvent event) {
        try {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(event.getItemRevision().getId());
            itemRevisionRepository.save(itemRevision);

            PLMItem item = itemRepository.findOne(event.getItemRevision().getItemMaster());
            itemRepository.save(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
