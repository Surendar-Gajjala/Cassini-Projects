package com.cassinisys.is.filtering;

import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.pm.QISProject;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.model.procm.QISMaterialItem;
import com.cassinisys.is.model.store.AttributeSearchDto;
import com.cassinisys.is.model.store.ISTopInventory;
import com.cassinisys.is.model.store.QISTopInventory;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.mysema.query.types.ExpressionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by swapna on 25/07/18.
 */

@Component
public class TopInventoryPredicateBuilder {

    @Autowired
    private MaterialItemRepository materialItemRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    public TypedQuery<ISTopInventory> getItemTypeQuery(TopInventoryCriteria criteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ISTopInventory> cq = builder.createQuery(ISTopInventory.class);
        Root<ISTopInventory> from = cq.from(ISTopInventory.class);
        cq.select(from);
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getStoreId() != null) {
            Predicate p1 = builder.equal((from.get("store")), criteria.getStoreId());
            predicates.add(p1);
        }
        cq.where(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));
        TypedQuery<ISTopInventory> typedQuery1 = entityManager.createQuery(cq);
        return typedQuery1;
    }

    public com.mysema.query.types.Predicate getMaterialItemPredicate(TopInventoryCriteria criteria, QISMaterialItem pathBase) {
        List<com.mysema.query.types.Predicate> materialPredicates = new ArrayList<>();
        if (criteria.getItemName() != null && !criteria.getItemName().equals("null")) {
            materialPredicates.add(pathBase.itemName.containsIgnoreCase(criteria.getItemName()));
        }
        if (criteria.getItemNumber() != null && !criteria.getItemNumber().equals("null")) {
            materialPredicates.add(pathBase.itemNumber.containsIgnoreCase(criteria.getItemNumber()));
        }
        if (criteria.getItemDescription() != null && !criteria.getItemDescription().equals("null")) {
            materialPredicates.add(pathBase.description.containsIgnoreCase(criteria.getItemDescription()));
        }
        if (criteria.getItemType() > 0 && criteria.getItemType() != null && !criteria.getItemType().equals("null")) {
            materialPredicates.add(pathBase.itemType.id.eq((criteria.getItemType())));
        }
        if (criteria.getUnits() != null && !criteria.getUnits().equals("null")) {
            materialPredicates.add(pathBase.units.containsIgnoreCase(criteria.getUnits()));
        }
        if (criteria.getFreeTextSearch()) {
            materialPredicates.add(getMaterialFreeTextSearchPredicate(criteria, QISMaterialItem.iSMaterialItem));
        }
        if (criteria.getSearchAttributes().size() > 0) {
            List<ISMaterialItem> materialItems = materialItemRepository.findAll();
            List<com.mysema.query.types.Predicate> predicates2 = new ArrayList();
            if (materialItems.size() > 0) {
                for (AttributeSearchDto attributeSearchDto : criteria.getSearchAttributes()) {
                    List<com.mysema.query.types.Predicate> predicates1 = new ArrayList();
                    for (ISMaterialItem materialItem : materialItems) {
                        if (attributeSearchDto.getText() != null && !Criteria.isEmpty(attributeSearchDto.getText())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(materialItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getText());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getLongText() != null && !Criteria.isEmpty(attributeSearchDto.getLongText())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(materialItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getLongText());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getInteger() != null && !Criteria.isEmpty(attributeSearchDto.getInteger())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(materialItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getInteger());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getList() != null && !Criteria.isEmpty(attributeSearchDto.getList())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(materialItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getList());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getaBoolean() != null) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndBooleanValue(materialItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), Boolean.parseBoolean(attributeSearchDto.getaBoolean()));
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getaDouble() != null && attributeSearchDto.getaDouble() != 0.0) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndDoubleValue(materialItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(),
                                    attributeSearchDto.getaDouble());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getCurrency() != null && !Criteria.isEmpty(attributeSearchDto.getCurrency())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(materialItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getCurrency());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getDate() != null && !Criteria.isEmpty(attributeSearchDto.getDate())) {
                            try {
                                List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(materialItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getDate());
                                if (integers.size() > 0) {
                                    predicates1.add(pathBase.id.in(integers));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        if (attributeSearchDto.getTime() != null && !Criteria.isEmpty(attributeSearchDto.getTime())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(materialItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getTime());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                    }
                    if (predicates1.size() > 0)
                        predicates2.add(ExpressionUtils.anyOf(predicates1));
                }
                if (predicates2.size() > 0) {
                    materialPredicates.add(ExpressionUtils.allOf(predicates2));
                } else {
                    predicates2.add(pathBase.id.in(0));
                    materialPredicates.add(ExpressionUtils.allOf(predicates2));
                }

            }
        }
        Iterable<ISMaterialItem> materialItems = materialItemRepository.findAll(ExpressionUtils.allOf(materialPredicates));
        List<ISMaterialItem> materialItems1 = new ArrayList();
        materialItems.forEach(materialItems1::add);
        List<Integer> idList = materialItems1.stream().map(ISMaterialItem::getId).collect(Collectors.toList());
        List<com.mysema.query.types.Predicate> predicates2 = new ArrayList();
        predicates2.add(QISTopInventory.iSTopInventory.item.in(idList));
        predicates2.add(QISTopInventory.iSTopInventory.store.id.eq(criteria.getStoreId()));
        if (criteria.getProjectId() != null) {
            List<Integer> projectIds = getProjectPredicate(criteria, QISProject.iSProject);
            predicates2.add(QISTopInventory.iSTopInventory.project.in(projectIds));
        }
        return ExpressionUtils.allOf(predicates2);
    }

    private List<Integer> getProjectPredicate(TopInventoryCriteria criteria, QISProject pathBase) {
        com.mysema.query.types.Predicate predicate = pathBase.id.eq(criteria.getProjectId());
        Iterable<ISProject> projects = projectRepository.findAll(predicate);
        List<ISProject> projects1 = new ArrayList();
        projects.forEach(projects1::add);
        List<Integer> idList = projects1.stream().map(ISProject::getId).collect(Collectors.toList());
        return idList;
    }

    private com.mysema.query.types.Predicate getMaterialFreeTextSearchPredicate(TopInventoryCriteria criteria, QISMaterialItem pathBase) {
        List<com.mysema.query.types.Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                com.mysema.query.types.Predicate predicate = pathBase.itemType.name.containsIgnoreCase(s).
                        or(pathBase.itemNumber.containsIgnoreCase(s)).
                        or(pathBase.itemName.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.units.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }
}
