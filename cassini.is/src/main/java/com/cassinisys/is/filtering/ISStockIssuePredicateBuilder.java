package com.cassinisys.is.filtering;

import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.pm.QISProject;
import com.cassinisys.is.model.store.AttributeSearchDto;
import com.cassinisys.is.model.store.ISStockIssue;
import com.cassinisys.is.model.store.QISStockIssue;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.store.ISStockIssueRepository;
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
import java.util.stream.Collectors;

/**
 * Created by swapna on 31/12/18.
 */
@Component
public class ISStockIssuePredicateBuilder implements PredicateBuilder<ISStockIssueCriteria, QISStockIssue> {

    @Autowired
    private ISStockIssueRepository issueRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Predicate build(ISStockIssueCriteria criteria, QISStockIssue pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else if (criteria.getSearchAttributes().size() > 0 || criteria.getAttributeSearch()) {
            return getAttributeSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getAttributeSearchPredicate(ISStockIssueCriteria criteria, QISStockIssue pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        List<ISStockIssue> isStockIssues = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getStoreId())) {
            predicates.add(pathBase.store.eq(criteria.getStoreId()));
        }
        if (!Criteria.isEmpty(criteria.getIssueNumberSource())) {
            predicates.add(pathBase.issueNumberSource.containsIgnoreCase(criteria.getIssueNumberSource()));
        }
        if (!Criteria.isEmpty(criteria.getMaterialIssueType())) {
            predicates.add(pathBase.materialIssueType.id.eq(criteria.getMaterialIssueType()));
        }
        if (!Criteria.isEmpty(criteria.getProject())) {
            List<Integer> projectIds = getProjectPredicate(criteria, QISProject.iSProject);
            predicates.add(QISStockIssue.iSStockIssue.project.in(projectIds));
        }
        if (!Criteria.isEmpty(criteria.getNotes())) {
            predicates.add(pathBase.notes.containsIgnoreCase(criteria.getNotes()));
        }
        if (criteria.getSearchAttributes().size() > 0) {
            if (criteria.getStoreId() != null) {
                isStockIssues = issueRepository.findByStore(criteria.getStoreId());
            } else {
                isStockIssues = issueRepository.findAll();
            }
            if (isStockIssues.size() > 0) {
                for (AttributeSearchDto attributeSearchDto : criteria.getSearchAttributes()) {
                    List<Predicate> predicates1 = new ArrayList();
                    for (ISStockIssue isStockIssue : isStockIssues) {
                        if (attributeSearchDto.getText() != null && !Criteria.isEmpty(attributeSearchDto.getText())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getText());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getLongText() != null && !Criteria.isEmpty(attributeSearchDto.getLongText())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getLongText());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getInteger() != null && !Criteria.isEmpty(attributeSearchDto.getInteger())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getInteger());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getList() != null && !attributeSearchDto.getObjectTypeAttribute().isListMultiple() && !Criteria.isEmpty(attributeSearchDto.getList())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getList());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getaBoolean() != null && attributeSearchDto.getBooleanSearch()) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndBooleanValue(isStockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), Boolean.parseBoolean(attributeSearchDto.getaBoolean()));
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getaDouble() != null && attributeSearchDto.getaDouble() != 0.0) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndDoubleValue(isStockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(),
                                    attributeSearchDto.getaDouble());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getCurrency() != null && !Criteria.isEmpty(attributeSearchDto.getCurrency())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getCurrency());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                        if (attributeSearchDto.getDate() != null && !Criteria.isEmpty(attributeSearchDto.getDate())) {
                            try {
                                Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(attributeSearchDto.getDate());
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                String parsedDate = formatter.format(initDate);
                                List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), parsedDate);
                                if (integers.size() > 0) {
                                    predicates1.add(pathBase.id.in(integers));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        if (attributeSearchDto.getTime() != null && !Criteria.isEmpty(attributeSearchDto.getTime())) {
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockIssue.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getTime());
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        }
                    }
                    if (predicates1.size() > 0) {
                        predicates.add(ExpressionUtils.anyOf(predicates1));
                    }
                }
                if (predicates.size() > 0) {
                    predicates.add(ExpressionUtils.allOf(predicates));
                } else {
                    predicates.add(pathBase.id.in(0));
                    predicates.add(ExpressionUtils.allOf(predicates));
                }
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */

    private Predicate getFreeTextSearchPredicate(ISStockIssueCriteria criteria, QISStockIssue pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.issueNumberSource.containsIgnoreCase(s).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.notes.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    private List<Integer> getProjectPredicate(ISStockIssueCriteria criteria, QISProject pathBase) {
        Predicate predicate = pathBase.name.containsIgnoreCase(criteria.getProject());
        Iterable<ISProject> projects = projectRepository.findAll(predicate);
        List<ISProject> projects1 = new ArrayList();
        projects.forEach(projects1::add);
        List<Integer> idList = projects1.stream().map(ISProject::getId).collect(Collectors.toList());
        return idList;
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */

    private Predicate getDefaultPredicate(ISStockIssueCriteria criteria, QISStockIssue pathBase) {
        return null;
    }
}
