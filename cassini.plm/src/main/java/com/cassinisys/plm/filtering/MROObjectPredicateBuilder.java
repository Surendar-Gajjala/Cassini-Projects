package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mro.*;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MROObjectPredicateBuilder implements PredicateBuilder<MROObjectCriteria, QMROObject> {

    @Autowired
    private MROObjectTypeRepository mroObjectTypeRepository;
    @Autowired
    private MROAssetTypeRepository assetTypeRepository;
    @Autowired
    private MROAssetRepository assetRepository;
    @Autowired
    private MROMeterTypeRepository meterTypeRepository;
    @Autowired
    private MROMeterRepository meterRepository;
    @Autowired
    private MROSparePartTypeRepository sparePartTypeRepository;
    @Autowired
    private MROSparePartRepository sparePartRepository;
    @Autowired
    private MROWorkOrderTypeRepository workOrderTypeRepository;
    @Autowired
    private MROWorkOrderRepository workOrderRepository;
    @Autowired
    private MROWorkRequestTypeRepository workRequestTypeRepository;
    @Autowired
    private MROWorkRequestRepository workRequestRepository;

    @Override
    public Predicate build(MROObjectCriteria mroObjectCriteria, QMROObject pathBase) {
        return getDefaultPredicate(mroObjectCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(MROObjectCriteria criteria, QMROObject pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.number.containsIgnoreCase(s).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }

        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }

        return ExpressionUtils.allOf(predicates);
    }

    public Predicate getDefaultPredicates(MROObjectCriteria criteria, QMROObject pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.number.containsIgnoreCase(s).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getType())) {
            MROObjectType objectType = mroObjectTypeRepository.findOne(criteria.getType());
            List<Integer> ids = new ArrayList<>();
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ASSETTYPE.toString()))) {
                MROAssetType type = assetTypeRepository.findOne(objectType.getId());
                List<MROAsset> assets = assetRepository.findByType(type);
                assets.forEach(asset -> {
                    ids.add(asset.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.METERTYPE.toString()))) {
                MROMeterType type = meterTypeRepository.findOne(objectType.getId());
                List<MROMeter> meters = meterRepository.findByType(type);
                meters.forEach(meter -> {
                    ids.add(meter.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.SPAREPARTTYPE.toString()))) {
                MROSparePartType type = sparePartTypeRepository.findOne(objectType.getId());
                List<MROSparePart> spareParts = sparePartRepository.findByType(type);
                spareParts.forEach(sparePart -> {
                    ids.add(sparePart.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.WORKREQUESTTYPE.toString()))) {
                MROWorkRequestType type = workRequestTypeRepository.findOne(objectType.getId());
                List<MROWorkRequest> workRequests = workRequestRepository.findByType(type);
                workRequests.forEach(workRequest -> {
                    ids.add(workRequest.getId());
                });
            }
            if (objectType.getObjectType().equals(ObjectType.valueOf(PLMObjectType.WORKORDERTYPE.toString()))) {
                MROWorkOrderType type = workOrderTypeRepository.findOne(objectType.getId());
                List<MROWorkOrder> workOrders = workOrderRepository.findByType(type);
                workOrders.forEach(workOrder -> {
                    ids.add(workOrder.getId());
                });
            }
            predicates.add(pathBase.id.in(ids));
        }

        return ExpressionUtils.allOf(predicates);
    }
}