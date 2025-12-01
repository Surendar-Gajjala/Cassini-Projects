package com.cassinisys.platform.security;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.model.dto.ObjectAttributeDto;
import com.cassinisys.platform.model.security.PrivilegeType;
import com.cassinisys.platform.model.security.SecurityPermission;
import com.cassinisys.platform.repo.core.ObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomePrivilegeFilter {
    private static final Logger logger = LoggerFactory.getLogger(CustomePrivilegeFilter.class);
    ExpressionParser parser = new SpelExpressionParser();
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private BasicPolicyEnforcement basicPolicyEnforcement;

    @Transactional(readOnly = true)
    public boolean filter(Authentication authentication, Integer objectId, Object object, Object action) {
        Login subject = (Login) authentication.getPrincipal();
        boolean flag = false;
        if (!subject.getIsAdmin()) {
            List<SecurityPermission> deniedRules = new ArrayList<>();
            List<SecurityPermission> grantedRules = new ArrayList<>();
            CassiniObject cassiniObject = objectRepository.findById(objectId);
            ObjectAttributeDto objectAttributeDto = new ObjectAttributeDto();
            if (object instanceof ObjectTypeAttribute)
                objectAttributeDto.setObjectTypeAttribute((ObjectTypeAttribute) object);
            if (cassiniObject != null) objectAttributeDto.setCassiniObject(cassiniObject);
            SecurityAccessContext cxt = new SecurityAccessContext(subject, objectAttributeDto, action, null);
            List<SecurityPermission> allRules = subject.getGroupSecurityPermissions();
            allRules.forEach(securityPermission1 -> {
                if (securityPermission1.getObjectType().equals(EvaluationConstraints.ITEM))
                    securityPermission1.setObjectType(EvaluationConstraints.ITEMREVISION);
            });
            if (subject.getLoginSecurityPermissions().size() > 0)
                allRules.addAll(basicPolicyEnforcement.convertLoginPermissionToSecurityPermission(subject.getLoginSecurityPermissions()));
            deniedRules.addAll(allRules.stream().filter(rule -> rule.getPrivilegeType().equals(PrivilegeType.DENIED)
                    && (rule.getAttribute() != null || rule.getAttributeGroup() != null) && (rule.getPrivilege().equalsIgnoreCase("view") || rule.getPrivilege().equalsIgnoreCase("all"))).collect(Collectors.toList()));
            grantedRules.addAll(allRules.stream().filter(rule -> rule.getPrivilegeType().equals(PrivilegeType.GRANTED)
                    && (rule.getAttribute() != null || rule.getAttributeGroup() != null) && (rule.getPrivilege().equalsIgnoreCase("view") || rule.getPrivilege().equalsIgnoreCase("all"))).collect(Collectors.toList()));
            if (grantedRules.size() == 0 && deniedRules.size() == 0) return true;
            if (grantedRules.size() == 0 && deniedRules.size() > 0) {
                flag = !filterDeniedAttribute(deniedRules, cxt);
            } else if (grantedRules.size() > 0 && deniedRules.size() == 0) {
                flag = filterGrantedAttribute(grantedRules, cxt);
            } else if (grantedRules.size() > 0 && deniedRules.size() > 0) {
                if (!filterDeniedAttribute(deniedRules, cxt) || filterGrantedAttribute(grantedRules, cxt)) flag = true;
            }
        } else {
            flag = true;
        }

        return flag;
    }

    @Transactional(readOnly = true)
    public boolean filterGrantedAttribute(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        boolean flag = false;
        List<SecurityPermission> criteriaList = new ArrayList<>();
        List<SecurityPermission> nonCriteriaList = new ArrayList<>();
        List<SecurityPermission> filterObjectTypes = basicPolicyEnforcement.filterObjectTypes(rules, cxt, EvaluationConstraints.ATTRIBUTE);
        if (filterObjectTypes.size() > 0) {
            List<SecurityPermission> filterSubType = basicPolicyEnforcement.filterSubType(filterObjectTypes, cxt, EvaluationConstraints.ATTRIBUTE);
            if (filterSubType.size() > 0) {
                List<SecurityPermission> filterAttributeGroups = basicPolicyEnforcement.filterAttributesGroup(filterSubType, cxt);
                if (filterAttributeGroups.size() > 0) {
                    List<SecurityPermission> filterAttributes = basicPolicyEnforcement.filterAttributes(filterAttributeGroups, cxt);
                    if (filterAttributes.size() > 0) {
                        List<SecurityPermission> filerPrivileges = basicPolicyEnforcement.filterPrivileges(filterAttributes, cxt.getAction());
                        if (filerPrivileges.size() > 0) {
                            criteriaList.addAll(filerPrivileges.stream().filter(permission -> permission.getCriteria() != null).collect(Collectors.toList()));
                            nonCriteriaList.addAll(filerPrivileges.stream().filter(permission -> permission.getCriteria() == null).collect(Collectors.toList()));
                            flag = nonCriteriaList.size() != 0 || basicPolicyEnforcement.filterCriteria(criteriaList, cxt).size() > 0;
                        }
                    }
                }
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public boolean filterDeniedAttribute(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        boolean flag = false;
        List<SecurityPermission> criteriaList = new ArrayList<>();
        List<SecurityPermission> nonCriteriaList = new ArrayList<>();
        List<SecurityPermission> filterObjectTypes = basicPolicyEnforcement.filterObjectTypes(rules, cxt, EvaluationConstraints.ATTRIBUTE);
        if (filterObjectTypes.size() > 0) {
            List<SecurityPermission> filterSubType = basicPolicyEnforcement.filterSubType(rules, cxt, EvaluationConstraints.ATTRIBUTE);
            if (filterSubType.size() > 0) {
                List<SecurityPermission> filterAttributeGroups = basicPolicyEnforcement.filterAttributesGroup(filterSubType, cxt);
                if (filterAttributeGroups.size() > 0) {
                    List<SecurityPermission> filterAttributes = basicPolicyEnforcement.filterAttributes(filterAttributeGroups, cxt);
                    if (filterAttributes.size() > 0) {
                        List<SecurityPermission> filerPrivileges = basicPolicyEnforcement.filterPrivileges(filterAttributes, cxt.getAction());
                        if (filerPrivileges.size() > 0) {
                            criteriaList.addAll(filerPrivileges.stream().filter(permission -> permission.getCriteria() != null).collect(Collectors.toList()));
                            nonCriteriaList.addAll(filerPrivileges.stream().filter(permission -> permission.getCriteria() == null).collect(Collectors.toList()));
                            flag = nonCriteriaList.size() != 0 || basicPolicyEnforcement.filterCriteria(criteriaList, cxt).size() > 0;
                        }
                    }
                }
            }
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public boolean filterPrivilage(Authentication authentication, Object action, String type) {
        Login subject = (Login) authentication.getPrincipal();
        boolean flag = false;
        if (!subject.getIsAdmin()) {
            List<SecurityPermission> deniedRules = new ArrayList<>();
            List<SecurityPermission> grantedRules = new ArrayList<>();
            List<SecurityPermission> allRules = subject.getGroupSecurityPermissions();
            if (subject.getLoginSecurityPermissions().size() > 0)
                allRules.addAll(basicPolicyEnforcement.convertLoginPermissionToSecurityPermission(subject.getLoginSecurityPermissions()));
            deniedRules.addAll(allRules.stream().filter(rule -> rule.getPrivilegeType().equals(PrivilegeType.DENIED)
                    && (rule.getAttribute() == null || rule.getAttributeGroup() == null)).collect(Collectors.toList()));
            grantedRules.addAll(allRules.stream().filter(rule -> rule.getPrivilegeType().equals(PrivilegeType.GRANTED)
                    && (rule.getAttribute() == null || rule.getAttributeGroup() == null)).collect(Collectors.toList()));
            SecurityAccessContext cxt = new SecurityAccessContext(subject, null, action, null);
            if (grantedRules.size() == 0 && deniedRules.size() == 0) return true;
            if (grantedRules.size() == 0 && deniedRules.size() > 0) {
                List<SecurityPermission> filerPrivileges = filterPrivilegeByType(deniedRules, cxt.getAction(), type);
                flag = !(filerPrivileges.size() > 0);
            } else if (grantedRules.size() > 0 && deniedRules.size() == 0) {
                List<SecurityPermission> filerPrivileges = filterPrivilegeByType(grantedRules, cxt.getAction(), type);
                flag = (filerPrivileges.size() > 0);
            } else if (grantedRules.size() > 0 && deniedRules.size() > 0) {
                if (!(filterPrivilegeByType(deniedRules, cxt.getAction(), type).size() > 0) || (filterPrivilegeByType(grantedRules, cxt.getAction(), type).size() > 0)) flag = true;
            }
        } else {
            flag = true;
        }
        return flag;
    }

    protected List<SecurityPermission> filterPrivilegeByType(List<SecurityPermission> rules, Object action, String type) {
        List<SecurityPermission> matchedRules = new ArrayList<>();
        for (SecurityPermission rule : rules) {
            try {
                if (rule.getPrivilege() != null) {
                    if (rule.getPrivilege().contains(",")) {
                        String[] privilages = rule.getPrivilege().split(",");
                        for (String privilage : privilages)
                            if (rule.getObjectType().equals(type) && privilage.equals(action)) matchedRules.add(rule);
                    } else {
                        if (rule.getObjectType().equals(type) && (rule.getPrivilege().equals(action) || rule.getPrivilege().equals("all")))
                            matchedRules.add(rule);
                    }
                } else matchedRules.add(rule);
            } catch (Exception ignored) {
                logger.info("An error occurred while evaluating PolicyRule.", ignored);
            }
        }
        return matchedRules;
    }

    @Transactional(readOnly = true)
    public boolean filterDTO(Authentication authentication, Object object, Object action) {
        Login subject = (Login) authentication.getPrincipal();
        boolean flag = false;
        if (!subject.getIsAdmin()) {
            List<SecurityPermission> deniedRules = new ArrayList<>();
            List<SecurityPermission> grantedRules = new ArrayList<>();
            List<SecurityPermission> allRules = subject.getGroupSecurityPermissions();
            if (subject.getLoginSecurityPermissions().size() > 0)
                allRules.addAll(basicPolicyEnforcement.convertLoginPermissionToSecurityPermission(subject.getLoginSecurityPermissions()));
            deniedRules.addAll(allRules.stream().filter(rule -> rule.getPrivilegeType().equals(PrivilegeType.DENIED)
                    && (rule.getAttribute() == null && rule.getAttributeGroup() == null)).collect(Collectors.toList()));
            grantedRules.addAll(allRules.stream().filter(rule -> rule.getPrivilegeType().equals(PrivilegeType.GRANTED)
                    && (rule.getAttribute() == null && rule.getAttributeGroup() == null)).collect(Collectors.toList()));
            SecurityAccessContext cxt = new SecurityAccessContext(subject, object, action, null);
            if (grantedRules.size() == 0 && deniedRules.size() == 0) return true;
            if (grantedRules.size() == 0 && deniedRules.size() > 0) {
                flag = !deniedDtoFiltering(deniedRules, cxt);
            } else if (grantedRules.size() > 0 && deniedRules.size() == 0) {
                flag = grantedDtoFiltering(grantedRules, cxt);
            } else if (grantedRules.size() > 0 && deniedRules.size() > 0) {
                if (!deniedDtoFiltering(deniedRules, cxt) || grantedDtoFiltering(grantedRules, cxt)) flag = true;
            }
        } else {
            flag = true;
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public boolean grantedDtoFiltering(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        boolean flag = false;
        List<SecurityPermission> criteriaList = new ArrayList<>();
        List<SecurityPermission> nonCriteriaList = new ArrayList<>();
        List<SecurityPermission> filterObjectTypes = basicPolicyEnforcement.filterObjectTypes(rules, cxt, EvaluationConstraints.OBJECT);
        if (filterObjectTypes.size() > 0) {
            List<SecurityPermission> filterDtoSubType = filterDtoSubType(filterObjectTypes, cxt, EvaluationConstraints.OBJECT);
            if (filterDtoSubType.size() > 0) {
                criteriaList.addAll(filterDtoSubType.stream().filter(permission -> permission.getCriteria() != null).collect(Collectors.toList()));
                nonCriteriaList.addAll(filterDtoSubType.stream().filter(permission -> permission.getCriteria() == null).collect(Collectors.toList()));
                flag = nonCriteriaList.size() != 0 || basicPolicyEnforcement.filterCriteria(criteriaList, cxt).size() > 0;
            }
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public boolean deniedDtoFiltering(List<SecurityPermission> rules, SecurityAccessContext cxt) {
        boolean flag = false;
        List<SecurityPermission> criteriaList = new ArrayList<>();
        List<SecurityPermission> nonCriteriaList = new ArrayList<>();
        List<SecurityPermission> filterObjectTypes = basicPolicyEnforcement.filterObjectTypes(rules, cxt, EvaluationConstraints.OBJECT);
        if (filterObjectTypes.size() > 0) {
            List<SecurityPermission> filterSubTypes = filterDtoSubType(filterObjectTypes, cxt, EvaluationConstraints.OBJECT);
            if (filterSubTypes.size() > 0) {
                criteriaList.addAll(filterSubTypes.stream().filter(permission -> permission.getCriteria() != null).collect(Collectors.toList()));
                nonCriteriaList.addAll(filterSubTypes.stream().filter(permission -> permission.getCriteria() == null).collect(Collectors.toList()));
                flag = nonCriteriaList.size() != 0 || basicPolicyEnforcement.filterCriteria(criteriaList, cxt).size() > 0;
            }
        }
        return flag;
    }

    protected List<SecurityPermission> filterDtoSubType(List<SecurityPermission> rules, SecurityAccessContext cxt, String type) {
        List<SecurityPermission> matchedRules = new ArrayList<>();
        for (SecurityPermission rule : rules) {
            try {
                if (rule.getSubType() != null) {
                    if (rule.getSubType().contains(",")) {
                        String[] objectClass = rule.getSubType().split(",");
                        for (String className : objectClass) {
                            String expression = "object.subType.toString().equalsIgnoreCase(" + "\"" + className + "\"" + ")";
                            if (parser.parseExpression(expression).getValue(cxt, Boolean.class)) {
                                matchedRules.add(rule);
                            }
                        }
                    } else {
                        String expression = "object.subType.toString().equalsIgnoreCase(" + "\"" + rule.getSubType() + "\"" + ")";
                        if (parser.parseExpression(expression).getValue(cxt, Boolean.class)) {
                            matchedRules.add(rule);
                        }
                    }
                } else matchedRules.add(rule);
            } catch (Exception ignored) {
                logger.info("An error occurred while evaluating PolicyRule.", ignored);
            }
        }
        return matchedRules;
    }

}