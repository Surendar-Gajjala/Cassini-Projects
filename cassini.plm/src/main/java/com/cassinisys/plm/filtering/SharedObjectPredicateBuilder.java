package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.CustomObjectCriteria;
import com.cassinisys.platform.filtering.CustomObjectPredicateBuilder;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.QCustomObject;
import com.cassinisys.platform.repo.custom.CustomObjectRepository;
import com.cassinisys.platform.service.common.GroupMemberService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.pgc.PGCDeclaration;
import com.cassinisys.plm.model.pgc.QPGCDeclaration;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.QPLMItem;
import com.cassinisys.plm.model.plm.QPLMSharedObject;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.pgc.PGCDeclarationRepository;
import com.cassinisys.plm.repo.plm.FolderRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.pm.PLMActivityRepository;
import com.cassinisys.plm.repo.pm.ProgramRepository;
import com.cassinisys.plm.repo.pm.ProjectRepository;
import com.cassinisys.plm.repo.pm.TaskRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SharedObjectPredicateBuilder implements PredicateBuilder<SharedObjectCriteria, QPLMSharedObject> {

    @Autowired
    private GroupMemberService groupMemberService;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ItemPredicateBuilder itemPredicateBuilder;
    @Autowired
    private ManufacturerPredicateBuilder manufacturerPredicateBuilder;
    @Autowired
    private ManufacturerPartPredicateBuilder manufacturerPartPredicateBuilder;
    @Autowired
    private DeclarationPredicateBuilder declarationPredicateBuilder;
    @Autowired
    private ProjectPredicateBuilder projectPredicateBuilder;
    @Autowired
    private ProgramPredicateBuilder programPredicateBuilder;
    @Autowired
    private ProjectActivityPredicateBuilder projectActivityPredicateBuilder;
    @Autowired
    private ProjectTaskPredicateBuilder projectTaskPredicateBuilder;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private PGCDeclarationRepository pgcDeclarationRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private PLMActivityRepository plmActivityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private CustomObjectPredicateBuilder customObjectPredicateBuilder;
    @Autowired
    private PLMSupplierPredicateBuilder supplierPredicateBuilder;

    @Override
    public Predicate build(SharedObjectCriteria criteria, QPLMSharedObject pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(SharedObjectCriteria criteria, QPLMSharedObject pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            if (criteria.getSharedObjectType().equals("ITEM")) {
                List<Integer> itemIds = getItemIds(criteria);
                predicates.add(pathBase.objectId.in(itemIds));
            } else if (criteria.getSharedObjectType().equals("MANUFACTURER")) {
                List<Integer> itemIds = getMfrIds(criteria);
                predicates.add(pathBase.objectId.in(itemIds));
            } else if (criteria.getSharedObjectType().equals("MANUFACTURERPART")) {
                List<Integer> itemIds = getMfrPartIds(criteria);
                predicates.add(pathBase.objectId.in(itemIds));
            } else if (criteria.getSharedObjectType().equals("MFRSUPPLIER")) {
                List<Integer> itemIds = getSupplierIds(criteria);
                predicates.add(pathBase.objectId.in(itemIds));
            } else if (criteria.getSharedObjectType().equals("CUSTOMOBJECT")) {
                List<Integer> itemIds = getCustomObjectIds(criteria);
                predicates.add(pathBase.objectId.in(itemIds));
            } else if (criteria.getSharedObjectType().equals("PGCDECLARATION")) {
                List<Integer> itemIds = getDeclarationIds(criteria);
                predicates.add(pathBase.objectId.in(itemIds));
            } else if (criteria.getSharedObjectType().equals("PROJECT")) {
                List<Integer> itemIds = getProjectIds(criteria);
                predicates.add(pathBase.objectId.in(itemIds));
            } else if (criteria.getSharedObjectType().equals("PROGRAM")) {
                List<Integer> itemIds = getProgramIds(criteria);
                predicates.add(pathBase.objectId.in(itemIds));
            } else if (criteria.getSharedObjectType().equals("PROJECTACTIVITY")) {
                List<Integer> itemIds = getProjectActivityIds(criteria);
                predicates.add(pathBase.objectId.in(itemIds));
            } else if (criteria.getSharedObjectType().equals("PROJECTTASK")) {
                List<Integer> itemIds = getProjectTaskIds(criteria);
                predicates.add(pathBase.objectId.in(itemIds));
            }
        }

        if (!Criteria.isEmpty(criteria.getSharedObjectType())) {
            if (criteria.getSharedObjectType().equals("MANUFACTURER")) {
                List<Integer> personIds = new ArrayList<>();
                if (!Criteria.isEmpty(criteria.getPerson())) {
                    personIds = groupMemberService.findGroupIdsByPerson(criteria.getPerson());
                    personIds.add(criteria.getPerson());
                    predicates.add(pathBase.sharedTo.in(personIds));
                } else {
                    personIds = groupMemberService
                            .findGroupIdsByPerson(sessionWrapper.getSession().getLogin().getPerson().getId());
                    personIds.add(sessionWrapper.getSession().getLogin().getPerson().getId());
                    predicates.add(pathBase.sharedTo.in(personIds));
                }
            } else if (criteria.getSharedObjectType().equals("MANUFACTURERPART")) {
                List<Integer> personIds = new ArrayList<>();
                if (!Criteria.isEmpty(criteria.getPerson())) {
                    personIds = groupMemberService.findGroupIdsByPerson(criteria.getPerson());
                    personIds.add(criteria.getPerson());
                    predicates.add(pathBase.sharedTo.in(personIds));
                } else {
                    personIds = groupMemberService
                            .findGroupIdsByPerson(sessionWrapper.getSession().getLogin().getPerson().getId());
                    personIds.add(sessionWrapper.getSession().getLogin().getPerson().getId());
                    predicates.add(pathBase.sharedTo.in(personIds));
                }
            } else if (criteria.getSharedObjectType().equals("MFRSUPPLIER")) {
                List<Integer> personIds = new ArrayList<>();
                if (!Criteria.isEmpty(criteria.getPerson())) {
                    personIds = groupMemberService.findGroupIdsByPerson(criteria.getPerson());
                    personIds.add(criteria.getPerson());
                    predicates.add(pathBase.sharedTo.in(personIds));
                } else {
                    personIds = groupMemberService
                            .findGroupIdsByPerson(sessionWrapper.getSession().getLogin().getPerson().getId());
                    personIds.add(sessionWrapper.getSession().getLogin().getPerson().getId());
                    predicates.add(pathBase.sharedTo.in(personIds));
                }
            } else if (criteria.getSharedObjectType().equals("CUSTOMOBJECT")) {
                List<Integer> personIds = new ArrayList<>();
                if (!Criteria.isEmpty(criteria.getPerson())) {
                    personIds = groupMemberService.findGroupIdsByPerson(criteria.getPerson());
                    personIds.add(criteria.getPerson());
                    predicates.add(pathBase.sharedTo.in(personIds));
                } else {
                    personIds = groupMemberService
                            .findGroupIdsByPerson(sessionWrapper.getSession().getLogin().getPerson().getId());
                    personIds.add(sessionWrapper.getSession().getLogin().getPerson().getId());
                    predicates.add(pathBase.sharedTo.in(personIds));
                }
            } else if (criteria.getSharedObjectType().equals("PGCDECLARATION")) {
                List<Integer> personIds = new ArrayList<>();
                if (!Criteria.isEmpty(criteria.getPerson())) {
                    personIds = groupMemberService.findGroupIdsByPerson(criteria.getPerson());
                    personIds.add(criteria.getPerson());
                    predicates.add(pathBase.sharedTo.in(personIds));
                } else {
                    personIds = groupMemberService
                            .findGroupIdsByPerson(sessionWrapper.getSession().getLogin().getPerson().getId());
                    personIds.add(sessionWrapper.getSession().getLogin().getPerson().getId());
                    predicates.add(pathBase.sharedTo.in(personIds));
                }
            } else if (criteria.getSharedObjectType().equals("PROJECT")
                    || criteria.getSharedObjectType().equals("PROJECTACTIVITY")
                    || criteria.getSharedObjectType().equals("PROGRAM")
                    || criteria.getSharedObjectType().equals("PROJECTTASK")) {
                List<Integer> personIds = new ArrayList<>();
                if (!Criteria.isEmpty(criteria.getPerson())) {
                    personIds = groupMemberService.findGroupIdsByPerson(criteria.getPerson());
                    personIds.add(criteria.getPerson());
                    predicates.add(pathBase.sharedTo.in(personIds));
                } else {
                    personIds = groupMemberService
                            .findGroupIdsByPerson(sessionWrapper.getSession().getLogin().getPerson().getId());
                    personIds.add(sessionWrapper.getSession().getLogin().getPerson().getId());
                    predicates.add(pathBase.sharedTo.in(personIds));
                }
            } else if (criteria.getSharedObjectType().equals("ITEM")) {
                List<Integer> personIds = new ArrayList<>();
                if (!Criteria.isEmpty(criteria.getPerson())) {
                    personIds = groupMemberService.findGroupIdsByPerson(criteria.getPerson());
                    personIds.add(criteria.getPerson());
                    predicates.add(pathBase.sharedTo.in(personIds));
                } else {
                    personIds = groupMemberService
                            .findGroupIdsByPerson(sessionWrapper.getSession().getLogin().getPerson().getId());
                    personIds.add(sessionWrapper.getSession().getLogin().getPerson().getId());
                    predicates.add(pathBase.sharedTo.in(personIds));
                }
            } else if (criteria.getSharedObjectType().equals("DOCUMENT")) {
                List<Integer> personIds = new ArrayList<>();
                if (!Criteria.isEmpty(criteria.getPerson())) {
                    personIds = groupMemberService.findGroupIdsByPerson(criteria.getPerson());
                    personIds.add(criteria.getPerson());
                    predicates.add(pathBase.sharedTo.in(personIds));
                } else {
                    personIds = groupMemberService
                            .findGroupIdsByPerson(sessionWrapper.getSession().getLogin().getPerson().getId());
                    personIds.add(sessionWrapper.getSession().getLogin().getPerson().getId());
                    predicates.add(pathBase.sharedTo.in(personIds));
                }
            } else if (criteria.getSharedObjectType().equals("SHAREDFOLDERSFILES")) {
                List<Integer> personIds = new ArrayList<>();
                if (!Criteria.isEmpty(criteria.getPerson())) {

                    predicates.add(pathBase.sharedTo.in(criteria.getPerson()));
                }
            }
            predicates.add(pathBase.sharedObjectType.eq(ObjectType.valueOf(criteria.getSharedObjectType())));
        }
        return ExpressionUtils.allOf(predicates);
    }

    private List<Integer> getItemIds(SharedObjectCriteria criteria) {
        List<Integer> itemIds = new ArrayList<>();
        ItemCriteria itemCriteria = new ItemCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = itemPredicateBuilder.build(itemCriteria, QPLMItem.pLMItem);
        Page<PLMItem> items = itemRepository.findAll(predicate, pageable);

        for (PLMItem item : items.getContent()) {
            itemIds.add(item.getId());
        }

        return itemIds;
    }

    private List<Integer> getMfrIds(SharedObjectCriteria criteria) {
        List<Integer> mfrIds = new ArrayList<>();
        ManufacturerCriteria itemCriteria = new ManufacturerCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        itemCriteria.setFreeTextSearch(true);
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = manufacturerPredicateBuilder.build(itemCriteria, QPLMManufacturer.pLMManufacturer);
        Page<PLMManufacturer> manufacturers = manufacturerRepository.findAll(predicate, pageable);

        for (PLMManufacturer manufacturer : manufacturers.getContent()) {
            mfrIds.add(manufacturer.getId());
        }

        return mfrIds;
    }

    private List<Integer> getMfrPartIds(SharedObjectCriteria criteria) {
        List<Integer> partIds = new ArrayList<>();
        ManufacturerPartCriteria itemCriteria = new ManufacturerPartCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        itemCriteria.setFreeTextSearch(true);
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = manufacturerPartPredicateBuilder.build(itemCriteria,
                QPLMManufacturerPart.pLMManufacturerPart);
        Page<PLMManufacturerPart> items = manufacturerPartRepository.findAll(predicate, pageable);

        for (PLMManufacturerPart item : items.getContent()) {
            partIds.add(item.getId());
        }

        return partIds;
    }

    private List<Integer> getSupplierIds(SharedObjectCriteria criteria) {
        List<Integer> partIds = new ArrayList<>();
        PLMSupplierCriteria itemCriteria = new PLMSupplierCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        itemCriteria.setFreeTextSearch(true);
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = supplierPredicateBuilder.build(itemCriteria,
                QPLMSupplier.pLMSupplier);
        Page<PLMSupplier> items = supplierRepository.findAll(predicate, pageable);

        for (PLMSupplier item : items.getContent()) {
            partIds.add(item.getId());
        }

        return partIds;
    }


    private List<Integer> getCustomObjectIds(SharedObjectCriteria criteria) {
        List<Integer> partIds = new ArrayList<>();
        CustomObjectCriteria itemCriteria = new CustomObjectCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = customObjectPredicateBuilder.build(itemCriteria,
                QCustomObject.customObject);
        Page<CustomObject> items = customObjectRepository.findAll(predicate, pageable);

        for (CustomObject item : items.getContent()) {
            partIds.add(item.getId());
        }

        return partIds;
    }

    private List<Integer> getDeclarationIds(SharedObjectCriteria criteria) {
        List<Integer> inspectionPlanIds = new ArrayList<>();
        DeclarationCriteria itemCriteria = new DeclarationCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = declarationPredicateBuilder.build(itemCriteria, QPGCDeclaration.pGCDeclaration);
        Page<PGCDeclaration> items = pgcDeclarationRepository.findAll(predicate, pageable);

        for (PGCDeclaration item : items.getContent()) {
            inspectionPlanIds.add(item.getId());
        }

        return inspectionPlanIds;
    }

    private List<Integer> getProjectIds(SharedObjectCriteria criteria) {
        List<Integer> projectIds = new ArrayList<>();
        ProjectCriteria itemCriteria = new ProjectCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = projectPredicateBuilder.build(itemCriteria, QPLMProject.pLMProject);
        Page<PLMProject> projects = projectRepository.findAll(predicate, pageable);

        for (PLMProject item : projects.getContent()) {
            projectIds.add(item.getId());
        }

        return projectIds;
    }

    private List<Integer> getProgramIds(SharedObjectCriteria criteria) {
        List<Integer> programIds = new ArrayList<>();
        ProjectCriteria itemCriteria = new ProjectCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = programPredicateBuilder.build(itemCriteria, QPLMProgram.pLMProgram);
        Page<PLMProgram> programs = programRepository.findAll(predicate, pageable);

        for (PLMProgram item : programs.getContent()) {
            programIds.add(item.getId());
        }

        return programIds;
    }

    private List<Integer> getProjectActivityIds(SharedObjectCriteria criteria) {
        List<Integer> activityIds = new ArrayList<>();
        ProjectActivityCriteria itemCriteria = new ProjectActivityCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = projectActivityPredicateBuilder.build(itemCriteria, QPLMActivity.pLMActivity);
        Page<PLMActivity> activities = plmActivityRepository.findAll(predicate, pageable);

        for (PLMActivity item : activities.getContent()) {
            activityIds.add(item.getId());
        }

        return activityIds;
    }

    private List<Integer> getProjectTaskIds(SharedObjectCriteria criteria) {
        List<Integer> taskIds = new ArrayList<>();
        ProjectTaskCriteria itemCriteria = new ProjectTaskCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = projectTaskPredicateBuilder.build(itemCriteria, QPLMTask.pLMTask);
        Page<PLMTask> tasks = taskRepository.findAll(predicate, pageable);

        for (PLMTask item : tasks.getContent()) {
            taskIds.add(item.getId());
        }

        return taskIds;
    }
}
