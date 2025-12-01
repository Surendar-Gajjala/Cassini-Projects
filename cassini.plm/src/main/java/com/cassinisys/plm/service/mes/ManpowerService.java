package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.PersonCriteria;
import com.cassinisys.platform.filtering.PersonPredicateBuilder;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.QPerson;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.ManpowerEvents;
import com.cassinisys.plm.filtering.ManpowerContactCriteria;
import com.cassinisys.plm.filtering.ManpowerCriteria;
import com.cassinisys.plm.filtering.ManpowerPredicateBuilder;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.pgc.PGCDeclarationRepository;
import com.cassinisys.plm.service.activitystream.ManpowerActivityStream;
import com.cassinisys.plm.service.cm.DCOService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aredl on 28-10-2020.
 */
@Service
public class ManpowerService implements CrudService<MESManpower, Integer> {
    @Autowired
    private MESManpowerRepository manpowerRepo;
    @Autowired
    private ManpowerPredicateBuilder manpowerPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private ManpowerTypeRepository manpowerTypeRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PersonService personService;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ManpowerContactRepository manpowerContactRepository;
    @Autowired
    private PGCDeclarationRepository declarationRepository;
    @Autowired
    private ManpowerActivityStream manpowerActivityStream;
    @Autowired
    private PersonPredicateBuilder personPredicateBuilder;
    @Autowired
    private ShiftPersonsRepository shiftPersonsRepository;
    @Autowired
    private ProductionOrderRepository productionOrderRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesManpower,'create')")
    public MESManpower create(MESManpower mesManpower) {

        // Person existPerson1 = this.personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(mesManpower.getPersonDetails().getFirstName(), mesManpower.getPersonDetails().getLastName());
        // if(existPerson1 != null) {
        //     String message = messageSource.getMessage("full_name_already_exist", null, "{0} : full name already exist", LocaleContextHolder.getLocale());
        //     String result = MessageFormat.format(message + ".", existPerson1.getFirstName() + " " + existPerson1.getLastName());
        //     throw new CassiniException(result);
        // }

        // if (mesManpower.getNewPerson()) {
        //     if (mesManpower.getPersonDetails().getPersonType() == null) {
        //         mesManpower.getPersonDetails().setPersonType(1);
        //     }
        //     Person person = personService.create(mesManpower.getPersonDetails());
        //     mesManpower.setPerson(person.getId());
        // }
        autoNumberService.saveNextNumber(mesManpower.getType().getAutoNumberSource().getId(), mesManpower.getNumber());

        List<MESObjectAttribute> mesObjectAttributes = mesManpower.getMesObjectAttributes();
        MESManpower existingManpower = manpowerRepo.findByName(mesManpower.getName());
        if (existingManpower != null) {
            String message = messageSource.getMessage("manpower_name_already_exists", null, "{0} Manpower name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingManpower.getName());
            throw new CassiniException(result);
        }
        MESManpower existManpower = manpowerRepo.findByNumber(mesManpower.getNumber());
        if (existManpower != null) {
            String message = messageSource.getMessage("manpower_number_already_exists", null, "{0} Manpower number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existManpower.getNumber());
            throw new CassiniException(result);
        }
        mesManpower = manpowerRepo.save(mesManpower);
        for (MESObjectAttribute attribute : mesObjectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(mesManpower.getId(), attribute.getId().getAttributeDef()));
                mesObjectAttributeRepository.save(attribute);
            }
        }
        applicationEventPublisher.publishEvent(new ManpowerEvents.ManpowerCreatedEvent(mesManpower));
        return mesManpower;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesManpower.id ,'edit')")
    public MESManpower update(MESManpower mesManpower) {
        MESManpower oldManpower = JsonUtils.cloneEntity(manpowerRepo.findOne(mesManpower.getId()), MESManpower.class);
        MESManpower existingManpower = manpowerRepo.findByName(mesManpower.getName());
        if (existingManpower != null && !existingManpower.getId().equals(mesManpower.getId())) {
            throw new CassiniException("Manpower name already exists");
        } else {
            mesManpower = manpowerRepo.save(mesManpower);
        }
        applicationEventPublisher.publishEvent(new ManpowerEvents.ManpowerBasicInfoUpdatedEvent(oldManpower, mesManpower));
        return mesManpower;
    }

    @Override
    @PreAuthorize("hasPermission(#manpowerId,'delete')")
    public void delete(Integer manpowerId) {
        manpowerRepo.delete(manpowerId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESManpower get(Integer manpowerId) {
        MESManpower manpower = manpowerRepo.findOne(manpowerId);
        return manpower;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESManpower> getAll() {
        return manpowerRepo.findAll();
    }

    public void saveManpowerAttributes(List<MESObjectAttribute> attributes) {
        for (MESObjectAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                mesObjectAttributeRepository.save(attribute);
            }
        }
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESManpower> getAllManpowersByPageable(Pageable pageable, ManpowerCriteria criteria) {
        Predicate predicate = manpowerPredicateBuilder.build(criteria, QMESManpower.mESManpower);
        Page<MESManpower> manpowers = manpowerRepo.findAll(predicate, pageable);
        // manpowers.getContent().forEach(manpower -> {
        //     if (manpower.getPerson() != null) {
        //         manpower.setPersonName(personRepository.findOne(manpower.getPerson()).getFullName());
        //     }
        // });

        return manpowers;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESManpower> getManpowerByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESManpowerType type = manpowerTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return manpowerRepo.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESManpowerType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESManpowerType> children = manpowerTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESManpowerType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }


    @Transactional(readOnly = true)
    public Page<MESManpower> getAllFilteredManpowers(Pageable pageable, ManpowerCriteria criteria) {
        Predicate predicate = manpowerPredicateBuilder.build(criteria, QMESManpower.mESManpower);
        Page<MESManpower> manpowers = manpowerRepo.findAll(predicate, pageable);

        return manpowers;
    }

    /*
     * Manpower Persons
     *
     */

    @Transactional(readOnly = true)
    public List<MESManpowerContact> getManpowerContacts(Integer manpowerId) {
        List<MESManpowerContact> contacts = manpowerContactRepository.findByManpower(manpowerId);
        for (MESManpowerContact contact : contacts) {

            Person person = personRepository.findOne(contact.getContact());
            contact.setPerson(person);

        }

        return contacts;
    }

    @Transactional
    public MESManpowerContact createContactManpower(MESManpowerContact contact) throws JsonProcessingException {

        MESManpower manpower = manpowerRepo.findOne(contact.getManpower());
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
        MESManpowerContact existingContact = manpowerContactRepository.findByContactAndManpower(contact.getContact(),
                contact.getManpower());
        if (existingContact != null) {
            throw new CassiniException(messageSource.getMessage("manpower_person_already_exist", null,
                    "Manpower person already exist", LocaleContextHolder.getLocale()));

        } else {
            contact = manpowerContactRepository.save(contact);
        }
        applicationEventPublisher.publishEvent(new ManpowerEvents.ManpowerPersonCreatedEvent(manpower, person));
        return contact;
    }

    @Transactional
    public MESManpowerContact updateManpowerContact(MESManpowerContact contact) {
        MESManpowerContact oldContact = JsonUtils.cloneEntity(manpowerContactRepository.findOne(contact.getId()),
                MESManpowerContact.class);
        Person oldPerson = personRepository.findOne(oldContact.getContact());
        oldContact.setPerson(oldPerson);
        MESManpowerContact existingContact = manpowerContactRepository.findByContactAndManpower(contact.getContact(),
                contact.getManpower());
        if (existingContact != null && !existingContact.getId().equals(contact.getId())) {
            throw new CassiniException(messageSource.getMessage("manpower_person_already_exist", null,
                    "Manpower person already exist", LocaleContextHolder.getLocale()));

        } else {
            applicationEventPublisher.publishEvent(new ManpowerEvents.ManpowerPersonUpdatedEvent(oldContact, contact));
            personService.update(contact.getPerson());
            contact = manpowerContactRepository.save(contact);
        }
        return contact;
    }

    @Transactional(readOnly = true)
    public MESManpowerContact getContact(Integer contact) {
        return manpowerContactRepository.findOne(contact);
    }

    public void deleteContact(Integer contactId) throws JsonProcessingException {
        MESManpowerContact contact = manpowerContactRepository.findOne(contactId);
        MESManpowerContact manpowerContact = manpowerContactRepository.findOne(contactId);
        List<MESShiftPerson> shiftPersons = shiftPersonsRepository.findShiftsByPerson(manpowerContact.getContact());
        for (MESShiftPerson shiftPerson : shiftPersons) {
            shiftPersonsRepository.delete(shiftPerson.getId());
        }
        MESManpower manpower = manpowerRepo.findOne(contact.getManpower());
        Person person = personRepository.findOne(contact.getContact());
        applicationEventPublisher.publishEvent(new ManpowerEvents.ManpowerPersonDeletedEvent(manpower, person));
        manpowerContactRepository.delete(contactId);
    }


    @Transactional(readOnly = true)
    public Page<Person> getAllManpowerContacts(Pageable pageable, ManpowerContactCriteria criteria) {
        List<Integer> personIds = shiftPersonsRepository.findByPersonIdsByShift(criteria.getShift());
        List<Integer> manpowerContactIds = manpowerContactRepository.getUniqueContacts();
        PersonCriteria personCriteria = new PersonCriteria();
        if (personIds.size() > 0) {
            personCriteria.setPersonIds(personIds);
        }
        personCriteria.setFilterPersons(true);
        personCriteria.setFilterIds(manpowerContactIds);
        personCriteria.setFirstName(criteria.getSearchQuery());
        Predicate predicate = personPredicateBuilder.build(personCriteria, QPerson.person);
        return personRepository.findAll(predicate, pageable);
    }

    @Transactional
    public Integer getAllManpowerContactExitOrNot(Integer id) {
        MESManpowerContact manpowerContact = manpowerContactRepository.findOne(id);
        Integer shiftPersons = shiftPersonsRepository.getShiftPersonCountByPerson(manpowerContact.getContact());
        Integer productionOrders = productionOrderRepository.getProductionOrdersCountByAssignedTo(manpowerContact.getContact());
        if(productionOrders > 0){
            throw new CassiniException(messageSource.getMessage("manpower_person_already_exist_in_production_order", null,
                    "Manpower person already exist in productionOrders", LocaleContextHolder.getLocale()));
        }
        return shiftPersons;
    }

}
