package com.cassinisys.is.filtering;

import com.cassinisys.is.model.procm.ISManpowerItem;
import com.cassinisys.is.model.procm.QISManpowerItem;
import com.cassinisys.is.model.store.AttributeSearchDto;
import com.cassinisys.is.repo.procm.ManpowerItemRepository;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ManpowerPredicateBuilder implements PredicateBuilder<ManpowerCriteria, QISManpowerItem> {

    @Autowired
    private ManpowerItemRepository manpowerItemRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    /**
     * The method used to build Predicate
     */

    @Override
    public Predicate build(ManpowerCriteria criteria, QISManpowerItem pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else if (criteria.getAttributeSearch()) {
            return getAttributeSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */

    private Predicate getFreeTextSearchPredicate(ManpowerCriteria criteria, QISManpowerItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.itemType.name.containsIgnoreCase(s).
                        or(pathBase.itemNumber.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.units.containsIgnoreCase(s)).
                        or(pathBase.person.firstName.containsIgnoreCase(s)).
                        or(pathBase.person.lastName.containsIgnoreCase(s)).
                        or(pathBase.person.phoneMobile.contains(s)).
                        or(pathBase.person.email.contains(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */

    private Predicate getDefaultPredicate(ManpowerCriteria criteria, QISManpowerItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getItemType())) {
            predicates.add(pathBase.itemType.name.equalsIgnoreCase(criteria.getItemType()));
        }
        if (!Criteria.isEmpty(criteria.getItemNumber())) {
            predicates.add(pathBase.itemNumber.equalsIgnoreCase(criteria.getItemNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.equalsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getUnits())) {
            predicates.add(pathBase.units.equalsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getFirstName())) {
            predicates.add(pathBase.person.firstName.containsIgnoreCase(criteria.getFirstName()));
        }
        if (!Criteria.isEmpty(criteria.getLastName())) {
            predicates.add(pathBase.person.lastName.containsIgnoreCase(criteria.getLastName()));
        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getAttributeSearchPredicate(ManpowerCriteria criteria, QISManpowerItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getAttributeSearch()) {
            List<ISManpowerItem> manpowerItems = manpowerItemRepository.findAll();
            List<Predicate> predicates1 = new ArrayList();
            if (manpowerItems.size() > 0) {
                for (ISManpowerItem manpowerItem : manpowerItems) {
                    for (AttributeSearchDto attributeSearchDto : criteria.getSearchAttributes()) {
                        if (attributeSearchDto.getText() != null && !Criteria.isEmpty(attributeSearchDto.getText())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(manpowerItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getText());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getLongText() != null && !Criteria.isEmpty(attributeSearchDto.getLongText())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(manpowerItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getLongText());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getInteger() != null && !Criteria.isEmpty(attributeSearchDto.getInteger())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(manpowerItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getInteger());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getList() != null && !attributeSearchDto.getObjectTypeAttribute().isListMultiple() && !Criteria.isEmpty(attributeSearchDto.getList())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(manpowerItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getList());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getaBoolean() != null && attributeSearchDto.getBooleanSearch()) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndBooleanValue(manpowerItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), Boolean.parseBoolean(attributeSearchDto.getaBoolean()));
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getaDouble() != null && attributeSearchDto.getaDouble() != 0.0) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndDoubleValue(manpowerItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(),
                                    attributeSearchDto.getaDouble());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getCurrency() != null && !Criteria.isEmpty(attributeSearchDto.getCurrency())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(manpowerItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getCurrency());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getDate() != null && !Criteria.isEmpty(attributeSearchDto.getDate())) {
                            try {
                                Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(attributeSearchDto.getDate());
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                String parsedDate = formatter.format(initDate);
                                List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(manpowerItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), parsedDate);
                                if (integers.size() > 0) {
                                    predicates1.add(pathBase.id.in(integers));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        if (attributeSearchDto.getTime() != null && !Criteria.isEmpty(attributeSearchDto.getTime())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(manpowerItem.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getTime());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                    }
                }
            }
            predicates.add(ExpressionUtils.anyOf(predicates1));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
