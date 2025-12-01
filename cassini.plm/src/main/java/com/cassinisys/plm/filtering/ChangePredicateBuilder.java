package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.cm.*;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChangePredicateBuilder implements PredicateBuilder<ECOCriteria, QPLMChange> {


    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private ECOPredicateBuilder predicateBuilder;
    @Autowired
    private DCOPredicateBuilder dcoPredicateBuilder;
    @Autowired
    private DCRPredicateBuilder dcrPredicateBuilder;
    @Autowired
    private ECRPredicateBuilder ecrPredicateBuilder;
    @Autowired
    private MCOPredicateBuilder mcoPredicateBuilder;
    @Autowired
    private DCRRepository dcrRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private VariancePredicateBuilder variancePredicateBuilder;
    @Autowired
    private VarianceRepository varianceRepository;

    @Override
    public Predicate build(ECOCriteria assemblyLineCriteria, QPLMChange pathBase) {
        return getDefaultPredicate(assemblyLineCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(ECOCriteria criteria, QPLMChange pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.changeReasonType.containsIgnoreCase(s);
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    public Predicate getDefaultPredicates(ECOCriteria criteria, QPLMChange pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        PLMChangeType type = null;
        if (criteria.getType() != null) {
            type = changeTypeRepository.findOne(criteria.getType());
        }
        if ((type != null && type.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ECOTYPE.toString()))) || (type == null && criteria.getType() == null)) {
            ECOCriteria ecoCriteria = new ECOCriteria();
            ecoCriteria.setSearchQuery(criteria.getSearchQuery());
            if (type != null && criteria.getType() != null) {
                ecoCriteria.setType(type.getId());
            }
            Predicate predicate = predicateBuilder.getDefaultPredicates(ecoCriteria, QPLMECO.pLMECO);
            Iterable<PLMECO> plmecos = ecoRepository.findAll(predicate);
            plmecos.forEach(plmeco -> {
                ids.add(plmeco.getId());
            });
        }
        if ((type != null && type.getObjectType().equals(ObjectType.valueOf(PLMObjectType.DCOTYPE.toString()))) || (type == null && criteria.getType() == null)) {
            DCOCriteria dcoCriteria = new DCOCriteria();
            dcoCriteria.setSearchQuery(criteria.getSearchQuery());
            if (type != null && criteria.getType() != null) {
                dcoCriteria.setType(type.getId());
            }
            Predicate predicate = dcoPredicateBuilder.build(dcoCriteria, QPLMDCO.pLMDCO);
            Iterable<PLMDCO> plmdcos = dcoRepository.findAll(predicate);
            plmdcos.forEach(plmdco -> {
                ids.add(plmdco.getId());
            });
        }
        if ((type != null && type.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ECRTYPE.toString()))) || (type == null && criteria.getType() == null)) {
            DCOCriteria dcoCriteria = new DCOCriteria();
            dcoCriteria.setSearchQuery(criteria.getSearchQuery());
            if (type != null && criteria.getType() != null) {
                dcoCriteria.setType(type.getId());
            }
            Predicate predicate = ecrPredicateBuilder.build(dcoCriteria, QPLMECR.pLMECR);
            Iterable<PLMECR> plmecrs = ecrRepository.findAll(predicate);
            plmecrs.forEach(plmecr -> {
                ids.add(plmecr.getId());
            });
        }
        if ((type != null && type.getObjectType().equals(ObjectType.valueOf(PLMObjectType.DCRTYPE.toString()))) || (type == null && criteria.getType() == null)) {
            DCRCriteria dcrCriteria = new DCRCriteria();
            dcrCriteria.setSearchQuery(criteria.getSearchQuery());
            if (type != null && criteria.getType() != null) {
                dcrCriteria.setType(type.getId());
            }
            Predicate predicate = dcrPredicateBuilder.build(dcrCriteria, QPLMDCR.pLMDCR);
            Iterable<PLMDCR> plmdcrs = dcrRepository.findAll(predicate);
            plmdcrs.forEach(plmdcr -> {
                ids.add(plmdcr.getId());
            });
        }
        if ((type != null && type.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MCOTYPE.toString()))) || (type == null && criteria.getType() == null)) {
            MCOCriteria mcoCriteria = new MCOCriteria();
            mcoCriteria.setSearchQuery(criteria.getSearchQuery());
            if (type != null && criteria.getType() != null) {
                mcoCriteria.setMcoType(type.getId());
            }
            Predicate predicate = mcoPredicateBuilder.build(mcoCriteria, QPLMMCO.pLMMCO);
            Iterable<PLMMCO> plmmcos = mcoRepository.findAll(predicate);
            plmmcos.forEach(plmmco -> {
                ids.add(plmmco.getId());
            });
        }
        if ((type != null && (type.getObjectType().equals(ObjectType.valueOf(PLMObjectType.DEVIATIONTYPE.toString())) || type.getObjectType().equals(ObjectType.valueOf(PLMObjectType.WAIVERTYPE.toString())))) || (type == null && criteria.getType() == null)) {
            VarianceCriteria varianceCriteria = new VarianceCriteria();
            varianceCriteria.setSearchQuery(criteria.getSearchQuery());
            if (type != null && criteria.getType() != null) {
                if (type.getObjectType().equals(ObjectType.valueOf(PLMObjectType.DEVIATIONTYPE.toString()))) {
                    varianceCriteria.setVarianceType(VarianceType.DEVIATION);
                } else if (type.getObjectType().equals(ObjectType.valueOf(PLMObjectType.WAIVERTYPE.toString()))) {
                    varianceCriteria.setVarianceType(VarianceType.WAIVER);
                }
            }
            Predicate predicate = variancePredicateBuilder.build(varianceCriteria, QPLMVariance.pLMVariance);
            Iterable<PLMVariance> variances = varianceRepository.findAll(predicate);
            variances.forEach(plmmco -> {
                ids.add(plmmco.getId());
            });
        }

        predicates.add(pathBase.id.in(ids));

        return ExpressionUtils.allOf(predicates);
    }
}