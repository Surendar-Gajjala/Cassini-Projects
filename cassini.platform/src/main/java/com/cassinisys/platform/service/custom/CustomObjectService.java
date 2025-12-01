package com.cassinisys.platform.service.custom;

import com.cassinisys.platform.events.CustomObjectEvents;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.CustomObjectAdvancedCriteria;
import com.cassinisys.platform.filtering.CustomObjectCriteria;
import com.cassinisys.platform.filtering.CustomObjectPredicateBuilder;
import com.cassinisys.platform.filtering.CustomParameterCriteria;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.common.GroupMember;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.model.custom.*;
import com.cassinisys.platform.model.dto.AttributeDetailsDto;
import com.cassinisys.platform.model.dto.CustomObjectDto;
import com.cassinisys.platform.repo.col.AttachmentRepository;
import com.cassinisys.platform.repo.common.GroupMemberRepository;
import com.cassinisys.platform.repo.common.PersonGroupRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.custom.*;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CustomObjectService {
    ExpressionParser parser = new SpelExpressionParser();
    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private CustomObjectWithLifecycleRepository customObjectWithLifecycleRepository;
    @Autowired
    private CustomRevisionedObjectRepository customRevisionedObjectRepository;
    @Autowired
    private CustomObjectAttributeRepository customObjectAttributeRepository;
    @Autowired
    private CustomObjectTypeAttributeRepository customObjectTypeAttributeRepository;
    @Autowired
    private CustomObjectPredicateBuilder customObjectPredicateBuilder;
    @Autowired
    private AttributeAttachmentService attributeAttachmentService;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private CustomObjectBomRepository customObjectBomRepository;
    @Autowired
    private CustomObjectRelatedRepository customObjectRelatedRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CustomObjectFileRepository customObjectFileRepository;
    @Autowired
    private CustomObjectService customObjectService;
    @Autowired
    private CustomObjectTypeService customObjectTypeService;
    @Autowired
    private CustomObjectAdvancedCriteria objectAdvancedCriteria;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private LoginRepository loginRepository;

    /* Custom object methods */
    @Transactional
    @PreAuthorize("hasPermission(#customObject,'create')")
    public CustomObject createObject(CustomObject customObject) {
        String supplierEmail = customObject.getSupplierEmail();
        String supplierName = customObject.getSupplierName();
        CustomObject existObject = customObjectRepository.findByNumber(customObject.getNumber());
        if (existObject != null) {
            String message = messageSource.getMessage("custom_object_number_already_exist", null,
                    "{0} : Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existObject.getNumber());
            throw new CassiniException(result);
        }
        customObject = customObjectRepository.save(customObject);
        customObject.setSupplierEmail(supplierEmail);
        customObject.setSupplierName(supplierName);
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectCreatedEvent(customObject));
        return customObject;
    }

    @Transactional
    @PreAuthorize("hasPermission(#customObject.id ,'edit')")
    public CustomObject updateObject(CustomObject customObject) {
        CustomObject oldCustomObject = JsonUtils.cloneEntity(customObjectRepository.findOne(customObject.getId()),
                CustomObject.class);
        customObject = customObjectRepository.save(customObject);
        applicationEventPublisher
                .publishEvent(new CustomObjectEvents.CustomObjectBasicInfoUpdatedEvent(oldCustomObject, customObject));
        return customObject;
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteObject(Integer id) {
        customObjectRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<CustomObject> getAllCustomObjects() {
        return customObjectRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public CustomObject getCustomObject(Integer id) {
        CustomObject customObject = customObjectRepository.findOne(id);
        return customObject;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<CustomObject> getCustomObjects(Pageable pageable, CustomObjectCriteria customObjectCriteria) {
        Predicate predicate = customObjectPredicateBuilder.build(customObjectCriteria, QCustomObject.customObject);
        Page<CustomObject> customObjects = customObjectRepository.findAll(predicate, pageable);
        List<Integer> customObjectIds = new ArrayList<>();
        customObjects.getContent().forEach(customObject -> {
            customObjectIds.add(customObject.getId());

        });
        if (customObjectIds.size() > 0) {
            List<CustomObjectFile> customFiles = customObjectFileRepository
                    .getCustomObjectFilesByIdsIn(customObjectIds);
            Map<Integer, List<CustomObjectFile>> customObjectFileMap = new ConcurrentHashMap<>();
            customObjectFileMap = customFiles.stream().collect(Collectors.groupingBy(t -> t.getObject()));

            List<Integer> customFileIds = new ArrayList<>();
            customFiles.forEach(customFile -> {
                customFileIds.add(customFile.getId());
            });

            if (customFileIds.size() > 0) {
                List<CustomObjectFile> folders = customObjectFileRepository
                        .findByParentFileInAndLatestTrueOrderByCreatedDateDesc(customFileIds);
                Map<Integer, List<CustomObjectFile>> customObjectFileFolderCountMap = new ConcurrentHashMap<>();
                customObjectFileFolderCountMap = folders.stream().collect(Collectors.groupingBy(t -> t.getParentFile()));


                for (CustomObjectFile customFile : customFiles) {
                    customFile.setCount((customObjectFileFolderCountMap.containsKey(customFile.getId()) ? customObjectFileFolderCountMap.get(customFile.getId()) : new ArrayList<>()).size());
                    customFile.setParentObject(ObjectType.CUSTOMOBJECTTYPE);
                }
            }


            List<CustomObjectBom> bomCounts = customObjectBomRepository.getCustomObjectBomCountByParentIdsIn(customObjectIds);
            Map<Integer, List<CustomObjectBom>> customObjectBomCountMap = new ConcurrentHashMap<>();

            customObjectBomCountMap = bomCounts.stream().collect(Collectors.groupingBy(t -> t.getParent().getId()));
            for (CustomObject customObject : customObjects.getContent()) {
                customObject.setItemFiles(customObjectFileMap.containsKey(customObject.getId()) ? customObjectFileMap.get(customObject.getId()) : new ArrayList<>());
                customObject.setHasBom((customObjectBomCountMap.containsKey(customObject.getId()) ? customObjectBomCountMap.get(customObject.getId()) : new ArrayList<>()).size() > 0);
            }

        }


        return customObjects;
    }

    @Transactional(readOnly = true)
    public List<CustomObject> getCustomObjectBySupplier(Integer id) {
        List<CustomObject> customObjects = customObjectRepository.findBySupplier(id);
        return customObjects;
    }

    @Transactional(readOnly = true)
    public List<CustomObject> getCustomObjectByTypeAndSupplier(Integer type, Integer id) {
        List<CustomObject> customObjects = customObjectRepository.findByTypeIdAndSupplier(type, id);
        return customObjects;
    }

    @Transactional(readOnly = true)
    public List<CustomObjectFile> findByCustomObject(CustomObject customObject) {
        List<CustomObjectFile> customFiles = customObjectFileRepository
                .getCustomObjectFiles(customObject.getId());
        customFiles.forEach(customFile -> {
            customFile.setParentObject(ObjectType.CUSTOMOBJECTTYPE);
            if (customFile.getFileType().equals("FOLDER")) {
                customFile.setCount(customObjectFileRepository
                        .findByParentFileAndLatestTrueOrderByCreatedDateDesc(customFile.getId()).size());
            }
        });
        return customFiles;
    }


    @Transactional(readOnly = true)
    public List<CustomObjectFile> findByCustomObjectDuplicate(List<Integer> custobObjectIds) {
        List<CustomObjectFile> customFiles = customObjectFileRepository
                .getCustomObjectFilesByIdsIn(custobObjectIds);
        Map<Integer, List<CustomObjectFile>> customObjectFileMap = new ConcurrentHashMap<>();
        customObjectFileMap = customFiles.stream().collect(Collectors.groupingBy(t -> t.getObject()));

        List<Integer> customFileIds = new ArrayList<>();
        customFiles.forEach(customFile -> {
            customFileIds.add(customFile.getId());
            customFile.setParentObject(ObjectType.CUSTOMOBJECTTYPE);
        });

        List<CustomObjectFile> folders = customObjectFileRepository
                .findByParentFileInAndLatestTrueOrderByCreatedDateDesc(customFileIds);
        Map<Integer, List<CustomObjectFile>> customObjectFileFolderCountMap = new ConcurrentHashMap<>();
        customObjectFileFolderCountMap = folders.stream().collect(Collectors.groupingBy(t -> t.getParentFile()));


        return customFiles;
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<CustomObject> getMultipleObjects(List<Integer> ids) {
        return customObjectRepository.findByIdIn(ids);
    }

    @Transactional
    public CustomObjectDto createCustomObject(CustomObjectDto customObjectDto) {
        autoNumberService.saveNextNumber(customObjectDto.getCustomObject().getType().getNumberSource().getId(),
                customObjectDto.getCustomObject().getNumber());
        CustomObject customObject = customObjectService.createObject(customObjectDto.getCustomObject());
        List<CustomObjectAttribute> customObjectAttributes = customObjectDto.getCustomObjectAttributes();
        List<ObjectAttribute> objectAttributes = customObjectDto.getObjectAttributes();
        saveCustomObjectAttributes(customObjectAttributes, customObject);
        saveObjectAttributes(objectAttributes, customObject);
        return customObjectDto;
    }

    private void saveCustomObjectAttributes(List<CustomObjectAttribute> customObjectAttributes, CassiniObject obj) {
        for (CustomObjectAttribute attribute : customObjectAttributes) {
            CustomObjectTypeAttribute customObjectTypeAttribute = customObjectTypeAttributeRepository
                    .findOne(attribute.getId().getAttributeDef());
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

                attribute.setId(new ObjectAttributeId(obj.getId(), attribute.getId().getAttributeDef()));
                customObjectAttributeRepository.save(attribute);
            }
        }
    }

    public void saveObjectAttributes(List<ObjectAttribute> objectAttributes, CassiniObject obj) {
        for (ObjectAttribute attribute : objectAttributes) {
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

                attribute.setId(new ObjectAttributeId(obj.getId(), attribute.getId().getAttributeDef()));
                objectAttributeRepository.save(attribute);
            }
        }
    }

    @Transactional
    public CustomObjectDto saveImageAttributeValue(ObjectType objectType, Integer objectId, Integer attributeId,
                                                   Map<String, MultipartFile> fileMap) {
        if (objectType.equals(ObjectType.CUSTOMOBJECTTYPE)) {
            CustomObjectAttribute customObjectAttribute = new CustomObjectAttribute();
            customObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, customObjectAttribute);
        } else {
            ObjectAttribute objectAttribute = new ObjectAttribute();
            objectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            setImage(files, objectAttribute);
        }

        return null;
    }

    public void setImage(List<MultipartFile> files, ObjectAttribute attribute) {
        if (files.size() > 0) {
            MultipartFile file = files.get(0);
            try {
                attribute.setImageValue(file.getBytes());
                attribute = objectAttributeRepository.save(attribute);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public CustomObjectDto saveAttachmentAttributeValue(ObjectType objectType, Integer objectId, Integer attributeId,
                                                        Map<String, MultipartFile> fileMap) {
        List<AttributeAttachment> attributeAttachments = null;
        List<Integer> attachmentIds = new ArrayList<>();
        try {
            List<MultipartFile> files = new ArrayList<>();
            fileMap.values().forEach(multipartFile -> {
                files.add(multipartFile);
            });
            String type = objectType.toString();
            type = "CUSTOMOBJECT";

            attributeAttachments = attributeAttachmentService.addAttributeMultipleAttachments(objectId, attributeId,
                    ObjectType.valueOf(type), files);
            attributeAttachments.forEach(attributeAttachment -> {
                attachmentIds.add(attributeAttachment.getId());
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (objectType.equals(ObjectType.CUSTOMOBJECTTYPE)) {
            CustomObjectAttribute customObjectAttribute = new CustomObjectAttribute();
            customObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            customObjectAttribute.setAttachmentValues(values);
            customObjectAttribute = customObjectAttributeRepository.save(customObjectAttribute);
        } else {
            ObjectAttribute objectAttribute = new ObjectAttribute();
            objectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            Integer[] values = attachmentIds.toArray(new Integer[attachmentIds.size()]);
            objectAttribute.setAttachmentValues(values);
            objectAttribute = objectAttributeRepository.save(objectAttribute);
        }

        return null;
    }

    @Transactional
    public CustomObjectAttribute updateCustomAttribute(CustomObjectAttribute attribute) {
        CustomObjectAttribute oldValue = customObjectAttributeRepository
                .findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, CustomObjectAttribute.class);
        attribute = customObjectAttributeRepository.save(attribute);
        CustomObject customObject = customObjectRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectAttributesUpdatedEvent(
                customObject.getId(), customObject.getObjectType(), oldValue, attribute));
        return attribute;
    }

    /* Custom object with lifecycle methods */
    @Transactional
    public CustomObjectWithLifecycle updateObjectWithLifecycle(CustomObjectWithLifecycle customObjectWithLifecycle) {
        return customObjectWithLifecycleRepository.save(customObjectWithLifecycle);
    }

    @Transactional(readOnly = true)
    public CustomObjectWithLifecycle getObjectWithLifecycle(Integer id) {
        return customObjectWithLifecycleRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Page<CustomObjectWithLifecycle> getObjectsWithLifecycle(Pageable pageable) {
        return customObjectWithLifecycleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<CustomObjectWithLifecycle> getMultipleObjectsWithLifecycle(List<Integer> ids) {
        return customObjectWithLifecycleRepository.findByIdIn(ids);
    }

    /* Custom revisioned object methods */
    @Transactional
    public CustomRevisionedObject updateRevisionedObject(CustomRevisionedObject customRevisionedObject) {
        return customRevisionedObjectRepository.save(customRevisionedObject);
    }

    @Transactional(readOnly = true)
    public CustomRevisionedObject getRevisionedObject(Integer id) {
        return customRevisionedObjectRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Page<CustomRevisionedObject> getRevisionedObjects(Pageable pageable) {
        return customRevisionedObjectRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<CustomRevisionedObject> getMultipleRevisionedObjects(List<Integer> ids) {
        return customRevisionedObjectRepository.findByIdIn(ids);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','custombom')")
    public List<CustomObjectBomDto> createMultipleCustomObjectBom(Integer id,
                                                                  List<CustomObjectBomDto> customObjectBomDtos) {
        CustomObject object = customObjectRepository.findOne(id);
        Integer count = customObjectBomRepository
                .getCustomObjectBomCountByParent(customObjectBomDtos.get(0).getParent());
        count = count + 1;
        List<CustomObjectBom> customObjectBoms = new LinkedList<>();
        for (CustomObjectBomDto customObjectBomDto : customObjectBomDtos) {
            checkForCyclicalUpwards(customObjectBomDto.getParent(), customObjectBomDto);
            CustomObjectBom customObjectBom = new CustomObjectBom();
            customObjectBom.setParent(customObjectRepository.findOne(customObjectBomDto.getParent()));
            customObjectBom.setChild(customObjectRepository.findOne(customObjectBomDto.getChild()));
            customObjectBom.setQuantity(customObjectBomDto.getQuantity());
            customObjectBom.setNotes(customObjectBomDto.getNotes());
            customObjectBom.setSequence(count);
            count++;
            customObjectBoms.add(customObjectBom);
        }
        customObjectBoms = customObjectBomRepository.save(customObjectBoms);
        applicationEventPublisher
                .publishEvent(new CustomObjectEvents.CustomObjectBomsAddedEvent(object, customObjectBoms));
        return customObjectBomDtos;
    }

    private void checkForCyclicalUpwards(Integer parent, CustomObjectBomDto bomObject) {
        if (parent.equals(bomObject.getChild())) {
            throw new CassiniException(
                    messageSource.getMessage("cannot_add_item_to_BOM_as_it_creates_a_recursive_infinite_loop",
                            null, "Cannot add item to BOM as it creates a recursive infinite loop",
                            LocaleContextHolder.getLocale()));
        }
        CustomObject customObject = customObjectRepository.findOne(parent);
        List<CustomObjectBom> boms = customObjectBomRepository.findByChild(customObject);
        boms.forEach(bom -> checkForCyclicalUpwards(bom.getParent().getId(), bomObject));
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','custombom')")
    public CustomObjectBomDto createCustomObjectBom(Integer id, CustomObjectBomDto customObjectBomDto) {
        CustomObject object = customObjectRepository.findOne(id);
        Integer count = customObjectBomRepository.getCustomObjectBomCountByParent(customObjectBomDto.getParent());
        checkForCyclicalUpwards(customObjectBomDto.getParent(), customObjectBomDto);
        CustomObjectBom customObjectBom = new CustomObjectBom();
        customObjectBom.setParent(customObjectRepository.findOne(customObjectBomDto.getParent()));
        customObjectBom.setChild(customObjectRepository.findOne(customObjectBomDto.getChild()));
        customObjectBom.setQuantity(customObjectBomDto.getQuantity());
        customObjectBom.setNotes(customObjectBomDto.getNotes());
        customObjectBom.setSequence(count + 1);
        customObjectBom = customObjectBomRepository.save(customObjectBom);
        applicationEventPublisher
                .publishEvent(new CustomObjectEvents.CustomObjectBomAddedEvent(object, customObjectBom));
        return convertBomToDto(customObjectBom);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'edit','custombom')")
    public CustomObjectBomDto updateCustomObjectBom(Integer id, CustomObjectBomDto customObjectBomDto) {
        CustomObject object = customObjectRepository.findOne(id);
        CustomObjectBom oldBom = JsonUtils.cloneEntity(customObjectBomRepository.findOne(customObjectBomDto.getId()),
                CustomObjectBom.class);
        CustomObjectBom customObjectBom = customObjectBomRepository.findOne(customObjectBomDto.getId());
        customObjectBom.setQuantity(customObjectBomDto.getQuantity());
        customObjectBom.setNotes(customObjectBomDto.getNotes());
        customObjectBom = customObjectBomRepository.save(customObjectBom);
        applicationEventPublisher
                .publishEvent(new CustomObjectEvents.CustomObjectBomUpdatedEvent(object, oldBom, customObjectBom));
        return convertBomToDto(customObjectBom);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','custombom')")
    public void deleteCustomObjectBom(Integer id, Integer bomId) {
        CustomObject object = customObjectRepository.findOne(id);
        CustomObjectBom objectBom = customObjectBomRepository.findOne(bomId);
        List<CustomObjectBom> bomList = customObjectBomRepository
                .findByParentOrderBySequenceAsc(objectBom.getParent().getId());
        for (CustomObjectBom customObjectBom : bomList) {
            if (customObjectBom.getSequence() != null && objectBom.getSequence() != null
                    && customObjectBom.getSequence() > objectBom.getSequence()) {
                customObjectBom.setSequence(customObjectBom.getSequence() - 1);
                customObjectBom = customObjectBomRepository.save(customObjectBom);
            }
        }
        customObjectBomRepository.delete(bomId);
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectBomDeletedEvent(object, objectBom));
    }

    @Transactional(readOnly = true)
    public CustomObjectBomDto getCustomObjectBom(Integer id, Integer bomId) {
        CustomObjectBomDto objectBomDto = new CustomObjectBomDto();
        CustomObjectBom customObjectBom = customObjectBomRepository.findOne(bomId);
        objectBomDto.setId(customObjectBom.getId());
        objectBomDto.setParent(customObjectBom.getParent().getId());
        objectBomDto.setChild(customObjectBom.getChild().getId());
        objectBomDto.setQuantity(customObjectBom.getQuantity());
        objectBomDto.setNotes(customObjectBom.getNotes());
        Integer count = customObjectBomRepository.getCustomObjectBomCountByParent(bomId);
        if (count > 0) {
            objectBomDto.setHasBom(true);
        }
        return objectBomDto;
    }

    @Transactional(readOnly = true)
    public List<CustomObjectBomDto> getAllCustomObjectBom(Integer id, Boolean hierarchy) {
        List<CustomObjectBomDto> objectBomDto = new LinkedList<>();
        List<CustomObjectBom> customObjectBoms = customObjectBomRepository.findByParentOrderBySequenceAsc(id);
        customObjectBoms.forEach(customObjectBom -> {
            CustomObjectBomDto bomDto = convertBomToDto(customObjectBom);
            if (hierarchy) {
                bomDto = visitChildrenForCustomBomChildren(bomDto);
            }
            Integer count = customObjectBomRepository
                    .getCustomObjectBomCountByParent(customObjectBom.getChild().getId());
            if (count > 0) {
                bomDto.setHasBom(true);
                bomDto.setCount(count);
            }
            objectBomDto.add(bomDto);
        });
        return objectBomDto;
    }

    private CustomObjectBomDto visitChildrenForCustomBomChildren(CustomObjectBomDto dto) {

        List<CustomObjectBom> bomList = customObjectBomRepository.findByParentOrderBySequenceAsc(dto.getChild());
        bomList.forEach(bom -> {
            CustomObjectBomDto bomDto = convertBomToDto(bom);
            dto.setHasBom(true);
            dto.getChildren().add(bomDto);
            bomDto = visitChildrenForCustomBomChildren(bomDto);
        });

        return dto;
    }

    private CustomObjectBomDto convertBomToDto(CustomObjectBom customObjectBom) {
        CustomObjectBomDto bomDto = new CustomObjectBomDto();
        bomDto.setId(customObjectBom.getId());
        bomDto.setParent(customObjectBom.getParent().getId());
        bomDto.setChild(customObjectBom.getChild().getId());
        bomDto.setQuantity(customObjectBom.getQuantity());
        bomDto.setNotes(customObjectBom.getNotes());
        bomDto.setNumber(customObjectBom.getChild().getNumber());
        bomDto.setTypeName(customObjectBom.getChild().getType().getName());
        bomDto.setName(customObjectBom.getChild().getName());
        bomDto.setDescription(customObjectBom.getChild().getDescription());
        bomDto.setSequence(customObjectBom.getSequence());
        Integer count = customObjectBomRepository.getCustomObjectBomCountByParent(customObjectBom.getChild().getId());
        bomDto.setCount(count);
        if (count > 0) {
            bomDto.setHasBom(true);
        }
        return bomDto;
    }

    @Transactional(readOnly = true)
    public List<CustomObjectRelated> getRelatedCustomObjects(Integer id) {
        return customObjectRelatedRepository.findByParentOrderBySequenceAsc(id);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','customrelated')")
    public List<CustomObjectRelated> createMultipleRelatedCustomObject(Integer id,
                                                                       List<CustomObjectRelated> customObjectRelatedList) {
        CustomObject object = customObjectRepository.findOne(id);
        Integer count = customObjectRelatedRepository.getRelatedCustomObjectCountByParent(id);
        count = count + 1;
        List<CustomObjectRelated> customObjectRelateds = new LinkedList<>();
        for (CustomObjectRelated customObjectRelated : customObjectRelatedList) {
            customObjectRelated.setSequence(count);
            count++;
            customObjectRelateds.add(customObjectRelated);
        }
        customObjectRelateds = customObjectRelatedRepository.save(customObjectRelateds);
        applicationEventPublisher
                .publishEvent(new CustomObjectEvents.CustomObjectRelatedsAddedEvent(object, customObjectRelateds));
        return customObjectRelateds;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','customrelated')")
    public CustomObjectRelated createRelatedCustomObject(Integer id, CustomObjectRelated customObjectRelated) {
        CustomObject object = customObjectRepository.findOne(id);
        Integer count = customObjectRelatedRepository.getRelatedCustomObjectCountByParent(id);
        customObjectRelated.setSequence(count + 1);
        customObjectRelated = customObjectRelatedRepository.save(customObjectRelated);
        applicationEventPublisher
                .publishEvent(new CustomObjectEvents.CustomObjectRelatedAddedEvent(object, customObjectRelated));
        return customObjectRelated;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'edit','customrelated')")
    public CustomObjectRelated updateRelatedCustomObject(Integer id, CustomObjectRelated customObjectRelated) {
        CustomObject object = customObjectRepository.findOne(id);
        CustomObjectRelated oldRelated = JsonUtils.cloneEntity(
                customObjectRelatedRepository.findOne(customObjectRelated.getId()), CustomObjectRelated.class);
        customObjectRelated = customObjectRelatedRepository.save(customObjectRelated);
        applicationEventPublisher.publishEvent(
                new CustomObjectEvents.CustomObjectRelatedUpdatedEvent(object, oldRelated, customObjectRelated));
        return customObjectRelated;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','customrelated')")
    public void deleteRelatedCustomObject(Integer id, Integer relatedId) {
        CustomObject object = customObjectRepository.findOne(id);
        CustomObjectRelated related = customObjectRelatedRepository.findOne(relatedId);
        List<CustomObjectRelated> relatedList = customObjectRelatedRepository.findByParentOrderBySequenceAsc(id);
        for (CustomObjectRelated customObjectRelated : relatedList) {
            if (customObjectRelated.getSequence() != null && related.getSequence() != null
                    && customObjectRelated.getSequence() > related.getSequence()) {
                customObjectRelated.setSequence(customObjectRelated.getSequence() - 1);
                customObjectRelated = customObjectRelatedRepository.save(customObjectRelated);
            }
        }
        customObjectRelatedRepository.delete(related);
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectRelatedDeletedEvent(object, related));
    }

    @Transactional(readOnly = true)
    public Integer getCustomObjectByType(Integer typeId) {
        return customObjectRepository.getCustomObjectByType(typeId);
    }

    @Transactional(readOnly = true)
    public Page<CustomObject> advancedSearchItem(Integer objectTypeId,
                                                 CustomParameterCriteria[] customParameterCriterias, Pageable pageable) {
        TypedQuery<CustomObject> typedQuery = objectAdvancedCriteria.getObjectTypeQuery(objectTypeId,
                customParameterCriterias);
        List<CustomObject> resultlist1 = typedQuery.getResultList();

        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > resultlist1.size() ? resultlist1.size()
                : (start + pageable.getPageSize());
        Page<CustomObject> plmItemPage = new PageImpl<CustomObject>(resultlist1.subList(start, end), pageable,
                resultlist1.size());
        return plmItemPage;
    }

    @Transactional(readOnly = true)
    public Page<CustomObject> searchItems(Predicate predicate, Pageable pageable) {
        Page<CustomObject> customObjects = customObjectRepository.findAll(predicate, pageable);

        return customObjects;
    }

    @Transactional(readOnly = true)
    public List<CustomObjectBom> getCustomObjectWhereUsed(Integer objectId, Boolean hierarchy) {
        List<CustomObjectBom> customObjectBoms = customObjectBomRepository.findByChildOrderBySequenceAsc(objectId);
        List<CustomObjectBom> whereUsedBom = new ArrayList<>();
        for (CustomObjectBom customObjectBom : customObjectBoms) {
            Integer bomCount = customObjectBomRepository.getObjectsCountByParent(customObjectBom.getParent().getId());
            if (hierarchy) {
                visitItemWhereUsedParent(customObjectBom);
            }
            whereUsedBom.add(customObjectBom);
        }
        return whereUsedBom;
    }

    private void visitItemWhereUsedParent(CustomObjectBom customObjectBom) {
        List<CustomObjectBom> list = customObjectBomRepository
                .findByChildOrderBySequenceAsc(customObjectBom.getParent().getId());
        list.forEach(bom -> {
            Integer bomCount = customObjectBomRepository.getObjectsCountByParent(customObjectBom.getParent().getId());
            customObjectBom.getChildren().add(bom);
            visitItemWhereUsedParent(bom);
        });
    }

    public void sendCustomObjectCreatedNotification(CustomObject customObject) {
        String[] recipientAddress = null;
        List<Person> persons = new ArrayList<>();
        if (!customObject.getType().getName().equals("CPI Form")) {
            persons.addAll(getQARolePerson("DEFAULT_QUALITY_ADMINISTRATOR_ROLE"));
            persons.addAll(getQARolePerson("DEFAULT_QUALITY_ANALYST_ROLE"));
        }
        if (customObject.getSupplier() != null) {
            Person person = personRepository.findByEmailIgnoreCase(customObject.getSupplierEmail());
            if (person != null) {
                persons.add(person);
            }
        }

        if (persons.size() > 0) {
            String email = "";
            recipientAddress = new String[persons.size()];
            for (int i = 0; i < persons.size(); i++) {
                Person subscribe = persons.get(i);
                if (email.equals("")) {
                    email = subscribe.getEmail();
                } else {
                    email = email + "," + subscribe.getEmail();
                }
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            Date createdDate = customObject.getCreatedDate();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
            String currentDateStr = df.format(createdDate);
            List<AttributeDetailsDto> list = getObjectAttributes(customObject);
            Person person = personRepository.findOne(customObject.getCreatedBy());
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            final String[] recipientAddressFinal = recipientAddress;
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                if (persons.size() > 1) {
                    model.put("personName", "all");
                } else if (persons.size() == 1) {
                    model.put("personName", persons.get(0).getFullName());
                }
                model.put("host", host);
                model.put("message", customObject.getName());
                model.put("number", customObject.getNumber());
                model.put("createdByName", person.getFullName());
                model.put("currentDateStr", currentDateStr);
                model.put("customObjectType", customObject.getType().getName());
                model.put("description", customObject.getDescription());
                model.put("attributes", list);
                if (customObject.getSupplier() != null) {
                    model.put("supplierName", customObject.getSupplierName());
                }
                Mail mail = new Mail();
                mail.setMailToList(recipientAddressFinal);
                if (!customObject.getType().getName().equals("CPI Form")) {
                    mail.setMailSubject(customObject.getName() + " Create Notification");
                } else {
                    mail.setMailSubject(customObject.getName());
                }
                mail.setTemplatePath("email/security/customObjectCreatedMail.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    private List<Person> getQARolePerson(String name) {
        List<Person> persons = new ArrayList<>();
        Preference pref = preferenceRepository.findByPreferenceKey(name);
        PersonGroup personGroup = null;
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer id = jsonNode.get("typeId").asInt();
                    if (id != null) {
                        personGroup = personGroupRepository.findOne(id);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        if (personGroup != null) {
            List<GroupMember> groupMembers = groupMemberRepository.findByPersonGroup(personGroup);
            groupMembers.forEach(groupMember -> {
                Login login = loginRepository.findByPersonId(groupMember.getPerson().getId());
                if (login != null && login.getIsActive()) {
                    persons.add(groupMember.getPerson());
                }
            });
        }
        return persons;
    }

    @Transactional(readOnly = true)
    public List<AttributeDetailsDto> getObjectAttributes(CustomObject customObject) {
        List<AttributeDetailsDto> list = new ArrayList<>();
        Map<Integer, AttributeDetailsDto> attributeDetailsMap = new HashMap<>();
        Map<String, AttributeDetailsDto> attributeNameMap = new HashMap<>();
        Map<String, Object> attributes = new HashMap();
        List<Integer> ids = new ArrayList<>();
        List<CustomObjectTypeAttribute> objectTypeAttributes = customObjectTypeService.getAttributes(customObject.getType().getId(), true);
        objectTypeAttributes.forEach(customObjectTypeAttribute -> {
            AttributeDetailsDto attributeDetailsDto = new AttributeDetailsDto();
            attributeDetailsDto.setId(customObjectTypeAttribute.getId());
            attributeDetailsDto.setAttributeName(customObjectTypeAttribute.getName());
            attributeDetailsDto.setDataType(customObjectTypeAttribute.getDataType());
            attributeDetailsDto.setListMultiple(customObjectTypeAttribute.isListMultiple());
            attributeDetailsDto.setFormula(customObjectTypeAttribute.getFormula());
            ids.add(customObjectTypeAttribute.getId());
            attributeDetailsMap.put(customObjectTypeAttribute.getId(), attributeDetailsDto);
            attributeNameMap.put(customObjectTypeAttribute.getName(), attributeDetailsDto);
        });

        if (ids.size() > 0) {
            List<CustomObjectAttribute> customObjectAttributes = customObjectAttributeRepository.getObjectAttributesByObjectIdAndAttributeDefs(customObject.getId(), ids);
            customObjectAttributes.forEach(customObjectAttribute -> {
                AttributeDetailsDto detailsDto = attributeDetailsMap.get(customObjectAttribute.getId().getAttributeDef());
                if (detailsDto.getDataType().equals(DataType.TEXT)) {
                    detailsDto.setStringValue(customObjectAttribute.getStringValue());
                    if (customObjectAttribute.getStringValue() != null) {
                        attributes.put(detailsDto.getAttributeName(), customObjectAttribute.getStringValue());
                    }
                } else if (detailsDto.getDataType().equals(DataType.INTEGER)) {
                    detailsDto.setIntegerValue(customObjectAttribute.getIntegerValue());
                    if (customObjectAttribute.getIntegerValue() != null) {
                        attributes.put(detailsDto.getAttributeName(), customObjectAttribute.getIntegerValue());
                    }
                } else if (detailsDto.getDataType().equals(DataType.ATTACHMENT)) {
                    if (customObjectAttribute.getAttachmentValues().length > 0) {
                        List<Integer> attributeAttachmentIds = new ArrayList<Integer>();
                        for (Integer integer : customObjectAttribute.getAttachmentValues()) {
                            attributeAttachmentIds.add(integer);
                        }
                        List<String> attachments = attachmentRepository.getAttachmentNamesByIds(attributeAttachmentIds);
                        detailsDto.setAttachments(attachments);
                    }
                } else if (detailsDto.getDataType().equals(DataType.BOOLEAN)) {
                    if (customObjectAttribute.getBooleanValue() != null) {
                        if (customObjectAttribute.getBooleanValue()) {
                            detailsDto.setBooleanValue("True");
                        } else {
                            detailsDto.setBooleanValue("False");
                        }
                    }
                } else if (detailsDto.getDataType().equals(DataType.CURRENCY)) {
                    detailsDto.setCurrencyValue(customObjectAttribute.getCurrencyValue());
                } else if (detailsDto.getDataType().equals(DataType.DATE)) {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    if (customObjectAttribute.getDateValue() != null) {
                        String dateValue = df.format(customObjectAttribute.getDateValue());
                        detailsDto.setDateValue(dateValue);
                    }
                } else if (detailsDto.getDataType().equals(DataType.DOUBLE)) {
                    detailsDto.setDoubleValue(customObjectAttribute.getDoubleValue());
                    if (customObjectAttribute.getDoubleValue() != null) {
                        attributes.put(detailsDto.getAttributeName(), customObjectAttribute.getDoubleValue());
                    }
                } else if (detailsDto.getDataType().equals(DataType.FORMULA)) {
                    detailsDto.setFormulaValue(customObjectAttribute.getFormulaValue());
                } else if (detailsDto.getDataType().equals(DataType.HYPERLINK)) {
                    detailsDto.setHyperLinkValue(customObjectAttribute.getHyperLinkValue());
                } else if (detailsDto.getDataType().equals(DataType.LIST) && !detailsDto.getListMultiple()) {
                    detailsDto.setListValue(customObjectAttribute.getListValue());
                } else if (detailsDto.getDataType().equals(DataType.LIST) && detailsDto.getListMultiple()) {
                    List<String> strings = new ArrayList<String>();
                    for (int i = 0; i < customObjectAttribute.getMListValue().length; i++) {
                        strings.add(customObjectAttribute.getMListValue()[i]);
                    }
                    detailsDto.setMultiValues(strings);
                } else if (detailsDto.getDataType().equals(DataType.LONGTEXT)) {
                    detailsDto.setLongTextValue(customObjectAttribute.getLongTextValue());
                    if (customObjectAttribute.getLongTextValue() != null) {
                        attributes.put(detailsDto.getAttributeName(), customObjectAttribute.getLongTextValue());
                    }
                } else if (detailsDto.getDataType().equals(DataType.MULTILISTCHECKBOX)) {
                    List<String> strings = new ArrayList<String>();
                    for (int i = 0; i < customObjectAttribute.getMListValue().length; i++) {
                        strings.add(customObjectAttribute.getMListValue()[i]);
                    }
                    detailsDto.setMultiValues(strings);
                } else if (detailsDto.getDataType().equals(DataType.TIME)) {
                    detailsDto.setTimeValue(customObjectAttribute.getTimeValue().toString());
                } else if (detailsDto.getDataType().equals(DataType.TIMESTAMP)) {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
                    if (customObjectAttribute.getTimestampValue() != null) {
                        String dateValue = df.format(customObjectAttribute.getTimestampValue());
                        detailsDto.setTimestampValue(dateValue);
                    }
                }
                attributeDetailsMap.put(customObjectAttribute.getId().getAttributeDef(), detailsDto);
                attributeNameMap.put(detailsDto.getAttributeName(), detailsDto);
            });
        }

        list = attributeDetailsMap.values().stream().collect(Collectors.toList());
        for (AttributeDetailsDto detailsDTO : list) {
            AttributeDetailsDto detailsDto = attributeNameMap.get(detailsDTO.getAttributeName());
            if (detailsDto != null && detailsDto.getDataType().toString().equals("FORMULA")) {
                try {
                    detailsDto.setFormulaValue(parser.parseExpression(detailsDto.getFormula()).getValue(attributes, String.class));
                } catch (SpelEvaluationException e) {

                }
            }
        }
        return list;
    }

}
