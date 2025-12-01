package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectBom;
import com.cassinisys.platform.model.custom.CustomObjectRelated;
import com.cassinisys.platform.model.custom.QCustomObject;
import com.cassinisys.platform.repo.custom.CustomObjectBomRepository;
import com.cassinisys.platform.repo.custom.CustomObjectRelatedRepository;
import com.cassinisys.platform.repo.custom.CustomObjectRepository;
import com.cassinisys.platform.service.custom.CustomObjectTypeService;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomObjectPredicateBuilder implements PredicateBuilder<CustomObjectCriteria, QCustomObject> {

    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private CustomObjectBomRepository customObjectBomRepository;
    @Autowired
    private CustomObjectRelatedRepository customObjectRelatedRepository;
    @Autowired
    private CustomObjectTypeService customObjectTypeService;

    @Override
    public Predicate build(CustomObjectCriteria customObjectCriteria, QCustomObject pathBase) {
        return getDefaultPredicate(customObjectCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(CustomObjectCriteria criteria, QCustomObject pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.type.name.containsIgnoreCase(s).
                        or(pathBase.number.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.type.id.eq(criteria.getType()));
        }
        if (!Criteria.isEmpty(criteria.getCustomType())) {
            List<Integer> types = new ArrayList<>();
            types.add(criteria.getCustomType());
            types.addAll(customObjectTypeService.getIdsByType(criteria.getCustomType()));
            predicates.add(pathBase.type.id.in(types));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getSupplier())) {
            predicates.add(pathBase.supplier.eq(criteria.getSupplier()));
        }

        if (!Criteria.isEmpty(criteria.getObject())) {
            CustomObject customObject = customObjectRepository.findOne(criteria.getObject());

            predicates.add(pathBase.id.ne(customObject.getId()));
            if (!criteria.getRelated()) {
                if (Criteria.isEmpty(criteria.getBomObject())) {
                    List<CustomObjectBom> bomItemChildren = customObjectBomRepository.findByParentOrderBySequenceAsc(customObject.getId());
                    if (bomItemChildren.size() > 0) {
                        for (CustomObjectBom plmBom : bomItemChildren) {
                            predicates.add(pathBase.id.ne(plmBom.getChild().getId()));
                        }
                    }
                } else {
                    CustomObjectBom customObjectBom = customObjectBomRepository.findOne(criteria.bomObject);
                    if (customObjectBom != null) {
                        predicates.add(pathBase.id.ne(customObjectBom.getChild().getId()));
                        List<CustomObjectBom> bomItemChildren = customObjectBomRepository.findByParentOrderBySequenceAsc(customObjectBom.getChild().getId());
                        if (bomItemChildren.size() > 0) {
                            for (CustomObjectBom plmBom : bomItemChildren) {
                                predicates.add(pathBase.id.ne(plmBom.getChild().getId()));
                            }
                        }
                        List<CustomObjectBom> parentBoms = customObjectBomRepository.findByChild(customObjectBom.getChild());
                        if (parentBoms.size() > 0) {
                            for (CustomObjectBom plmBom : parentBoms) {
                                predicates.add(pathBase.id.ne(plmBom.getParent().getId()));
                                predicates = getParentObjects(plmBom, predicates, pathBase);
                            }
                        }
                    }

                }
                List<CustomObjectBom> parentBoms = customObjectBomRepository.findByChild(customObject);
                if (parentBoms.size() > 0) {
                    for (CustomObjectBom plmBom : parentBoms) {
                        predicates.add(pathBase.id.ne(plmBom.getParent().getId()));
                        predicates = getParentObjects(plmBom, predicates, pathBase);
                    }
                }
            } else {
                List<CustomObjectRelated> customObjectRelatedList = customObjectRelatedRepository.findByParentOrderBySequenceAsc(customObject.getId());
                if (customObjectRelatedList.size() > 0) {
                    for (CustomObjectRelated customObjectRelated : customObjectRelatedList) {
                        predicates.add(pathBase.id.ne(customObjectRelated.getRelated().getId()));
                    }
                }
            }
        }

        return ExpressionUtils.allOf(predicates);
    }


    private List<Predicate> getParentObjects(CustomObjectBom customObjectBom, List<Predicate> predicates, QCustomObject pathBase) {
        CustomObject item = customObjectRepository.findOne(customObjectBom.getParent().getId());
        List<CustomObjectBom> parentBoms = customObjectBomRepository.findByChild(item);
        if (parentBoms.size() > 0) {
            for (CustomObjectBom plmBom1 : parentBoms) {
                predicates.add(pathBase.id.ne(plmBom1.getParent().getId()));
                predicates = getParentObjects(plmBom1, predicates, pathBase);
            }
        }
        return predicates;
    }

}