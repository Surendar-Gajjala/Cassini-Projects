package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.activitystream.ActivityStreamObjectRepository;
import com.cassinisys.platform.repo.col.AttachmentRepository;
import com.cassinisys.platform.repo.col.AttributeAttachmentRepository;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.common.TagRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import com.cassinisys.platform.service.core.*;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.platform.util.converter.ImportConverter;
import com.cassinisys.plm.event.ItemEvents;
import com.cassinisys.plm.event.PushNotificationEvents;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.dto.*;
import com.cassinisys.plm.model.mfr.ManufacturerPartStatus;
import com.cassinisys.plm.model.mfr.PLMItemManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.pdm.*;
import com.cassinisys.plm.model.pgc.PGCItemSpecification;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.AlternatePartDto;
import com.cassinisys.plm.model.plm.dto.SubstitutePartDto;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.pqm.PQMPRProblemItem;
import com.cassinisys.plm.model.pqm.PQMProblemReport;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.model.rm.RecentlyVisited;
import com.cassinisys.plm.model.rm.RequirementDeliverable;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.mfr.ItemManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.pdm.*;
import com.cassinisys.plm.repo.pgc.PGCItemSpecificationRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.pqm.ItemInspectionRepository;
import com.cassinisys.plm.repo.pqm.PRProblemItemRepository;
import com.cassinisys.plm.repo.pqm.ProblemReportRepository;
import com.cassinisys.plm.repo.pqm.QCRProblemItemRepository;
import com.cassinisys.plm.repo.req.PLMRequirementItemRepository;
import com.cassinisys.plm.repo.rm.RecentlyVisitedRepository;
import com.cassinisys.plm.repo.rm.RequirementDeliverableRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.cassinisys.plm.service.cm.*;
import com.cassinisys.plm.service.exim.importer.Importer;
import com.cassinisys.plm.service.pdm.PDMVaultService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.service.pqm.ProblemReportService;
import com.cassinisys.plm.service.pqm.QCRService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class ItemService implements CrudService<PLMItem, Integer>,
        PageableService<PLMItem, Integer>, ItemRevisionTypeSystem {

    ExpressionParser parser = new SpelExpressionParser();
    @Autowired
    SavedSearchRepository savedSearchRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private PRProblemItemRepository prProblemItemRepository;
    @Autowired
    private ItemRevisionAttributeRepository itemRevisionAttributeRepository;
    @Autowired
    private ItemAdvancedCriteria itemAdvancedCriteria;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ItemReferenceRepository itemReferenceRepository;
    @Autowired
    private ItemAttributeRepository itemAttributeRepository;
    @Autowired
    private ItemPredicateBuilder predicateBuilder;
    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private BomService bomService;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private ItemRevisionStatusHistoryRepository revisionStatusHistoryRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private BomItemFilterPredicateBuilder bomItemFilterPredicateBuilder;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private RecentlyVisitedRepository recentlyVisitedRepository;
    @Autowired
    private ItemFileRepository itemFileRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private EMailTemplateConfigRepository eMailTemplateConfigRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectDeliveravbleRepository projectDeliveravbleRepository;
    @Autowired
    private ActivityDeliverableRepository activityDeliverableRepository;
    @Autowired
    private TaskDeliverableRepository taskDeliverableRepository;
    @Autowired
    private AffectedItemRepository affectedItemRepository;
    @Autowired
    private WbsElementRepository wbsElementRepository;
    @Autowired
    private RequirementDeliverableRepository requirementDeliverableRepository;
    @Autowired
    private RelatedItemRepository relatedItemRepository;
    @Autowired
    private ECOService ecoService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private BOMConfigurationService bomConfigurationService;
    @Autowired
    private BOMConfigurationRepository bomConfigurationRepository;
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
    private ItemConfigurableAttributesRepository itemConfigurableAttributesRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ECRService ecrService;
    @Autowired
    private ProblemReportService problemReportService;
    @Autowired
    private QCRService qcrService;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private DCRService dcrService;
    @Autowired
    private PLMVarianceService varianceService;
    @Autowired
    private DCOAffectedItemRepository dcoAffectedItemRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private ItemInspectionRepository itemInspectionRepository;
    @Autowired
    private AttributeAttachmentService attributeAttachmentService;
    @Autowired
    private AttributeAttachmentRepository attributeAttachmentRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private ItemRevisionPredicateBuilder itemRevisionPredicateBuilder;
    @Autowired
    private Importer importer;
    @Autowired
    private ImportConverter importConverter;

    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private PDMGitHubItemRepositoryRepository pdmGitHubItemRepositoryRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PDMGitHubItemRevisionReleaseRepository pdmGitHubItemRevisionReleaseRepository;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private PGCItemSpecificationRepository pgcItemSpecificationRepository;
    @Autowired
    private SubstitutePartRepository substitutePartRepository;
    @Autowired
    private AlternatePartRepository alternatePartRepository;
    @Autowired
    private AppDetailsService appDetailsService;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private ItemRevisionTypeService itemRevisionTypeService;
    @Autowired
    private PLMRequirementItemRepository requirementItemRepository;
    @Autowired
    private ActivityStreamObjectRepository activityStreamObjectRepository;
    @Autowired
    private QCRProblemItemRepository qcrProblemItemRepository;
    @Autowired
    private VarianceAffectedItemRepository varianceAffectedItemRepository;
    @Autowired
    private PDMRevisionedObjectRepository pdmRevisionedObjectRepository;
    @Autowired
    private PDMAssemblyRepository pdmAssemblyRepository;
    @Autowired
    private PDMPartRepository pdmPartRepository;
    @Autowired
    private PDMDrawingRepository pdmDrawingRepository;
    @Autowired
    private PDMFileVersionRepository pdmFileVersionRepository;
    @Autowired
    private PDMFileRepository pdmFileRepository;
    @Autowired
    private PDMVaultService pdmVaultService;
    @Autowired
    private PDMThumbnailRepository pdmThumbnailRepository;
    @Autowired
    private PDMAssemblyConfigurationRepository pdmAssemblyConfigurationRepository;
    @Autowired
    private PDMPartConfigurationRepository pdmPartConfigurationRepository;
    @Autowired
    private PDMBOMOccurrenceRepository pdmBOMOccurrenceRepository;
    @Autowired
    private PDMAssemblyBOMOccurrenceRepository pdmAssemblyBOMOccurrenceRepository;
    @Autowired
    private PDMPartBOMOccurrenceRepository pdmPartBOMOccurrenceRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private PDMRevisionMasterRepository pdmRevisionMasterRepository;
    @Autowired
    private TagRepository tagRepository;

    @PostConstruct
    public void InitItemService() {
        itemRevisionTypeService.registerTypeSystem("item", new ItemService());
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#item,'create')")
    public PLMItem create(PLMItem item) {
        checkNotNull(item);
        item.setId(null);
        Integer workflowDef = null;

        if (item.getWorkflowDefId() != null) {
            workflowDef = item.getWorkflowDefId();
        }
        if (item.getItemType() == null && item.getTypeId() != null) {
            PLMItemType itemType = itemTypeRepository.findOne(item.getTypeId());
            item.setItemType(itemType);
        } else if (item.getItemType() == null && item.getTypeName() != null) {
            PLMItemType itemType = itemTypeRepository.findByName(item.getTypeName());
            item.setItemType(itemType);
        }

        if (item.getItemNumber() != null) {
            PLMItem item1 = itemRepository.findByItemNumberEqualsIgnoreCase(item.getItemNumber());
            if (item1 != null) {
                throw new CassiniException(messageSource.getMessage("itemNumber_already_exists", null, "Item Number already exist", LocaleContextHolder.getLocale()));
            }
        }

        Date fromDate = item.getFromDate();
        Date toDate = item.getToDate();
        if (item.getItemNumber() == null) {
            String pNum = autoNumberService.getNextNumber(item.getItemType().getItemNumberSource().getId());
            item.setItemNumber(pNum);
        } else {
            autoNumberService.saveNextNumber(item.getItemType().getItemNumberSource().getId(), item.getItemNumber());
        }
        item = itemRepository.save(item);

        PLMItemRevision revision = new PLMItemRevision();
        revision.setItemMaster(item.getId());
        revision.setObjectType(PLMObjectType.ITEMREVISION);

        PLMItemType plmItemType = itemTypeRepository.findOne(item.getItemType().getId());
        Lov lov = plmItemType.getRevisionSequence();
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(plmItemType.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        revision.setLifeCyclePhase(lifeCyclePhase);

        if (plmItemType.getRequiredEco()) {
            if (lov.getDefaultValue().equals("")) {
                revision.setRevision(lov.getValues()[0]);
            } else {
                revision.setRevision(lov.getDefaultValue());
            }
        } else {
            if (lov.getValues()[0].equals("-")) {
                revision.setRevision(lov.getValues()[1]);
            } else {
                revision.setRevision(lov.getValues()[0]);
            }
        }

        revision.setEffectiveFrom(fromDate);
        revision.setEffectiveTo(toDate);

        revision.setHasBom(false);
        revision.setHasFiles(false);
        PLMItemRevision itemRevision1 = itemRevisionRepository.save(revision);
        item.setLatestRevision(itemRevision1.getId());
        item = itemRepository.save(item);

        List<PLMItemTypeAttribute> configurableAttributes = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(item.getItemType().getId());
        configurableAttributes.forEach(plmItemTypeAttribute -> {
            ItemConfigurableAttributes itemConfigurableAttribute = new ItemConfigurableAttributes();
            itemConfigurableAttribute.setItem(itemRevision1.getId());
            itemConfigurableAttribute.setAttribute(plmItemTypeAttribute);
            itemConfigurableAttribute.setValues(plmItemTypeAttribute.getLov().getValues());
            itemConfigurableAttribute = itemConfigurableAttributesRepository.save(itemConfigurableAttribute);
        });

        if (item.getWorkflowDefId() != null) {
            attachItemWorkflow(itemRevision1.getId(), workflowDef);
        }
        PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
        statusHistory.setItem(itemRevision1.getId());
        statusHistory.setOldStatus(itemRevision1.getLifeCyclePhase());
        statusHistory.setNewStatus(itemRevision1.getLifeCyclePhase());
        statusHistory.setUpdatedBy(item.getCreatedBy());
        PLMItemRevisionStatusHistory statusHistory1 = revisionStatusHistoryRepository.save(statusHistory);

        /* App events */
        applicationEventPublisher.publishEvent(new ItemEvents.ItemCreatedEvent(item, revision));
        applicationEventPublisher.publishEvent(new ItemEvents.ItemRevisionCreatedEvent(item, revision));
        applicationEventPublisher.publishEvent(new PushNotificationEvents.CreateItemNotification(item));

        return item;
    }

    @Transactional
    @PreAuthorize("hasPermission(#item,'create')")
    public PLMItem createCopyItem(PLMItem item) {
        checkNotNull(item);
        PLMItem existItem = itemRepository.findByItemNumberEqualsIgnoreCase(item.getItemNumber());
        if (existItem != null) {
            throw new CassiniException(messageSource.getMessage("itemNumber_already_exists", null, "Item Number already exist", LocaleContextHolder.getLocale()));
        }
        PLMItem oldItem = itemRepository.findOne(item.getId());
        item.setId(null);
        item.setLatestReleasedRevision(null);

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
        PLMItem item1 = itemRepository.save(item);
        PLMItemRevision revision = new PLMItemRevision();
        revision.setItemMaster(item1.getId());
        revision.setObjectType(PLMObjectType.ITEMREVISION);
        PLMItemType plmItemType = itemTypeRepository.findOne(item1.getItemType().getId());
        Lov lov = plmItemType.getRevisionSequence();

        PLMLifeCyclePhase lifeCyclePhase = plmItemType.getLifecycle().getPhaseByType(LifeCyclePhaseType.PRELIMINARY);
        revision.setLifeCyclePhase(lifeCyclePhase);
        revision.setRevision(lov.getDefaultValue());
        revision.setHasBom(false);
        revision.setHasFiles(false);
        PLMItemRevision itemRevision1 = itemRevisionRepository.save(revision);
        item1.setLatestRevision(itemRevision1.getId());
        autoNumberService.saveNextNumber(item1.getItemType().getItemNumberSource().getId(), item1.getItemNumber());
        item1 = itemRepository.save(item1);

        PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
        statusHistory.setItem(itemRevision1.getId());
        statusHistory.setOldStatus(itemRevision1.getLifeCyclePhase());
        statusHistory.setNewStatus(itemRevision1.getLifeCyclePhase());
        statusHistory.setUpdatedBy(item.getCreatedBy());
        statusHistory = revisionStatusHistoryRepository.save(statusHistory);

        copyItemMasterAttributes(oldItem, item1);
        copyItemRevisionAttributes(itemRevision, itemRevision1);
        copyItems(itemRevision, itemRevision1);
        copyFolderStructure(itemRevision, itemRevision1);
        copyManufactureParts(itemRevision, itemRevision1);
        copyBomConfigurations(itemRevision, itemRevision1);
        copyConfigurableAttributes(itemRevision, itemRevision1);
        /* App events */
        applicationEventPublisher.publishEvent(new ItemEvents.ItemCopiedEvent(itemRevision1, itemRevision));

        return item;
    }

    @Transactional
    public List<PLMItemManufacturerPart> createManufacturerParts(Integer itemId, List<PLMItemManufacturerPart> itemManufacturerParts) {
        List<PLMItemManufacturerPart> manufacturerParts = new ArrayList<>();
        String addParts = null;
        List<PLMManufacturerPart> parts = new ArrayList<>();
        for (PLMItemManufacturerPart itemManufacturerPart : itemManufacturerParts) {
            itemManufacturerPart.setItem(itemId);
            PLMItemManufacturerPart itemMfrPart = itemManufacturerPartRepository.save(itemManufacturerPart);
            manufacturerParts.add(itemMfrPart);
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(itemMfrPart.getManufacturerPart().getId());
            if (addParts == null) {
                addParts = manufacturerPart.getPartName();
            } else {
                addParts = addParts + " ," + manufacturerPart.getPartName();
            }
            parts.add(manufacturerPart);
        }
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        applicationEventPublisher.publishEvent(new ItemEvents.ItemMfrPartsAddedEvent(itemRevision, itemManufacturerParts));
        updateItem(itemId);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

        String message = messageSource.getMessage("mfr_parts_added_to_item", null, "{0} Manufacturer Parts added by {1} to ( {2} - {3} : Rev {4} {5} ) Item", LocaleContextHolder.getLocale());
        String result = MessageFormat.format(message + ".", addParts, person.getFullName(), item.getItemNumber(), item.getItemName(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
        String itemNotification = messageSource.getMessage("item_notification_subject", null, "{0} : Rev {1} : {2} Notification", LocaleContextHolder.getLocale());
        String notificationResult = MessageFormat.format(itemNotification + ".", item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());

        sendItemSubscribeNotification(item, result, notificationResult);
        return manufacturerParts;
    }

    @Transactional
    public void deleteItemMfrPart(Integer mfrPart) {
        PLMItemManufacturerPart mfrPart1 = itemManufacturerPartRepository.findOne(mfrPart);
        itemManufacturerPartRepository.deleteByManufacturerPart(mfrPart1.getManufacturerPart());
    }

    @Transactional
    public void deleteMfrPartByItem(Integer itemId, Integer mfrId) {
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(mfrId);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        List<PLMManufacturerPart> parts = new ArrayList<>();
        parts.add(manufacturerPart);
        PLMItemManufacturerPart itemManufacturerPart = itemManufacturerPartRepository.findByItemAndManufacturerPart(itemId, manufacturerPart);
        applicationEventPublisher.publishEvent(new ItemEvents.ItemMfrPartDeletedEvent(itemRevision, itemManufacturerPart));
        itemManufacturerPartRepository.deleteByItemAndManufacturerPart(itemId, manufacturerPart);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        String message = messageSource.getMessage("mfr_parts_deleted_in_item", null, "{0} Manufacturer Part deleted by {1} from ( {2} - {3} : Rev {4} : {5} ) Item", LocaleContextHolder.getLocale());
        String result = MessageFormat.format(message + ".", manufacturerPart.getPartName(), person.getFullName(), item.getItemNumber(), item.getItemName(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
        String itemNotification = messageSource.getMessage("item_notification_subject", null, "{0} : Rev {1} : {2} Notification", LocaleContextHolder.getLocale());
        String notificationResult = MessageFormat.format(itemNotification + ".", item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());

        sendItemSubscribeNotification(item, result, notificationResult);
    }

    @Transactional(readOnly = true)
    public Long getItemMasterCount() {
        return itemRepository.count();
    }

    @Transactional(readOnly = true)
    public Long getItemRevisionCount() {
        return itemRevisionRepository.count();
    }

    @Transactional
    @PreAuthorize("hasPermission(#itemRevision,'create')")
    public PLMItemRevision createRevision(PLMItemRevision itemRevision) {
        checkNotNull(itemRevision);
        itemRevision.setId(null);
        itemRevision = itemRevisionRepository.save(itemRevision);
        return itemRevision;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#item.id ,'edit')")
    public PLMItem update(PLMItem item) {
        checkNotNull(item);

        PLMItem oldItem = JsonUtils.cloneEntity(itemRepository.findOne(item.getId()), PLMItem.class);

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
        String message = null;
        String mailSubject = item.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
        if (message != null) {
            sendItemSubscribeNotification(item, message, mailSubject);
        }
        item = itemRepository.save(item);
        if (!oldItem.getConfigurable() && item.getConfigurable()) {
            List<PLMItemTypeAttribute> configurableAttributes = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(item.getItemType().getId());
            configurableAttributes.forEach(plmItemTypeAttribute -> {
                ItemConfigurableAttributes existAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(itemRevision.getId(), plmItemTypeAttribute);
                if (existAttribute == null) {
                    ItemConfigurableAttributes itemConfigurableAttribute = new ItemConfigurableAttributes();
                    itemConfigurableAttribute.setItem(itemRevision.getId());
                    itemConfigurableAttribute.setAttribute(plmItemTypeAttribute);
                    itemConfigurableAttribute.setValues(plmItemTypeAttribute.getLov().getValues());
                    itemConfigurableAttribute = itemConfigurableAttributesRepository.save(itemConfigurableAttribute);
                }
            });
        }
        /* App events */
        applicationEventPublisher.publishEvent(new ItemEvents.ItemBasicInfoUpdatedEvent(oldItem, item, itemRevision));

        return item;
    }

    @Transactional
    public PLMItem updateItemFromEco(PLMItem item) {
        checkNotNull(item);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMItem existItemData = itemRepository.findOne(item.getId());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
        String message = null;
        String result = null;

        if (!existItemData.getItemName().equals(item.getItemName())) {
            message = messageSource.getMessage("item_name_changed", null, "{0} : Rev {1} Name ' {2} ' changed by {3} to ' {4} '", LocaleContextHolder.getLocale());
            result = MessageFormat.format(message + ".", item.getItemNumber(), itemRevision.getRevision(), existItemData.getItemName(), person.getFullName(), item.getItemName());

        }
        if (existItemData.getDescription() != null && !existItemData.getDescription().isEmpty()) {
            if (item.getDescription() != null && item.getDescription() != "") {
                if (!existItemData.getDescription().equals(item.getDescription())) {
                    message = messageSource.getMessage("item_description_changed", null, "{0} : Rev {1} Description ' {2} ' changed by {3} to ' {4} '", LocaleContextHolder.getLocale());
                    result = MessageFormat.format(message + ".", item.getItemNumber(), itemRevision.getRevision(), existItemData.getDescription(), person.getFullName(), item.getDescription());

                }
            } else {
                message = messageSource.getMessage("item_description_removed", null, "{0} : Rev {1} Description ' {2} ' removed by {3}", LocaleContextHolder.getLocale());
                result = MessageFormat.format(message + ".", item.getItemNumber(), itemRevision.getRevision(), existItemData.getDescription(), person.getFullName());

            }

        } else {
            if (item.getDescription() != null) {
                message = messageSource.getMessage("item_description_added", null, "{0} : Rev {1} Description ' {2} ' added by {3}", LocaleContextHolder.getLocale());
                result = MessageFormat.format(message + ".", item.getItemNumber(), itemRevision.getRevision(), item.getDescription(), person.getFullName());

            }
        }
        String itemNotification = messageSource.getMessage("item_notification_subject", null, "{0} : Rev {1} : {2} Notification", LocaleContextHolder.getLocale());
        String notificationResult = MessageFormat.format(itemNotification + ".", item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());

        if (result != null) {
            try {
                sendItemSubscribeNotification(item, result, notificationResult);
            } catch (Exception e) {
            }
        }
        item = itemRepository.save(item);

        return item;
    }

    @Transactional
    @PreAuthorize("hasPermission(#itemRevision.id ,'edit')")
    public PLMItemRevision updateRevision(PLMItemRevision itemRevision) {
        checkNotNull(itemRevision);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(itemRevision);
        if (bomList.size() == 0) {
            itemRevision.setHasBom(false);
        }
        PLMItemRevision revision = JsonUtils.cloneEntity(itemRevisionRepository.findOne(itemRevision.getId()), PLMItemRevision.class);
        if (!itemRevision.getLifeCyclePhase().getId().equals(revision.getLifeCyclePhase().getId())) {
            PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
            statusHistory.setItem(revision.getId());
            statusHistory.setOldStatus(revision.getLifeCyclePhase());
            statusHistory.setNewStatus(itemRevision.getLifeCyclePhase());
            statusHistory.setUpdatedBy(item.getModifiedBy());
            statusHistory = revisionStatusHistoryRepository.save(statusHistory);
        }
        if (!revision.getIncorporate() && itemRevision.getIncorporate()) {
            itemRevision.setIncorporateDate(new Date());
        } else if (revision.getIncorporate() && !itemRevision.getIncorporate()) {
            itemRevision.setIncorporateDate(null);
        }
        itemRevision = itemRevisionRepository.save(itemRevision);
        applicationEventPublisher.publishEvent(new ItemEvents.ItemEffectiveDatesUpdatedEvent(item, revision, itemRevision));
        applicationEventPublisher.publishEvent(new ItemEvents.ItemIncorporateUpdatedEvent(item, revision, itemRevision));
        return itemRevision;
    }

    @Transactional
    @PreAuthorize("hasPermission(#item.id ,'edit')")
    public PLMItemRevision updateRevisionBomInclusionRules(PLMItemRevision item) {
        item = itemRevisionRepository.save(item);
        return item;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        checkNotNull(id);
        PLMItem item = itemRepository.findOne(id);
        if (item == null) {
            throw new ResourceNotFoundException();
        }
        List<RecentlyVisited> recentlyVisiteds = recentlyVisitedRepository.findByObjectId(item.getId());
        for (RecentlyVisited recentlyVisited : recentlyVisiteds) {
            recentlyVisitedRepository.delete(recentlyVisited.getId());
        }

        List<PLMItemRevision> revisions = itemRevisionRepository.findByItemMasterOrderByIdAsc(item.getId());

        if (item.getConfigured()) {
            PLMItem parent = itemRepository.findOne(item.getInstance());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(parent.getLatestRevision());
            applicationEventPublisher.publishEvent(new ItemEvents.ItemInstanceDeletedEvent(itemRevision, item));
        }
        itemRepository.delete(id);

        for (PLMItemRevision revision : revisions) {
            activityStreamObjectRepository.deleteActivityStreamObjectByObjectId(revision.getId());
            CassiniObject object = objectRepository.findById(revision.getId());
            if (object != null) {
                applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(object.getId()));
                objectRepository.deleteById(revision.getId());
            }
        }

    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMItem get(Integer id) {
        checkNotNull(id);
        PLMItem item = itemRepository.findOne(id);
        List<PLMAffectedItem> affectedItem = affectedItemRepository.findByItem(item.getLatestRevision());
        for (PLMAffectedItem affectedItem1 : affectedItem) {
            PLMECO plmeco = ecoRepository.findOne(affectedItem1.getChange());
            if (plmeco != null) {
                item.setEcoNumber(plmeco.getEcoNumber());
            }
        }
        if (item.getItemImage() != null) {
            PLMItemFile itemFile = itemFileRepository.findOne(item.getItemImage());
            item.setItemImageObj(itemFile);
        }

        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(item.getLatestRevision());
        if (workflow != null) {
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    item.setStartWorkflow(true);
                }
            }
        }

        return item;
    }

    @Transactional(readOnly = true)
    public PLMItem getItemByRevision(Integer revId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(revId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        return item;
    }

    @Transactional
    @PreAuthorize("hasPermission(#revisionId,'delete')")
    public void deleteItemRevision(Integer revisionId) {
        itemRevisionRepository.delete(revisionId);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMItemRevision getItemRevision(Integer revisionId) {
        return itemRevisionRepository.findOne(revisionId);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMItemRevision getRevision(Integer id) {
        checkNotNull(id);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        List<PLMAffectedItem> affectedItem = new ArrayList<>();
        if (itemRevision.getChangeOrder() == null) {
            affectedItem.addAll(affectedItemRepository.findByToItem(itemRevision.getId()));
            affectedItem.addAll(affectedItemRepository.findByItem(itemRevision.getId()));
            for (PLMAffectedItem affectedItem1 : affectedItem) {
                PLMECO plmeco = ecoRepository.findOne(affectedItem1.getChange());
                if (plmeco != null) {
                    itemRevision.setPlmeco(plmeco);
                }
            }
            List<PLMDCOAffectedItem> dcoAffectedItems = dcoAffectedItemRepository.findByToItem(itemRevision.getId());
            if (dcoAffectedItems.size() > 0) {
                dcoAffectedItems.forEach(plmdcoAffectedItem -> {
                    PLMDCO plmdco = dcoRepository.findOne(plmdcoAffectedItem.getDco());
                    if (plmdco != null) {
                        itemRevision.setDco(plmdco);
                        itemRevision.setOldRevision(plmdcoAffectedItem.getFromRevision());
                    }
                });
            }
        } else {
            PLMChange change = changeRepository.findOne(itemRevision.getChangeOrder());
            if (change.getChangeType().equals(ChangeType.ECO)) {
                itemRevision.setPlmeco(ecoRepository.findOne(change.getId()));
            } else {
                itemRevision.setDco(dcoRepository.findOne(change.getId()));
            }
        }
        return itemRevision;
    }

    @Transactional(readOnly = true)
    public List<PLMItemRevision> findMultipleItemMaster(List<Integer> ids) {
        return itemRevisionRepository.findByItemMasterIn(ids);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMItemRevision getByItemMasterAndRevision(Integer itemMaster, String rev) {
        return itemRevisionRepository.getByItemMasterAndRevision(itemMaster, rev);
    }

    @Transactional(readOnly = true)
    public List<PLMItemRevision> getRevisionsByIds(List<Integer> ids) {
        return itemRevisionRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemRevision> getItemRevisions(Integer itemId) {
        return itemRevisionRepository.findByItemMasterOrderByIdAsc(itemId);
    }

    @Transactional(readOnly = true)
    public List<Integer> getItemRevisionIds(Integer itemId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        return itemRevisionRepository.getRevisionIdsByItemId(itemRevision.getItemMaster());
    }

    public List<Integer> getItemRevisionsIdsByItem(Integer itemId) {
        List<Integer> ids = new ArrayList<>();
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        itemRevisionRepository = webApplicationContext.getBean(ItemRevisionRepository.class);
        List<PLMItemRevision> itemRevisions = itemRevisionRepository.findByItemMasterOrderByIdAsc(itemId);
        ids.addAll(itemRevisions.stream().map(PLMItemRevision::getId).collect(Collectors.toList()));
        return ids;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemRevision> getItemRevisionStatusHistory(Integer item) {
        PLMItem item1 = itemRepository.findOne(item);
        List<PLMItemRevision> revisions = itemRevisionRepository.getByItemMasterOrderByCreatedDateDesc(item1.getId());
        revisions.forEach(revision -> {
            List<PLMItemRevisionStatusHistory> history = revisionStatusHistoryRepository.findByItemOrderByTimestampDesc(revision.getId());
            if (revision.getChangeOrder() == null) {
                List<PLMAffectedItem> plmAffectedItems = affectedItemRepository.findByToItem(revision.getId());
                plmAffectedItems.forEach(eco -> {
                            PLMECO plmecos = ecoRepository.findById(eco.getChange());
                            revision.setPlmeco(plmecos);
                            revision.setOldRevision(eco.getFromRevision());
                        }
                );
                List<PLMDCOAffectedItem> dcoAffectedItems = dcoAffectedItemRepository.findByToItem(revision.getId());
                if (dcoAffectedItems.size() > 0) {
                    dcoAffectedItems.forEach(plmdcoAffectedItem -> {
                        PLMDCO plmdco = dcoRepository.findOne(plmdcoAffectedItem.getDco());
                        revision.setDco(plmdco);
                        revision.setOldRevision(plmdcoAffectedItem.getFromRevision());
                    });
                }
            } else {
                PLMChange change = changeRepository.findOne(revision.getChangeOrder());
                if (change.getChangeType().equals(ChangeType.ECO)) {
                    revision.setPlmeco(ecoRepository.findOne(change.getId()));
                    PLMAffectedItem affectedItem = affectedItemRepository.findByChangeAndToItem(change.getId(), revision.getId());
                    if (affectedItem != null) {
                        revision.setOldRevision(affectedItem.getFromRevision());
                    } else {
                        affectedItem = affectedItemRepository.findByChangeAndItem(change.getId(), revision.getId());
                        revision.setOldRevision(affectedItem.getFromRevision());
                    }
                } else {
                    revision.setDco(dcoRepository.findOne(change.getId()));
                    PLMDCOAffectedItem affectedItem = dcoAffectedItemRepository.findByDcoAndToItem(change.getId(), revision.getId());
                    if (affectedItem != null) {
                        revision.setOldRevision(affectedItem.getFromRevision());
                    } else {
                        affectedItem = dcoAffectedItemRepository.findByDcoAndItem(change.getId(), revision.getId());
                        revision.setOldRevision(affectedItem.getFromRevision());
                    }
                }
            }
            revision.setStatusHistory(history);
        });
        return revisions;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItem> getAll() {
        return itemRepository.findAll();
    }


    /* Saved Searches Methods*/

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMItem> findAll(Pageable pageable) {
        Page<PLMItem> plmItems = itemRepository.findAll(pageable);
        for (PLMItem item : plmItems.getContent()) {
            item = setItemCounts(item);
        }
        return plmItems;
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<ItemsDto> getAllItems(Pageable pageable, ItemCriteria itemCriteria) {
        Predicate predicate = predicateBuilder.build(itemCriteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItems = itemRepository.findAll(predicate, pageable);
        List<ItemsDto> items = new ArrayList<>();
        plmItems.getContent().stream().forEach(item -> {
            ItemsDto itemDto = convertToItemsDto(item);
            items.add(itemDto);
        });

        return new PageImpl<ItemsDto>(items, pageable, plmItems.getTotalElements());
    }

    private ItemsDto convertToItemsDto(PLMItem item) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
        ItemsDto itemDto = new ItemsDto();
        itemDto.setId(item.getId());
        itemDto.setItemName(item.getItemName());
        itemDto.setItemNumber(item.getItemNumber());
        itemDto.setItemTypeName(item.getItemType().getName());
        itemDto.setItemType(item.getItemType());
        itemDto.setDescription(item.getDescription());
        itemDto.setUnits(item.getUnits());
        itemDto.setRevision(itemRevision.getRevision());
        itemDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
        itemDto.setLatestRevision(item.getLatestRevision());
        itemDto.setCreatedBy(item.getCreatedBy());
        itemDto.setModifiedBy(item.getModifiedBy());
        itemDto.setCreatedByName(personRepository.findOne(item.getCreatedBy()).getFullName());
        itemDto.setModifiedByName(personRepository.findOne(item.getModifiedBy()).getFullName());
        itemDto.setItemClass(item.getItemType().getItemClass());
        itemDto.setMakeOrBuy(item.getMakeOrBuy());
        itemDto.setReleased(itemRevision.getReleased());
        itemDto.setRejected(itemRevision.getRejected());
        itemDto.setReleasedDate(itemRevision.getReleasedDate());
        itemDto.setConfigured(item.getConfigured());
        itemDto.setConfigurable(item.getConfigurable());
        itemDto.setLockObject(item.getLockObject());
        itemDto.setHasAlternates(item.getHasAlternates());
        itemDto.setHasBom(itemRevision.getHasBom());
        if (item.getLockedBy() != null) {
            itemDto.setLockedByName(item.getLockedBy().getFullName());
            itemDto.setLockedBy(item.getLockedBy().getId());
        }
        if (item.getThumbnail() != null) {
            itemDto.setHasThumbnail(true);
        }
        itemDto.setModifiedDate(item.getModifiedDate());
        itemDto.setCreatedDate(item.getCreatedDate());
        itemDto.setLatestRevisionObject(itemRevision);
        ObjectFileDto objectFileDto = objectFileService.getObjectFiles(itemRevision.getId(), PLMObjectType.ITEM,false);
        List<PLMSubscribe> subscribes = subscribeRepository.findByObjectIdAndSubscribeTrue(item.getId());
        Integer itemManufacturerParts = itemManufacturerPartRepository.getMfrPartsCountByItem(itemRevision.getId());
        if (itemManufacturerParts > 0) {
            itemDto.setHasMfrParts(true);
        }
        itemDto.setItemFiles(objectFileDto.getObjectFiles());
        itemDto.setSubscribes(subscribes);

        if (item.getItemType().getItemClass() != null && item.getItemType().getItemClass().equals(ItemClass.DOCUMENT)) {
            List<PLMDCO> dco = dcoService.findByAffectedItem(itemRevision.getId());
            List<PLMDCR> dcr = dcrService.findByAffectedItem(itemRevision.getId());
            if (dco.size() > 0 || dcr.size() > 0) {
                itemDto.setHasChanges(true);
            }

        }

        if (item.getItemType().getItemClass() != null && !item.getItemType().getItemClass().equals(ItemClass.DOCUMENT)) {
            List<PLMECO> eco = ecoService.findByAffectedItem(itemRevision.getId());
            List<PLMECR> ecr = ecrService.findByAffectedItem(itemRevision.getId());
            if (eco.size() > 0 || ecr.size() > 0) {
                itemDto.setHasChanges(true);
            }
        }

        Integer pqmProblemReportsCount = prProblemItemRepository.getProblemReportCountByItem(itemRevision.getId());
        Integer qcrCount = qcrProblemItemRepository.getQCRCountByItem(itemRevision.getId());
        if (pqmProblemReportsCount > 0 || qcrCount > 0) {
            itemDto.setHasQuality(true);
        }
        List<Integer> revisionIds = itemRevisionRepository.getRevisionIdsByItemId(item.getId());
        Integer variances = varianceAffectedItemRepository.getVarianceCountByItem(revisionIds);
        if (variances > 0) {
            itemDto.setHasVariance(true);
        }
        itemDto.setTagsCount(tagRepository.getObjectTagCount(item.getId()));
        return itemDto;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMItem> getItemsByClass(ItemClass itemClass, Pageable pageable) {
        Page<PLMItem> plmItems = itemRepository.findByItemClass(itemClass, pageable);
        for (PLMItem item : plmItems.getContent()) {
            item = setItemCounts(item);
        }
        return plmItems;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMItem> getFilterBomItems(Pageable pageable, BomItemFilterCriteria bomItemFilterCriteria) {
        Predicate predicate = bomItemFilterPredicateBuilder.build(bomItemFilterCriteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItem = itemRepository.findAll(predicate, pageable);
        for (PLMItem item : plmItem.getContent()) {
            PLMItemRevision revision = itemRevisionRepository.findOne(item.getLatestRevision());
            List<PLMItemFile> itemFiles = itemFileRepository.findByItem(revision);
            item.setItemFiles(itemFiles);

            if (bomItemFilterCriteria.getEco() != null || bomItemFilterCriteria.getDco() != null) {
                String toRevision = getNextRevisionSequence(item);
                for (PLMLifeCyclePhase lifeCyclePhase : item.getItemType().getLifecycle().getPhases()) {
                    if (lifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                        item.getToLifecyclePhases().add(lifeCyclePhase);
                    }
                }
                item.setToRevision(toRevision);
                PLMItemRevision oldRevision = itemRevisionRepository.getByItemMasterAndRevision(item.getId(), "-");
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
                if (item.getLatestReleasedRevision() == null && itemRevision.getRejected()) {
                    if (oldRevision != null) {
                        item.setRev(oldRevision);
                    }
                }

                List<PLMAffectedItem> affectedItems = affectedItemRepository.findByItemAndToRevision(item.getLatestRevision(), toRevision);
                List<PLMAffectedItem> affectedToItems = affectedItemRepository.findByToItemAndToRevision(item.getLatestRevision(), itemRevision.getRevision());
                if (item.getLatestReleasedRevision() != null) {
                    List<PLMAffectedItem> releasedAffectedItems = affectedItemRepository.findByItemAndToRevision(item.getLatestReleasedRevision(), toRevision);
                    setChangesForItem(releasedAffectedItems, item);

                }
                setChangesForItem(affectedToItems, item);

                setChangesForItem(affectedItems, item);

                if (oldRevision != null && item.getLatestReleasedRevision() == null && itemRevision.getRejected()) {
                    List<PLMAffectedItem> plmAffectedItems = affectedItemRepository.findByItemAndToRevision(oldRevision.getId(), toRevision);
                    setChangesForItem(plmAffectedItems, item);

                }
            } else if (!Criteria.isEmpty(bomItemFilterCriteria.getProblemReport()) && !bomItemFilterCriteria.getRelated()) {
                PQMProblemReport problemReport = problemReportRepository.findOne(bomItemFilterCriteria.getProblemReport());
                if (problemReport.getProduct() != null) {
                    PLMItemRevision productRevision = itemRevisionRepository.findOne(problemReport.getProduct());
                    List<Integer> ids = prProblemItemRepository.getItemsByProblemReport(problemReport.getId());

                    PLMBom bom = bomRepository.findByParentAndItem(productRevision, item);
                    if (bom != null && bom.getAsReleasedRevision() != null) {
                        item.setAsReleasedRevision(bom.getAsReleasedRevision());
                        PLMItemRevision asReleasedRevision = itemRevisionRepository.findOne(item.getAsReleasedRevision());
                        if (asReleasedRevision.getReleased()) {
                        /*--------  To Hide added items to problem report --------------*/
                        /*if (ids.size() > 0) {
                            Integer count = bomRepository.getReleasedBomItemsCountByItemAndIds(asReleasedRevision.getId(), ids);
                            if (count > 0) {
                                item.setHasBom(true);
                            }
                        } else {
                            Integer count = bomRepository.getReleasedBomItemsCountByItem(asReleasedRevision.getId());
                            if (count > 0) {
                                item.setHasBom(true);
                            }
                        }*/
                            Integer count = bomRepository.getReleasedBomItemsCountByItem(asReleasedRevision.getId());
                            if (count > 0) {
                                item.setHasBom(true);
                            }
                            PQMPRProblemItem problemItem = prProblemItemRepository.findByItemAndProblemReport(item.getAsReleasedRevision(), bomItemFilterCriteria.getProblemReport());
                            if (problemItem != null) {
                                item.setAlreadyExist(true);
                            }
                        }
                    }
                }
            }
        }
        return plmItem;
    }

    private void setChangesForItem(List<PLMAffectedItem> plmAffectedItems, PLMItem item) {
        if (plmAffectedItems.size() > 0) {
            plmAffectedItems.forEach(plmAffectedItem -> {
                PLMECO plmeco = ecoRepository.findOne(plmAffectedItem.getChange());
                if (plmeco != null) {
                    if (!plmeco.getReleased() && !plmeco.getCancelled()) {
                        item.setPendingEco(true);
                        item.setEcoNumber(plmeco.getEcoNumber());
                        item.setChangeId(plmeco.getId());
                    }
                } else {
                    PLMDCO plmdco = dcoRepository.findOne(plmAffectedItem.getChange());
                    if (plmdco != null && !plmdco.getStatusType().equals(WorkflowStatusType.RELEASED) && !plmdco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                        item.setPendingEco(true);
                        item.setEcoNumber(plmdco.getDcoNumber());
                        item.setChangeId(plmdco.getId());
                    }
                }
            });
        }
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMItemRevision> getAllLatestItemRevisions(Pageable pageable) {
        return itemRevisionRepository.getLatestItemRevisions(pageable);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMItemRevision> searchItemRevisions(Pageable pageable, ItemCriteria itemCriteria) {
        itemCriteria.setLatest(true);
        Predicate predicate = itemRevisionPredicateBuilder.build(itemCriteria, QPLMItemRevision.pLMItemRevision);
        return itemRevisionRepository.findAll(predicate, pageable);
    }

    @Transactional
    @PreAuthorize("hasPermission(#PLMSavedSearch,'create')")
    public PLMSavedSearch saveSearches(PLMSavedSearch PLMSavedSearch) {
        checkNotNull(PLMSavedSearch);
        return savedSearchRepository.save(PLMSavedSearch);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMSavedSearch> getAllSavedSearches() {
        return savedSearchRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public PLMSavedSearch getSavedSearchBySearchId(Integer searchId) {
        checkNotNull(searchId);
        return savedSearchRepository.findOne(searchId);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public PLMSavedSearch updateSavedSearches(Integer searchId, PLMSavedSearch PLMSavedSearch) {
        checkNotNull(searchId);
        return savedSearchRepository.save(PLMSavedSearch);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteSavedSearch(Integer searchId) {
        checkNotNull(searchId);
        savedSearchRepository.delete(searchId);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMItemRevision reviseItem(Integer itemId) {
        PLMItem item = itemRepository.findOne(itemId);
        PLMItemRevision revision = itemRevisionRepository.findOne(item.getLatestRevision());
        if (revision != null) {
            return reviseRevisionItem(revision, null);
        } else {
            throw new ItemServiceException(messageSource.getMessage("revise_item_failed_item_not_found", null, Locale.getDefault()));
        }
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMItemRevision reviseRevisionItem(PLMItemRevision revision, String nextRev) {
        PLMItem item = itemRepository.findOne(revision.getItemMaster());
        if (nextRev == null) {
            nextRev = getNextRevisionSequence(item);
        }
        if (nextRev != null) {
            PLMItemRevision copy = createNextRev(revision, nextRev);
            item.setLatestRevision(copy.getId());
            itemRepository.save(item);
            //Copy the related
            copyDesignObject(revision, copy);
            copyItems(revision, copy);
            copyItemInstance(revision, copy);
            copyItemRevisionAttributes(revision, copy);
            copyRevisionAttributes(revision, copy);
            copyFolderStructure(revision, copy);
            copyManufactureParts(revision, copy);
            copyBomConfigurations(revision, copy);
            copyConfigurableAttributes(revision, copy);
            return copy;
        } else {
            throw new ItemServiceException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMItemRevision getLatestRevision(Integer itemMaster) {
        PLMItem item = itemRepository.findOne(itemMaster);
        return itemRevisionRepository.findOne(item.getLatestRevision());
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItemRevision> getItemRevisionsByItemIds(List<Integer> itemIds) {
        List<Integer> revisionIds = itemRepository.getLatestRevisionIdsByItemIds(itemIds);
        return itemRevisionRepository.findByIdIn(revisionIds);
    }

    public void copyManufactureParts(PLMItemRevision oldRevision, PLMItemRevision newRevision) {
        List<PLMItemManufacturerPart> manufacturerParts = itemManufacturerPartRepository.findByItem(oldRevision.getId());
        for (PLMItemManufacturerPart manufacturerPart : manufacturerParts) {
            PLMItemManufacturerPart existPart = itemManufacturerPartRepository.findByItemAndManufacturerPart(newRevision.getId(), manufacturerPart.getManufacturerPart());
            if (existPart == null) {
                PLMItemManufacturerPart newPart = new PLMItemManufacturerPart();
                newPart.setItem(newRevision.getId());
                newPart.setManufacturerPart(manufacturerPart.getManufacturerPart());
                newPart.setStatus(manufacturerPart.getStatus());
                newPart.setNotes(manufacturerPart.getNotes());
                PLMItemManufacturerPart part = itemManufacturerPartRepository.save(newPart);
            }
        }
    }

    private void copyConfigurableAttributes(PLMItemRevision oldRevision, PLMItemRevision newRevision) {
        List<ItemConfigurableAttributes> configurableAttributes = itemConfigurableAttributesRepository.findByItem(oldRevision.getId());
        configurableAttributes.forEach(itemConfigurableAttribute -> {
            ItemConfigurableAttributes existConfigurableAttr = itemConfigurableAttributesRepository.findByItemAndAttribute(newRevision.getId(), itemConfigurableAttribute.getAttribute());
            if (existConfigurableAttr == null) {
                existConfigurableAttr = new ItemConfigurableAttributes();
                existConfigurableAttr.setItem(newRevision.getId());
                existConfigurableAttr.setAttribute(itemConfigurableAttribute.getAttribute());
                existConfigurableAttr.setValues(itemConfigurableAttribute.getValues());
                existConfigurableAttr = itemConfigurableAttributesRepository.save(existConfigurableAttr);
            }
        });
    }

    private void copyBomConfigurations(PLMItemRevision oldRevision, PLMItemRevision newRevision) {
        List<BOMConfiguration> configurations = bomConfigurationRepository.findByItem(oldRevision.getId());
        for (BOMConfiguration bomConfiguration : configurations) {
            BOMConfiguration configuration = bomConfigurationRepository.findByItemAndName(newRevision.getId(), bomConfiguration.getName());
            if (configuration == null) {
                BOMConfiguration newConfiguration = new BOMConfiguration();

                newConfiguration.setItem(newRevision.getId());
                newConfiguration.setName(bomConfiguration.getName());
                newConfiguration.setDescription(bomConfiguration.getDescription());

                String json = bomConfiguration.getRules();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = null;
                try {
                    map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                    });

                    Set<Map.Entry<String, Object>> entries1 = map.entrySet();

                    for (Map.Entry<String, Object> entry : entries1) {
                        if (entry.getKey().equals("id")) {
                            entry.setValue(newRevision.getId());
                        }
                    }

                    String value = new ObjectMapper().writeValueAsString(map);
                    newConfiguration.setRules(value);
                } catch (Exception e) {
                    new CassiniException(e.getMessage());
                }

                newConfiguration = bomConfigurationService.create(newConfiguration);
            }
        }
    }

    @PreAuthorize("hasPermission(#item,'create')")
    private PLMItemRevision createNextRev(PLMItemRevision item, String nextRev) {
        Integer notReleasedDocumentCount = objectDocumentRepository.getNotReleasedDocumentsCount(item.getId());
        PLMItem item1 = itemRepository.findOne(item.getItemMaster());
        if (notReleasedDocumentCount > 0) {
            String message = messageSource.getMessage("item_has_unreleased_documents", null, "[{0}] item has some unreleased documents", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", item1.getItemNumber());
            throw new CassiniException(result);
        }
        PLMItemRevision copy = new PLMItemRevision();
        PLMItemType plmItemType = itemTypeRepository.findOne(item1.getItemType().getId());
        PLMLifeCyclePhase lifeCyclePhase = plmItemType.getLifecycle().getPhaseByType(LifeCyclePhaseType.PRELIMINARY);
        copy.setItemMaster(item.getItemMaster());
        copy.setRevision(nextRev);
        copy.setLifeCyclePhase(lifeCyclePhase);
        copy.setHasBom(item.getHasBom());
        copy.setHasFiles(item.getHasFiles());
        copy.setInclusionRules(item.getInclusionRules());
        copy.setItemExclusionRules(item.getItemExclusionRules());
        copy.setAttributeExclusionRules(item.getAttributeExclusionRules());
        PLMItemRevision revision = itemRevisionRepository.save(copy);
        PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
        statusHistory.setItem(revision.getId());
        statusHistory.setOldStatus(revision.getLifeCyclePhase());
        statusHistory.setNewStatus(revision.getLifeCyclePhase());
        statusHistory.setUpdatedBy(item.getCreatedBy());
        PLMItemRevisionStatusHistory statusHistory1 = revisionStatusHistoryRepository.save(statusHistory);

        return revision;
    }

    private void copyItemRevisionAttributes(PLMItemRevision oldRevision, PLMItemRevision newRevision) {
        List<PLMItemRevisionAttribute> attrs = getItemRevisionAttributes(oldRevision.getId());
        for (PLMItemRevisionAttribute attr : attrs) {
            PLMItemRevisionAttribute revisionAttribute = new PLMItemRevisionAttribute();
            revisionAttribute.setId(new ObjectAttributeId(newRevision.getId(), attr.getId().getAttributeDef()));
            revisionAttribute.setStringValue(attr.getStringValue());
            revisionAttribute.setIntegerValue(attr.getIntegerValue());
            revisionAttribute.setBooleanValue(attr.getBooleanValue());
            revisionAttribute.setLongTextValue(attr.getLongTextValue());
            revisionAttribute.setRichTextValue(attr.getRichTextValue());
            revisionAttribute.setDateValue(attr.getDateValue());
            revisionAttribute.setDoubleValue(attr.getDoubleValue());
            revisionAttribute.setListValue(attr.getListValue());
            revisionAttribute.setTimeValue(attr.getTimeValue());
            revisionAttribute.setTimestampValue(attr.getTimestampValue());
            revisionAttribute.setImageValue(attr.getImageValue());
            revisionAttribute.setAttachmentValues(attr.getAttachmentValues());
            revisionAttribute.setRefValue(attr.getRefValue());
            revisionAttribute.setCurrencyValue(attr.getCurrencyValue());
            revisionAttribute.setMListValue(attr.getMListValue());
            revisionAttribute.setHyperLinkValue(attr.getHyperLinkValue());
            copyAttachments(oldRevision.getId(), revisionAttribute);
            revisionAttribute = itemRevisionAttributeRepository.save(revisionAttribute);
        }

    }

    private void copyRevisionAttributes(PLMItemRevision oldRevision, PLMItemRevision newRevision) {
        List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectId(oldRevision.getId());
        for (ObjectAttribute attr : objectAttributes) {
            ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(attr.getId().getAttributeDef());
            if (objectTypeAttribute.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMREVISION.toString()))) {
                ObjectAttribute objectAttribute = new ObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(newRevision.getId(), attr.getId().getAttributeDef()));
                objectAttribute.setStringValue(attr.getStringValue());
                objectAttribute.setIntegerValue(attr.getIntegerValue());
                objectAttribute.setBooleanValue(attr.getBooleanValue());
                objectAttribute.setLongTextValue(attr.getLongTextValue());
                objectAttribute.setRichTextValue(attr.getRichTextValue());
                objectAttribute.setDateValue(attr.getDateValue());
                objectAttribute.setDoubleValue(attr.getDoubleValue());
                objectAttribute.setListValue(attr.getListValue());
                objectAttribute.setTimeValue(attr.getTimeValue());
                objectAttribute.setTimestampValue(attr.getTimestampValue());
                objectAttribute.setImageValue(attr.getImageValue());
                objectAttribute.setAttachmentValues(attr.getAttachmentValues());
                objectAttribute.setRefValue(attr.getRefValue());
                objectAttribute.setCurrencyValue(attr.getCurrencyValue());
                objectAttribute.setMListValue(attr.getMListValue());
                objectAttribute.setHyperLinkValue(attr.getHyperLinkValue());
                copyAttachments(oldRevision.getId(), objectAttribute);
                objectAttribute = objectAttributeRepository.save(objectAttribute);
            }
        }

    }

    private void copyAttachments(Integer oldRevisionId, ObjectAttribute objectAttribute) {
        if (objectAttribute.getAttachmentValues().length > 0) {
            List<AttributeAttachment> attachments = attributeAttachmentService.getMultipleAttributeAttachments(Arrays.asList(objectAttribute.getAttachmentValues()));
            List<Integer> integers = new ArrayList<>();
            for (AttributeAttachment attachment : attachments) {
                AttributeAttachment attachment1 = new AttributeAttachment();
                attachment1.setObjectId(objectAttribute.getId().getObjectId());
                attachment1.setAttributeDef(attachment.getAttributeDef());
                attachment1.setExtension(attachment.getExtension());
                attachment1.setAddedBy(attachment.getAddedBy());
                attachment1.setObjectType(attachment.getObjectType());
                attachment1.setAddedOn(new Date());
                attachment1.setName(attachment.getName());
                attachment1.setSize(attachment.getSize());
                attachment1 = attributeAttachmentRepository.save(attachment1);
                integers.add(attachment1.getId());
                try {
                    String dir = "";
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + "attachments" + File.separator + objectAttribute.getId().getObjectId();
                    File folder = new File(dir);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    dir = dir + File.separator + attachment1.getId();
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    String oldFileDir = "";

                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + "attachments" + File.separator + oldRevisionId;
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    File infile = new File(oldFileDir);
                    File outfile = new File(dir);
                    instream = new FileInputStream(infile);
                    outstream = new FileOutputStream(outfile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = instream.read(buffer)) > 0) {
                        outstream.write(buffer, 0, length);
                    }
                    instream.close();
                    outstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            objectAttribute.setAttachmentValues(integers.stream().filter(Objects::nonNull).toArray(Integer[]::new));
        }
    }

    private void copyItemMasterAttributes(PLMItem oldItem, PLMItem newItem) {
        List<PLMItemAttribute> attrs = getItemAttributes(oldItem.getId());
        for (PLMItemAttribute attr : attrs) {
            PLMItemAttribute itemAttribute = new PLMItemAttribute();
            itemAttribute.setId(new ObjectAttributeId(newItem.getId(), attr.getId().getAttributeDef()));
            itemAttribute.setStringValue(attr.getStringValue());
            itemAttribute.setIntegerValue(attr.getIntegerValue());
            itemAttribute.setBooleanValue(attr.getBooleanValue());
            itemAttribute.setLongTextValue(attr.getLongTextValue());
            itemAttribute.setRichTextValue(attr.getRichTextValue());
            itemAttribute.setDateValue(attr.getDateValue());
            itemAttribute.setDoubleValue(attr.getDoubleValue());
            itemAttribute.setListValue(attr.getListValue());
            itemAttribute.setTimeValue(attr.getTimeValue());
            itemAttribute.setTimestampValue(attr.getTimestampValue());
            itemAttribute.setImageValue(attr.getImageValue());
            itemAttribute.setAttachmentValues(attr.getAttachmentValues());
            itemAttribute.setRefValue(attr.getRefValue());
            itemAttribute.setCurrencyValue(attr.getCurrencyValue());
            itemAttribute.setMListValue(attr.getMListValue());
            itemAttribute.setHyperLinkValue(attr.getHyperLinkValue());
            itemAttribute = itemAttributeRepository.save(itemAttribute);
            copyAttachments(oldItem.getId(), itemAttribute);
        }

    }

    @Transactional
    private void copyDesignObject(PLMItemRevision oldRevision, PLMItemRevision newRevision) {
        if (oldRevision.getDesignObject() != null) {
            Integer personId = sessionWrapper.getSession().getLogin().getId();
            Integer newDesignObject = null;
            PDMRevisionedObject revisionedObject = pdmRevisionedObjectRepository.findOne(oldRevision.getDesignObject());
            List<PDMRevisionedObject> revisionedObjects = pdmRevisionedObjectRepository.findByMasterId(revisionedObject.getMaster().getId());
            revisionedObjects.forEach(pdmRevisionedObject -> {
                pdmRevisionedObject.setLatestRevision(false);
                pdmRevisionedObject.setLatestVersion(false);
            });
            revisionedObjects = pdmRevisionedObjectRepository.save(revisionedObjects);
            if (revisionedObject.getObjectType().name().equals(PDMObjectType.PDM_ASSEMBLY.name())) {
                PDMAssembly copy = JsonUtils.cloneEntity(pdmAssemblyRepository.findOne(revisionedObject.getId()), PDMAssembly.class);
                copy.setId(null);
                copy.setRevision(newRevision.getRevision());
                copy.setVersion(1);
                copy.setLatestRevision(true);
                copy.setLatestVersion(true);
                copy.setItemObject(newRevision.getId());
                copy.setCreatedDate(new Date());
                copy.setModifiedDate(new Date());
                copy.setCreatedBy(personId);
                copy.setModifiedBy(personId);
                copy = pdmAssemblyRepository.save(copy);
                newDesignObject = copy.getId();

                PDMRevisionMaster revisionMaster = pdmRevisionMasterRepository.findOne(copy.getMaster().getId());
                revisionMaster.setLatestVersion(copy.getVersion());
                revisionMaster.setLatestRevision(copy.getRevision());
                revisionMaster = pdmRevisionMasterRepository.save(revisionMaster);

                if (copy.getDrawingRevision() != null) {
                    PDMDrawing newDrawing = reviseDrawingObject(copy.getDrawingRevision(), newRevision.getRevision());
                    copy.setDrawingRevision(newDrawing);
                    copy = pdmAssemblyRepository.save(copy);
                }

                PDMAssembly pdmAssembly = pdmAssemblyRepository.findOne(revisionedObject.getId());
                PDMAssemblyConfiguration config = pdmAssemblyConfigurationRepository.findOne(pdmAssembly.getDefaultConfiguration());

                PDMAssemblyConfiguration assemblyConfiguration = new PDMAssemblyConfiguration();
                assemblyConfiguration.setAssembly(copy.getId());
                assemblyConfiguration = pdmAssemblyConfigurationRepository.save(assemblyConfiguration);
                copy.setDefaultConfiguration(assemblyConfiguration.getId());
                copy = pdmAssemblyRepository.save(copy);

                List<PDMBOMOccurrence> occs = pdmBOMOccurrenceRepository.findByParent(config.getId());
                for (PDMBOMOccurrence occ : occs) {
                    if (occ instanceof PDMAssemblyBOMOccurrence) {
                        PDMAssemblyBOMOccurrence assemblyBOMOccurrence = ((PDMAssemblyBOMOccurrence) occ);
                        PDMAssemblyBOMOccurrence bomOccurrence = JsonUtils.cloneEntity(assemblyBOMOccurrence, PDMAssemblyBOMOccurrence.class);
                        bomOccurrence.setId(null);
                        bomOccurrence.setParent(assemblyConfiguration.getId());
                        bomOccurrence = pdmAssemblyBOMOccurrenceRepository.save(bomOccurrence);
                    } else if (occ instanceof PDMPartBOMOccurrence) {
                        PDMPartBOMOccurrence partBOMOccurrence = ((PDMPartBOMOccurrence) occ);
                        PDMPartBOMOccurrence bomOccurrence = JsonUtils.cloneEntity(partBOMOccurrence, PDMPartBOMOccurrence.class);
                        bomOccurrence.setId(null);
                        bomOccurrence.setParent(assemblyConfiguration.getId());
                        bomOccurrence = pdmPartBOMOccurrenceRepository.save(bomOccurrence);
                    }
                }

            } else if (revisionedObject.getObjectType().name().equals(PDMObjectType.PDM_PART.name())) {
                PDMPart copy = JsonUtils.cloneEntity(pdmPartRepository.findOne(revisionedObject.getId()), PDMPart.class);
                copy.setId(null);
                copy.setRevision(newRevision.getRevision());
                copy.setVersion(1);
                copy.setLatestRevision(true);
                copy.setLatestVersion(true);
                copy.setItemObject(newRevision.getId());
                copy.setCreatedDate(new Date());
                copy.setModifiedDate(new Date());
                copy.setCreatedBy(personId);
                copy.setModifiedBy(personId);
                copy = pdmPartRepository.save(copy);
                newDesignObject = copy.getId();
                PDMPart pdmPart = pdmPartRepository.findOne(revisionedObject.getId());
                PDMPartConfiguration config = pdmPartConfigurationRepository.findOne(pdmPart.getDefaultConfiguration());

                PDMPartConfiguration partConfiguration = new PDMPartConfiguration();
                partConfiguration.setPart(copy.getId());
                partConfiguration = pdmPartConfigurationRepository.save(partConfiguration);
                copy.setDefaultConfiguration(partConfiguration.getId());
                copy = pdmPartRepository.save(copy);

                PDMRevisionMaster revisionMaster = pdmRevisionMasterRepository.findOne(copy.getMaster().getId());
                revisionMaster.setLatestVersion(copy.getVersion());
                revisionMaster.setLatestRevision(copy.getRevision());
                revisionMaster = pdmRevisionMasterRepository.save(revisionMaster);

                if (copy.getDrawingRevision() != null) {
                    PDMDrawing newDrawing = reviseDrawingObject(copy.getDrawingRevision(), newRevision.getRevision());
                    copy.setDrawingRevision(newDrawing);
                    copy = pdmPartRepository.save(copy);
                }

                List<PDMBOMOccurrence> occs = pdmBOMOccurrenceRepository.findByParent(config.getId());
                for (PDMBOMOccurrence occ : occs) {
                    if (occ instanceof PDMAssemblyBOMOccurrence) {
                        PDMAssemblyBOMOccurrence assemblyBOMOccurrence = ((PDMAssemblyBOMOccurrence) occ);
                        PDMAssemblyBOMOccurrence bomOccurrence = JsonUtils.cloneEntity(assemblyBOMOccurrence, PDMAssemblyBOMOccurrence.class);
                        bomOccurrence.setId(null);
                        bomOccurrence.setParent(partConfiguration.getId());
                        bomOccurrence = pdmAssemblyBOMOccurrenceRepository.save(bomOccurrence);
                    } else if (occ instanceof PDMPartBOMOccurrence) {
                        PDMPartBOMOccurrence partBOMOccurrence = ((PDMPartBOMOccurrence) occ);
                        PDMPartBOMOccurrence bomOccurrence = JsonUtils.cloneEntity(partBOMOccurrence, PDMPartBOMOccurrence.class);
                        bomOccurrence.setId(null);
                        bomOccurrence.setParent(partConfiguration.getId());
                        bomOccurrence = pdmPartBOMOccurrenceRepository.save(bomOccurrence);
                    }
                }
            } else if (revisionedObject.getObjectType().name().equals(PDMObjectType.PDM_DRAWING.name())) {
                PDMDrawing copy = JsonUtils.cloneEntity(pdmDrawingRepository.findOne(revisionedObject.getId()), PDMDrawing.class);
                copy.setId(null);
                copy.setRevision(newRevision.getRevision());
                copy.setVersion(1);
                copy.setLatestRevision(true);
                copy.setLatestVersion(true);
                copy.setItemObject(newRevision.getId());
                copy.setCreatedDate(new Date());
                copy.setModifiedDate(new Date());
                copy.setCreatedBy(personId);
                copy.setModifiedBy(personId);
                copy = pdmDrawingRepository.save(copy);

                PDMRevisionMaster revisionMaster = pdmRevisionMasterRepository.findOne(copy.getMaster().getId());
                revisionMaster.setLatestVersion(copy.getVersion());
                revisionMaster.setLatestRevision(copy.getRevision());
                revisionMaster = pdmRevisionMasterRepository.save(revisionMaster);

                newDesignObject = copy.getId();
            }

            newRevision.setDesignObject(newDesignObject);
            newRevision = itemRevisionRepository.save(newRevision);
            PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findByAttachedTo(oldRevision.getDesignObject());
            PDMFile oldFile = pdmFileRepository.findOne(pdmFileVersion.getFile().getId());
            oldFile.setVersions(oldFile.getVersions() + 1);
            oldFile = pdmFileRepository.save(oldFile);
            pdmFileVersion.setFile(null);

            PDMFileVersion fileVersion = JsonUtils.cloneEntity(pdmFileVersion, PDMFileVersion.class);
            pdmFileVersion.setFile(oldFile);
            fileVersion.setId(null);
            fileVersion.setFile(oldFile);
            fileVersion.setLatest(true);
            fileVersion.setVersion(1);
            fileVersion.setAttachedTo(newDesignObject);
            fileVersion = pdmFileVersionRepository.save(fileVersion);
            fileVersion.setIdPath(oldFile.getFolder().toString() + "/" + fileVersion.getId().toString());
            fileVersion = pdmFileVersionRepository.save(fileVersion);

            pdmFileVersion.setLatest(false);
            pdmFileVersion = pdmFileVersionRepository.save(pdmFileVersion);
            PDMObjectThumbnail objectThumbnail = pdmThumbnailRepository.findOne(pdmFileVersion.getId());
            if (objectThumbnail != null) {
                PDMObjectThumbnail thumbnail = new PDMObjectThumbnail();
                thumbnail.setId(fileVersion.getId());
                thumbnail.setThumbnail(objectThumbnail.getThumbnail());
                pdmThumbnailRepository.save(thumbnail);
            }

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + oldFile.getFolder() + File.separator + fileVersion.getId();

            String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + oldFile.getFolder() + File.separator + pdmFileVersion.getId();

            try {
                if (!dir.equals("")) {
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }

                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    private PDMDrawing reviseDrawingObject(PDMDrawing drawing, String revision) {
        List<PDMRevisionedObject> revisionedObjects = pdmRevisionedObjectRepository.findByMasterId(drawing.getMaster().getId());
        revisionedObjects.forEach(pdmRevisionedObject -> {
            pdmRevisionedObject.setLatestRevision(false);
            pdmRevisionedObject.setLatestVersion(false);
        });
        revisionedObjects = pdmRevisionedObjectRepository.save(revisionedObjects);
        PDMDrawing copy = JsonUtils.cloneEntity(pdmDrawingRepository.findOne(drawing.getId()), PDMDrawing.class);
        copy.setId(null);
        copy.setRevision(revision);
        copy.setVersion(1);
        copy.setLatestRevision(true);
        copy.setLatestVersion(true);
        copy.setCreatedDate(new Date());
        copy.setModifiedDate(new Date());
        copy = pdmDrawingRepository.save(copy);

        PDMRevisionMaster revisionMaster = pdmRevisionMasterRepository.findOne(copy.getMaster().getId());
        revisionMaster.setLatestVersion(copy.getVersion());
        revisionMaster.setLatestRevision(copy.getRevision());
        revisionMaster = pdmRevisionMasterRepository.save(revisionMaster);

        if (drawing.getPdfFile() != null) {
            Attachment attachment = attachmentRepository.findOne(drawing.getPdfFile());
            Attachment copyAttachment = new Attachment();
            copyAttachment.setName(attachment.getName());
            copyAttachment.setObjectType(attachment.getObjectType());
            copyAttachment.setSize(attachment.getSize());
            copyAttachment.setExtension(attachment.getExtension());
            copyAttachment.setObjectId(copy.getId());
            copyAttachment.setAddedOn(new Date());
            copyAttachment.setAddedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
            copyAttachment = attachmentRepository.save(copyAttachment);
            copy.setPdfFile(copyAttachment.getId());
            copy = pdmDrawingRepository.save(copy);
            String path = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + "attachments" + File.separator + drawing.getId() + File.separator + drawing.getPdfFile();

            String newPath = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + "attachments" + File.separator + copy.getId();

            try {
                if (!newPath.equals("")) {
                    File fDir = new File(newPath);
                    if (!fDir.exists()) {
                        fDir.mkdir();
                    }

                    newPath = newPath + File.separator + copyAttachment.getId();
                    fDir = new File(newPath);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;

                    Utils.copyDataUsingStream(path, instream, outstream, newPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findByAttachedTo(drawing.getId());
        PDMFile oldFile = pdmFileRepository.findOne(pdmFileVersion.getFile().getId());
        oldFile.setVersions(oldFile.getVersions() + 1);
        oldFile = pdmFileRepository.save(oldFile);
        pdmFileVersion.setFile(null);

        PDMFileVersion fileVersion = JsonUtils.cloneEntity(pdmFileVersion, PDMFileVersion.class);
        pdmFileVersion.setFile(oldFile);
        fileVersion.setId(null);
        fileVersion.setFile(oldFile);
        fileVersion.setLatest(true);
        fileVersion.setVersion(1);
        fileVersion.setAttachedTo(copy.getId());
        fileVersion = pdmFileVersionRepository.save(fileVersion);
        fileVersion.setIdPath(oldFile.getFolder().toString() + "/" + fileVersion.getId().toString());
        fileVersion = pdmFileVersionRepository.save(fileVersion);

        pdmFileVersion.setLatest(false);
        pdmFileVersion = pdmFileVersionRepository.save(pdmFileVersion);
        PDMObjectThumbnail objectThumbnail = pdmThumbnailRepository.findOne(pdmFileVersion.getId());
        if (objectThumbnail != null) {
            PDMObjectThumbnail thumbnail = new PDMObjectThumbnail();
            thumbnail.setId(fileVersion.getId());
            thumbnail.setThumbnail(objectThumbnail.getThumbnail());
            pdmThumbnailRepository.save(thumbnail);
        }

        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + oldFile.getFolder() + File.separator + fileVersion.getId();

        String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + oldFile.getFolder() + File.separator + pdmFileVersion.getId();

        try {
            if (!dir.equals("")) {
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.createNewFile();
                }

                FileInputStream instream = null;
                FileOutputStream outstream = null;

                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copy;
    }

    private void copyItems(PLMItemRevision oldItem, PLMItemRevision newItem) {
        List<PLMBom> plmBoms = bomRepository.findByParentOrderByCreatedDateAsc(oldItem);
        List<PLMBom> copyBoms = new ArrayList<>();
        bomConfigurationService.copyBomItems(plmBoms, newItem, copyBoms);
        if (copyBoms.size() > 0) {
            newItem.setHasBom(true);
            bomRepository.save(copyBoms);
        }
    }

    private void copyItemInstance(PLMItemRevision oldItem, PLMItemRevision newItem) {
        PLMItem item = itemRepository.findOne(oldItem.getItemMaster());
        List<PLMItem> items = itemRepository.findByInstanceOrderByCreatedDateDesc(item.getId());
        PLMItemType plmItemType = itemTypeRepository.findOne(item.getItemType().getId());
        PLMLifeCyclePhase lifeCyclePhase = plmItemType.getLifecycle().getPhaseByType(LifeCyclePhaseType.PRELIMINARY);
        for (PLMItem item1 : items) {
            PLMItemRevision revision = new PLMItemRevision();
            revision.setItemMaster(item1.getId());
            revision.setRevision(newItem.getRevision());
            revision.setLifeCyclePhase(lifeCyclePhase);
            revision.setInstance(newItem.getId());
            revision.setHasBom(false);
            revision.setHasFiles(false);
            revision = itemRevisionRepository.save(revision);
            PLMItemRevision oldRevision = itemRevisionRepository.findOne(item1.getLatestRevision());
            copyItems(oldRevision, revision);
            copyBomConfiguration(oldItem, newItem, oldRevision, revision);
            /*copyManufactureParts(oldRevision, revision);
            copyFolderStructure(oldRevision, revision);*/

            PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
            statusHistory.setItem(revision.getId());
            statusHistory.setOldStatus(revision.getLifeCyclePhase());
            statusHistory.setNewStatus(revision.getLifeCyclePhase());
            statusHistory.setUpdatedBy(item.getCreatedBy());
            statusHistory = revisionStatusHistoryRepository.save(statusHistory);

            item1.setLatestRevision(revision.getId());
            item1 = itemRepository.save(item1);

        }

    }

    private void copyBomConfiguration(PLMItemRevision oldItem, PLMItemRevision newItem, PLMItemRevision oldRevision, PLMItemRevision newRevision) {

        if (oldRevision.getBomConfiguration() != null) {
            BOMConfiguration bomConfiguration = bomConfigurationRepository.findOne(oldRevision.getBomConfiguration());
            if (bomConfiguration != null) {
                BOMConfiguration configuration = new BOMConfiguration();
                configuration.setName(bomConfiguration.getName());
                configuration.setDescription(bomConfiguration.getDescription());
                configuration.setItem(newItem.getId());
                String json = bomConfiguration.getRules();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = null;
                try {
                    map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                    });

                    Set<Map.Entry<String, Object>> entries1 = map.entrySet();

                    for (Map.Entry<String, Object> entry : entries1) {
                        if (entry.getKey().equals("id")) {
                            entry.setValue(newItem.getId());
                        }
                    }

                    if (map != null) {
                        String value = new ObjectMapper().writeValueAsString(map);
                        configuration.setRules(value);
                    }
                } catch (Exception e) {
                    new CassiniException(e.getMessage());
                }

                configuration = bomConfigurationRepository.save(configuration);

                newRevision.setBomConfiguration(configuration.getId());
                newRevision = itemRevisionRepository.save(newRevision);
            }

        }

    }

    private void copyFiles(PLMItemRevision oldItem, PLMItemRevision newItem) {
        List<PLMItemFile> itemFiles = itemFileService.findAllByItem(oldItem);
        for (PLMItemFile itemFile : itemFiles) {
            itemFileService.copyItemFile(oldItem, newItem, itemFile);
        }
    }

    public void copyFolderStructure(PLMItemRevision oldItem, PLMItemRevision newItem) {
        List<PLMItemFile> itemFiles = itemFileRepository.findByItemAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(oldItem);
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObject(oldItem.getId());
        List<PLMObjectDocument> documents = new ArrayList<>();
        for (PLMObjectDocument plmObjectDocument : objectDocuments) {
            PLMObjectDocument objectDocument = new PLMObjectDocument();
            objectDocument.setDocument(plmObjectDocument.getDocument());
            objectDocument.setObject(newItem.getId());
            objectDocument.setFolder(plmObjectDocument.getFolder());
            documents.add(objectDocument);
        }
        if (documents.size() > 0) {
            objectDocumentRepository.save(documents);
            newItem.setHasFiles(true);
            newItem = itemRevisionRepository.save(newItem);
        }
        for (PLMItemFile itemFile : itemFiles) {
            itemFileService.copyItemFile(oldItem, newItem, itemFile);
        }
    }

    public String getNextRevisionSequence(PLMItem item) {
        String nextRev = null;
        List<String> revs = getRevisions(item);
        String lastRev = revs.get(revs.size() - 1);
        Lov lov = item.getItemType().getRevisionSequence();
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

    private List<String> getRevisions(PLMItem item) {
        List<String> revs = new ArrayList<>();
        List<PLMItem> items = findByItemNumber(item.getItemNumber());
        PLMItemRevision revisions = itemRevisionRepository.findOne(item.getLatestRevision());
        /*revisions.forEach(it -> {
            String rev = it.getRevision();
            if (!revs.contains(rev)) {
                revs.add(rev);
            }
        });*/
        String rev = revisions.getRevision();
        if (!revs.contains(rev)) {
            revs.add(rev);
        }
        Collections.sort(revs);
        return revs;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItem> getItemByItemType(Integer itemTypeId) {
        PLMItemType itemType = itemTypeRepository.findOne(itemTypeId);
        List<PLMItem> items = itemRepository.findByItemType(itemType);
        return items;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItem> getNormalAndConfigurableItemByItemType(Integer itemTypeId) {
        PLMItemType itemType = itemTypeRepository.findOne(itemTypeId);
        List<PLMItem> items = itemRepository.getNormalAndConfigurableItemByItemType(itemType.getId());
        return items;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMItem> getItemsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PLMItemType type = itemTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return itemRepository.getByItemTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PLMItemType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PLMItemType> children = itemTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (PLMItemType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItem> findMultiple(List<Integer> ids) {
        List<PLMItem> items = itemRepository.findByIdIn(ids);
        for (PLMItem item : items) {
            item = setItemCounts(item);
        }
        return items;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItem> getPageableItemsByIds(List<Integer> ids, Pageable pageable) {
        return itemRepository.findByIdIn(ids, pageable);
    }

    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByItemIdsAndAttributeIds(Integer[] plmItems, Integer[] objectAttributeIds) {
        Map<Integer, List<ObjectAttribute>> objectAttributesMap = new HashMap<>();
        for (Integer item : plmItems) {
            List<ObjectAttribute> attributes = objectAttributeRepository.findByObjectIdAndAttributeDefIdsIn(item, objectAttributeIds);
            List<Integer> attributeDefIds = new ArrayList();
            for (ObjectAttribute attribute : attributes) {
                attributeDefIds.add(attribute.getId().getAttributeDef());
            }
            List<ObjectTypeAttribute> plmItemTypeAttributes = objectTypeAttributeRepository.findByIdIn(attributeDefIds);
            Map<Integer, ObjectTypeAttribute> objectTypeAttributeMap = plmItemTypeAttributes.stream().collect(Collectors.toMap(ObjectTypeAttribute::getId, typeAttribute -> typeAttribute));

            Map<String, Double> stringDoubleMap = new HashMap();

            for (ObjectAttribute objectAttribute : attributes) {
                ObjectTypeAttribute typeAttribute4 = objectTypeAttributeMap.get(objectAttribute.getId().getAttributeDef());
                if (typeAttribute4 != null) {
                    if (typeAttribute4.getDataType().toString().equals("INTEGER")) {
                        stringDoubleMap.put(typeAttribute4.getName(), objectAttribute.getIntegerValue().doubleValue());
                    } else if (typeAttribute4.getDataType().toString().equals("DOUBLE")) {
                        stringDoubleMap.put(typeAttribute4.getName(), objectAttribute.getDoubleValue());
                    }
                }
            }

            for (ObjectAttribute objectAttribute : attributes) {
                ObjectTypeAttribute typeAttribute4 = objectTypeAttributeMap.get(objectAttribute.getId().getAttributeDef());
                if (typeAttribute4 != null && typeAttribute4.getDataType().toString().equals("FORMULA")) {
                    if (stringDoubleMap.size() > 0) {
                        try {
                            objectAttribute.setFormulaValue(parser.parseExpression(typeAttribute4.getFormula()).getValue(stringDoubleMap, String.class));
                        } catch (Exception e) {
                            objectAttribute.setFormulaValue(null);
                        }
                    }
                }
            }

            for (ObjectAttribute attribute : attributes) {
                Integer id = attribute.getId().getObjectId();
                List<ObjectAttribute> objectAttributes = objectAttributesMap.get(id);
                if (objectAttributes == null) {
                    objectAttributes = new ArrayList<>();
                    objectAttributesMap.put(id, objectAttributes);
                }
                objectAttributes.add(attribute);
            }
        }

        return objectAttributesMap;

    }

    public List<PLMItem> getItemRevisionByIds(List<Integer> revisionIds) {
        return itemRepository.findByLatestRevisionIn(revisionIds);
    }

    @Transactional(readOnly = true)
    public List<PLMItemAttribute> getItemAttributes(Integer itemId) {
        return itemAttributeRepository.findByItemId(itemId);
    }

    @Transactional(readOnly = true)
    public List<PLMItemRevisionAttribute> getItemRevisionAttributes(Integer revisionId) {
        return itemRevisionAttributeRepository.findByItemId(revisionId);
    }

    @Transactional(readOnly = true)
    public List<String> getUniqueRevisions() {
        return itemRevisionRepository.getUniqueRevisions();
    }

    @Transactional
    public PLMItemAttribute createItemAttribute(PLMItemAttribute attribute) {
        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (itemTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(itemTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(itemTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                attribute.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactor());
            } else {
                attribute.setDoubleValue(attribute.getDoubleValue());
            }
        } else {
            attribute.setDoubleValue(attribute.getDoubleValue());
        }

        attribute = itemAttributeRepository.save(attribute);
        PLMItem plmItem = itemRepository.findOne(attribute.getId().getObjectId());
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());

        applicationEventPublisher.publishEvent(new ItemEvents.ItemAttributesUpdatedEvent(plmItem, plmItemRevision, null, attribute));
        return attribute;
    }

    @Transactional
    public PLMItemRevisionAttribute createItemRevisionAttribute(PLMItemRevisionAttribute attribute) {
        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (itemTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(itemTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(itemTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                attribute.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactor());
            } else {
                attribute.setDoubleValue(attribute.getDoubleValue());
            }
        } else {
            attribute.setDoubleValue(attribute.getDoubleValue());
        }
        attribute = itemRevisionAttributeRepository.save(attribute);
        return attribute;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PLMItemAttribute updateItemAttribute(PLMItemAttribute attribute) {
        PLMItemAttribute oldValue = itemAttributeRepository.findByItemAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PLMItemAttribute.class);

        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (itemTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(itemTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(itemTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                attribute.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactor());
            } else {
                attribute.setDoubleValue(attribute.getDoubleValue());
            }
        } else {
            attribute.setDoubleValue(attribute.getDoubleValue());
        }
        attribute = itemAttributeRepository.save(attribute);

        PLMItem plmItem = itemRepository.findOne(attribute.getId().getObjectId());
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());

        /* App events */
        applicationEventPublisher.publishEvent(new ItemEvents.ItemAttributesUpdatedEvent(plmItem, plmItemRevision, oldValue, attribute));
        return attribute;
    }

    @Transactional
    public PLMItemRevisionAttribute updateItemRevisionAttribute(PLMItemRevisionAttribute attribute) {
        PLMItemRevisionAttribute oldValue = itemRevisionAttributeRepository.findByItemRevisionAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PLMItemRevisionAttribute.class);

        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (itemTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(itemTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(itemTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                attribute.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactor());
            } else {
                attribute.setDoubleValue(attribute.getDoubleValue());
            }
        } else {
            attribute.setDoubleValue(attribute.getDoubleValue());
        }

        attribute = itemRevisionAttributeRepository.save(attribute);

        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(attribute.getId().getObjectId());
        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());

        /* App events */
        applicationEventPublisher.publishEvent(new ItemEvents.ItemRevisionAttributesUpdatedEvent(plmItem, plmItemRevision, oldValue, attribute));

        return attribute;
    }

    @Transactional
    public ObjectAttribute createAttribute(PLMItemAttribute attribute) {
        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
        if (itemTypeAttribute.getRevisionSpecific().equals(true)) {
            PLMItem plmItem = itemRepository.findOne(attribute.getId().getObjectId());
            PLMItemRevisionAttribute revisionAttribute1 = new PLMItemRevisionAttribute();
            revisionAttribute1.setId(new ObjectAttributeId(plmItem.getLatestRevision(), itemTypeAttribute.getId()));
            revisionAttribute1.setImageValue(attribute.getImageValue());
            PLMItemRevisionAttribute revisionAttribute2 = itemRevisionAttributeRepository.save(revisionAttribute1);
            return revisionAttribute2;
        } else if (itemTypeAttribute.getRevisionSpecific().equals(false)) {
            PLMItemAttribute itemAttribute = new PLMItemAttribute();
            PLMItem plmItem1 = itemRepository.findOne(attribute.getId().getObjectId());
            itemAttribute.setId(new ObjectAttributeId(plmItem1.getId(), attribute.getId().getAttributeDef()));
            itemAttribute.setImageValue(attribute.getImageValue());
            PLMItemAttribute itemAttribute1 = itemAttributeRepository.save(itemAttribute);
            return itemAttribute1;
        }
        return null;
    }

    @Transactional
    public PLMItem uploadThumbnailImage(PLMItem item) {
        return itemRepository.save(item);
    }

    @Transactional
    public List<ObjectAttribute> saveItemAttributes(List<PLMItemAttribute> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList<>();
        for (PLMItemAttribute attribute : attributes) {
            PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null && attribute.getCurrencyValue() != null)
                    || attribute.getDateValue() != null || (attribute.getDoubleValue() != null && attribute.getMeasurementUnit() == null) || attribute.getHyperLinkValue() != null || attribute.getStringValue() != null
                    || attribute.getRichTextValue() != null || attribute.getLongTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()
                    || itemTypeAttribute.getDataType().toString().equals("FORMULA") || (attribute.getDoubleValue() != null && attribute.getMeasurementUnit() != null)) {

                if (itemTypeAttribute.getRevisionSpecific().equals(true)) {
                    PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(attribute.getId().getObjectId());
                    PLMItemRevisionAttribute revisionAttribute1 = new PLMItemRevisionAttribute();
                    revisionAttribute1.setId(new ObjectAttributeId(plmItemRevision.getId(), itemTypeAttribute.getId()));
                    revisionAttribute1.setStringValue(attribute.getStringValue());
                    revisionAttribute1.setLongTextValue(attribute.getLongTextValue());
                    revisionAttribute1.setRichTextValue(attribute.getRichTextValue());
                    revisionAttribute1.setIntegerValue(attribute.getIntegerValue());
                    revisionAttribute1.setBooleanValue(attribute.getBooleanValue());
                    if (itemTypeAttribute.getMeasurement() != null) {
                        List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(itemTypeAttribute.getMeasurement().getId());
                        MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
                        MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(itemTypeAttribute.getMeasurement().getId());

                        Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
                        Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

                        if (!attributeUnitIndex.equals(baseUnitIndex)) {
                            revisionAttribute1.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactor());
                        } else {
                            revisionAttribute1.setDoubleValue(attribute.getDoubleValue());
                        }
                        revisionAttribute1.setMeasurementUnit(attribute.getMeasurementUnit());
                    } else {
                        revisionAttribute1.setDoubleValue(attribute.getDoubleValue());
                    }
                    revisionAttribute1.setDateValue(attribute.getDateValue());
                    revisionAttribute1.setTimeValue(attribute.getTimeValue());
                    revisionAttribute1.setAttachmentValues(attribute.getAttachmentValues());
                    revisionAttribute1.setRefValue(attribute.getRefValue());
                    revisionAttribute1.setCurrencyType(attribute.getCurrencyType());
                    revisionAttribute1.setCurrencyValue(attribute.getCurrencyValue());
                    revisionAttribute1.setTimestampValue(attribute.getTimestampValue());
                    revisionAttribute1.setHyperLinkValue(attribute.getHyperLinkValue());
//                    revisionAttribute1.setImageValue(null);
                    revisionAttribute1.setListValue(attribute.getListValue());
                    revisionAttribute1.setMListValue(attribute.getMListValue());
                    PLMItemRevisionAttribute revisionAttribute2 = itemRevisionAttributeRepository.save(revisionAttribute1);
                    objectAttributes.add(revisionAttribute2);
                } else if (itemTypeAttribute.getRevisionSpecific().equals(false)) {
                    PLMItemAttribute itemAttribute = new PLMItemAttribute();
                    PLMItem plmItem1 = itemRepository.findOne(attribute.getId().getObjectId());
                    itemAttribute.setId(new ObjectAttributeId(plmItem1.getId(), attribute.getId().getAttributeDef()));
                    itemAttribute.setStringValue(attribute.getStringValue());
                    itemAttribute.setIntegerValue(attribute.getIntegerValue());
                    itemAttribute.setLongTextValue(attribute.getLongTextValue());
                    itemAttribute.setRichTextValue(attribute.getRichTextValue());
                    itemAttribute.setBooleanValue(attribute.getBooleanValue());
                    if (itemTypeAttribute.getMeasurement() != null) {
                        List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(itemTypeAttribute.getMeasurement().getId());
                        MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
                        MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(itemTypeAttribute.getMeasurement().getId());

                        Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
                        Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

                        if (!attributeUnitIndex.equals(baseUnitIndex)) {
                            itemAttribute.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactor());
                        } else {
                            itemAttribute.setDoubleValue(attribute.getDoubleValue());
                        }
                        itemAttribute.setMeasurementUnit(attribute.getMeasurementUnit());
                    } else {
                        itemAttribute.setDoubleValue(attribute.getDoubleValue());
                    }

                    itemAttribute.setTimeValue(attribute.getTimeValue());
                    itemAttribute.setTimestampValue(attribute.getTimestampValue());
                    itemAttribute.setAttachmentValues(attribute.getAttachmentValues());
//                    itemAttribute.setImageValue(null);
                    itemAttribute.setDateValue(attribute.getDateValue());
                    itemAttribute.setCurrencyType(attribute.getCurrencyType());
                    itemAttribute.setCurrencyValue(attribute.getCurrencyValue());
                    itemAttribute.setRefValue(attribute.getRefValue());
                    itemAttribute.setListValue(attribute.getListValue());
                    itemAttribute.setMListValue(attribute.getMListValue());
                    itemAttribute.setHyperLinkValue(attribute.getHyperLinkValue());
                    PLMItemAttribute itemAttribute1 = itemAttributeRepository.save(itemAttribute);
                    objectAttributes.add(itemAttribute1);
                }
            }

        }
        return objectAttributes;

    }

    @Transactional
    public ObjectAttribute saveItemAttribute(PLMItemAttribute attribute) {
        if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null ||
                attribute.getStringValue() != null || attribute.getRichTextValue() != null || attribute.getLongTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
            PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
            if (itemTypeAttribute.getRevisionSpecific().equals(true)) {
                PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(attribute.getId().getObjectId());
                PLMItemRevisionAttribute revisionAttribute1 = new PLMItemRevisionAttribute();
                revisionAttribute1.setId(new ObjectAttributeId(plmItemRevision.getId(), itemTypeAttribute.getId()));
                revisionAttribute1.setStringValue(attribute.getStringValue());
                revisionAttribute1.setLongTextValue(attribute.getLongTextValue());
                revisionAttribute1.setRichTextValue(attribute.getRichTextValue());
                revisionAttribute1.setIntegerValue(attribute.getIntegerValue());
                revisionAttribute1.setBooleanValue(attribute.getBooleanValue());
                revisionAttribute1.setDoubleValue((attribute.getDoubleValue()));
                revisionAttribute1.setDateValue(attribute.getDateValue());
                revisionAttribute1.setTimeValue(attribute.getTimeValue());
                revisionAttribute1.setAttachmentValues(attribute.getAttachmentValues());
                revisionAttribute1.setRefValue(attribute.getRefValue());
                revisionAttribute1.setCurrencyType(attribute.getCurrencyType());
                revisionAttribute1.setCurrencyValue(attribute.getCurrencyValue());
                revisionAttribute1.setTimestampValue(attribute.getTimestampValue());
//                    revisionAttribute1.setImageValue(null);
                revisionAttribute1.setListValue(attribute.getListValue());
                revisionAttribute1.setMListValue(attribute.getMListValue());
                PLMItemRevisionAttribute revisionAttribute2 = itemRevisionAttributeRepository.save(revisionAttribute1);
            } else if (itemTypeAttribute.getRevisionSpecific().equals(false)) {
                PLMItemAttribute itemAttribute = new PLMItemAttribute();
                PLMItem plmItem1 = itemRepository.findOne(attribute.getId().getObjectId());
                itemAttribute.setId(new ObjectAttributeId(plmItem1.getId(), attribute.getId().getAttributeDef()));
                itemAttribute.setStringValue(attribute.getStringValue());
                itemAttribute.setIntegerValue(attribute.getIntegerValue());
                itemAttribute.setLongTextValue(attribute.getLongTextValue());
                itemAttribute.setRichTextValue(attribute.getRichTextValue());
                itemAttribute.setBooleanValue(attribute.getBooleanValue());
                itemAttribute.setDoubleValue(attribute.getDoubleValue());
                itemAttribute.setTimeValue(attribute.getTimeValue());
                itemAttribute.setTimestampValue(attribute.getTimestampValue());
                itemAttribute.setAttachmentValues(attribute.getAttachmentValues());
//                    itemAttribute.setImageValue(null);
                itemAttribute.setDateValue(attribute.getDateValue());
                itemAttribute.setCurrencyType(attribute.getCurrencyType());
                itemAttribute.setCurrencyValue(attribute.getCurrencyValue());
                itemAttribute.setRefValue(attribute.getRefValue());
                itemAttribute.setListValue(attribute.getListValue());
                itemAttribute.setMListValue(attribute.getMListValue());
                PLMItemAttribute itemAttribute1 = itemAttributeRepository.save(itemAttribute);
            }
        }
        return attribute;

    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItem> findByItemNumber(String itemNumber) {
        itemNumber = itemNumber.toUpperCase();
        return itemRepository.findByItemNumber(itemNumber);
    }

    @Transactional(readOnly = true)
    public List<PLMItemReference> getItemReferences(Integer itemId) {
        List<PLMItemReference> refs = itemReferenceRepository.findByParent(itemId);
        return refs;
    }

    @Transactional(readOnly = true)
    public PLMItemReference getItemReference(Integer refId) {
        return itemReferenceRepository.findOne(refId);
    }

    @Transactional
    public PLMItemReference createItemReference(PLMItemReference itemReference) {
        itemReferenceRepository.save(itemReference);
        return itemReference;
    }

    @Transactional
    public PLMItemReference updateItemReference(PLMItemReference itemReference) {
        itemReference = itemReferenceRepository.save(itemReference);
        return itemReference;
    }

    @Transactional
    public void deleteItemReference(Integer itemReferenceId) {
        PLMItemReference itemReference = itemReferenceRepository.findOne(itemReferenceId);
        itemReferenceRepository.delete(itemReference);

    }

    @Transactional(readOnly = true)
    public List<PLMItemManufacturerPart> getManufacturerParts(Integer itemId) {
        return itemManufacturerPartRepository.findByItem(itemId);
    }

    @Transactional(readOnly = true)
    public List<PLMItemManufacturerPart> getPartsByMfrPart(Integer itemId) {
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(itemId);
        return itemManufacturerPartRepository.findByManufacturerPart(manufacturerPart);
    }

    @Transactional(readOnly = true)
    public PLMItemManufacturerPart getManufacturerPartById(Integer mpnId) {
        return itemManufacturerPartRepository.findOne(mpnId);
    }

    @Transactional
    public PLMItemManufacturerPart createManufacturerPart(Integer itemId, PLMItemManufacturerPart itemManufacturerPart) {
        itemManufacturerPart.setId(itemId);
        PLMItemManufacturerPart itemMfrPart = itemManufacturerPartRepository.save(itemManufacturerPart);
        return itemManufacturerPart;
    }

    @Transactional
    public PLMItemManufacturerPart updateManufacturerPart(PLMItemManufacturerPart itemManufacturerPart) {
        itemManufacturerPart = itemManufacturerPartRepository.save(itemManufacturerPart);
        return itemManufacturerPart;
    }

    @Transactional
    public void deleteManufacturerPart(Integer itemId) {
        checkNotNull(itemId);
        PLMItemManufacturerPart manufacturerPart = itemManufacturerPartRepository.findOne(itemId);
        if (manufacturerPart == null) {
            throw new ResourceNotFoundException();
        }
        itemManufacturerPartRepository.delete(itemId);
    }

    private PLMItem setItemCounts(PLMItem item) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
        List<PLMItemFile> itemFiles = itemFileService.findByItem(itemRevision);
        List<PLMSubscribe> subscribes = subscribeRepository.findByObjectIdAndSubscribeTrue(item.getId());
        Integer itemManufacturerParts = itemManufacturerPartRepository.getMfrPartsCountByItem(itemRevision.getId());
        if (itemManufacturerParts > 0) {
            item.setHasMfrParts(true);
        }
        item.setItemFiles(itemFiles);
        item.setSubscribes(subscribes);

        if (item.getItemType().getItemClass() != null && item.getItemType().getItemClass().equals(ItemClass.DOCUMENT)) {
            List<PLMDCO> dco = dcoService.findByAffectedItem(itemRevision.getId());
            List<PLMDCR> dcr = dcrService.findByAffectedItem(itemRevision.getId());
            if (dco.size() > 0 || dcr.size() > 0) {
                item.setHasChanges(true);
            }

        }

        if (item.getItemType().getItemClass() != null && !item.getItemType().getItemClass().equals(ItemClass.DOCUMENT)) {
            List<PLMECO> eco = ecoService.findByAffectedItem(itemRevision.getId());
            List<PLMECR> ecr = ecrService.findByAffectedItem(itemRevision.getId());
            if (eco.size() > 0 || ecr.size() > 0) {
                item.setHasChanges(true);
            }
        }

        Integer pqmProblemReportsCount = prProblemItemRepository.getProblemReportCountByItem(itemRevision.getId());
        Integer qcrCount = qcrProblemItemRepository.getQCRCountByItem(itemRevision.getId());
        if (pqmProblemReportsCount > 0 || qcrCount > 0) {
            item.setHasQuality(true);
        }
        List<Integer> revisionIds = itemRevisionRepository.getRevisionIdsByItemId(item.getId());
        Integer variances = varianceAffectedItemRepository.getVarianceCountByItem(revisionIds);
        if (variances > 0) {
            item.setHasVariance(true);
        }

        return item;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMItem> searchItems(Predicate predicate, Pageable pageable) {
        Page<PLMItem> plmItems = itemRepository.findAll(predicate, pageable);
        for (PLMItem item : plmItems.getContent()) {
            item = setItemCounts(item);
        }
        return plmItems;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMItem> searchItemsAll(Predicate predicate) {
        List<PLMItem> plmItems = IteratorUtils.toList(itemRepository.findAll(predicate).iterator());
        for (PLMItem item : plmItems) {
            item = setItemCounts(item);
        }
        return plmItems;
    }

    @Transactional(readOnly = true)
//    @PostFilter("hasPermission(filterObject,'view')")
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<ItemsDto> advancedSearchItem(ParameterCriteria[] parameterCriterias, Pageable pageable) {
        TypedQuery<PLMItem> typedQuery = itemAdvancedCriteria.getItemTypeQuery(parameterCriterias);
        List<PLMItem> resultList = typedQuery.getResultList();
        List<ItemsDto> dtoList = new LinkedList<>();
        resultList.forEach(item -> {
            ItemsDto itemDto = convertToItemsDto(item);
            dtoList.add(itemDto);
        });
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > dtoList.size() ? dtoList.size() : (start + pageable.getPageSize());
        Page<ItemsDto> plmItemPage = new PageImpl<ItemsDto>(dtoList.subList(start, end), pageable, dtoList.size());
        return plmItemPage;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> advancedSearchItemAll(ParameterCriteria[] parameterCriterias) {
        TypedQuery<PLMItem> typedQuery = itemAdvancedCriteria.getItemTypeQuery(parameterCriterias);
        List<PLMItem> resultlist1 = typedQuery.getResultList();
        for (PLMItem item : resultlist1) {
            item = setItemCounts(item);
        }
        /*int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > resultlist1.size() ? resultlist1.size() : (start + pageable.getPageSize());
        Page<PLMItem> plmItemPage = new PageImpl<PLMItem>(resultlist1.subList(start, end), pageable, resultlist1.size());*/
        return resultlist1;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMItem> freeTextSearch(Pageable pageable, ItemCriteria criteria) {
        Predicate predicate = predicateBuilder.build(criteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItems = itemRepository.findAll(predicate, pageable);
        for (PLMItem item : plmItems.getContent()) {
            item = setItemCounts(item);
        }
        return plmItems;
    }

    @Transactional(readOnly = true)
    public Page<ItemDto> searchItemsForMobile(Pageable pageable, ItemCriteria criteria) {
        Predicate predicate = predicateBuilder.build(criteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItems = itemRepository.findAll(predicate, pageable);
        List<ItemDto> items = new ArrayList<>();
        for (PLMItem item : plmItems.getContent()) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
            ItemDto itemDto = new ItemDto();
            itemDto.setId(item.getId());
            itemDto.setItemName(item.getItemName());
            itemDto.setItemNumber(item.getItemNumber());
            itemDto.setRevision(itemRevision.getRevision());
            itemDto.setLifecycle(itemRevision.getLifeCyclePhase().getPhase());
            itemDto.setPhaseType(itemRevision.getLifeCyclePhase().getPhaseType().toString());
            itemDto.setLatestRevision(item.getLatestRevision());
            itemDto.setSubType(item.getItemType().getName());
            itemDto.setSubType(item.getItemType().getName());
            items.add(itemDto);
        }

        return new PageImpl<ItemDto>(items, pageable, plmItems.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<PLMItem> freeTextSearchAll(ItemCriteria criteria) {
        Predicate predicate = predicateBuilder.build(criteria, QPLMItem.pLMItem);
        List<PLMItem> plmItems = IteratorUtils.toList(itemRepository.findAll(predicate).iterator());
        for (PLMItem item : plmItems) {
            item = setItemCounts(item);
        }
        return plmItems;
    }

    public void exportItems(String file) {
        JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int i = fc.showOpenDialog(null);
        if (i == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            String filepath = f.getAbsolutePath();
            //System.out.println(filepath);
            String extension = FilenameUtils.getExtension(f.getName());
            switch (file) {
                case "excel":
                    break;
                case "csv":
                    if (extension.contains("csv")) {
                        exportDataToCsv(f);
                    }
                    break;
                case "pdf":
                    break;
                case "html":
                    break;
                default:

            }
        }
    }

    private void exportDataToCsv(File file) {
        String items = null;
        PLMItemType itemType = null;
        try {
            List<PLMItem> plmItems = itemRepository.findAll();
            FileWriter writer = new FileWriter(file);
            String recordAsCsv = plmItems.stream()
                    .map(PLMItem::toCsvRow)
                    .collect(Collectors.joining(System.getProperty("line.separator")));
            writer.append(recordAsCsv);
            writer.flush();
            for (PLMItem item : plmItems) {
                itemType = item.getItemType();
                if (items == null) {
                    items = item.getItemNumber() + "[" + item.getItemName() + "]";
                } else {
                    items = items + " ," + item.getItemNumber() + "[" + item.getItemName() + "]";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getImageItem(Integer itemId, HttpServletResponse response) {
        PLMItem plmItem = itemRepository.findById(itemId);
        if (plmItem != null) {
            InputStream is = new ByteArrayInputStream(plmItem.getThumbnail());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PLMItemAttribute> getItemUsedAttributes(Integer attributeId) {
        List<PLMItemAttribute> itemAttributes = itemAttributeRepository.findByAttributeId(attributeId);
        return itemAttributes;
    }

    @Transactional
    public PLMItem saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        PLMItem item = itemRepository.findOne(objectId);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(objectId);
        if (item != null) {
            PLMItemAttribute itemAttribute = new PLMItemAttribute();
            itemAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, itemAttribute);

        }
        if (itemRevision != null) {
            PLMItemRevisionAttribute itemRevisionAttribute = new PLMItemRevisionAttribute();
            itemRevisionAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, itemRevisionAttribute);

        }
        return item;
    }

    @Transactional
    public PLMItem lockItem(Integer itemId, ItemsDto item) {
        PLMItem plmItem = itemRepository.findOne(item.getId());
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        plmItem.setLockObject(item.getLockObject());
        plmItem.setLockedBy(person);
        plmItem.setLockedDate(new Date());
        plmItem = itemRepository.save(plmItem);
        return plmItem;
    }

    @Transactional
    public PLMSubscribe subscribeItem(Integer itemId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMSubscribe subscribe = subscribeRepository.findByPersonAndObjectId(person, itemId);
        PLMItem item = itemRepository.findOne(itemId);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
        if (subscribe == null) {
            subscribe = new PLMSubscribe();
            subscribe.setPerson(person);
            subscribe.setObjectId(itemId);
            subscribe.setObjectType(item.getObjectType().name());
            subscribe.setSubscribe(true);
            subscribe = subscribeRepository.save(subscribe);
            applicationEventPublisher.publishEvent(new ItemEvents.ItemSubscribeEvent(itemRevision, item));
        } else {
            if (subscribe.getSubscribe()) {
                subscribe.setSubscribe(false);
                subscribe = subscribeRepository.save(subscribe);
                applicationEventPublisher.publishEvent(new ItemEvents.ItemUnSubscribeEvent(itemRevision, item));
            } else {
                subscribe.setSubscribe(true);
                subscribe = subscribeRepository.save(subscribe);
                applicationEventPublisher.publishEvent(new ItemEvents.ItemSubscribeEvent(itemRevision, item));
            }
        }
        return subscribe;
    }

    @Transactional(readOnly = true)
    public PLMSubscribe getSubscribeItemByPerson(Integer itemId, Integer personId) {
        Person person = personRepository.findOne(personId);
        return subscribeRepository.findByPersonAndObjectId(person, itemId);
    }

    private void sendSubscribeNotification(PLMItem item, String message, String mailSubject) {
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
                EmailTemplateConfiguration emailTemplateConfiguration = eMailTemplateConfigRepository.findByTemplateName("subscribeNotification.html");
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
                        mail.setTemplatePath("email/subscribeNotification.html");
                    }
                } else {
                    mail.setTemplatePath("email/subscribeNotification.html");
                }
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    public void sendItemSubscribeNotification(PLMItem item, String message, String mailSubject) {
        List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(item.getId());
        String[] recipientAddress = new String[subscribes.size()];
        String email = "";
        List<String> tokens = new LinkedList();
        if (subscribes.size() > 0) {
            for (int i = 0; i < subscribes.size(); i++) {
                PLMSubscribe subscribe = subscribes.get(i);
                if (email == "") {
                    email = subscribe.getPerson().getEmail();
                } else {
                    email = email + "," + subscribe.getPerson().getEmail();
                }
                if (subscribe.getPerson().getMobileDevice() != null && !subscribe.getPerson().getMobileDevice().getDisablePushNotification() &&
                        subscribe.getPerson().getMobileDevice().getDeviceId() != null) {
                    tokens.add(subscribe.getPerson().getMobileDevice().getDeviceId());
                }
            }

            if (tokens.size() > 0) {
                applicationEventPublisher.publishEvent(new PushNotificationEvents.UpdateItemNotification(item, mailSubject, tokens));
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
                mail.setTemplatePath("email/subscribeNotification.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    @Transactional(readOnly = true)
    public List<ProjectItemsDto> getProjectItems(Integer itemId) {
        List<ProjectItemsDto> projectItemsDtos = new ArrayList<>();
        Map<Integer, PLMProject> projectMap = new HashMap<>();
        Map<Integer, PLMActivity> activityMap = new HashMap<>();
        Map<Integer, PLMTask> taskMap = new HashMap<>();
        List<PLMTaskDeliverable> taskDeliverables = taskDeliverableRepository.findByItemRevision(itemId);
        if (taskDeliverables.size() > 0) {
            for (PLMTaskDeliverable taskDeliverable : taskDeliverables) {
                ProjectItemsDto projectItemsDto = new ProjectItemsDto();
                PLMTask task = taskRepository.findOne(taskDeliverable.getTask());
                taskMap.put(task.getId(), task);
                PLMActivity plmActivity = activityRepository.findOne(task.getActivity());
                activityMap.put(plmActivity.getId(), plmActivity);
                PLMWbsElement wbsElement = wbsElementRepository.findOne(plmActivity.getWbs());
                PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
                projectMap.put(project.getId(), project);
                projectItemsDto.setProject(project);
                projectItemsDto.setActivity(plmActivity);
                projectItemsDto.setTask(task);
                projectItemsDtos.add(projectItemsDto);
            }
        }
        List<PLMActivityDeliverable> activityDeliverables = activityDeliverableRepository.findByItemRevision(itemId);
        if (activityDeliverables.size() > 0) {
            for (PLMActivityDeliverable activityDeliverable : activityDeliverables) {
                ProjectItemsDto projectItemsDto = new ProjectItemsDto();
                PLMActivity activity = activityRepository.findOne(activityDeliverable.getActivity());
                PLMActivity plmActivity = activityMap.get(activity.getId());
                if (plmActivity == null) {
                    PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
                    PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
                    projectMap.put(project.getId(), project);
                    projectItemsDto.setProject(project);
                    projectItemsDto.setActivity(activity);
                    projectItemsDtos.add(projectItemsDto);
                    activityMap.put(activity.getId(), activity);
                }

            }
        }
        List<PLMProjectDeliverable> projectDeliverables = projectDeliveravbleRepository.findByItemRevision(itemId);
        if (projectDeliverables.size() > 0) {
            for (PLMProjectDeliverable projectDeliverable : projectDeliverables) {
                ProjectItemsDto projectItemsDto = new ProjectItemsDto();
                PLMProject project = projectRepository.findOne(projectDeliverable.getProject());
                PLMProject plmProject = projectMap.get(project.getId());
                if (plmProject == null) {
                    projectItemsDto.setProject(project);
                    projectItemsDtos.add(projectItemsDto);
                    projectMap.put(project.getId(), project);
                }
            }
        }
        return projectItemsDtos;
    }

    @Transactional(readOnly = true)
    public List<RequirementDeliverable> getItemRequirements(Integer itemId) {
        List<RequirementDeliverable> requirementDeliverables1 = new ArrayList<>();
        List<RequirementDeliverable> requirementDeliverables = requirementDeliverableRepository.findByObjectIdAndObjectType(itemId, PLMObjectType.ITEMREVISION.toString());
        if (requirementDeliverables.size() > 0) {
            for (RequirementDeliverable requirementDeliverable : requirementDeliverables) {
                if (requirementDeliverable.getRequirement().getLatest()) {
                    requirementDeliverables1.add(requirementDeliverable);
                }
            }
        }
        return requirementDeliverables1;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> getItemsByType(String name) {
        PLMItemType itemType = itemTypeRepository.findByName(name);
        List<PLMItem> items = itemRepository.findByItemType(itemType);
        return items;
    }

    @Transactional
    public PLMBom importBom(Integer id, Map<String, MultipartFile> fileMap) throws Exception {
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(id);
        PLMItem item = itemRepository.findOne(plmItemRevision.getItemMaster());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        for (MultipartFile file1 : fileMap.values()) {
            File file2 = importConverter.trimAndConvertMultipartFileToFile(file1);

            HashMap<String, List<String>> headerValueMap = importer.readExcel(file2);

            List<String> levels = headerValueMap.get("Level");
            List<String> numbers = headerValueMap.get("Item Number");
            for (int i = 1; i < levels.size() - 1; i++) {
                if (levels.get(i).equals("0") && numbers.get(i).equals(item.getItemNumber())) {
                    return importer.importData(headerValueMap);
                } else {
                    throw new CassiniException(messageSource.getMessage("please_import_proper_file_for_ref_see_help", null, "Please import proper format file! check help videos or document for proper format", LocaleContextHolder.getLocale()));
                }
            }
        }
        return null;
    }


    /*@Transactional
    public PLMBom importBom(Integer id, Map<String, MultipartFile> fileMap) throws Exception {
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(id);
        PLMItem item = itemRepository.findOne(plmItemRevision.getItemMaster());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        for (MultipartFile file1 : fileMap.values()) {
//			File file = importConverter.trimAndConvertMultipartFileToFile(file1);
            if (file1.getOriginalFilename().trim().endsWith(".xlsx")) {
                XSSFWorkbook workbook = new XSSFWorkbook(file1.getInputStream());
                XSSFSheet worksheet = workbook.getSheetAt(0);
                XSSFRow headerRow = worksheet.getRow(0);
                HashMap<Integer, String> itemNumberMap = new HashMap<>();
                for (int i = 0; i <= worksheet.getLastRowNum(); i++) {
                    if (i == 0) continue;
                    XSSFRow entryDetailRow = worksheet.getRow(i);
                    XSSFCell itemNumberCell = entryDetailRow.getCell(0);
                    XSSFCell quantityCell = entryDetailRow.getCell(3);
                    XSSFCell level = entryDetailRow.getCell(4);
                    XSSFCell refDesCell = entryDetailRow.getCell(7);
                    XSSFCell notesCell = entryDetailRow.getCell(8);
                    DataFormatter df = new DataFormatter();
                    String levelString = null;
                    if (level != null) {
                        levelString = df.formatCellValue(level);
                    }

                    String quantityString = null;
                    if (quantityCell != null) {
                        quantityString = df.formatCellValue(entryDetailRow.getCell(3));
                    }

                    String itemNumber = null;
                    if (itemNumberCell != null) {
                        itemNumber = itemNumberCell.getStringCellValue();
                    }

                    String refDes = null;
                    if (refDesCell != null) {
                        refDes = refDesCell.getStringCellValue();
                    }

                    String notes = null;
                    if (notesCell != null) {
                        notes = notesCell.getStringCellValue();
                    }

                    if (itemNumberCell == null || quantityCell == null || level == null || refDesCell == null || notesCell == null) {
                        throw new CassiniException(messageSource.getMessage("file_format_not_valid",
                                null, "File format is not valid for BOM import", LocaleContextHolder.getLocale()));
                    }

                    itemNumber = itemNumber.replace(" ", "");
                    PLMItem plmItem = itemRepository.findOneByItemNumber(itemNumber);
                    if (Integer.parseInt(levelString) == 0) {
                        PLMBom existBom = bomRepository.findByParentAndItem(itemRevision, plmItem);
                        itemNumberMap.put(Integer.parseInt(levelString), entryDetailRow.getCell(0).getStringCellValue().replace(" ", ""));
                        if (existBom == null) {
                            PLMBom bom = new PLMBom();
                            if (itemRevision != null) {
                                itemRevision.setHasBom(true);
                                itemRevision = itemRevisionRepository.save(itemRevision);
                                bom.setParent(itemRevision);
                            } else {
                                throw new CassiniException(messageSource.getMessage("parent_cannot_be_empty",
                                        null, "Parent can't be empty", LocaleContextHolder.getLocale()));
                            }
                            if (plmItem != null) {
                                bom.setItem(plmItem);
                                bom.setQuantity(Integer.parseInt(quantityString));
                            } else {
                                throw new CassiniException(messageSource.getMessage("item_number_does_not_exist",
                                        null, "Item Number does not exist", LocaleContextHolder.getLocale()));
                            }

                            if (refDes != null && !refDes.equals("")) {
                                checkRefDesValues(bom, refDes);
                            }
                            bom.setNotes(notes);
                            bom = bomRepository.save(bom);
                        } else {
                            if (refDes != null && !refDes.equals("")) {
                                checkRefDesValues(existBom, refDes);
                            }
                            existBom.setNotes(notes);
                            existBom = bomRepository.save(existBom);
                        }
                    } else {
                        Integer level1 = Integer.parseInt(levelString);
                        String parentItemNumber = itemNumberMap.get(level1 - 1);
                        PLMItem parentItem = itemRepository.findOneByItemNumber(parentItemNumber);
                        PLMItemRevision parentItemRevision = itemRevisionRepository.findOne(parentItem.getLatestRevision());
                        PLMItem presentItem = itemRepository.findOneByItemNumber(entryDetailRow.getCell(0).getStringCellValue().replace(" ", ""));
                        PLMBom existBomItem = bomRepository.findByParentAndItem(parentItemRevision, presentItem);
                        itemNumberMap.put(Integer.parseInt(levelString), entryDetailRow.getCell(0).getStringCellValue().replace(" ", ""));
                        if (existBomItem == null) {
                            PLMBom bom = new PLMBom();
                            if (parentItemRevision != null) {
                                parentItemRevision.setHasBom(true);
                                parentItemRevision = itemRevisionRepository.save(parentItemRevision);
                                bom.setParent(parentItemRevision);
                            } else {
                                throw new CassiniException(messageSource.getMessage("parent_cannot_be_empty",
                                        null, "Parent can't be empty", LocaleContextHolder.getLocale()));
                            }
                            if (presentItem != null) {
                                bom.setItem(presentItem);
                                bom.setQuantity(Integer.parseInt(quantityString));
                            } else {
                                throw new CassiniException(messageSource.getMessage("item_number_does_not_exist",
                                        null, "Item Number does not exist", LocaleContextHolder.getLocale()));
                            }

                            if (refDes != null && !refDes.equals("")) {
                                checkRefDesValues(bom, refDes);
                            }
                            bom.setNotes(notes);
                            bom = bomRepository.save(bom);
                        } else {
                            if (refDes != null && !refDes.equals("")) {
                                checkRefDesValues(existBomItem, refDes);
                            }
                            existBomItem.setNotes(notes);
                            existBomItem = bomRepository.save(existBomItem);
                        }
                    }
                }
            } else if (file1.getOriginalFilename().trim().endsWith(".xls")) {
                HSSFWorkbook workbook = new HSSFWorkbook(file1.getInputStream());
                HSSFSheet worksheet = workbook.getSheetAt(0);
//                HSSFRow headerRow = worksheet.getRow(0);
                HashMap<Integer, String> itemNumberMap = new HashMap<>();
                for (int i = 0; i <= worksheet.getLastRowNum(); i++) {
                    if (i == 0) continue;
                    HSSFRow entryDetailRow = worksheet.getRow(i);
                    HSSFCell level = entryDetailRow.getCell(4);
                    DataFormatter df = new DataFormatter();
                    String levelString = null;
                    if (level != null) {
                        levelString = df.formatCellValue(level);
                    }

                    HSSFCell quantityCell = entryDetailRow.getCell(3);
                    String quantityString = null;
                    if (quantityCell != null) {
                        quantityString = df.formatCellValue(entryDetailRow.getCell(3));
                    }

                    HSSFCell itemNumberCell = entryDetailRow.getCell(0);
                    String itemNumber = null;
                    if (itemNumberCell != null) {
                        itemNumber = itemNumberCell.getStringCellValue();
                    }

                    HSSFCell refDesCell = entryDetailRow.getCell(7);
                    String refDes = null;
                    if (refDesCell != null) {
                        refDes = refDesCell.getStringCellValue();
                    }

                    HSSFCell notesCell = entryDetailRow.getCell(8);
                    String notes = null;
                    if (notesCell != null) {
                        notes = notesCell.getStringCellValue();
                    }

                    if (itemNumberCell == null || quantityCell == null || level == null || refDesCell == null || notesCell == null) {
                        throw new CassiniException(messageSource.getMessage("file_format_not_valid",
                                null, "File format is not valid for BOM import", LocaleContextHolder.getLocale()));
                    }

                    itemNumber = itemNumber.replace(" ", "");
                    PLMItem plmItem = itemRepository.findOneByItemNumber(itemNumber);
                    if (Integer.parseInt(levelString) == 0) {
                        PLMBom existBom = bomRepository.findByParentAndItem(itemRevision, plmItem);
                        itemNumberMap.put(Integer.parseInt(levelString), entryDetailRow.getCell(0).getStringCellValue().replace(" ", ""));
                        if (existBom == null) {
                            PLMBom bom = new PLMBom();
                            if (itemRevision != null) {
                                itemRevision.setHasBom(true);
                                itemRevision = itemRevisionRepository.save(itemRevision);
                                bom.setParent(itemRevision);
                            } else {
                                throw new CassiniException(messageSource.getMessage("parent_cannot_be_empty",
                                        null, "Parent can't be empty", LocaleContextHolder.getLocale()));
                            }
                            if (plmItem != null) {
                                bom.setItem(plmItem);
                                bom.setQuantity(Integer.parseInt(quantityString));
                            } else {
                                throw new CassiniException(messageSource.getMessage("item_number_does_not_exist",
                                        null, "Item Number does not exist", LocaleContextHolder.getLocale()));
                            }
                            if (refDes != null && !refDes.equals("")) {
                                checkRefDesValues(bom, refDes);
                            }
                            bom.setNotes(notes);
                            bom = bomRepository.save(bom);
                        } else {
                            if (refDes != null && !refDes.equals("")) {
                                checkRefDesValues(existBom, refDes);
                            }
                            existBom.setNotes(notes);
                            existBom = bomRepository.save(existBom);
                        }
                    } else {
                        Integer level1 = Integer.parseInt(levelString);
                        String parentItemNumber = itemNumberMap.get(level1 - 1);
                        PLMItem parentItem = itemRepository.findOneByItemNumber(parentItemNumber);
                        PLMItemRevision parentItemRevision = itemRevisionRepository.findOne(parentItem.getLatestRevision());
                        PLMItem presentItem = itemRepository.findOneByItemNumber(entryDetailRow.getCell(0).getStringCellValue().replace(" ", ""));
                        PLMBom existBomItem = bomRepository.findByParentAndItem(parentItemRevision, presentItem);
                        itemNumberMap.put(Integer.parseInt(levelString), entryDetailRow.getCell(0).getStringCellValue().replace(" ", ""));
                        if (existBomItem == null) {
                            PLMBom bom = new PLMBom();
                            if (parentItemRevision != null) {
                                parentItemRevision.setHasBom(true);
                                parentItemRevision = itemRevisionRepository.save(parentItemRevision);
                                bom.setParent(parentItemRevision);
                            } else {
                                throw new CassiniException(messageSource.getMessage("parent_cannot_be_empty",
                                        null, "Parent can't be empty", LocaleContextHolder.getLocale()));
                            }
                            if (presentItem != null) {
                                bom.setItem(presentItem);
                                bom.setQuantity(Integer.parseInt(quantityString));
                            } else {
                                throw new CassiniException(messageSource.getMessage("item_number_does_not_exist",
                                        null, "Item Number does not exist", LocaleContextHolder.getLocale()));
                            }
                            if (refDes != null && !refDes.equals("")) {
                                checkRefDesValues(bom, refDes);
                            }
                            bom.setNotes(notes);
                            bom = bomRepository.save(bom);
                        } else {
                            if (refDes != null && !refDes.equals("")) {
                                checkRefDesValues(existBomItem, refDes);
                            }
                            existBomItem.setNotes(notes);
                            existBomItem = bomRepository.save(existBomItem);
                        }
                    }
                }
            } else {
                throw new CassiniException(messageSource.getMessage("upload_valid_file_format",
                        null, "Please upload valid file format", LocaleContextHolder.getLocale()));
            }
        }
        return null;
    }*/

    private PLMBom checkRefDesValues(PLMBom bom, String refDes) {
        if (refDes != null && !refDes.equals("")) {
            HashMap<String, String> refDesMap = new HashMap<>();

            List<PLMBom> children = bomRepository.findByParentOrderByCreatedDateAsc(bom.getParent());
            for (PLMBom bomItem : children) {
                if (!bomItem.getId().equals(bom.getId())) {
                    if (bomItem.getRefdes() != null && !bomItem.getRefdes().equals("")) {
                        String[] refDesignators = bomItem.getRefdes().split(",");
                        if (refDesignators.length == bomItem.getQuantity()) {
                            for (String refDe : refDesignators) {
                                String existRefDes = refDesMap.get(refDe.toUpperCase().trim());
                                if (existRefDes == null) {
                                    refDesMap.put(refDe.toUpperCase().trim(), refDe);
                                }
                            }
                        }
                    }
                }
            }
            String[] referenceDes = refDes.split(",");
            if (referenceDes.length == bom.getQuantity()) {
                for (String refDe : referenceDes) {
                    String existRefDes = refDesMap.get(refDe.toUpperCase().trim());
                    if (existRefDes == null) {
                        refDesMap.put(refDe.toUpperCase().trim(), refDe);
                    } else {
                        throw new CassiniException(messageSource.getMessage(bom.getItem().getItemNumber() + " - " + refDe + " : " + "ref_desig_already_in_use",
                                null, bom.getItem().getItemNumber() + " - " + refDe + " : " + "Reference designator already in use", LocaleContextHolder.getLocale()));
                    }
                }
            } else {
                throw new CassiniException(messageSource.getMessage(bom.getItem().getItemNumber() + " : " + "bom_item_ref_desig_should_same",
                        null, bom.getItem().getItemNumber() + " : " + "BOM item quantity and reference designators should be same", LocaleContextHolder.getLocale()));
            }
            bom.setRefdes(refDes);
        }

        return bom;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto itemDetails(Integer id) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

        ItemDetailsDto itemDetailsDto = new ItemDetailsDto();
        if (item != null && item.getConfigured()) {
            PLMItemRevision parentRevision = itemRevisionRepository.findOne(itemRevision.getInstance());

            Integer files = itemFileRepository.getChildrenCountByItemAndParentFileAndLatestTrue(parentRevision.getId(), "FILE");
            itemDetailsDto.setItemFiles(files);
            itemDetailsDto.setItemFiles(itemDetailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(parentRevision.getId()));

            List<PLMItemManufacturerPart> itemManufacturerParts = itemManufacturerPartRepository.findByItem(parentRevision.getId());
            itemDetailsDto.setManufacturerParts(itemManufacturerParts.size());
            itemManufacturerParts = itemManufacturerPartRepository.findByItem(itemRevision.getId());
            itemDetailsDto.setManufacturerParts(itemDetailsDto.getManufacturerParts() + itemManufacturerParts.size());
        } else {
            Integer files = itemFileRepository.getChildrenCountByItemAndParentFileAndLatestTrue(itemRevision.getId(), "FILE");
            itemDetailsDto.setItemFiles(files);
            itemDetailsDto.setItemFiles(itemDetailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(itemRevision.getId()));

            List<PLMItemManufacturerPart> itemManufacturerParts = itemManufacturerPartRepository.findByItem(itemRevision.getId());
            itemDetailsDto.setManufacturerParts(itemManufacturerParts.size());
        }
        List<PLMRelatedItem> plmRelatedItems = relatedItemRepository.findByFromItem(itemRevision.getId());
        itemDetailsDto.setRelatedItems(plmRelatedItems.size());
        HashMap<Integer, PLMRelatedItem> relatedItemMap = new HashMap<>();
        plmRelatedItems.forEach(plmRelatedItem -> {
            relatedItemMap.put(plmRelatedItem.getToItem().getId(), plmRelatedItem);
        });

        plmRelatedItems = relatedItemRepository.findByToItem(itemRevision);
        plmRelatedItems.forEach(plmRelatedItem -> {
            PLMRelatedItem existItem = relatedItemMap.get(plmRelatedItem.getFromItem());
            if (existItem == null) {
                itemDetailsDto.setRelatedItems(itemDetailsDto.getRelatedItems() + 1);
            }
        });

        Integer alternateParts = alternatePartRepository.getAlternatePartsCount(itemRevision.getItemMaster());
        Integer substituteParts = substitutePartRepository.getSubstitutePartsCount(itemRevision.getItemMaster());
        itemDetailsDto.setRelatedItems(itemDetailsDto.getRelatedItems() + alternateParts + substituteParts);

        itemDetailsDto.setInspections(itemInspectionRepository.getItemInspectionCount(id));

        if (item != null && item.getItemType().getItemClass() != null && item.getItemType().getItemClass().equals(ItemClass.DOCUMENT)) {
            List<PLMDCO> dco = dcoService.findByAffectedItem(itemRevision.getId());
            List<PLMDCR> dcr = dcrService.findByAffectedItem(itemRevision.getId());
            if (dco.size() > 0 || dcr.size() > 0) {
                itemDetailsDto.setChangeItems(dco.size() + dcr.size());
            }

        }

        if (item != null && item.getItemType().getItemClass() != null && !item.getItemType().getItemClass().equals(ItemClass.DOCUMENT)) {
            List<PLMECO> eco = ecoService.findByAffectedItem(itemRevision.getId());
            List<PLMECR> ecr = ecrService.findByAffectedItem(itemRevision.getId());
            if (eco.size() > 0 || ecr.size() > 0) {
                itemDetailsDto.setChangeItems(eco.size() + ecr.size());
            }
        }

        Integer pqmProblemReportsCount = problemReportService.findByProblemItem(itemRevision.getId()).size();
        Integer qcrCount = qcrProblemItemRepository.getQCRCountByItem(itemRevision.getId());
        if (pqmProblemReportsCount > 0 || qcrCount > 0) {
            itemDetailsDto.setQualityItems(pqmProblemReportsCount + qcrCount);
        }

        if (item != null && item.getItemType().getItemClass() != null && item.getItemType().getItemClass().equals(ItemClass.PRODUCT)) {
            Integer count = problemReportRepository.getPrCountByItem(id);
            itemDetailsDto.setQualityItems(itemDetailsDto.getQualityItems() + count);
        }

        Integer itemBoms = bomRepository.getItemBomCount(itemRevision.getId());
        if (itemBoms > 0) {
            itemDetailsDto.setBom(itemBoms);
        }
        List<PLMECO> changes = ecoService.findByAffectedItem(itemRevision.getId());
        if (changes.size() > 0) {
            itemDetailsDto.setChanges(true);
        }
        if (item != null) {
            List<PLMBom> whereUsedItems = bomService.getWhereUsedCounts(item);
            if (whereUsedItems.size() > 0) {
                itemDetailsDto.setWhereUsedItems(whereUsedItems.size());
            }
        }
        List<PLMVariance> variances = varianceService.findByAffectedItem(id);
        if (variances.size() > 0) {
            itemDetailsDto.setVarianceItems(variances.size());
        }
        itemDetailsDto.setSpecifications(pgcItemSpecificationRepository.getItemSpecificationCount(id));
        itemDetailsDto.setRequirements(requirementItemRepository.getRequirementsByItemCount(id));
        List<ProjectItemsDto> projectItemsDtos = getProjectItems(itemRevision.getId());
        itemDetailsDto.setProjectItemsDtos(projectItemsDtos.size());
        /*List<RequirementDeliverable> deliverables = getItemRequirements(itemRevision.getId());
        itemDetailsDto.setRequirements(deliverables.size());*/
        List<PLMItem> items = getInstances(itemRevision.getId());
        itemDetailsDto.setConfiguredItems(items.size());
        String toRevision = getNextRevisionSequence(item);

        PLMItemRevision oldRevision = itemRevisionRepository.getByItemMasterAndRevision(item.getId(), "-");
        List<PLMAffectedItem> affectedItems = affectedItemRepository.findByItemAndToRevision(item.getLatestRevision(), toRevision);
        if (item.getLatestReleasedRevision() != null) {
            PLMItemRevision latestRevisison = itemRevisionRepository.findOne(item.getLatestRevision());
            List<PLMAffectedItem> releasedAffectedItems = affectedItemRepository.findByItemAndToRevision(item.getLatestReleasedRevision(), toRevision);
            List<PLMAffectedItem> releasedAffectedItems1 = affectedItemRepository.findByItemAndToRevision(item.getLatestReleasedRevision(), latestRevisison.getRevision());
            List<PLMAffectedItem> releasedAffectedItems2 = affectedItemRepository.findByItemAndToRevision(item.getLatestRevision(), latestRevisison.getRevision());
            List<PLMAffectedItem> releasedAffectedItems3 = affectedItemRepository.findByItemAndToRevision(itemRevision.getId(), latestRevisison.getRevision());
            if (releasedAffectedItems.size() > 0) {
                releasedAffectedItems.forEach(releasedAffectedItem -> {
                    setPendingEco(itemDetailsDto, releasedAffectedItem);
                });
            }
            if (releasedAffectedItems1.size() > 0) {
                releasedAffectedItems1.forEach(releasedAffectedItem1 -> {
                    setPendingEco(itemDetailsDto, releasedAffectedItem1);
                });
            }
            if (releasedAffectedItems2.size() > 0) {
                releasedAffectedItems2.forEach(releasedAffectedItem2 -> {
                    setPendingEco(itemDetailsDto, releasedAffectedItem2);
                });
            }
            if (releasedAffectedItems3.size() > 0) {
                releasedAffectedItems3.forEach(releasedAffectedItem3 -> {
                    setPendingEco(itemDetailsDto, releasedAffectedItem3);
                });
            }
        }
        if (affectedItems.size() > 0) {
            affectedItems.forEach(affectedItem -> {
                setPendingEco(itemDetailsDto, affectedItem);
            });
        }

        if (oldRevision != null && item.getLatestReleasedRevision() == null && itemRevision.getRejected()) {
            List<PLMAffectedItem> plmAffectedItems = affectedItemRepository.findByItemAndToRevision(oldRevision.getId(), toRevision);
            if (plmAffectedItems.size() > 0) {
                plmAffectedItems.forEach(plmAffectedItem -> {
                    setPendingEco(itemDetailsDto, plmAffectedItem);
                });
            }
        }

        List<PLMAffectedItem> affectedToItems = affectedItemRepository.findByToItemAndToRevision(item.getLatestRevision(), itemRevision.getRevision());
        if (affectedToItems.size() > 0) {
            affectedToItems.forEach(affectedToItem -> {
                setPendingEco(itemDetailsDto, affectedToItem);
            });
        }

        if (item.getLatestReleasedRevision() != null && item.getLatestReleasedRevision().equals(itemRevision.getId())) {
            itemDetailsDto.setRejectedOrOldRevision(false);
        }

        if (!itemDetailsDto.getPendingEco()) {
            if (item.getLatestReleasedRevision() != null && !item.getLatestReleasedRevision().equals(itemRevision.getId())) {
                itemDetailsDto.setRejectedOrOldRevision(true);
            }
            if (itemRevision.getRejected()) {
                itemDetailsDto.setRejectedOrOldRevision(true);
            }
            if (itemRevision.getRevision().equals("-")) {
                String lastRev = itemRevision.getRevision();
                Lov lov = item.getItemType().getRevisionSequence();
                String[] values = lov.getValues();
                int lastIndex = -1;
                for (int i = 0; i < values.length; i++) {
                    String rev = values[i];
                    if (rev.equalsIgnoreCase(lastRev)) {
                        lastIndex = i;
                        break;
                    }
                }
                String nextRev = "";
                if (lastIndex != -1 && lastIndex < values.length) {
                    nextRev = values[lastIndex + 1];
                }
                PLMItemRevision nextRevision = itemRevisionRepository.getByItemMasterAndRevision(item.getId(), nextRev);
                if (nextRevision != null) {
                    itemDetailsDto.setRejectedOrOldRevision(true);
                }
            }
        }
        return itemDetailsDto;
    }

    private ItemDetailsDto setPendingEco(ItemDetailsDto itemDetailsDto, PLMAffectedItem affectedItem) {
        PLMECO plmeco = ecoRepository.findOne(affectedItem.getChange());
        if (plmeco != null) {
            if (!plmeco.getReleased() && !plmeco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                itemDetailsDto.setPendingEco(true);
            }
            if (plmeco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                itemDetailsDto.setRejectedOrOldRevision(true);
            }
        } else {
            PLMDCO plmdco = dcoRepository.findOne(affectedItem.getChange());
            if (plmdco != null) {
                if (!plmdco.getIsReleased() && !plmdco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                    itemDetailsDto.setPendingEco(true);
                }
                if (plmdco.getStatusType().equals(WorkflowStatusType.REJECTED)) {
                    itemDetailsDto.setRejectedOrOldRevision(true);
                }
            }
        }

        return itemDetailsDto;
    }

    @Transactional
    public Comment createComment(Comment comment) {
        if (comment.getObjectId() != null) {
            String commentText = "";
            String mailSubject = "";
            PLMItemRevision revision = itemRevisionRepository.findOne(comment.getObjectId());
            PLMItem item = itemRepository.findOne(revision.getItemMaster());
            comment.setObjectId(item.getId());

            List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(item.getId());
            if (subscribes.size() > 0) {
                Person person = sessionWrapper.getSession().getLogin().getPerson();
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
                commentText = person.getFullName() + " has commented  " + " ' " + comment.getComment() + "'" + " on (" + item.getItemNumber() + " - " + item.getItemName() + ")Item";
                mailSubject = item.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
                sendItemSubscribeNotification(item, commentText, mailSubject);
            }

            checkNotNull(comment);
            comment.setId(null);
            comment.setCommentedBy(sessionWrapper.getSession().getLogin().getPerson());
            comment.setCommentedDate(new Date());
            comment = commentRepository.save(comment);

            /* App events */
            applicationEventPublisher.publishEvent(new ItemEvents.ItemCommentAddedEvent(revision, comment));
        }

        return comment;
    }


    private Long getFileSizes(List<PLMFile> files) {
        long sum = 0L;
        for (PLMFile file : files) {
            sum = sum + file.getSize();
        }
        return sum;
    }

    @Transactional(readOnly = true)
    public SystemInfoDto getSystemInfo() {
        SystemInfoDto systemInfoDto = new SystemInfoDto();
        Map<String, Map<String, Integer>> systemInfo = new LinkedHashMap<>();
        Map<String, Integer> separateMap = new LinkedHashMap<>();
        List<PLMItem> plmItems = itemRepository.findAll();
        List<PLMFile> files = fileRepository.findAll();
        Long totalFileSystemSize = this.getFileSizes(files);
        String fileSystemSize = this.appDetailsService.getSize(totalFileSystemSize);
        systemInfoDto.setFileSystemSize(fileSystemSize);
        separateMap.put("Total Items", plmItems.size());
        systemInfo.put("Items", separateMap);
        separateMap = new LinkedHashMap<>();
        separateMap.put("Total Files", files.size());
        systemInfo.put("Files", separateMap);
        if (plmItems.size() > 0) {
            systemInfoDto.setItems(plmItems.size());
        }
        if (files.size() > 0) {
            systemInfoDto.setFiles(files.size());
        }
        List<PLMItemType> itemTypes = itemTypeRepository.findAll();
        if (itemTypes.size() > 0) {
            systemInfoDto.setItemTypes(itemTypes);
            for (PLMItemType itemType : itemTypes) {
                List<PLMItem> plmItems1 = itemRepository.findByItemType(itemType);
                Map<String, Integer> itemTypeMap = systemInfo.containsKey("Items") ? systemInfo.get("Items") : new LinkedHashMap<>();
                itemTypeMap.put(itemType.getName(), plmItems1.size());
                systemInfo.put("Items", itemTypeMap);
            }
        }
        systemInfoDto.setAdminInfo(systemInfo);
        return systemInfoDto;
    }

    @Transactional
    public void setImageAsDefaultForItem(Integer id, Integer fileId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        item.setItemImage(fileId);
        itemRepository.save(item);
    }

    public void updateItem(Integer id) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        if (item != null) {
            item.setModifiedDate(new Date());
            item = itemRepository.save(item);
        }
    }

    @Transactional
    public void updateEmailTemplate(EmailTemplateConfiguration emailTemplateConfiguration) {
        eMailTemplateConfigRepository.save(emailTemplateConfiguration);
    }

    @Transactional(readOnly = true)
    public EmailTemplateConfiguration findTemplate(EmailTemplateConfiguration emailTemplateConfiguration) throws IOException {
        if (emailTemplateConfiguration.getTemplateName().equals("shareobjectmail.html")) {
            URL url = ItemService.class
                    .getClassLoader().getResource("templates/email/share/" + "shareobjectmail.html");
            String content = IOUtils.toString(url, "utf-8");
            EmailTemplateConfiguration emailTemplateConfigurationResult = eMailTemplateConfigRepository.findByTemplateName("shareobjectmail.html");
            if (emailTemplateConfigurationResult == null) {
                emailTemplateConfiguration.setTemplateId(null);
                emailTemplateConfiguration.setTemplateName("shareobjectmail.html");
                emailTemplateConfiguration.setTemplateSourceCode(content);
            } else {
                emailTemplateConfiguration = emailTemplateConfigurationResult;
            }
        }
        if (emailTemplateConfiguration.getTemplateName().equals("sharedProjectObject.html")) {
            URL url = ItemService.class
                    .getClassLoader().getResource("templates/email/share/" + "sharedProjectObject.html");
            String content = IOUtils.toString(url, "utf-8");
            EmailTemplateConfiguration emailTemplateConfigurationResult = eMailTemplateConfigRepository.findByTemplateName("sharedProjectObject.html");
            if (emailTemplateConfigurationResult == null) {
                emailTemplateConfiguration.setTemplateId(null);
                emailTemplateConfiguration.setTemplateName("sharedProjectObject.html");
                emailTemplateConfiguration.setTemplateSourceCode(content);
            } else {
                emailTemplateConfiguration = emailTemplateConfigurationResult;
            }
        }
        if (emailTemplateConfiguration.getTemplateName().equals("workflowApprover.html")) {
            URL url = ItemService.class
                    .getClassLoader().getResource("templates/email/workflow/" + "workflowApprover.html");
            String content = IOUtils.toString(url, "utf-8");
            EmailTemplateConfiguration emailTemplateConfigurationResult = eMailTemplateConfigRepository.findByTemplateName("workflowApprover.html");
            if (emailTemplateConfigurationResult == null) {
                emailTemplateConfiguration.setTemplateId(null);
                emailTemplateConfiguration.setTemplateName("workflowApprover.html");
                emailTemplateConfiguration.setTemplateSourceCode(content);
            } else {
                emailTemplateConfiguration = emailTemplateConfigurationResult;
            }
        }
        if (emailTemplateConfiguration.getTemplateName().equals("subscribeNotification.html")) {
            URL url = ItemService.class
                    .getClassLoader().getResource("templates/email/" + "subscribeNotification.html");
            String content = IOUtils.toString(url, "utf-8");
            EmailTemplateConfiguration emailTemplateConfigurationResult = eMailTemplateConfigRepository.findByTemplateName("subscribeNotification.html");
            if (emailTemplateConfigurationResult == null) {
                emailTemplateConfiguration.setTemplateId(null);
                emailTemplateConfiguration.setTemplateName("subscribeNotification.html");
                emailTemplateConfiguration.setTemplateSourceCode(content);
            } else {
                emailTemplateConfiguration = emailTemplateConfigurationResult;
            }
        }
        if (emailTemplateConfiguration.getTemplateName().equals("subscribeSpecificationNotification.html")) {
            URL url = ItemService.class
                    .getClassLoader().getResource("templates/email/" + "subscribeSpecificationNotification.html");
            String content = IOUtils.toString(url, "utf-8");
            EmailTemplateConfiguration emailTemplateConfigurationResult = eMailTemplateConfigRepository.findByTemplateName("subscribeSpecificationNotification.html");
            if (emailTemplateConfigurationResult == null) {
                emailTemplateConfiguration.setTemplateId(null);
                emailTemplateConfiguration.setTemplateName("subscribeSpecificationNotification.html");
                emailTemplateConfiguration.setTemplateSourceCode(content);
            } else {
                emailTemplateConfiguration = emailTemplateConfigurationResult;
            }
        }
        if (emailTemplateConfiguration.getTemplateName().equals("requirementTemplate.html")) {
            URL url = ItemService.class
                    .getClassLoader().getResource("templates/email/" + "requirementTemplate.html");
            String content = IOUtils.toString(url, "utf-8");
            EmailTemplateConfiguration emailTemplateConfigurationResult = eMailTemplateConfigRepository.findByTemplateName("requirementTemplate.html");
            if (emailTemplateConfigurationResult == null) {
                emailTemplateConfiguration.setTemplateId(null);
                emailTemplateConfiguration.setTemplateName("requirementTemplate.html");
                emailTemplateConfiguration.setTemplateSourceCode(content);
            } else {
                emailTemplateConfiguration = emailTemplateConfigurationResult;
            }
        }
        return emailTemplateConfiguration;
    }

    public List<PLMItem> getAllHasBomItems(Integer itemMasterId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemMasterId);
        List<PLMItem> plmItems = new ArrayList<>();
        List<PLMItemRevision> plmItemRevisions = itemRevisionRepository.findByHasBomTrue();
        plmItemRevisions.remove(itemRevision);
        for (PLMItemRevision revision : plmItemRevisions) {
            if (!itemRevision.getItemMaster().equals(revision.getItemMaster())) {
                PLMItem item = itemRepository.findOne(revision.getItemMaster());
                if (item != null) {
                    item.setRev(revision);
                    plmItems.add(item);

                }
            }

        }
        return plmItems;
    }

    public List<PLMItem> getAllHasBomItemsLatestFalse(Integer itemMasterId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemMasterId);
        List<PLMItem> plmItems = new ArrayList<>();
        List<PLMItemRevision> plmItemRevisions = itemRevisionRepository.findByHasBomTrue();
        plmItemRevisions.remove(itemRevision);
        for (PLMItemRevision revision : plmItemRevisions) {
            if (!itemRevision.getItemMaster().equals(revision.getItemMaster())) {
                PLMItem item = itemRepository.findOne(revision.getItemMaster());
                if (item != null && item.getLatestRevision().equals(revision.getId())) {
                    item.setRev(revision);
                    plmItems.add(item);

                }
            }
        }
        return plmItems;
    }

    public List<PLMItem> getAllItemsToCompare(Integer itemMasterId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemMasterId);
        List<PLMItem> plmItems = new ArrayList<>();
        List<PLMItemRevision> plmItemRevisions = itemRevisionRepository.findAll();
        plmItemRevisions.remove(itemRevision);
        for (PLMItemRevision revision : plmItemRevisions) {
            if (!itemRevision.getItemMaster().equals(revision.getItemMaster())) {
                PLMItem item = itemRepository.findOne(revision.getItemMaster());
                if (item != null) {
                    item.setRev(revision);
                    plmItems.add(item);

                }
            }
        }
        return plmItems;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getItemsToCompare(Pageable pageable, ItemCriteria itemCriteria) {
        Predicate predicate = itemRevisionPredicateBuilder.build(itemCriteria, QPLMItemRevision.pLMItemRevision);
        Page<PLMItemRevision> itemRevisions = itemRevisionRepository.findAll(predicate, pageable);
        List<PLMItem> items = new ArrayList<>();

        itemRevisions.forEach(plmItemRevision -> {
            PLMItem item = JsonUtils.cloneEntity(itemRepository.findOne(plmItemRevision.getItemMaster()), PLMItem.class);
            if (item != null) {
                item.setRev(plmItemRevision);
            }
            items.add(item);
        });

        return new PageImpl<PLMItem>(items, pageable, itemRevisions.getTotalElements());
    }

    public List<PLMItem> getAllItemsToCompareLatestFalse(Integer itemMasterId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemMasterId);
        List<PLMItem> plmItems = new ArrayList<>();
        List<PLMItemRevision> plmItemRevisions = itemRevisionRepository.findAll();
        plmItemRevisions.remove(itemRevision);
        for (PLMItemRevision revision : plmItemRevisions) {
            if (!itemRevision.getItemMaster().equals(revision.getItemMaster())) {
                PLMItem item = itemRepository.findOne(revision.getItemMaster());
                if (item != null && item.getLatestRevision().equals(revision.getId())) {
                    item.setRev(revision);
                    plmItems.add(item);

                }
            }
        }
        return plmItems;
    }

    @Transactional(readOnly = true)
    public List<PLMItemRevision> getAllItemRevisionsByMaster(Integer id) {
        List<PLMItemRevision> revisions = new ArrayList<>();
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        List<PLMItemRevision> plmItemRevisions = itemRevisionRepository.findByItemMasterOrderByIdAsc(itemRevision.getItemMaster());
        plmItemRevisions.remove(itemRevision);
        for (PLMItemRevision rev : plmItemRevisions) {
            PLMItem plmItem = itemRepository.findOne(rev.getItemMaster());
            rev.setItem(plmItem);
            rev.setItemName(plmItem.getItemNumber() + "-" + "Rev" + " -" + rev.getRevision());
            revisions.add(rev);
        }
        PLMItemRevision rev = new PLMItemRevision();
        String message = messageSource.getMessage("select_item_to_compare", null, "Select Item To Compare", LocaleContextHolder.getLocale());
        rev.setItemName(message);
        revisions.add(rev);
        return revisions;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> getItemInstances(Integer id) {
        PLMItemType itemType = itemTypeRepository.findOne(id);
        List<PLMItem> items = itemRepository.findByItemTypeAndConfigurableTrue(itemType);
        items.forEach(plmItem -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
            plmItem.setHasBom(itemRevision.getHasBom());
        });
        return items;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> getInstances(Integer id) {
        List<PLMItem> items = new ArrayList<>();
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        List<PLMItemRevision> revisions = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(itemRevision.getId());
        for (PLMItemRevision itemRevision1 : revisions) {
            PLMItem item = itemRepository.findOne(itemRevision1.getItemMaster());
            if (itemRevision1.getBomConfiguration() != null) {
                BOMConfiguration configuration = bomConfigurationRepository.findOne(itemRevision1.getBomConfiguration());
                if (configuration != null) {
                    item.setConfigurationName(configuration.getName());
                }
            }
            items.add(item);
        }
        return items;
    }


    public void checkGitReleasedTag(PLMItem item, PLMItemRevision revision) {
        if (item.getItemType().getSoftwareType() == true) {
            PDMGitHubItemRepository repository = pdmGitHubItemRepositoryRepository.findByItem(item.getId());
            if (repository != null) {
                PDMGitHubItemRevisionRelease release = pdmGitHubItemRevisionReleaseRepository.findByItemRevision(revision.getId());
                if (release == null) {
                    throw new CassiniException(messageSource.getMessage("check_released_tag_validation", null, "The software item should have release tag", LocaleContextHolder.getLocale()));
                }
            }

        }
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'promote','item')")
    public PLMItemRevision promoteItem(Integer id, PLMItemRevision revision) {
        revision = itemRevisionRepository.findOne(id);
        PLMItem item = itemRepository.findOne(revision.getItemMaster());

        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(item.getItemType().getLifecycle().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = revision.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
        String message = null;
        String mailSubject = null;
        if (index != -1) {
            PLMLifeCyclePhase oldStatus = revision.getLifeCyclePhase();
            index++;
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                revision.setLifeCyclePhase(lifeCyclePhase);
                if (lifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    Integer notReleasedDocumentCount = objectDocumentRepository.getNotReleasedDocumentsCount(revision.getId());
                    if (notReleasedDocumentCount > 0) {
                        message = messageSource.getMessage("item_has_unreleased_documents", null, "[{0}] item has some unreleased documents", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", item.getItemNumber());
                        throw new CassiniException(result);
                    }
                    checkGitReleasedTag(item, revision);
                    revision.setReleased(true);
                    revision.setReleasedDate(new Date());
                    item.setLatestRevision(revision.getId());

                    /* App events */
                    applicationEventPublisher.publishEvent(new ItemEvents.ItemReleasedEvent(item, revision, null));
                }
                applicationEventPublisher.publishEvent(new ItemEvents.ItemPromotedEvent(item, revision, oldStatus, lifeCyclePhase));
                revision = itemRevisionRepository.save(revision);
                PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
                statusHistory.setItem(revision.getId());
                statusHistory.setOldStatus(oldStatus);
                statusHistory.setNewStatus(revision.getLifeCyclePhase());
                statusHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                statusHistory = revisionStatusHistoryRepository.save(statusHistory);
                message = item.getItemNumber() + " Promoted lifeCyclePhase by " + sessionWrapper.getSession().getLogin().getPerson().getFullName() + " to ( " + oldStatus.getPhase() + " to " + revision.getLifeCyclePhase().getPhase() + ")";
                mailSubject = item.getItemNumber() + " : " + " Rev " + revision.getRevision() + " : " + revision.getLifeCyclePhase().getPhase() + " Notification";
                sendItemSubscribeNotification(item, message, mailSubject);
            }
        }
        return revision;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'demote','item')")
    public PLMItemRevision demoteItem(Integer id, PLMItemRevision revision) {
        revision = itemRevisionRepository.findOne(id);
        PLMItem item = itemRepository.findOne(revision.getItemMaster());
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(item.getItemType().getLifecycle().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = revision.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
        String message = null;
        String mailSubject = null;
        if (index != -1) {
            PLMLifeCyclePhase oldStatus = revision.getLifeCyclePhase();
            index--;
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                revision.setLifeCyclePhase(lifeCyclePhase);
                if (!lifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    revision.setReleased(false);
                    revision.setReleasedDate(null);
                    //item.setLatestRevision(null);
                }
                applicationEventPublisher.publishEvent(new ItemEvents.ItemDemotedEvent(item, revision, oldStatus, lifeCyclePhase));
                revision = itemRevisionRepository.save(revision);
                PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
                statusHistory.setItem(revision.getId());
                statusHistory.setOldStatus(oldStatus);
                statusHistory.setNewStatus(revision.getLifeCyclePhase());
                statusHistory.setUpdatedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                statusHistory = revisionStatusHistoryRepository.save(statusHistory);
                message = item.getItemNumber() + " Demoted lifeCyclePhase by " + sessionWrapper.getSession().getLogin().getPerson().getFullName() + " to ( " + oldStatus.getPhase() + " to " + revision.getLifeCyclePhase().getPhase() + ")";
                mailSubject = item.getItemNumber() + " : " + " Rev " + revision.getRevision() + " : " + revision.getLifeCyclePhase().getPhase() + " Notification";
                sendItemSubscribeNotification(item, message, mailSubject);

            }
        }
        return revision;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getWorkflows(String type) {
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignable(type);
        List<PLMWorkflowDefinition> workflowDefinitions = workFlowDefinitionRepository.findByWorkflowType(workflowType);
        return workflowDefinitions;
    }

    @Transactional
    public PLMWorkflow attachItemWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        PLMWorkflow workflow1 = plmWorkflowRepository.findByAttachedTo(id);
        if (workflow1 != null) {
            workflowService.deleteWorkflow(id);
        }
        if (itemRevision != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.ITEMREVISION, itemRevision.getId(), wfDef);
            itemRevision.setWorkflow(workflow.getId());
            itemRevision = itemRevisionRepository.save(itemRevision);
            applicationEventPublisher.publishEvent(new ItemEvents.ItemWorkflowChangeEvent(itemRevision, workflow1, workflow));
        }
        return workflow;
    }

    /*-----------------  Updating Configurable attributes ------------------*/
    @Transactional
    public List<ItemConfigurableAttributes> updateConfigurableAttributes(Integer itemId) {

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

        List<PLMItemTypeAttribute> configurableAttributes = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(item.getItemType().getId());
        for (PLMItemTypeAttribute configurableAttribute : configurableAttributes) {
            ItemConfigurableAttributes itemConfigurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(itemId, configurableAttribute);
            if (itemConfigurableAttribute == null) {
                ItemConfigurableAttributes attribute = new ItemConfigurableAttributes();
                attribute.setItem(itemId);
                attribute.setAttribute(configurableAttribute);
                attribute.setValues(configurableAttribute.getLov().getValues());

                attribute = itemConfigurableAttributesRepository.save(attribute);
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Map<String, ItemConfigurableAttributes> getConfigurableAttributes(Integer itemId) {
        Map<String, ItemConfigurableAttributes> attributesMap = new LinkedHashMap<>();

        List<ItemConfigurableAttributes> configurableAttributes = itemConfigurableAttributesRepository.findByItemOrderByIdAsc(itemId);
        configurableAttributes.forEach(itemConfigurableAttribute -> {
            PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(itemConfigurableAttribute.getAttribute().getId());
            attributesMap.put(itemTypeAttribute.getName(), itemConfigurableAttribute);
        });

        if (attributesMap.size() > 0) {
            return attributesMap;
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<ItemConfigurableAttributes> getItemConfigurableAttributes(Integer itemId) {

        List<ItemConfigurableAttributes> configurableAttributes = itemConfigurableAttributesRepository.findByItemOrderByIdAsc(itemId);

        return configurableAttributes;
    }

    @Transactional
    public ItemConfigurableAttributes updateItemConfigurableAttributes(ItemConfigurableAttributes itemConfigurableAttribute) {
        return itemConfigurableAttributesRepository.save(itemConfigurableAttribute);
    }

    @Transactional
    public PLMItemManufacturerPart createItemPart(Integer id, PLMItemManufacturerPart part) {
        PLMItemManufacturerPart itemManufacturerPart1 = null;
        if (part.getStatus().equals(ManufacturerPartStatus.PREFERRED)) {
            PLMItemManufacturerPart itemManufacturerPart = itemManufacturerPartRepository.findByItemAndStatus(id, ManufacturerPartStatus.PREFERRED);
            if (itemManufacturerPart != null && itemManufacturerPart.getId() != part.getId()) {
                throw new CassiniException(messageSource.getMessage("item_mfr_Status_already_exists", null, "Item manufacturer part Preferred status already exist", LocaleContextHolder.getLocale()));
            } else {
                part.setItem(id);
                itemManufacturerPart1 = itemManufacturerPartRepository.save(part);
            }
        } else {
            part.setItem(id);
            itemManufacturerPart1 = itemManufacturerPartRepository.save(part);
        }
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        List<PLMItemManufacturerPart> itemManufacturerParts = new ArrayList<>();
        itemManufacturerParts.add(part);
        applicationEventPublisher.publishEvent(new ItemEvents.ItemMfrPartsAddedEvent(itemRevision, itemManufacturerParts));
        return itemManufacturerPart1;
    }

    @Transactional
    public List<PLMItemManufacturerPart> createItemParts(Integer id, List<PLMItemManufacturerPart> parts) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        List<PLMItemManufacturerPart> itemManufacturerParts = new ArrayList<>();
        parts.forEach(part -> {
            if (part.getStatus().equals(ManufacturerPartStatus.PREFERRED)) {
                PLMItemManufacturerPart itemManufacturerPart = itemManufacturerPartRepository.findByItemAndStatus(id, ManufacturerPartStatus.PREFERRED);
                if (itemManufacturerPart != null && !itemManufacturerPart.getId().equals(part.getId())) {
                    throw new CassiniException(messageSource.getMessage("item_mfr_Status_already_exists", null, "Item manufacturer part Preferred status already exist", LocaleContextHolder.getLocale()));
                } else {
                    part.setItem(id);
                    part = itemManufacturerPartRepository.save(part);
                }
            } else {
                part.setItem(id);
                part = itemManufacturerPartRepository.save(part);
            }
            itemManufacturerParts.add(part);
        });

        applicationEventPublisher.publishEvent(new ItemEvents.ItemMfrPartsAddedEvent(itemRevision, itemManufacturerParts));
        return itemManufacturerParts;
    }

    @Transactional
    public PLMItemManufacturerPart updateItemPart(Integer id, PLMItemManufacturerPart part) {
        PLMItemManufacturerPart itemManufacturerPart1 = null;
        if (part.getStatus().equals(ManufacturerPartStatus.PREFERRED)) {
            PLMItemManufacturerPart itemManufacturerPart = itemManufacturerPartRepository.findByItemAndStatus(id, ManufacturerPartStatus.PREFERRED);
            if (itemManufacturerPart != null && itemManufacturerPart.getId() != part.getId()) {
                throw new CassiniException(messageSource.getMessage("item_mfr_Status_already_exists", null, "Item manufacturer part Preferred status already exist", LocaleContextHolder.getLocale()));
            } else {
                part.setItem(id);
                itemManufacturerPart1 = itemManufacturerPartRepository.save(part);
            }
        } else {
            part.setItem(id);
            itemManufacturerPart1 = itemManufacturerPartRepository.save(part);
        }
        return itemManufacturerPart1;
    }

    @Transactional(readOnly = true)
    public List<PLMItemManufacturerPart> getItemMfrParts(Integer id) {
        List<PLMItemManufacturerPart> itemManufacturerParts = itemManufacturerPartRepository.findByItemOrderByModifiedDateDesc(id);
        itemManufacturerParts.forEach(plmItemManufacturerPart -> {
            plmItemManufacturerPart.getManufacturerPart().setMfrName(manufacturerRepository.findOne(plmItemManufacturerPart.getManufacturerPart().getManufacturer()).getName());
        });
        return itemManufacturerParts;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        PLMItemType itemType = itemTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (itemType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, itemType.getParentType(), type);
        }
        return workflowDefinitions;
    }

    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        PLMItemType itemType = itemTypeRepository.findOne(typeId);
        if (itemType != null) {
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
            if (itemType.getParentType() != null) {
                getWorkflowsFromHierarchy(definitions, itemType.getParentType(), type);
            }
        }
    }

    @Transactional
    public List<PLMItemType> updateAllConfigurableAttributes() {
        List<PLMItemType> itemTypes = itemTypeRepository.findAll();

        itemTypes.forEach(plmItemType -> {
            List<PLMItem> items = itemRepository.findByItemTypeAndConfigurableTrue(plmItemType);

            for (PLMItem item : items) {
                List<PLMItemRevision> itemRevisions = itemRevisionRepository.findByItemMasterOrderByIdAsc(item.getId());
                for (PLMItemRevision itemRevision : itemRevisions) {
                    List<PLMItemTypeAttribute> configurableAttributes = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(item.getItemType().getId());
                    for (PLMItemTypeAttribute configurableAttribute : configurableAttributes) {
                        ItemConfigurableAttributes itemConfigurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(itemRevision.getId(), configurableAttribute);
                        if (itemConfigurableAttribute == null) {
                            ItemConfigurableAttributes attribute = new ItemConfigurableAttributes();
                            attribute.setItem(itemRevision.getId());
                            attribute.setAttribute(configurableAttribute);
                            attribute.setValues(configurableAttribute.getLov().getValues());

                            attribute = itemConfigurableAttributesRepository.save(attribute);
                        }
                    }
                }
            }
        });
        return itemTypes;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> getItemsByItemClass(ItemClass itemClass) {
        List<PLMItem> items = new ArrayList<>();

        List<PLMItemType> itemTypes = itemTypeRepository.findByItemClass(itemClass);
        itemTypes.forEach(plmItemType -> {
            items.addAll(itemRepository.findByItemType(plmItemType));
        });

        return items;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> getNormalItemsByItemClass(ItemClass itemClass) {
        List<PLMItem> items = new ArrayList<>();

        List<PLMItemType> itemTypes = itemTypeRepository.findByItemClass(itemClass);
        itemTypes.forEach(plmItemType -> {
            items.addAll(itemRepository.getNormalItemByItemType(plmItemType.getId()));
        });

        return items;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> getReleasedItemsByItemClass(ItemClass itemClass) {
        List<PLMItem> items = new ArrayList<>();
        List<Integer> itemIds = new ArrayList<>();

        List<PLMItemType> itemTypes = itemTypeRepository.findByItemClass(itemClass);
        itemTypes.forEach(plmItemType -> {
            items.addAll(itemRepository.getReleasedNormalAndConfigurableItemByItemType(plmItemType.getId()));
        });

        return items;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> getNormalAndConfigurab1eItemsByItemClass(ItemClass itemClass) {
        List<PLMItem> items = new ArrayList<>();
        List<Integer> itemIds = new ArrayList<>();

        List<PLMItemType> itemTypes = itemTypeRepository.findByItemClass(itemClass);
        itemTypes.forEach(plmItemType -> {
            items.addAll(itemRepository.getNormalAndConfigurableItemByItemType(plmItemType.getId()));
        });

        return items;
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'ITEMREVISION'")
    public void itemWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMItemRevision revision = (PLMItemRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        applicationEventPublisher.publishEvent(new ItemEvents.ItemWorkflowStartedEvent(revision, plmWorkflow));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'ITEMREVISION'")
    public void itemWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMItemRevision revision = (PLMItemRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new ItemEvents.ItemWorkflowPromotedEvent(revision, plmWorkflow, fromStatus, toStatus));

    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'ITEMREVISION'")
    public void itemWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMItemRevision itemRevision = (PLMItemRevision) event.getAttachedToObject();
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'ITEMREVISION'")
    public void itemWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMItemRevision revision = (PLMItemRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new ItemEvents.ItemWorkflowFinishedEvent(revision, plmWorkflow));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'ITEMREVISION'")
    public void itemWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMItemRevision revision = (PLMItemRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ItemEvents.ItemWorkflowHoldEvent(revision, plmWorkflow, fromStatus));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'ITEMREVISION'")
    public void itemWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMItemRevision revision = (PLMItemRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ItemEvents.ItemWorkflowUnholdEvent(revision, plmWorkflow, fromStatus));
    }

    /*
    *
    * Item Specification
    * */
    @Transactional(readOnly = true)
    public List<PGCItemSpecification> getItemSpecifications(Integer decId) {
        return pgcItemSpecificationRepository.getItemSpecifications(decId);
    }

    @Transactional
    public List<PGCItemSpecification> createItemSpecifications(Integer decId, List<PGCItemSpecification> itemSpecifications) {
        itemSpecifications = pgcItemSpecificationRepository.save(itemSpecifications);
        applicationEventPublisher.publishEvent(new ItemEvents.ItemComplianceAddEvent(decId, itemSpecifications));
        return itemSpecifications;
    }

    @Transactional
    public void deleteItemSpecification(Integer itemId, Integer specId) {
        PGCItemSpecification itemSpecification = pgcItemSpecificationRepository.findOne(specId);
        applicationEventPublisher.publishEvent(new ItemEvents.ItemComplianceDeleteEvent(itemId, itemSpecification));
        pgcItemSpecificationRepository.delete(specId);
    }

    @Transactional
    public List<PLMSubstitutePart> createSubstituteParts(Integer id, List<PLMSubstitutePart> substituteParts) {
        PLMBom bom = bomRepository.findOne(id);
        bom.setHasSubstitutes(true);
        bom = bomRepository.save(bom);
        List<PLMSubstitutePart> parts = new ArrayList<>();
        for (PLMSubstitutePart substitutePart : substituteParts) {
            PLMSubstitutePart existPart = substitutePartRepository.findByPartAndReplacementPart(substitutePart.getPart(), substitutePart.getReplacementPart());
            if (existPart == null) {
                parts.add(substitutePartRepository.save(substitutePart));
            }
        }
        return parts;
    }

    @Transactional(readOnly = true)
    public List<SubstitutePartDto> getSubstituteParts(Integer id) {
        List<SubstitutePartDto> parts = new ArrayList<>();
        List<PLMSubstitutePart> substituteParts = substitutePartRepository.findByPart(id);

        substituteParts.forEach(substitutePart -> {
            PLMItem parent = itemRepository.findOne(substitutePart.getParent());
            PLMItemRevision parentRevision = itemRevisionRepository.findOne(parent.getLatestRevision());
            PLMItem replacementItem = itemRepository.findOne(substitutePart.getReplacementPart());
            PLMItemRevision replacementItemRevision = itemRevisionRepository.findOne(replacementItem.getLatestRevision());

            SubstitutePartDto partDto = new SubstitutePartDto();
            partDto.setId(substitutePart.getId());
            partDto.setPart(substitutePart.getPart());
            partDto.setParent(substitutePart.getParent());
            partDto.setReplacementPart(substitutePart.getReplacementPart());

            partDto.setParentRevision(parent.getLatestRevision());
            partDto.setReplacementPartRevision(replacementItem.getLatestRevision());
            partDto.setParentItemName(parent.getItemName());
            partDto.setParentItemNumber(parent.getItemNumber());
            partDto.setParentItemRevision(parentRevision.getRevision());
            partDto.setReplacementPartName(replacementItem.getItemName());
            partDto.setReplacementPartNumber(replacementItem.getItemNumber());
            partDto.setReplacementPartDescription(replacementItem.getDescription());
            partDto.setReplacementPartType(replacementItem.getItemType().getName());
            partDto.setLifeCyclePhase(replacementItemRevision.getLifeCyclePhase());
            partDto.setReplacementRevision(replacementItemRevision.getRevision());

            parts.add(partDto);
        });
        return parts;
    }

    @Transactional
    public void deleteSubstitutePart(Integer itemId, Integer partId) {
        PLMSubstitutePart substitutePart = substitutePartRepository.findOne(partId);
        PLMItem parentItem = itemRepository.findOne(substitutePart.getParent());
        PLMItemRevision parentItemRevision = itemRevisionRepository.findOne(parentItem.getLatestRevision());
        PLMItem item = itemRepository.findOne(substitutePart.getPart());
        PLMBom bom = bomRepository.findByParentAndItem(parentItemRevision, item);

        substitutePartRepository.delete(partId);

        List<PLMSubstitutePart> substituteParts = substitutePartRepository.findByPartAndParent(substitutePart.getPart(), substitutePart.getParent());
        if (substituteParts.size() == 0 && bom != null) {
            bom.setHasSubstitutes(false);
            bom = bomRepository.save(bom);
        }
    }

    @Transactional
    public List<PLMAlternatePart> createAlternateParts(Integer id, List<PLMAlternatePart> alternateParts) {
        List<PLMAlternatePart> parts = new ArrayList<>();
        for (PLMAlternatePart alternatePart : alternateParts) {
            PLMAlternatePart existPart = alternatePartRepository.findByPartAndReplacementPart(alternatePart.getPart(), alternatePart.getReplacementPart());
            if (existPart == null) {
                alternatePart = alternatePartRepository.save(alternatePart);
                PLMItem item = itemRepository.findOne(alternatePart.getPart());
                item.setHasAlternates(true);
                item = itemRepository.save(item);
                if (alternatePart.getDirection().equals(ReplacementType.TWOWAY)) {
                    item = itemRepository.findOne(alternatePart.getReplacementPart());
                    item.setHasAlternates(true);
                    item = itemRepository.save(item);
                    PLMAlternatePart existAlternatePart = alternatePartRepository.findByPartAndReplacementPart(alternatePart.getReplacementPart(), alternatePart.getPart());
                    if (existAlternatePart == null) {
                        PLMAlternatePart plmAlternatePart = new PLMAlternatePart();
                        plmAlternatePart.setPart(alternatePart.getReplacementPart());
                        plmAlternatePart.setReplacementPart(alternatePart.getPart());
                        plmAlternatePart.setDirection(ReplacementType.TWOWAY);
                        plmAlternatePart = alternatePartRepository.save(plmAlternatePart);
                    }
                }
                parts.add(alternatePart);
            }
        }
        return parts;
    }

    @Transactional
    public PLMAlternatePart createAlternatePart(Integer id, PLMAlternatePart alternatePart) {
        List<PLMAlternatePart> parts = new ArrayList<>();
        PLMAlternatePart existPart = alternatePartRepository.findByPartAndReplacementPart(alternatePart.getPart(), alternatePart.getReplacementPart());
        if (existPart == null) {
            parts.add(alternatePartRepository.save(alternatePart));
        } else {
            PLMItem item = itemRepository.findOne(alternatePart.getReplacementPart());
            throw new CassiniException(item.getItemNumber() + " part already exist");
        }
        PLMItem item = itemRepository.findOne(alternatePart.getPart());
        item.setHasAlternates(true);
        item = itemRepository.save(item);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
        if (alternatePart.getDirection().equals(ReplacementType.TWOWAY)) {
            item = itemRepository.findOne(alternatePart.getReplacementPart());
            item.setHasAlternates(true);
            item = itemRepository.save(item);
            PLMAlternatePart existAlternatePart = alternatePartRepository.findByPartAndReplacementPart(alternatePart.getReplacementPart(), alternatePart.getPart());
            if (existAlternatePart == null) {
                PLMAlternatePart plmAlternatePart = new PLMAlternatePart();
                plmAlternatePart.setPart(alternatePart.getReplacementPart());
                plmAlternatePart.setReplacementPart(alternatePart.getPart());
                plmAlternatePart.setDirection(ReplacementType.TWOWAY);
                plmAlternatePart = alternatePartRepository.save(plmAlternatePart);
            }
        }
        applicationEventPublisher.publishEvent(new ItemEvents.ItemAlternatePartsAddedEvent(itemRevision, parts));
        return alternatePart;
    }

    @Transactional
    public PLMAlternatePart updateAlternatePart(Integer id, PLMAlternatePart alternatePart) {
        PLMAlternatePart plmAltPart = JsonUtils.cloneEntity(alternatePartRepository.findOne(alternatePart.getId()), PLMAlternatePart.class);
        List<PLMAlternatePart> parts = new ArrayList<>();
        PLMAlternatePart existPart = alternatePartRepository.findByPartAndReplacementPart(alternatePart.getPart(), alternatePart.getReplacementPart());
        PLMItem plmItem = itemRepository.findOne(alternatePart.getReplacementPart());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
        if (existPart != null && !existPart.getId().equals(alternatePart.getId())) {
            PLMItem item = itemRepository.findOne(alternatePart.getReplacementPart());
            throw new CassiniException(item.getItemNumber() + " part already exist");
        } else {
            alternatePart = alternatePartRepository.save(alternatePart);
            if (alternatePart.getDirection().equals(ReplacementType.ONEWAY)) {
                PLMItem item = itemRepository.findOne(alternatePart.getReplacementPart());
                item.setHasAlternates(false);
                item = itemRepository.save(item);
                PLMAlternatePart existAlternatePart = alternatePartRepository.findByPartAndReplacementPart(alternatePart.getReplacementPart(), alternatePart.getPart());
                if (existAlternatePart != null) {
                    alternatePartRepository.delete(existAlternatePart.getId());
                }
            } else {
                PLMItem item = itemRepository.findOne(alternatePart.getReplacementPart());
                item.setHasAlternates(true);
                item = itemRepository.save(item);
                PLMAlternatePart existAlternatePart = alternatePartRepository.findByPartAndReplacementPart(alternatePart.getReplacementPart(), alternatePart.getPart());
                if (existAlternatePart == null) {
                    PLMAlternatePart plmAlternatePart = new PLMAlternatePart();
                    plmAlternatePart.setPart(alternatePart.getReplacementPart());
                    plmAlternatePart.setReplacementPart(alternatePart.getPart());
                    plmAlternatePart.setDirection(ReplacementType.TWOWAY);
                    plmAlternatePart = alternatePartRepository.save(plmAlternatePart);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ItemEvents.ItemAlternatePartUpdatedEvent(itemRevision, plmAltPart, alternatePart));
        return alternatePart;
    }

    @Transactional(readOnly = true)
    public List<AlternatePartDto> getAlternateParts(Integer id) {
        List<AlternatePartDto> parts = new ArrayList<>();
        List<PLMAlternatePart> alternateParts = alternatePartRepository.findByPartOrderByModifiedDateDesc(id);

        alternateParts.forEach(alternatePart -> {
            PLMItem replacementItem = itemRepository.findOne(alternatePart.getReplacementPart());
            PLMItemRevision replacementItemRevision = itemRevisionRepository.findOne(replacementItem.getLatestRevision());

            AlternatePartDto partDto = new AlternatePartDto();
            partDto.setId(alternatePart.getId());
            partDto.setPart(alternatePart.getPart());
            partDto.setReplacementPart(alternatePart.getReplacementPart());

            partDto.setReplacementPartRevision(replacementItem.getLatestRevision());
            partDto.setReplacementPartName(replacementItem.getItemName());
            partDto.setReplacementPartNumber(replacementItem.getItemNumber());
            partDto.setReplacementPartDescription(replacementItem.getDescription());
            partDto.setReplacementPartType(replacementItem.getItemType().getName());
            partDto.setLifeCyclePhase(replacementItemRevision.getLifeCyclePhase());
            partDto.setReplacementRevision(replacementItemRevision.getRevision());
            partDto.setDirection(alternatePart.getDirection());
            parts.add(partDto);
        });
        return parts;
    }

    @Transactional
    public void deleteAlternatePart(Integer itemId, Integer partId) {
        PLMAlternatePart alternatePart = alternatePartRepository.findOne(partId);
        alternatePartRepository.delete(partId);
        List<PLMAlternatePart> alternateParts = alternatePartRepository.findByPart(itemId);
        PLMItem plmItem = itemRepository.findOne(itemId);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
        if (alternateParts.size() == 0) {
            PLMItem item = itemRepository.findOne(itemId);
            item.setHasAlternates(false);
            item = itemRepository.save(item);
        }

        if (alternatePart.getDirection().equals(ReplacementType.TWOWAY)) {
            PLMAlternatePart twoWayPart = alternatePartRepository.findByPartAndReplacementPart(alternatePart.getReplacementPart(), alternatePart.getPart());
            if (twoWayPart != null) {
                alternatePartRepository.delete(twoWayPart.getId());
                alternateParts = alternatePartRepository.findByPart(twoWayPart.getPart());
                if (alternateParts.size() == 0) {
                    PLMItem item = itemRepository.findOne(twoWayPart.getPart());
                    item.setHasAlternates(false);
                    item = itemRepository.save(item);
                }
            }
        }
         applicationEventPublisher.publishEvent(new ItemEvents.ItemAlternatePartsDeletedEvent(itemRevision,alternateParts));
    }

    @Transactional
    public List<PLMItem> getReleasedItemsByItem(Integer itemId, Integer problemReport) {
        List<PLMBom> bomList = bomRepository.getReleasedBomItemIdsByItem(itemId);
        List<PLMItem> items = new ArrayList<>();
        if (bomList.size() > 0) {
            for (PLMBom bom : bomList) {
                PLMItem item = bom.getItem();
                item.setAsReleasedRevision(bom.getAsReleasedRevision());
                items.add(item);
            }
            items.forEach(item -> {
                PLMItemRevision revision = itemRevisionRepository.findOne(item.getAsReleasedRevision());
                if (revision.getReleased()) {
                    /*--------- To hide added items to problem report ------*/
                    /*if (ids.size() > 0) {
                        Integer count = bomRepository.getReleasedBomItemsCountByItemAndIds(revision.getId(), ids);
                        if (count > 0) {
                            item.setHasBom(true);
                        }
                    } else {
                        Integer count = bomRepository.getReleasedBomItemsCountByItem(revision.getId());
                        if (count > 0) {
                            item.setHasBom(true);
                        }
                    }*/
                    Integer count = bomRepository.getReleasedBomItemsCountByItem(revision.getId());
                    if (count > 0) {
                        item.setHasBom(true);
                    }
                    PQMPRProblemItem problemItem = prProblemItemRepository.findByItemAndProblemReport(item.getAsReleasedRevision(), problemReport);
                    if (problemItem != null) {
                        item.setAlreadyExist(true);
                    }
                }
            });
        }
        return items;
    }

    @Transactional
    public List<ItemDto> getReleasedItems(Boolean hasBom) {
        List<ItemDto> dtoList = new ArrayList<>();
        List<Integer> itemIds = new ArrayList<>();
        if (hasBom) {
            itemIds = itemRevisionRepository.getLatestReleasedItemMasterIdsAndHasBomTrue();
        } else {
            itemIds = itemRevisionRepository.getLatestReleasedItemMasterIds();
        }
        List<PLMItem> items = itemRepository.findByIdIn(itemIds);
        items.forEach(plmItem -> {
            ItemDto itemDto = new ItemDto();
            itemDto.setId(plmItem.getId());
            itemDto.setItemName(plmItem.getItemName());
            itemDto.setItemNumber(plmItem.getItemNumber());
            List<PLMItemRevision> itemRevisions = itemRevisionRepository.findByItemMasterAndReleasedTrue(plmItem.getId());
            itemRevisions.forEach(plmItemRevision -> {
                ItemRevisionDto revisionDto = new ItemRevisionDto();
                revisionDto.setId(plmItemRevision.getId());
                revisionDto.setRevision(plmItemRevision.getRevision());
                revisionDto.setItemMaster(plmItemRevision.getItemMaster());
                itemDto.getItemRevisions().add(revisionDto);
            });
            dtoList.add(itemDto);
        });

        return dtoList;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getItemsByItemType(Pageable pageable, ItemCriteria itemCriteria) {
        Predicate predicate = predicateBuilder.getPredicates(itemCriteria, QPLMItem.pLMItem);
        return itemRepository.findAll(predicate, pageable);
    }

}