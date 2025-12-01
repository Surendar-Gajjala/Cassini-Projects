package com.cassinisys.platform.service.security;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.SecurityPermissionCriteria;
import com.cassinisys.platform.filtering.SecurityPermissionPredicateBuilder;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.dto.SecurityPermissionsDto;
import com.cassinisys.platform.model.security.*;
import com.cassinisys.platform.repo.common.PersonGroupRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.security.*;
import com.cassinisys.platform.security.EvaluationConstraints;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.mysema.query.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by reddy on 7/27/15.
 */
@Service
public class SecurityPermissionService {
    @Autowired
    private SecurityPermissionRepository securityPermissionRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private SecurityPermissionPredicateBuilder securityPermissionPredicateBuilder;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private GroupSecurityPermissionRepository groupSecurityPermissionRepository;
    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LoginSecurityPermissionRepository loginSecurityPermissionRepository;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private DMFolderPermissionRepository dmFolderPermissionRepository;


    @Transactional
    public SecurityPermission create(SecurityPermission securityPermission) {
        if (checkSecurityPermission(securityPermission)) {
            throw new CassiniException(messageSource.getMessage("permission_already_exist", null, "Permission already exists", LocaleContextHolder.getLocale()));
        } else {
            securityPermission.setId(securityPermissionRepository.getMaxValue() + 1);
            securityPermission.setModule(getModuleTypeByObjectTypeAndSubType(securityPermission.getObjectType(), securityPermission.getSubType()));
            if (securityPermission.getCriteria() != null && securityPermission.getCriteria().equals("[]"))
                securityPermission.setCriteria(null);
            return securityPermissionRepository.save(securityPermission);
        }
    }

    @Transactional
    public SecurityPermission update(SecurityPermission securityPermission) {
        if (securityPermission.getCriteria() != null && securityPermission.getCriteria().equals("[]"))
            securityPermission.setCriteria(null);
        return securityPermissionRepository.save(securityPermission);
    }

    @Transactional
    public SecurityPermission createCriteria(Integer permissionId, String criteria) {
        if (criteria.equals("[]")) criteria = null;
        SecurityPermission securityPermission = securityPermissionRepository.findById(permissionId);
        securityPermission.setCriteria(criteria);
        return securityPermissionRepository.save(securityPermission);
    }

    @Transactional
    public void delete(Integer id) {
        securityPermissionRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public SecurityPermissionsDto getObjectTypes() {
        SecurityPermissionsDto securityPermissionsDto = new SecurityPermissionsDto();
        securityPermissionsDto.setObjectTypes(securityPermissionRepository.findByObjectTypeDistinct());
        securityPermissionsDto.setModuleTypes(securityPermissionRepository.findByModuleDistinct());
        securityPermissionsDto.setPrivileges(privilegeRepository.findAll().stream().map(Privilege::getName).collect(Collectors.toList()));
        return securityPermissionsDto;
    }

    @Transactional(readOnly = true)
    public CassiniObject getObjectType(Integer id) {
        return objectRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<SecurityPermission> getAllSecurityPermissionsByObjectType(String objectType) {
        List<SecurityPermission> securityPermissions = securityPermissionRepository.findByObjectTypeOrderByIdAsc(objectType);
        return securityPermissions;
    }

    @Transactional(readOnly = true)
    public SecurityPermission get(Integer permissionId) {
        return securityPermissionRepository.findById(permissionId);
    }

    @Transactional(readOnly = true)
    public Page<SecurityPermission> findAllSecurityPermission(Pageable pageable, SecurityPermissionCriteria criteria) {
        Predicate predicate = securityPermissionPredicateBuilder.build(criteria, QSecurityPermission.securityPermission);
        Page<SecurityPermission> securityPermissions = securityPermissionRepository.findAll(predicate, pageable);
        for (SecurityPermission securityPermission : securityPermissions.getContent()) {
            List<PersonGroup> personGroups = groupSecurityPermissionRepository.getPersonGroupsByPermission(securityPermission.getId());
            if (personGroups.size() > 0) {
                securityPermission.setUsedPermission(true);
            }
        }
        return securityPermissions;
    }

    public boolean checkSecurityPermission(SecurityPermission securityPermission) {
        List<SecurityPermission> securityPermissions = securityPermissionRepository.findAll();
        if (securityPermissions.size() > 0) {
            List<SecurityPermission> nameFiler = securityPermissions.stream().filter(securityPermission1 -> StringUtils.equals(securityPermission1.getName(), securityPermission.getName())).collect(Collectors.toList());
            if (nameFiler.size() > 0) {
                List<SecurityPermission> objectTypeFilter = nameFiler.stream().filter(securityPermission1 -> StringUtils.equals(securityPermission1.getObjectType(), securityPermission.getObjectType())).collect(Collectors.toList());
                if (objectTypeFilter.size() > 0) {
                    List<SecurityPermission> privilegeFilter = objectTypeFilter.stream().filter(securityPermission1 -> StringUtils.equals(securityPermission1.getPrivilege(), securityPermission.getPrivilege())).collect(Collectors.toList());
                    if (privilegeFilter.size() > 0) {
                        List<SecurityPermission> subTypeFilter = privilegeFilter.stream().filter(securityPermission1 -> StringUtils.equals(securityPermission1.getSubType(), securityPermission.getSubType())).collect(Collectors.toList());
                        if (subTypeFilter.size() > 0) {
                            List<SecurityPermission> attributeFilter = privilegeFilter.stream().filter(securityPermission1 -> StringUtils.equals(securityPermission1.getAttribute(), securityPermission.getAttribute())).collect(Collectors.toList());
                            if (attributeFilter.size() > 0) {
                                List<SecurityPermission> privilegeTypeFilter = attributeFilter.stream().filter(securityPermission1 -> StringUtils.equals(securityPermission1.getPrivilegeType().name(), securityPermission.getPrivilegeType().name())).collect(Collectors.toList());
                                if (privilegeTypeFilter.size() > 0) {
                                    List<SecurityPermission> criteriaFilter = privilegeTypeFilter.stream().filter(securityPermission1 -> StringUtils.equals(securityPermission1.getCriteria(), securityPermission.getCriteria())).collect(Collectors.toList());
                                    if (criteriaFilter.size() > 0) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Transactional
    public void createGroupSecurityPermission(Integer groupId, List<SecurityPermission> securityPermissions) {
        PersonGroup personGroup = personGroupRepository.findOne(groupId);
        for (SecurityPermission securityPermission : securityPermissions) {
            GroupSecurityPermission groupSecurityPermission = new GroupSecurityPermission();
            groupSecurityPermission.setId(new GroupSecurityPermissionId(personGroup, securityPermission));
            groupSecurityPermissionRepository.save(groupSecurityPermission);
        }

    }

    @Transactional
    public void deleteGroupSecurityPermission(Integer groupId, Integer permissionId) {
        PersonGroup personGroup = personGroupRepository.findOne(groupId);
        SecurityPermission securityPermission = securityPermissionRepository.findOne(permissionId);
        List<SecurityPermission> securityPermissions = securityPermissionRepository.findByObjectTypeAndSubType(securityPermission.getObjectType(), securityPermission.getSubType());
        if (securityPermissions.size() > 0) {
            for (SecurityPermission permissionElement : securityPermissions) {
                if (securityPermission.getPrivilege().equals("create") || securityPermission.getPrivilege().equals("edit") || securityPermission.getPrivilege().equals("delete") || securityPermission.getPrivilege().equals("view")) {
                    if (permissionElement.getPrivilege().equals("all")) {
                        GroupSecurityPermission groupSecurityPermission = new GroupSecurityPermission();
                        groupSecurityPermission.setId(new GroupSecurityPermissionId(personGroup, permissionElement));
                        groupSecurityPermissionRepository.delete(groupSecurityPermission);
                    }

                }
            }


        }
        GroupSecurityPermission groupSecurityPermission = new GroupSecurityPermission();
        groupSecurityPermission.setId(new GroupSecurityPermissionId(personGroup, securityPermission));
        groupSecurityPermissionRepository.delete(groupSecurityPermission);
    }

    public void addLoginSecurityPermission(Integer personId, CassiniObject cassiniObject, String privilege) {
        Person person = personRepository.findOne(personId);
        if (!cassiniObject.getObjectType().name().equals("INSPECTIONPLANREVISION")) {
            String objectType = cassiniObject.getObjectType().name().toLowerCase();
            LoginSecurityPermission loginSecurityPermission = getLoginSecurityPermission(objectType, null, privilege, getModuleType(objectType), null, null, "object.id == " + cassiniObject.getId(), PrivilegeType.GRANTED);
            loginSecurityPermission.setPerson(person);
            loginSecurityPermission.setName("Login Permission for user " + person.getFullName() + "on object " + cassiniObject.getObjectType().name());
            if (checkLoginPermission(person, loginSecurityPermission))
                loginSecurityPermissionRepository.save(loginSecurityPermission);
        } else {
            List<LoginSecurityPermission> loginSecurityPermission = new ArrayList<>();
            loginSecurityPermission.add(getLoginSecurityPermission("PRODUCTINSPECTIONPLAN".toLowerCase(), null, privilege, ModuleType.PQ, null, null, "object.id == " + cassiniObject.getId(), PrivilegeType.GRANTED));
            loginSecurityPermission.add(getLoginSecurityPermission("MATERIALINSPECTIONPLAN".toLowerCase(), null, privilege, ModuleType.PQ, null, null, "object.id == " + cassiniObject.getId(), PrivilegeType.GRANTED));
            loginSecurityPermission.forEach(loginSecurityPermission1 -> {
                loginSecurityPermission1.setName("Login Permission for user " + person.getFullName() + " on object " + cassiniObject.getObjectType().name());
                loginSecurityPermission1.setPerson(person);
                if (checkLoginPermission(person, loginSecurityPermission1))
                    loginSecurityPermissionRepository.save(loginSecurityPermission1);
            });
        }
    }

    public LoginSecurityPermission getLoginSecurityPermission(String objectType, String subType, String privilage, ModuleType moduleType, String attribute, String attributeGroup, String critiera, PrivilegeType privilegeType) {
        LoginSecurityPermission loginSecurityPermission = new LoginSecurityPermission();
        loginSecurityPermission.setObjectType(objectType);
        loginSecurityPermission.setSubType(subType);
        loginSecurityPermission.setPrivilege(privilage);
        loginSecurityPermission.setModule(moduleType);
        loginSecurityPermission.setPrivilegeType(privilegeType);
        loginSecurityPermission.setAttribute(attribute);
        loginSecurityPermission.setAttributeGroup(attributeGroup);
        loginSecurityPermission.setCriteria(critiera);
        return loginSecurityPermission;
    }

    public boolean checkLoginPermission(Person person, LoginSecurityPermission loginSecurityPermission) {
        boolean flag = true;
        List<LoginSecurityPermission> loginSecurityPermissions = loginSecurityPermissionRepository.findByPerson(person);
        for (LoginSecurityPermission l : loginSecurityPermissions) {
            if (l.getName().equals(loginSecurityPermission.getName()) && l.getCriteria().equals(loginSecurityPermission.getCriteria()) & flag) {
                flag = false;
            }
        }
        return flag;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public List getTypeAttributes(String type, Integer typeId, Boolean hier) {
        List typeAttributes = null;
        if (type.equalsIgnoreCase(EvaluationConstraints.ITEMTYPE)) {
            typeAttributes = objectTypeService.getTypeSystem("itemType").getTypeAttributes(typeId, hier);
        }
        if (type.equalsIgnoreCase(EvaluationConstraints.ECOTYPE) || type.equalsIgnoreCase(EvaluationConstraints.ECRTYPE)
                || type.equalsIgnoreCase(EvaluationConstraints.DCOTYPE) || type.equalsIgnoreCase(EvaluationConstraints.DCRTYPE)
                || type.equalsIgnoreCase(EvaluationConstraints.MCOTYPE) || type.equalsIgnoreCase(EvaluationConstraints.ECRTYPE)
                || type.equalsIgnoreCase(EvaluationConstraints.ECRTYPE)) {
            typeAttributes = objectTypeService.getTypeSystem("changeType").getTypeAttributes(typeId, hier);
        }
        if (type.equalsIgnoreCase(EvaluationConstraints.QUALITY_TYPE)) {
            typeAttributes = objectTypeService.getTypeSystem("qualityType").getTypeAttributes(typeId, hier);
        }
        if (type.equalsIgnoreCase(EvaluationConstraints.REQUIREMENTTYPE) || type.equalsIgnoreCase(EvaluationConstraints.REQUIREMENTDOCUMENTTYPE)) {
            typeAttributes = objectTypeService.getTypeSystem("pmType").getTypeAttributes(typeId, hier);
        }
        if (type.equalsIgnoreCase(EvaluationConstraints.PLANTTYPE) || type.equalsIgnoreCase(EvaluationConstraints.WORKCENTERTYPE) ||
                type.equalsIgnoreCase(EvaluationConstraints.MACHINETYPE) || type.equalsIgnoreCase(EvaluationConstraints.EQUIPMENTTYPE) ||
                type.equalsIgnoreCase(EvaluationConstraints.INSTRUMENTTYPE) || type.equalsIgnoreCase(EvaluationConstraints.TOOLTYPE) ||
                type.equalsIgnoreCase(EvaluationConstraints.JIGFIXTURETYPE) || type.equalsIgnoreCase(EvaluationConstraints.MATERIALTYPE) ||
                type.equalsIgnoreCase(EvaluationConstraints.MANPOWERTYPE) || type.equalsIgnoreCase(EvaluationConstraints.OPERATIONTYPE) ||
                type.equalsIgnoreCase(EvaluationConstraints.PRODUCTIONORDERTYPE) || type.equalsIgnoreCase(EvaluationConstraints.SERVICEORDERTYPE)) {
            typeAttributes = objectTypeService.getTypeSystem("MESObjectType").getTypeAttributes(typeId, hier);
        }
        if (type.equalsIgnoreCase(EvaluationConstraints.ASSETTYPE) || type.equalsIgnoreCase(EvaluationConstraints.METERTYPE) ||
                type.equalsIgnoreCase(EvaluationConstraints.SPAREPARTTYPE) || type.equalsIgnoreCase(EvaluationConstraints.WORKREQUESTTYPE) ||
                type.equalsIgnoreCase(EvaluationConstraints.WORKORDERTYPE) || type.equalsIgnoreCase(EvaluationConstraints.ASSEMBLYLINETYPE)) {
            typeAttributes = objectTypeService.getTypeSystem("MROObjectType").getTypeAttributes(typeId, hier);
        }
        if (type.equalsIgnoreCase(EvaluationConstraints.MANUFACTURERTYPE)) {
            typeAttributes = objectTypeService.getTypeSystem("manufacturerType").getTypeAttributes(typeId, hier);
        }
        if (type.equalsIgnoreCase(EvaluationConstraints.MANUFACTURERPARTTYPE)) {
            typeAttributes = objectTypeService.getTypeSystem("manufacturerPartType").getTypeAttributes(typeId, hier);
        }
        if (type.equalsIgnoreCase(EvaluationConstraints.PGCSUBSTANCETYPE) ||
                type.equalsIgnoreCase(EvaluationConstraints.PGCSPECIFICATIONTYPE) || type.equalsIgnoreCase(EvaluationConstraints.PGCDECLARATIONTYPE)) {
            typeAttributes = objectTypeService.getTypeSystem("PGCObjectType").getTypeAttributes(typeId, hier);
        }
        return typeAttributes;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public ModuleType getModuleType(String objectType) {
        ModuleType moduleType;
        List<SecurityPermission> securityPermissionList;
        securityPermissionList = securityPermissionRepository.findByObjectType(objectType);
        if (securityPermissionList.size() > 0) moduleType = securityPermissionList.get(0).getModule();
        else moduleType = ModuleType.PLATFORM;
        return moduleType;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public ModuleType getModuleTypeByObjectTypeAndSubType(String objectType, String subType) {
        objectType = objectType.toLowerCase();
        ModuleType moduleType;
        List<SecurityPermission> securityPermissionList;
        subType = subType.toLowerCase().replaceAll("\\s", "");
        if (subType.contains("mco")) subType = "mco";
        securityPermissionList = securityPermissionRepository.findByObjectTypeAndSubType(objectType, subType);
        if (securityPermissionList.size() > 0) {
            moduleType = securityPermissionList.get(0).getModule();
        } else {
            moduleType = getModuleType(objectType);
        }
        return moduleType;
    }

    public boolean checkPermission(String objectType, String subType, String privilege) {
        List<SecurityPermission> securityPermissions = sessionWrapper.getSession().getLogin().getGroupSecurityPermissions();
        if(!sessionWrapper.getSession().getLogin().getIsAdmin()) {
            if(!securityPermissions.isEmpty()) {
                securityPermissions = securityPermissions.stream().filter(securityPermission1 -> StringUtils.equals(securityPermission1.getObjectType(), objectType)).collect(Collectors.toList());
                if(!securityPermissions.isEmpty() && subType != null){
                    securityPermissions = securityPermissions.stream().filter(securityPermission1 -> StringUtils.equals(securityPermission1.getSubType(), subType) || StringUtils.equals(securityPermission1.getSubType(), null)).collect(Collectors.toList());
                } else if(!securityPermissions.isEmpty()){
                    securityPermissions = securityPermissions.stream().filter(securityPermission1 -> StringUtils.equals(securityPermission1.getPrivilege(), privilege) || StringUtils.equals(securityPermission1.getPrivilege(), "all")).collect(Collectors.toList());
                }
            }
        } else return true;
        return !securityPermissions.isEmpty();
    }

    public List<DMFolderPermission> saveDMPermissions(List<DMFolderPermission> dmFolderPermissions) {
        return dmFolderPermissionRepository.save(dmFolderPermissions);
    }

    public List<DMFolderPermission> getAllDMFolderPermissions() {
        return dmFolderPermissionRepository.findAll();
    }

    public List<DMFolderPermission> getDMPermissionByIdAndType(Integer id, String type) {
        List<DMFolderPermission> folderPermissions;
        switch (type) {
            case "permissionId" :
                folderPermissions = dmFolderPermissionRepository.findByPermissionId(id);
                break;
            case "groupId" :
                folderPermissions = dmFolderPermissionRepository.findByGroupId(id);
                break;
            case "folderId" :
                folderPermissions = dmFolderPermissionRepository.findByFolderId(id);
                break;
            default:
                folderPermissions = dmFolderPermissionRepository.findByPermissionId(id);
        }
        return folderPermissions;
    }

}
