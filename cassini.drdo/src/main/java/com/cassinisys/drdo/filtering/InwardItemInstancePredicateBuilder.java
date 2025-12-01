package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.bom.ItemInstanceStatus;
import com.cassinisys.drdo.model.transactions.*;
import com.cassinisys.drdo.repo.transactions.InwardItemInstanceRepository;
import com.cassinisys.drdo.repo.transactions.InwardItemRepository;
import com.cassinisys.drdo.repo.transactions.InwardRepository;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 31-12-2018.
 */
@Component
public class InwardItemInstancePredicateBuilder implements PredicateBuilder<InwardCriteria, QInwardItemInstance> {

    @Autowired
    private InwardRepository inwardRepository;

    @Autowired
    private InwardItemRepository inwardItemRepository;

    @Autowired
    private InwardItemInstanceRepository inwardItemInstanceRepository;


    @Override
    public Predicate build(InwardCriteria criteria, QInwardItemInstance inwardItemInstance) {
        List<Predicate> predicates = new ArrayList<>();

        List<Predicate> predicates1 = new ArrayList<>();

        if (criteria.getStoreApprove() && !criteria.getAdminPermission()) {

            List<Inward> inwards = inwardRepository.getAllNotFinishedInwards();

            for (Inward inward : inwards) {

                if (inward.getStatus().equals(InwardStatus.STORE)) {
                    List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());

                    for (InwardItem inwardItem : inwardItems) {
                        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());
                        for (InwardItemInstance inwardItemInstance1 : inwardItemInstances) {
                            if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.NEW) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            }
                        }
                    }
                } else if (inward.getStatus().equals(InwardStatus.SSQAG)) {
                    List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());

                    for (InwardItem inwardItem : inwardItems) {
                        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());
                        for (InwardItemInstance inwardItemInstance1 : inwardItemInstances) {
                            if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.P_ACCEPT) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            } else if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.REVIEW) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            } else if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.REJECTED) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            } else if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.ACCEPT) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            } /*else if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.INVENTORY) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            }*/ else if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.STORE_SUBMITTED) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            }
                        }
                    }
                } else if (inward.getStatus().equals(InwardStatus.INVENTORY)) {
                    List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());

                    for (InwardItem inwardItem : inwardItems) {
                        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());

                        for (InwardItemInstance inwardItemInstance1 : inwardItemInstances) {
                            if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.ACCEPT) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            }/* else if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.INVENTORY) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            }*/
                        }
                    }
                }
            }
        }

        if (criteria.getSsqagApprove() && !criteria.getAdminPermission()) {

            List<Inward> inwards = inwardRepository.getAllNotFinishedInwards();

            for (Inward inward : inwards) {
                if (!inward.getStatus().equals(InwardStatus.STORE)) {
                    List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward.getId());

                    for (InwardItem inwardItem : inwardItems) {
                        List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());

                        for (InwardItemInstance inwardItemInstance1 : inwardItemInstances) {
                            if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.STORE_SUBMITTED) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            } else if (inwardItemInstance1.getItem().getStatus().equals(ItemInstanceStatus.REVIEWED) && inwardItemInstance1.getLatest()) {
                                predicates1.add(inwardItemInstance.id.eq(inwardItemInstance1.getId()));
                            }
                        }
                    }
                }
            }
        }

        if (criteria.getAdminPermission()) {
            predicates1.add(inwardItemInstance.item.status.eq(ItemInstanceStatus.NEW).and(inwardItemInstance.latest.eq(true)));
            predicates1.add(inwardItemInstance.item.status.eq(ItemInstanceStatus.P_ACCEPT).and(inwardItemInstance.latest.eq(true)));
            predicates1.add(inwardItemInstance.item.status.eq(ItemInstanceStatus.REVIEW).and(inwardItemInstance.latest.eq(true)));
            predicates1.add(inwardItemInstance.item.status.eq(ItemInstanceStatus.STORE_SUBMITTED).and(inwardItemInstance.latest.eq(true)));
            predicates1.add(inwardItemInstance.item.status.eq(ItemInstanceStatus.REVIEWED).and(inwardItemInstance.latest.eq(true)));
            predicates1.add(inwardItemInstance.item.status.eq(ItemInstanceStatus.ACCEPT).and(inwardItemInstance.latest.eq(true)));
//            predicates1.add(inwardItemInstance.item.status.eq(ItemInstanceStatus.INVENTORY).and(inwardItemInstance.latest.eq(true)));
        }


        return ExpressionUtils.and(ExpressionUtils.anyOf(predicates), ExpressionUtils.anyOf(predicates1));
    }
}
