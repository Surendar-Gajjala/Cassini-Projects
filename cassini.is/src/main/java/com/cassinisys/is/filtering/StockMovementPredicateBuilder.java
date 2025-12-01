package com.cassinisys.is.filtering;

import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.model.procm.MovementType;
import com.cassinisys.is.model.store.ISTopStockMovement;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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
    public TypedQuery<ISTopStockMovement> getItemTypeQuery(StockMovementCriteria criteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ISTopStockMovement> cq = builder.createQuery(ISTopStockMovement.class);
        Root<ISTopStockMovement> from = cq.from(ISTopStockMovement.class);
        cq.select(from);
        List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
        if (criteria.getMovementType() == MovementType.ISSUED || criteria.getMovementType() == MovementType.RECEIVED || criteria.getMovementType() == MovementType.LOANISSUED || criteria.getMovementType() == MovementType.ALLOCATED || criteria.getMovementType() == MovementType.RETURNED ||
                criteria.getMovementType() == MovementType.LOANRECEIVED || criteria.getMovementType() == MovementType.LOANRETURNITEMISSUED || criteria.getMovementType() == MovementType.LOANRETURNITEMRECEIVED || criteria.getMovementType() == MovementType.OPENINGBALANCE) {
            Predicate p1 = builder.equal((from.get("movementType")), criteria.getMovementType());
            predicates.add(p1);
        }
        if (criteria.getProjectName() != null && !criteria.getProjectName().equals("null")) {
            Predicate bp4 = getProjectPredicate(criteria, builder, cq, from);
            predicates.add(bp4);
        }
        if (criteria.getItemName() != null || criteria.getItemNumber() != null || criteria.getDescription() != null || criteria.getUnits() != null) {
            Predicate predicate = getMaterialItemPredicate(criteria, builder, cq, from);
            predicates.add(predicate);
        }
        if (criteria.getStoreId() != null) {
            Predicate p1 = builder.equal((from.get("store")), criteria.getStoreId());
            predicates.add(p1);
        }
        cq.where(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));
        TypedQuery<ISTopStockMovement> typedQuery1 = entityManager.createQuery(cq);
        return typedQuery1;
    }

    private Predicate getMaterialItemPredicate(StockMovementCriteria criteria, CriteriaBuilder builder, CriteriaQuery cq, Root from) {
        Subquery<Integer> bomSq = cq.subquery(Integer.class);
        Root<ISMaterialItem> sFrom = bomSq.from(ISMaterialItem.class);
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
        if (criteria.getUnits() != null && !criteria.getUnits().equals("null")) {
            javax.persistence.criteria.Predicate bp4 = builder.like(builder.lower(sFrom.get("units")), "%" + criteria.getUnits().toLowerCase() + "%");
            bomPredicates.add(bp4);
        }
        bomSq.where(bomPredicates.toArray(new Predicate[]{}));
        Predicate p1 = builder.in(from.get("item")).value(bomSq);
        return p1;
    }

    private Predicate getProjectPredicate(StockMovementCriteria criteria, CriteriaBuilder builder, CriteriaQuery cq, Root sFrom) {
        Subquery<Integer> projectSq = cq.subquery(Integer.class);
        Root<ISProject> pFrom = projectSq.from(ISProject.class);
        projectSq.select(pFrom.get("id"));
        List<javax.persistence.criteria.Predicate> projectPredicates = new ArrayList<>();
        javax.persistence.criteria.Predicate pp1 = builder.like(builder.lower(pFrom.get("name")), "%" + criteria.getProjectName().toLowerCase() + "%");
        projectPredicates.add(pp1);
        projectSq.where(projectPredicates.toArray(new Predicate[]{}));
        Predicate p1 = builder.in(sFrom.get("project")).value(projectSq);
        return p1;

    }

}
