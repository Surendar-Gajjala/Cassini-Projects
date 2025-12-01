package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.store.ERPInventory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ERPInventoryPredicateBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    public TypedQuery<ERPInventory> getItemTypeQuery(ERPInventoryCriteria criteria) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ERPInventory> cq = builder.createQuery(ERPInventory.class);
        Root<ERPInventory> from = cq.from(ERPInventory.class);
        cq.select(from);
        List<Predicate> predicates = new ArrayList<>();

        Subquery<Integer> projectSq = cq.subquery(Integer.class);
//        Root<ISProject> pFrom = projectSq.from(ISProject.class);
        List<Predicate> projectPredicates = new ArrayList<>();


        /*if(criteria.getItemName() != null || criteria.getItemNumber() != null || criteria.getItemDescription() != null){
            Predicate predicate = getBoqPredicate(criteria, builder, cq, from);
            predicates.add(predicate);
        }*/

        cq.where(predicates.toArray(new Predicate[]{}));
        TypedQuery<ERPInventory> typedQuery1 = entityManager.createQuery(cq);
        return typedQuery1;
    }

    /*private Predicate getBoqPredicate(ERPInventoryCriteria criteria, CriteriaBuilder builder, CriteriaQuery cq,Root from){
        Subquery<Integer> bomSq = cq.subquery(Integer.class);
        Root<ISBoqItem> sFrom = bomSq.from(ISBoqItem.class);
        bomSq.select(sFrom.get("id"));
        List<Predicate> bomPredicates = new ArrayList<>();

        if(criteria.getItemName() != null && !criteria.getItemName().equals("null")) {
            Predicate bp1 = builder.like(builder.lower(sFrom.get("itemName")), "%" + criteria.getItemName().toLowerCase() + "%");
            bomPredicates.add(bp1);
        }

        if(criteria.getItemNumber() != null && !criteria.getItemNumber().equals("null")) {
            Predicate bp2 = builder.like(builder.lower(sFrom.get("itemNumber")), "%" + criteria.getItemNumber().toLowerCase() + "%");
            bomPredicates.add(bp2);
        }

        if(criteria.getItemDescription() != null && !criteria.getItemDescription().equals("null")) {
            Predicate bp3 = builder.like(builder.lower(sFrom.get("description")), "%" + criteria.getItemDescription().toLowerCase() + "%");
            bomPredicates.add(bp3);
        }

        if(criteria.getUnits() != null && !criteria.getUnits().equals("null")) {
            Predicate bp4 = builder.like(builder.lower(sFrom.get("units")), "%" + criteria.getUnits().toLowerCase() + "%");
            bomPredicates.add(bp4);
        }

        bomSq.where(bomPredicates.toArray(new Predicate[]{}));
        Predicate p1 = builder.in(from.get("item")).value(bomSq);

        return p1;
    }*/

   /* private Predicate getProjectPredicate(StockMovementCriteria criteria, CriteriaBuilder builder, Subquery bomSq,Root sFrom) {
        Subquery<Integer> projectSq = bomSq.subquery(Integer.class);
        Root<ISProject> pFrom = projectSq.from(ISProject.class);
        projectSq.select(pFrom.get("id"));
        List<Predicate> projectPredicates = new ArrayList<>();

        Predicate pp1 = builder.like(builder.lower(pFrom.get("name")), "%" + criteria.getProjectName().toLowerCase() + "%");
        projectPredicates.add(pp1);

        projectSq.where(projectPredicates.toArray(new Predicate[]{}));
        Predicate p1 = builder.in(sFrom.get("project")).value(projectSq);

        return p1;

    }*/
}
