package com.cassinisys.plm.filtering;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 14-06-2016.
 */
@Component
public class AttributeAdvancedCriteria {

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    public Predicate getAttributePredicate(ParameterCriteria parameterCriteria, CriteriaBuilder builder,
                                           CriteriaQuery cq, Root<PLMItem> from) {
        Path val1 = null;
        Subquery<Integer> attSq = cq.subquery(Integer.class);
        Root<PLMItemAttribute> aSFrom = attSq.from(PLMItemAttribute.class);
        attSq.select(aSFrom.get("id").get("objectId")).distinct(true);

        List<Predicate> attributePredicates = new ArrayList<>();
        List<Predicate> attributePredicates1 = new ArrayList<>();
        String attributeName = parameterCriteria.getField().substring(parameterCriteria.getField().lastIndexOf(".") + 1);
        ObjectTypeAttribute typeAttribute1 = objectTypeAttributeRepository.findOne(Integer.parseInt(parameterCriteria.getAttributeId()));


        if (parameterCriteria.getField().startsWith("attribute")) {
            Predicate predicate11 = builder.equal(aSFrom.get("id").get("attributeDef"), parameterCriteria.getAttributeId());
            attributePredicates.add(predicate11);
            if (typeAttribute1.getDataType().toString().equals("DOUBLE")) {
                if (aSFrom.get("doubleValue") != null) {
                    if (parameterCriteria.getOperator().equals("equals")) {
                        Predicate ap1 = builder.equal(aSFrom.get("doubleValue"), parameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (parameterCriteria.getOperator().equals("greterthan")) {
                        Predicate ap1 = builder.greaterThan(aSFrom.get("doubleValue"), parameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (parameterCriteria.getOperator().equals("lessthan")) {
                        Predicate ap1 = builder.lessThan(aSFrom.get("doubleValue"), parameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (parameterCriteria.getOperator().equals("greterthanorequals")) {
                        Predicate ap1 = builder.greaterThanOrEqualTo(aSFrom.get("doubleValue"), parameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (parameterCriteria.getOperator().equals("lessthanorequals")) {
                        Predicate ap1 = builder.lessThanOrEqualTo(aSFrom.get("doubleValue"), parameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("STRING") || typeAttribute1.getDataType().toString().equals("TEXT")) {
                if (aSFrom.get("stringValue") != null) {
                    if (parameterCriteria.getOperator().equals("contains")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("stringValue")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("startswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("stringValue")), parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("endswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("stringValue")), "%" + parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("equals")) {
                        Predicate pn = builder.equal(builder.lower(aSFrom.get("stringValue")), parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("notequal")) {
                        Predicate pn = builder.notEqual(builder.lower(aSFrom.get("stringValue")), parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("stringValue")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("stringValue")), parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("stringValue")), "%" + parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("LIST")) {
                if (aSFrom.get("listValue") != null) {
                    if (parameterCriteria.getOperator().equals("contains")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("listValue")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("startswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("listValue")), parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("endswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("listValue")), "%" + parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("equals")) {
                        Predicate pn = builder.equal(builder.lower(aSFrom.get("listValue")), parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("notequal")) {
                        Predicate pn = builder.notEqual(builder.lower(aSFrom.get("listValue")), parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("listValue")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("listValue")), parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("listValue")), "%" + parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("INTEGER")) {
                if (aSFrom.get("integerValue") != null) {
                    if (parameterCriteria.getOperator().equals("equals")) {
                        Predicate ap1 = builder.equal(aSFrom.get("integerValue"), parameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (parameterCriteria.getOperator().equals("greterthan")) {
                        Predicate ap1 = builder.greaterThan(aSFrom.get("integerValue"), parameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (parameterCriteria.getOperator().equals("lessthan")) {
                        Predicate ap1 = builder.lessThan(aSFrom.get("integerValue"), parameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (parameterCriteria.getOperator().equals("greterthanorequals")) {
                        Predicate ap1 = builder.greaterThanOrEqualTo(aSFrom.get("integerValue"), parameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (parameterCriteria.getOperator().equals("lessthanorequals")) {
                        Predicate ap1 = builder.lessThanOrEqualTo(aSFrom.get("integerValue"), parameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("BOOLEAN")) {
                if (aSFrom.get("booleanValue") != null) {
                    if (parameterCriteria.getOperator().equals("equals")) {
                        Predicate ap1 = builder.equal(aSFrom.get("booleanValue"), Boolean.parseBoolean(parameterCriteria.getValue()));
                        attributePredicates.add(ap1);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("LONGTEXT")) {
                if (aSFrom.get("longTextValue") != null) {
                    if (parameterCriteria.getOperator().equals("contains")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("longTextValue")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("startswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("longTextValue")), parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("endswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("longTextValue")), "%" + parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("equals")) {
                        Predicate pn = builder.equal(builder.lower(aSFrom.get("longTextValue")), parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("notequal")) {
                        Predicate pn = builder.notEqual(builder.lower(aSFrom.get("longTextValue")), parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("longTextValue")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("longTextValue")), parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("longTextValue")), "%" + parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("RICHTEXT")) {
                if (aSFrom.get("richTextValue") != null) {
                    if (parameterCriteria.getOperator().equals("contains")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("richTextValue")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("startswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("richTextValue")), parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("endswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("richTextValue")), "%" + parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("equals")) {
                        Predicate pn = builder.equal(builder.lower(aSFrom.get("richTextValue")), parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("notequal")) {
                        Predicate pn = builder.notEqual(builder.lower(aSFrom.get("richTextValue")), parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("richTextValue")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("richTextValue")), parameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("richTextValue")), "%" + parameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    }
                }
            }
            attributePredicates1.add(builder.and(attributePredicates.toArray(new Predicate[]{})));
        }


        attSq.where(builder.or(attributePredicates1.toArray(new Predicate[]{})));
        Predicate p6 = builder.in(from.get("id")).value(attSq);
        return p6;
    }
}
