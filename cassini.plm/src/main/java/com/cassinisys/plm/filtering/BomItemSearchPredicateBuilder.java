package com.cassinisys.plm.filtering;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.PLMBom;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.QPLMBom;
import com.cassinisys.plm.repo.cm.AffectedItemRepository;
import com.cassinisys.plm.repo.plm.BomRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyam reddy on 19-09-2018.
 */
@Component
public class BomItemSearchPredicateBuilder implements PredicateBuilder<BomItemSearchCriteria, QPLMBom> {

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

    @Override
    public Predicate build(BomItemSearchCriteria criteria, QPLMBom pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(BomItemSearchCriteria criteria, QPLMBom pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicates1 = new ArrayList<>();
        List<Predicate> predicates2 = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            Predicate predicate = pathBase.item.itemNumber.containsIgnoreCase(criteria.getSearchQuery()).
                    or(pathBase.item.itemType.name.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.item.itemName.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.item.description.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.notes.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.refdes.containsIgnoreCase(criteria.getSearchQuery()));
            predicates.add(predicate);
        }

        if (!Criteria.isEmpty(criteria.getItem())) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(criteria.getItem());

            List<PLMBom> bomItemChildren = bomRepository.findByParent(itemRevision);
            if (bomItemChildren.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                for (PLMBom plmBom : bomItemChildren) {
                    ids.add(plmBom.getId());
                }
                predicates1.add(pathBase.id.in(ids));

                if (!Criteria.isEmpty(criteria.getFromDate()) && !Criteria.isEmpty(criteria.getToDate())) {
                    try {
                        Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getToDate());
                        Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));
                        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getFromDate());
                        Date fromDateMinus = new Date(fromDate.getTime() - (1000 * 60 * 60 * 24));
                        predicates2.add(pathBase.effectiveFrom.after(fromDateMinus).and(pathBase.effectiveTo.before(tomorrow)).or(pathBase.effectiveFrom.isNull().and(pathBase.effectiveTo.isNull())));
                    } catch (Exception e) {
                        throw new CassiniException(e.getMessage());
                    }
                } else if (!Criteria.isEmpty(criteria.getFromDate())) {
                    try {
                        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getFromDate());
                        Date fromDateMinus = new Date(fromDate.getTime() - (1000 * 60 * 60 * 24));

                        predicates2.add(pathBase.effectiveFrom.isNull().and(pathBase.effectiveTo.isNull()));
                        predicates2.add(pathBase.effectiveFrom.isNotNull().and(pathBase.effectiveFrom.after(fromDateMinus)));
                        predicates2.add(pathBase.effectiveFrom.isNull().and(pathBase.effectiveTo.after(fromDateMinus)));
                    } catch (Exception e) {
                        throw new CassiniException(e.getMessage());
                    }
                } else if (!Criteria.isEmpty(criteria.getToDate())) {
                    try {
                        Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getToDate());
                        Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));

                        predicates2.add(pathBase.effectiveFrom.isNull().and(pathBase.effectiveTo.isNull()));
                        predicates2.add(pathBase.effectiveTo.isNotNull().and(pathBase.effectiveTo.before(tomorrow)));
                        predicates2.add(pathBase.effectiveTo.isNull().and(pathBase.effectiveFrom.before(tomorrow)));
                    } catch (Exception e) {
                        throw new CassiniException(e.getMessage());
                    }
                }
            }

        }

        Predicate predicate = ExpressionUtils.and(ExpressionUtils.anyOf(predicates), ExpressionUtils.anyOf(predicates1));
        return ExpressionUtils.and(ExpressionUtils.anyOf(predicate), ExpressionUtils.anyOf(predicates2));
    }
}
