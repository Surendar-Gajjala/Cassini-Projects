package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.PLMECRPR;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.QPLMItem;
import com.cassinisys.plm.model.pqm.PQMQCRAggregatePR;
import com.cassinisys.plm.model.pqm.QPQMProblemReport;
import com.cassinisys.plm.repo.cm.ECRPRRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.pqm.QCRAggregatePRRepository;
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

@Component
public class ProblemReportPredicateBuilder implements PredicateBuilder<ProblemReportCriteria, QPQMProblemReport> {

    @Autowired
    private ItemPredicateBuilder predicateBuilder;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private QCRAggregatePRRepository qcrAggregatePRRepository;
    @Autowired
    private ECRPRRepository ecrprRepository;

    @Override
    public Predicate build(ProblemReportCriteria criteria, QPQMProblemReport pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(ProblemReportCriteria criteria, QPQMProblemReport pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {

            List<Integer> itemIds = getItems(criteria);

            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                if (itemIds.size() > 0) {
                    Predicate predicate = pathBase.prNumber.containsIgnoreCase(s).
                            or(pathBase.prType.name.containsIgnoreCase(s)).
                            or(pathBase.problem.containsIgnoreCase(s)).
                            or(pathBase.description.containsIgnoreCase(s)).
                            or(pathBase.failureType.containsIgnoreCase(s)).
                            or(pathBase.severity.containsIgnoreCase(s)).
                            or(pathBase.disposition.containsIgnoreCase(s))
                            .or(pathBase.product.in(itemIds));
                    predicates.add(predicate);
                } else {
                    Predicate predicate = pathBase.prNumber.containsIgnoreCase(s).
                            or(pathBase.prType.name.containsIgnoreCase(s)).
                            or(pathBase.problem.containsIgnoreCase(s)).
                            or(pathBase.description.containsIgnoreCase(s)).
                            or(pathBase.failureType.containsIgnoreCase(s)).
                            or(pathBase.severity.containsIgnoreCase(s)).
                            or(pathBase.disposition.containsIgnoreCase(s));
                    predicates.add(predicate);
                }
            }
        }

        if (!Criteria.isEmpty(criteria.getPrNumber())) {
            predicates.add(pathBase.prNumber.containsIgnoreCase(criteria.getPrNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getProblem())) {
            predicates.add(pathBase.problem.containsIgnoreCase(criteria.getProblem()));
        }
        if (!Criteria.isEmpty(criteria.getPrType())) {
            predicates.add(pathBase.prType.id.eq(criteria.getPrType()));
        }

        if (!Criteria.isEmpty(criteria.getProduct())) {
            ItemCriteria itemCriteria = new ItemCriteria();
            itemCriteria.setItemName(criteria.getProduct());
            Pageable pageable = new PageRequest(0, 1000,
                    new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
            Predicate predicate = predicateBuilder.build(itemCriteria, QPLMItem.pLMItem);
            Page<PLMItem> plmItems = itemRepository.findAll(predicate, pageable);

            List<Integer> itemIds = new ArrayList<>();

            for (PLMItem item : plmItems.getContent()) {
                itemIds.add(item.getLatestRevision());
            }

            predicates.add(pathBase.product.in(itemIds));
        }

        if (!Criteria.isEmpty(criteria.getQcr())) {
            List<PQMQCRAggregatePR> aggregatePRList = qcrAggregatePRRepository.findByQcr(criteria.getQcr());
            for (PQMQCRAggregatePR pqmqcrAggregatePR : aggregatePRList) {
                predicates.add(pathBase.id.ne(pqmqcrAggregatePR.getPr().getId()));
            }
            List<PLMECRPR> ecrPrs = ecrprRepository.findAll();
            for (PLMECRPR plmecrpr : ecrPrs) {
                predicates.add(pathBase.id.ne(plmecrpr.getProblemReport()));
            }
        }

        if (!Criteria.isEmpty(criteria.getEcr())) {
            List<PLMECRPR> ecrPrs = ecrprRepository.findAll();
            for (PLMECRPR plmecrpr : ecrPrs) {
                predicates.add(pathBase.id.ne(plmecrpr.getProblemReport()));
            }
            List<PQMQCRAggregatePR> aggregatePRList = qcrAggregatePRRepository.findAll();
            for (PQMQCRAggregatePR pqmqcrAggregatePR : aggregatePRList) {
                predicates.add(pathBase.id.ne(pqmqcrAggregatePR.getPr().getId()));
            }
        }

        if (criteria.getReleased()) {
            predicates.add(pathBase.released.eq(true).and(pathBase.isImplemented.eq(false)));
        }

        return ExpressionUtils.allOf(predicates);
    }

    private List<Integer> getItems(ProblemReportCriteria criteria) {
        List<Integer> itemRevisionIds = new ArrayList<>();
        ItemCriteria itemCriteria = new ItemCriteria();
        itemCriteria.setItemName(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = predicateBuilder.build(itemCriteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItems = itemRepository.findAll(predicate, pageable);

        for (PLMItem item : plmItems.getContent()) {
            itemRevisionIds.add(item.getLatestRevision());
        }

        return itemRevisionIds;
    }
}
