package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.custom.CustomObject;
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
 * Created by Nageshreddy on 02-08-2021.
 */

@Component
public class CustomObjectAdvancedCriteria {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ObjectAttributeAdvancedCriteria attributeAdvancedCriteria;

    public TypedQuery<CustomObject> getObjectTypeQuery(Integer objectTypeId, CustomParameterCriteria[] customParameterCriterias) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustomObject> cq = builder.createQuery(CustomObject.class);
        Root<CustomObject> from = cq.from(CustomObject.class);
        cq.select(from);
        List<Predicate> predicates = new ArrayList<>();
        if (customParameterCriterias.length > 0) {
            for (CustomParameterCriteria customParameterCriteria : customParameterCriterias) {
                if (customParameterCriteria.getField().contains("object.")) {
                    if (customParameterCriteria.getField().equals("object.number")) {
                        if (customParameterCriteria.getOperator().equals("contains")) {
                            Predicate p1 = builder.like(builder.lower(from.get("number")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p1);
                        } else if (customParameterCriteria.getOperator().equals("startswith")) {
                            Predicate p1 = builder.like(builder.lower(from.get("number")), customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p1);
                        } else if (customParameterCriteria.getOperator().equals("endswith")) {
                            Predicate p1 = builder.like(builder.lower(from.get("number")), "%" + customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p1);
                        } else if (customParameterCriteria.getOperator().equals("equals")) {
                            Predicate p1 = builder.equal(builder.lower(from.get("number")), customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p1);
                        } else if (customParameterCriteria.getOperator().equals("notequal")) {
                            Predicate p1 = builder.notEqual(builder.lower(from.get("number")), customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p1);
                        } else if (customParameterCriteria.getOperator().equals("doesnotcontain")) {
                            Predicate p1 = builder.notLike(builder.lower(from.get("number")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p1);
                        } else if (customParameterCriteria.getOperator().equals("doesnotstartwith")) {
                            Predicate p1 = builder.notLike(builder.lower(from.get("number")), customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p1);
                        } else if (customParameterCriteria.getOperator().equals("doesnotendwith")) {
                            Predicate p1 = builder.notLike(builder.lower(from.get("number")), "%" + customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p1);
                        }
                    } else if (customParameterCriteria.getField().equals("object.name")) {
                        if (customParameterCriteria.getOperator().equals("contains")) {
                            Predicate pn = builder.like(builder.lower(from.get("name")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(pn);
                        } else if (customParameterCriteria.getOperator().equals("startswith")) {
                            Predicate pn = builder.like(builder.lower(from.get("name")), customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(pn);
                        } else if (customParameterCriteria.getOperator().equals("endswith")) {
                            Predicate pn = builder.like(builder.lower(from.get("name")), "%" + customParameterCriteria.getValue().toLowerCase());
                            predicates.add(pn);
                        } else if (customParameterCriteria.getOperator().equals("equals")) {
                            Predicate pn = builder.equal(builder.lower(from.get("name")), customParameterCriteria.getValue().toLowerCase());
                            predicates.add(pn);
                        } else if (customParameterCriteria.getOperator().equals("notequal")) {
                            Predicate pn = builder.notEqual(builder.lower(from.get("name")), customParameterCriteria.getValue().toLowerCase());
                            predicates.add(pn);
                        } else if (customParameterCriteria.getOperator().equals("doesnotcontain")) {
                            Predicate pn = builder.notLike(builder.lower(from.get("name")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(pn);
                        } else if (customParameterCriteria.getOperator().equals("doesnotstartwith")) {
                            Predicate pn = builder.notLike(builder.lower(from.get("name")), customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(pn);
                        } else if (customParameterCriteria.getOperator().equals("doesnotendwith")) {
                            Predicate pn = builder.notLike(builder.lower(from.get("name")), "%" + customParameterCriteria.getValue().toLowerCase());
                            predicates.add(pn);
                        }
                    } else if (customParameterCriteria.getField().equals("object.description")) {
                        if (customParameterCriteria.getOperator().equals("contains")) {
                            Predicate p3 = builder.like(builder.lower(from.get("description")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p3);
                        } else if (customParameterCriteria.getOperator().equals("startswith")) {
                            Predicate p3 = builder.like(builder.lower(from.get("description")), customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p3);
                        } else if (customParameterCriteria.getOperator().equals("endswith")) {
                            Predicate p3 = builder.like(builder.lower(from.get("description")), "%" + customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p3);
                        } else if (customParameterCriteria.getOperator().equals("equals")) {
                            Predicate p3 = builder.equal(builder.lower(from.get("description")), customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p3);
                        } else if (customParameterCriteria.getOperator().equals("notequal")) {
                            Predicate p3 = builder.notEqual(builder.lower(from.get("description")), customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p3);
                        } else if (customParameterCriteria.getOperator().equals("doesnotcontain")) {
                            Predicate p3 = builder.notLike(builder.lower(from.get("description")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p3);
                        } else if (customParameterCriteria.getOperator().equals("doesnotstartwith")) {
                            Predicate p3 = builder.notLike(builder.lower(from.get("description")), customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p3);
                        } else if (customParameterCriteria.getOperator().equals("doesnotendwith")) {
                            Predicate p3 = builder.notLike(builder.lower(from.get("description")), "%" + customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p3);
                        } else if (customParameterCriteria.getOperator().equals("isempty")) {
                            predicates.add(builder.or(builder.isNull(from.get("description")), builder.equal(from.get("description"), "")));
                        } else if (customParameterCriteria.getOperator().equals("isnotempty")) {
                            predicates.add(builder.and(builder.isNotNull(from.get("description")), builder.notEqual(from.get("description"), "")));
                        }
                    } else if (customParameterCriteria.getField().equals("object.type")) {
                        if (customParameterCriteria.getOperator().equals("contains")) {
                            Predicate p4 = builder.like(builder.lower(from.get("type").get("name")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p4);
                        } else if (customParameterCriteria.getOperator().equals("startswith")) {
                            Predicate p4 = builder.like(builder.lower(from.get("type").get("name")), customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p4);
                        } else if (customParameterCriteria.getOperator().equals("endswith")) {
                            Predicate p4 = builder.like(builder.lower(from.get("type").get("name")), "%" + customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p4);
                        } else if (customParameterCriteria.getOperator().equals("equals")) {
                            Predicate p4 = builder.equal(builder.lower(from.get("type").get("name")), customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p4);
                        } else if (customParameterCriteria.getOperator().equals("notequal")) {
                            Predicate p4 = builder.notEqual(builder.lower(from.get("type").get("name")), customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p4);
                        } else if (customParameterCriteria.getOperator().equals("doesnotcontain")) {
                            Predicate p4 = builder.notLike(builder.lower(from.get("type").get("name")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p4);
                        } else if (customParameterCriteria.getOperator().equals("doesnotstartwith")) {
                            Predicate p4 = builder.notLike(builder.lower(from.get("type").get("name")), customParameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p4);
                        } else if (customParameterCriteria.getOperator().equals("doesnotendwith")) {
                            Predicate p4 = builder.notLike(builder.lower(from.get("type").get("name")), "%" + customParameterCriteria.getValue().toLowerCase());
                            predicates.add(p4);
                        }
                    }
                } else if (customParameterCriteria.getField().contains("attribute.")) {
                    Predicate p6 = attributeAdvancedCriteria.getAttributePredicate(customParameterCriteria, builder, cq, from);
                    predicates.add(p6);
                }
            }
            if (objectTypeId != null) {
                Predicate p4 = builder.equal(from.get("type"), objectTypeId);
                predicates.add(p4);
            }
            cq.where(predicates.toArray(new Predicate[]{}));
            TypedQuery<CustomObject> typedQuery1 = entityManager.createQuery(cq);
            return typedQuery1;
        } else {
            return entityManager.createQuery(cq);
        }
    }

}
