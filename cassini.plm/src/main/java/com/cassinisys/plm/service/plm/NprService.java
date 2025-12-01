package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonGroupRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.event.ItemEvents;
import com.cassinisys.plm.event.NprEvents;
import com.cassinisys.plm.filtering.NprCriteria;
import com.cassinisys.plm.filtering.NprPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by GSR on 09-06-2020.
 */
@Service
public class NprService implements CrudService<PLMNpr, Integer> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private NprRepository nprRepository;
    @Autowired
    private NprItemRepository nprItemRepository;
    @Autowired
    private NprFileRepository nprFileRepository;
    @Autowired
    private NprPredicateBuilder nprPredicateBuilder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ItemConfigurableAttributesRepository itemConfigurableAttributesRepository;
    @Autowired
    private ItemRevisionStatusHistoryRepository revisionStatusHistoryRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ItemAttributeRepository itemAttributeRepository;
    @Autowired
    private ItemRevisionAttributeRepository itemRevisionAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#npr,'create')")
    public PLMNpr create(PLMNpr npr) {
        Integer workflowDef = npr.getWorkflow();
        PLMNpr existingNpr = nprRepository.findByNumber(npr.getNumber());
        if (existingNpr != null) {
            throw new CassiniException(messageSource.getMessage("npr_number_already_exist", null, "NPR number already exist", LocaleContextHolder.getLocale()));

        }
        npr.setWorkflow(null);
        AutoNumber autoNumber = autoNumberService.getByName("Default NPR Number Source");
        autoNumberService.saveNextNumber(autoNumber.getId(), npr.getNumber());
        npr = nprRepository.save(npr);
        if (workflowDef != null) {
            addNprWorkflow(npr.getId(), workflowDef);
        }
        applicationEventPublisher.publishEvent(new NprEvents.NprCreatedEvent(npr));
        return npr;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#npr.id ,'edit')")
    public PLMNpr update(PLMNpr npr) {
        PLMNpr oldNpr = JsonUtils.cloneEntity(nprRepository.findOne(npr.getId()), PLMNpr.class);
        npr = nprRepository.save(npr);
        applicationEventPublisher.publishEvent(new NprEvents.NprBasicInfoUpdatedEvent(oldNpr, npr));
        return npr;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        nprRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMNpr get(Integer id) {
        PLMNpr npr = nprRepository.findOne(id);
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(npr.getId());
        if (workflow != null) {
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    npr.setStartWorkflow(true);
                }
            }
        }
        return npr;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMNpr> getAll() {
        return nprRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMNpr> findMultiple(List<Integer> ids) {
        return nprRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMNpr> getAllNprs(Pageable pageable, NprCriteria nprCriteria) {
        Predicate predicate = nprPredicateBuilder.build(nprCriteria, QPLMNpr.pLMNpr);
        Page<PLMNpr> nprs = nprRepository.findAll(predicate, pageable);
        return nprs;
    }

    @Transactional(readOnly = true)
    public List<PLMNprItem> getNPRItems(Integer id) {
        return nprItemRepository.findByNpr(id);
    }

    @Transactional
    public PLMNprItem createNprItem(Integer id, PLMNprItem nprItem) {
        nprItem.setId(null);

        List<ObjectAttribute> objectAttributes = nprItem.getObjectAttributes();
        List<ObjectAttribute> itemRevisionAttributes = nprItem.getRevisionObjectAttributes();
        List<PLMItemAttribute> itemTypeAttributes = nprItem.getItemTypeAttributes();

        Integer workflowDef = null;
        if (nprItem.getWorkflowDefId() != null) {
            workflowDef = nprItem.getWorkflowDefId();
        }

        String itemNumber = autoNumberService.createAutoNumberIfNotExist("Temporary Part Number Source", "Part Number Source", 5, 1, 1, "0", "TEMP-", 1);

        nprItem.setItemNumber(itemNumber);
        Date fromDate = nprItem.getFromDate();
        Date toDate = nprItem.getToDate();

        nprItem = nprItemRepository.save(nprItem);
        PLMItemRevision revision = new PLMItemRevision();
        revision.setItemMaster(nprItem.getId());
        revision.setObjectType(PLMObjectType.ITEMREVISION);
        PLMItemType plmItemType = itemTypeRepository.findOne(nprItem.getItemType().getId());
        Lov lov = plmItemType.getRevisionSequence();
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(plmItemType.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        revision.setLifeCyclePhase(lifeCyclePhase);
        if (plmItemType.getRequiredEco()) {
            revision.setRevision(lov.getDefaultValue());
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
        revision = itemRevisionRepository.save(revision);
        nprItem.setLatestRevision(revision.getId());
        nprItem = nprItemRepository.save(nprItem);


        for (PLMItemAttribute attribute : itemTypeAttributes) {
            PLMItemTypeAttribute qualityTypeAttribute = itemTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
            if (qualityTypeAttribute.getRevisionSpecific()) {
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    PLMItemRevisionAttribute revisionAttribute1 = new PLMItemRevisionAttribute();
                    revisionAttribute1.setId(new ObjectAttributeId(revision.getId(), attribute.getId().getAttributeDef()));
                    revisionAttribute1.setStringValue(attribute.getStringValue());
                    revisionAttribute1.setLongTextValue(attribute.getLongTextValue());
                    revisionAttribute1.setRichTextValue(attribute.getRichTextValue());
                    revisionAttribute1.setIntegerValue(attribute.getIntegerValue());
                    revisionAttribute1.setBooleanValue(attribute.getBooleanValue());
                    revisionAttribute1.setDoubleValue(attribute.getDoubleValue());
                    revisionAttribute1.setDateValue(attribute.getDateValue());
                    revisionAttribute1.setTimeValue(attribute.getTimeValue());
                    revisionAttribute1.setAttachmentValues(attribute.getAttachmentValues());
                    revisionAttribute1.setRefValue(attribute.getRefValue());
                    revisionAttribute1.setCurrencyType(attribute.getCurrencyType());
                    revisionAttribute1.setCurrencyValue(attribute.getCurrencyValue());
                    revisionAttribute1.setTimestampValue(attribute.getTimestampValue());
                    revisionAttribute1.setHyperLinkValue(attribute.getHyperLinkValue());
                    revisionAttribute1.setListValue(attribute.getListValue());
                    revisionAttribute1.setMListValue(attribute.getMListValue());
                    itemRevisionAttributeRepository.save(revisionAttribute1);
                }
            } else {
                if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                        attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                        attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                        attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                    attribute.setId(new ObjectAttributeId(nprItem.getId(), attribute.getId().getAttributeDef()));
                    itemAttributeRepository.save(attribute);
                }
            }
        }

        for (ObjectAttribute attribute : objectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(nprItem.getId(), attribute.getId().getAttributeDef()));
                objectAttributeRepository.save(attribute);
            }
        }
        for (ObjectAttribute attribute : itemRevisionAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(revision.getId(), attribute.getId().getAttributeDef()));
                objectAttributeRepository.save(attribute);
            }
        }

        List<PLMItemTypeAttribute> configurableAttributes = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(nprItem.getItemType().getId());
        for (PLMItemTypeAttribute configurableAttribute : configurableAttributes) {
            ItemConfigurableAttributes itemConfigurableAttribute = new ItemConfigurableAttributes();
            itemConfigurableAttribute.setItem(revision.getId());
            itemConfigurableAttribute.setAttribute(configurableAttribute);
            itemConfigurableAttribute.setValues(configurableAttribute.getLov().getValues());
            itemConfigurableAttribute = itemConfigurableAttributesRepository.save(itemConfigurableAttribute);
        }

        if (nprItem.getWorkflowDefId() != null) {
            attachItemWorkflow(revision.getId(), workflowDef);
        }
        PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
        statusHistory.setItem(revision.getId());
        statusHistory.setOldStatus(revision.getLifeCyclePhase());
        statusHistory.setNewStatus(revision.getLifeCyclePhase());
        statusHistory.setUpdatedBy(nprItem.getCreatedBy());
        statusHistory = revisionStatusHistoryRepository.save(statusHistory);

        /* App events */
        /*applicationEventPublisher.publishEvent(new ItemEvents.ItemCreatedEvent(nprItem, revision));
        applicationEventPublisher.publishEvent(new ItemEvents.ItemRevisionCreatedEvent(nprItem, revision));

        applicationEventPublisher.publishEvent(new PushNotificationEvents.CreateItemNotification(nprItem));*/
        PLMNpr plmNpr = nprRepository.findOne(id);
        List<PLMNprItem> relatedItems = new ArrayList<>();
        relatedItems.add(nprItem);
        applicationEventPublisher.publishEvent(new NprEvents.NPRRequestedItemAddedEvent(plmNpr,relatedItems));


        return nprItem;
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

    @Transactional
    public PLMNprItem updateNprItem(Integer id,Integer itemid, PLMNprItem nprItem) {
        PLMNpr npr = nprRepository.findOne(id);
        PLMNprItem oldAffectedItem = JsonUtils.cloneEntity(nprItemRepository.findOne(itemid), PLMNprItem.class);

//        PLMNprItem oldPlmNprItem = nprItemRepository.findOne(nprItem);
        applicationEventPublisher.publishEvent(new NprEvents.NPRRequestedItemUpdateEvent(npr, oldAffectedItem, nprItem));
        nprItem = nprItemRepository.save(nprItem);
        return nprItem;
    }

    @Transactional
    public void deleteNprItem(Integer id, Integer nprItem) {

        PLMNprItem requestedItem = nprItemRepository.findOne(nprItem);
        PLMNpr npr = nprRepository.findOne(id);
        applicationEventPublisher.publishEvent(new NprEvents.NPRRequestedItemDeletedEvent(npr, requestedItem));
        nprItemRepository.delete(nprItem);
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getNprTabCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setRequestedItems(nprItemRepository.findByNpr(id).size());
        detailsDto.setUnAssignedItems(nprItemRepository.getUnAssignedNprItems(id));
        detailsDto.setItemFiles(nprFileRepository.findByNprAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        return detailsDto;
    }

    @Transactional
    public PLMWorkflow addNprWorkflow(Integer nprId, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMNpr npr = nprRepository.findOne(nprId);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (npr != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.PLMNPR, npr.getId(), wfDef);
            npr.setWorkflow(workflow.getId());
            nprRepository.save(npr);
        }
        return workflow;
    }

    @Transactional
    public PLMNpr submitNpr(Integer id) {
        PLMNpr npr = nprRepository.findOne(id);
        if (npr.getStatus().equals(NPRStatus.OPEN)) {
            npr.setStatus(NPRStatus.PENDING);
        }
        npr = nprRepository.save(npr);
        return npr;
    }

    @Transactional
    public PLMNpr approveNpr(Integer id) {
        PLMNpr npr = nprRepository.findOne(id);
        if (npr.getStatus().equals(NPRStatus.PENDING)) {
            npr.setStatus(NPRStatus.APPROVED);
        }
        npr = nprRepository.save(npr);
        List<PLMNprItem> items = nprItemRepository.findByNpr(id);
        for (PLMNprItem item : items) {
            item.setItemNumber(item.getTemporaryNumber());
            item.setIsTemporary(false);
            item = nprItemRepository.save(item);
        }
        sendNotificationToNprRequester(npr);
        return npr;
    }

    @Transactional
    public PLMNpr rejectNpr(PLMNpr npr) {
        npr.setStatus(NPRStatus.REJECTED);
        npr = nprRepository.save(npr);
        sendNotificationToNprRequester(npr);
        return npr;
    }

    @Transactional
    public PLMNprItem assignItemNumber(Integer id, Integer itemId) {
        PLMNprItem nprItem = nprItemRepository.findOne(itemId);
        String itemNumber = autoNumberService.getNextNumber(nprItem.getItemType().getItemNumberSource().getId());
        /*nprItem.setItemNumber(itemNumber);*/
        nprItem.setTemporaryNumber(itemNumber);
        nprItem.setAssignedNumber(true);
        nprItem = nprItemRepository.save(nprItem);
        return nprItem;
    }

    @Async
    private void sendNotificationToNprRequester(PLMNpr npr) {
        Person person = personRepository.findOne(npr.getRequester());
        PersonGroup group = personGroupRepository.findOne(person.getDefaultGroup());
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));
        URL companyLogo = null;
        Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.LOGO");
        if (preference != null) {
            if (preference.getCustomLogo() != null) {
                URL url1 = ItemService.class
                        .getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                File file = new File(url1.getPath());
                try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                    outputStream.write(preference.getCustomLogo());
                    companyLogo = ItemService.class.getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("host", host);
        model.put("npr", npr);
        model.put("companyLogo", companyLogo);
        model.put("personName", person.getFullName());
        model.put("loginRole", group.getName());
        Mail mail = new Mail();
        mail.setMailTo(person.getEmail());
        if (npr.getStatus().equals(NPRStatus.APPROVED) && person.getEmail() != null) {
            mail.setMailSubject("NPR Approved");
        } else if (npr.getStatus().equals(NPRStatus.REJECTED) && person.getEmail() != null) {
            mail.setMailSubject("NPR Rejected");
        }
        mail.setTemplatePath("email/nprNotification.html");
        mail.setModel(model);
        mailService.sendEmail(mail);

    }

    @Transactional
    public PLMWorkflow attachNprWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMNpr npr = nprRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        PLMWorkflow workflow1 = plmWorkflowRepository.findByAttachedTo(id);
        if (workflow1 != null) {
            workflowService.deleteWorkflow(id);
        }
        if (npr != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.PLMNPR, npr.getId(), wfDef);
            npr.setWorkflow(workflow.getId());
            npr = nprRepository.save(npr);
        }
        return workflow;
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PLMNPR'")
    public void nprWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMNpr npr = (PLMNpr) event.getAttachedToObject();
        npr.setStatus(NPRStatus.PENDING);
        npr = nprRepository.save(npr);
        applicationEventPublisher.publishEvent(new NprEvents.NprWorkflowStartedEvent(npr, event.getPlmWorkflow()));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PLMNPR'")
    public void nprWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMNpr npr = (PLMNpr) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        if (fromStatus.getType().equals(WorkflowStatusType.RELEASED)) {
            npr.setStatus(NPRStatus.APPROVED);
            npr = nprRepository.save(npr);
            sendNotificationToNprRequester(npr);
        } else if (fromStatus.getType().equals(WorkflowStatusType.REJECTED)) {
            if (npr.getRejectReason() == null) {
                throw new CassiniException(messageSource.getMessage("please_enter_npr_reject_reason", null, "Please enter npr reject reason",
                        LocaleContextHolder.getLocale()));
            }
            npr.setStatus(NPRStatus.REJECTED);
            npr = nprRepository.save(npr);
            sendNotificationToNprRequester(npr);
        }
        applicationEventPublisher.publishEvent(new NprEvents.NprWorkflowPromotedEvent(npr, plmWorkflow, fromStatus, toStatus));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PLMNPR'")
    public void nprWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMNpr npr = (PLMNpr) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new NprEvents.NprWorkflowDemotedEvent(npr, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PLMNPR'")
    public void nprWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMNpr npr = (PLMNpr) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        if (fromStatus.getType().equals(WorkflowStatusType.RELEASED)) {
            validateAssignedItemNumbers(npr);
            npr.setStatus(NPRStatus.APPROVED);
            npr = nprRepository.save(npr);
            List<PLMNprItem> items = nprItemRepository.findByNpr(npr.getId());
            for (PLMNprItem item : items) {
                item.setItemNumber(item.getTemporaryNumber());
                item.setIsTemporary(false);
                item = nprItemRepository.save(item);
            }
            sendNotificationToNprRequester(npr);
        } else if (fromStatus.getType().equals(WorkflowStatusType.REJECTED)) {
            if (npr.getRejectReason() == null) {
                throw new CassiniException(messageSource.getMessage("please_enter_npr_reject_reason", null, "Please enter npr reject reason",
                        LocaleContextHolder.getLocale()));
            }
            npr.setStatus(NPRStatus.REJECTED);
            npr = nprRepository.save(npr);
            sendNotificationToNprRequester(npr);
        }
        applicationEventPublisher.publishEvent(new NprEvents.NprWorkflowFinishedEvent(npr, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PLMNPR'")
    public void nprWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMNpr npr = (PLMNpr) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        npr.setStatus(NPRStatus.HOLD);
        npr = nprRepository.save(npr);
        applicationEventPublisher.publishEvent(new NprEvents.NprWorkflowHoldEvent(npr, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PLMNPR'")
    public void nprWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMNpr npr = (PLMNpr) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        npr.setStatus(NPRStatus.PENDING);
        npr = nprRepository.save(npr);
        applicationEventPublisher.publishEvent(new NprEvents.NprWorkflowUnholdEvent(npr, plmWorkflow, fromStatus));
    }

    private void validateAssignedItemNumbers(PLMNpr npr) {
        List<PLMNprItem> items = nprItemRepository.findByNpr(npr.getId());
        items.forEach(nprItem -> {
            if (!nprItem.getAssignedNumber()) {
                throw new CassiniException(messageSource.getMessage("please_assigned_npr_item_numbers", null, "Please assign item numbers before we approve the workflow",
                        LocaleContextHolder.getLocale()));
            }
        });
    }
}
