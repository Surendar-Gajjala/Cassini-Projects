package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.production.InventoryType;
import com.cassinisys.erp.model.store.ERPStockMovement;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 16/07/18.
 */
@Component
public class StockMovementPredicateBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public TypedQuery<ERPStockMovement> getItemTypeQuery(StockMovementCriteria criteria) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ERPStockMovement> cq = builder.createQuery(ERPStockMovement.class);
        Root<ERPStockMovement> from = cq.from(ERPStockMovement.class);
        cq.select(from);
        List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();

        if (criteria.getMovementType() == InventoryType.STOCKIN || criteria.getMovementType() == InventoryType.STOCKOUT) {
            Predicate p1 = builder.equal((from.get("movementType")), criteria.getMovementType());
            predicates.add(p1);
        }

        /*if (criteria.getItemName() != null || criteria.getItemNumber() != null || criteria.getDescription() != null) {
            Predicate predicate = getBoqPredicate(criteria, builder, cq, from);
            predicates.add(predicate);
        }*/

        if (criteria.getStoreId() != null) {
            Predicate p1 = builder.equal((from.get("store")), criteria.getStoreId());
            predicates.add(p1);
        }

        cq.where(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));
        TypedQuery<ERPStockMovement> typedQuery1 = entityManager.createQuery(cq);
        return typedQuery1;
    }

    /*private Predicate getBoqPredicate(StockMovementCriteria criteria, CriteriaBuilder builder, CriteriaQuery cq, Root from) {
        Subquery<Integer> bomSq = cq.subquery(Integer.class);
        Root<ERPMaterial> sFrom = bomSq.from(ERPMaterial.class);
        bomSq.select(sFrom.get("id"));
        List<javax.persistence.criteria.Predicate> bomPredicates = new ArrayList<>();

        if (criteria.getItemName() != null && !criteria.getItemName().equals("null")) {
            javax.persistence.criteria.Predicate bp1 = builder.like(builder.lower(sFrom.get("itemName")), "%" + criteria.getItemName().toLowerCase() + "%");
            bomPredicates.add(bp1);
        }

        if (criteria.getItemNumber() != null && !criteria.getItemNumber().equals("null")) {
            javax.persistence.criteria.Predicate bp2 = builder.like(builder.lower(sFrom.get("itemNumber")), "%" + criteria.getItemNumber().toLowerCase() + "%");
            bomPredicates.add(bp2);
        }

        if (criteria.getDescription() != null && !criteria.getDescription().equals("null")) {
            javax.persistence.criteria.Predicate bp3 = builder.like(builder.lower(sFrom.get("description")), "%" + criteria.getDescription().toLowerCase() + "%");
            bomPredicates.add(bp3);
        }

        bomSq.where(bomPredicates.toArray(new Predicate[]{}));
        Predicate p1 = builder.in(from.get("item")).value(bomSq);

        return p1;
    }*/

}
