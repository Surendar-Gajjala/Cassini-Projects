package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 12-06-2020.
 */
@Component
public class DCRItemsPredicateBuilder implements PredicateBuilder<DCRItemsCriteria, QPLMItem> {

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Autowired
    private DCRAffectedItemRepository dcrAffectedItemRepository;

    @Autowired
    private DCOAffectedItemRepository dcoAffectedItemRepository;

    @Autowired
    private ECRAffectedItemRepository ecrAffectedItemRepository;

    @Autowired
    private ChangeRelatedItemRepository changeRelatedItemRepository;
    @Autowired
    private PRProblemItemRepository prProblemItemRepository;
    @Autowired
    private PRRelatedItemRepository prRelatedItemRepository;
    @Autowired
    private QCRProblemItemRepository qcrProblemItemRepository;
    @Autowired
    private QCRRelatedItemRepository qcrRelatedItemRepository;
    @Autowired
    private ItemInspectionRelatedItemRepository itemInspectionRelatedItemRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private VarianceAffectedItemRepository varianceAffectedItemRepository;


    @Override
    public Predicate build(DCRItemsCriteria criteria, QPLMItem pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(DCRItemsCriteria criteria, QPLMItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getItemNumber())) {
            predicates.add(pathBase.itemNumber.containsIgnoreCase(criteria.getItemNumber()));
        }
        if (!Criteria.isEmpty(criteria.getItemName())) {
            predicates.add(pathBase.itemName.containsIgnoreCase(criteria.getItemName()));
        }
        if (!Criteria.isEmpty(criteria.getItemType())) {
            PLMItemType itemType = itemTypeRepository.findOne(criteria.getItemType());
            predicates.add(pathBase.itemType.id.eq(itemType.getId()));
        }

        List<PLMItem> itemList = itemRepository.findAll();
        for (PLMItem item : itemList) {
            PLMItemRevision revision = itemRevisionRepository.findOne(item.getLatestRevision());
            if (revision.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.CANCELLED)) {
                predicates.add(pathBase.id.ne(item.getId()));
            }
        }

        if (!Criteria.isEmpty(criteria.getDcr())) {
            List<PLMDCRAffectedItem> affectedItems = dcrAffectedItemRepository.findByDcr(criteria.getDcr());
            if (affectedItems.size() > 0) {
                for (PLMDCRAffectedItem affectedItem : affectedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
                }
            }
            List<PLMChangeRelatedItem> changeRelatedItems = changeRelatedItemRepository.findByChange(criteria.getDcr());
            if (changeRelatedItems.size() > 0) {
                for (PLMChangeRelatedItem relatedItem : changeRelatedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
                    predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
                }
            }

        }
        if (!Criteria.isEmpty(criteria.getDco())) {
            List<PLMDCOAffectedItem> affectedItems = dcoAffectedItemRepository.findByDco(criteria.getDco());
            if (affectedItems.size() > 0) {
                for (PLMDCOAffectedItem affectedItem : affectedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
                }
            }
            List<PLMChangeRelatedItem> changeRelatedItems = changeRelatedItemRepository.findByChange(criteria.getDco());
            if (changeRelatedItems.size() > 0) {
                for (PLMChangeRelatedItem relatedItem : changeRelatedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
                    predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
                }
            }

        }
        if (!Criteria.isEmpty(criteria.getVariance())) {
            List<PLMVarianceAffectedItem> affectedItems = varianceAffectedItemRepository.findByVariance(criteria.getVariance());
            if (affectedItems.size() > 0) {
                for (PLMVarianceAffectedItem affectedItem : affectedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
                }
            }
            List<PLMChangeRelatedItem> changeRelatedItems = changeRelatedItemRepository.findByChange(criteria.getVariance());
            if (changeRelatedItems.size() > 0) {
                for (PLMChangeRelatedItem relatedItem : changeRelatedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
                    predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
                }
            }

        }
        if (!Criteria.isEmpty(criteria.getEcr())) {
            List<PLMChangeRelatedItem> changeRelatedItems = changeRelatedItemRepository.findByChange(criteria.getEcr());
            List<PLMECRAffectedItem> affectedItems = ecrAffectedItemRepository.findByEcr(criteria.getEcr());

            if (changeRelatedItems.size() > 0) {
                for (PLMChangeRelatedItem relatedItem : changeRelatedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
                    predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
                }
            }

            for (PLMECRAffectedItem affectedItem : affectedItems) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
            }

        }
        if (!Criteria.isEmpty(criteria.getMco())) {
            List<PLMChangeRelatedItem> changeRelatedItems = changeRelatedItemRepository.findByChange(criteria.getMco());
            if (changeRelatedItems.size() > 0) {
                for (PLMChangeRelatedItem relatedItem : changeRelatedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
                    predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
                }
            }

        }
        if (!Criteria.isEmpty(criteria.getInspection())) {
            List<PQMItemInspectionRelatedItem> inspectionRelatedItems = itemInspectionRelatedItemRepository.findByInspection(criteria.getInspection());
            if (inspectionRelatedItems.size() > 0) {
                for (PQMItemInspectionRelatedItem relatedItem : inspectionRelatedItems) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
                    predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
                }
            }

        }
        if (!Criteria.isEmpty(criteria.getProblemReport())) {
            List<PQMPRProblemItem> problemItems = prProblemItemRepository.findByProblemReport(criteria.getProblemReport());
            List<PQMPRRelatedItem> relatedItems = prRelatedItemRepository.findByProblemReport(criteria.getProblemReport());

            for (PQMPRProblemItem relatedItem : problemItems) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
                predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
            }

            for (PQMPRRelatedItem relatedItem : relatedItems) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
                predicates.add(pathBase.id.ne(itemRevision.getItemMaster()));
            }
        }

        if (!Criteria.isEmpty(criteria.getQcr())) {
            List<PQMQCRProblemItem> problemItems = qcrProblemItemRepository.findByQcr(criteria.getQcr());
            for (PQMQCRProblemItem affectedItem : problemItems) {
                PLMItemRevision revision = itemRevisionRepository.findOne(affectedItem.getItem());
                predicates.add(pathBase.id.ne(revision.getItemMaster()));
            }

            if (criteria.getRelated()) {
                List<PQMQCRRelatedItem> relatedItems = qcrRelatedItemRepository.findByQcr(criteria.getQcr());
                for (PQMQCRRelatedItem relatedItem : relatedItems) {
                    PLMItemRevision revision = itemRevisionRepository.findOne(relatedItem.getItem());
                    predicates.add(pathBase.id.ne(revision.getItemMaster()));
                }
            }
        }

        predicates.add(pathBase.configured.eq(false));
        return ExpressionUtils.allOf(predicates);
    }


}
