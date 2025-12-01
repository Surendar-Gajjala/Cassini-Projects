package com.cassinisys.plm.service.pgc;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.event.DeclarationEvents;
import com.cassinisys.plm.filtering.DeclarationCriteria;
import com.cassinisys.plm.filtering.DeclarationPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMSupplierContact;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.model.pgc.dto.BosItemDto;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.PLMSharedObject;
import com.cassinisys.plm.model.plm.SharePermission;
import com.cassinisys.plm.model.plm.ShareType;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.mfr.SupplierContactRepository;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.pgc.*;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.plm.SharedObjectService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 27-11-2020.
 */
@Service
public class DeclarationService implements CrudService<PGCDeclaration, Integer> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PGCDeclarationRepository pgcDeclarationRepository;
    @Autowired
    private PGCDeclarationTypeRepository pgcDeclarationTypeRepository;
    @Autowired
    private PGCObjectAttributeRepository pgcObjectAttributeRepository;
    @Autowired
    private DeclarationPredicateBuilder declarationPredicateBuilder;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierContactRepository supplierContactRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private PGCDeclarationPartRepository declarationPartRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private SharedObjectService sharedObjectService;
    @Autowired
    private PGCBosRepository pgcBosRepository;
    @Autowired
    private PGCBosItemRepository pgcBosItemRepository;
    @Autowired
    private PGCSubstanceRepository pgcSubstanceRepository;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private PGCDeclarationSpecificationRepository pgcDeclarationSpecificationRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private PGCDeclarationPartComplianceRepository declarationPartComplianceRepository;
    @Autowired
    private PGCSpecificationSubstanceRepository specificationSubstanceRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private SubstanceService substanceService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#declaration,'create')")
    public PGCDeclaration create(PGCDeclaration declaration) {
        List<PGCObjectAttribute> pgcObjectAttributes = declaration.getPgcObjectAttributes();
        List<ObjectAttribute> objectAttributes = declaration.getObjectAttributes();
        PGCDeclaration existDeclaration = pgcDeclarationRepository.findByNumber(declaration.getNumber());
        PGCDeclaration existDeclarationName = pgcDeclarationRepository.findByNameContainingIgnoreCase(declaration.getName());
        if (existDeclaration != null) {
            String message = messageSource.getMessage("declaration_number_already_exists", null, "{0} Declaration Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existDeclaration.getNumber());
            throw new CassiniException(result);
        }
        if (existDeclarationName != null) {
            String message = messageSource.getMessage("declaration_name_already_exists", null, "{0} Declaration Name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existDeclarationName.getName());
            throw new CassiniException(result);
        }
        autoNumberService.saveNextNumber(declaration.getType().getAutoNumberSource().getId(), declaration.getNumber());
        declaration = pgcDeclarationRepository.save(declaration);
        substanceService.savePGCObjectAttributes(declaration.getId(), pgcObjectAttributes);
        substanceService.saveObjectAttributes(declaration.getId(), objectAttributes);
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationCreatedEvent(declaration));
        return declaration;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#declaration.id ,'edit')")
    public PGCDeclaration update(PGCDeclaration declaration) {
        PGCDeclaration oldDeclaration = JsonUtils.cloneEntity(pgcDeclarationRepository.findOne(declaration.getId()), PGCDeclaration.class);
        declaration = pgcDeclarationRepository.save(declaration);
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationBasicInfoUpdatedEvent(oldDeclaration, declaration));
        return declaration;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PGCDeclaration declaration = pgcDeclarationRepository.findOne(id);
        pgcDeclarationRepository.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PGCDeclaration get(Integer id) {
        PGCDeclaration declaration = pgcDeclarationRepository.findOne(id);
        if (declaration.getSupplier() != null) {
            declaration.setSupplierName(supplierRepository.findOne(declaration.getSupplier()).getName());
        }
        if (declaration.getSupplier() != null) {
            PLMSupplierContact supplierContact = supplierContactRepository.findOne(declaration.getContact());
            Person person = personRepository.findOne(supplierContact.getContact());
            declaration.setSupplierContactName(person.getFullName());
            declaration.setSupplierContact(person.getId());
        }
        return declaration;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCDeclaration> getAll() {
        return pgcDeclarationRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PGCDeclaration> findMultiple(List<Integer> ids) {
        return pgcDeclarationRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PGCDeclaration> getAllDeclarations(Pageable pageable, DeclarationCriteria declarationCriteria) {
        Predicate predicate = declarationPredicateBuilder.build(declarationCriteria, QPGCDeclaration.pGCDeclaration);
        Page<PGCDeclaration> declarations = pgcDeclarationRepository.findAll(predicate, pageable);
        declarations.getContent().forEach(declaration -> {
            if (declaration.getSupplier() != null) {
                declaration.setSupplierName(supplierRepository.findOne(declaration.getSupplier()).getName());
            }
            if (declaration.getContact() != null) {
                PLMSupplierContact supplierContact = supplierContactRepository.findOne(declaration.getContact());
                Person person = personRepository.findOne(supplierContact.getContact());
                declaration.setSupplierContactName(person.getFullName());
            }
        });
        return declarations;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PGCDeclaration> getObjectsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PGCDeclarationType type = pgcDeclarationTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return pgcDeclarationRepository.getByTypeIds(ids, pageable);
    }

    @Transactional
    public ItemDetailsDto getDeclarationTabCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();

        detailsDto.setParts(declarationPartRepository.getDeclarationPartCount(id));
        detailsDto.setSpecifications(pgcDeclarationSpecificationRepository.getDeclarationSpecificationCount(id));

        return detailsDto;
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PGCDeclarationType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PGCDeclarationType> children = pgcDeclarationTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (PGCDeclarationType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional
    public PGCDeclarationPart createDeclarationPart(Integer id, PGCDeclarationPart declarationPart) {
        List<PGCDeclarationPart> declarationParts = new ArrayList<>();
        if (declarationPart.getDeclaration() != null) {
            declarationPart.setDeclaration(id);
        }
        PGCDeclaration declaration = pgcDeclarationRepository.findOne(id);
        declarationPart = declarationPartRepository.save(declarationPart);
        declarationParts.add(declarationPart);
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationPartAddEvent(declaration, declarationParts));
        return declarationPart;
    }

    @Transactional
    public List<PGCDeclarationPart> createDeclarationParts(Integer id, List<PGCDeclarationPart> declarationParts) {
        declarationParts.forEach(substance -> {
            substance.setDeclaration(id);
        });
        declarationParts = declarationPartRepository.save(declarationParts);
        PGCDeclaration declaration = pgcDeclarationRepository.findOne(id);
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationPartAddEvent(declaration, declarationParts));
        return declarationParts;
    }

    @Transactional
    public PGCDeclarationPart updateDeclarationPart(Integer id, PGCDeclarationPart declarationPart) {
        declarationPart.setDeclaration(id);
        return declarationPartRepository.save(declarationPart);
    }

    @Transactional(readOnly = true)
    public List<PGCDeclarationPart> getAllDeclarationParts(Integer id) {
        List<PGCDeclarationPart> declarationParts = declarationPartRepository.findByDeclaration(id);
        List<Integer> specificationIds = pgcDeclarationSpecificationRepository.getSpecificationIdsByDeclaration(id);
        declarationParts.forEach(pgcDeclarationPart -> {
            PLMManufacturer manufacturer = manufacturerRepository.findOne(pgcDeclarationPart.getPart().getManufacturer());
            pgcDeclarationPart.getPart().setMfrName(manufacturer.getName());
            if (pgcDeclarationPart.getBos() != null) {
                List<PGCBosItem> bosItems = pgcBosItemRepository.findByBos(pgcDeclarationPart.getBos());
                bosItems.forEach(pgcBosItem -> {
                    BosItemDto bosItemDto = new BosItemDto();
                    bosItemDto.setId(pgcBosItem.getId());
                    bosItemDto.setBos(pgcBosItem.getBos());
                    bosItemDto.setBosItemType(pgcBosItem.getBosItemType());
                    if (pgcBosItem.getBosItemType().equals(BosItemType.SUBSTANCE)) {
                        PGCSubstance substance = pgcSubstanceRepository.findOne(pgcBosItem.getSubstance());
                        bosItemDto.setSubstance(substance.getId());
                        bosItemDto.setCasNumber(substance.getCasNumber());
                        bosItemDto.setSubstanceName(substance.getName());
                        bosItemDto.setSubstanceType(substance.getType().getName());

                        List<PGCSpecificationSubstance> specificationSubstances = specificationSubstanceRepository.getSubstancesBySpecificationIds(substance.getId(), specificationIds);
                        if (specificationSubstances.size() > 0) {
                            MeasurementUnit specMeasurementUnit = measurementUnitRepository.findOne(specificationSubstances.get(0).getUom());
                            Measurement measurement = measurementRepository.findOne(specMeasurementUnit.getMeasurement());
                            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurement.getId());
                            if (!baseUnit.getId().equals(specMeasurementUnit.getId())) {
                                bosItemDto.setThresholdMass(specificationSubstances.get(0).getThresholdMass() * specMeasurementUnit.getConversionFactor());
                            } else {
                                bosItemDto.setThresholdMass(specificationSubstances.get(0).getThresholdMass());
                            }
                            bosItemDto.setThresholdUnitName(specMeasurementUnit.getName());
                            bosItemDto.setThresholdUnitSymbol(specMeasurementUnit.getSymbol());
                        }
                    }
                    if (pgcBosItem.getUom() != null) {
                        MeasurementUnit measurementUnit = measurementUnitRepository.findOne(pgcBosItem.getUom());
                        if (measurementUnit != null) {
                            Measurement measurement = measurementRepository.findOne(measurementUnit.getMeasurement());
                            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurement.getId());
                            if (!baseUnit.getId().equals(measurementUnit.getId())) {
                                bosItemDto.setMass(pgcBosItem.getMass() * measurementUnit.getConversionFactor());
                            } else {
                                bosItemDto.setMass(pgcBosItem.getMass());
                            }
                            bosItemDto.setUom(pgcBosItem.getUom());
                            bosItemDto.setUnitName(measurementUnit.getName());
                            bosItemDto.setUnitSymbol(measurementUnit.getSymbol());
                        }
                    }
                    pgcDeclarationPart.getSubstances().add(bosItemDto);
                });
            }
        });
        return declarationParts;
    }

    @Transactional
    public void deleteDeclarationPart(Integer decId, Integer part) {
        PGCDeclaration declaration = pgcDeclarationRepository.findOne(decId);
        PGCDeclarationPart declarationPart = declarationPartRepository.findOne(part);
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationPartDeletedEvent(declaration, declarationPart));
        declarationPartRepository.delete(part);
    }

    @Transactional
    public PGCBosItem createBOSItem(Integer partId, PGCBosItem bosItem) {
        List<PGCBosItem> bosItems = new ArrayList<>();
        PGCDeclarationPart declarationPart = declarationPartRepository.findOne(partId);
        Integer bosId = declarationPart.getBos();
        if (declarationPart.getBos() == null) {
            PGCBos pgcBos = new PGCBos();
            pgcBos.setPart(declarationPart.getPart().getId());
            pgcBos = pgcBosRepository.save(pgcBos);
            bosId = pgcBos.getId();
        }

        bosItem.setBos(bosId);
        if (bosItem.getUom() != null) {
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(bosItem.getUom());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurementUnit.getMeasurement());

            if (!measurementUnit.getId().equals(baseUnit.getId())) {
                bosItem.setMass(bosItem.getMass() / measurementUnit.getConversionFactor());
            } else {
                bosItem.setMass(bosItem.getMass());
            }
        } else {
            bosItem.setMass(bosItem.getMass());
        }
        bosItem = pgcBosItemRepository.save(bosItem);
        bosItems.add(bosItem);
        declarationPart.setBos(bosId);
        declarationPartRepository.save(declarationPart);
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationPartSubstanceAddEvent(declarationPart.getDeclaration(), declarationPart, bosItems));
        return bosItem;
    }

    @Transactional
    public List<PGCBosItem> createMultipleBOSItem(Integer partId, List<PGCBosItem> bosItems) {
        PGCDeclarationPart declarationPart = declarationPartRepository.findOne(partId);

        for (PGCBosItem bosItem : bosItems) {
            Integer bosId = declarationPart.getBos();
            if (declarationPart.getBos() == null) {
                PGCBos pgcBos = new PGCBos();
                pgcBos.setPart(declarationPart.getPart().getId());
                pgcBos = pgcBosRepository.save(pgcBos);
                bosId = pgcBos.getId();
            }

            bosItem.setBos(bosId);
            if (bosItem.getUom() != null) {
                MeasurementUnit measurementUnit = measurementUnitRepository.findOne(bosItem.getUom());
                MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurementUnit.getMeasurement());

                if (!measurementUnit.getId().equals(baseUnit.getId())) {
                    bosItem.setMass(bosItem.getMass() / measurementUnit.getConversionFactor());
                } else {
                    bosItem.setMass(bosItem.getMass());
                }
            } else {
                bosItem.setMass(bosItem.getMass());
            }
            bosItem = pgcBosItemRepository.save(bosItem);
            declarationPart.setBos(bosId);
            declarationPartRepository.save(declarationPart);
        }
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationPartSubstanceAddEvent(declarationPart.getDeclaration(), declarationPart, bosItems));
        return bosItems;
    }

    @Transactional
    public PGCBosItem updateBOSItem(Integer partId, PGCBosItem bosItem) {
        PGCDeclarationPart declarationPart = declarationPartRepository.findOne(partId);
        PGCBosItem oldBosItem = JsonUtils.cloneEntity(pgcBosItemRepository.findOne(bosItem.getId()), PGCBosItem.class);
        if (bosItem.getUom() != null) {
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(bosItem.getUom());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurementUnit.getMeasurement());

            if (!baseUnit.getId().equals(measurementUnit.getId())) {
                bosItem.setMass(bosItem.getMass() / measurementUnit.getConversionFactor());
            } else {
                bosItem.setMass(bosItem.getMass());
            }
        } else {
            bosItem.setMass(bosItem.getMass());
        }
        bosItem = pgcBosItemRepository.save(bosItem);
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationPartSubstanceUpdatedEvent(declarationPart.getDeclaration(), declarationPart, oldBosItem, bosItem));
        return bosItem;
    }

    @Transactional
    public void deleteBOSItem(Integer partId, Integer bosItemId) {
        PGCDeclarationPart declarationPart = declarationPartRepository.findOne(partId);
        PGCBosItem bosItem = pgcBosItemRepository.findOne(bosItemId);
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationPartSubstanceDeletedEvent(declarationPart.getDeclaration(), declarationPart, bosItem));
        pgcBosItemRepository.delete(bosItemId);
    }

    @Transactional(readOnly = true)
    public List<PGCDeclarationSpecification> getDeclarationSpecifications(Integer decId) {
        return pgcDeclarationSpecificationRepository.getDeclarationSpecifications(decId);
    }

    @Transactional
    public List<PGCDeclarationSpecification> createDeclarationSpecifications(Integer decId, List<PGCDeclarationSpecification> declarationSpecifications) {
        declarationSpecifications = pgcDeclarationSpecificationRepository.save(declarationSpecifications);
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationSpecificationAddEvent(decId, declarationSpecifications));
        return declarationSpecifications;
    }

    @Transactional
    public void deleteDeclarationSpecification(Integer decId, Integer specId) {
        PGCDeclarationSpecification declarationSpecification = pgcDeclarationSpecificationRepository.findOne(specId);
        applicationEventPublisher.publishEvent(new DeclarationEvents.DeclarationSpecificationDeletedEvent(decId, declarationSpecification));
        pgcDeclarationSpecificationRepository.delete(specId);
    }

    @Transactional
    public PGCDeclaration submitDeclarationToContactPerson(Integer id) {
        List<Integer> persons = new ArrayList<>();
        PGCDeclaration declaration = pgcDeclarationRepository.findOne(id);

        if (declaration.getStatus().equals(DeclarationStatus.OPEN)) {
            PLMSharedObject sharedObject = new PLMSharedObject();
            sharedObject.setObjectId(declaration.getId());
            sharedObject.setShareType(ShareType.PERSON);
            sharedObject.setSharedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
            PLMSupplierContact contact = supplierContactRepository.findOne(declaration.getContact());
            persons.add(contact.getContact());
            sharedObject.setSharedToObjects(persons);
            sharedObject.setPermission(SharePermission.WRITE);
            sharedObject.setSharedObjectType(ObjectType.valueOf(PLMObjectType.PGCDECLARATION.toString()));
            sharedObjectService.createDeclarationObject(sharedObject);

            declaration.setStatus(DeclarationStatus.SUBMITTED);
            declaration = pgcDeclarationRepository.save(declaration);

            sendNotificationToSupplierContact(declaration);
        } else if (declaration.getStatus().equals(DeclarationStatus.SUBMITTED)) {

            List<PGCDeclarationPart> declarationParts = declarationPartRepository.findByDeclaration(id);
            for (PGCDeclarationPart declarationPart : declarationParts) {
                if (declarationPart.getBos() == null) {
                    String message = messageSource.getMessage("add_atleast_one_substance_to_part", null, "Add at least one substance to part [ {0} ]", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", declarationPart.getPart().getPartNumber());
                    throw new CassiniException(result);
                } else {
                    List<PGCBosItem> bosItems = pgcBosItemRepository.findByBos(declarationPart.getBos());
                    if (bosItems.size() == 0) {
                        String message = messageSource.getMessage("add_atleast_one_substance_to_part", null, "Add at least one substance to part [ {0} ]", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", declarationPart.getPart().getPartNumber());
                        throw new CassiniException(result);
                    }
                }
            }
            declaration.setStatus(DeclarationStatus.RECEIVED);
            declaration = pgcDeclarationRepository.save(declaration);

            sendNotificationToSupplierContact(declaration);
        }

        return declaration;
    }

    private void sendNotificationToSupplierContact(PGCDeclaration declaration) {
        PLMSupplierContact contact = supplierContactRepository.findOne(declaration.getContact());
        Person supplierContact = personRepository.findOne(contact.getContact());
        Person createdBy = personRepository.findOne(declaration.getCreatedBy());

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
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String createdDate = df.format(declaration.getCreatedDate());
        String modifiedDate = df.format(declaration.getModifiedDate());
        if (declaration.getStatus().equals(DeclarationStatus.SUBMITTED) && supplierContact.getEmail() != null) {
            final URL companyLogos = companyLogo;
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("declaration", declaration);
                model.put("companyLogo", companyLogos);
                model.put("personName", supplierContact.getFullName());
                Mail mail = new Mail();
                mail.setMailTo(supplierContact.getEmail());
                mail.setMailSubject("New Declaration");
                mail.setTemplatePath("email/declarationNotification.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        } else if (declaration.getStatus().equals(DeclarationStatus.RECEIVED) && createdBy.getEmail() != null) {
            final URL companyLogos = companyLogo;
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("declaration", declaration);
                model.put("companyLogo", companyLogos);
                model.put("personName", createdBy.getFullName());
                Mail mail = new Mail();
                mail.setMailTo(supplierContact.getEmail());
                mail.setMailSubject("Declaration Submitted");
                mail.setTemplatePath("email/declarationNotification.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    @Transactional
    public PGCDeclaration generateComplianceReport(Integer id) {
        PGCDeclaration declaration = pgcDeclarationRepository.findOne(id);

        List<PGCDeclarationPart> declarationParts = declarationPartRepository.findByDeclaration(id);
        for (PGCDeclarationPart declarationPart : declarationParts) {

            if (declarationPart.getBos() != null) {
                List<PGCBosItem> bosItems = pgcBosItemRepository.findByBos(declarationPart.getBos());
                for (PGCBosItem bosItem : bosItems) {
                    if (bosItem.getBosItemType().equals(BosItemType.SUBSTANCE)) {
                        PGCSubstance substance = pgcSubstanceRepository.findOne(bosItem.getSubstance());

                        List<PGCSpecificationSubstance> specificationSubstances = specificationSubstanceRepository.findBySubstance(substance);
                        for (PGCSpecificationSubstance specificationSubstance : specificationSubstances) {
                            PGCDeclarationSpecification declarationSpecification = pgcDeclarationSpecificationRepository.getByDeclarationAndSpecification(declaration.getId(), specificationSubstance.getSpecification());
                            if (declarationSpecification != null) {
                                PGCDeclarationPartCompliance partCompliance = declarationPartComplianceRepository.findByDeclarationPartAndDeclarationSpec(declarationPart.getId(), declarationSpecification.getId());
                                if (partCompliance == null) {
                                    partCompliance = new PGCDeclarationPartCompliance();
                                }
                                partCompliance.setDeclarationPart(declarationPart.getId());
                                partCompliance.setDeclarationSpec(declarationSpecification.getId());

                                Double substanceMass = bosItem.getMass();
                                Double specSubstanceMass = specificationSubstance.getThresholdMass();
                                /*MeasurementUnit specMeasurementUnit = measurementUnitRepository.findOne(specificationSubstance.getUom());
                                MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(specMeasurementUnit.getMeasurement());
                                if (!baseUnit.getId().equals(specMeasurementUnit.getId())) {
                                    specSubstanceMass = specificationSubstance.getThresholdMass() * specMeasurementUnit.getConversionFactor();
                                } else {
                                    specSubstanceMass = specificationSubstance.getThresholdMass();
                                }

                                MeasurementUnit measurementUnit = measurementUnitRepository.findOne(bosItem.getUom());
                                baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurementUnit.getMeasurement());
                                if (!baseUnit.getId().equals(measurementUnit.getId())) {
                                    substanceMass = bosItem.getMass() * measurementUnit.getConversionFactor();
                                } else {
                                    substanceMass = bosItem.getMass();
                                }*/

                                if (substanceMass <= specSubstanceMass) {
                                    partCompliance.setCompliant(true);
                                } else {
                                    partCompliance.setCompliant(false);
                                }

                                partCompliance = declarationPartComplianceRepository.save(partCompliance);
                            }
                        }
                    }
                }
            }
        }

        declaration.setStatus(DeclarationStatus.ACCEPTED);
        declaration = pgcDeclarationRepository.save(declaration);

        return declaration;
    }
}
