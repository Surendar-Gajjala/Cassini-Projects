package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.bom.Bom;
import com.cassinisys.drdo.model.bom.ItemInstanceStatus;
import com.cassinisys.drdo.model.bom.ItemType;
import com.cassinisys.drdo.model.transactions.*;
import com.cassinisys.drdo.repo.bom.BomRepository;
import com.cassinisys.drdo.repo.bom.ItemInstanceStatusHistoryRepository;
import com.cassinisys.drdo.repo.bom.ItemTypeRepository;
import com.cassinisys.drdo.repo.transactions.InwardItemInstanceRepository;
import com.cassinisys.drdo.repo.transactions.InwardItemRepository;
import com.cassinisys.drdo.repo.transactions.InwardRepository;
import com.cassinisys.drdo.service.bom.ItemTypeService;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyam reddy on 27-11-2018.
 */
@Component
public class InwardPredicateBuilder implements PredicateBuilder<InwardCriteria, QInward> {

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private InwardRepository inwardRepository;

    @Autowired
    private InwardItemRepository inwardItemRepository;

    @Autowired
    private InwardItemInstanceRepository inwardItemInstanceRepository;

    @Autowired
    private ItemInstanceStatusHistoryRepository instanceStatusHistoryRepository;

    @Override
    public Predicate build(InwardCriteria criteria, QInward inward) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            predicates.add(inward.number.containsIgnoreCase(criteria.getSearchQuery())
                    .or(inward.gatePass.gatePassNumber.containsIgnoreCase(criteria.getSearchQuery()))
                    .or(inward.notes.containsIgnoreCase(criteria.getSearchQuery()))
                    .or(inward.supplier.supplierName.containsIgnoreCase(criteria.getSearchQuery())));

            ItemType itemType = itemTypeRepository.findByName("System");
            List<ItemType> itemTypes = itemTypeService.getHierarchicalChildren(itemType);
            List<Bom> boms = bomRepository.findSystemTypeBoms(itemTypes);

            for (Bom bom : boms) {
                if (bom.getItem().getItemMaster().getItemName().contains(criteria.getSearchQuery())) {
                    predicates.add(inward.bom.id.eq(bom.getId()));
                }
            }
        }

        List<Predicate> predicates1 = new ArrayList<>();

        if (!criteria.getFinishedPage()) {
            if (!Criteria.isEmpty(criteria.getStatus())) {
                predicates1.add(inward.status.eq(InwardStatus.valueOf(criteria.getStatus())));
            }

            if (criteria.getNotification() && criteria.getAdminPermission()) {
                predicates1.add(inward.status.ne(InwardStatus.FINISH));
            }

            if (!criteria.getNotification() && criteria.getAdminPermission()) {
                predicates1.add(inward.status.ne(InwardStatus.FINISH));
            }

            if (criteria.getStoreApprove() && !criteria.getAdminPermission()) {
                List<Inward> inwards = inwardRepository.getAllNotFinishedInwards();

                Boolean predicateExist = false;

                for (Inward inward1 : inwards) {
                    List<InwardItemInstance> provisionalOrReviewItems = new ArrayList<>();
                    List<InwardItemInstance> acceptedOrInventoryItems = new ArrayList<>();
                    List<InwardItemInstance> verifiedItems = new ArrayList<>();

                    List<InwardItem> inwardItems = inwardItemRepository.findByInward(inward1.getId());

                    for (InwardItem inwardItem : inwardItems) {
                        provisionalOrReviewItems.addAll(inwardItemInstanceRepository.getProvisionalOrReviewInstances(inwardItem.getId(), ItemInstanceStatus.P_ACCEPT,
                                ItemInstanceStatus.REVIEW));
                        provisionalOrReviewItems.addAll(inwardItemInstanceRepository.getProvisionallyAcceptedInwardItemInstances(inwardItem.getId()));
                        provisionalOrReviewItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.REJECTED));
                        provisionalOrReviewItems.addAll(inwardItemInstanceRepository.getInwardItemInstancesByStatus(inwardItem.getId(), ItemInstanceStatus.STORE_SUBMITTED));
                    }

                    if (provisionalOrReviewItems.size() > 0) {
                        predicates1.add(inward.id.eq(inward1.getId()));
                        predicateExist = true;
                    }

                    for (InwardItem inwardItem : inwardItems) {
                        acceptedOrInventoryItems.addAll(inwardItemInstanceRepository.getAcceptOrInventoryInstances(inwardItem.getId(), ItemInstanceStatus.ACCEPT,
                                ItemInstanceStatus.INVENTORY));
                    }

                    if (acceptedOrInventoryItems.size() > 0) {
                        predicates1.add(inward.id.eq(inward1.getId()));
                        predicateExist = true;
                    }

                    if (inward1.getStatus().equals(InwardStatus.STORE)) {
                        predicates1.add(inward.id.eq(inward1.getId()));
                    }
                }
            }

            if (criteria.getSsqagApprove() && !criteria.getAdminPermission()) {
                predicates1.add(inward.status.eq(InwardStatus.SSQAG));
            }

            if (criteria.getCasApprove() && !criteria.getStoreApprove()) {
                predicates1.add(inward.status.ne(InwardStatus.FINISH));
            }
        } else {
            predicates1.add(inward.status.eq(InwardStatus.FINISH));
        }

        List<Predicate> predicates2 = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getFromDate()) && !Criteria.isEmpty(criteria.getToDate())) {
            try {
                Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getToDate());
                Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));
                predicates2.add(inward.createdDate.after(new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getFromDate())));
                predicates2.add(inward.createdDate.before(tomorrow));
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }

        if (!Criteria.isEmpty(criteria.getMonth())) {
            try {
                Date dt = new SimpleDateFormat("MM/yyyy").parse(criteria.getMonth());
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                String firstDate = dateFormat.format(dt);

                LocalDate lastDate = LocalDate.parse(firstDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                lastDate = lastDate.withDayOfMonth(lastDate.getMonth().length(lastDate.isLeapYear()));

                Date lastDateOfMonth = java.sql.Date.valueOf(lastDate);
                Date tomorrow = new Date(lastDateOfMonth.getTime() + (1000 * 60 * 60 * 24));

                predicates2.add(inward.createdDate.after(dt));
                predicates2.add(inward.createdDate.before(tomorrow));
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }

        Predicate predicate = ExpressionUtils.and(ExpressionUtils.anyOf(predicates), ExpressionUtils.anyOf(predicates1));
        return ExpressionUtils.and(ExpressionUtils.anyOf(predicate), ExpressionUtils.allOf(predicates2));
    }
}
