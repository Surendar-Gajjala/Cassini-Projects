package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.PLMAffectedItem;
import com.cassinisys.plm.model.cm.PLMChangeRelatedItem;
import com.cassinisys.plm.model.cm.PLMDCOAffectedItem;
import com.cassinisys.plm.model.cm.PLMVarianceAffectedItem;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMProjectItemReference;
import com.cassinisys.plm.model.pqm.PQMPRProblemItem;
import com.cassinisys.plm.model.pqm.PQMPRRelatedItem;
import com.cassinisys.plm.model.pqm.PQMProblemReport;
import com.cassinisys.plm.model.pqm.PQMQCRProblemItem;
import com.cassinisys.plm.model.req.PLMRequirementDocumentChildren;
import com.cassinisys.plm.model.req.PLMRequirementItem;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.ProjectItemReferenceRepository;
import com.cassinisys.plm.repo.pm.ProjectRepository;
import com.cassinisys.plm.repo.pqm.PRProblemItemRepository;
import com.cassinisys.plm.repo.pqm.PRRelatedItemRepository;
import com.cassinisys.plm.repo.pqm.ProblemReportRepository;
import com.cassinisys.plm.repo.pqm.QCRProblemItemRepository;
import com.cassinisys.plm.repo.req.PLMRequirementItemRepository;
import com.cassinisys.plm.repo.req.RequirementDocumentChildrenRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 19-09-2018.
 */
@Component
public class BomItemFilterPredicateBuilder implements PredicateBuilder<BomItemFilterCriteria, QPLMItem> {

    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private AffectedItemRepository affectedItemRepository;
    @Autowired
    private DCRAffectedItemRepository dcrAffectedItemRepository;
    @Autowired
    private DCOAffectedItemRepository dcoAffectedItemRepository;
    @Autowired
    private ECRAffectedItemRepository ecrAffectedItemRepository;
    @Autowired
    private VarianceAffectedItemRepository varianceAffectedItemRepository;
    @Autowired
    private ChangeRelatedItemRepository changeRelatedItemRepository;
    @Autowired
    private PRRelatedItemRepository prRelatedItemRepository;
    @Autowired
    private PRProblemItemRepository prProblemItemRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private QCRProblemItemRepository qcrProblemItemRepository;
    @Autowired
    private ProjectItemReferenceRepository projectItemReferenceRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SubstitutePartRepository substitutePartRepository;
    @Autowired
    private AlternatePartRepository alternatePartRepository;
    @Autowired
    private PLMRequirementItemRepository requirementItemRepository;
    @Autowired
    private RequirementDocumentChildrenRepository requirementDocumentChildrenRepository;

    @Override
    public Predicate build(BomItemFilterCriteria criteria, QPLMItem pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(BomItemFilterCriteria criteria, QPLMItem pathBase) {
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


        if (!Criteria.isEmpty(criteria.getItem())) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(Integer.parseInt(criteria.getItem()));
            PLMItem itemMaster = itemRepository.findOne(itemRevision.getItemMaster());
            if (!itemMaster.getConfigurable()) {
                predicates.add(pathBase.configurable.eq(false));
            }

            predicates.add(pathBase.id.ne(itemMaster.getId()));
            if (Criteria.isEmpty(criteria.getBomItem())) {
                List<Integer> itemIds = bomRepository.getItemIdsByParent(itemRevision.getId());
                if (itemIds.size() > 0) {
                    predicates.add(pathBase.id.notIn(itemIds));
                }
            }
            List<Integer> parentBomIds = bomRepository.getParentItemIdsByItem(itemMaster.getId());
            if (parentBomIds.size() > 0) {
                List<Integer> parentItemIds = new ArrayList<>();
                for (Integer parentItem : parentBomIds) {
                    parentItemIds.add(parentItem);
                    predicates = getParentItems(parentItem, predicates, pathBase);
                }
                predicates.add(pathBase.id.notIn(parentItemIds));
            }
            predicates = removeRejectedItems(predicates, pathBase);
        }
        if (!Criteria.isEmpty(criteria.getBomItem())) {
            PLMBom bomItem = bomRepository.findOne(criteria.getBomItem());
            PLMItem item = itemRepository.findOne(bomItem.getItem().getId());
            if (!item.getConfigurable()) {
                predicates.add(pathBase.configurable.eq(false));
            }
            predicates.add(pathBase.id.ne(item.getId()));
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
            List<Integer> ids = bomRepository.getItemIdsByParent(itemRevision.getId());
            if (ids.size() > 0) {
                predicates.add(pathBase.id.notIn(ids));
            }
            List<Integer> parentBomIds = bomRepository.getParentItemIdsByItem(item.getId());
            if (parentBomIds.size() > 0) {
                List<Integer> parentItemIds = new ArrayList<>();
                for (Integer parentItem : parentBomIds) {
                    parentItemIds.add(parentItem);
                    predicates = getParentItems(parentItem, predicates, pathBase);
                }
                predicates.add(pathBase.id.notIn(parentItemIds));
            }
            predicates = removeRejectedItems(predicates, pathBase);
        }
        if (!Criteria.isEmpty(criteria.getReplaceBomItem())) {
            PLMBom bomItem = bomRepository.findOne(criteria.getReplaceBomItem());
            if (bomItem.getSubstituteItem() != null) {
                predicates.add(pathBase.id.ne(bomItem.getSubstituteItem().getId()));
            }
            predicates.add(pathBase.id.ne(bomItem.getItem().getId()));
            predicates.add(pathBase.itemType.eq(bomItem.getItem().getItemType()));
        }
        if (!Criteria.isEmpty(criteria.getSubstitutePart())) {
            PLMBom bomItem = bomRepository.findOne(criteria.getSubstitutePart());
            List<PLMSubstitutePart> substituteParts = substitutePartRepository.findByPart(bomItem.getItem().getId());
            for (PLMSubstitutePart substitutePart : substituteParts) {
                predicates.add(pathBase.id.ne(substitutePart.getReplacementPart()));
            }
            predicates.add(pathBase.id.ne(bomItem.getItem().getId()));
            predicates.add(pathBase.itemType.itemClass.eq(ItemClass.PART));
        }
        if (!Criteria.isEmpty(criteria.getAlternatePart())) {
            List<PLMAlternatePart> alternateParts = alternatePartRepository.findByPart(criteria.getAlternatePart());
            for (PLMAlternatePart alternatePart : alternateParts) {
                predicates.add(pathBase.id.ne(alternatePart.getReplacementPart()));
            }
            predicates.add(pathBase.id.ne(criteria.getAlternatePart()));
            predicates.add(pathBase.itemType.itemClass.eq(ItemClass.PART));
        }
        if (!Criteria.isEmpty(criteria.getEco())) {
            List<PLMAffectedItem> affectedItems = affectedItemRepository.findByChange(criteria.getEco());
            if (affectedItems.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                affectedItems.forEach(affectedItem -> {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    ids.add(itemRevision.getItemMaster());
                });
                predicates.add(pathBase.id.notIn(ids));
            }

            if (criteria.getAffectedItemIds().size() > 0) {
                List<Integer> ids = new ArrayList<>();
                criteria.getAffectedItemIds().forEach(affectedItem -> {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem);
                    ids.add(itemRevision.getItemMaster());
                });
                predicates.add(pathBase.id.notIn(ids));
            }

            List<Integer> itemIds = new ArrayList<>();
            List<PLMItem> items = itemRepository.findAll();
            items.forEach(plmItem -> {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
                if (itemRevision.getRevision().equals("-")) {
                    itemIds.add(itemRevision.getItemMaster());
                } else if (itemRevision.getReleased()) {
                    itemIds.add(itemRevision.getItemMaster());
                } else if (plmItem.getLatestReleasedRevision() != null && itemRevision.getRejected()) {
                    PLMItemRevision releasedRevision = itemRevisionRepository.findOne(plmItem.getLatestReleasedRevision());
                    if (releasedRevision.getReleased()) {
                        itemIds.add(itemRevision.getItemMaster());
                    }
                } else if (itemRevision.getRejected()) {
                    PLMItemRevision oldRevision = itemRevisionRepository.getByItemMasterAndRevision(plmItem.getId(), "-");
                    if (oldRevision != null) {
                        itemIds.add(oldRevision.getItemMaster());
                    }
                } else if (!itemRevision.getReleased()) {
                    if (itemIds.indexOf(itemRevision.getItemMaster()) == -1) {
                        itemIds.add(itemRevision.getItemMaster());
                    }
                }
            });
            if (itemIds.size() > 0) {
                predicates.add(pathBase.id.in(itemIds));
            }
            predicates.add(pathBase.itemType.requiredEco.eq(true).and(pathBase.isTemporary.eq(false)));

        }

        if (!Criteria.isEmpty(criteria.getVariance())) {
            List<PLMVarianceAffectedItem> varianceAffectedItems = varianceAffectedItemRepository.findByVariance(criteria.getVariance());
            if (varianceAffectedItems.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                varianceAffectedItems.forEach(varianceAffectedItem -> {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(varianceAffectedItem.getItem());
                    ids.add(itemRevision.getItemMaster());
                });
                predicates.add(pathBase.id.notIn(ids));
            }
            predicates = removeRejectedItems(predicates, pathBase);
        }

        if (!Criteria.isEmpty(criteria.getVariance()) && criteria.getRelated()) {
            List<PLMChangeRelatedItem> changeRelatedItems = changeRelatedItemRepository.findByChange(criteria.getVariance());
            if (changeRelatedItems.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                changeRelatedItems.forEach(changeRelatedItem -> {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(changeRelatedItem.getItem());
                    ids.add(itemRevision.getItemMaster());
                });
                predicates.add(pathBase.id.notIn(ids));
            }
            predicates = removeRejectedItems(predicates, pathBase);
        }

        if (!Criteria.isEmpty(criteria.getDcr())) {
            /*List<Integer> ids = new ArrayList<>();
            List<PLMItemType> itemTypes = itemTypeRepository.findByItemClass(ItemClass.DOCUMENT);
            itemTypes.forEach(itemType -> {
                ids.add(itemType.getId());
            });
            List<PLMItem> items = itemRepository.findByItemTypeIdIn(ids);
            List<Integer> integers = new ArrayList<>();
            for (PLMItem plmItem : items) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
                if (itemRevision.getReleased()) {
                    integers.add(itemRevision.getItemMaster());
                } else if (plmItem.getLatestReleasedRevision() != null && itemRevision.getRejected()) {
                    PLMItemRevision releasedRevision = itemRevisionRepository.findOne(plmItem.getLatestReleasedRevision());
                    if (releasedRevision.getReleased()) {
                        integers.add(itemRevision.getItemMaster());
                    }
                } else {
                    predicates.add(pathBase.id.ne(plmItem.getId()));
                }
            }*/

           /* List<PLMDCRAffectedItem> affectedItems = dcrAffectedItemRepository.findByDcr(criteria.getDcr());
            if (affectedItems.size() > 0) {
                List<Integer> itemIds = new ArrayList<>();
                affectedItems.forEach(affectedItem -> {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    itemIds.add(itemRevision.getItemMaster());
                });
                predicates.add(pathBase.id.notIn(itemIds));
            }*/

            List<Integer> affectedItemIds = dcrAffectedItemRepository.getAffectedItemIdsByDcr(criteria.getDcr());
            if (affectedItemIds.size() > 0) {
                List<Integer> itemMasterIds = itemRevisionRepository.getItemIdsByRevisionIds(affectedItemIds);
                predicates.add(pathBase.id.notIn(itemMasterIds));
            }

            List<Integer> itemIds = itemRevisionRepository.getDocumentItemsReleasedItemMasterIds();

            List<Integer> ids = itemRevisionRepository.getDocumentLatestReleasedItemMasterIds();
            if (ids.size() > 0) {
                List<Integer> itemMasterIds = itemRevisionRepository.getDocumentItemMasterIdsByLatestReleasedItemIds(ids);
                itemIds.addAll(itemMasterIds);
            }

            predicates.add(pathBase.id.in(itemIds));
            predicates.add(pathBase.itemType.itemClass.eq(ItemClass.DOCUMENT));

        }

        if (!Criteria.isEmpty(criteria.getDco())) {
            List<Integer> ids = new ArrayList<>();
            List<Integer> itemIds = new ArrayList<>();
            List<PLMItemType> itemTypes = itemTypeRepository.findByItemClass(ItemClass.DOCUMENT);
            itemTypes.forEach(itemType -> {
                ids.add(itemType.getId());
            });
            List<PLMItem> items = itemRepository.findByItemTypeIdIn(ids);
            List<Integer> integers = new ArrayList<>();
            for (PLMItem plmItem : items) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
                if (itemRevision.getRevision().equals("-")) {
                    itemIds.add(itemRevision.getItemMaster());
                } else if (itemRevision.getReleased()) {
                    itemIds.add(itemRevision.getItemMaster());
                } else if (plmItem.getLatestReleasedRevision() != null && itemRevision.getRejected()) {
                    PLMItemRevision releasedRevision = itemRevisionRepository.findOne(plmItem.getLatestReleasedRevision());
                    if (releasedRevision.getReleased()) {
                        itemIds.add(itemRevision.getItemMaster());
                    }
                } else if (itemRevision.getRejected()) {
                    PLMItemRevision oldRevision = itemRevisionRepository.getByItemMasterAndRevision(plmItem.getId(), "-");
                    if (oldRevision != null) {
                        itemIds.add(oldRevision.getItemMaster());
                    }
                } else if (!itemRevision.getReleased()) {
                    if (itemIds.indexOf(itemRevision.getItemMaster()) == -1) {
                        itemIds.add(itemRevision.getItemMaster());
                    }
                }
            }
            if (itemIds.size() > 0) {
                predicates.add(pathBase.id.in(itemIds));
            }
            predicates.add(pathBase.itemType.requiredEco.eq(true).and(pathBase.isTemporary.eq(false)));

            List<PLMDCOAffectedItem> affectedItems = dcoAffectedItemRepository.findByDco(criteria.getDco());
            if (affectedItems.size() > 0) {
                List<Integer> affectedItemIds = new ArrayList<>();
                affectedItems.forEach(affectedItem -> {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    affectedItemIds.add(itemRevision.getItemMaster());
                });
                predicates.add(pathBase.id.notIn(affectedItemIds));
            }
            predicates.add(pathBase.itemType.itemClass.eq(ItemClass.DOCUMENT));
        }
        if (!Criteria.isEmpty(criteria.getEcr())) {

            /*List<PLMItemRevision> releasedRevisions = itemRevisionRepository.findByReleasedTrueOrderByReleasedDate();
            List<Integer> itemIds = new ArrayList<>();
            releasedRevisions.forEach(plmItemRevision -> {

                PLMItem item = itemRepository.findOne(plmItemRevision.getItemMaster());
                if (item.getLatestRevision().equals(plmItemRevision.getId())) {
                    if (itemIds.indexOf(plmItemRevision.getItemMaster()) == -1) {
                        itemIds.add(plmItemRevision.getItemMaster());
                    }
                }
            });
            List<PLMECRAffectedItem> affectedItems = ecrAffectedItemRepository.findByEcr(criteria.getEcr());
            if (affectedItems.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                affectedItems.forEach(affectedItem -> {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    ids.add(itemRevision.getItemMaster());
                });
                predicates.add(pathBase.id.notIn(ids));
            }
            List<PLMItem> items = itemRepository.findAll();
            items.forEach(plmItem -> {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
                if (itemRevision.getReleased()) {
                    if (itemIds.indexOf(itemRevision.getItemMaster()) == -1) {
                        itemIds.add(itemRevision.getItemMaster());
                    }
                } else if (plmItem.getLatestReleasedRevision() != null && itemRevision.getRejected()) {
                    PLMItemRevision releasedRevision = itemRevisionRepository.findOne(plmItem.getLatestReleasedRevision());
                    if (releasedRevision.getReleased()) {
                        if (itemIds.indexOf(releasedRevision.getItemMaster()) == -1) {
                            itemIds.add(itemRevision.getItemMaster());
                        }
                    }
                }
            });*/
            List<Integer> affectedItemIds = ecrAffectedItemRepository.getAffectedItemIdsByEcr(criteria.getEcr());
            if (affectedItemIds.size() > 0) {
                List<Integer> itemMasterIds = itemRevisionRepository.getItemIdsByRevisionIds(affectedItemIds);
                predicates.add(pathBase.id.notIn(itemMasterIds));
            }

            List<Integer> itemIds = itemRevisionRepository.getReleasedItemMasterIds();

            List<Integer> ids = itemRevisionRepository.getLatestReleasedItemMasterIds();
            if (ids.size() > 0) {
                List<Integer> itemMasterIds = itemRevisionRepository.getItemMasterIdsByLatestReleasedItemIds(ids);
                itemIds.addAll(itemMasterIds);
            }
            predicates.add(pathBase.id.in(itemIds));
            predicates.add(pathBase.itemType.requiredEco.eq(true));

        }

        if (!Criteria.isEmpty(criteria.getProblemReport()) && criteria.getRelated()) {
            List<PQMPRRelatedItem> affectedItems = prRelatedItemRepository.findByProblemReport(criteria.getProblemReport());
            if (affectedItems.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                affectedItems.forEach(affectedItem -> {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
                    ids.add(itemRevision.getItemMaster());
                });
                predicates.add(pathBase.id.notIn(ids));
            }
            predicates = removeRejectedItems(predicates, pathBase);
        }

        if (!Criteria.isEmpty(criteria.getProblemReport()) && !criteria.getRelated()) {
            PQMProblemReport problemReport = problemReportRepository.findOne(criteria.getProblemReport());
            if (problemReport.getProduct() != null) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemReport.getProduct());
                List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(itemRevision);

                List<Integer> ids = new ArrayList<>();

                for (PLMBom bom : bomList) {
                    if (bom.getAsReleasedRevision() != null) {
                        PLMItemRevision revision = itemRevisionRepository.findOne(bom.getAsReleasedRevision());
                        if (revision.getReleased()) {
                            ids.add(revision.getItemMaster());
                        }
                    }
                }

                predicates.add(pathBase.id.in(ids));
            } else {
                predicates.add(pathBase.latestReleasedRevision.isNotNull());
            }
            List<PQMPRProblemItem> affectedItems = prProblemItemRepository.findByProblemReport(criteria.getProblemReport());
            if (affectedItems.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                for (PQMPRProblemItem affectedItem : affectedItems) {
                    PLMItemRevision revision = itemRevisionRepository.findOne(affectedItem.getItem());
                    ids.add(revision.getItemMaster());
                }
                predicates.add(pathBase.id.notIn(ids));
            }
        }

        if (!Criteria.isEmpty(criteria.getQcr())) {
            List<PLMItemRevision> releasedRevisions = itemRevisionRepository.findByReleasedTrueOrderByReleasedDate();
            List<Integer> itemIds = new ArrayList<>();
            releasedRevisions.forEach(plmItemRevision -> {
                PLMItem item = itemRepository.findOne(plmItemRevision.getItemMaster());
                if (item.getLatestRevision().equals(plmItemRevision.getId())) {
                    if (itemIds.indexOf(plmItemRevision.getItemMaster()) == -1) {
                        itemIds.add(plmItemRevision.getItemMaster());
                    }
                }
            });

            List<PQMQCRProblemItem> problemItems = qcrProblemItemRepository.findByQcr(criteria.getQcr());
            for (PQMQCRProblemItem affectedItem : problemItems) {
                PLMItemRevision revision = itemRevisionRepository.findOne(affectedItem.getItem());
                predicates.add(pathBase.id.ne(revision.getItemMaster()));
            }

            List<PLMItem> items = itemRepository.findAll();
            items.forEach(plmItem -> {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
                if (itemRevision.getReleased()) {
                    if (itemIds.indexOf(itemRevision.getItemMaster()) == -1) {
                        itemIds.add(itemRevision.getItemMaster());
                    }
                } else if (plmItem.getLatestReleasedRevision() != null && itemRevision.getRejected()) {
                    PLMItemRevision releasedRevision = itemRevisionRepository.findOne(plmItem.getLatestReleasedRevision());
                    if (releasedRevision.getReleased()) {
                        if (itemIds.indexOf(releasedRevision.getItemMaster()) == -1) {
                            itemIds.add(releasedRevision.getItemMaster());
                        }
                    }
                }
            });
            predicates.add(pathBase.id.in(itemIds));
            predicates.add(pathBase.itemType.requiredEco.eq(true));
        }
        if (!Criteria.isEmpty(criteria.getProject())) {
            PLMProject plmProject = projectRepository.findOne(criteria.getProject());
            List<PLMProjectItemReference> plmProjectItemReferences = projectItemReferenceRepository.findByProject(plmProject);
            for (PLMProjectItemReference plmProjectItemReference : plmProjectItemReferences) {
                predicates.add(pathBase.id.ne(plmProjectItemReference.getItem().getItemMaster()));
            }
        }
        if (!Criteria.isEmpty(criteria.getRequirement())) {
            PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(criteria.getRequirement());
            List<PLMRequirementItem> requirementItems = requirementItemRepository.getItemsByRequirement(documentChildren.getRequirementVersion().getId());
            for (PLMRequirementItem requirementItem : requirementItems) {
                predicates.add(pathBase.id.ne(requirementItem.getItem().getItemMaster()));
            }
        }
        predicates.add(pathBase.configured.eq(false));
        return ExpressionUtils.allOf(predicates);
    }

    private List<Predicate> getParentItems(Integer parentItem, List<Predicate> predicates, QPLMItem pathBase) {
        List<Integer> parentBomIds = bomRepository.getParentItemIdsByItem(parentItem);
        if (parentBomIds.size() > 0) {
            List<Integer> parentItemIds = new ArrayList<>();
            for (Integer parent : parentBomIds) {
                parentItemIds.add(parent);
                predicates = getParentItems(parent, predicates, pathBase);
            }
            predicates.add(pathBase.id.notIn(parentItemIds));
        }
        return predicates;
    }

    private List<Predicate> removeRejectedItems(List<Predicate> predicates, QPLMItem pathBase) {
        List<PLMItemRevision> itemRevisions = itemRevisionRepository.getRejectedItemRevisions();
        List<Integer> obsoleteIds = itemRevisionRepository.getObsoleteItemRevisions();
        List<Integer> ids = new ArrayList<>();
        itemRevisions.forEach(revision -> {
            PLMItem item = itemRepository.findOne(revision.getItemMaster());
            if (item.getLatestRevision().equals(revision.getId()) && revision.getRejected()) {
                ids.add(revision.getItemMaster());
            }
        });
        if (obsoleteIds.size() > 0) {
            ids.addAll(obsoleteIds);
        }
        if (ids.size() > 0) {
            predicates.add(pathBase.id.notIn(ids));
        }
        return predicates;
    }
}
