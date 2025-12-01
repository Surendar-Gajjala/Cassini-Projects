package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.ChangeType;
import com.cassinisys.plm.model.cm.PLMChange;
import com.cassinisys.plm.model.cm.PLMItemMCO;
import com.cassinisys.plm.model.cm.PLMManufacturerMCO;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.repo.cm.ChangeRepository;
import com.cassinisys.plm.repo.cm.ItemMCORepository;
import com.cassinisys.plm.repo.cm.ManufacturerMCORepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
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
 * Created by GSR.
 */
@Component
public class WorkflowPredicateBuilder implements PredicateBuilder<WorkflowCriteria, QPLMWorkflow> {

    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;

    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;

    @Autowired
    private PLMWorkflowDefinitionRepository plmWorkflowDefinitionRepository;
    @Autowired
    private WorkflowDefinitionPredicateBuilder workflowDefinitionPredicateBuilder;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private ItemMCORepository itemMCORepository;
    @Autowired
    private ManufacturerMCORepository manufacturerMCORepository;

    @Override
    public Predicate build(WorkflowCriteria criteria, QPLMWorkflow pathBase) {
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
            return predicate;
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(WorkflowCriteria criteria, QPLMWorkflow pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            List<Integer> integers = getDefIds(criteria);
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).or(pathBase.workflowRevision.in(integers));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(WorkflowCriteria criteria, QPLMWorkflow pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getType())) {
            List<PLMWorkflow> workflow = plmWorkflowRepository.findAll();
            List<Integer> ids = new ArrayList<>();
            for (PLMWorkflow workflow1 : workflow) {
                PLMWorkflowDefinition workflowDefinition = plmWorkflowDefinitionRepository.findOne(workflow1.getWorkflowRevision());
                PLMWorkflowType workflowType = workflowTypeRepository.findOne(workflowDefinition.getWorkflowType().getId());
                if (workflowType.getAssignable().equals(criteria.getType())) {
                    ids.add(workflow1.getId());
                }
                if (criteria.getType().equals("PROJECTS")) {
                    if (workflowType.getAssignable().equals("PROJECT ACTIVITIES")) {
                        ids.add(workflow1.getId());
                    }
                }
            }
            predicates.add(pathBase.id.in(ids));
        }
        return ExpressionUtils.allOf(predicates);
    }


    private List<Integer> getDefIds(WorkflowCriteria criteria) {
        List<Integer> defIds = new ArrayList<>();
        WorkflowDefinitionCriteria workflowDefinitionCriteria = new WorkflowDefinitionCriteria();
        workflowDefinitionCriteria.setNumber(criteria.getSearchQuery());
        workflowDefinitionCriteria.setSearchQuery(null);
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = workflowDefinitionPredicateBuilder.build(workflowDefinitionCriteria, QPLMWorkflowDefinition.pLMWorkflowDefinition);
        Page<PLMWorkflowDefinition> workflowDefinitions = plmWorkflowDefinitionRepository.findAll(predicate, pageable);

        for (PLMWorkflowDefinition item : workflowDefinitions.getContent()) {
            defIds.add(item.getId());
        }

        return defIds;
    }


    public Predicate getWfObjectSearchPredicate(WorkflowCriteria criteria, QPLMWorkflow pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        getFreeTextSearchPredicate(criteria, pathBase);
        if (!Criteria.isEmpty(criteria.getObjectType())) {
            List<Integer> ids = new ArrayList<>();
            ChangeType changeType = null;
            if (criteria.getObjectType().equals(PLMObjectType.ECO) || criteria.getObjectType().equals(PLMObjectType.DCO)
                    || criteria.getObjectType().equals(PLMObjectType.DCR) || criteria.getObjectType().equals(PLMObjectType.ECR)
                    || criteria.getObjectType().equals(PLMObjectType.DEVIATION) || criteria.getObjectType().equals(PLMObjectType.WAIVER)) {
                changeType = getChangeType(criteria);
                List<PLMChange> changes = changeRepository.findByChangeType(changeType);
                for (PLMChange plmeco : changes) {
                    ids.add(plmeco.getId());
                }
                predicates.add(pathBase.attachedTo.in(ids));
            } else if (criteria.getObjectType().equals(PLMObjectType.ITEMMCO)) {
                List<PLMItemMCO> itemMCOs = itemMCORepository.findAll();
                for (PLMItemMCO itemMCO : itemMCOs) {
                    ids.add(itemMCO.getId());
                }
                predicates.add(pathBase.attachedTo.in(ids));
            } else if (criteria.getObjectType().equals(PLMObjectType.OEMPARTMCO)) {
                List<PLMManufacturerMCO> manufacturerMCOs = manufacturerMCORepository.findAll();
                for (PLMManufacturerMCO manufacturerMCO : manufacturerMCOs) {
                    ids.add(manufacturerMCO.getId());
                }
                predicates.add(pathBase.attachedTo.in(ids));
            } else {
                predicates.add(pathBase.attachedToType.eq(criteria.getObjectType()));
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    private ChangeType getChangeType(WorkflowCriteria criteria) {
        ChangeType changeType = null;
        if (criteria.getObjectType().equals(PLMObjectType.ECO)) {
            changeType = ChangeType.ECO;
        } else if (criteria.getObjectType().equals(PLMObjectType.DCO)) {
            changeType = ChangeType.DCO;
        } else if (criteria.getObjectType().equals(PLMObjectType.DCR)) {
            changeType = ChangeType.DCR;
        } else if (criteria.getObjectType().equals(PLMObjectType.ECR)) {
            changeType = ChangeType.ECR;
        } else if (criteria.getObjectType().equals(PLMObjectType.DEVIATION)) {
            changeType = ChangeType.DEVIATION;
        } else if (criteria.getObjectType().equals(PLMObjectType.WAIVER)) {
            changeType = ChangeType.WAIVER;
        }
        return changeType;
    }
}
