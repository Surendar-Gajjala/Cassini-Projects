package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectAttribute;
import com.cassinisys.platform.model.custom.CustomObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import freemarker.core.CustomAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 02-08-2021.
 */

@Component
public class ObjectAttributeAdvancedCriteria {


    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    public Predicate getAttributePredicate(CustomParameterCriteria customParameterCriteria, CriteriaBuilder builder,
                                           CriteriaQuery cq, Root<CustomObject> from) {
        Path val1 = null;
        Subquery<Integer> attSq = cq.subquery(Integer.class);
        Root<CustomObjectAttribute> aSFrom = attSq.from(CustomObjectAttribute.class);
        attSq.select(aSFrom.get("id").get("objectId")).distinct(true);

        List<Predicate> attributePredicates = new ArrayList<>();
        List<Predicate> attributePredicates1 = new ArrayList<>();
        String attributeName = customParameterCriteria.getField().substring(customParameterCriteria.getField().lastIndexOf(".") + 1);
        ObjectTypeAttribute typeAttribute1 = objectTypeAttributeRepository.findOne(Integer.parseInt(customParameterCriteria.getAttributeId()));

        if (customParameterCriteria.getField().startsWith("attribute")) {
            Predicate predicate11 = builder.equal(aSFrom.get("id").get("attributeDef"), customParameterCriteria.getAttributeId());
            attributePredicates.add(predicate11);
            if (typeAttribute1.getDataType().toString().equals("DOUBLE")) {
                if (aSFrom.get("doubleValue") != null) {
                    if (customParameterCriteria.getOperator().equals("equals")) {
                        Predicate ap1 = builder.equal(aSFrom.get("doubleValue"), customParameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (customParameterCriteria.getOperator().equals("greterthan")) {
                        Predicate ap1 = builder.greaterThan(aSFrom.get("doubleValue"), customParameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (customParameterCriteria.getOperator().equals("lessthan")) {
                        Predicate ap1 = builder.lessThan(aSFrom.get("doubleValue"), customParameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (customParameterCriteria.getOperator().equals("greterthanorequals")) {
                        Predicate ap1 = builder.greaterThanOrEqualTo(aSFrom.get("doubleValue"), customParameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (customParameterCriteria.getOperator().equals("lessthanorequals")) {
                        Predicate ap1 = builder.lessThanOrEqualTo(aSFrom.get("doubleValue"), customParameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("STRING") || typeAttribute1.getDataType().toString().equals("TEXT")) {
                if (aSFrom.get("stringValue") != null) {
                    if (customParameterCriteria.getOperator().equals("contains")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("stringValue")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("startswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("stringValue")), customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("endswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("stringValue")), "%" + customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("equals")) {
                        Predicate pn = builder.equal(builder.lower(aSFrom.get("stringValue")), customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("notequal")) {
                        Predicate pn = builder.notEqual(builder.lower(aSFrom.get("stringValue")), customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotcontain")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("stringValue")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotstartwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("stringValue")), customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotendwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("stringValue")), "%" + customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("LIST")) {
                if (aSFrom.get("listValue") != null) {
                    if (customParameterCriteria.getOperator().equals("contains")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("listValue")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("startswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("listValue")), customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("endswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("listValue")), "%" + customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("equals")) {
                        Predicate pn = builder.equal(builder.lower(aSFrom.get("listValue")), customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("notequal")) {
                        Predicate pn = builder.notEqual(builder.lower(aSFrom.get("listValue")), customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotcontain")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("listValue")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotstartwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("listValue")), customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotendwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("listValue")), "%" + customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("INTEGER")) {
                if (aSFrom.get("integerValue") != null) {
                    if (customParameterCriteria.getOperator().equals("equals")) {
                        Predicate ap1 = builder.equal(aSFrom.get("integerValue"), customParameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (customParameterCriteria.getOperator().equals("greterthan")) {
                        Predicate ap1 = builder.greaterThan(aSFrom.get("integerValue"), customParameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (customParameterCriteria.getOperator().equals("lessthan")) {
                        Predicate ap1 = builder.lessThan(aSFrom.get("integerValue"), customParameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (customParameterCriteria.getOperator().equals("greterthanorequals")) {
                        Predicate ap1 = builder.greaterThanOrEqualTo(aSFrom.get("integerValue"), customParameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    } else if (customParameterCriteria.getOperator().equals("lessthanorequals")) {
                        Predicate ap1 = builder.lessThanOrEqualTo(aSFrom.get("integerValue"), customParameterCriteria.getValue());
                        attributePredicates.add(ap1);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("BOOLEAN")) {
                if (aSFrom.get("booleanValue") != null) {
                    if (customParameterCriteria.getOperator().equals("equals")) {
                        Predicate ap1 = builder.equal(aSFrom.get("booleanValue"), Boolean.parseBoolean(customParameterCriteria.getValue()));
                        attributePredicates.add(ap1);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("LONGTEXT")) {
                if (aSFrom.get("longTextValue") != null) {
                    if (customParameterCriteria.getOperator().equals("contains")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("longTextValue")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("startswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("longTextValue")), customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("endswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("longTextValue")), "%" + customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("equals")) {
                        Predicate pn = builder.equal(builder.lower(aSFrom.get("longTextValue")), customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("notequal")) {
                        Predicate pn = builder.notEqual(builder.lower(aSFrom.get("longTextValue")), customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotcontain")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("longTextValue")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotstartwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("longTextValue")), customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotendwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("longTextValue")), "%" + customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    }
                }
            }
            if (typeAttribute1.getDataType().toString().equals("RICHTEXT")) {
                if (aSFrom.get("richTextValue") != null) {
                    if (customParameterCriteria.getOperator().equals("contains")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("richTextValue")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("startswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("richTextValue")), customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("endswith")) {
                        Predicate pn = builder.like(builder.lower(aSFrom.get("richTextValue")), "%" + customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("equals")) {
                        Predicate pn = builder.equal(builder.lower(aSFrom.get("richTextValue")), customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("notequal")) {
                        Predicate pn = builder.notEqual(builder.lower(aSFrom.get("richTextValue")), customParameterCriteria.getValue().toLowerCase());
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotcontain")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("richTextValue")), "%" + customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotstartwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("richTextValue")), customParameterCriteria.getValue().toLowerCase() + "%");
                        attributePredicates.add(pn);
                    } else if (customParameterCriteria.getOperator().equals("doesnotendwith")) {
                        Predicate pn = builder.notLike(builder.lower(aSFrom.get("richTextValue")), "%" + customParameterCriteria.getValue().toLowerCase());
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
