package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.plm.model.plm.ItemClass;
import com.cassinisys.plm.model.plm.PLMItem;
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
    private ItemrevAdvancedCriteria itemrevAdvancedCriteria;

    @Autowired
    private BomAdvancedCriteria bomAdvancedCriteria;

    @Autowired
    private ItemFileAdvancedCriteria itemFileAdvancedCriteria;

    @Autowired
    private AttributeAdvancedCriteria attributeAdvancedCriteria;

    @Autowired
    private ReferenceAvancedCriteria referenceAvancedCriteria;

    public TypedQuery<PLMItem> getItemTypeQuery(ParameterCriteria[] parameterCriterias) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PLMItem> cq = builder.createQuery(PLMItem.class);
        Root<PLMItem> from = cq.from(PLMItem.class);
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
                        } else if (parameterCriteria.getOperator().equals("notequal")) {
                            Predicate p1 = builder.notEqual(builder.lower(from.get("itemNumber")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p1);
                        } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                            Predicate p1 = builder.notLike(builder.lower(from.get("itemNumber")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p1);
                        } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                            Predicate p1 = builder.notLike(builder.lower(from.get("itemNumber")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p1);
                        } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                            Predicate p1 = builder.notLike(builder.lower(from.get("itemNumber")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p1);
                        }
                    } else if (parameterCriteria.getField().equals("item.name")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate pn = builder.like(builder.lower(from.get("itemName")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(pn);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate pn = builder.like(builder.lower(from.get("itemName")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(pn);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate pn = builder.like(builder.lower(from.get("itemName")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(pn);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate pn = builder.equal(builder.lower(from.get("itemName")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(pn);
                        } else if (parameterCriteria.getOperator().equals("notequal")) {
                            Predicate pn = builder.notEqual(builder.lower(from.get("itemName")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(pn);
                        } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                            Predicate pn = builder.notLike(builder.lower(from.get("itemName")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(pn);
                        } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                            Predicate pn = builder.notLike(builder.lower(from.get("itemName")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(pn);
                        } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                            Predicate pn = builder.notLike(builder.lower(from.get("itemName")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(pn);
                        }
                    }/* else if (parameterCriteria.getField().equals("item.revision")) {
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
                    }*/ else if (parameterCriteria.getField().equals("item.description")) {
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
                        } else if (parameterCriteria.getOperator().equals("notequal")) {
                            Predicate p3 = builder.notEqual(builder.lower(from.get("description")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p3);
                        } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                            Predicate p3 = builder.notLike(builder.lower(from.get("description")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p3);
                        } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                            Predicate p3 = builder.notLike(builder.lower(from.get("description")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p3);
                        } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                            Predicate p3 = builder.notLike(builder.lower(from.get("description")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p3);
                        } else if (parameterCriteria.getOperator().equals("isempty")) {
                            predicates.add(builder.or(builder.isNull(from.get("description")), builder.equal(from.get("description"), "")));
                        } else if (parameterCriteria.getOperator().equals("isnotempty")) {
                            predicates.add(builder.and(builder.isNotNull(from.get("description")), builder.notEqual(from.get("description"), "")));
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
                        } else if (parameterCriteria.getOperator().equals("notequal")) {
                            Predicate p4 = builder.notEqual(builder.lower(from.get("itemType").get("name")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p4);
                        } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                            Predicate p4 = builder.notLike(builder.lower(from.get("itemType").get("name")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p4);
                        } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                            Predicate p4 = builder.notLike(builder.lower(from.get("itemType").get("name")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p4);
                        } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                            Predicate p4 = builder.notLike(builder.lower(from.get("itemType").get("name")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p4);
                        }
                    } else if (parameterCriteria.getField().equals("item.revision") || parameterCriteria.getField().equals("item.lifeCycle")) {
                        Predicate p8 = itemrevAdvancedCriteria.getItemPredicate(parameterCriteria, builder, cq, from);
                        predicates.add(p8);
                    }
                    if (!Criteria.isEmpty(parameterCriteria.getItemClass())) {
                        Predicate p1 = builder.equal(from.get("itemType").get("itemClass"), ItemClass.valueOf(parameterCriteria.getItemClass()));
                        predicates.add(p1);
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
                } else if (parameterCriteria.getField().contains("reference.")) {
                    Predicate p7 = referenceAvancedCriteria.getReferencePredicate(parameterCriteria, builder, cq, from);
                    predicates.add(p7);
                }
            }
            cq.where(predicates.toArray(new Predicate[]{}));
            TypedQuery<PLMItem> typedQuery1 = entityManager.createQuery(cq);
            return typedQuery1;
        } else {
            return entityManager.createQuery(cq);
        }
    }
}
