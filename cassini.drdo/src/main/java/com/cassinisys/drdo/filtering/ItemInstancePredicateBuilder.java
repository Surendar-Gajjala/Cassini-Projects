package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.QItemInstance;
import com.cassinisys.drdo.model.transactions.*;
import com.cassinisys.drdo.repo.transactions.GatePassRepository;
import com.cassinisys.drdo.repo.transactions.InwardItemInstanceRepository;
import com.cassinisys.drdo.repo.transactions.InwardItemRepository;
import com.cassinisys.drdo.repo.transactions.InwardRepository;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.QAttachment;
import com.cassinisys.platform.repo.col.AttachmentRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 04-03-2019.
 */
@Component
public class ItemInstancePredicateBuilder implements PredicateBuilder<ItemInstanceCriteria, QItemInstance> {

    @Autowired
    private AttachmentPredicateBuilder attachmentPredicateBuilder;

    @Autowired
    private GatePassPredicateBuilder gatePassPredicateBuilder;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private GatePassRepository gatePassRepository;

    @Autowired
    private InwardRepository inwardRepository;

    @Autowired
    private InwardItemRepository inwardItemRepository;

    @Autowired
    private InwardItemInstanceRepository inwardItemInstanceRepository;

    @Override
    public Predicate build(ItemInstanceCriteria criteria, QItemInstance itemInstance) {
        List<Predicate> predicates = new ArrayList<>();

        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.ASC, "name")));
        criteria.setObjectType(DRDOObjectType.ITEMINSTANCE);

        Predicate predicate = attachmentPredicateBuilder.build(criteria, QAttachment.attachment);

        Page<Attachment> attachments = attachmentRepository.findAll(predicate, pageable);

        List<Integer> objectIds = new ArrayList<>();

        if (attachments.getContent().size() > 0) {
            attachments.getContent().forEach(attachment -> {
                objectIds.add(attachment.getObjectId());
            });
        }

        GatePassCriteria gatePassCriteria = new GatePassCriteria();
        gatePassCriteria.setSearchQuery(criteria.getSearchQuery());

        pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.ASC, "gatePassNumber")));
        predicate = gatePassPredicateBuilder.build(gatePassCriteria, QGatePass.gatePass1);
        Page<GatePass> gatePasses = gatePassRepository.findAll(predicate, pageable);

        if (gatePasses.getContent().size() > 0) {
            gatePasses.getContent().forEach(gatePass -> {
                List<Inward> inwards = inwardRepository.getInwardsByGatePass(gatePass.getId());
                if (inwards.size() > 0) {
                    inwards.forEach(inward -> {
                        List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());
                        if (inwardItems.size() > 0) {
                            inwardItems.forEach(inwardItem -> {
                                List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());
                                if (inwardItemInstances.size() > 0) {
                                    inwardItemInstances.forEach(inwardItemInstance -> {
                                        objectIds.add(inwardItemInstance.getItem().getId());
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }

        predicates.add(itemInstance.oemNumber.containsIgnoreCase(criteria.getSearchQuery())
                .or(itemInstance.upnNumber.containsIgnoreCase(criteria.getSearchQuery()))
                .or(itemInstance.item.itemMaster.itemName.containsIgnoreCase(criteria.getSearchQuery()))
                .or(itemInstance.id.in(objectIds)));

        return ExpressionUtils.allOf(predicates);
    }
}
