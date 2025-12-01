package com.cassinisys.plm.filtering;

import com.cassinisys.plm.model.cm.PLMECO;
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

@Component
public class ECOAdvancedCriteria {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ECOFileAdvancedCriteria ecoFileAdvancedCriteria;

    public TypedQuery<PLMECO> getECOTypeQuery(ParameterCriteria[] parameterCriterias) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PLMECO> cq = builder.createQuery(PLMECO.class);
        Root<PLMECO> from = cq.from(PLMECO.class);
        cq.select(from);
        List<Predicate> predicates = new ArrayList<>();
        if (parameterCriterias.length > 0) {
            for (ParameterCriteria parameterCriteria : parameterCriterias) {
                if (parameterCriteria.getField().contains("eco.")) {
                    if (parameterCriteria.getField().equals("eco.number")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate p1 = builder.like(builder.lower(from.get("ecoNumber")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p1);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate p1 = builder.like(builder.lower(from.get("ecoNumber")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p1);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate p1 = builder.like(builder.lower(from.get("ecoNumber")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p1);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate p1 = builder.equal(builder.lower(from.get("ecoNumber")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p1);
                        }
                    } else if (parameterCriteria.getField().equals("eco.status")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate p2 = builder.like(builder.lower(from.get("status")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p2);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate p2 = builder.like(builder.lower(from.get("status")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p2);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate p2 = builder.like(builder.lower(from.get("status")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p2);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate p2 = builder.equal(builder.lower(from.get("status")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p2);
                        }
                    } else if (parameterCriteria.getField().equals("eco.description")) {
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
                    } /*else if (parameterCriteria.getField().equals("eco.reasonForChange")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate p4 = builder.like(builder.lower(from.get("reasonForChange")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p4);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate p4 = builder.like(builder.lower(from.get("reasonForChange")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p4);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate p4 = builder.like(builder.lower(from.get("reasonForChange")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p4);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate p4 = builder.equal(builder.lower(from.get("reasonForChange")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p4);
                        }
                    }*/ else if (parameterCriteria.getField().equals("eco.title")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate p5 = builder.like(builder.lower(from.get("title")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p5);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate p5 = builder.like(builder.lower(from.get("title")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p5);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate p5 = builder.like(builder.lower(from.get("title")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p5);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate p5 = builder.equal(builder.lower(from.get("title")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p5);
                        }
                    } else if (parameterCriteria.getField().equals("eco.changeType")) {
                        if (parameterCriteria.getOperator().equals("contains")) {
                            Predicate p6 = builder.like(builder.lower(from.get("requestType")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p6);
                        } else if (parameterCriteria.getOperator().equals("startswith")) {
                            Predicate p6 = builder.like(builder.lower(from.get("requestType")), parameterCriteria.getValue().toLowerCase() + "%");
                            predicates.add(p6);
                        } else if (parameterCriteria.getOperator().equals("endswith")) {
                            Predicate p6 = builder.like(builder.lower(from.get("requestType")), "%" + parameterCriteria.getValue().toLowerCase());
                            predicates.add(p6);
                        } else if (parameterCriteria.getOperator().equals("equals")) {
                            Predicate p6 = builder.equal(builder.lower(from.get("requestType")), parameterCriteria.getValue().toLowerCase());
                            predicates.add(p6);
                        }
                    }
                } else if (parameterCriteria.getField().contains("ecoFile.")) {
                    Predicate ep5 = ecoFileAdvancedCriteria.getECOFilePredicate(parameterCriteria, builder, cq, from);
                    predicates.add(ep5);
                }
            }
            cq.where(predicates.toArray(new Predicate[]{}));
            TypedQuery<PLMECO> typedQuery1 = entityManager.createQuery(cq);
            return typedQuery1;
        } else {
            return entityManager.createQuery(cq);
        }
    }
}
