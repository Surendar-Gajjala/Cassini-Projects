package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.event.*;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.dto.RelatedItemDto;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.RelatedItemsDto;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pqm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by subramanyamreddy on 011 11-Jan -18.
 */
@Service
public class RelatedItemService implements CrudService<PLMRelatedItem, Integer> {

    @Autowired
    private RelatedItemRepository relatedItemRepository;

    @Autowired
    private RelatedItemAttributeRepository relatedItemAttributeRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private RelationshipAttributeRepository relationshipAttributeRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private EMailTemplateConfigRepository eMailTemplateConfigRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ChangeRelatedItemRepository changeRelatedItemRepository;
    @Autowired
    private PRRelatedItemRepository prRelatedItemRepository;
    @Autowired
    private QCRRelatedItemRepository qcrRelatedItemRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private DCRRepository dcrRepository;
    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private ItemInspectionRelatedItemRepository itemInspectionRelatedItemRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private VarianceRepository varianceRepository;

    @Override
    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','relateditem')")
    public PLMRelatedItem create(PLMRelatedItem relatedItem) {
        String addParts = null;
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getFromItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        PLMItemRevision toRevision = itemRevisionRepository.findOne(relatedItem.getToItem().getId());
        PLMItem toItem = itemRepository.findOne(toRevision.getItemMaster());
        addParts = toItem.getItemType().getName() + " - " + toItem.getItemNumber() + " - " + toItem.getItemName() + " : Rev " +
                toRevision.getRevision() + " Related Item added by " + person.getFullName() + " to ( " + item.getItemNumber() + " - " + item.getItemName()
                + " : Rev " + itemRevision.getRevision() + " " + itemRevision.getLifeCyclePhase().getPhase() + " ) Item";
        String mailSubject = item.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
        itemService.updateItem(itemRevision.getId());
        itemService.sendItemSubscribeNotification(item, addParts, mailSubject);
        relatedItem = relatedItemRepository.save(relatedItem);
        List<PLMRelatedItem> relatedItems = new ArrayList<>();
        relatedItems.add(relatedItem);
        applicationEventPublisher.publishEvent(new ItemEvents.ItemRelatedItemsAddedEvent(itemRevision, relatedItems));
        return relatedItem;
    }

    @Override
    public PLMRelatedItem update(PLMRelatedItem relatedItem) {
        return relatedItemRepository.save(relatedItem);
    }

    @Override
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void delete(Integer id) {
        PLMRelatedItem relatedItem = relatedItemRepository.findOne(id);
        String addParts = null;
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getFromItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        PLMItemRevision toRevision = itemRevisionRepository.findOne(relatedItem.getToItem().getId());
        PLMItem toItem = itemRepository.findOne(toRevision.getItemMaster());
        addParts = toItem.getItemType().getName() + " - " + toItem.getItemNumber() + " - " + toItem.getItemName() + " : Rev " +
                toRevision.getRevision() + " Related Item deleted by " + person.getFullName() + " from ( " + item.getItemNumber() + " - " + item.getItemName()
                + " : Rev " + itemRevision.getRevision() + " " + itemRevision.getLifeCyclePhase().getPhase() + " ) Item";
        String mailSubject = item.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
        itemService.sendItemSubscribeNotification(item, addParts, mailSubject);
        applicationEventPublisher.publishEvent(new ItemEvents.ItemRelatedItemDeletedEvent(itemRevision, relatedItem));
        relatedItemRepository.delete(id);
    }

    @Override
    public PLMRelatedItem get(Integer id) {
        return relatedItemRepository.findOne(id);
    }

    @Override
    public List<PLMRelatedItem> getAll() {
        return relatedItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PLMRelatedItem> getRelatedItemsByRelationShip(Integer relationshipId) {
        PLMRelationship relationship = relationshipRepository.findOne(relationshipId);
        List<PLMRelatedItem> relatedItems = relatedItemRepository.findByRelationship(relationship);
        return relatedItems;
    }

    @Transactional(readOnly = true)
    public List<PLMRelatedItem> getRelatedItemsByItem(Integer itemId) {
        List<PLMRelatedItem> relatedItems = relatedItemRepository.findByFromItem(itemId);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        relatedItems.addAll(relatedItemRepository.findByToItem(itemRevision));

        return relatedItems;
    }

    @Transactional(readOnly = true)
    public List<RelatedItemsDto> getRelatedItems(Integer itemId) {
        List<RelatedItemsDto> relatedItemsDtos = new ArrayList<>();

        List<PLMRelatedItem> relatedItems = relatedItemRepository.findByFromItem(itemId);
        HashMap<Integer, RelatedItemsDto> relatedItemMap = new HashMap<>();
        relatedItems.forEach(plmRelatedItem -> {
            RelatedItemsDto relatedItemDto = new RelatedItemsDto();
            relatedItemDto.setId(plmRelatedItem.getId());
            relatedItemDto.setRelationship(plmRelatedItem.getRelationship());

            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmRelatedItem.getFromItem());
            relatedItemDto.setFromItemRevision(itemRevision);

            relatedItemDto.setToItemRevision(plmRelatedItem.getToItem());
            PLMItem item = itemRepository.findOne(plmRelatedItem.getToItem().getItemMaster());
            relatedItemDto.setToItemName(item.getItemName());
            relatedItemDto.setToItemNumber(item.getItemNumber());
            relatedItemDto.setToItemDescription(item.getDescription());
            relatedItemMap.put(plmRelatedItem.getToItem().getId(), relatedItemDto);
            relatedItemsDtos.add(relatedItemDto);
        });

        PLMItemRevision revision = itemRevisionRepository.findOne(itemId);

        relatedItems = relatedItemRepository.findByToItem(revision);

        relatedItems.forEach(plmRelatedItem -> {
            RelatedItemsDto existItem = relatedItemMap.get(plmRelatedItem.getFromItem());
            if (existItem == null) {
                RelatedItemsDto relatedItemDto = new RelatedItemsDto();
                relatedItemDto.setId(plmRelatedItem.getId());
                relatedItemDto.setRelationship(plmRelatedItem.getRelationship());
                relatedItemDto.setFromItem(plmRelatedItem.getFromItem());
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmRelatedItem.getFromItem());
                relatedItemDto.setFromItemRevision(itemRevision);
                PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
                relatedItemDto.setFromItemName(plmItem.getItemName());
                relatedItemDto.setFromItemNumber(plmItem.getItemNumber());
                relatedItemDto.setFromItemDescription(plmItem.getDescription());

                relatedItemDto.setToItemRevision(plmRelatedItem.getToItem());
                PLMItem item = itemRepository.findOne(plmRelatedItem.getToItem().getItemMaster());
                relatedItemDto.setToItemName(item.getItemName());
                relatedItemDto.setToItemNumber(item.getItemNumber());
                relatedItemDto.setToItemDescription(item.getDescription());

                relatedItemsDtos.add(relatedItemDto);
            } else {
                existItem.setBothDirection(true);
            }
        });

        return relatedItemsDtos;
    }

    public List<PLMRelatedItem> saveMultipleRelatedItems(List<PLMRelatedItem> relatedItems) {
        List<PLMRelatedItem> relatedItems1 = new ArrayList<>();
        for (PLMRelatedItem relatedItem : relatedItems) {
            PLMRelatedItem item = new PLMRelatedItem();
            item.setFromItem(relatedItem.getFromItem());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getToItem().getId());
            item.setToItem(itemRevision);
            item.setRelationship(relatedItem.getRelationship());
            relatedItems1.add(relatedItemRepository.save(item));

        }
        return relatedItems1;
    }

    @Transactional
    public List<ObjectAttribute> saveRelatedItemAttributes(List<PLMRelatedItemAttribute> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList();
        for (PLMRelatedItemAttribute attribute : attributes) {
            PLMRelationshipAttribute relationshipAttribute = relationshipAttributeRepository.findOne(attribute.getId().getAttributeDef());
            PLMRelatedItem plmRelatedItem = relatedItemRepository.findOne(attribute.getId().getObjectId());
            PLMRelatedItemAttribute revisionAttribute1 = new PLMRelatedItemAttribute();
            revisionAttribute1.setId(new ObjectAttributeId(plmRelatedItem.getId(), relationshipAttribute.getId()));
            revisionAttribute1.setStringValue(attribute.getStringValue());
            revisionAttribute1.setIntegerValue(attribute.getIntegerValue());
            revisionAttribute1.setLongTextValue(attribute.getLongTextValue());
            revisionAttribute1.setRichTextValue(attribute.getRichTextValue());
            revisionAttribute1.setMListValue(attribute.getMListValue());
            revisionAttribute1.setBooleanValue(attribute.getBooleanValue());
            revisionAttribute1.setDoubleValue((attribute.getDoubleValue()));
            revisionAttribute1.setDateValue(attribute.getDateValue());
            revisionAttribute1.setTimeValue(attribute.getTimeValue());
            revisionAttribute1.setAttachmentValues(attribute.getAttachmentValues());
            revisionAttribute1.setRefValue(attribute.getRefValue());
            revisionAttribute1.setCurrencyType(attribute.getCurrencyType());
            revisionAttribute1.setCurrencyValue(attribute.getCurrencyValue());
            revisionAttribute1.setTimestampValue(attribute.getTimestampValue());
            revisionAttribute1.setListValue(attribute.getListValue());
            PLMRelatedItemAttribute revisionAttribute2 = relatedItemAttributeRepository.save(revisionAttribute1);
            objectAttributes.add(revisionAttribute2);
        }
        return objectAttributes;

    }

    public List<PLMRelatedItemAttribute> getRelatedItemAttributes(Integer itemId) {
        return relatedItemAttributeRepository.findByRelatedItemId(itemId);
    }

    public PLMRelatedItemAttribute createRelatedItemAttribute(PLMRelatedItemAttribute attribute) {
        return relatedItemAttributeRepository.save(attribute);

    }

    public PLMRelatedItemAttribute updateRelatedItemAttribute(PLMRelatedItemAttribute attribute) {
        return relatedItemAttributeRepository.save(attribute);

    }

    private void sendSubscribeNotification(PLMItem item, String message, String mailSubject) {
        List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(item.getId());
        if (subscribes.size() > 0) {
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

    @Transactional(readOnly = true)
    public List<RelatedItemDto> getRelatedItemsByObject(Integer id, PLMObjectType objectType) {
        List<RelatedItemDto> relatedItemDtos = new ArrayList<>();
        if (objectType.equals(PLMObjectType.DCR) || objectType.equals(PLMObjectType.DCO) || objectType.equals(PLMObjectType.ECR) || objectType.equals(PLMObjectType.VARIANCE)) {
            List<PLMChangeRelatedItem> changeRelatedItems = changeRelatedItemRepository.findByChange(id);
            for (PLMChangeRelatedItem changeRelatedItem : changeRelatedItems) {
                RelatedItemDto relatedItemDto = new RelatedItemDto();
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(changeRelatedItem.getItem());
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                relatedItemDto.setItemNumber(item.getItemNumber());
                relatedItemDto.setItemName(item.getItemName());
                relatedItemDto.setObjectId(changeRelatedItem.getId());
                relatedItemDto.setPhase(itemRevision.getLifeCyclePhase().getPhase());
                relatedItemDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
                relatedItemDto.setItemId(itemRevision.getId());
                relatedItemDto.setItemType(item.getItemType().getName());
                relatedItemDto.setRevision(itemRevision.getRevision());
                relatedItemDto.setDescription(item.getDescription());
                relatedItemDtos.add(relatedItemDto);
            }
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            List<PQMPRRelatedItem> relatedItems = prRelatedItemRepository.findByProblemReport(id);
            for (PQMPRRelatedItem relatedItem : relatedItems) {
                RelatedItemDto relatedItemDto = new RelatedItemDto();
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                relatedItemDto.setItemNumber(item.getItemNumber());
                relatedItemDto.setItemName(item.getItemName());
                relatedItemDto.setObjectId(relatedItem.getId());
                relatedItemDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
                relatedItemDto.setItemId(itemRevision.getId());
                relatedItemDto.setItemType(item.getItemType().getName());
                relatedItemDto.setRevision(itemRevision.getRevision());
                relatedItemDto.setDescription(item.getDescription());
                relatedItemDtos.add(relatedItemDto);
            }
        } else if (objectType.equals(PLMObjectType.QCR)) {
            List<PQMQCRRelatedItem> relatedItems = qcrRelatedItemRepository.findByQcr(id);
            relatedItems.forEach(qcrItem -> {
                RelatedItemDto relatedItemDto = new RelatedItemDto();
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(qcrItem.getItem());
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                relatedItemDto.setItemNumber(item.getItemNumber());
                relatedItemDto.setItemName(item.getItemName());
                relatedItemDto.setObjectId(qcrItem.getId());
                relatedItemDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
                relatedItemDto.setItemId(itemRevision.getId());
                relatedItemDto.setItemType(item.getItemType().getName());
                relatedItemDto.setRevision(itemRevision.getRevision());
                relatedItemDto.setDescription(item.getDescription());
                relatedItemDtos.add(relatedItemDto);

            });
        } else if (objectType.equals(PLMObjectType.ITEMINSPECTION)) {
            List<PQMItemInspectionRelatedItem> relatedItems = itemInspectionRelatedItemRepository.findByInspection(id);

            for (PQMItemInspectionRelatedItem relatedItem : relatedItems) {
                RelatedItemDto relatedItemDto = new RelatedItemDto();
            /*	itemsDto.setId(relatedItem.getId());
				itemsDto.setInspection(relatedItem.getInspection());
				itemsDto.setItem(relatedItem.getItem());*/
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                relatedItemDto.setItemNumber(item.getItemNumber());
                relatedItemDto.setItemName(item.getItemName());
                relatedItemDto.setObjectId(relatedItem.getId());
                relatedItemDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
                relatedItemDto.setItemId(itemRevision.getId());
                relatedItemDto.setItemType(item.getItemType().getName());
                relatedItemDto.setRevision(itemRevision.getRevision());
                relatedItemDto.setDescription(item.getDescription());
                relatedItemDtos.add(relatedItemDto);
            }
        }
        return relatedItemDtos;
    }

    @Transactional
    public void deleteRelatedItemByObject(Integer id, PLMObjectType objectType) {
        if (objectType.equals(PLMObjectType.DCO)) {
            PLMChangeRelatedItem relatedItem = changeRelatedItemRepository.findOne(id);
            PLMDCO dco = dcoRepository.findOne(relatedItem.getChange());
            applicationEventPublisher.publishEvent(new DCOEvents.DCORelatedItemDeletedEvent(dco, relatedItem));
            changeRelatedItemRepository.delete(id);
        } else if (objectType.equals(PLMObjectType.DCR)) {
            PLMChangeRelatedItem relatedItem = changeRelatedItemRepository.findOne(id);
            PLMDCR plmdcr = dcrRepository.findOne(relatedItem.getChange());
            applicationEventPublisher.publishEvent(new DCREvents.DCRRelatedItemDeletedEvent(plmdcr, relatedItem));
            changeRelatedItemRepository.delete(id);
        } else if (objectType.equals(PLMObjectType.ECR)) {
            PLMChangeRelatedItem relatedItem = changeRelatedItemRepository.findOne(id);
            PLMECR plmecr = ecrRepository.findOne(relatedItem.getChange());
            applicationEventPublisher.publishEvent(new ECREvents.ECRRelatedItemsDeletedEvent(plmecr, relatedItem));
            changeRelatedItemRepository.delete(id);
        } else if (objectType.equals(PLMObjectType.VARIANCE)) {
            PLMChangeRelatedItem relatedItem = changeRelatedItemRepository.findOne(id);
            PLMVariance variance = varianceRepository.findOne(relatedItem.getChange());
            applicationEventPublisher.publishEvent(new VarianceEvents.VarianceChangeRelatedDeletedEvent(variance, relatedItem));
            changeRelatedItemRepository.delete(id);
        } else if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            PQMPRRelatedItem relatedItem = prRelatedItemRepository.findOne(id);
            PQMProblemReport problemReport = problemReportRepository.findOne(relatedItem.getProblemReport());
            applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportRelatedItemDeletedEvent(problemReport, relatedItem));
            prRelatedItemRepository.delete(relatedItem);
        } else if (objectType.equals(PLMObjectType.QCR)) {
            PQMQCRRelatedItem relatedItem = qcrRelatedItemRepository.findOne(id);
            PQMQCR pqmqcr = qcrRepository.findOne(relatedItem.getQcr());
            applicationEventPublisher.publishEvent(new QCREvents.QCRRelatedItemDeletedEvent(pqmqcr, relatedItem));
            qcrRelatedItemRepository.delete(id);
        } 
        else if (objectType.equals(PLMObjectType.ITEMINSPECTION)) {
            PQMItemInspectionRelatedItem item = itemInspectionRelatedItemRepository.findOne(id);
            PQMInspection inspection = inspectionRepository.findOne(item.getInspection());
			applicationEventPublisher.publishEvent(new InspectionEvents.ItemInspectionRelatedItemDeletedEvent(inspection, item));
            itemInspectionRelatedItemRepository.delete(id);
        }

    }
}
