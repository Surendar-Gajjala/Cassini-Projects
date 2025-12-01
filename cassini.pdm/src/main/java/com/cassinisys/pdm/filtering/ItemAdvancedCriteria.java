package com.cassinisys.pdm.filtering;

import com.cassinisys.pdm.model.PDMItem;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by Nageshreddy on 15-06-2016.
 */

@Component
public class ItemAdvancedCriteria {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BomAdvancedCriteria bomAdvancedCriteria;

    @Autowired
    private ItemFileAdvancedCriteria itemFileAdvancedCriteria;

    @Autowired
    private AttributeAdvancedCriteria attributeAdvancedCriteria;


    public TypedQuery<PDMItem> getItemTypeQuery(ParameterCriteria[] parameterCriterias) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PDMItem> cq = builder.createQuery(PDMItem.class);
        Root<PDMItem> from = cq.from(PDMItem.class);
        cq.select(from);

        List<Predicate> predicates = new ArrayList<>();
        if (parameterCriterias.length > 0) {
            for (ParameterCriteria parameterCriteria : parameterCriterias) {
                if (parameterCriteria.getField().contains("item.")) {

                    if (parameterCriteria.getField().equals("item.itemNumber")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate p1 = builder.like(builder.lower(from.get("itemNumber")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p1);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate p1 = builder.like(builder.lower(from.get("itemNumber")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p1);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate p1 = builder.like(builder.lower(from.get("itemNumber")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p1);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate p1 = builder.equal(builder.lower(from.get("itemNumber")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p1);
                        }
                    } else if (parameterCriteria.getField().equals("item.revision")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate p2 = builder.like(builder.lower(from.get("revision")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p2);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate p2 = builder.like(builder.lower(from.get("revision")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p2);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate p2 = builder.like(builder.lower(from.get("revision")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p2);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate p2 = builder.equal(builder.lower(from.get("revision")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p2);
                        }
                    } else if (parameterCriteria.getField().equals("item.description")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate p3 = builder.like(builder.lower(from.get("description")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p3);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate p3 = builder.like(builder.lower(from.get("description")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p3);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate p3 = builder.like(builder.lower(from.get("description")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p3);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate p3 = builder.equal(builder.lower(from.get("description")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p3);
                        }
                    } else if (parameterCriteria.getField().equals("item.itemType")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate p4 = builder.like(builder.lower(from.get("itemType").get("name")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p4);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate p4 = builder.like(builder.lower(from.get("itemType").get("name")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p4);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate p4 = builder.like(builder.lower(from.get("itemType").get("name")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p4);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate p4 = builder.equal(builder.lower(from.get("itemType").get("name")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p4);
                        }
                    } else if (parameterCriteria.getField().equals("item.revision")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate p5 = builder.like(builder.lower(from.get("revision")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p5);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate p5 = builder.like(builder.lower(from.get("revision")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p5);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate p5 = builder.like(builder.lower(from.get("revision")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p5);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate p5 = builder.equal(builder.lower(from.get("revision")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p5);
                        }
                    }
                } else if (parameterCriteria.getField().contains("bom.")) {
                    Predicate p4 = bomAdvancedCriteria.getBomPredicate(parameterCriteria, builder, cq, from);
                    predicates.add(p4);
                } else if (parameterCriteria.getField().contains("itemFile.")) {
                    Predicate p5 = itemFileAdvancedCriteria.getItemFilePredicate(parameterCriteria, builder, cq, from);
                    predicates.add(p5);
                } else if (parameterCriteria.getField().contains("attribute.")) {
                    Predicate p6 = attributeAdvancedCriteria.getAttributePredicate(parameterCriteria, builder, cq, from);
                    predicates.add(p6);
                }
            }
            cq.where(predicates.toArray(new Predicate[]{}));
            TypedQuery<PDMItem> typedQuery1 = entityManager.createQuery(cq);
            return typedQuery1;
        } else {
            return entityManager.createQuery(cq);
        }
    }
}
