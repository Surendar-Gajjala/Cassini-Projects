package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.security.ModuleType;
import com.cassinisys.platform.model.security.QSecurityPermission;
import com.cassinisys.platform.model.security.SecurityPermission;
import com.cassinisys.platform.repo.security.GroupSecurityPermissionRepository;
import com.cassinisys.platform.repo.security.SecurityPermissionRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suresh CassiniPLM on 27-10-2020.
 */
@Component
public class SecurityPermissionPredicateBuilder implements PredicateBuilder<SecurityPermissionCriteria, QSecurityPermission> {

    @Autowired
    private GroupSecurityPermissionRepository groupSecurityPermissionRepository;

    @Override
    public Predicate build(SecurityPermissionCriteria shiftCriteria, QSecurityPermission pathBase) {
        return getDefaultPredicate(shiftCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(SecurityPermissionCriteria criteria, QSecurityPermission pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.objectType.containsIgnoreCase(s)).
                        or(pathBase.subType.containsIgnoreCase(s)).
                        or(pathBase.attribute.containsIgnoreCase(s)).
                        or(pathBase.attributeGroup.containsIgnoreCase(s)).
                        or(pathBase.privilege.containsIgnoreCase(s)).
                        or(pathBase.criteria.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if(!Criteria.isEmpty(criteria.getId())){
            List<SecurityPermission> securityPermissions = groupSecurityPermissionRepository.getSecurityPermissionsByGroupId(criteria.getId());
            if(securityPermissions.size() > 0){
                securityPermissions.forEach(s -> predicates.add(pathBase.id.ne(s.getId())));
            }
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getObjectType())) {
            predicates.add(pathBase.objectType.containsIgnoreCase(criteria.getObjectType()));
        }
        if (!Criteria.isEmpty(criteria.getSubType())) {
            predicates.add(pathBase.subType.containsIgnoreCase(criteria.getSubType()));
        }
        if (!Criteria.isEmpty(criteria.getPrivilege())) {
            predicates.add(pathBase.privilege.containsIgnoreCase(criteria.getPrivilege()));
        }
        if (!Criteria.isEmpty(criteria.getAttribute())) {
            predicates.add(pathBase.attribute.containsIgnoreCase(criteria.getAttribute()));
        }
        if (!Criteria.isEmpty(criteria.getAttributeGroup())) {
            predicates.add(pathBase.attributeGroup.containsIgnoreCase(criteria.getAttributeGroup()));
        }
        if (!Criteria.isEmpty(criteria.getPrivilegeType())) {
            predicates.add(pathBase.privilegeType.eq(criteria.getPrivilegeType()));
        }
        if (!Criteria.isEmpty(criteria.getModule()) && !criteria.getModule().equals(ModuleType.ALL)) {
            predicates.add(pathBase.module.eq(criteria.getModule()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }

        return ExpressionUtils.allOf(predicates);
    }

}