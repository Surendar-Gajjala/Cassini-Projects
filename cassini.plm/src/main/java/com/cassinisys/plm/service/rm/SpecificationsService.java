package com.cassinisys.plm.service.rm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ErrorCodes;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.model.dto.ColumnData;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.col.AttributeAttachmentRepository;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import com.cassinisys.platform.service.common.ForgeService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.service.utils.ExcelService;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.filtering.ProjectSpecificationDeliverableBuilder;
import com.cassinisys.plm.filtering.RequirementSearchCriteria;
import com.cassinisys.plm.filtering.RequirementSearchPredicateBuilder;
import com.cassinisys.plm.filtering.SpecificationBuilderCriteria;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pm.ProjectTemplate;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.ECORepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.PLMActivityRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pm.ProjectRepository;
import com.cassinisys.plm.repo.pm.TaskRepository;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.classification.ClassificationTypeService;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.plm.ItemServiceException;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class SpecificationsService implements CrudService<SpecificationType, Integer>,
        PageableService<SpecificationType, Integer>, ClassificationTypeService<SpecificationType, RmObjectTypeAttribute> {

    @Autowired
    private RequirementTypeRepository requirementTypeRepository;
    @Autowired
    private RmObjectTypeAttributeRepository rmObjectTypeAttributeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SpecificationTypeRepository specificationTypeRepository;
    @Autowired
    private RmObjectAttributeRepository rmObjectAttributeRepository;
    @Autowired
    private SpecificationRepository specificationRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private RmObjectFileRepository rmObjectFileRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FileDownloadHistoryRepository fileDownloadHistoryRepository;
    @Autowired
    private RmObjectRepository rmObjectRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private SpecSectionRepository specSectionRepository;
    @Autowired
    private SpecRequirementRepository specRequirementRepository;
    @Autowired
    private SpecElementRepository specElementRepository;
    @Autowired
    private RmRelationShipRepository rmRelationShipRepository;
    @Autowired
    private RmRelatedItemRepository rmRelatedItemRepository;

    @Autowired
    private RequirementsService requirementsService;

    @Autowired
    private RequirementRepository requirementRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RequirementSearchPredicateBuilder requirementSearchPredicateBuilder;

    @Autowired
    private SpecificationDeliverableRepository specificationDeliverableRepository;

    @Autowired
    private ProjectSpecificationDeliverableBuilder specificationDeliverableBuilder;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private AutoNumberService autoNumberService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Autowired
    private ECORepository ecoRepository;

    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private GlossaryRepository glossaryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectTemplateRepository projectTemplateRepository;

    @Autowired
    private UtilService utilService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;

    @Autowired
    private RequirementEditRepository requirementEditRepository;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;

    @Autowired
    private LovRepository lovRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private SpecificationRevisionHistoryRepository specificationRevisionHistoryRepository;
    @Autowired
    private SpecificationRevisionStatusHistoryRepository specificationRevisionStatusHistoryRepository;

    @Autowired
    private SpecPermissionRepository specPermissionRepository;

    @Autowired
    private AttributeAttachmentService attributeAttachmentService;

    @Autowired
    private AttributeAttachmentRepository attributeAttachmentRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private EMailTemplateConfigRepository eMailTemplateConfigRepository;

    @Autowired
    private FileHelpers fileHelpers;

    @Autowired
    private ItemFileService itemFileService;

    @Value("classpath:mime.types.txt")
    private Resource reportsResource;

    private MimetypesFileTypeMap mimeTypes = null;

    @Autowired
    private ForgeService forgeService;

    @Autowired
    private PLMActivityRepository activityRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;

    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;

    @Autowired
    private PLMWorkflowService workflowService;

    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;

    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private RmObjectTypeRepository rmObjectTypeRepository;
    @Autowired
    private ObjectFileService qualityFileService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    public SpecificationType create(SpecificationType rmObjectType) {
        checkNotNull(rmObjectType);
        rmObjectType = specificationTypeRepository.save(rmObjectType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.SPECIFICATIONTYPE, rmObjectType));
        return rmObjectType;
    }

    @Override
    public SpecificationType update(SpecificationType rmObjectType) {
        checkNotNull(rmObjectType);
        checkNotNull(rmObjectType.getId());
        SpecificationType existSepcType = specificationTypeRepository.findOne(rmObjectType.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.SPECIFICATIONTYPE, existSepcType, rmObjectType));
        rmObjectType = specificationTypeRepository.save(rmObjectType);
        return rmObjectType;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        SpecificationType rmObjectType = specificationTypeRepository.findOne(id);
        if (rmObjectType.getParentType() != null) {
            applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeDeletedEvent(PLMObjectType.SPECIFICATIONTYPE, rmObjectType));
        }
        specificationTypeRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public SpecificationType get(Integer id) {
        checkNotNull(id);
        SpecificationType rmObjectType = specificationTypeRepository.findOne(id);
        if (rmObjectType == null) {
            throw new ResourceNotFoundException();
        }
        return rmObjectType;
    }


    @Transactional(readOnly = true)
    public List<SpecificationType> findMultiple(List<Integer> ids) {
        return specificationTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<SpecificationType> getRootTypes() {
        return specificationTypeRepository.findByParentTypeIsNullOrderByNameCreatedDateAsc();
    }

    @Transactional(readOnly = true)
    public List<SpecificationType> getChildren(Integer parent) {
        return specificationTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(parent);
    }

    @Transactional(readOnly = true)
    public List<SpecificationType> getClassificationTree() {
        List<SpecificationType> types = getRootTypes();
        for (SpecificationType type : types) {
            visitChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<SpecificationType> getSpecificationTypesWithAttributes() {
        List<SpecificationType> types = getRootTypes();
        for (SpecificationType type : types) {
            if (type.getParentType() != null) {
                type.setParentTypeReference(specificationTypeRepository.findOne(type.getParentType()));
            }
            type.setAttributes(rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(type.getId()));
        }
        return types;
    }

    private void visitChildren(SpecificationType parent) {
        List<SpecificationType> children = getChildren(parent.getId());
        for (SpecificationType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    @Transactional(readOnly = true)
    public List<Integer> getAllSubTypes(Integer parent) {
        List<Integer> subtypes = new ArrayList<>();
        subtypes.add(parent);
        List<SpecificationType> children = getChildren(parent);
        children.forEach(item -> {
            List<Integer> childSubtypes = getAllSubTypes(item.getId());
            subtypes.addAll(childSubtypes);
        });
        return subtypes;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecificationType> getAll() {
        return specificationTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecificationType> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        return specificationTypeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<RmObjectTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return rmObjectTypeAttributeRepository.findByRmObjectTypeOrderBySeq(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    @Transactional(readOnly = true)
    public List<RmObjectTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<RmObjectTypeAttribute> collector = new ArrayList<>();
        List<RmObjectTypeAttribute> atts = rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<RmObjectTypeAttribute> collector, Integer typeId) {
        SpecificationType rmObjectType = specificationTypeRepository.findOne(typeId);
        if (rmObjectType != null) {
            Integer parentType = rmObjectType.getParentType();
            if (parentType != null) {
                List<RmObjectTypeAttribute> atts = rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Transactional(readOnly = true)
    public RmObjectTypeAttribute getAttribute(Integer id) {
        return rmObjectTypeAttributeRepository.findOne(id);
    }

    public RmObjectTypeAttribute createAttribute(RmObjectTypeAttribute attribute) {
        List<RmObjectTypeAttribute> rmObjectTypeAttributes = rmObjectTypeAttributeRepository.findByRmObjectType(attribute.getRmObjectType());
        if (rmObjectTypeAttributes.size() >= 0) {
            attribute.setSeq(rmObjectTypeAttributes.size() + 1);
        }

        RmObjectTypeAttribute rmObjectTypeAttribute = rmObjectTypeAttributeRepository.save(attribute);
        SpecificationType rmObjectType = specificationTypeRepository.findOne(attribute.getRmObjectType());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.SPECIFICATIONTYPE, rmObjectTypeAttribute));
        return rmObjectTypeAttribute;
    }

    public RmObjectTypeAttribute updateAttribute(RmObjectTypeAttribute attribute) {
        RmObjectTypeAttribute existAttribute = rmObjectTypeAttributeRepository.findOne(attribute.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.SPECIFICATIONTYPE, existAttribute, attribute));
        RmObjectTypeAttribute rmObjectTypeAttribute = rmObjectTypeAttributeRepository.save(attribute);
        return rmObjectTypeAttribute;

    }

    public void deleteAttribute(Integer id) {
        RmObjectTypeAttribute attribute = rmObjectTypeAttributeRepository.findOne(id);
        List<RmObjectTypeAttribute> rmObjectTypeAttributes = rmObjectTypeAttributeRepository.findByRmObjectType(attribute.getRmObjectType());
        if (rmObjectTypeAttributes.size() > 0) {
            for (RmObjectTypeAttribute rmObjectTypeAttribute : rmObjectTypeAttributes) {
                if (rmObjectTypeAttribute.getSeq() > attribute.getSeq()) {
                    rmObjectTypeAttribute.setSeq(rmObjectTypeAttribute.getSeq() - 1);
                    rmObjectTypeAttribute = rmObjectTypeAttributeRepository.save(rmObjectTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.SPECIFICATIONTYPE, attribute));
        rmObjectTypeAttributeRepository.delete(id);
    }


    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getAllItemTypeAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public SpecificationType getItemTypeName(String name) {
        return specificationTypeRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Page<Specification> getType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        SpecificationType type = specificationTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return specificationRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, SpecificationType type) {
        if (type != null) {
            collector.add(type.getId());
            List<SpecificationType> children = specificationTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(type.getId());
            for (SpecificationType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    /*--------------------- Specification Methods  -------------------------------*/

    public Specification createSpecification(Specification specification) {
        Specification existSpec = specificationRepository.findByNameAndObjectTypeAndLatestTrue(specification.getName(), ObjectType.valueOf(PLMObjectType.SPECIFICATION.toString()));
        if (existSpec != null) {
            throw new CassiniException(messageSource.getMessage("specification_already_exist", null, "Specification already exist", LocaleContextHolder.getLocale()));
        }
        Integer workflowDef = null;
        if (specification.getWorkflowDefId() != null) {
            workflowDef = specification.getWorkflowDefId();
        }
        SpecificationType specificationType = specificationTypeRepository.findOne(specification.getType().getId());
        Lov lov = specificationType.getRevisionSequence();
        PLMLifeCyclePhase lifeCyclePhase = specificationType.getLifecycle().getPhaseByType(LifeCyclePhaseType.PRELIMINARY);
        specification.setLifecyclePhase(lifeCyclePhase);
        specification.setLatest(true);
        specification.setRevision(lov.getDefaultValue());
        specification.setType(specificationType);
        autoNumberService.saveNextNumber(specification.getType().getNumberSource().getId(), specification.getObjectNumber());
        specification = specificationRepository.save(specification);
        if (workflowDef != null) {
            attachSpecWorkflow(specification.getId(), workflowDef);
        }
        //Create Specification Revision History
        Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
        SpecificationRevisionStatusHistory revisionStatusHistory = new SpecificationRevisionStatusHistory();
        revisionStatusHistory.setSpecification(specification.getId());
        revisionStatusHistory.setFromStatus(specification.getLifecyclePhase());
        revisionStatusHistory.setToStatus(specification.getLifecyclePhase());
        revisionStatusHistory.setRevision(specification.getRevision());
        revisionStatusHistory.setTimeStamp(new Date());
        revisionStatusHistory.setUpdatedBy(personId);
        revisionStatusHistory = specificationRevisionStatusHistoryRepository.save(revisionStatusHistory);
        return specification;
    }

    public Specification updateSpecification(Specification specification) {
        checkNotNull(specification);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        Specification existSpec = specificationRepository.findOne(specification.getId());
        if (existSpec != null && !existSpec.getId().equals(specification.getId())) {
            throw new CassiniException(messageSource.getMessage("specification_already_exist", null, "Specification already exist", LocaleContextHolder.getLocale()));
        }
        String message = null;
        if (!existSpec.getName().equals(specification.getName())) {
            message = specification.getObjectNumber() + " Name '" + existSpec.getName() + "' changed by " + person.getFullName() + "to ' "
                    + specification.getName() + " '";
        }
        if (existSpec.getDescription() != null && existSpec.getDescription() != "") {
            if (specification.getDescription() != null && specification.getDescription() != "") {
                if (!existSpec.getDescription().equals(specification.getDescription())) {
                    message = specification.getObjectNumber() + " " + " Description : '" + existSpec.getDescription() + "' changed by " + person.getFullName() + " to ' "
                            + specification.getDescription() + " '";
                }
            } else {
                message = specification.getObjectNumber() + " " + " Description : '" + existSpec.getDescription() + "' removed by " + person.getFullName();
            }

        } else {
            if (specification.getDescription() != null) {
                message = specification.getObjectNumber() + " " + " Description : '" + specification.getDescription() + "' added by " + person.getFullName();
            }
        }
        String mailSubject = specification.getObjectNumber() + "  " + existSpec.getLifecyclePhase().getPhase() + " Notification";
        if (message != null) {
            sendSpecSubscribeNotification(specification, message, mailSubject);
        }
        return specificationRepository.save(specification);

    }

    private void sendSpecificationSubscribeNotification(Specification item, String message, String mailSubject) {
        List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(item.getId());
        String[] recipientAddress = new String[subscribes.size()];
        String email = "";
        if (subscribes.size() > 0) {
            for (int i = 0; i < subscribes.size(); i++) {
                PLMSubscribe subscribe = subscribes.get(i);
                if (email == "") {
                    email = subscribe.getPerson().getEmail();
                } else {
                    email = email + "," + subscribe.getPerson().getEmail();
                }
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            final String messageContent = message;
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("message", messageContent);
                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);
                mail.setMailSubject(mailSubject);
                EmailTemplateConfiguration emailTemplateConfiguration = eMailTemplateConfigRepository.findByTemplateName("subscribeSpecificationNotification.html");
                Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.LOGO");
                if (preference != null) {
                    if (preference.getCustomLogo() != null) {
                        URL url1 = ItemService.class
                                .getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                        File file = new File(url1.getPath());
                        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                            outputStream.write(preference.getCustomLogo());
                            model.put("companyLogo", ItemService.class.getClassLoader().getResource("templates/email/share/" + "dummy_logo.png"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if (emailTemplateConfiguration != null) {
                    if (emailTemplateConfiguration.getTemplateSourceCode() != null && emailTemplateConfiguration.getTemplateSourceCode() != "") {
                        byte[] data = DatatypeConverter.parseBase64Binary(emailTemplateConfiguration.getTemplateSourceCode());
                        URL url1 = ItemService.class
                                .getClassLoader().getResource("templates/email/share/" + "customTemplate.html");
                        File file = new File(url1.getPath());
                        try {
                            OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
                            Writer writer = new OutputStreamWriter(outputStream);
                            writer.write(emailTemplateConfiguration.getTemplateSourceCode());
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mail.setTemplatePath("email/share/customTemplate.html");
                    } else {
                        mail.setTemplatePath("email/subscribeSpecificationNotification.html");
                    }
                } else {
                    mail.setTemplatePath("email/subscribeSpecificationNotification.html");
                }
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    public void saveSpecAttributes(List<RmObjectAttribute> attributes) {
        for (RmObjectAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null ||
                    attribute.getStringValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue() || attribute.getHyperLinkValue() != null ||
                    attribute.getLongTextValue() != null || attribute.getRichTextValue() != null) {
                rmObjectAttributeRepository.save(attribute);
            }
        }
    }

    public RmObjectAttribute saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        Specification specification = specificationRepository.findOne(objectId);
        RmObjectAttribute rmObjectAttribute = new RmObjectAttribute();
        if (specification != null) {
            rmObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    rmObjectAttribute.setImageValue(file.getBytes());
                    rmObjectAttribute = rmObjectAttributeRepository.save(rmObjectAttribute);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return rmObjectAttribute;
    }

    @Transactional(readOnly = true)
    public Page<Specification> findAllSpecifications(Pageable pageable) {
        Page<Specification> specifications = specificationRepository.findByLatestTrueOrderByModifiedDateDesc(pageable);
        for (Specification specification : specifications.getContent()) {
            List<PLMSubscribe> subscribes = subscribeRepository.findByObjectIdAndSubscribeTrue(specification.getId());
            specification.setSubscribes(subscribes);
        }
        return specifications;
    }

    @Transactional(readOnly = true)
    public Specification findById(Integer id) {
        Specification specification = specificationRepository.findOne(id);
        List<SpecRequirement> requirements = specRequirementRepository.findBySpecification(specification.getId());
        SpecPermission specPermission = specPermissionRepository.findBySpecificationAndSpecUser(specification.getId(), sessionWrapper.getSession().getLogin().getPerson().getId());
        specification.setTotalReqs(requirements.size());
        specification.setNone(0);
        specification.setPending(0);
        specification.setFinished(0);
        specification.setSpecPermission(specPermission);
        for (SpecRequirement requirement : requirements) {
            if (requirement.getRequirement().getStatus().equals(RequirementStatus.NONE)) {
                specification.setNone(specification.getNone() + 1);
            } else if (requirement.getRequirement().getStatus().equals(RequirementStatus.PENDING)) {
                specification.setPending(specification.getPending() + 1);
            } else if (requirement.getRequirement().getStatus().equals(RequirementStatus.FINISHED)) {
                specification.setFinished(specification.getFinished() + 1);
            }
        }

        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(specification.getId());
        if (workflow != null) {
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    specification.setStartWorkflow(true);
                }
            }
        }

        return specification;
    }

    /*------------------Specification file Methods---------------*/

    @Transactional(readOnly = true)
    public RmObjectFile getRmObjectFile(Integer id) {
        return rmObjectFileRepository.findOne(id);
    }

    @Transactional
    public void deleteRmObjectFile(Integer itemId, Integer id) {
        checkNotNull(id);
        RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(id);
        RmObject rmObject = rmObjectRepository.findOne(rmObjectFile.getObject());
        String fileName = rmObjectFile.getName() + " - Version " + rmObjectFile.getVersion();
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        String fNames = null;
        String message = null;
        String mailSubject = null;
        List<RmObjectFile> itemFiles = rmObjectFileRepository.findAllByObjectAndName(itemId, rmObjectFile.getName());
        for (RmObjectFile itemFile : itemFiles) {
            if (itemFile == null) {
                throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
            }
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + rmObjectFile.getObject();
            fileSystemService.deleteDocumentFromDisk(rmObjectFile.getId(), dir);
            if (fNames == null) {
                fNames = itemFile.getName();
            } else {
                fNames = fNames + " , " + itemFile.getName();
            }
            rmObjectFileRepository.delete(rmObjectFile.getId());
        }
        mailSubject = rmObject.getObjectNumber() + "Notification";
        message = rmObjectFile.getName() + "file deleted by " + person.getFullName() + "to (" + rmObject.getObjectNumber() + ")";
        if (rmObject.getObjectType().toString().equals("SPECIFICATION")) {
            Specification specification = specificationRepository.findOne(rmObject.getId());
            sendSpecSubscribeNotification(specification, message, mailSubject);
        }
        if (rmObject.getObjectType().toString().equals("REQUIREMENT")) {
            Requirement requirement = requirementRepository.findOne(rmObject.getId());
            sendReqSubscribeNotification(requirement, message, mailSubject);
        }
    }

    @Transactional(readOnly = true)
    public List<RmObjectFile> findByItem(RmObject item) {
        List<RmObjectFile> projectFiles = rmObjectFileRepository.findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(item.getId());
        projectFiles.forEach(changeFile -> {
            changeFile.setParentObject(PLMObjectType.SPECIFICATION);
            if (changeFile.getFileType().equals("FOLDER")) {
                changeFile.setCount(rmObjectFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(changeFile.getId()).size());
            }
        });
        return projectFiles;
    }

    @Transactional(readOnly = true)
    public List<RmObjectFile> findBySpecandFileName(Integer item, String name) {
        return rmObjectFileRepository.findByObjectAndNameContainingIgnoreCaseAndLatestTrue(item, name);
    }

    public RmObjectFile create(RmObjectFile pdmItemFile) {
        pdmItemFile.setId(null);
        pdmItemFile = rmObjectFileRepository.save(pdmItemFile);
        return pdmItemFile;
    }

    public RmObjectFile update(RmObjectFile pdmItemFile) {
        pdmItemFile = rmObjectFileRepository.save(pdmItemFile);
        return pdmItemFile;
    }

    public PLMFile updateFile(Integer id, RmObjectFile pdmItemFile) {
        PLMFile file = fileRepository.findOne(id);
        RmObjectFile itemFile = rmObjectFileRepository.findOne(file.getId());
        if (file != null) {
            file.setDescription(pdmItemFile.getDescription());
            file.setLocked(pdmItemFile.getLocked());
            file.setLockedBy(pdmItemFile.getLockedBy());
            file.setLockedDate(pdmItemFile.getLockedDate());
            file = fileRepository.save(file);
        }
        return file;
    }

    public RmObjectFile updateFileName(Integer id, String newFileName) throws IOException {
        RmObjectFile file1 = rmObjectFileRepository.findOne(id);
        file1.setLatest(false);
        RmObjectFile plmProjectFile = rmObjectFileRepository.save(file1);
        RmObjectFile rmObjectFile = (RmObjectFile) Utils.cloneObject(plmProjectFile, RmObjectFile.class);
        if (rmObjectFile != null) {
            rmObjectFile.setId(null);
            rmObjectFile.setName(newFileName);
            rmObjectFile.setVersion(file1.getVersion() + 1);
            rmObjectFile.setLatest(true);
            rmObjectFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            rmObjectFile = rmObjectFileRepository.save(rmObjectFile);
            qualityFileService.copyFileAttributes(file1.getId(), rmObjectFile.getId());
            String dir = "";
            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + rmObjectFile.getObject();
            dir = dir + File.separator + rmObjectFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + rmObjectFile.getObject() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
        }
        return rmObjectFile;
    }

    public RmObject getRevision(Integer id) {
        checkNotNull(id);
        return rmObjectRepository.findOne(id);
    }

    @Transactional
    public List<RmObjectFile> uploadRmObjectFiles(Integer objectId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<RmObjectFile> uploadedFiles = new ArrayList<>();
        String fileNames = null;
        String fNames = null;
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String(file.getOriginalFilename().getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    RmObjectFile rmObjectFile = rmObjectFileRepository.findByObjectAndNameAndLatestTrue(objectId, name);
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (rmObjectFile != null) {
                        rmObjectFile.setLatest(false);
                        Integer oldVersion = rmObjectFile.getVersion();
                        version = oldVersion + 1;
                        oldFile = rmObjectFile.getId();
                        autoNumber1 = rmObjectFile.getFileNo();
                        rmObjectFileRepository.save(rmObjectFile);

                    }
                    if (rmObjectFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    rmObjectFile = new RmObjectFile();
                    rmObjectFile.setName(name);
                    rmObjectFile.setFileNo(autoNumber1);
                    rmObjectFile.setCreatedBy(login.getPerson().getId());
                    rmObjectFile.setModifiedBy(login.getPerson().getId());
                    rmObjectFile.setVersion(version);
                    rmObjectFile.setObject(objectId);
                    rmObjectFile.setSize(file.getSize());
                    rmObjectFile.setFileType("FILE");
                    rmObjectFile = rmObjectFileRepository.save(rmObjectFile);
                    if (rmObjectFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, rmObjectFile.getId());
                    }
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    if (fileNames == null) {
                        fNames = rmObjectFile.getName();
                        fileNames = rmObjectFile.getName() + " - Version : " + rmObjectFile.getVersion();
                    } else {
                        fNames = fNames + "  , " + rmObjectFile.getName();
                        fileNames = fileNames + " , " + rmObjectFile.getName() + " - Version : " + rmObjectFile.getVersion();
                    }
                    String path = dir + File.separator + rmObjectFile.getId();
                    saveDocumentToDisk(file, path);
                    uploadedFiles.add(rmObjectFile);
                }
                Person person = sessionWrapper.getSession().getLogin().getPerson();
                Specification specification = specificationRepository.findOne(objectId);
                if (specification != null) {
                    fileNames = fileNames + " files added by " + person.getFullName() + " to (" + specification.getObjectNumber() + " - " + specification.getName() + " : Rev " + specification.getRevision() + " : " + specification.getLifecyclePhase().getPhase() + " ) Specification";
                    String mailSubject = specification.getObjectNumber() + " : " + " Rev " + specification.getRevision() + " : " + specification.getLifecyclePhase().getPhase() + " Notification";
                    sendSpecSubscribeNotification(specification, fileNames, mailSubject);
                } else {
                    Requirement requirement = requirementRepository.findOne(objectId);
                    fileNames = fileNames + " files added by " + person.getFullName() + " to (" + requirement.getObjectNumber() + " - " + requirement.getName() + " : Rev " + requirement.getVersion() + " ) Requirement";
                    String mailSubject = requirement.getObjectNumber() + " : " + " Rev " + requirement.getVersion() + " Notification";
                    sendReqSubscribeNotification(requirement, fileNames, mailSubject);

                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploadedFiles;
    }

    public List<RmObjectFile> replaceRmObjectFiles(Integer specId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<RmObjectFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        RmObject object = rmObjectRepository.findOne(specId);
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        String fNames = null;
        String name = null;
        RmObjectFile rmObjectFile1 = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    RmObjectFile rmObjectFile = null;
                    rmObjectFile1 = rmObjectFileRepository.findOne(fileId);
                    if (rmObjectFile1 != null && rmObjectFile1.getParentFile() != null) {
                        rmObjectFile = rmObjectFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        rmObjectFile = rmObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(specId, name);
                    }
                    if (rmObjectFile != null) {
                        comments = commentRepository.findAllByObjectId(rmObjectFile.getId());
                    }
                    if (rmObjectFile1 != null) {
                        rmObjectFile1.setLatest(false);
                        rmObjectFile1 = rmObjectFileRepository.save(rmObjectFile1);
                    }
                    if (rmObjectFile != null) {
                        comments = commentRepository.findAllByObjectId(rmObjectFile.getId());
                    }
                    rmObjectFile = new RmObjectFile();
                    rmObjectFile.setName(name);
                    if (rmObjectFile1 != null && rmObjectFile1.getParentFile() != null) {
                        rmObjectFile.setParentFile(rmObjectFile1.getParentFile());
                    }
                    if (rmObjectFile1 != null) {
                        rmObjectFile.setFileNo(rmObjectFile1.getFileNo());
                        rmObjectFile.setVersion(rmObjectFile1.getVersion() + 1);
                        rmObjectFile.setReplaceFileName(rmObjectFile1.getName() + " Replaced to " + name);
                    }
                    rmObjectFile.setCreatedBy(login.getPerson().getId());
                    rmObjectFile.setModifiedBy(login.getPerson().getId());
                    rmObjectFile.setObject(specId);
                    rmObjectFile.setSize(file.getSize());
                    rmObjectFile.setFileType("FILE");
                    rmObjectFile = rmObjectFileRepository.save(rmObjectFile);
                    if (rmObjectFile1 != null) {
                        qualityFileService.copyFileAttributes(rmObjectFile1.getId(), rmObjectFile.getId());
                    }
                    String dir = "";
                    if (rmObjectFile1 != null && rmObjectFile1.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(specId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + specId;
                    }
                    String path = dir + File.separator + rmObjectFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(rmObjectFile);
                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    private String getReplaceFileSystemPath(Integer projectId, Integer fileId) {
        String path = "";
        RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(fileId);
        if (rmObjectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(projectId, rmObjectFile.getParentFile(), path);
        } else {
            path = File.separator + projectId;
        }
        return path;
    }

    private String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (rmObjectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, rmObjectFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + rmObjectFile.getId();
        }
        return path;
    }

    @Transactional(readOnly = true)
    public List<RmObjectFile> getAllFileVersions(Integer itemRevision, String name) {
        List<RmObjectFile> RmObjectFiles = rmObjectFileRepository.findAllByObjectAndName(itemRevision, name);
        for (RmObjectFile RmObjectFile : RmObjectFiles) {
            List<Comment> comments = commentRepository.findAllByObjectId(RmObjectFile.getId());
            RmObjectFile.setComments(comments);
        }
        return RmObjectFiles;
    }

    protected void saveDocumentToDisk(MultipartFile multipartFile, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    protected void saveFileToDisk(File fileToSave, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(new FileInputStream(fileToSave), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    @Transactional(readOnly = true)
    public File getRmObjectFile(Integer itemId, Integer fileId) {
        checkNotNull(itemId);
        checkNotNull(fileId);
        RmObject revision = rmObjectRepository.findOne(itemId);
        if (revision == null) {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
        RmObjectFile itemFile = rmObjectFileRepository.findOne(fileId);
        if (itemFile == null) {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(itemId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
    }

    public RmObjectFile copyItemFile(RmObject oldItem, RmObject newItem, RmObjectFile itemFile) {
        RmObjectFile newItemFile = null;
        File file = getRmObjectFile(oldItem.getId(), itemFile.getId());
        if (file != null) {
            newItemFile = new RmObjectFile();
            Login login = sessionWrapper.getSession().getLogin();
            newItemFile.setName(itemFile.getName());
            newItemFile.setCreatedBy(login.getPerson().getId());
            newItemFile.setModifiedBy(login.getPerson().getId());
            //newItemFile.setItem(newItem);
            newItemFile.setVersion(itemFile.getVersion());
            newItemFile.setSize(itemFile.getSize());
            newItemFile.setLatest(itemFile.getLatest());
            newItemFile = rmObjectFileRepository.save(newItemFile);
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + newItem.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            String path = dir + File.separator + newItemFile.getId();
            saveFileToDisk(file, path);
        }
        return newItemFile;
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer itemId, Integer fileId) {
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        RmObjectFile plmItemFile = rmObjectFileRepository.findOne(fileId);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        return plmFileDownloadHistory;
    }

    @Transactional(readOnly = true)
    public List<PLMFileDownloadHistory> getDownloadHistory(Integer fileId) {
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateAsc(fileId);
        return fileDownloadHistories;
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllFileComments(Integer itemId, Integer fileId, ObjectType objectType) {
        List<Comment> comments = new ArrayList<>();
        List<Comment> fileComments = commentRepository.findByObjectIdAndObjectType(fileId, objectType);
        RmObjectFile RmObjectFile = rmObjectFileRepository.findOne(fileId);
        if (fileComments.size() > 0) {
            comments.addAll(fileComments);
        }
        List<RmObjectFile> files = rmObjectFileRepository.findByObjectAndNameAndLatestFalseOrderByCreatedDateDesc(RmObjectFile.getObject(), RmObjectFile.getName());
        if (files.size() > 0) {
            for (RmObjectFile file : files) {
                List<Comment> commentList = commentRepository.findByObjectIdAndObjectType(file.getId(), objectType);
                if (commentList.size() > 0) {
                    comments.addAll(commentList);
                }
            }
        }
        return comments;
    }

    @Transactional(readOnly = true)
    public List<RmObjectFile> getAllFileVersionComments(Integer itemId, Integer fileId, ObjectType objectType) {
        List<RmObjectFile> itemFiles = new ArrayList<>();
        RmObjectFile RmObjectFile = rmObjectFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(RmObjectFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(RmObjectFile.getId());
        if (comments.size() > 0) {
            RmObjectFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            RmObjectFile.setDownloadHistories(fileDownloadHistories);
        }
        itemFiles.add(RmObjectFile);
        List<RmObjectFile> files = rmObjectFileRepository.findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(RmObjectFile.getObject(), RmObjectFile.getFileNo());
        if (files.size() > 0) {
            for (RmObjectFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<PLMFileDownloadHistory> oldFileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                itemFiles.add(file);
            }
        }
        return itemFiles;
    }

    @Transactional(readOnly = true)
    public List<RmObjectAttribute> getItemAttributes(Integer itemId) {
        return rmObjectAttributeRepository.findByObjectId(itemId);
    }

    public RmObjectAttribute createSpecAttribute(RmObjectAttribute attribute) {
        return rmObjectAttributeRepository.save(attribute);
    }

    public RmObjectAttribute updateSpecAttribute(RmObjectAttribute attribute) {
        attribute = rmObjectAttributeRepository.save(attribute);
        return attribute;
    }

    /*----------- SpecSection --------------*/

    public SpecSection createSpecSection(SpecSection specSection) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        Specification specification = specificationRepository.findOne(specSection.getSpecification());
        if (specSection.getParent() == null) {
            List<SpecSection> specSectionList = specSectionRepository.findBySpecificationAndParentIsNullOrderByCreatedDateAsc(specSection.getSpecification());
            int seqNumber = specSectionList.size() + 1;
            specSection.setSeqNumber(String.valueOf(seqNumber));
            specSection = specSectionRepository.save(specSection);
            String message = person.getFullName() + " " + "created " + specSection.getName() + " " + "Section  under " + specification.getObjectNumber() + "  " + specification.getLifecyclePhase().getPhase() + " Notification";
            String mailSubject = specification.getObjectNumber() + "  " + specification.getLifecyclePhase().getPhase() + " Notification";
            sendSpecSubscribeNotification(specification, message, mailSubject);

        } else {
            List<SpecSection> specSectionList = specSectionRepository.findByParentOrderByCreatedDateDesc(specSection.getParent());
            List<SpecRequirement> reqChildren = specRequirementRepository.findByParentOrderByCreatedDateDesc(specSection.getParent());
            if (specSectionList.size() == 0 && reqChildren.size() == 0) {
                SpecSection specSection1 = specSectionRepository.findOne(specSection.getParent());
                specSection1.setCanAddRequirement(false);
                specSectionRepository.save(specSection1);
                specSection.setSeqNumber(specSection1.getSeqNumber() + ".1");
                specSectionRepository.save(specSection);
            } else {
                SpecSection specSection1 = specSectionRepository.findOne(specSection.getParent());
                specSection1.setCanAddRequirement(false);
                specSectionRepository.save(specSection1);
                specSection.setSeqNumber(specSection1.getSeqNumber() + "." + ((specSectionList.size() + reqChildren.size()) + 1));
                specSectionRepository.save(specSection);
            }
            String message = person.getFullName() + " " + "created " + specSection.getName() + " " + "Section  under " + specification.getObjectNumber() + "  " + specification.getLifecyclePhase().getPhase() + " Notification";
            String mailSubject = specification.getObjectNumber() + "  " + specification.getLifecyclePhase().getPhase() + " Notification";
            sendSpecSubscribeNotification(specification, message, mailSubject);

        }
        return specSection;
    }

    public SpecRequirement createSpecRequirement(SpecRequirement specRequirement) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        Requirement requirement = requirementsService.createRequirement(specRequirement.getRequirement());
        Specification specification = specificationRepository.findOne(specRequirement.getSpecification());
        if (specRequirement.getParent() == null) {
            List<SpecSection> specSectionList = specSectionRepository.findBySpecificationAndParentIsNullOrderByCreatedDateAsc(specRequirement.getSpecification());
            int seqNumber = specSectionList.size() + 1;
            specRequirement.setSeqNumber(String.valueOf(seqNumber));
            specRequirement = specRequirementRepository.save(specRequirement);
            String message = person.getFullName() + " " + "created " + requirement.getName() + " " + "Requirement  under " + specification.getObjectNumber() + "  " + " Notification";
            String mailSubject = specification.getObjectNumber() + "  " + " Notification";
            sendSpecSubscribeNotification(specification, message, mailSubject);

        } else {
            SpecSection specSection1 = specSectionRepository.findOne(specRequirement.getParent());
            specSection1.setCanAddSection(false);
            specSectionRepository.save(specSection1);
            List<SpecSection> children = specSectionRepository.findByParentOrderByCreatedDateDesc(specRequirement.getParent());
            List<SpecRequirement> reqChildren = specRequirementRepository.findByParentOrderByCreatedDateDesc(specRequirement.getParent());
            String seqNumber;
            if (children.size() == 0 && reqChildren.size() == 0) {
                seqNumber = specSection1.getSeqNumber() + ".1";
            } else {
                seqNumber = specSection1.getSeqNumber() + "." + ((children.size() + reqChildren.size()) + 1);
            }
            specRequirement.setSeqNumber(seqNumber);
            specRequirementRepository.save(specRequirement);
            String message = person.getFullName() + " " + "created " + requirement.getName() + " " + "Requirement  under " + specSection1.getName() + "  " + " Notification";
            String mailSubject = requirement.getObjectNumber() + "  " + " Notification";
            sendSpecSubscribeNotification(specification, message, mailSubject);

        }
        specRequirement.setRequirement(requirement);
        // specRequirement.setObjectType(SpecElementType.REQUIREMENT);
        specRequirement = specRequirementRepository.save(specRequirement);
        if (requirement.getAssignedTo() != null) {
            sendSubscribeNotification(requirement.getAssignedTo(), requirement);
        }
        return specRequirement;
    }

    @Transactional(readOnly = true)
    public List<SpecSection> getAllSpecSections(Integer specId) {
        List<SpecSection> specSectionList = specSectionRepository.findBySpecificationAndParentIsNullOrderByCreatedDateAsc(specId);
        for (SpecSection specSection : specSectionList) {
            Integer integer = 0;
            integer = getSectionLevelRequirementEdit(integer, specSection);
            specSection.setRequirementsEdit(integer);

        }
        return specSectionList;
    }

    private Integer getSectionLevelRequirementEdit(Integer integer, SpecElement specSection) {
        List<SpecElement> sectionChildren = specElementRepository.findByParentOrderByCreatedDateAsc(specSection.getId());
        for (SpecElement child : sectionChildren) {
            if (child.getType().equals(SpecElementType.REQUIREMENT)) {
                SpecRequirement specRequirement = specRequirementRepository.findOne(child.getId());
                List<RequirementEdit> requirementEdit = requirementEditRepository.findByRequirementOrderByEditedDateAsc(specRequirement.getRequirement().getId());
                integer = integer + requirementEdit.size();
            } else {
                integer = getSectionLevelRequirementEdit(integer, child);
            }
        }
        return integer;
    }

    @Transactional(readOnly = true)
    public List<SpecElement> getSectionChildren(Integer specId, Integer sectionId) {
        List<SpecElement> children = new ArrayList<>();
        List<SpecElement> elements = specElementRepository.findByParentOrderByCreatedDateAsc(sectionId);
        for (SpecElement elem : elements) {
            elem.getChildren().addAll(getSectionChildren(specId, elem.getId()));
            if (elem.getType() == SpecElementType.SECTION) {
                SpecSection section = specSectionRepository.findOne(elem.getId());
                Integer integer = 0;
                section.setRequirementsEdit(getSectionLevelRequirementEdit(integer, section));
                children.add(section);
            } else {
                SpecRequirement specRequirement = specRequirementRepository.findOne(elem.getId());
                List<RequirementEdit> requirementEdits = requirementEditRepository.findByRequirementOrderByEditedDateDesc(specRequirement.getRequirement().getId());
                if (requirementEdits.size() >= 0) {
                    Integer integer = 0;
                    integer = requirementEdits.size();
                    specRequirement.setReqEdits(integer);
                }
                RequirementEdit finalAcceptEdit = requirementEditRepository.findByRequirementAndStatus(specRequirement.getRequirement().getId(), RequirementEditStatus.FINAL);
                if (finalAcceptEdit != null) {
                    specRequirement.getRequirement().setFinalAcceptEdit(true);
                }
                children.add(specRequirement);
            }

        }
        return children;
    }

    private void sendSubscribeNotification(Person person, Requirement requirement) {
        if (person.getEmail() != null) {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            Specification specification = specificationRepository.findOne(requirement.getSpecification());
            Person createdBy = sessionWrapper.getSession().getLogin().getPerson();
            String message = createdBy.getFullName() + " has assigned the following requirement";
            String plannedFinishDate = null;
            if (requirement.getPlannedFinishDate() != null) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                plannedFinishDate = df.format(requirement.getPlannedFinishDate());
            }
            final String plannedFinishDate1 = plannedFinishDate;
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("requirement", requirement);
                model.put("person", person);
                model.put("message", message);
                model.put("plannedFinishDate", plannedFinishDate1);
                model.put("specification", specification);
                model.put("mode", "CREATE");
                Mail mail = new Mail();
                mail.setMailTo(person.getEmail());
                mail.setMailSubject(message);
                EmailTemplateConfiguration emailTemplateConfiguration = eMailTemplateConfigRepository.findByTemplateName("requirementTemplate.html");
                Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.LOGO");
                if (preference != null) {
                    if (preference.getCustomLogo() != null) {
                        URL url1 = ItemService.class
                                .getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                        File file = new File(url1.getPath());
                        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                            outputStream.write(preference.getCustomLogo());
                            model.put("companyLogo", ItemService.class.getClassLoader().getResource("templates/email/share/" + "dummy_logo.png"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if (emailTemplateConfiguration != null) {
                    if (emailTemplateConfiguration.getTemplateSourceCode() != null && emailTemplateConfiguration.getTemplateSourceCode() != "") {
                        byte[] data = DatatypeConverter.parseBase64Binary(emailTemplateConfiguration.getTemplateSourceCode());
                        URL url1 = ItemService.class
                                .getClassLoader().getResource("templates/email/share/" + "customTemplate.html");
                        File file = new File(url1.getPath());
                        try {
                            OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
                            Writer writer = new OutputStreamWriter(outputStream);
                            writer.write(emailTemplateConfiguration.getTemplateSourceCode());
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mail.setTemplatePath("email/share/customTemplate.html");
                    } else {
                        mail.setTemplatePath("email/requirementTemplate.html");
                    }
                } else {
                    mail.setTemplatePath("email/requirementTemplate.html");
                }
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    public Requirement updateRequirement(Requirement requirement) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        Requirement existRequirement = requirementRepository.findOne(requirement.getId());
        String mailSubject = requirement.getObjectNumber() + "  " + " Notification";
        String message = null;
        if (requirement.getAssignedTo() != null) {
            requirement.setStatus(RequirementStatus.PENDING);
        }
        if (existRequirement.getAssignedTo() != null && requirement.getAssignedTo() != null) {
            if (!existRequirement.getAssignedTo().getId().equals(requirement.getAssignedTo().getId())) {
                message = requirement.getObjectNumber() + " " + " Assigned To : '" + existRequirement.getAssignedTo().getFullName() + "' changed by " + person.getFullName() + " to ' "
                        + requirement.getAssignedTo().getFullName() + " '";
                sendReqSubscribeNotification(requirement, message, mailSubject);
            }
        } else {
            if (requirement.getAssignedTo() != null) {
                message = requirement.getObjectNumber() + " " + " Assigned To : '" + requirement.getAssignedTo().getFullName() + "' changed by " + person.getFullName() + " to ' "
                        + requirement.getAssignedTo().getFullName() + " '";
                sendReqSubscribeNotification(requirement, message, mailSubject);
            }
        }

/*------- Mail subscription Part ------------*/
        String date = "";
        String date1 = "";
        if (requirement.getPlannedFinishDate() != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss.S";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            date = simpleDateFormat.format(requirement.getPlannedFinishDate());
        }
        if (existRequirement.getPlannedFinishDate() != null) {
            String pattern = "yyyy-MM-dd HH:mm:ss.S";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            date1 = simpleDateFormat.format(existRequirement.getPlannedFinishDate());
        }
        if (!existRequirement.getName().equals(requirement.getName())) {
            message = requirement.getObjectNumber() + " Name '" + existRequirement.getName() + "' changed by " + person.getFullName() + "to ' "
                    + requirement.getName() + " '";
        } else if (!date1.equals(date)) {
            message = requirement.getObjectNumber() + " Planned FinishDate '" + existRequirement.getPlannedFinishDate() + "' changed by " + person.getFullName() + "to ' "
                    + date + " '";
        } else if (existRequirement.getDescription() != null && existRequirement.getDescription() != "") {
            if (requirement.getDescription() != null && requirement.getDescription() != "") {
                if (!existRequirement.getDescription().equals(requirement.getDescription())) {
                    message = requirement.getObjectNumber() + " " + " Description : '" + existRequirement.getDescription() + "' changed by " + person.getFullName() + " to ' "
                            + requirement.getDescription() + " '";
                }
            } else {
                message = requirement.getObjectNumber() + " " + " Description : '" + existRequirement.getDescription() + "' removed by " + person.getFullName();
            }

        } else {
            if (requirement.getDescription() != null) {
                message = requirement.getObjectNumber() + " " + " Description : '" + requirement.getDescription() + "' added by " + person.getFullName();
            }
        }
        if (message != null) {
            sendReqSubscribeNotification(requirement, message, mailSubject);
        }
        requirement = requirementRepository.save(requirement);
        if (requirement != null) {
            RequirementEdit requirementEdit = requirementEditRepository.findByRequirement(requirement.getId());
            if (requirementEdit != null) {
                requirementEdit.setEditedName(requirement.getName());
                requirementEdit.setEditedDescription(requirement.getDescription());
                requirementEdit = requirementEditRepository.save(requirementEdit);
            }
        }
        return requirement;
    }

    private void sendSpecificationSubscribeNotification(Requirement item, String message, String mailSubject) {
        List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(item.getId());
        String[] recipientAddress = new String[subscribes.size()];
        String email = "";
        if (subscribes.size() > 0) {
            for (int i = 0; i < subscribes.size(); i++) {
                PLMSubscribe subscribe = subscribes.get(i);
                if (email == "") {
                    email = subscribe.getPerson().getEmail();
                } else {
                    email = email + "," + subscribe.getPerson().getEmail();
                }
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            final String messageContent = message;
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("message", messageContent);
                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);
                mail.setMailSubject(mailSubject);
                EmailTemplateConfiguration emailTemplateConfiguration = eMailTemplateConfigRepository.findByTemplateName("subscribeSpecificationNotification.html");
                Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.LOGO");
                if (preference != null) {
                    if (preference.getCustomLogo() != null) {
                        URL url1 = ItemService.class
                                .getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                        File file = new File(url1.getPath());
                        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                            outputStream.write(preference.getCustomLogo());
                            model.put("companyLogo", ItemService.class.getClassLoader().getResource("templates/email/share/" + "dummy_logo.png"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if (emailTemplateConfiguration != null) {
                    if (emailTemplateConfiguration.getTemplateSourceCode() != null && emailTemplateConfiguration.getTemplateSourceCode() != "") {
                        byte[] data = DatatypeConverter.parseBase64Binary(emailTemplateConfiguration.getTemplateSourceCode());
                        URL url1 = ItemService.class
                                .getClassLoader().getResource("templates/email/share/" + "customTemplate.html");
                        File file = new File(url1.getPath());
                        try {
                            OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
                            Writer writer = new OutputStreamWriter(outputStream);
                            writer.write(emailTemplateConfiguration.getTemplateSourceCode());
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mail.setTemplatePath("email/share/customTemplate.html");
                    } else {
                        mail.setTemplatePath("email/subscribeSpecificationNotification.html");
                    }
                } else {
                    mail.setTemplatePath("email/subscribeSpecificationNotification.html");
                }
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }


    public void sendSpecSubscribeNotification(Specification item, String message, String mailSubject) {
        List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(item.getId());
        String[] recipientAddress = new String[subscribes.size()];
        String email = "";
        if (subscribes.size() > 0) {
            for (int i = 0; i < subscribes.size(); i++) {
                PLMSubscribe subscribe = subscribes.get(i);
                if (email == "") {
                    email = subscribe.getPerson().getEmail();
                } else {
                    email = email + "," + subscribe.getPerson().getEmail();
                }
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            final String messageContent = message;
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("message", messageContent);
                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);
                mail.setMailSubject(mailSubject);
                mail.setTemplatePath("email/subscribeSpecificationNotification.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }

    }

    public void sendReqSubscribeNotification(Requirement item, String message, String mailSubject) {
        List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(item.getId());
        String[] recipientAddress = new String[subscribes.size()];
        String email = "";
        if (subscribes.size() > 0) {
            for (int i = 0; i < subscribes.size(); i++) {
                PLMSubscribe subscribe = subscribes.get(i);
                if (email == "") {
                    email = subscribe.getPerson().getEmail();
                } else {
                    email = email + "," + subscribe.getPerson().getEmail();
                }
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            final String messageContent = message;
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("message", messageContent);
                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);
                mail.setMailSubject(mailSubject);
                mail.setTemplatePath("email/subscribeRequirementNotification.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }

    }

    public Requirement finishRequirement(Requirement requirement) {
        requirement.setStatus(RequirementStatus.FINISHED);
        requirement.setActualFinishDate(new Date());
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        String mailSubject = requirement.getObjectNumber() + "  " + " Notification";
        String message = null;
        message = requirement.getObjectNumber() + " " + " Status : '" + "PENDING" + "' changed by " + person.getFullName() + " to ' "
                + "FINISHED" + " '";
        sendSpecificationSubscribeNotification(requirement, message, mailSubject);
        requirement = requirementRepository.save(requirement);
        return requirement;
    }

    @Transactional(readOnly = true)
    public Map<String, Integer> getSpecRequirementsCounts(Integer specId) {
        Map<String, Integer> counts = new HashMap<>();
        List<Requirement> reqs = requirementRepository.findBySpecificationAndLatestTrue(specId);
        counts.put("total", reqs.size());
        reqs.forEach(req -> {
            if (req.getStatus() == RequirementStatus.FINISHED) {
                Integer count = counts.get("finished");
                if (count == null) {
                    count = 0;
                }
                count = count + 1;
                counts.put("finished", count);
            } else if (req.getStatus() == RequirementStatus.PENDING) {
                Integer count = counts.get("pending");
                if (count == null) {
                    count = 0;
                }
                count = count + 1;
                counts.put("pending", count);
            }
        });
        return counts;
    }

    @Transactional(readOnly = true)
    public Page<SpecRequirement> getRequirementSearchResults(RequirementSearchCriteria requirementSearchCriteria, Pageable pageable) {
        Predicate predicate = requirementSearchPredicateBuilder.build(requirementSearchCriteria, QSpecRequirement.specRequirement);
        if (predicate != null) {
            Page<SpecRequirement> specRequirements = specRequirementRepository.findAll(predicate, pageable);
            for (SpecRequirement specRequirement : specRequirements.getContent()) {
                List<RequirementEdit> requirementEdits = requirementEditRepository.findByRequirementOrderByEditedDateDesc(specRequirement.getRequirement().getId());
                specRequirement.setReqEdits(requirementEdits.size());
            }
            return specRequirements;
        } else {
            return null;
        }
    }

  /*----------- Specification Deliverables -----------------------*/

    @Transactional(readOnly = true)
    public List<Specification> getProjectSpecificationDeliverablesByProjectId(Integer projectId) {
        List<Specification> specifications = new ArrayList<>();
        List<SpecificationDeliverable> SpecificationDeliverables = specificationDeliverableRepository.findByObjectId(projectId);
        List<Specification> totalSpecifications = specificationRepository.findAll();
        if (SpecificationDeliverables.size() != 0) {
            for (Specification glossary : totalSpecifications) {
                Boolean exist = false;
                for (SpecificationDeliverable glossaryDeliverable : SpecificationDeliverables) {
                    if (glossaryDeliverable.getSpecification().equals(glossary.getId())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    specifications.add(glossary);
                }
            }
        } else {
            specifications.addAll(totalSpecifications);
        }
        return specifications;
    }

    @Transactional(readOnly = true)
    public Page<Specification> getProjectSpecificationDeliverables(Integer project, Pageable pageable, SpecificationBuilderCriteria criteria) {
        criteria.setProject(project);
        List<Specification> specificationList = new ArrayList<>();
        Predicate predicate = specificationDeliverableBuilder.build(criteria, QSpecification.specification);
        Page<Specification> specifications = specificationRepository.findAll(predicate, pageable);
        if (specifications.getContent().size() > 0) {
            for (Specification specification : specifications) {
                if (specification.getLatest()) {
                    specificationList.add(specification);
                }

            }
        }
        Page<Specification> specificationPage = new PageImpl<>(specificationList,
                new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
                specificationList.size());
        return specificationPage;
    }

    public List<SpecificationDeliverable> createProjectSpecificationDeliverables(Integer projectId, String objectType, List<Specification> specifications) {
        List<SpecificationDeliverable> deliverables = new ArrayList<>();
        String specNames = null;
        for (Specification glossary : specifications) {
            SpecificationDeliverable glossaryDeliverable1 = new SpecificationDeliverable();
            glossaryDeliverable1.setObjectId(projectId);
            glossaryDeliverable1.setObjectType(objectType);
            glossaryDeliverable1.setSpecification(glossary);
            deliverables.add(glossaryDeliverable1);
            if (specNames == null) {
                specNames = glossary.getName();
            } else {
                specNames = specNames + " , " + glossary.getName();
            }
        }
        PLMProject project = projectRepository.findOne(projectId);
        if (project != null) {
            deliverables = specificationDeliverableRepository.save(deliverables);
        }
        PLMActivity activity = activityRepository.findOne(projectId);
        if (activity != null) {
            deliverables = specificationDeliverableRepository.save(deliverables);
        }
        PLMTask task = taskRepository.findOne(projectId);
        if (task != null) {
            deliverables = specificationDeliverableRepository.save(deliverables);
        }
        return deliverables;
    }

    @Transactional
    public void deleteSpecificationDeliverable(Integer projectId, Integer specId) {
        Specification specification = specificationRepository.findOne(specId);
        SpecificationDeliverable specificationDeliverable = specificationDeliverableRepository.findByObjectIdAndSpecification(projectId, specification);
        PLMProject project = projectRepository.findOne(projectId);
        if (project != null) {
            specificationDeliverableRepository.delete(specificationDeliverable);
        }
        PLMActivity activity = activityRepository.findOne(projectId);
        if (activity != null) {
            specificationDeliverableRepository.delete(specificationDeliverable);
        }
        PLMTask task = taskRepository.findOne(projectId);
        if (task != null) {
            specificationDeliverableRepository.delete(specificationDeliverable);
        }

    }
    /*-------------------- Specification and Requirement Subscription Methods*/

    public PLMSubscribe subscribeSpecificationAndRequirement(Integer itemId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMSubscribe subscribe = subscribeRepository.findByPersonAndObjectId(person, itemId);
        RmObject rmObject = rmObjectRepository.findOne(itemId);
        if (subscribe == null) {
            subscribe = new PLMSubscribe();
            subscribe.setPerson(person);
            subscribe.setObjectId(itemId);
            subscribe.setSubscribe(true);
            subscribe.setObjectType(rmObject.getObjectType().name());
            subscribe = subscribeRepository.save(subscribe);
        } else {
            if (subscribe.getSubscribe()) {
                subscribe.setSubscribe(false);
                subscribe.setObjectType(rmObject.getObjectType().name());
                subscribe = subscribeRepository.save(subscribe);
            } else {
                subscribe.setSubscribe(true);
                subscribe.setObjectType(rmObject.getObjectType().name());
                subscribe = subscribeRepository.save(subscribe);
            }
        }
        return subscribe;
    }

    @Transactional(readOnly = true)
    public PLMSubscribe getSubscribeSpecificationAndRequirementByPerson(Integer itemId, Integer personId) {
        Person person = personRepository.findOne(personId);
        return subscribeRepository.findByPersonAndObjectId(person, itemId);
    }

    @Transactional(readOnly = true)
    public Page<PLMSubscribe> findBySubscribesAssignedTo(Integer personId, Pageable pageable) {
        Person person = personRepository.findOne(personId);
        List<PLMSubscribe> subscribesElements = new ArrayList<>();
        Page<PLMSubscribe> subscribes = subscribeRepository.findByPersonAndSubscribeTrue(person, pageable);
        if (subscribes.getContent().size() > 0) {
            for (PLMSubscribe object : subscribes.getContent()) {
                PLMSubscribe plmSubscribe = new PLMSubscribe();
                if (object.getObjectType().toString().equals("ITEM")) {
                    PLMItem item = itemRepository.findOne(object.getObjectId());
                    if (item != null) {
                        plmSubscribe.setType("ITEM");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(item.getItemName());
                        plmSubscribe.setItem(item);
                    }

                } else if (object.getObjectType().toString().equals("CHANGE")) {
                    PLMECO plmeco = ecoRepository.findOne(object.getObjectId());
                    if (plmeco != null) {
                        plmSubscribe.setType("ECO");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(plmeco.getTitle());
                    }

                } else if (object.getObjectType().toString().equals("PLMWORKFLOWDEFINITION")) {
                    PLMWorkflowDefinition plmWorkflow = workFlowDefinitionRepository.findOne(object.getObjectId());
                    if (plmWorkflow != null) {
                        plmSubscribe.setType("WORKFLOW");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(plmWorkflow.getName());
                    }

                } else if (object.getObjectType().toString().equals("PROJECT")) {
                    PLMProject plmProject = projectRepository.findOne(object.getObjectId());
                    if (plmProject != null) {
                        plmSubscribe.setType("PROJECT");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(plmProject.getName());
                    }

                } else if (object.getObjectType().toString().equals("TEMPLATE")) {
                    ProjectTemplate projectTemplate = projectTemplateRepository.findOne(object.getObjectId());
                    if (projectTemplate != null) {
                        plmSubscribe.setType("TEMPLATE");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(projectTemplate.getName());
                    }

                } else if (object.getObjectType().toString().equals("GLOSSARY")) {
                    PLMGlossary plmGlossary = glossaryRepository.findOne(object.getObjectId());
                    if (plmGlossary != null) {
                        plmSubscribe.setType("GLOSSARY");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(plmGlossary.getName());
                    }

                } else if (object.getObjectType().toString().equals("MANUFACTURER")) {
                    PLMManufacturer plmManufacturer = manufacturerRepository.findOne(object.getObjectId());
                    if (plmManufacturer != null) {
                        plmSubscribe.setType("MANUFACTURER");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(plmManufacturer.getName());
                    }

                } else if (object.getObjectType().toString().equals("MANUFACTURERPART")) {
                    plmSubscribe.setType("MANUFACTURERPART");
                    plmSubscribe.setObjectId(object.getObjectId());
                    PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(object.getObjectId());
                    if (manufacturerPart != null) {
                        plmSubscribe.setType("MANUFACTURERPART");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(manufacturerPartRepository.findOne(object.getObjectId()).getPartName());
                        plmSubscribe.setManufacturer(manufacturerRepository.findOne(manufacturerPart.getManufacturer()));
                    }

                } else if (object.getObjectType().toString().equals("SPECIFICATION")) {
                    RmObject rmObject = rmObjectRepository.findOne(object.getObjectId());
                    if (rmObject != null) {
                        plmSubscribe.setType("SPECIFICATION");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(rmObject.getName());
                    }

                } else if (object.getObjectType().toString().equals("REQUIREMENT")) {
                    RmObject rmObject = rmObjectRepository.findOne(object.getObjectId());
                    if (rmObject != null) {
                        plmSubscribe.setType("REQUIREMENT");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(rmObjectRepository.findOne(object.getObjectId()).getName());
                    }

                }
                if (plmSubscribe.getType() != null) {
                    subscribesElements.add(plmSubscribe);
                }


            }

        }//Converting ObjectList to Pageable
        Page pages = new PageImpl<>(subscribesElements, pageable, subscribesElements.size());
        return pages;
    }

        /*----------- Delete Specification and Section and Requirement */

    public void deleteSpecification(Integer specId) {
        Specification specification = specificationRepository.findOne(specId);
        List<SpecElement> specElements = specElementRepository.findBySpecification(specification.getId());
        if (specElements.size() > 0) {
            throw new CassiniException(messageSource.getMessage("specification_has_requirements", null, "Specification has requirements we cannot delete", LocaleContextHolder.getLocale()));
        } else {
            List<Specification> specification1 = specificationRepository.findByObjectNumberOrderByCreatedDateDesc(specification.getObjectNumber());
            if (specification1.size() > 0) {
                for (Specification spec : specification1) {
                    applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(spec.getId()));
                    specificationRepository.delete(spec.getId());
                }
            }
        }

    }

    public void deleteSpecElement(Integer specId) {
        String deletedSequenceNumber = null;
        Integer deletedElementParentId = null;
        Integer specificationId = null;
        SpecElement specElement = specElementRepository.findOne(specId);
        if (specElement != null) {
            deletedSequenceNumber = specElement.getSeqNumber();
            deletedElementParentId = specElement.getParent();
            Specification specification = specificationRepository.findOne(specElement.getSpecification());
            specificationId = specification.getId();
            if (specElement.getType().toString().equals("SECTION")) {
                SpecSection specSection = specSectionRepository.findOne(specId);
                if (specElement.getParent() != null) {
                    SpecSection specSectionForParentObjectUpdate = specSectionRepository.findOne(specElement.getParent());
                    if (specSectionForParentObjectUpdate != null) {
                        if (!specSectionForParentObjectUpdate.getCanAddRequirement()) {
                            specSectionForParentObjectUpdate.setCanAddRequirement(Boolean.FALSE);
                        } else {
                            specSectionForParentObjectUpdate.setCanAddRequirement(specSectionForParentObjectUpdate.getCanAddRequirement());
                        }
                        if (!specSectionForParentObjectUpdate.getCanAddSection()) {
                            specSectionForParentObjectUpdate.setCanAddSection(Boolean.FALSE);
                        } else {
                            specSectionForParentObjectUpdate.setCanAddSection(specSectionForParentObjectUpdate.getCanAddSection());
                        }
                        specSectionRepository.save(specSectionForParentObjectUpdate);
                    }
                }
                toDeleteSectionOrRequirementRecursion(specElement);
                specElementRepository.delete(specId);
                Person person = sessionWrapper.getSession().getLogin().getPerson();
                String message = person.getFullName() + " " + "deleted " + specSection.getName() + " " + "Specification  under " + specification.getObjectNumber() + "  " + specification.getLifecyclePhase().getPhase() + " Notification";
                String mailSubject = specification.getObjectNumber() + "  " + specification.getLifecyclePhase().getPhase() + " Notification";
                sendSpecSubscribeNotification(specification, message, mailSubject);
            }
            if (specElement.getType().toString().equals("REQUIREMENT")) {
                SpecRequirement specRequirement = specRequirementRepository.findOne(specId);
                PLMSubscribe plmSubscribe = subscribeRepository.findByObjectId(specRequirement.getRequirement().getId());
                if (plmSubscribe != null) {
                    subscribeRepository.delete(plmSubscribe.getId());
                }
                specElementRepository.delete(specId);
                List<Requirement> requirements = requirementRepository.findByObjectNumberOrderByCreatedDateDesc(specRequirement.getRequirement().getObjectNumber());
                if (requirements.size() > 0) {
                    for (Requirement req : requirements) {
                        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(req.getId()));
                        rmObjectRepository.delete(req.getId());
                    }
                }
                /*rmObjectRepository.delete(specRequirement.getRequirement().getId());*/
                Person person = sessionWrapper.getSession().getLogin().getPerson();
                String message = person.getFullName() + " " + "deleted " + specRequirement.getRequirement().getName() + " " + "Specification  under " + specification.getObjectNumber() + "  " + " Notification";
                String mailSubject = specRequirement.getRequirement().getObjectNumber() + "  " + specification.getLifecyclePhase().getPhase() + " Notification";
                sendSpecSubscribeNotification(specification, message, mailSubject);

            }
        }
        //ReSequence Section Or Requirement Once delete any Section Or Requirement Under SubSections
        if (deletedElementParentId != null) {
            reSequenceSectionOrRequirement(deletedSequenceNumber, deletedElementParentId);

        }
        //ReSequence Section Or Requirement Once delete any Parent Level Sections
        if (deletedElementParentId == null) {
            List<SpecSection> specSectionList = specSectionRepository.findBySpecificationAndParentIsNullOrderByCreatedDateAsc(specificationId);
            Integer i = 1;
            for (SpecSection speChild : specSectionList) {
                speChild.setSeqNumber(Integer.toString(i));
                speChild = specSectionRepository.save(speChild);
                recursionToChildrenToParentSequence(speChild);
                i++;
            }

        }

    }

    public void recursionToChildrenToParentSequence(SpecSection specSection) {
        List<SpecElement> children = specElementRepository.findByParentOrderByCreatedDateAsc(specSection.getId());
        for (SpecElement specElement : children) {
            if (specElement.getType().equals(SpecElementType.SECTION)) {
                specElement.setSeqNumber(specSection.getSeqNumber() + "." + (children.indexOf(specElement) + 1));
                specElementRepository.save(specElement);
                recursionToChildrenToParentSequence(specSectionRepository.findOne(specElement.getId()));
            } else if (specElement.getType().equals(SpecElementType.REQUIREMENT)) {
                specElement.setSeqNumber(specSection.getSeqNumber() + "." + (children.indexOf(specElement) + 1));
                specElementRepository.save(specElement);
            }
        }
    }

    public void toDeleteSectionOrRequirementRecursion(SpecElement specElement) {
        List<SpecElement> children = specElementRepository.findByParentOrderByCreatedDateDesc(specElement.getId());
        for (SpecElement secEle : children) {
            if (secEle.getType().equals(SpecElementType.SECTION)) {
                specElementRepository.delete(secEle.getId());

            } else if (secEle.getType().equals(SpecElementType.REQUIREMENT)) {
                SpecRequirement specRequirement = specRequirementRepository.findOne(secEle.getSpecification());
                if (specRequirement != null) {
                    PLMSubscribe plmSubscribe = subscribeRepository.findByObjectId(specRequirement.getRequirement().getId());
                    if (plmSubscribe != null) {
                        subscribeRepository.delete(plmSubscribe.getId());
                    }
                    rmObjectRepository.delete(specRequirement.getRequirement().getId());
                }
            }
            toDeleteSectionOrRequirementRecursion(secEle);
        }
    }

    /* -----------  ReSequence Section Or Requirement Once delete any Section Or Requirement Under subSections ------ */

    public void reSequenceSectionOrRequirement(String deletedSequenceNumber, Integer deletedElementParentId) {
        String actualSeqNumber = deletedSequenceNumber;
        SpecSection parentSection = specSectionRepository.findOne(deletedElementParentId);
        List<SpecElement> children = specElementRepository.findByParentOrderByCreatedDateAsc(deletedElementParentId);
        if (children.size() == 0) {
            parentSection.setCanAddSection(Boolean.TRUE);
            parentSection.setCanAddRequirement(Boolean.TRUE);
            specSectionRepository.save(parentSection);
        }
        for (SpecElement specElement : children) {
            if (specElement.getType().equals(SpecElementType.SECTION)) {
                specElement.setSeqNumber(parentSection.getSeqNumber() + "." + (children.indexOf(specElement) + 1));
                specElementRepository.save(specElement);
                recursionToChildrenToParentSequence(specSectionRepository.findOne(specElement.getId()));
            } else if (specElement.getType().equals(SpecElementType.REQUIREMENT)) {
                specElement.setSeqNumber(parentSection.getSeqNumber() + "." + (children.indexOf(specElement) + 1));
                specElementRepository.save(specElement);
            }
        }

    }





    /*--------- Get Parent Section After Delete SpecElement  ----------*/

    public SpecSection getParentSection(Integer specELementId) {
        SpecSection specSection = specSectionRepository.findOne(specELementId);
        return specSection;
    }

    /*--------------------- Promote and Demote Requirement --------------*/

    public Specification promoteSpecification(Integer specId) {
        Specification specification = specificationRepository.findOne(specId);
        PLMLifeCyclePhase presentStatus = specification.getLifecyclePhase();
        if (specification.getLifecyclePhase().getPhaseType().equals(LifeCyclePhaseType.PRELIMINARY)) {
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.REVIEW, specification.getLifecyclePhase().getLifeCycle());
            specification.setLifecyclePhase(lifeCyclePhase);
        } else if (specification.getLifecyclePhase().getPhaseType().equals(LifeCyclePhaseType.REVIEW)) {
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.RELEASED, specification.getLifecyclePhase().getLifeCycle());
            specification.setLifecyclePhase(lifeCyclePhase);
            specification.setReleasedDate(new Date());
            specification.setReleased(true);
        }
        Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
        specification.setModifiedBy(personId);
        specification.setModifiedDate(new Date());
        specification = specificationRepository.save(specification);
        SpecificationRevisionStatusHistory revisionStatusHistory = new SpecificationRevisionStatusHistory();
        revisionStatusHistory.setSpecification(specification.getId());
        revisionStatusHistory.setFromStatus(presentStatus);
        revisionStatusHistory.setToStatus(specification.getLifecyclePhase());
        revisionStatusHistory.setRevision(specification.getRevision());
        revisionStatusHistory.setTimeStamp(new Date());
        revisionStatusHistory.setUpdatedBy(personId);
        revisionStatusHistory = specificationRevisionStatusHistoryRepository.save(revisionStatusHistory);
        return specification;
    }

    public Specification demoteSpecification(Integer specId) {
        Specification specification = specificationRepository.findOne(specId);
        PLMLifeCyclePhase presentStatus = specification.getLifecyclePhase();
        if (specification.getLifecyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.REVIEW, specification.getLifecyclePhase().getLifeCycle());
            specification.setLifecyclePhase(lifeCyclePhase);
            specification.setReleasedDate(null);
            specification.setReleased(false);

        } else if (specification.getLifecyclePhase().getPhaseType().equals(LifeCyclePhaseType.REVIEW)) {
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.PRELIMINARY, specification.getLifecyclePhase().getLifeCycle());
            specification.setLifecyclePhase(lifeCyclePhase);
        }
        Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
        specification.setModifiedBy(personId);
        specification.setModifiedDate(new Date());
        specification = specificationRepository.save(specification);
        SpecificationRevisionStatusHistory revisionStatusHistory = new SpecificationRevisionStatusHistory();
        revisionStatusHistory.setSpecification(specification.getId());
        revisionStatusHistory.setFromStatus(presentStatus);
        revisionStatusHistory.setToStatus(specification.getLifecyclePhase());
        revisionStatusHistory.setRevision(specification.getRevision());
        revisionStatusHistory.setTimeStamp(new Date());
        revisionStatusHistory.setUpdatedBy(personId);
        revisionStatusHistory = specificationRevisionStatusHistoryRepository.save(revisionStatusHistory);
        return specification;
    }

    public List<SpecificationRevisionHistoryDto> getSpecificationRevisionHistories(Integer specId) {
        List<SpecificationRevisionHistoryDto> revisionHistoryDtos = new ArrayList<>();
        Specification specification = specificationRepository.findOne(specId);
        List<Specification> specElementList = specificationRepository.findByObjectNumberOrderByCreatedDateDesc(specification.getObjectNumber());
        for (Specification specElement : specElementList) {
            SpecificationRevisionHistoryDto revisionHistoryDto = new SpecificationRevisionHistoryDto();
            List<SpecificationRevisionStatusHistory> revisionStatusHistories = specificationRevisionStatusHistoryRepository.findBySpecificationOrderByTimeStampDesc(specElement.getId());
            if (revisionStatusHistories.size() > 0) {
                revisionHistoryDto.setSpecification(specElement);
                revisionHistoryDto.setSpecificationRevisionStatusHistories(revisionStatusHistories);
                revisionHistoryDto.setPerson(personRepository.findOne(specElement.getCreatedBy()));
                revisionHistoryDtos.add(revisionHistoryDto);

            }
        }
        return revisionHistoryDtos;
    }

    @Transactional
    public Specification reviseSpecification(Integer specId) {
        Specification specification = specificationRepository.findOne(specId);
        if (specification != null) {
            return reviseNextRequirement(specification, null);
        } else {
            throw new ItemServiceException(messageSource.getMessage("revise_item_failed_item_not_found", null, Locale.getDefault()));
        }

    }

    @Transactional
    private Specification reviseNextRequirement(Specification specification, String revision) {
        if (revision == null) {
            revision = getNextRevisionSequence(specification, revision);
        }
        if (revision != null) {
            Specification copy = createNextRevisionRequirement(specification, revision);
            specification.setLatest(false);
            specificationRepository.save(specification);

            List<SpecSection> specSectionList = specSectionRepository.findBySpecificationAndParentIsNullOrderByCreatedDateAsc(specification.getId());
            if (specSectionList.size() > 0) {
                for (SpecSection specSection : specSectionList) {
                    SpecSection specSectionNewObject = new SpecSection();
                    specSectionNewObject.setSpecification(copy.getId());
                    specSectionNewObject.setSeqNumber(specSection.getSeqNumber());
                    specSectionNewObject.setType(specSection.getType());
                    specSectionNewObject.setName(specSection.getName());
                    if (!specSection.getCanAddRequirement()) {
                        specSectionNewObject.setCanAddRequirement(Boolean.FALSE);
                    } else {
                        specSectionNewObject.setCanAddRequirement(specSection.getCanAddRequirement());
                    }
                    if (!specSection.getCanAddSection()) {
                        specSectionNewObject.setCanAddSection(Boolean.FALSE);
                    } else {
                        specSectionNewObject.setCanAddSection(specSection.getCanAddSection());
                    }
                    specSectionNewObject.setObjectType(PLMObjectType.SPECSECTION);
                    specSectionNewObject.setDescription(specSection.getDescription());
                    specSectionNewObject = specSectionRepository.save(specSectionNewObject);
                    List<SpecElement> elements = specElementRepository.findByParentOrderByCreatedDateAsc(specSection.getId());
                    if (elements.size() > 0) {
                        vistSpecChildren(elements, specSectionNewObject, copy.getId());
                    }

                }
            }
            //Copy the related
            copyFolderStructure(specification, copy);
            copySpecAttributes(specification, copy);
        /*	copyFiles(requirementOldObject, requirement);
            copyAttributes(requirementOldObject, requirement);*/
            return copy;

        } else {
            throw new ItemServiceException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    private void copyFiles(Requirement oldRequirement, Requirement newRequirement) {
        List<RmObjectFile> rmObjectFiles = rmObjectFileRepository.findByObject(oldRequirement.getId());
        for (RmObjectFile rmObjectFile : rmObjectFiles) {
            RmObjectFile newRmObjectFile = null;
            File file = getFileByRequirement(oldRequirement.getId(), rmObjectFile.getId());
            if (file != null) {
                newRmObjectFile = new RmObjectFile();
                Login login = sessionWrapper.getSession().getLogin();
                newRmObjectFile.setName(rmObjectFile.getName());
                newRmObjectFile.setCreatedBy(login.getPerson().getId());
                newRmObjectFile.setModifiedBy(login.getPerson().getId());
                newRmObjectFile.setObject(newRequirement.getId());
                newRmObjectFile.setVersion(rmObjectFile.getVersion());
                newRmObjectFile.setSize(rmObjectFile.getSize());
                newRmObjectFile.setLatest(rmObjectFile.getLatest());
                newRmObjectFile = rmObjectFileRepository.save(newRmObjectFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + newRequirement.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                String path = dir + File.separator + newRmObjectFile.getId();
                saveFileToDisk(file, path);
            }
        }
    }

    private File getFileByRequirement(Integer reqId, Integer fileId) {
        checkNotNull(reqId);
        checkNotNull(fileId);
        Requirement glossary = requirementRepository.findOne(reqId);
        if (glossary == null) {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
        RmObjectFile glossaryFile = rmObjectFileRepository.findOne(fileId);
        if (glossaryFile == null) {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + reqId + File.separator + fileId;
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
    }

    private void copyAttributes(Requirement oldRequirement, Requirement newRequirement) {
        List<ObjectAttribute> oldRequirementAttributes = objectAttributeRepository.findByObjectId(oldRequirement.getId());
        if (oldRequirementAttributes.size() > 0) {
            for (ObjectAttribute attr : oldRequirementAttributes) {
                ObjectAttribute objectAttribute1 = new ObjectAttribute();
                objectAttribute1.setId(new ObjectAttributeId(newRequirement.getId(), attr.getId().getAttributeDef()));
                objectAttribute1.setStringValue(attr.getStringValue());
                objectAttribute1.setIntegerValue(attr.getIntegerValue());
                objectAttribute1.setBooleanValue(attr.getBooleanValue());
                objectAttribute1.setDateValue(attr.getDateValue());
                objectAttribute1.setDoubleValue(attr.getDoubleValue());
                objectAttribute1.setListValue(attr.getListValue());
                objectAttribute1.setTimeValue(attr.getTimeValue());
                objectAttribute1.setTimestampValue(attr.getTimestampValue());
                objectAttribute1.setImageValue(attr.getImageValue());
                objectAttribute1.setAttachmentValues(attr.getAttachmentValues());
                objectAttribute1.setRefValue(attr.getRefValue());
                objectAttribute1.setCurrencyType(attr.getCurrencyType());
                objectAttribute1.setCurrencyValue(attr.getCurrencyValue());
                objectAttribute1 = objectAttributeRepository.save(objectAttribute1);
            }
        }
    }

    @Transactional
    private void copySpecFiles(Specification oldSpecification, Specification newSpecification) {
        List<RmObjectFile> rmObjectFiles = rmObjectFileRepository.findByObject(oldSpecification.getId());
        for (RmObjectFile rmObjectFile : rmObjectFiles) {
            RmObjectFile newRmObjectFile = null;
            File file = getFileBySpecification(oldSpecification.getId(), rmObjectFile.getId());
            if (file != null) {
                newRmObjectFile = new RmObjectFile();
                Login login = sessionWrapper.getSession().getLogin();
                newRmObjectFile.setName(rmObjectFile.getName());
                newRmObjectFile.setFileNo(rmObjectFile.getFileNo());
                newRmObjectFile.setCreatedBy(login.getPerson().getId());
                newRmObjectFile.setModifiedBy(login.getPerson().getId());
                newRmObjectFile.setObject(newSpecification.getId());
                newRmObjectFile.setVersion(rmObjectFile.getVersion());
                newRmObjectFile.setSize(rmObjectFile.getSize());
                newRmObjectFile.setLatest(rmObjectFile.getLatest());
                newRmObjectFile = rmObjectFileRepository.save(newRmObjectFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + newSpecification.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                String path = dir + File.separator + newRmObjectFile.getId();
                saveFileToDisk(file, path);
            }
        }
    }

    @Transactional
    public void copyFolderStructure(Specification oldSpecification, Specification newSpecification) {
        List<RmObjectFile> rmObjectFiles = rmObjectFileRepository.findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(oldSpecification.getId());
        for (RmObjectFile rmObjectFile : rmObjectFiles) {
            RmObjectFile newRmObjectFile = null;
            File file = getFileBySpecification(oldSpecification.getId(), rmObjectFile.getId());
            if (file != null) {
                newRmObjectFile = new RmObjectFile();
                Login login = sessionWrapper.getSession().getLogin();
                newRmObjectFile.setName(rmObjectFile.getName());
                newRmObjectFile.setFileNo(rmObjectFile.getFileNo());
                newRmObjectFile.setFileType(rmObjectFile.getFileType());
                newRmObjectFile.setCreatedBy(login.getPerson().getId());
                newRmObjectFile.setModifiedBy(login.getPerson().getId());
                newRmObjectFile.setObject(newSpecification.getId());
                newRmObjectFile.setVersion(rmObjectFile.getVersion());
                newRmObjectFile.setSize(rmObjectFile.getSize());
                newRmObjectFile.setLatest(rmObjectFile.getLatest());
                newRmObjectFile = rmObjectFileRepository.save(newRmObjectFile);
                if (newRmObjectFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getParentFileSystemPath(newSpecification.getId(), newRmObjectFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getReplaceFileSystemPath(newSpecification.getId(), newRmObjectFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = dir + File.separator + newRmObjectFile.getId();
                    saveFileToDisk(file, dir);
                    saveOldVersionItemFiles(oldSpecification, newSpecification, rmObjectFile);
                }
            }
            saveItemFileChildren(oldSpecification, newSpecification, rmObjectFile, newRmObjectFile);
        }
    }

    @Transactional
    private void saveItemFileChildren(Specification oldItem, Specification newItem, RmObjectFile rmObjectFile, RmObjectFile plmRmObjectFile) {
        List<RmObjectFile> childrenFiles = rmObjectFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(rmObjectFile.getId());
        for (RmObjectFile childrenFile : childrenFiles) {
            RmObjectFile newObjectFile = null;
            File file = getFileBySpecification(oldItem.getId(), childrenFile.getId());
            if (file != null) {
                newObjectFile = new RmObjectFile();
                Login login = sessionWrapper.getSession().getLogin();
                newObjectFile.setName(childrenFile.getName());
                newObjectFile.setFileNo(childrenFile.getFileNo());
                newObjectFile.setFileType(childrenFile.getFileType());
                newObjectFile.setCreatedBy(login.getPerson().getId());
                newObjectFile.setModifiedBy(login.getPerson().getId());
                newObjectFile.setObject(newItem.getId());
                newObjectFile.setVersion(childrenFile.getVersion());
                newObjectFile.setSize(childrenFile.getSize());
                newObjectFile.setLatest(childrenFile.getLatest());
                newObjectFile.setParentFile(plmRmObjectFile.getId());
                newObjectFile = rmObjectFileRepository.save(newObjectFile);
                if (newObjectFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getParentFileSystemPath(newItem.getId(), newObjectFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getReplaceFileSystemPath(newItem.getId(), newObjectFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = dir + File.separator + newObjectFile.getId();
                    saveFileToDisk(file, dir);
                    saveChildrenOldVersionItemFiles(oldItem, newItem, childrenFile, plmRmObjectFile);
                }
            }
            saveItemFileChildren(oldItem, newItem, childrenFile, newObjectFile);
        }

    }

    private void saveChildrenOldVersionItemFiles(Specification oldItem, Specification newItem, RmObjectFile itemFile, RmObjectFile plmItemFile) {
        List<RmObjectFile> oldVersionFiles = rmObjectFileRepository.findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(oldItem.getId(), itemFile.getFileNo());
        for (RmObjectFile oldVersionFile : oldVersionFiles) {
            RmObjectFile newItemFile = null;
            File file = getFileBySpecification(oldItem.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new RmObjectFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setObject(newItem.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = rmObjectFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(newItem.getId(), newItemFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newItemFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }

    private void saveOldVersionItemFiles(Specification oldSpecification, Specification newSpecification, RmObjectFile rmObjectFile) {
        List<RmObjectFile> oldVersionFiles = rmObjectFileRepository.findByObjectAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(oldSpecification.getId(), rmObjectFile.getFileNo());
        for (RmObjectFile oldVersionFile : oldVersionFiles) {
            RmObjectFile newRmObjectFile = null;
            File file = getFileBySpecification(oldSpecification.getId(), oldVersionFile.getId());
            if (file != null) {
                newRmObjectFile = new RmObjectFile();
                Login login = sessionWrapper.getSession().getLogin();
                newRmObjectFile.setName(oldVersionFile.getName());
                newRmObjectFile.setFileNo(oldVersionFile.getFileNo());
                newRmObjectFile.setFileType(oldVersionFile.getFileType());
                newRmObjectFile.setCreatedBy(login.getPerson().getId());
                newRmObjectFile.setModifiedBy(login.getPerson().getId());
                newRmObjectFile.setObject(newSpecification.getId());
                newRmObjectFile.setVersion(oldVersionFile.getVersion());
                newRmObjectFile.setSize(oldVersionFile.getSize());
                newRmObjectFile.setLatest(oldVersionFile.getLatest());
                newRmObjectFile = rmObjectFileRepository.save(newRmObjectFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(newSpecification.getId(), newRmObjectFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newRmObjectFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }

    private File getFileBySpecification(Integer specId, Integer fileId) {
        checkNotNull(specId);
        checkNotNull(fileId);
        Specification specification = specificationRepository.findOne(specId);
        if (specification == null) {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
        RmObjectFile glossaryFile = rmObjectFileRepository.findOne(fileId);
        if (glossaryFile == null) {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(specId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
    }

    @Transactional
    private void copySpecAttributes(Specification oldSpecification, Specification newSpecification) {
        List<ObjectAttribute> oldRequirementAttributes = objectAttributeRepository.findByObjectId(oldSpecification.getId());
        if (oldRequirementAttributes.size() > 0) {
            for (ObjectAttribute objectAttribute : oldRequirementAttributes) {
                ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(objectAttribute.getId().getAttributeDef());
                if (objectTypeAttribute != null && objectTypeAttribute.getObjectType().equals(ObjectType.valueOf("SPECIFICATIONTYPE"))) {
                    RmObjectAttribute rmObjectAttribute1 = (RmObjectAttribute) Utils.cloneObject(objectAttribute, RmObjectAttribute.class);
                    rmObjectAttribute1.setId(new ObjectAttributeId(newSpecification.getId(), objectAttribute.getId().getAttributeDef()));
                    if (rmObjectAttribute1.getAttachmentValues().length > 0) {
                        List<AttributeAttachment> attachments = attributeAttachmentService.getMultipleAttributeAttachments(Arrays.asList(rmObjectAttribute1.getAttachmentValues()));
                        List<Integer> integers = new ArrayList<>();
                        for (AttributeAttachment attachment : attachments) {
                            AttributeAttachment attachment1 = new AttributeAttachment();
                            attachment1.setObjectId(newSpecification.getId());
                            attachment1.setAttributeDef(attachment.getAttributeDef());
                            attachment1.setExtension(attachment.getExtension());
                            attachment1.setAddedBy(attachment.getAddedBy());
                            attachment1.setObjectType(attachment.getObjectType());
                            attachment1.setAddedOn(new Date());
                            attachment1.setName(attachment.getName());
                            attachment1.setSize(attachment.getSize());
                            attachment1 = attributeAttachmentRepository.save(attachment1);
                            integers.add(attachment1.getId());
                        }
                        rmObjectAttribute1.setAttachmentValues(integers.stream().filter(Objects::nonNull).toArray(Integer[]::new));
                    }
                    rmObjectAttributeRepository.save(rmObjectAttribute1);

                } else {
                    ObjectAttribute objectAttribute1 = (ObjectAttribute) Utils.cloneObject(objectAttribute, ObjectAttribute.class);
                    objectAttribute1.setId(new ObjectAttributeId(newSpecification.getId(), objectAttribute.getId().getAttributeDef()));
                    if (objectAttribute1.getAttachmentValues().length > 0) {
                        List<AttributeAttachment> attachments = attributeAttachmentService.getMultipleAttributeAttachments(Arrays.asList(objectAttribute1.getAttachmentValues()));
                        List<Integer> integers = new ArrayList<>();
                        for (AttributeAttachment attachment : attachments) {
                            AttributeAttachment attachment1 = new AttributeAttachment();
                            attachment1.setObjectId(newSpecification.getId());
                            attachment1.setAttributeDef(attachment.getAttributeDef());
                            attachment1.setExtension(attachment.getExtension());
                            attachment1.setAddedBy(attachment.getAddedBy());
                            attachment1.setObjectType(attachment.getObjectType());
                            attachment1.setAddedOn(new Date());
                            attachment1.setName(attachment.getName());
                            attachment1.setSize(attachment.getSize());
                            attachment1 = attributeAttachmentRepository.save(attachment1);
                            integers.add(attachment1.getId());
                        }
                        objectAttribute1.setAttachmentValues(integers.stream().filter(Objects::nonNull).toArray(Integer[]::new));
                    }
                    objectAttribute1 = objectAttributeRepository.save(objectAttribute1);
                }
            }
        }

    }

    private String getNextRevisionSequence(Specification specification, String revision) {
        String nextRev = null;
        String lastRev = specification.getRevision();
        Lov lov = lovRepository.findByName("Default Revision Sequence");
        String[] values = lov.getValues();
        int lastIndex = -1;
        for (int i = 0; i < values.length; i++) {
            String rev = values[i];
            if (rev.equalsIgnoreCase(lastRev)) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1 && lastIndex < values.length) {
            nextRev = values[lastIndex + 1];
        }
        return nextRev;
    }


    private Specification createNextRevisionRequirement(Specification specification, String nextRev) {
        Specification copy = new Specification();
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.PRELIMINARY, specification.getLifecyclePhase().getLifeCycle());
        copy.setName(specification.getName());
        copy.setDescription(specification.getDescription());
        copy.setRevision(nextRev);
        copy.setLifecyclePhase(lifeCyclePhase);
        copy.setObjectType(PLMObjectType.SPECIFICATION);
        copy.setType(specification.getType());
        copy.setLatest(true);
        copy.setReleased(false);
        copy.setObjectNumber(specification.getObjectNumber());
        copy = specificationRepository.save(copy);
        specification.setLatest(false);
        specification = specificationRepository.save(specification);
        Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
        SpecificationRevisionStatusHistory revisionStatusHistory = new SpecificationRevisionStatusHistory();
        revisionStatusHistory.setSpecification(copy.getId());
        revisionStatusHistory.setFromStatus(copy.getLifecyclePhase());
        revisionStatusHistory.setToStatus(copy.getLifecyclePhase());
        revisionStatusHistory.setRevision(copy.getRevision());
        revisionStatusHistory.setTimeStamp(new Date());
        revisionStatusHistory.setUpdatedBy(personId);
        revisionStatusHistory = specificationRevisionStatusHistoryRepository.save(revisionStatusHistory);
        SpecificationRevisionHistory specificationRevisionHistory = new SpecificationRevisionHistory();
        specificationRevisionHistory.setSpecification(specification.getId());
        specificationRevisionHistory.setFromRevision(specification.getRevision());
        specificationRevisionHistory.setToRevision(copy.getRevision());
        specificationRevisionHistory.setTimeStamp(new Date());
        specificationRevisionHistory.setUpdatedBy(personId);
        specificationRevisionHistory = specificationRevisionHistoryRepository.save(specificationRevisionHistory);
        return copy;
    }


    @Transactional(readOnly = true)
    public Page<Specification> specificationFreeTextSearch(Pageable pageable, SpecificationBuilderCriteria criteria) {
        Predicate predicate = specificationDeliverableBuilder.build(criteria, QSpecification.specification);
        return specificationRepository.findAll(predicate, pageable);

    }


    @Transactional(readOnly = true)
    public SpecRequirement acceptFinalData(Integer reqId) {
        Requirement requirement = requirementRepository.findOne(reqId);
        SpecRequirement specRequirement = specRequirementRepository.findByRequirement(requirement);
        RequirementEdit finalAcceptEdit = requirementEditRepository.findByRequirementAndStatus(specRequirement.getRequirement().getId(), RequirementEditStatus.FINAL);
        if (finalAcceptEdit != null) {
            specRequirement.getRequirement().setFinalAcceptEdit(true);
        }
        return specRequirement;
    }

    private void vistSpecChildren(List<SpecElement> elements, SpecSection specSection, Integer id) {
        for (SpecElement specElement : elements) {
            if (specElement.getType().equals(SpecElementType.SECTION)) {
                SpecSection specSection1 = new SpecSection();
                SpecSection specSection2 = specSectionRepository.findOne(specElement.getId());
                if (!specSection2.getCanAddRequirement()) {
                    specSection1.setCanAddRequirement(Boolean.FALSE);
                } else {
                    specSection1.setCanAddRequirement(specSection2.getCanAddRequirement());
                }
                if (!specSection2.getCanAddSection()) {
                    specSection1.setCanAddSection(Boolean.FALSE);
                } else {
                    specSection1.setCanAddSection(specSection2.getCanAddSection());
                }
                specSection1.setSeqNumber(specElement.getSeqNumber());
                specSection1.setType(specElement.getType());
                specSection1.setSpecification(id);
                specSection1.setName(specSection2.getName());
                specSection1.setObjectType(PLMObjectType.SPECSECTION);
                specSection1.setParent(specSection.getId());
                specSection1.setDescription(specSection2.getDescription());
                SpecSection specSection3 = specSectionRepository.save(specSection1);
                List<SpecElement> specSections = specElementRepository.findByParentOrderByCreatedDateAsc(specElement.getId());
                if (specSections.size() > 0) {
                    vistSpecChildren(specSections, specSection3, id);
                }

            } else if (specElement.getType().equals(SpecElementType.REQUIREMENT)) {
                SpecRequirement specRequirement = new SpecRequirement();
                SpecRequirement specRequirement1 = specRequirementRepository.findOne(specElement.getId());
                Requirement requirement = (Requirement) Utils.cloneObject(specRequirement1.getRequirement(), Requirement.class);
                requirement.setLatest(false);
                requirement = requirementRepository.save(requirement);
                Requirement requirement1 = new Requirement();
                requirement1.setSpecification(id);
                requirement1.setVersion(requirement.getVersion());
                requirement1.setLatest(true);
                requirement1.setName(requirement.getName());
                requirement1.setDescription(requirement.getDescription());
                requirement1.setObjectNumber(requirement.getObjectNumber());
                requirement1.setType(requirement.getType());
                requirement1.setAssignedTo(requirement.getAssignedTo());
                requirement1.setStatus(requirement.getStatus());
                requirement1.setObjectType(PLMObjectType.REQUIREMENT);
                requirement1.setPlannedFinishDate(requirement.getPlannedFinishDate());
                requirement1.setActualFinishDate(requirement.getActualFinishDate());
                requirement1 = requirementRepository.save(requirement1);
                List<RequirementEdit> requirementEdits = requirementEditRepository.findByRequirementOrderByEditedDateAsc(requirement.getId());
                RequirementEdit requirementEdit1 = new RequirementEdit();
                for (RequirementEdit requirementEdit : requirementEdits) {
                    requirementEdit1.setEditedName(requirementEdit.getEditedName());
                    requirementEdit1.setEditedDescription(requirementEdit.getEditedDescription());
                    requirementEdit1.setEditNotes(requirementEdit.getEditNotes());
                    requirementEdit1.setRequirement(requirement1.getId());
                    requirementEdit1.setVersion(requirementEdit.getVersion());
                    requirementEdit1.setAcceptedDate(requirementEdit.getAcceptedDate());
                    requirementEdit1.setEditedDate(requirementEdit.getEditedDate());
                    requirementEdit1.setFinalDate(requirementEdit.getFinalDate());
                    requirementEdit1.setPerson(requirementEdit.getPerson());
                    requirementEdit1.setLatest(requirementEdit.getLatest());
                    requirementEdit1 = requirementEditRepository.save(requirementEdit1);
                }
                specRequirement.setSpecification(id);
                specRequirement.setSeqNumber(specElement.getSeqNumber());
                specRequirement.setType(specElement.getType());
                specRequirement.setRequirement(requirement1);
                specRequirement.setObjectType(PLMObjectType.SPECREQUIREMENT);
                specRequirement.setParent(specSection.getId());
                specRequirement = specRequirementRepository.save(specRequirement);

            }
        }

    }

    public SpecRequirement updateSpecRequirement(SpecRequirement specRequirement, Integer parent) {
        checkNotNull(specRequirement);
        if ((specRequirement.getParent() != null)) {
            SpecSection specSection1 = specSectionRepository.findOne(specRequirement.getParent());
            List<SpecSection> children = specSectionRepository.findByParentOrderByCreatedDateDesc(specRequirement.getParent());
            List<SpecRequirement> reqChildren = specRequirementRepository.findByParentOrderByCreatedDateDesc(specRequirement.getParent());
            if (children.size() > 0) {
                throw new CassiniException(messageSource.getMessage("target_sections_has_subsections", null, "Target section has subSections", LocaleContextHolder.getLocale()));
            }
            String seqNumber;
            if (children.size() == 0 && reqChildren.size() == 0) {
                seqNumber = specSection1.getSeqNumber() + ".1";
            } else {
                seqNumber = specSection1.getSeqNumber() + "." + ((children.size() + reqChildren.size()) + 1);
            }
            specRequirement.setSeqNumber(seqNumber);
            specRequirement = specRequirementRepository.save(specRequirement);

        }
        if (parent != null) {
            SpecSection draggedSpecSection = specSectionRepository.findOne(parent);
            List<SpecSection> children = specSectionRepository.findByParentOrderByCreatedDateAsc(parent);
            List<SpecRequirement> reqChildren = specRequirementRepository.getRequirementsByParent(parent);
            Integer childrenCount = children.size() + reqChildren.size();
            Integer count = children.size() + reqChildren.size();
            for (SpecSection speChild : children) {
                speChild.setSeqNumber(draggedSpecSection.getSeqNumber() + "." + ((childrenCount - count) + 1));
                speChild = specSectionRepository.save(speChild);
                count--;
            }
            for (SpecRequirement reqChild : reqChildren) {
                reqChild.setSeqNumber(draggedSpecSection.getSeqNumber() + "." + ((childrenCount - count) + 1));
                reqChild = specRequirementRepository.save(reqChild);
                count--;
            }

        }
        return specRequirement;
    }

    public SpecRequirement reOrderSpecRequirement(SpecRequirement specRequirement, Integer targetId, Integer parent) {
        checkNotNull(specRequirement);
        if (parent != null) {
            SpecRequirement specReq = specRequirementRepository.findOne(targetId);
            String targetSeqNumber = specReq.getSeqNumber();
            String actualSeqNumber = specRequirement.getSeqNumber();
            SpecSection draggedSpecSection = specSectionRepository.findOne(parent);
            List<SpecSection> children = specSectionRepository.findByParentOrderByCreatedDateAsc(parent);
            List<SpecRequirement> reqChildren = specRequirementRepository.getRequirementsByParent(parent);
            Integer childrenCount = children.size() + reqChildren.size();
            Integer count = children.size() + reqChildren.size();
            for (SpecSection speChild : children) {
                speChild.setSeqNumber(draggedSpecSection.getSeqNumber() + "." + ((childrenCount - count) + 1));
                speChild = specSectionRepository.save(speChild);
                count--;
            }
            for (SpecRequirement reqChild : reqChildren) {
                reqChild.setSeqNumber(draggedSpecSection.getSeqNumber() + "." + ((childrenCount - count) + 1));
                if (reqChild.getSeqNumber() == targetSeqNumber) {
                    reqChild.setSeqNumber(targetSeqNumber);
                    reqChild = specRequirementRepository.save(reqChild);
                }
                count--;
            }

        }
        specRequirement = specRequirementRepository.save(specRequirement);
        return specRequirement;
    }

    public SpecSection updateSpecSection(SpecSection specSection) {
        SpecSection section = specSectionRepository.findOne(specSection.getId());
        section.setName(specSection.getName());
        section.setDescription(specSection.getDescription());
        return specSectionRepository.save(section);
    }

    public List<Person> createSpecPersons(Integer specId, List<Person> persons) {
        String names = null;
        for (Person person : persons) {
            SpecPermission specPermission = new SpecPermission();
            specPermission.setSpecification(specId);
            specPermission.setSpecUser(person.getId());
            if (names == null) {
                names = person.getFullName();
            } else {
                names = names + " , " + person.getFullName();
            }
            specPermission = specPermissionRepository.save(specPermission);
        }
        return persons;
    }

    @Transactional(readOnly = true)
    public List<SpecPermission> getAllSpecPersons(Integer specId) {
        List<SpecPermission> specPermissions = specPermissionRepository.findBySpecification(specId);
        return specPermissions;
    }

    @Transactional(readOnly = true)
    public List<Login> getAllPersons(Integer specId) {
        List<Login> specPersons = new ArrayList<>();
        List<SpecPermission> specPermissions = specPermissionRepository.findBySpecification(specId);
        List<Login> logins = loginRepository.findByExternalFalse();
        if (logins.size() != 0) {
            for (Login login : logins) {
                Boolean exist = false;
                for (SpecPermission specPermission : specPermissions) {
                    if (login.getPerson().getId().equals(specPermission.getSpecUser())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    specPersons.add(login);
                }
            }
        } else {
            specPersons.addAll(logins);
        }
        return specPersons;
    }

    public SpecPermission createSpecPermission(SpecPermission specPermission) {
        SpecPermission specPermission1 = specPermissionRepository.save(specPermission);
        Specification specification = specificationRepository.findOne(specPermission.getSpecification());
        return specPermission1;
    }

    public void deleteSpecPerson(Integer id) {
        SpecPermission specPermission = specPermissionRepository.findOne(id);
        Person person = personRepository.findOne(specPermission.getSpecUser());
        Specification specification = specificationRepository.findOne(specPermission.getSpecification());
        specPermissionRepository.delete(id);
    }

    public Specification importSpecificationFromExcel(Integer specId, MultipartHttpServletRequest request) {
        Specification specification = specificationRepository.findOne(specId);
        List<SpecElement> elements = specElementRepository.findBySpecificationAndParentIsNullOrderByCreatedDateAsc(specId);
        if (elements.size() > 0) {
            throw new CassiniException(messageSource.getMessage("cannot_importInto_specification", null, "Cannot import into a specification that has content in it.", LocaleContextHolder.getLocale()));
        }
        Map<String, MultipartFile> fileMap = request.getFileMap();
        for (MultipartFile file : fileMap.values()) {
            try {
                InputStream is = file.getInputStream();
                TableData tableData = excelService.readData(is);
                List<RowData> rows = tableData.getRows();
                Map<String, SpecElement> mapRows = new HashMap<>();
                for (RowData row : rows) {
                    String seq = row.get("Sequence");
                    String type = row.get("Type");
                    String name = row.get("Name");
                    String desc = row.get("Description");
                    if (seq != null && type != null && name != null && desc != null) {
                        seq = seq.trim();
                        if (type.equalsIgnoreCase("section")) {
                            SpecSection section = new SpecSection();
                            section.setSpecification(specId);
                            section.setName(name);
                            section.setDescription(desc);
                            if (seq.endsWith(".0")) {
                                seq = seq.substring(0, seq.lastIndexOf('.')).trim();
                            }
                            section.setSeqNumber(seq);
                            if (seq.indexOf('.') != -1) {
                                String parentSeq = seq.substring(0, seq.lastIndexOf('.')).trim();
                                SpecElement parentElem = (SpecSection) mapRows.get(parentSeq);
                                if (parentElem != null) {
                                    section.setParent(parentElem.getId());
                                }
                            }
                            section = specSectionRepository.save(section);
                            mapRows.put(seq, section);
                        } else {
                            String parentSeq = seq.substring(0, seq.lastIndexOf('.')).trim();
                            SpecElement parentElem = (SpecSection) mapRows.get(parentSeq);
                            if (parentElem != null) {
                                Requirement requirement = new Requirement();
                                RequirementType requirementType = requirementTypeRepository.findByName("Requirement");
                                AutoNumber autoNumber = requirementType.getNumberSource();
                                requirement.setType(requirementType);
                                requirement.setObjectNumber(autoNumberService.getNextNumber(autoNumber.getId()));
                                requirement.setSpecification(specId);
                                requirement.setName(name);
                                requirement.setDescription(desc);
                                requirement = requirementRepository.save(requirement);
                                SpecRequirement reqElem = new SpecRequirement();
                                reqElem.setSpecification(specId);
                                reqElem.setSeqNumber(seq);
                                reqElem.setRequirement(requirement);
                                reqElem.setParent(parentElem.getId());
                                reqElem = specRequirementRepository.save(reqElem);
                                mapRows.put(seq, reqElem);
                            }
                        }
                    }
                }

            } catch (IOException e) {
                throw new CassiniException(messageSource.getMessage("error_reading_uploaded_file" + e.getMessage(), null, "Error reading uploaded file. REASON: " + e.getMessage(), LocaleContextHolder.getLocale()));
            }
            break;
        }
        return specification;
    }

    public void exportSpecificationToExcel(Integer specId, HttpServletResponse response) {
        Specification specification = specificationRepository.findOne(specId);
        TableData tableData = new TableData();
        ColumnData columnData = new ColumnData();
        tableData.setColumns(columnData);
        columnData.setNames(Arrays.asList("Sequence", "Type", "Name", "Description"));
        List<RowData> rows = tableData.getRows();
        List<SpecElement> elements = specElementRepository.findBySpecificationAndParentIsNullOrderByCreatedDateAsc(specId);
        for (SpecElement element : elements) {
            collectSpecChildren(element, columnData, rows);
        }
        String fileName = specification.getName() + ".xlsx";
        response.setContentType(getMimeType(fileName));
        if (fileName.contains(" ")) {
            response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        } else {
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        }
        try {
            OutputStream out = response.getOutputStream();
            excelService.writeData(tableData, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collectSpecChildren(SpecElement specElement, ColumnData columnData, List<RowData> rows) {
        /*RowData row = new RowData();
        rows.add(row);
        row.getValues().add(specElement.getSeqNumber());
        SpecElementType type = specElement.getType();
        row.getValues().add(type == SpecElementType.SECTION ? "Section" : "Requirement");
        if (type == SpecElementType.SECTION) {
            SpecSection section = (SpecSection) specElement;
            row.getValues().add(section.getName());
            row.getValues().add(section.getDescription());
            row.getValues().add("-");
            List<SpecElement> children = specElementRepository.findByParentOrderByCreatedDateAsc(specElement.getId());
            for (SpecElement child : children) {
                collectSpecChildren(child, columnData, rows);
            }
        } else {
            SpecRequirement specRequirement = (SpecRequirement) specElement;
            Requirement requirement = specRequirement.getRequirement();
            row.getValues().add(requirement.getName());
            row.getValues().add(requirement.getDescription());
            row.getValues().add("-");
        }*/
    }

    private String getMimeType(String fileName) {
        try {
            if (mimeTypes == null) {
                mimeTypes = new MimetypesFileTypeMap(reportsResource.getInputStream());
            }
            return mimeTypes.getContentType(fileName);
        } catch (IOException e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Requirement> getRequirementVersionsBySpecification(Integer specId) {
        List<Requirement> requirementVersions = requirementRepository.getVersionsBySpecification(specId);
        return requirementVersions;
    }


    @Transactional(readOnly = true)
    public List<Requirement> getRequirementCurrentAndPreviousVersions(String objectNumber, Integer specId) {
        List<Requirement> requirementVersions = requirementRepository.findBySpecificationAndObjectNumberOrderByCreatedDateAsc(specId, objectNumber);
        List<Requirement> requirements = new ArrayList<>();
        if (requirementVersions.size() > 0) {
            if (requirementVersions.size() >= 2) {
                Requirement reqPreviousVersion = requirementVersions.get(requirementVersions.size() - 2);
                for (Requirement reqElement : requirementVersions) {
                    if (reqElement.getLatest() == true) {
                        requirements.add(reqElement);
                    }
                }
                requirements.add(reqPreviousVersion);
            } else {
                throw new CassiniException(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED,
                        messageSource.getMessage("there_is_no_previous_versions", null, "There is no previous versions", LocaleContextHolder.getLocale()));
            }
        }
        return requirements;
    }


    @Transactional(readOnly = true)
    public List<List<ObjectAttribute>> getVersionAttributes(List<Integer> reqIds) {
        List<List<ObjectAttribute>> objectAttributes1 = new ArrayList<>();
        for (Integer reqId : reqIds) {
            List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectId(reqId);
            for (ObjectAttribute objElement : objectAttributes) {
                ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(objElement.getId().getAttributeDef());
                objElement.setObjectTypeAttribute(objectTypeAttribute);
            }
            Collections.sort(objectAttributes, (ObjectAttribute a1, ObjectAttribute a2) -> a1.getObjectTypeAttribute().getName().compareTo((a2.getObjectTypeAttribute().getName())));
            objectAttributes1.add(objectAttributes);
        }
        return objectAttributes1;
    }


    public RmObjectFile createSpecFolder(Integer specId, RmObjectFile rmObjectFile) {
        rmObjectFile.setId(null);
        String folderNumber = null;
        Specification specification = null;
        Requirement requirement = null;
        RmObject rmObject = rmObjectRepository.findOne(specId);
        RmObjectFile existFolderName = null;
        if (rmObjectFile.getParentFile() != null) {
            existFolderName = rmObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(rmObjectFile.getName(), rmObjectFile.getParentFile(), specId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(rmObjectFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", rmObjectFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = rmObjectFileRepository.findByNameEqualsIgnoreCaseAndObjectAndLatestTrue(rmObjectFile.getName(), specId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", rmObjectFile.getName());
                throw new CassiniException(result);
            }
        }
        if (rmObject != null && rmObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.SPECIFICATION.toString()))) {
            specification = specificationRepository.findOne(specId);
        } else {
            requirement = requirementRepository.findOne(specId);
        }
        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        rmObjectFile.setObject(specId);
        rmObjectFile.setFileNo(folderNumber);
        rmObjectFile.setFileType("FOLDER");
        rmObjectFile = rmObjectFileRepository.save(rmObjectFile);
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(specId, rmObjectFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return rmObjectFile;
    }

    public List<RmObjectFile> uploadSpecFolderFiles(Integer specId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<RmObjectFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Specification specification = null;
        Requirement requirement = null;
        RmObject rmObject = rmObjectRepository.findOne(specId);
        if (rmObject != null && rmObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.SPECIFICATION.toString()))) {
            specification = specificationRepository.findOne(specId);
        } else {
            requirement = requirementRepository.findOne(specId);
        }
        Login login = sessionWrapper.getSession().getLogin();
        String fileNames = null;
        RmObjectFile rmObjectFile = null;
        String fNames = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                rmObjectFile = rmObjectFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                if (rmObjectFile != null) {
                    comments = commentRepository.findAllByObjectId(rmObjectFile.getId());
                }
                Integer version = 1;
                String autoNumber1 = null;
                if (rmObjectFile != null) {
                    rmObjectFile.setLatest(false);
                    Integer oldVersion = rmObjectFile.getVersion();
                    version = oldVersion + 1;
                    autoNumber1 = rmObjectFile.getFileNo();
                    rmObjectFileRepository.save(rmObjectFile);
                }
                if (rmObjectFile == null) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                }
                rmObjectFile = new RmObjectFile();
                rmObjectFile.setName(name);
                rmObjectFile.setFileNo(autoNumber1);
                rmObjectFile.setParentFile(fileId);
                /*itemFile.setReplaceFileName(name);*/
                rmObjectFile.setCreatedBy(login.getPerson().getId());
                rmObjectFile.setModifiedBy(login.getPerson().getId());
                rmObjectFile.setObject(specId);
                rmObjectFile.setVersion(version);
                rmObjectFile.setSize(file.getSize());
                rmObjectFile.setFileType("FILE");
                rmObjectFile = rmObjectFileRepository.save(rmObjectFile);
                if (fileNames == null) {
                    fNames = rmObjectFile.getName();
                    fileNames = rmObjectFile.getName() + " - Version : " + rmObjectFile.getVersion();
                } else {
                    fNames = fNames + " , " + rmObjectFile.getName();
                    fileNames = fileNames + " , " + rmObjectFile.getName() + " - Version : " + rmObjectFile.getVersion();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(specId, fileId);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                String path = dir + File.separator + rmObjectFile.getId();
                fileSystemService.saveDocumentToDisk(file, path);
                /*Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
                if (map != null) {
                    rmObjectFile.setUrn(map.get("urn"));
                    rmObjectFile.setThumbnail(map.get("thumbnail"));
                    rmObjectFile = rmObjectFileRepository.save(rmObjectFile);
                }*/
                uploaded.add(rmObjectFile);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    @Transactional
    public PLMFile moveSpecFileToFolder(Integer id, RmObjectFile rmObjectFile) throws Exception {
        RmObjectFile file = rmObjectFileRepository.findOne(rmObjectFile.getId());
        RmObjectFile existFile = (RmObjectFile) Utils.cloneObject(file, RmObjectFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getObject(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getObject() + File.separator + existFile.getId();
        }
        if (rmObjectFile.getParentFile() != null) {
            RmObjectFile existItemFile = rmObjectFileRepository.findByParentFileAndNameAndLatestTrue(rmObjectFile.getParentFile(), rmObjectFile.getName());
            RmObjectFile folder = rmObjectFileRepository.findOne(rmObjectFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                rmObjectFile = rmObjectFileRepository.save(rmObjectFile);
            }
        } else {
            RmObjectFile existItemFile = rmObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(rmObjectFile.getObject(), rmObjectFile.getName());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                rmObjectFile = rmObjectFileRepository.save(rmObjectFile);
            }
        }
        if (rmObjectFile != null) {
            String dir = "";
            if (rmObjectFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(rmObjectFile.getObject(), rmObjectFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + rmObjectFile.getObject() + File.separator + rmObjectFile.getId();
            }
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            File e = new File(oldFileDir);
            System.gc();
            Thread.sleep(1000L);
            FileUtils.deleteQuietly(e);
            List<RmObjectFile> oldVersionFiles = rmObjectFileRepository.findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getObject(), existFile.getFileNo());
            for (RmObjectFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getObject(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getObject() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(rmObjectFile.getObject(), rmObjectFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(rmObjectFile.getParentFile());
                oldVersionFile = rmObjectFileRepository.save(oldVersionFile);
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.createNewFile();
                }

                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                e = new File(oldFileDir);
                System.gc();
                Thread.sleep(1000L);
                FileUtils.deleteQuietly(e);
            }
        }
        return rmObjectFile;
    }

    @Transactional(readOnly = true)
    public List<RmObjectFile> getSpecFolderChidren(Integer folderId) {
        List<RmObjectFile> rmObjectFiles = rmObjectFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        rmObjectFiles.forEach(rmObjectFile -> {
            rmObjectFile.setParentObject(PLMObjectType.SPECIFICATION);
            if (rmObjectFile.getFileType().equals("FOLDER")) {
                rmObjectFile.setCount(rmObjectFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(rmObjectFile.getId()).size());
            }
        });
        return rmObjectFiles;
    }

    @Transactional
    public void deleteFolder(Integer specId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(specId, folderId);
        List<RmObjectFile> projectFiles = rmObjectFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) projectFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        PLMFile file = fileRepository.findOne(folderId);
        Specification specification = null;
        Requirement requirement = null;
        RmObject rmObject = rmObjectRepository.findOne(specId);
        if (rmObject != null && rmObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.SPECIFICATION.toString()))) {
            specification = specificationRepository.findOne(specId);
        } else {
            requirement = requirementRepository.findOne(specId);
        }
        rmObjectFileRepository.delete(folderId);
    }

    @Transactional(readOnly = true)
    public DetailsCount getSpecDetails(Integer specId) {
        DetailsCount detailsCount = new DetailsCount();
        List<RmObjectFile> files = rmObjectFileRepository.findByObjectAndFileTypeAndLatestTrueOrderByModifiedDateDesc(specId, "FILE");
        List<SpecRequirement> requirements = specRequirementRepository.findBySpecification(specId);
        detailsCount.setFiles(files.size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(specId));
        detailsCount.setRequirements(requirements.size());
        return detailsCount;
    }

    @Transactional(readOnly = true)
    public void generateZipFile(Integer specId, HttpServletResponse response) throws FileNotFoundException, IOException {
        Specification specification = specificationRepository.findOne(specId);
        List<RmObjectFile> rmObjectFiles = rmObjectFileRepository.findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(specId);
        ArrayList<String> fileList = new ArrayList<>();
        rmObjectFiles.forEach(rmObjectFile -> {
            File file = getRmObjectFile(specId, rmObjectFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = specification.getName() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "RM",specId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public RmObjectFile updateFileDescription(Integer id, RmObjectFile rmObjectFile) {
        RmObjectFile objectFile = rmObjectFileRepository.findOne(id);
        if (objectFile != null) {
            objectFile.setDescription(rmObjectFile.getDescription());
            objectFile = fileRepository.save(objectFile);
        }
        return objectFile;
    }

    @Transactional
    public RmObjectFile getLatestUploadedObjectFile(Integer specId, Integer fileId) {
        RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(fileId);
        RmObjectFile objectFile = rmObjectFileRepository.findByObjectAndFileNoAndLatestTrue(rmObjectFile.getObject(), rmObjectFile.getFileNo());
        return objectFile;
    }

    @Transactional
    public List<RmObjectFile> pasteFromClipboard(Integer objectId, Integer fileId, List<PLMFile> files) {
        List<RmObjectFile> fileList = new ArrayList<>();
        for (PLMFile file : files) {
            RmObjectFile objectFile = new RmObjectFile();
            RmObjectFile existFile = null;
            if (fileId != 0) {
                objectFile.setParentFile(fileId);
                existFile = rmObjectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndObjectAndLatestTrue(file.getName(), fileId, objectId);
            } else {
                existFile = rmObjectFileRepository.findByObjectAndNameAndParentFileIsNullAndLatestTrue(objectId, file.getName());
            }
            if (existFile == null) {
                objectFile.setName(file.getName());
                objectFile.setDescription(file.getDescription());
                objectFile.setObject(objectId);
                objectFile.setVersion(1);
                objectFile.setSize(file.getSize());
                objectFile.setLatest(file.getLatest());
                String autoNumber1 = null;
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                }
                objectFile.setFileNo(autoNumber1);
                objectFile.setFileType("FILE");
                objectFile = rmObjectFileRepository.save(objectFile);
                objectFile.setParentObject(PLMObjectType.SPECIFICATION);
                fileList.add(objectFile);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(file);
                String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + objectId;
                File fDirectory = new File(directory);
                if (!fDirectory.exists()) {
                    fDirectory.mkdirs();
                }
                String dir = "";
                if (objectFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(objectId, objectFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + objectId + File.separator + objectFile.getId();
                }
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    try {
                        fDir.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                FileInputStream instream = null;
                FileOutputStream outstream = null;
                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            }
        }
        return fileList;
    }

    public void undoCopiedObjectFiles(Integer objectId, List<RmObjectFile> rmObjectFiles) {
        rmObjectFiles.forEach(rmObjectFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(objectId, rmObjectFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(rmObjectFile.getId(), dir);
            rmObjectFileRepository.delete(rmObjectFile.getId());
        });
    }


    @Transactional
    public PLMWorkflow attachSpecWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        Specification specification = specificationRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (specification != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.SPECIFICATION, specification.getId(), wfDef);
            specification.setWorkflow(workflow.getId());
            specificationRepository.save(specification);
        }
        return workflow;
    }

    @Transactional(readOnly = true)
    public RmObjectType getObjectType(Integer id) {
        return rmObjectTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        SpecificationType specificationType = specificationTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (specificationType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, specificationType.getParentType(), type);
        }
        return workflowDefinitions;
    }


    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        SpecificationType specificationType = specificationTypeRepository.findOne(typeId);
        if (specificationType != null) {
            PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
            if (workflowType != null) {
                List<PLMWorkflowDefinition> workflowDefinition2 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
                if (workflowDefinition2.size() > 0) {
                    workflowDefinition2.forEach(workflowDefinition -> {
                        if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                            definitions.add(workflowDefinition);
                        }
                    });
                }
            }
            if (specificationType.getParentType() != null) {
                getWorkflowsFromHierarchy(definitions, specificationType.getParentType(), type);
            }
        }
    }

    @EventListener(condition = "#event.attachedToType.name() == 'SPECIFICATION'")
    public void specWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        Specification specification = (Specification) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
    }

    @EventListener(condition = "#event.attachedToType.name() == 'SPECIFICATION'")
    public void specWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        Specification specification = (Specification) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();

    }

    @EventListener(condition = "#event.attachedToType.name() == 'SPECIFICATION'")
    public void specWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        Specification specification = (Specification) event.getAttachedToObject();
    }

    @EventListener(condition = "#event.attachedToType.name() == 'SPECIFICATION'")
    public void specWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        Specification specification = (Specification) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
    }

    @EventListener(condition = "#event.attachedToType.name() == 'SPECIFICATION'")
    public void partWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        Specification specification = (Specification) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
    }

    @EventListener(condition = "#event.attachedToType.name() == 'SPECIFICATION'")
    public void partWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        Specification specification = (Specification) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
    }


    @Transactional(readOnly = true)
    public Page<PLMSubscribe> getAllSubscribersByPerson(Integer personId, Pageable pageable) {
        Person person = personRepository.findOne(personId);
        List<PLMSubscribe> subscribesElements = new ArrayList<>();
        Page<PLMSubscribe> subscribes = subscribeRepository.findByPersonAndSubscribeTrue(person, pageable);
        if (subscribes.getContent().size() > 0) {
            for (PLMSubscribe object : subscribes.getContent()) {
                PLMSubscribe plmSubscribe = new PLMSubscribe();
                if (object.getObjectType().toString().equals("ITEM")) {
                    PLMItem item = itemRepository.findOne(object.getObjectId());
                    if (item != null) {
                        PLMItemRevision rev = itemRevisionRepository.findOne(item.getLatestRevision());
                        plmSubscribe.setType("ITEM");
                        plmSubscribe.setObjectId(object.getObjectId());
                        plmSubscribe.setName(item.getItemName());
                        plmSubscribe.setItem(item);
                        plmSubscribe.getItem().setRev(rev);
                    }

                }
                if (plmSubscribe.getType() != null) {
                    subscribesElements.add(plmSubscribe);
                }


            }

        }//Converting ObjectList to Pageable
        Page pages = new PageImpl<>(subscribesElements, pageable, subscribesElements.size());
        return pages;
    }


}