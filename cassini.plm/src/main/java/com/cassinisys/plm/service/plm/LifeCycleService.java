package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.plm.filtering.LifeCycleCriteria;
import com.cassinisys.plm.filtering.LifeCyclePredicateBuilder;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartType;
import com.cassinisys.plm.model.mfr.PLMManufacturerType;
import com.cassinisys.plm.model.mfr.PLMSupplierType;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.QPLMLifeCycle;
import com.cassinisys.plm.repo.mes.MESMBOMRepository;
import com.cassinisys.plm.repo.mes.ProductionOrderRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartTypeRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerTypeRepository;
import com.cassinisys.plm.repo.mfr.SupplierTypeRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.pqm.InspectionPlanRepository;
import com.cassinisys.plm.repo.pqm.MaterialInspectionPlanRepository;
import com.cassinisys.plm.repo.pqm.ProductInspectionPlanRepository;
import com.cassinisys.plm.repo.rm.GlossaryRepository;
import com.cassinisys.plm.repo.rm.SpecificationRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 018 18-May -17.
 */
@Service
public class LifeCycleService implements CrudService<PLMLifeCycle, Integer> {

    @Autowired
    private LifeCycleRepository lifeCycleRepository;

    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;

    @Autowired
    private LifeCyclePredicateBuilder lifeCyclePredicateBuilder;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ManufacturerTypeRepository manufacturerTypeRepository;
    @Autowired
    private SupplierTypeRepository supplierTypeRepository;

    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InspectionPlanRepository inspectionPlanRepository;
    @Autowired
    private ProductInspectionPlanRepository productInspectionPlanRepository;
    @Autowired
    private MaterialInspectionPlanRepository materialInspectionPlanRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private SpecificationRepository specificationRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private GlossaryRepository glossaryRepository;
    @Autowired
    private ProductionOrderRepository productionOrderRepository;
    @Autowired
    private MESMBOMRepository mesmbomRepository;

    @Override
    @Transactional
    public PLMLifeCycle create(PLMLifeCycle plmLifeCycle) {
        checkNotNull(plmLifeCycle);
        return lifeCycleRepository.save(plmLifeCycle);
    }

    @Override
    @Transactional
    public PLMLifeCycle update(PLMLifeCycle plmLifeCycle) {
        checkNotNull(plmLifeCycle);
        checkNotNull(plmLifeCycle.getId());
        return lifeCycleRepository.save(plmLifeCycle);
    }

    @Override
    @Transactional
    public void delete(Integer integer) {
        checkNotNull(integer);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(integer);
        List<PLMManufacturerType> manufacturerTypes = manufacturerTypeRepository.findByLifecycle(lifeCycle);
        List<PLMManufacturerPartType> manufacturerPartTypes = manufacturerPartTypeRepository.findByLifecycle(lifeCycle);
        List<PLMSupplierType> supplierTypes = supplierTypeRepository.findByLifecycle(lifeCycle);
        Integer items = itemRepository.getItemByLifeCycle(lifeCycle.getId());
        Integer inspectionPlans = productInspectionPlanRepository.getPlanByLifeCycle(lifeCycle.getId());
        Integer materialInspectionPlans = materialInspectionPlanRepository.getPlanByLifeCycle(lifeCycle.getId());
        Integer specifications = specificationRepository.getSpecByLifeCycle(lifeCycle.getId());
        Integer workflowDefinitions = workflowDefinitionRepository.getWorkflowsByLifeCycle(lifeCycle.getId());
        Integer glossaries = glossaryRepository.getGlossariesByLifeCycle(lifeCycle.getId());
        Integer productionOrder = productionOrderRepository.getProductionOrderByLifeCycle(lifeCycle.getId());
        Integer mbom = mesmbomRepository.getMBOMsOrderByLifeCycle(lifeCycle.getId());
        if (specifications > 0 || inspectionPlans > 0 || materialInspectionPlans > 0 || items > 0 || manufacturerTypes.size() > 0
                || manufacturerPartTypes.size() > 0 || supplierTypes.size() > 0 || workflowDefinitions > 0 || glossaries > 0
                || productionOrder > 0 || mbom > 0) {
            String message = messageSource.getMessage("lifecycle_already_in_use", null, "{0} lifecycle already in use", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", lifeCycle.getName());
            throw new CassiniException(result);
        } else {
            lifeCycleRepository.delete(integer);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PLMLifeCycle get(Integer id) {
        checkNotNull(id);
        PLMLifeCycle plmLifeCycle = lifeCycleRepository.findOne(id);
        if (plmLifeCycle == null) {
            throw new ResourceNotFoundException();
        }
        return plmLifeCycle;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PLMLifeCycle> getAll() {
        List<PLMLifeCycle> lifeCycles = lifeCycleRepository.findAllByOrderByIdDesc();
        for (PLMLifeCycle lifeCycle : lifeCycles) {
            List<PLMManufacturerType> manufacturerTypes = manufacturerTypeRepository.findByLifecycle(lifeCycle);
            List<PLMManufacturerPartType> manufacturerPartTypes = manufacturerPartTypeRepository.findByLifecycle(lifeCycle);
            List<PLMSupplierType> supplierTypes = supplierTypeRepository.findByLifecycle(lifeCycle);
            Integer items = itemRepository.getItemByLifeCycle(lifeCycle.getId());
            Integer inspectionPlans = productInspectionPlanRepository.getPlanByLifeCycle(lifeCycle.getId());
            Integer materialInspectionPlans = materialInspectionPlanRepository.getPlanByLifeCycle(lifeCycle.getId());
            Integer specifications = specificationRepository.getSpecByLifeCycle(lifeCycle.getId());
            Integer workflowDefinitions = workflowDefinitionRepository.getWorkflowsByLifeCycle(lifeCycle.getId());
            Integer glossaries = glossaryRepository.getGlossariesByLifeCycle(lifeCycle.getId());
            Integer productionOrder = productionOrderRepository.getProductionOrderByLifeCycle(lifeCycle.getId());
            Integer mbom = mesmbomRepository.getMBOMsOrderByLifeCycle(lifeCycle.getId());
            if (specifications > 0 || inspectionPlans > 0 || materialInspectionPlans > 0 || items > 0 || manufacturerTypes.size() > 0
                    || manufacturerPartTypes.size() > 0 || supplierTypes.size() > 0 || workflowDefinitions > 0 || glossaries > 0
                    || productionOrder > 0 || mbom > 0) {
                lifeCycle.setUsedLifeCycle(true);
            }
        }
        return lifeCycles;
    }

    @Transactional(readOnly = true)
    public Page<PLMLifeCycle> find(LifeCycleCriteria criteria, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("name")));
        }
        Predicate predicate = lifeCyclePredicateBuilder.build(criteria, QPLMLifeCycle.pLMLifeCycle);
        return lifeCycleRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public List<PLMLifeCycle> find(LifeCycleCriteria criteria) {
        Predicate predicate = lifeCyclePredicateBuilder.build(criteria, QPLMLifeCycle.pLMLifeCycle);
        List<PLMLifeCycle> list = new ArrayList<>();
        Iterable<PLMLifeCycle> iter = lifeCycleRepository.findAll(predicate);
        iter.iterator().forEachRemaining(list::add);
        return list;
    }

    @Transactional(readOnly = true)
    public PLMLifeCycle findLifecycleByName(String name) {
        return lifeCycleRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<PLMLifeCyclePhase> getPhases(Integer lcId) {
        return lifeCyclePhaseRepository.findByLifeCycle(lcId);
    }

    @Transactional(readOnly = true)
    public List<PLMLifeCyclePhase> getAllPhases() {
        return lifeCyclePhaseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PLMLifeCyclePhase> getAllLifeCyclePhases() {
        return lifeCyclePhaseRepository.getAllPhases();
    }

    @Transactional
    public PLMLifeCyclePhase createPhase(PLMLifeCyclePhase phase) {
        PLMLifeCyclePhase plmLifeCyclePhase = null;
        if (!phase.getPhaseType().equals(LifeCyclePhaseType.REVIEW)) {
            PLMLifeCyclePhase lifeCyclePhaseType = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(phase.getPhaseType(), phase.getLifeCycle());
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseAndLifeCycle(phase.getPhase(), phase.getLifeCycle());
            if (lifeCyclePhase == null && lifeCyclePhaseType == null) {
                plmLifeCyclePhase = lifeCyclePhaseRepository.save(phase);
            } else {
                String message = messageSource.getMessage("phase_type_already_exist", null, "{0} phase type already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", phase.getPhaseType());
                throw new CassiniException(result);
            }
        } else if (phase.getPhaseType().equals(LifeCyclePhaseType.REVIEW)) {
            PLMLifeCyclePhase cyclePhase = lifeCyclePhaseRepository.findByPhaseAndLifeCycleAndPhaseType(phase.getPhase(), phase.getLifeCycle(), phase.getPhaseType());
            if (cyclePhase == null) {
                plmLifeCyclePhase = lifeCyclePhaseRepository.save(phase);
            } else {
                String message = messageSource.getMessage("phase_name_already_exist_in_phase_type", null, "{0} phase name already exist {1} phaseType", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", phase.getPhase(), phase.getPhaseType());
                throw new CassiniException(result);
            }

        }
        return plmLifeCyclePhase;
    }

    @Transactional
    public PLMLifeCyclePhase updatePhase(PLMLifeCyclePhase phase) {
        return lifeCyclePhaseRepository.save(phase);
    }

    @Transactional(readOnly = true)
    public PLMLifeCyclePhase getPhase(Integer id) {
        return lifeCyclePhaseRepository.findOne(id);
    }

    @Transactional
    public void deletePhase(Integer id) {
        lifeCyclePhaseRepository.delete(id);
    }
}
