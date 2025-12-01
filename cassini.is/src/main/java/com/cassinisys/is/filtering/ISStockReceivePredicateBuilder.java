package com.cassinisys.is.filtering;

import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.pm.QISProject;
import com.cassinisys.is.model.procm.ISSupplier;
import com.cassinisys.is.model.procm.QISSupplier;
import com.cassinisys.is.model.store.AttributeSearchDto;
import com.cassinisys.is.model.store.ISStockReceive;
import com.cassinisys.is.model.store.QISStockReceive;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.procm.SupplierRepository;
import com.cassinisys.is.repo.store.ISStockReceiveRepository;
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
public class ISStockReceivePredicateBuilder implements PredicateBuilder<ISStockReceiveCriteria, QISStockReceive> {

    @Autowired
    private ISStockReceiveRepository receiveRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public Predicate build(ISStockReceiveCriteria criteria, QISStockReceive pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else if (criteria.getSearchAttributes().size() > 0 || criteria.getAttributeSearch()) {
            return getAttributeSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getAttributeSearchPredicate(ISStockReceiveCriteria criteria, QISStockReceive pathBase) {
        List<Predicate> predicates = new ArrayList();
        if (!Criteria.isEmpty(criteria.getStoreId())) {
            predicates.add(pathBase.store.eq(criteria.getStoreId()));
        }
        if (!Criteria.isEmpty(criteria.getProject())) {
            List<Integer> projectIds = getProjectPredicate(criteria, QISProject.iSProject);
            predicates.add(QISStockReceive.iSStockReceive.project.in(projectIds));
        }
        if (!Criteria.isEmpty(criteria.getSupplier())) {
            List<Integer> supplierIds = getSupplierPredicate(criteria, QISSupplier.iSSupplier);
            predicates.add(QISStockReceive.iSStockReceive.supplier.in(supplierIds));
        }
        if (!Criteria.isEmpty(criteria.getReceiveNumberSource())) {
            predicates.add(pathBase.receiveNumberSource.containsIgnoreCase(criteria.getReceiveNumberSource()));
        }
        if (!Criteria.isEmpty(criteria.getNotes())) {
            predicates.add(pathBase.notes.containsIgnoreCase(criteria.getNotes()));
        }
        if (!Criteria.isEmpty(criteria.getMaterialReceiveType())) {
            predicates.add(pathBase.materialReceiveType.id.eq(criteria.getMaterialReceiveType()));
        }
        List<ISStockReceive> isStockReceives = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getStoreId())) {
            isStockReceives = receiveRepository.findByStore(criteria.getStoreId());
        } else {
            isStockReceives = receiveRepository.findAll();
        }
        if (isStockReceives.size() > 0) {
            for (AttributeSearchDto attributeSearchDto : criteria.getSearchAttributes()) {
                List<com.mysema.query.types.Predicate> predicates1 = new ArrayList();
                for (ISStockReceive isStockReceive : isStockReceives) {
                    if (attributeSearchDto.getText() != null && !Criteria.isEmpty(attributeSearchDto.getText())) {
                        List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getText());
                        if (integers.size() > 0) {
                            predicates1.add(pathBase.id.in(integers));
                        }
                    }
                    if (attributeSearchDto.getLongText() != null && !Criteria.isEmpty(attributeSearchDto.getLongText())) {
                        List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getLongText());
                        if (integers.size() > 0) {
                            predicates1.add(pathBase.id.in(integers));
                        }
                    }
                    if (attributeSearchDto.getInteger() != null && !Criteria.isEmpty(attributeSearchDto.getInteger())) {
                        List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getInteger());
                        if (integers.size() > 0) {
                            predicates1.add(pathBase.id.in(integers));
                        }
                    }
                    if (attributeSearchDto.getList() != null && !attributeSearchDto.getObjectTypeAttribute().isListMultiple() && !Criteria.isEmpty(attributeSearchDto.getList())) {
                        List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getList());
                        if (integers.size() > 0) {
                            predicates1.add(pathBase.id.in(integers));
                        }
                    }
                    if (attributeSearchDto.getaBoolean() != null) {
                        List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndBooleanValue(isStockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), Boolean.parseBoolean(attributeSearchDto.getaBoolean()));
                        if (integers.size() > 0) {
                            predicates1.add(pathBase.id.in(integers));
                        }
                    }
                    if (attributeSearchDto.getaDouble() != null && attributeSearchDto.getaDouble() != 0.0) {
                        List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndDoubleValue(isStockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(),
                                attributeSearchDto.getaDouble());
                        if (integers.size() > 0) {
                            predicates1.add(pathBase.id.in(integers));
                        }
                    }
                    if (attributeSearchDto.getCurrency() != null && !Criteria.isEmpty(attributeSearchDto.getCurrency())) {
                        List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getCurrency());
                        if (integers.size() > 0) {
                            predicates1.add(pathBase.id.in(integers));
                        }
                    }
                    if (attributeSearchDto.getDate() != null && !Criteria.isEmpty(attributeSearchDto.getDate())) {
                        try {
                            Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(attributeSearchDto.getDate());
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String parsedDate = formatter.format(initDate);
                            List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), parsedDate);
                            if (integers.size() > 0) {
                                predicates1.add(pathBase.id.in(integers));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    if (attributeSearchDto.getTime() != null && !Criteria.isEmpty(attributeSearchDto.getTime())) {
                        List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(isStockReceive.getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getTime());
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
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */

    private Predicate getFreeTextSearchPredicate(ISStockReceiveCriteria criteria, QISStockReceive pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.receiveNumberSource.containsIgnoreCase(s).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.notes.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    private List<Integer> getProjectPredicate(ISStockReceiveCriteria criteria, QISProject pathBase) {
        Predicate predicate = pathBase.name.containsIgnoreCase(criteria.getProject());
        Iterable<ISProject> projects = projectRepository.findAll(predicate);
        List<ISProject> projects1 = new ArrayList();
        projects.forEach(projects1::add);
        List<Integer> idList = projects1.stream().map(ISProject::getId).collect(Collectors.toList());
        return idList;
    }

    private List<Integer> getSupplierPredicate(ISStockReceiveCriteria criteria, QISSupplier pathBase) {
        Predicate predicate = pathBase.name.containsIgnoreCase(criteria.getSupplier());
        Iterable<ISSupplier> suppliers = supplierRepository.findAll(predicate);
        List<ISSupplier> suppliers1 = new ArrayList();
        suppliers.forEach(suppliers1::add);
        List<Integer> idList = suppliers1.stream().map(ISSupplier::getId).collect(Collectors.toList());
        return idList;
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */

    private Predicate getDefaultPredicate(ISStockReceiveCriteria criteria, QISStockReceive pathBase) {
        return null;
    }
}
