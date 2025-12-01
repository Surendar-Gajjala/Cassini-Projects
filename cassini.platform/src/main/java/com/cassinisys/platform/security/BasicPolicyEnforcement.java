package com.cassinisys.platform.security;

import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.model.dto.CriteriaDto;
import com.cassinisys.platform.model.dto.ObjectAttributeDto;
import com.cassinisys.platform.model.security.LoginSecurityPermission;
import com.cassinisys.platform.model.security.PrivilegeType;
import com.cassinisys.platform.model.security.SecurityPermission;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.security.SecurityService;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BasicPolicyEnforcement implements PolicyEnforcement {
    private static final Logger logger = LoggerFactory.getLogger(BasicPolicyEnforcement.class);
    ExpressionParser parser = new SpelExpressionParser();
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private CriteriaDto criteriaDto;
    @Autowired
    private SecurityService securityService;

    @Transactional(readOnly = true)
    public boolean checkObject(Login subject, Object object, Object action, Object environment) {
        boolean flag = false;
        if (object instanceof Integer) object = objectRepository.findById((Integer) object);
        List<SecurityPermission> allRules = subject.getGroupSecurityPermissions();
        if (subject.getLoginSecurityPermissions().size() > 0)
            allRules.addAll(convertLoginPermissionToSecurityPermission(subject.getLoginSecurityPermissions()));
        allRules.addAll(addItemRevisionRules(allRules, subject));
        SecurityAccessContext cxt = new SecurityAccessContext(subject, object, action, environment);
        List<SecurityPermission> deniedRules = new ArrayList<>();
        List<SecurityPermission> grantedRules = new ArrayList<>();
        deniedRules.addAll(allRules.stream().filter(rule -> rule.getPrivilegeType().equals(PrivilegeType.DENIED)
                && (rule.getAttributeGroup() == null && rule.getAttribute() == null)).collect(Collectors.toList()));
        grantedRules.addAll(allRules.stream().filter(rule -> rule.getPrivilegeType().equals(PrivilegeType.GRANTED)
                && (rule.getAttribute() == null && rule.getAttributeGroup() == null)).collect(Collectors.toList()));
        if (grantedRules.size() == 0 && deniedRules.size() == 0) return true;
        if (grantedRules.size() == 0 && deniedRules.size() > 0) {
            flag = !deniedObjectFiltering(deniedRules, cxt);
        } else if (grantedRules.size() > 0 && deniedRules.size() == 0) {
            flag = grantedObjectFiltering(grantedRules, cxt);
        } else if (grantedRules.size() > 0 && deniedRules.size() > 0) {
            flag = !deniedObjectFiltering(deniedRules, cxt) || grantedObjectFiltering(grantedRules, cxt);
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public boolean checkAttribute(Login subject, Integer objectId, Object attributeDef, Object action, Object environment) {
        boolean flag = false;
        List<SecurityPermission> allRules = subject.getGroupSecurityPermissions();
        if (subject.getLoginSecurityPermissions().size() > 0)
            allRules.addAll(convertLoginPermissionToSecurityPermission(subject.getLoginSecurityPermissions()));
        allRules.addAll(addItemRevisionRules(allRules, subject));
        CassiniObject cassiniObject = objectRepository.findById(objectId);
        ObjectTypeAttribute objectTypeAttribute;
        if (attributeDef instanceof Integer)
            objectTypeAttribute = objectTypeAttributeRepository.findOne((Integer) attributeDef);
        else objectTypeAttribute = (ObjectTypeAttribute) attributeDef;
        if (cassiniObject != null && objectTypeAttribute != null) {
            ObjectAttributeDto object = new ObjectAttributeDto();
            object.setCassiniObject(cassiniObject);
            object.setObjectTypeAttribute(objectTypeAttribute);
            SecurityAccessContext cxt = new SecurityAccessContext(subject, object, action, environment);
            List<SecurityPermission> deniedRules = new ArrayList<>();
            List<SecurityPermission> grantedRules = new ArrayList<>();
            deniedRules.addAll(allRules.stream().filter(rule -> rule.getPrivilegeType().equals(PrivilegeType.DENIED)
                    && (rule.getAttributeGroup() != null || rule.getAttribute() != null)).collect(Collectors.toList()));
            grantedRules.addAll(allRules.stream().filter(rule -> rule.getPrivilegeType().equals(PrivilegeType.GRANTED)
                    && (rule.getAttribute() != null || rule.getAttributeGroup() != null)).collect(Collectors.toList()));
            if (deniedRules.size() == 0 && grantedRules.size() == 0) {
                String objectType = cassiniObject.getObjectType().name();
                for (SecurityPermission rule : allRules) {
                    if (rule.getObjectType().equalsIgnoreCase(objectType) &&
                            (rule.getPrivilege().equalsIgnoreCase(action.toString()) || rule.getPrivilege().equals("all")))
                        return true;
                }
            } else {
                if (grantedRules.size() == 0 && deniedRules.size() > 0) {
                    flag = !deniedAttributeFiltering(deniedRules, cxt);
                } else if (grantedRules.size() > 0 && deniedRules.size() == 0) {
                    flag = grantedAttributeFiltering(grantedRules, cxt);
                } else if (grantedRules.size() > 0 && deniedRules.size() > 0) {
                    if (!deniedAttributeFiltering(deniedRules, cxt) || grantedAttributeFiltering(grantedRules, cxt)) flag = true;
                }
            }
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public boolean deniedObjectFiltering(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        boolean flag = false;
        List<SecurityPermission> criteriaList = new ArrayList<>();
        List<SecurityPermission> nonCriteriaList = new ArrayList<>();
        List<SecurityPermission> filterObjectTypes = filterObjectTypes(rules, cxt, EvaluationConstraints.OBJECT);
        if (filterObjectTypes.size() > 0) {
            List<SecurityPermission> filterSubTypes = filterSubType(filterObjectTypes, cxt, EvaluationConstraints.OBJECT);
            if (filterSubTypes.size() > 0) {
                List<SecurityPermission> filerPrivileges = filterPrivileges(filterSubTypes, cxt.getAction());
                if (filerPrivileges.size() > 0) {
                    criteriaList.addAll(filerPrivileges.stream().filter(permission -> permission.getCriteria() != null).collect(Collectors.toList()));
                    nonCriteriaList.addAll(filerPrivileges.stream().filter(permission -> permission.getCriteria() == null).collect(Collectors.toList()));
                    flag = nonCriteriaList.size() != 0 || filterCriteria(criteriaList, cxt).size() > 0;
                }
            }
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public boolean grantedObjectFiltering(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        boolean flag = false;
        List<SecurityPermission> criteriaList = new ArrayList<>();
        List<SecurityPermission> nonCriteriaList = new ArrayList<>();
        List<SecurityPermission> filterObjectTypes = filterObjectTypes(rules, cxt, EvaluationConstraints.OBJECT);
        if (filterObjectTypes.size() > 0) {
            List<SecurityPermission> filterSubTypes = filterSubType(filterObjectTypes, cxt, EvaluationConstraints.OBJECT);
            if (filterSubTypes.size() > 0) {
                List<SecurityPermission> filterPrivileges = filterPrivileges(filterSubTypes, cxt.getAction());
                if (filterPrivileges.size() > 0) {
                    criteriaList.addAll(filterPrivileges.stream().filter(permission -> permission.getCriteria() != null).collect(Collectors.toList()));
                    nonCriteriaList.addAll(filterPrivileges.stream().filter(permission -> permission.getCriteria() == null).collect(Collectors.toList()));
                    flag = nonCriteriaList.size() != 0 || filterCriteria(criteriaList, cxt).size() > 0;
                }

            }
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public boolean deniedAttributeFiltering(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        boolean flag = false;
        List<SecurityPermission> criteriaList = new ArrayList<>();
        List<SecurityPermission> nonCriteriaList = new ArrayList<>();
        List<SecurityPermission> filerAttributeGroups = filterAttributesGroup(rules, cxt);
        if (filerAttributeGroups.size() > 0) {
            List<SecurityPermission> filterAttributes = filterAttributes(filerAttributeGroups, cxt);
            if (filterAttributes.size() > 0) {
                List<SecurityPermission> filterObjectTypes = filterObjectTypes(filterAttributes, cxt, EvaluationConstraints.ATTRIBUTE);
                if (filterObjectTypes.size() > 0) {
                    List<SecurityPermission> filterSubTypes = filterSubType(filterObjectTypes, cxt, EvaluationConstraints.ATTRIBUTE);
                    if (filterSubTypes.size() > 0) {
                        List<SecurityPermission> filerPrivileges = filterPrivileges(filterSubTypes, cxt.getAction());
                        if (filerPrivileges.size() > 0) {
                            criteriaList.addAll(filerPrivileges.stream().filter(permission -> permission.getCriteria() != null).collect(Collectors.toList()));
                            nonCriteriaList.addAll(filerPrivileges.stream().filter(permission -> permission.getCriteria() == null).collect(Collectors.toList()));
                            flag = nonCriteriaList.size() != 0 || filterCriteria(criteriaList, cxt).size() > 0;
                        }

                    }
                }
            }
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public boolean grantedAttributeFiltering(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        List<SecurityPermission> criteriaList = new ArrayList<>();
        List<SecurityPermission> nonCriteriaList = new ArrayList<>();
        boolean flag = false;
        List<SecurityPermission> filterAttributeGroups = filterAttributesGroup(rules, cxt);
        if (filterAttributeGroups.size() > 0) {
            List<SecurityPermission> filterAttributes = filterAttributes(filterAttributeGroups, cxt);
            if (filterAttributes.size() > 0) {
                List<SecurityPermission> filterObjectTypes = filterObjectTypes(filterAttributes, cxt, EvaluationConstraints.ATTRIBUTE);
                if (filterObjectTypes.size() > 0) {
                    List<SecurityPermission> filterSubTypes = filterSubType(filterObjectTypes, cxt, EvaluationConstraints.ATTRIBUTE);
                    if (filterSubTypes.size() > 0) {
                        List<SecurityPermission> filterPrivileges = filterPrivileges(filterSubTypes, cxt.getAction());
                        if (filterPrivileges.size() > 0) {
                            criteriaList.addAll(filterPrivileges.stream().filter(permission -> permission.getCriteria() != null).collect(Collectors.toList()));
                            nonCriteriaList.addAll(filterPrivileges.stream().filter(permission -> permission.getCriteria() == null).collect(Collectors.toList()));
                                flag = nonCriteriaList.size() != 0 || filterCriteria(criteriaList, cxt).size() > 0;
                        }
                    }
                }
            }
        }
        return flag;
    }

    protected List<SecurityPermission> filterObjectTypes(List<SecurityPermission> rules, SecurityAccessContext cxt, String type) {
        List<SecurityPermission> matchedRules = new ArrayList<>();
        String expression = null;
        for (SecurityPermission rule : rules) {
            try {
                if (rule.getObjectType().equalsIgnoreCase(EvaluationConstraints.ANY)) {
                    matchedRules.add(rule);
                } else if(checkPDMObject(cxt, rule)) {
                    matchedRules.add(rule);
                } else {
                    if (type.equals(EvaluationConstraints.OBJECT)) {
                        expression = "object.objectType.toString().equalsIgnoreCase(" + "\"" + rule.getObjectType() + "\"" + ")";
                    }
                    if (type.equals(EvaluationConstraints.ATTRIBUTE)) {
                        expression = "object.cassiniObject.objectType.toString().equalsIgnoreCase(" + "\"" + rule.getObjectType() + "\"" + ")";
                    }
                    if (parser.parseExpression(expression).getValue(cxt, Boolean.class) || checkTypeByParentType(cxt, rule)) {
                        matchedRules.add(rule);
                    }
                }
            } catch (Exception ignored) {
                logger.info("An error occurred while evaluating PolicyRule.", ignored);
            }
        }
        return matchedRules;
    }

    protected List<SecurityPermission> filterSubType(List<SecurityPermission> rules, SecurityAccessContext cxt, String type) {
        List<SecurityPermission> matchedRules = new ArrayList<>();
        Boolean savedSearchFlag = parser.parseExpression("object instanceof T(com.cassinisys.plm.model.plm.PLMSavedSearch)").getValue(cxt, Boolean.class);
        for (SecurityPermission rule : rules) {
            try {
                if (rule.getSubType() != null && !savedSearchFlag) {
                    if (rule.getSubType().contains(",")) {
                        String[] objectClass = rule.getSubType().split(",");
                        for (String className : objectClass) {
                            if (objectTypeService.getTypeSystem(getTypeNameByObject(rule.getObjectType()))
                                    .isSubtypeOf(className, getTypeByObject(rule.getObjectType(), cxt, type), rule.getSubTypeId()))
                                matchedRules.add(rule);
                        }
                    } else {
                        if (objectTypeService.getTypeSystem(getTypeNameByObject(rule.getObjectType()))
                                .isSubtypeOf(rule.getSubType(), getTypeByObject(rule.getObjectType(), cxt, type), rule.getSubTypeId()))
                            matchedRules.add(rule);
                    }
                } else matchedRules.add(rule);
            } catch (Exception ignored) {
                logger.info("An error occurred while evaluating PolicyRule.", ignored);
            }
        }
        return matchedRules;
    }


    protected List<SecurityPermission> filterPrivileges(List<SecurityPermission> rules, Object action) {
        List<SecurityPermission> matchedRules = new ArrayList<>();
        if (action.toString().contains(",")) {
            String[] actions = action.toString().split(",");
            for (String action1 : actions) {
                for (SecurityPermission rule : rules) {
                    try {
                        if (rule.getPrivilege() != null) {
                            if (rule.getPrivilege().contains(",")) {
                                String[] privileges = rule.getPrivilege().split(",");
                                for (String privilege : privileges)
                                    if (privilege.equals(action1)) matchedRules.add(rule);
                            } else if (rule.getPrivilege().equals(action1) || rule.getPrivilege().equals("all"))
                                matchedRules.add(rule);
                        } else {
                            matchedRules.add(rule);
                        }
                    } catch (Exception ignored) {
                        logger.info("An error occurred while evaluating PolicyRule.", ignored);
                    }
                }
            }
        } else {
            for (SecurityPermission rule : rules) {
                try {
                    if (rule.getPrivilege() != null) {
                        if (rule.getPrivilege().contains(",")) {
                            String[] privilages = rule.getPrivilege().split(",");
                            for (String privilage : privilages) if (privilage.equals(action)) matchedRules.add(rule);
                        } else {
                            if (rule.getPrivilege().equals(action) || rule.getPrivilege().equals("all"))
                                matchedRules.add(rule);
                        }
                    } else matchedRules.add(rule);
                } catch (Exception ignored) {
                    logger.info("An error occurred while evaluating PolicyRule.", ignored);
                }
            }
        }
        return matchedRules;
    }

    protected List<SecurityPermission> filterAttributesGroup(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        List<SecurityPermission> matchedRules = new ArrayList<>();
        for (SecurityPermission rule : rules) {
            try {
                if (rule.getAttributeGroup() != null) {
                    if (rule.getAttributeGroup().contains(",")) {
                        String[] attributes = rule.getAttributeGroup().split(",");
                        for (String attribute : attributes) {
                            String objectAttribute = parser.parseExpression("object.objectTypeAttribute.attributeGroup").getValue(cxt, String.class).trim();
                            if (objectAttribute.equalsIgnoreCase(attribute.trim())) matchedRules.add(rule);
                        }
                    } else {
                        String objectAttribute = parser.parseExpression("object.objectTypeAttribute.attributeGroup").getValue(cxt, String.class).trim();
                        if (objectAttribute.equalsIgnoreCase(rule.getAttributeGroup().trim())) matchedRules.add(rule);
                    }
                } else {
                    matchedRules.add(rule);
                }
            } catch (Exception e) {
                logger.info("An error occurred while evaluating PolicyRule.", e);
            }
        }
        return matchedRules;
    }

    protected List<SecurityPermission> filterAttributes(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        List<SecurityPermission> matchedRules = new ArrayList<>();
        for (SecurityPermission rule : rules) {
            try {
                if (rule.getAttribute() != null) {
                    if (rule.getAttribute().contains(",")) {
                        String[] attributes = rule.getAttribute().split(",");
                        for (String attribute : attributes) {
                            String objectAttribute = parser.parseExpression("object.objectTypeAttribute.name").getValue(cxt, String.class).trim();
                            if (objectAttribute.equalsIgnoreCase(attribute.trim())) matchedRules.add(rule);
                        }
                    } else {
                        String objectAttribute = parser.parseExpression("object.objectTypeAttribute.name").getValue(cxt, String.class).trim();
                        if (objectAttribute.equalsIgnoreCase(rule.getAttribute().trim())) matchedRules.add(rule);
                    }
                } else {
                    matchedRules.add(rule);
                }
            } catch (Exception e) {
                logger.info("An error occurred while evaluating PolicyRule.", e);
            }
        }
        return matchedRules;
    }

    protected List<SecurityPermission> filterCriteria(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        List<SecurityPermission> matchedRules = new ArrayList<>();
        String expression;
        for (SecurityPermission rule : rules) {
            try {
                if (rule.getCriteria() != null) {
                    if (rule.getPerson() != null) expression = rule.getCriteria();
                    else if (rule.getIsExternal()) expression = rule.getCriteria();
                    else expression = criteriaDto.getSpel(rule.getCriteria());
                    if (expression.contains("attributesMap")) {
                        CassiniObject cassiniObject = (CassiniObject) getTypeByObject(rule.getObjectType(), cxt, EvaluationConstraints.OBJECT);
                        if (cassiniObject != null) {
                            List objectTypeAttributes;
                            if (getTypeNameByObject(rule.getObjectType()) != null) {
                                objectTypeAttributes = objectTypeService.getTypeSystem(getTypeNameByObject(rule.getObjectType())).getTypeAttributes(cassiniObject.getId(), true);
                            } else {
                                String objectType1 = (parser.parseExpression(EvaluationConstraints.OBJECTTYPE).getValue(cxt.getObject(), Enum.class)).toString();
                                objectTypeAttributes = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType1));
                            }
                            Integer objectId = parser.parseExpression(EvaluationConstraints.ID).getValue(cxt.getObject(), Integer.class);
                            if (cxt.getObject() instanceof CassiniObject) {
                                CassiniObject cassiniObject1 = (CassiniObject) cxt.getObject();
                                cassiniObject1.getAttributesMap().putAll(getAttributeTypeValue(objectTypeAttributes, objectId, cxt));
                                if (cassiniObject1.getAttributesMap().size() > 0) {
                                    cxt.setObject(cassiniObject1);
                                    if (parser.parseExpression(expression).getValue(cxt, Boolean.class))
                                        matchedRules.add(rule);
                                } /*else {
                                    matchedRules.add(rule);
                                }*/
                            }
                        }
                    } else {
                        if (parser.parseExpression(expression).getValue(cxt, Boolean.class)) matchedRules.add(rule);
                    }
                } else matchedRules.add(rule);
            } catch (Exception ignored) {
                logger.info("An error occurred while evaluating PolicyRule.", ignored);
            }
        }
        return matchedRules;
    }

    private Object getTypeByObject(String objectName, SecurityAccessContext cxt, String objectType) {
        Object type = null;
        if (objectType.equals(EvaluationConstraints.OBJECT)) {
            if (objectName.equals(EvaluationConstraints.ITEMREVISION)) {
                Integer itemMaster = parser.parseExpression("object.itemMaster").getValue(cxt, Integer.class);
                CassiniObject cassiniObject = objectRepository.findById(itemMaster);
                type = parser.parseExpression("itemType").getValue(cassiniObject, Object.class);
            }
            if (objectName.equals(EvaluationConstraints.ITEM))
                type = parser.parseExpression("object.itemType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.CHANGE))
                type = parser.parseExpression("object.changeClass").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.PRODUCTIONIP) || objectName.equals(EvaluationConstraints.MATERIALIP))
                type = parser.parseExpression("object.planType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.NCR))
                type = parser.parseExpression("object.ncrType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.PR))
                type = parser.parseExpression("object.prType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.QCR))
                type = parser.parseExpression("object.qcrType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.MANUFACTURER))
                type = parser.parseExpression("object.mfrType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.MANUFACTURERPART))
                type = parser.parseExpression("object.mfrPartType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.MFRSUPPLIER))
                type = parser.parseExpression("object.supplierType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.PLMWORKFLOWDEFINITION))
                type = parser.parseExpression("object.workflowType").getValue(cxt, Object.class);
            if (type == null) type = parser.parseExpression("object.type").getValue(cxt, Object.class);
        }
        if (objectType.equals(EvaluationConstraints.ATTRIBUTE)) {
            if (objectName.equals(EvaluationConstraints.ITEMREVISION)) {
                Integer itemMaster = parser.parseExpression("object.cassiniObject.itemMaster").getValue(cxt, Integer.class);
                CassiniObject cassiniObject = objectRepository.findById(itemMaster);
                type = parser.parseExpression("itemType").getValue(cassiniObject, Object.class);
            }
            if (objectName.equals(EvaluationConstraints.ITEM))
                type = parser.parseExpression("object.cassiniObject.itemType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.CHANGE))
                type = parser.parseExpression("object.cassiniObject.changeClass").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.PRODUCTIONIP) || objectName.equals(EvaluationConstraints.MATERIALIP))
                type = parser.parseExpression("object.cassiniObject.planType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.NCR))
                type = parser.parseExpression("object.cassiniObject.ncrType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.PR))
                type = parser.parseExpression("object.cassiniObject.prType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.QCR))
                type = parser.parseExpression("object.cassiniObject.qcrType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.MANUFACTURER))
                type = parser.parseExpression("object.cassiniObject.mfrType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.MANUFACTURERPART))
                type = parser.parseExpression("object.cassiniObject.mfrPartType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.MFRSUPPLIER))
                type = parser.parseExpression("object.cassiniObject.supplierType").getValue(cxt, Object.class);
            if (objectName.equals(EvaluationConstraints.PLMWORKFLOWDEFINITION))
                type = parser.parseExpression("object.cassiniObject.workflowType").getValue(cxt, Object.class);
            if (type == null) type = parser.parseExpression("object.cassiniObject.type").getValue(cxt, Object.class);
        }
        return type;
    }

    private String getTypeNameByObject(String objectName) {
        if (objectName.equals(EvaluationConstraints.ITEM) || objectName.equals(EvaluationConstraints.ITEMREVISION))
            return EvaluationConstraints.ITEMTYPE;
        if (objectName.equals(EvaluationConstraints.CHANGE)) return EvaluationConstraints.CHANGETYPE;
        if (objectName.equals(EvaluationConstraints.MANUFACTURER)) return EvaluationConstraints.MANUFACTURERTYPE;
        if (objectName.equals(EvaluationConstraints.MANUFACTURERPART))
            return EvaluationConstraints.MANUFACTURERPARTTYPE;
        if (objectName.equals(EvaluationConstraints.MFRSUPPLIER)) return EvaluationConstraints.SUPPLIERTYPE;
        if (objectName.equals(EvaluationConstraints.PLMWORKFLOWDEFINITION)) return EvaluationConstraints.WFTYPE;
        if (objectName.equals(EvaluationConstraints.CUSTOMOBJECT)) return EvaluationConstraints.CUSTOMOBJECTTYPE;
        if (objectName.equals(EvaluationConstraints.REQUIRMENT) || objectName.equals(EvaluationConstraints.REQUIRMENTDOCUMENT))
            return EvaluationConstraints.REQTYPE;
        if (objectName.equals(EvaluationConstraints.PGCDECLARATION) || objectName.equals(EvaluationConstraints.PGCSPECIFICATION)
                || objectName.equals(EvaluationConstraints.PGCSUBSTANCE)) return EvaluationConstraints.PGCTYPE;
        if (objectName.equals(EvaluationConstraints.PRODUCTIONIP) || objectName.equals(EvaluationConstraints.MATERIALIP)
                || objectName.equals(EvaluationConstraints.NCR) || objectName.equals(EvaluationConstraints.PR) || objectName.equals(EvaluationConstraints.QCR))
            return EvaluationConstraints.QUALITYTYPE;
        if (objectName.equals(EvaluationConstraints.MROASSET) || objectName.equals(EvaluationConstraints.MROMETER)
                || objectName.equals(EvaluationConstraints.MROSPAREPART) || objectName.equals(EvaluationConstraints.MROWORKORDER) || objectName.equals(EvaluationConstraints.MROWORKREQUEST))
            return EvaluationConstraints.MROTYPE;
        if (objectName.equals(EvaluationConstraints.PLANT) || objectName.equals(EvaluationConstraints.WORKCENTER)
                || objectName.equals(EvaluationConstraints.MACHINE) || objectName.equals(EvaluationConstraints.EQUIPMENT) || objectName.equals(EvaluationConstraints.INSTRUMENT)
                || objectName.equals(EvaluationConstraints.TOOL) || objectName.equals(EvaluationConstraints.JIGFIXTURE) || objectName.equals(EvaluationConstraints.MATERIAL)
                || objectName.equals(EvaluationConstraints.MANPOWER) || objectName.equals(EvaluationConstraints.OPERATION) || objectName.equals(EvaluationConstraints.PRODUCTIONORDER))
            return EvaluationConstraints.MESTYPE;
        return null;
    }

    private Map<String, Object> getAttributeTypeValue(List<ObjectTypeAttribute> objectTypeAttributes, Integer objectId, SecurityAccessContext cxt) {
        Map<String, Object> objectMap = new HashMap<>();
        for (ObjectTypeAttribute attrDef : objectTypeAttributes) {
            ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(objectId, attrDef.getId());
            if (objectAttribute != null) {
                if (attrDef.getDataType().equals(DataType.BOOLEAN))
                    objectMap.put(attrDef.getName(), objectAttribute.getBooleanValue());
                if (attrDef.getDataType().equals(DataType.TEXT) && objectAttribute.getStringValue() != null)
                    objectMap.put(attrDef.getName(), objectAttribute.getStringValue());
                if (attrDef.getDataType().equals(DataType.INTEGER) && objectAttribute.getIntegerValue() != null)
                    objectMap.put(attrDef.getName(), objectAttribute.getIntegerValue());
                if (attrDef.getDataType().equals(DataType.DOUBLE) && objectAttribute.getDoubleValue() != null)
                    objectMap.put(attrDef.getName(), objectAttribute.getDoubleValue());
                if (attrDef.getDataType().equals(DataType.DATE) && objectAttribute.getDateValue() != null)
                    objectMap.put(attrDef.getName(), objectAttribute.getDateValue());
                if (attrDef.getDataType().equals(DataType.TIME) && objectAttribute.getTimeValue() != null)
                    objectMap.put(attrDef.getName(), objectAttribute.getTimeValue());
                if (attrDef.getDataType().equals(DataType.CURRENCY) && objectAttribute.getCurrencyValue() != null)
                    objectMap.put(attrDef.getName(), objectAttribute.getCurrencyValue());
                if (attrDef.getDataType().equals(DataType.LONGTEXT) && objectAttribute.getLongTextValue() != null)
                    objectMap.put(attrDef.getName(), objectAttribute.getLongTextValue());
                if (attrDef.getDataType().equals(DataType.RICHTEXT) && objectAttribute.getRichTextValue() != null)
                    objectMap.put(attrDef.getName(), objectAttribute.getRichTextValue());
                if (attrDef.getDataType().equals(DataType.HYPERLINK) && objectAttribute.getHyperLinkValue() != null)
                    objectMap.put(attrDef.getName(), objectAttribute.getHyperLinkValue());
                if (attrDef.getDataType().equals(DataType.OBJECT) && objectAttribute.getRefValue() != null) {
                    CassiniObject object = objectRepository.findById(objectAttribute.getRefValue());
                    List objectTypeAttributes1;
                    if (getTypeNameByObject(object.getObjectType().toString()) != null) {
                        objectTypeAttributes1 = objectTypeService.getTypeSystem(getTypeNameByObject(object.getObjectType().toString())).getTypeAttributes(object.getId(), true);
                    } else {
                        objectTypeAttributes1 = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(object.getObjectType().toString()));
                    }
                    if (objectTypeAttributes1 != null)
                        object.getAttributesMap().putAll(getAttributeTypeValue(objectTypeAttributes1, objectId, cxt));
                    objectMap.put(attrDef.getName(), object);
                }
            }
        }
        return objectMap;
    }

    @Transactional(readOnly = true)
    public List<SecurityPermission> convertLoginPermissionToSecurityPermission(List<LoginSecurityPermission> loginSecurityPermissions) {
        List<SecurityPermission> securityPermissions = new ArrayList<>();
        loginSecurityPermissions.forEach(loginSecurityPermission -> {
            SecurityPermission securityPermission = new SecurityPermission();
            securityPermission.setObjectType(loginSecurityPermission.getObjectType());
            securityPermission.setSubType(loginSecurityPermission.getSubType());
            securityPermission.setName(loginSecurityPermission.getName());
            securityPermission.setDescription(loginSecurityPermission.getDescription());
            securityPermission.setAttribute(loginSecurityPermission.getAttribute());
            securityPermission.setCriteria(loginSecurityPermission.getCriteria());
            securityPermission.setPrivilegeType(loginSecurityPermission.getPrivilegeType());
            securityPermission.setPrivilege(loginSecurityPermission.getPrivilege());
            securityPermission.setPerson(loginSecurityPermission.getPerson().getFullName());
            securityPermissions.add(securityPermission);
        });
        return securityPermissions;
    }

    public boolean checkTypeByParentType(SecurityAccessContext cxt, SecurityPermission rule) {
        if (rule.getObjectType().equalsIgnoreCase(EvaluationConstraints.CHANGETYPE)) {
            String type1 = parser.parseExpression("object.objectType.toString()").getValue(cxt, String.class);
            if (type1.equalsIgnoreCase(EvaluationConstraints.ECOTYPE) || type1.equalsIgnoreCase(EvaluationConstraints.ECRTYPE)
                    || type1.equalsIgnoreCase(EvaluationConstraints.DCOTYPE) || type1.equalsIgnoreCase(EvaluationConstraints.DCRTYPE)
                    || type1.equalsIgnoreCase(EvaluationConstraints.MCOTYPE) || type1.equalsIgnoreCase(EvaluationConstraints.DEVIATIONTYPE)
                    || type1.equalsIgnoreCase(EvaluationConstraints.WAIVERTYPE)) return true;
        }
        if (rule.getObjectType().equalsIgnoreCase(EvaluationConstraints.REQUIREMENTTYPE)) {
            String type1 = parser.parseExpression("object.objectType.toString()").getValue(cxt, String.class);
            if (type1.equalsIgnoreCase(EvaluationConstraints.REQUIREMENTDOCUMENTTYPE))
                return true;
        }
        if (rule.getObjectType().equalsIgnoreCase(EvaluationConstraints.MESTYPE)) {
            String type1 = parser.parseExpression("object.objectType.toString()").getValue(cxt, String.class);
            if (type1.equalsIgnoreCase(EvaluationConstraints.PLANTTYPE) || type1.equalsIgnoreCase(EvaluationConstraints.WORKCENTERTYPE)
                    || type1.equalsIgnoreCase(EvaluationConstraints.MACHINETYPE) || type1.equalsIgnoreCase(EvaluationConstraints.EQUIPMENTTYPE)
                    || type1.equalsIgnoreCase(EvaluationConstraints.INSTRUMENTTYPE) || type1.equalsIgnoreCase(EvaluationConstraints.TOOLTYPE)
                    || type1.equalsIgnoreCase(EvaluationConstraints.JIGFIXTURETYPE) || type1.equalsIgnoreCase(EvaluationConstraints.MATERIALTYPE)
                    || type1.equalsIgnoreCase(EvaluationConstraints.MANPOWERTYPE)) return true;
        }
        if (rule.getObjectType().equalsIgnoreCase(EvaluationConstraints.MROTYPE)) {
            String type1 = parser.parseExpression("object.objectType.toString()").getValue(cxt, String.class);
            if (type1.equalsIgnoreCase(EvaluationConstraints.ASSETTYPE) || type1.equalsIgnoreCase(EvaluationConstraints.SPAREPARTTYPE)
                    || type1.equalsIgnoreCase(EvaluationConstraints.METERTYPE) || type1.equalsIgnoreCase(EvaluationConstraints.WORKORDERTYPE)
                    || type1.equalsIgnoreCase(EvaluationConstraints.WORKREQUESTTYPE))
                return true;
        }
        if (rule.getObjectType().equalsIgnoreCase(EvaluationConstraints.PGCTYPE)) {
            String type1 = parser.parseExpression("object.objectType.toString()").getValue(cxt, String.class);
            if (type1.equalsIgnoreCase(EvaluationConstraints.PGCDECLARATIONTYPE) || type1.equalsIgnoreCase(EvaluationConstraints.PGCSUBSTANCETYPE)
                    || type1.equalsIgnoreCase(EvaluationConstraints.PGCSPECIFICATIONTYPE))
                return true;
        }
        return false;
    }

    public boolean checkPDMObject(SecurityAccessContext cxt, SecurityPermission rule) {
        if (rule.getObjectType().equalsIgnoreCase(EvaluationConstraints.PDM_OBJECT)) {
            String type1 = parser.parseExpression("object.objectType.toString()").getValue(cxt, String.class);
            if (type1.toLowerCase().contains(EvaluationConstraints.PDM)) return true;
        }
        return false;
    }

    public List<SecurityPermission> addItemRevisionRules(List<SecurityPermission> allRules, Login subject) {
        List<SecurityPermission> itemRevisionRules = new ArrayList<>();
        allRules.forEach(securityPermission1 -> {
            if (securityPermission1.getObjectType().equals(EvaluationConstraints.ITEM))
                itemRevisionRules.add(securityService.getSecurityPermission(EvaluationConstraints.ITEMREVISION, null,
                        securityPermission1.getPrivilege(), securityPermission1.getAttribute(), securityPermission1.getAttributeGroup(), null, subject.getExternal(), securityPermission1.getPrivilegeType()));
        });
        return itemRevisionRules;
    }


}