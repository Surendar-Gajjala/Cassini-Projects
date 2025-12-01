package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mfr.ManufacturerPartStatus;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.QPLMManufacturer;
import com.cassinisys.plm.model.mfr.QPLMManufacturerPart;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
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
 * Created by Home on 5/3/2016.
 */
@Component
public class ManufacturerPartPredicateBuilder implements PredicateBuilder<ManufacturerPartCriteria, QPLMManufacturerPart> {

    @Autowired
    private ManufacturerPredicateBuilder manufacturerPredicateBuilder;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Override
    public Predicate build(ManufacturerPartCriteria criteria, QPLMManufacturerPart pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ManufacturerPartCriteria criteria, QPLMManufacturerPart pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            List<Integer> integers = getMfrIds(criteria);
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                if (integers.size() > 0) {
                    Predicate predicate = pathBase.partNumber.containsIgnoreCase(s).
                            or(pathBase.partName.containsIgnoreCase(s)).
                            or(pathBase.description.containsIgnoreCase(s)).
                            or(pathBase.mfrPartType.name.containsIgnoreCase(s)).or(pathBase.manufacturer.in(integers));
                    predicates.add(predicate);
                } else {
                    Predicate predicate = pathBase.partNumber.containsIgnoreCase(s).
                            or(pathBase.partName.containsIgnoreCase(s)).
                            or(pathBase.description.containsIgnoreCase(s)).
                            or(pathBase.mfrPartType.name.containsIgnoreCase(s));
                    predicates.add(predicate);
                }
            }
        }
        if (!Criteria.isEmpty(criteria.getManufacturer())) {
            predicates.add(pathBase.manufacturer.eq(criteria.getManufacturer()));
        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(ManufacturerPartCriteria criteria, QPLMManufacturerPart pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getPartNumber())) {
            predicates.add(pathBase.partNumber.containsIgnoreCase(criteria.getPartNumber()));
        }
        if (!Criteria.isEmpty(criteria.getPartName())) {
            predicates.add(pathBase.partName.containsIgnoreCase(criteria.getPartName()));
        }
        if (!Criteria.isEmpty(criteria.getManufacturer())) {
            predicates.add(pathBase.manufacturer.eq(criteria.getManufacturer()));
        }
        if (!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add(pathBase.status.eq(ManufacturerPartStatus.valueOf(criteria.getStatus())));

        }
        if (!Criteria.isEmpty(criteria.getMfrPartType())) {
            predicates.add(pathBase.mfrPartType.id.eq(Integer.parseInt(criteria.getMfrPartType())));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.mfrPartType.id.eq(criteria.getType()));
        }
      /*  if (!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add(pathBase.description.eq(Description.valueOf(criteria.getDescription())));

        }*/
        return ExpressionUtils.allOf(predicates);
    }

    private List<Integer> getMfrIds(ManufacturerPartCriteria criteria) {
        List<Integer> mfrIds = new ArrayList<>();
        ManufacturerCriteria manufacturerCriteria = new ManufacturerCriteria();
        manufacturerCriteria.setFreeTextSearch(true);
        manufacturerCriteria.setSearchQuery(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = manufacturerPredicateBuilder.getSearchPredicate(manufacturerCriteria, QPLMManufacturer.pLMManufacturer);
        Page<PLMManufacturer> manufacturers = manufacturerRepository.findAll(predicate, pageable);

        for (PLMManufacturer item : manufacturers.getContent()) {
            mfrIds.add(item.getId());
        }

        return mfrIds;
    }

    public Predicate getPredicates(ManufacturerPartCriteria criteria, QPLMManufacturerPart pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            Predicate predicate = pathBase.partNumber.containsIgnoreCase(criteria.getSearchQuery()).
                    or(pathBase.mfrPartType.name.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.partName.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.description.containsIgnoreCase(criteria.getSearchQuery()))
                    .and(pathBase.mfrPartType.id.eq(criteria.getType()));
            predicates.add(predicate);
        } else {
            predicates.add(pathBase.mfrPartType.id.eq(criteria.getType()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}