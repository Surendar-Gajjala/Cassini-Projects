package com.cassinisys.plm.service.mfr;

import com.cassinisys.platform.events.dto.ASVersionedFileDTO;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.model.custom.CustomObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.custom.CustomObjectRepository;
import com.cassinisys.platform.repo.custom.CustomObjectTypeRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.event.SupplierEvents;
import com.cassinisys.plm.filtering.PLMSupplierCriteria;
import com.cassinisys.plm.filtering.PLMSupplierPredicateBuilder;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.dto.SubscribeMailDto;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.pgc.PGCDeclaration;
import com.cassinisys.plm.model.pgc.PGCDeclarationPart;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMSubscribe;
import com.cassinisys.plm.model.pqm.PQMPPAP;
import com.cassinisys.plm.model.pqm.PQMSupplierAudit;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditPlan;
import com.cassinisys.plm.model.pqm.SupplierAuditDetailsDto;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.pgc.PGCDeclarationPartRepository;
import com.cassinisys.plm.repo.pgc.PGCDeclarationRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.plm.SubscribeRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.PPAPRepository;
import com.cassinisys.plm.repo.pqm.PQMSupplierAuditPlanRepository;
import com.cassinisys.plm.repo.pqm.PQMSupplierAuditReviewerRepository;
import com.cassinisys.plm.repo.pqm.SupplierAuditRepository;
import com.cassinisys.plm.service.activitystream.SupplierActivityStream;
import com.cassinisys.plm.service.activitystream.dto.ASContactToSupplier;
import com.cassinisys.plm.service.activitystream.dto.ASNewFileDTO;
import com.cassinisys.plm.service.activitystream.dto.ASNewMfrPartDTO;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.pgc.SubstanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Service
public class SupplierService implements CrudService<PLMSupplier, Integer> {
    @Autowired
    private SupplierTypeRepository supplierTypeRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private PLMSupplierPredicateBuilder supplierPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SupplierAttributeRepository supplierAttributeRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private SupplierTypeAttributeRepository supplierTypeAttributeRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SupplierFileRepository supplierFileRepository;
    @Autowired
    private SupplierContactRepository supplierContactRepository;
    @Autowired
    private SupplierPartRepository supplierPartRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private PGCDeclarationRepository declarationRepository;
    @Autowired
    private PGCDeclarationPartRepository declarationPartRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private SubstanceService substanceService;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SupplierActivityStream supplierActivityStream;
    @Autowired
    private PPAPRepository ppapRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PQMSupplierAuditPlanRepository supplierAuditPlanRepository;
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;
    @Autowired
    private PQMSupplierAuditReviewerRepository supplierAuditReviewerRepository;
    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private CustomObjectTypeRepository customObjectTypeRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#supplier,'create')")
    public PLMSupplier create(PLMSupplier supplier) {
        List<PLMSupplierAttribute> supplierAttributes = supplier.getSupplierAttributes();
        List<ObjectAttribute> objectAttributes = supplier.getObjectAttributes();
        PLMSupplier existingSupplier = supplierRepository.findByNameEqualsIgnoreCase(supplier.getName());
        PLMSupplier existSupplierNumber = supplierRepository.findByNumber(supplier.getNumber());
        if (existingSupplier != null) {
            throw new CassiniException(messageSource.getMessage("supplier_already_exist", null,
                    "Supplier name already exist", LocaleContextHolder.getLocale()));

        }
        if (existSupplierNumber != null) {
            String message = messageSource.getMessage("supplier_number_already_exists", null, "{0} Supplier Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existSupplierNumber.getNumber());
            throw new CassiniException(result);
        }
        autoNumberService.saveNextNumber(supplier.getSupplierType().getAutoNumberSource().getId(), supplier.getNumber());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository
                .findByLifeCycleOrderByIdAsc(supplier.getSupplierType().getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        supplier.setLifeCyclePhase(lifeCyclePhase);
        supplier = supplierRepository.save(supplier);

        saveSupplierObjectAttributes(supplier.getId(), supplierAttributes);
        substanceService.saveObjectAttributes(supplier.getId(), objectAttributes);
        applicationEventPublisher.publishEvent(new SupplierEvents.SupplierCreatedEvent(supplier));
        return supplier;
    }

    public void saveSupplierObjectAttributes(Integer id, List<PLMSupplierAttribute> supplierAttributes) {
        for (PLMSupplierAttribute attribute : supplierAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null
                    || attribute.getRefValue() != null || attribute.getListValue() != null
                    || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0
                    || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null)
                    || attribute.getDateValue() != null || attribute.getDoubleValue() != null
                    || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null
                    || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null
                    || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(id, attribute.getId().getAttributeDef()));
                supplierAttributeRepository.save(attribute);
            }
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#supplier.id ,'edit')")
    public PLMSupplier update(PLMSupplier supplier) {
        PLMSupplier oldSupplier = JsonUtils.cloneEntity(supplierRepository.findOne(supplier.getId()),
                PLMSupplier.class);
        PLMSupplier existingSupplier = supplierRepository.findByNameEqualsIgnoreCase(supplier.getName());
        if (existingSupplier != null && !existingSupplier.getId().equals(supplier.getId())) {
            throw new CassiniException(messageSource.getMessage("supplier_already_exist", null,
                    "Supplier name already exist", LocaleContextHolder.getLocale()));

        } else {
            supplier = supplierRepository.save(supplier);
        }
        try {
            sendSupplierSubscribeNotification(supplier,
                    supplierActivityStream.getSupplierBasicInfoUpdatedJson(oldSupplier, supplier), "basic");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        applicationEventPublisher.publishEvent(new SupplierEvents.SupplierBasicInfoUpdatedEvent(oldSupplier, supplier));
        return supplier;
    }

    @Override
    @PreAuthorize("hasPermission(#supplierId,'delete')")
    public void delete(Integer supplierId) {
        supplierRepository.delete(supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMSupplier get(Integer supplierId) {
        PLMSupplier supplier = supplierRepository.findOne(supplierId);
        return supplier;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMSupplier> getAll() {
        return supplierRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PLMSupplier> findMultiple(List<Integer> ids) {
        return supplierRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<PLMSupplier> getApprovedSuppliers() {
        return supplierRepository.getSuppliersByLifecyclePhase(LifeCyclePhaseType.RELEASED);
    }

    @Transactional
    public void saveSupplierAttributes(List<PLMSupplierAttribute> attributes) {
        for (PLMSupplierAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null
                    || attribute.getRefValue() != null || attribute.getListValue() != null
                    || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0
                    || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null)
                    || attribute.getDateValue() != null || attribute.getDoubleValue() != null
                    || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null
                    || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null
                    || attribute.getBooleanValue()) {
                supplierAttributeRepository.save(attribute);
            }
        }
    }

    @Transactional
    public PLMSupplier saveImageAttributeValue(Integer objectId, Integer attributeId,
                                               Map<String, MultipartFile> fileMap) {
        PLMSupplier supplier = supplierRepository.findOne(objectId);
        if (supplier != null) {
            PLMSupplierAttribute supplierAttribute = new PLMSupplierAttribute();
            supplierAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, supplierAttribute);

        }

        return supplier;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PLMSupplierAttribute updateSupplierAttribute(PLMSupplierAttribute attribute) {
        PLMSupplierAttribute oldValue = supplierAttributeRepository
                .findBySupplierAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PLMSupplierAttribute.class);
        PLMSupplierTypeAttribute mesObjectTypeAttribute = supplierTypeAttributeRepository
                .findOne(attribute.getId().getAttributeDef());

        attribute = supplierAttributeRepository.save(attribute);
        /* App events */
        PLMSupplier supplier = supplierRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher
                .publishEvent(new SupplierEvents.SupplierAttributesUpdatedEvent(supplier, oldValue, attribute));
        return attribute;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMSupplier> getAllSuppliersByPageable(Pageable pageable, PLMSupplierCriteria criteria) {
        Predicate predicate = supplierPredicateBuilder.build(criteria, QPLMSupplier.pLMSupplier);
        Page<PLMSupplier> suppliers = supplierRepository.findAll(predicate, pageable);
        for (PLMSupplier supplier : suppliers.getContent()) {
            List<PGCDeclaration> pgcDeclarations = declarationRepository.findBySupplier(supplier.getId());
            if (pgcDeclarations.size() > 0) {
                supplier.setUsedSupplier(true);
            }
        }
        return suppliers;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMSupplier> getSuppliersByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PLMSupplierType type = supplierTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return supplierRepository.getBySupplierTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PLMSupplierType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PLMSupplierType> children = supplierTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (PLMSupplierType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional(readOnly = true)
    public DetailsCount getSupplierDetails(Integer supplierId) {
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setFiles(supplierFileRepository
                .findBySupplierAndFileTypeAndLatestTrueOrderByModifiedDateDesc(supplierId, "FILE").size());
        detailsCount
                .setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(supplierId));
        detailsCount.setContacts(supplierContactRepository.findBySupplier(supplierId).size());
        detailsCount.setParts(supplierPartRepository.findBySupplierOrderByModifiedDateDesc(supplierId).size());
        detailsCount.setPpaps(ppapRepository.findBySupplier(supplierId).size());
        detailsCount.setSupplierAudits(supplierAuditPlanRepository.getPlanCountBySupplier(supplierId));
        CustomObjectType customObjectType = customObjectTypeRepository.findByNameEqualsIgnoreCase("Supplier Performance Rating");
        if (customObjectType != null)
            detailsCount.setSprCount(customObjectRepository.getCustomObjectCountBySupplierAndType(supplierId, customObjectType.getId()));
        customObjectType = customObjectTypeRepository.findByNameEqualsIgnoreCase("CPI Form");
        if (customObjectType != null)
            detailsCount.setCpiCount(customObjectRepository.getCustomObjectCountBySupplierAndType(supplierId, customObjectType.getId()));
        customObjectType = customObjectTypeRepository.findByNameEqualsIgnoreCase("4MChange-Supplier");
        if (customObjectType != null)
            detailsCount.setFmChangeCount(customObjectRepository.getCustomObjectCountBySupplierAndType(supplierId, customObjectType.getId()));
        return detailsCount;
    }

    @Transactional(readOnly = true)
    public List<PLMSupplierContact> getSupplierContacts(Integer supplierId) {
        List<PLMSupplierContact> contacts = supplierContactRepository.findBySupplier(supplierId);
        for (PLMSupplierContact contact : contacts) {
            List<PGCDeclaration> alreadyUsedContacts = declarationRepository.findBySupplierAndContact(supplierId,
                    contact.getId());
            if (alreadyUsedContacts.size() > 0) {
                contact.setUsedContact(true);
            }
            Person person = personRepository.findOne(contact.getContact());
            contact.setPerson(person);
            PLMSupplier supplier = supplierRepository.findOne(contact.getSupplier());
            contact.setSupplierObject(supplier);
        }

        return contacts;
    }

    @Transactional(readOnly = true)
    public List<PLMSupplierContact> getSupplierActiveContacts(Integer supplierId) {
        List<PLMSupplierContact> contacts = supplierContactRepository.findBySupplierAndActiveTrue(supplierId);
        for (PLMSupplierContact contact : contacts) {
            List<PGCDeclaration> alreadyUsedContacts = declarationRepository.findBySupplierAndContact(supplierId,
                    contact.getId());
            if (alreadyUsedContacts.size() > 0) {
                contact.setUsedContact(true);
            }
            Person person = personRepository.findOne(contact.getContact());
            contact.setPerson(person);
            PLMSupplier supplier = supplierRepository.findOne(contact.getSupplier());
            contact.setSupplierObject(supplier);
        }

        return contacts;
    }

    @Transactional
    public PLMSupplierContact createContactSupplier(PLMSupplierContact contact) throws JsonProcessingException {

        PLMSupplier supplier = supplierRepository.findOne(contact.getSupplier());
        Person person = null;
        if (contact.getNewPerson()) {
            Person existPerson1 = this.personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(
                    contact.getPerson().getFirstName(), contact.getPerson().getLastName());
            if (existPerson1 != null) {
                String message = messageSource.getMessage("full_name_already_exist", null, "{0} : full name already exist",
                        LocaleContextHolder.getLocale());
                String name = existPerson1.getFirstName();
                if (contact.getPerson().getLastName() != null && !contact.getPerson().getLastName().isEmpty()) {
                    name = name + " " + contact.getPerson().getLastName();
                }
                String result = MessageFormat.format(message + ".", name);
                throw new CassiniException(result);
            }
            if (contact.getPerson().getPersonType() == null) {
                contact.getPerson().setPersonType(1);
            }
            person = personService.create(contact.getPerson());
            contact.setContact(person.getId());
        } else {
            person = personRepository.findOne(contact.getContact());
        }
        PLMSupplierContact existingContact = supplierContactRepository.findByContactAndSupplier(contact.getContact(),
                contact.getSupplier());
        if (existingContact != null) {
            throw new CassiniException(messageSource.getMessage("supplier_contact_already_exist", null,
                    "Supplier contact already exist", LocaleContextHolder.getLocale()));

        } else {
            contact = supplierContactRepository.save(contact);
        }
        applicationEventPublisher.publishEvent(new SupplierEvents.SupplierContactCreatedEvent(supplier, person));
        sendSupplierSubscribeNotification(supplier, supplierActivityStream.getContactAddedJson(person, supplier),
                "contactAdded");
        return contact;
    }

    @Transactional
    public PLMSupplierContact updateSupplierContact(PLMSupplierContact contact) {
        PLMSupplierContact oldContact = JsonUtils.cloneEntity(supplierContactRepository.findOne(contact.getId()),
                PLMSupplierContact.class);
        PLMSupplierContact existingContact = supplierContactRepository.findByContactAndSupplier(contact.getContact(),
                contact.getSupplier());
        if (existingContact != null && !existingContact.getId().equals(contact.getId())) {
            throw new CassiniException(messageSource.getMessage("supplier_contact_already_exist", null,
                    "Supplier contact already exist", LocaleContextHolder.getLocale()));

        } else {
            personService.update(contact.getPerson());
            contact = supplierContactRepository.save(contact);
        }
        // applicationEventPublisher.publishEvent(new
        // SupplierEvents.SupplierContactBasicInfoUpdatedEvent(oldContact, contact));
        return contact;
    }

    @Transactional(readOnly = true)
    public PLMSupplierContact getContact(Integer contact) {
        return supplierContactRepository.findOne(contact);
    }

    public void deleteContact(Integer contactId) throws JsonProcessingException {
        PLMSupplierContact contact = supplierContactRepository.findOne(contactId);
        PLMSupplier supplier = supplierRepository.findOne(contact.getSupplier());
        Person person = personRepository.findOne(contact.getContact());
        applicationEventPublisher.publishEvent(new SupplierEvents.SupplierContactDeletedEvent(supplier, person));
        sendSupplierSubscribeNotification(supplier, person.getFullName(),
                "contactDeleted");
        supplierContactRepository.delete(contactId);
    }

    /*
     * Supplier Parts
     *
     */

    @Transactional(readOnly = true)
    public List<PLMSupplierPart> getSupplierMfrParts(Integer id) {
        List<PLMSupplierPart> supplierParts = supplierPartRepository.findBySupplierOrderByModifiedDateDesc(id);
        supplierParts.forEach(plmItemManufacturerPart -> {
            plmItemManufacturerPart.getManufacturerPart().setMfrName(manufacturerRepository
                    .findOne(plmItemManufacturerPart.getManufacturerPart().getManufacturer()).getName());
        });
        supplierParts.forEach(supplierPart -> {
            List<PGCDeclarationPart> declaration = declarationPartRepository
                    .getDeclarationPartByPart(supplierPart.getManufacturerPart().getId());
            if (declaration.size() > 0) {
                supplierPart.setAssignedDeclarationPart(true);
            } else {
                supplierPart.setAssignedDeclarationPart(false);
            }
        });

        return supplierParts;
    }

    @Transactional
    public List<PLMSupplierPart> createSupplierParts(List<PLMSupplierPart> parts) throws JsonProcessingException {
        List<PLMSupplierPart> mfrParts = supplierPartRepository.save(parts);
        List<PLMManufacturerPart> partElements = new ArrayList<>();
        for (PLMSupplierPart part : mfrParts) {
            partElements.add(part.getManufacturerPart());
        }
        PLMSupplier supplier = supplierRepository.findOne(mfrParts.get(0).getSupplier());
        applicationEventPublisher.publishEvent(new SupplierEvents.SupplierPartAddEvent(supplier, partElements));
        sendSupplierSubscribeNotification(supplier, supplierActivityStream.getPartAddedJson(partElements, supplier),
                "partsAdded");
        return mfrParts;

    }

    @Transactional
    public PLMSupplierPart createSupplierPart(PLMSupplierPart part) throws JsonProcessingException {
        PLMSupplierPart existPart = supplierPartRepository.findBySupplierAndManufacturerPart(part.getSupplier(),
                part.getManufacturerPart());
        List<PLMManufacturerPart> parts = new ArrayList<>();
        if (existPart == null) {
            part = supplierPartRepository.save(part);
            parts.add(part.getManufacturerPart());
        } else {
            throw new CassiniException("Supplier part already exist");
        }
        PLMSupplier supplier = supplierRepository.findOne(part.getSupplier());
        part.getManufacturerPart()
                .setMfrName(manufacturerRepository.findOne(part.getManufacturerPart().getManufacturer()).getName());

        applicationEventPublisher.publishEvent(new SupplierEvents.SupplierPartAddEvent(supplier, parts));
        sendSupplierSubscribeNotification(supplier, supplierActivityStream.getPartAddedJson(parts, supplier),
                "partsAdded");

        return part;
    }

    @Transactional
    public PLMSupplierPart updateSupplierPart(PLMSupplierPart part) throws JsonProcessingException {
        PLMSupplierPart oldPart = JsonUtils.cloneEntity(supplierPartRepository.findOne(part.getId()),
                PLMSupplierPart.class);
        PLMSupplier supplier = supplierRepository.findOne(part.getSupplier());
        part.setModifiedDate(new Date());
        part = supplierPartRepository.save(part);
        applicationEventPublisher
                .publishEvent(new SupplierEvents.SupplierPartUpdatedEvent(part.getSupplier(), oldPart, part));
        String messageJSon = supplierActivityStream.getSupplierPartUpdatedJson(oldPart, part);
        if (!messageJSon.equals("")) {
            sendSupplierSubscribeNotification(supplier, messageJSon, "partsUpdated");
        }

        return part;
    }

    @Transactional
    public void deleteSupplierMfrPart(Integer part) throws JsonProcessingException {
        PLMSupplierPart supplierPart = supplierPartRepository.findOne(part);
        List<PGCDeclaration> pgcDeclarations = declarationRepository.findBySupplier(supplierPart.getSupplier());
        if (pgcDeclarations.size() > 0) {
            for (PGCDeclaration dec : pgcDeclarations) {
                // check part already existed
                PGCDeclarationPart declarationPart = declarationPartRepository.findByDeclarationAndPart(dec.getId(),
                        supplierPart.getManufacturerPart());
                if (declarationPart != null) {
                    throw new CassiniException(messageSource.getMessage("supplier_part_already_exist_in_declaration",
                            null, "Supplier part already in use", LocaleContextHolder.getLocale()));
                }
            }
        }

        List<PQMPPAP> pqmppap = ppapRepository.findBySupplierPart(part);
        if (pqmppap.size() > 0) {
            throw new CassiniException(messageSource.getMessage("supplier_part_already_exist_in_ppap",
            null, "Supplier part already in use! We can't delete", LocaleContextHolder.getLocale()));
    }
    
        PLMSupplierPart existingPart = supplierPartRepository.findOne(part);
        PLMSupplier supplier = supplierRepository.findOne(existingPart.getSupplier());
        sendSupplierSubscribeNotification(supplier, existingPart.getPartNumber(),
                "partsDeleted");
        supplierPartRepository.delete(part);

    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'promote','mfrsupplier')")
    public PLMSupplier promoteSupplier(Integer id, PLMSupplier oldSupplier) {
        PLMSupplier supplier = supplierRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(supplier.getSupplierType().getLifecycle().getId());
        List<PLMLifeCyclePhase> plmLifeCyclePhases = lifeCyclePhaseRepository
                .findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = supplier.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = plmLifeCyclePhases.stream()
                .filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).findFirst().get();
        Integer index = plmLifeCyclePhases.indexOf(lifeCyclePhase2);
        index++;
        setLifeCyclePhases(index, supplier, oldSupplier, plmLifeCyclePhases);
        // applicationEventPublisher.publishEvent(new
        // ManufacturerEvents.ManufacturerPromotedEvent(manufacturer,
        // manufacturer.getLifeCyclePhase(), plmManufacturer.getLifeCyclePhase()));
        return supplier;
    }

    public void setLifeCyclePhases(Integer index, PLMSupplier newSupplier, PLMSupplier oldSupplier,
                                   List<PLMLifeCyclePhase> plmLifeCyclePhases) {
        if (index != -1) {
            PLMLifeCyclePhase oldStatus = oldSupplier.getLifeCyclePhase();
            PLMLifeCyclePhase lifeCyclePhase = plmLifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                newSupplier.setLifeCyclePhase(lifeCyclePhase);
                newSupplier = supplierRepository.save(newSupplier);
            }
        }
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'demote','mfrsupplier')")
    public PLMSupplier demoteSupplier(Integer id, PLMSupplier oldSupplier) {
        PLMSupplier newSupplier = supplierRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(newSupplier.getSupplierType().getLifecycle().getId());
        List<PLMLifeCyclePhase> plmLifeCyclePhases = lifeCyclePhaseRepository
                .findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = newSupplier.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = plmLifeCyclePhases.stream()
                .filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).findFirst().get();
        Integer index = plmLifeCyclePhases.indexOf(lifeCyclePhase2);
        index--;
        setLifeCyclePhases(index, newSupplier, oldSupplier, plmLifeCyclePhases);
        // applicationEventPublisher.publishEvent(new
        // ManufacturerEvents.ManufacturerDemotedEvent(manufacturer,
        // manufacturer.getLifeCyclePhase(), plmManufacturer.getLifeCyclePhase()));
        return newSupplier;
    }

    @Transactional
    public PLMSubscribe createSubscribeSupplier(Integer supplierId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMSubscribe subscribe = subscribeRepository.findByPersonAndObjectId(person, supplierId);
        PLMSupplier plmSupplier = supplierRepository.findOne(supplierId);
        if (subscribe == null) {
            subscribe = new PLMSubscribe();
            subscribe.setPerson(person);
            subscribe.setObjectId(supplierId);
            subscribe.setObjectType(plmSupplier.getObjectType().name());
            subscribe.setSubscribe(true);
            subscribe = subscribeRepository.save(subscribe);
            // applicationEventPublisher.publishEvent(new
            // ItemEvents.ItemSubscribeEvent(itemRevision, item));
        } else {
            if (subscribe.getSubscribe()) {
                subscribe.setSubscribe(false);
                subscribe = subscribeRepository.save(subscribe);
                // applicationEventPublisher.publishEvent(new
                // ItemEvents.ItemUnSubscribeEvent(itemRevision, item));
            } else {
                subscribe.setSubscribe(true);
                subscribe = subscribeRepository.save(subscribe);
                // applicationEventPublisher.publishEvent(new
                // ItemEvents.ItemSubscribeEvent(itemRevision, item));
            }
        }
        return subscribe;
    }

    /*
     * Send Notification to User when made any changes in supplier
     */

    public void sendSupplierSubscribeNotification(PLMSupplier supplier, String messageJson, String type)
            throws JsonProcessingException {
        List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(supplier.getId());
        String[] recipientAddress = new String[subscribes.size()];
        String email = "";
        if (subscribes.size() > 0) {
            for (int i = 0; i < subscribes.size(); i++) {
                PLMSubscribe subscribe = subscribes.get(i);
                if (email == "")
                    email = subscribe.getPerson().getEmail();
                else
                    email = email + "," + subscribe.getPerson().getEmail();
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
            SubscribeMailDto subscribeMailDto = getMessageAndSubject(supplier, messageJson, type);
            final String messageContent = subscribeMailDto.getMessage();
            final String finalMailSubject = subscribeMailDto.getMailSubject();
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("message", messageContent);
                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);
                mail.setMailSubject(finalMailSubject);
                mail.setTemplatePath("email/subscribeNotification.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    /*
     * 
     * Message creation for all events in Supplier Subscription
     */

    public SubscribeMailDto getMessageAndSubject(PLMSupplier supplier, String messageJson, String type)
            throws JsonProcessingException {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        SubscribeMailDto subscribeMailDto = new SubscribeMailDto();
        List<String> names = new ArrayList<>();
        String message;
        String mailSubject;
        String arrayNames[];
        switch (type) {
            case "basic":
                messageJson = messageJson.replaceAll("\\[|\\]", "");
                ASPropertyChangeDTO changeDTO = objectMapper.readValue(messageJson, ASPropertyChangeDTO.class);
                message = person.getFullName().trim() + " has updated property " + changeDTO.getProperty() + " from "
                        + changeDTO.getOldValue() + " to " + changeDTO.getNewValue() + " of supplier "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - Basic Information updated : Notification";
                break;
            case "contactAdded":
                ASContactToSupplier supplierContact = objectMapper.readValue(messageJson,
                        new TypeReference<ASContactToSupplier>() {
                        });
                message = person.getFullName().trim() + " has added contact " + supplierContact.getContact()
                        + " of supplier "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - Contact added : Notification";
                break;
            case "contactDeleted":
                message = person.getFullName() + "has deleted contact " + messageJson + " of supplier : "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - Contact deleted : Notification";
                break;
            case "partsAdded":
                objectMapper.readValue(messageJson, new TypeReference<List<ASNewMfrPartDTO>>() {
                }).forEach(f -> names.add(f.getPartNumber()));
                message = person.getFullName().trim() + " has added part(s) " + names + " of supplier "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - Part added : Notification";
                break;

            case "partsUpdated":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has updated part " + arrayNames[0] + " to " + arrayNames[1]
                        + "of supplier " + supplier.getName();
                mailSubject = supplier.getName() + " - Part updated : Notification";
                break;

            case "partsDeleted":
                message = person.getFullName() + "has deleted part " + messageJson + " of supplier : "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - Part deleted : Notification";
                break;

            case "fileAdded":
                objectMapper.readValue(messageJson, new TypeReference<List<ASNewFileDTO>>() {
                }).forEach(f -> names.add(f.getName()));
                message = person.getFullName().trim() + " has added new file(s) " + names + " of supplier "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - File added : Notification";
                break;
            case "fileDeleted":
                message = person.getFullName() + "has deleted file " + messageJson + " of supplier : "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - File deleted : Notification";
                break;
            case "fileVersioned":
                message = person.getFullName() + "has updated file(s) " + messageJson + " of supplier : "
                        + supplier.getName();
                final String[] s = {""};
                objectMapper.readValue(messageJson, new TypeReference<List<ASVersionedFileDTO>>() {
                }).forEach(f -> {
                    s[0] = s[0] + f.getName() + "from version " + f.getOldVersion() + " to " + f.getNewVersion();
                });
                message = message + s[0];
                mailSubject = supplier.getName() + " - File updated : Notification";
                break;
            case "fileRename":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has renamed from " + arrayNames[0] + " to " + arrayNames[1]
                        + " of supplier " + supplier.getName();
                mailSubject = supplier.getName() + " - File renamed : Notification";
                break;
            case "fileReplace":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has replace from " + arrayNames[0] + " to " + arrayNames[1]
                        + " of supplier " + supplier.getName();
                mailSubject = supplier.getName() + " - File replace : Notification";
                break;
            case "fileLocked":
                message = person.getFullName().trim() + " has locked file " + messageJson + " of supplier "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - File locked : Notification";
                break;
            case "fileUnLocked":
                message = person.getFullName().trim() + " has unlocked file " + messageJson + " of supplier "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - File unlocked : Notification";
                break;
            case "fileDownloaded":
                message = person.getFullName().trim() + " has downloaded file " + messageJson + " of supplier "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - File downloaded : Notification";
                break;
            case "folderAdded":
                message = person.getFullName().trim() + " has added folder " + messageJson + " of supplier "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - Folder added : Notification";
                break;
            case "folderDeleted":
                message = person.getFullName().trim() + " has deleted folder " + messageJson + " of supplier "
                        + supplier.getName();
                mailSubject = supplier.getName() + " - Folder deleted : Notification";
                break;

            default:
                message = "";
                mailSubject = "";
        }
        subscribeMailDto.setMailSubject(mailSubject);
        subscribeMailDto.setMessage(message);
        return subscribeMailDto;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMPPAP> getSupplierPpaps(Integer supplierId) {
        List<PQMPPAP> ppaps = ppapRepository.findBySupplier(supplierId);
        for (PQMPPAP pqmppap : ppaps) {
            PLMSupplierPart supplierPart = supplierPartRepository.findOne(pqmppap.getSupplierPart());
            pqmppap.setMfrPart(supplierPart.getManufacturerPart());
        }
        return ppaps;
    }

    @Transactional(readOnly = true)
    public List<SupplierAuditDetailsDto> getSupplierAuditPlans(Integer supplierId) {
        List<SupplierAuditDetailsDto> supplierAuditDetails = new ArrayList<>();

        List<PQMSupplierAuditPlan> auditPlans = supplierAuditPlanRepository.findBySupplier(supplierId);
        for (PQMSupplierAuditPlan auditPlan : auditPlans) {
            PQMSupplierAudit pqmSupplierAudit = supplierAuditRepository.findOne(auditPlan.getSupplierAudit());
            SupplierAuditDetailsDto detailsDto = new SupplierAuditDetailsDto();
            detailsDto.setId(auditPlan.getId());
            detailsDto.setSupplierAudit(pqmSupplierAudit.getId());
            detailsDto.setNumber(pqmSupplierAudit.getNumber());
            detailsDto.setName(pqmSupplierAudit.getName());
            detailsDto.setType(pqmSupplierAudit.getType().getName());
            detailsDto.setDescription(pqmSupplierAudit.getDescription());

            detailsDto.setStatus(auditPlan.getStatus());
            detailsDto.setPlannedStartDate(auditPlan.getPlannedStartDate());
            detailsDto.setFinishedDate(auditPlan.getFinishedDate());
            detailsDto.setPreparedByName(personRepository.findOne(auditPlan.getCreatedBy()).getFullName());
            detailsDto.setCreatedBy(auditPlan.getCreatedBy());
            detailsDto.setApprovers(supplierAuditReviewerRepository.findByPlan(auditPlan.getId()));
            detailsDto.getApprovers().forEach(supplierAuditReviewer -> {
                supplierAuditReviewer.setApproverName(personRepository.findOne(supplierAuditReviewer.getReviewer()).getFullName());
            });
            supplierAuditDetails.add(detailsDto);
        }

        return supplierAuditDetails;
    }

}
