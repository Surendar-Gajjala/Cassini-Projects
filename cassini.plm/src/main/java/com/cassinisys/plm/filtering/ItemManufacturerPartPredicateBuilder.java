package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.PLMMCOAffectedItem;
import com.cassinisys.plm.model.cm.PLMMCORelatedItem;
import com.cassinisys.plm.model.cm.PLMVarianceAffectedMaterial;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.repo.cm.MCOAffectedItemRepository;
import com.cassinisys.plm.repo.cm.MCORelatedItemRepository;
import com.cassinisys.plm.repo.cm.VarianceAffectedMaterialRepository;
import com.cassinisys.plm.repo.mfr.ItemManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartTypeRepository;
import com.cassinisys.plm.repo.mfr.SupplierPartRepository;
import com.cassinisys.plm.repo.pgc.PGCBosRepository;
import com.cassinisys.plm.repo.pgc.PGCDeclarationPartRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 20-05-2020.
 */
@Component
public class ItemManufacturerPartPredicateBuilder implements PredicateBuilder<ItemManufacturerPartCriteria, QPLMManufacturerPart> {

    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;
    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private MCOAffectedItemRepository mcoAffectedItemRepository;
    @Autowired
    private MCORelatedItemRepository mcoRelatedItemRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private NCRProblemItemRepository ncrProblemItemRepository;
    @Autowired
    private NCRRelatedItemRepository ncrRelatedItemRepository;
    @Autowired
    private QCRProblemMaterialRepository qcrProblemMaterialRepository;
    @Autowired
    private QCRRelatedMaterialRepository qcrRelatedMaterialRepository;
    @Autowired
    private VarianceAffectedMaterialRepository varianceAffectedMaterialRepository;
    @Autowired
    private MaterialInspectionRelatedItemRepository materialInspectionRelatedItemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private SupplierPartRepository supplierPartRepository;
    @Autowired
    private PGCDeclarationPartRepository declarationPartRepository;
    @Autowired
    private PGCBosRepository pgcBosRepository;

    public Predicate build(ItemManufacturerPartCriteria criteria, QPLMManufacturerPart pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(ItemManufacturerPartCriteria criteria, QPLMManufacturerPart pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getPartName())) {
            predicates.add(pathBase.partName.containsIgnoreCase(criteria.getPartName()));
        }
        if (!Criteria.isEmpty(criteria.getPartNumber())) {
            predicates.add(pathBase.partNumber.containsIgnoreCase(criteria.getPartNumber()));
        }
        if (!Criteria.isEmpty(criteria.getMfrPartType())) {
            PLMManufacturerPartType partType = manufacturerPartTypeRepository.findOne(criteria.getMfrPartType());
            predicates.add(pathBase.mfrPartType.eq(partType));
        }
        if (!Criteria.isEmpty(criteria.getItem())) {
            List<PLMItemManufacturerPart> itemManufacturerParts = new ArrayList<>();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(criteria.getItem());
            if (itemRevision.getInstance() != null) {
                itemManufacturerParts = itemManufacturerPartRepository.findByItem(itemRevision.getInstance());
            }

            itemManufacturerParts.addAll(itemManufacturerPartRepository.findByItem(criteria.getItem()));

            if (itemManufacturerParts.size() > 0) {
                for (PLMItemManufacturerPart itemManufacturerPart : itemManufacturerParts) {
                    predicates.add(pathBase.id.ne(itemManufacturerPart.getManufacturerPart().getId()));
                }
            }

        }


        if (!Criteria.isEmpty(criteria.getSupplier()) && Criteria.isEmpty(criteria.getDeclaration())) {
            List<PLMSupplierPart> supplierParts = new ArrayList<>();
            supplierParts.addAll(supplierPartRepository.findBySupplierOrderByModifiedDateDesc(criteria.getSupplier()));
            if (supplierParts.size() > 0) {
                for (PLMSupplierPart supplierPart : supplierParts) {
                    predicates.add(pathBase.id.ne(supplierPart.getManufacturerPart().getId()));
                }
            }

        }

        if (!Criteria.isEmpty(criteria.getVariance())) {
            List<PLMVarianceAffectedMaterial> varianceAffectedMaterials = varianceAffectedMaterialRepository.findByVariance(criteria.getVariance());

            if (varianceAffectedMaterials.size() > 0) {
                for (PLMVarianceAffectedMaterial varianceAffectedMaterial : varianceAffectedMaterials) {
                    predicates.add(pathBase.id.ne(varianceAffectedMaterial.getMaterial()));
                }
            }

        }

        if (!Criteria.isEmpty(criteria.getMco())) {
            List<PLMMCOAffectedItem> affectedItems = mcoAffectedItemRepository.findByMco(criteria.getMco());
            List<PLMMCORelatedItem> relatedItems = mcoRelatedItemRepository.findByMco(criteria.getMco());

            if (!Criteria.isEmpty(criteria.getMcoReplacement())) {
                PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(criteria.getMcoReplacement());
                predicates.add(pathBase.id.ne(criteria.getMcoReplacement()));
                predicates.add(pathBase.mfrPartType.id.eq(manufacturerPart.getMfrPartType().getId()));
            }

            if (criteria.getMcoAffectedItems().size() > 0) {
                for (Integer partId : criteria.getMcoAffectedItems()) {
                    predicates.add(pathBase.id.ne(partId));
                }
            }

            for (PLMMCORelatedItem relatedItem : relatedItems) {
                predicates.add(pathBase.id.ne(relatedItem.getPart().getId()));
            }

            if (affectedItems.size() > 0) {
                for (PLMMCOAffectedItem affectedItem : affectedItems) {
                    if (affectedItem.getReplacement() != null && !criteria.getRelated()) {
                        predicates.add(pathBase.id.ne(affectedItem.getReplacement()));
                    }
                    predicates.add(pathBase.id.ne(affectedItem.getMaterial()));
                }
            }

        }

        if (!Criteria.isEmpty(criteria.getNcr())) {
            List<PQMNCRProblemItem> affectedItems = ncrProblemItemRepository.findByNcr(criteria.getNcr());
            List<PQMNCRRelatedItem> relatedItems = ncrRelatedItemRepository.findByNcr(criteria.getNcr());


            if (criteria.getMcoAffectedItems().size() > 0) {
                for (Integer partId : criteria.getMcoAffectedItems()) {
                    predicates.add(pathBase.id.ne(partId));
                }
            }
            for (PQMNCRProblemItem affectedItem : affectedItems) {
                predicates.add(pathBase.id.ne(affectedItem.getMaterial().getId()));
            }
            if (criteria.getRelated()) {
                for (PQMNCRRelatedItem relatedItem : relatedItems) {
                    predicates.add(pathBase.id.ne(relatedItem.getMaterial().getId()));
                }
            }
        }

        if (!Criteria.isEmpty(criteria.getInspection())) {
            List<PQMMaterialInspectionRelatedItem> inspectionRelatedItems = materialInspectionRelatedItemRepository.findByInspection(criteria.getInspection());

            for (PQMMaterialInspectionRelatedItem inspectionRelatedItem : inspectionRelatedItems) {
                predicates.add(pathBase.id.ne(inspectionRelatedItem.getMaterial()));
            }
        }

        if (!Criteria.isEmpty(criteria.getQcr())) {
            List<PQMQCRProblemMaterial> problemMaterials = qcrProblemMaterialRepository.findByQcr(criteria.getQcr());
            List<PQMQCRRelatedMaterial> relatedItems = qcrRelatedMaterialRepository.findByQcr(criteria.getQcr());
            for (PQMQCRProblemMaterial problemMaterial : problemMaterials) {
                predicates.add(pathBase.id.ne(problemMaterial.getMaterial().getId()));
            }
            if (criteria.getRelated()) {
                for (PQMQCRRelatedMaterial relatedItem : relatedItems) {
                    predicates.add(pathBase.id.ne(relatedItem.getMaterial().getId()));
                }
            }
        }
        if (!Criteria.isEmpty(criteria.getSupplier()) && !Criteria.isEmpty(criteria.getDeclaration())) {

            List<Integer> partIds = pgcBosRepository.getUniquePartIds();

            List<Integer> declarationPartIds = declarationPartRepository.getPartIdByDeclaration(criteria.getDeclaration());
            if (declarationPartIds.size() > 0) {
                List<Integer> supplierParts = supplierPartRepository.getPartIdBySupplier(criteria.getSupplier(), declarationPartIds);
                predicates.add(pathBase.id.in(supplierParts));
            } else {
                List<Integer> integers = supplierPartRepository.getSupplierParts(criteria.getSupplier());
                predicates.add(pathBase.id.in(integers));
            }

            partIds.forEach(partId -> {
                predicates.add(pathBase.id.ne(partId));
            });
        }
        predicates.add(pathBase.lifeCyclePhase.phaseType.eq(LifeCyclePhaseType.RELEASED));
        return ExpressionUtils.allOf(predicates);
    }
}
