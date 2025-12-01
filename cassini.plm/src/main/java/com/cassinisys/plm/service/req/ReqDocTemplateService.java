package com.cassinisys.plm.service.req;

import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.plm.filtering.ReqDocTemplateCriteria;
import com.cassinisys.plm.filtering.ReqDocTemplatePredicateBuilder;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.req.*;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Lenovo on 25-06-2021.
 */
@Service
public class ReqDocTemplateService implements CrudService<PLMRequirementDocumentTemplate, Integer> {

    @Autowired
    private PLMRequirementDocumentTemplateRepository plmRequirementDocumentTemplateRepository;
    @Autowired
    private ReqDocTemplatePredicateBuilder reqDocTemplatePredicateBuilder;
    @Autowired
    private PLMRequirementTemplateRepository plmRequirementTemplateRepository;
    @Autowired
    private PLMRequirementDocumentTemplateReviewerRepository plmRequirementDocumentTemplateReviewerRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private PLMRequirementDocumentTypeRepository requirementDocumentTypeRepository;
    @Autowired
    private PLMRequirementTemplateReviewerRepository requirementTemplateReviewerRepository;
    @Autowired
    private PLMRequirementTypeRepository requirementTypeRepository;


    @Override
    public PLMRequirementDocumentTemplate create(PLMRequirementDocumentTemplate plmRequirementDocumentTemplate) {
        PLMRequirementDocumentType reqDocumentType = requirementDocumentTypeRepository.findOne(plmRequirementDocumentTemplate.getType().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(reqDocumentType.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        plmRequirementDocumentTemplate.setLifeCyclePhase(lifeCyclePhase);
        plmRequirementDocumentTemplate = plmRequirementDocumentTemplateRepository.save(plmRequirementDocumentTemplate);
        return plmRequirementDocumentTemplate;
    }

    @Override
    public PLMRequirementDocumentTemplate update(PLMRequirementDocumentTemplate plmRequirementDocumentTemplate) {
        plmRequirementDocumentTemplate = plmRequirementDocumentTemplateRepository.save(plmRequirementDocumentTemplate);
        return plmRequirementDocumentTemplate;
    }

    @Override
    public void delete(Integer id) {
        plmRequirementDocumentTemplateRepository.delete(id);
    }

    @Override
    public PLMRequirementDocumentTemplate get(Integer id) {
        PLMRequirementDocumentTemplate plmRequirementDocumentTemplate = plmRequirementDocumentTemplateRepository.findOne(id);
        return plmRequirementDocumentTemplate;
    }

    @Override
    public List<PLMRequirementDocumentTemplate> getAll() {
        return plmRequirementDocumentTemplateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<PLMRequirementDocumentTemplate> getAllDocumentTemplates(Pageable pageable, ReqDocTemplateCriteria criteria) {
        Predicate predicate = reqDocTemplatePredicateBuilder.build(criteria, QPLMRequirementDocumentTemplate.pLMRequirementDocumentTemplate);
        Page<PLMRequirementDocumentTemplate> documentTemplates = plmRequirementDocumentTemplateRepository.findAll(predicate, pageable);
        return documentTemplates;
    }


    // --------------------------- PLMRequirementTemplate -------------------------------------

    @Transactional
    public PLMRequirementTemplate createRequirementTemplate(PLMRequirementTemplate requirementTemplate) {
        PLMRequirementType type = requirementTypeRepository.findOne(requirementTemplate.getType().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(type.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        requirementTemplate.setLifeCyclePhase(lifeCyclePhase);
        requirementTemplate = createReqSequence(requirementTemplate);
        return plmRequirementTemplateRepository.save(requirementTemplate);
    }

    private PLMRequirementTemplate createReqSequence(PLMRequirementTemplate requirementTemplate) {
        if (requirementTemplate.getParent() != null) {
            List<PLMRequirementTemplate> childrens = plmRequirementTemplateRepository.findByParent(requirementTemplate.getParent());
            PLMRequirementTemplate parentReq = plmRequirementTemplateRepository.findOne(requirementTemplate.getParent());
            Integer seq = childrens.size() + 1;
            requirementTemplate.setSequence(seq);
            requirementTemplate.setPath(parentReq.getPath() + "." + requirementTemplate.getSequence());
        } else {
            List<PLMRequirementTemplate> requirements = plmRequirementTemplateRepository.findByDocumentTemplateAndParentIsNull(requirementTemplate.getDocumentTemplate());
            Integer seq = requirements.size() + 1;
            requirementTemplate.setSequence(seq);
            requirementTemplate.setPath(seq.toString());
        }

        return requirementTemplate;
    }

    @Transactional
    public PLMRequirementTemplate updateReqTemplate(PLMRequirementTemplate plmRequirementTemplate) {

        plmRequirementTemplate = plmRequirementTemplateRepository.save(plmRequirementTemplate);
        return plmRequirementTemplate;
    }


    @Transactional
    public void deleteReqTemplate(Integer id) {
        plmRequirementTemplateRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public PLMRequirementTemplate getReqTemplate(Integer id) {
        PLMRequirementTemplate plmRequirementDocumentTemplate = plmRequirementTemplateRepository.findOne(id);
        return plmRequirementDocumentTemplate;
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementTemplate> getAllReqTemplates() {
        return plmRequirementTemplateRepository.findAll();
    }


    //-------------------------PLMRequirementDocumentTemplateReviewer ------------------------

    @Transactional
    public PLMRequirementDocumentTemplateReviewer addReqDocTemplateReviewer(Integer reqDocTemplate, PLMRequirementDocumentTemplateReviewer plmRequirementDocumentTemplateReviewer) {
        plmRequirementDocumentTemplateReviewer.setDocumentTemplate(reqDocTemplate);
        plmRequirementDocumentTemplateReviewer = plmRequirementDocumentTemplateReviewerRepository.save(plmRequirementDocumentTemplateReviewer);
        return plmRequirementDocumentTemplateReviewer;
    }


    @Transactional
    public PLMRequirementDocumentTemplateReviewer updateReqDocTemplateReviewer(Integer reqDocTemId, PLMRequirementDocumentTemplateReviewer plmRequirementDocumentTemplateReviewer) {

        plmRequirementDocumentTemplateReviewer = plmRequirementDocumentTemplateReviewerRepository.save(plmRequirementDocumentTemplateReviewer);
        return plmRequirementDocumentTemplateReviewer;
    }


    @Transactional
    public void deleteReqDocTemplateReviewer(Integer id) {
        plmRequirementDocumentTemplateReviewerRepository.delete(id);

    }


    @Transactional(readOnly = true)
    public PLMRequirementDocumentTemplateReviewer getReqDocTemplateReviewer(Integer id) {
        PLMRequirementDocumentTemplateReviewer plmRequirementDocumentTemplateReviewer = plmRequirementDocumentTemplateReviewerRepository.findOne(id);
        return plmRequirementDocumentTemplateReviewer;
    }


    @Transactional(readOnly = true)
    public List<PLMRequirementDocumentTemplateReviewer> getAllReqDocTemplateReviewers(Integer id) {
        return plmRequirementDocumentTemplateReviewerRepository.findByDocumentTemplate(id);
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementTemplate> getRequirementTemplateTree(Integer reqDocId) {
        PLMRequirementDocumentTemplate template = plmRequirementDocumentTemplateRepository.findOne(reqDocId);
        List<PLMRequirementTemplate> requirementTemplates = plmRequirementTemplateRepository.findByDocumentTemplateAndParentIsNull(template);
        for (PLMRequirementTemplate plmRequirementTemplate : requirementTemplates) {
            visitRequirementVersionChildren(plmRequirementTemplate);
        }
        return requirementTemplates;
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementTemplate> getRequirementTemplateChildren(Integer id) {
        return plmRequirementTemplateRepository.findByParent(id);
    }

    private void visitRequirementVersionChildren(PLMRequirementTemplate requirementTemplate) {
        List<PLMRequirementTemplate> childrens = getRequirementTemplateChildren(requirementTemplate.getId());
        for (PLMRequirementTemplate child : childrens) {
            visitRequirementVersionChildren(child);
        }
        requirementTemplate.setChildren(childrens);
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementTemplateReviewer> getAllRequirementTemplateReviewers(Integer id) {
        return requirementTemplateReviewerRepository.findByRequirementTemplate(id);
    }

    @Transactional
    public PLMRequirementTemplateReviewer createRequirementTemplateReviewer(Integer reqTemplate, PLMRequirementTemplateReviewer requirementTemplateReviewer) {
        return requirementTemplateReviewerRepository.save(requirementTemplateReviewer);
    }

    @Transactional
    public PLMRequirementTemplateReviewer updateRequirementTemplateReviewer(Integer reqTemplate, PLMRequirementTemplateReviewer requirementTemplateReviewer) {

        return requirementTemplateReviewerRepository.save(requirementTemplateReviewer);
    }


    @Transactional
    public void deleteRequirementTemplateReviewer(Integer id) {
        requirementTemplateReviewerRepository.delete(id);

    }

}
