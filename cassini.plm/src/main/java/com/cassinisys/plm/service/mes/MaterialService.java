package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.MaterialEvents;
import com.cassinisys.plm.filtering.MaterialCriteria;
import com.cassinisys.plm.filtering.MaterialPredicateBuilder;
import com.cassinisys.plm.model.mes.MESMaterial;
import com.cassinisys.plm.model.mes.MESMaterialType;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.QMESMaterial;
import com.cassinisys.plm.model.mes.dto.MaterialDto;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.repo.mes.MESMaterialRepository;
import com.cassinisys.plm.repo.mes.MESObjectAttributeRepository;
import com.cassinisys.plm.repo.mes.MaterialTypeRepository;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Service
public class MaterialService implements CrudService<MESMaterial, Integer> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MESMaterialRepository materialRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private MaterialPredicateBuilder materialPredicateBuilder;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectFileService objectFileService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#material,'create')")
    public MESMaterial create(MESMaterial material) {
        List<MESObjectAttribute> mesObjectAttributes = material.getMesObjectAttributes();
        MESMaterial existMaterial = materialRepository.findByNumber(material.getNumber());
        MESMaterial existMaterialName = materialRepository.findByName(material.getName());
        if (existMaterial != null) {
            String message = messageSource.getMessage("material_number_already_exists", null, "{0} Material number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existMaterial.getNumber());
            throw new CassiniException(result);
        }
        if (existMaterialName != null) {
            String message = messageSource.getMessage("material_name_already_exists", null, "{0} Material name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existMaterialName.getName());
            throw new CassiniException(result);
        }
        autoNumberService.saveNextNumber(material.getType().getAutoNumberSource().getId(), material.getNumber());
        material = materialRepository.save(material);
        for (MESObjectAttribute attribute : mesObjectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(material.getId(), attribute.getId().getAttributeDef()));
                mesObjectAttributeRepository.save(attribute);
            }
        }
        applicationEventPublisher.publishEvent(new MaterialEvents.MaterialCreatedEvent(material));
        return material;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#material.id ,'edit')")
    public MESMaterial update(MESMaterial material) {
        MESMaterial oldMaterial = JsonUtils.cloneEntity(materialRepository.findOne(material.getId()), MESMaterial.class);
        MESMaterial existMaterialName = materialRepository.findByName(material.getName());
        if (existMaterialName != null && !existMaterialName.getId().equals(material.getId())) {
            throw new CassiniException(messageSource.getMessage("material_already_exist", null, "Material name already exist", LocaleContextHolder.getLocale()));
        }
        material = materialRepository.save(material);
        oldMaterial.setQomObject(measurementRepository.findOne(oldMaterial.getQom()));
        if (oldMaterial.getUom() != null) {
            oldMaterial.setUomObject(measurementUnitRepository.findOne(oldMaterial.getUom()));
        }
        material.setQomObject(measurementRepository.findOne(material.getQom()));
        if (material.getUom() != null) {
            material.setUomObject(measurementUnitRepository.findOne(material.getUom()));
        }
        applicationEventPublisher.publishEvent(new MaterialEvents.MaterialBasicInfoUpdatedEvent(oldMaterial, material));
        return material;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        materialRepository.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESMaterial get(Integer id) {
        MESMaterial material = materialRepository.findOne(id);
        Measurement measurement = measurementRepository.findOne(material.getQom());
        if (material.getUom() != null) {
            MeasurementUnit unit = measurementUnitRepository.findOne(material.getUom());
            material.setUomObject(unit);
        }
        measurement.getMeasurementUnits().addAll(measurementUnitRepository.findByMeasurementOrderByIdAsc(measurement.getId()));
        material.setQomObject(measurement);
        return material;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMaterial> getAll() {
        return materialRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMaterial> findMultiple(List<Integer> ids) {
        return materialRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<MaterialDto> getAllMaterials(Pageable pageable, MaterialCriteria materialCriteria) {
        Predicate predicate = materialPredicateBuilder.build(materialCriteria, QMESMaterial.mESMaterial);
        Page<MESMaterial> materials = materialRepository.findAll(predicate, pageable);

        List<MaterialDto> materialDtos = new LinkedList<>();
        materials.getContent().forEach(material -> {
            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(material.getId(), PLMObjectType.MESOBJECT,false);
            MaterialDto materialDto = new MaterialDto();
           materialDto.setMaterialFiles(objectFileDto.getObjectFiles());
            materialDto.setId(material.getId());
            materialDto.setName(material.getName());
            materialDto.setNumber(material.getNumber());
            materialDto.setDescription(material.getDescription());
            materialDto.setObjectType(material.getObjectType().name());
            materialDto.setSubType(material.getType().getName());
            materialDto.setType(material.getType());
            materialDto.setQomName(measurementRepository.findOne(material.getQom()).getName());
            materialDto.setQom(material.getQom());
            if (material.getUom() != null) {
                materialDto.setUom(material.getUom());;
                materialDto.setUomName(measurementUnitRepository.findOne(material.getUom()).getName());
            }
            materialDto.setCreatedBy(personRepository.findOne(material.getCreatedBy()).getFullName());
            materialDto.setCreatedDate(material.getCreatedDate());
            materialDto.setModifiedBy(personRepository.findOne(material.getModifiedBy()).getFullName());
            materialDto.setModifiedDate(material.getModifiedDate());
            if (material.getImage() != null) {
                materialDto.setHasImage(true);
            }
            materialDtos.add(materialDto);
        });

        return new PageImpl<MaterialDto>(materialDtos, pageable, materials.getTotalElements());
    }

    @Transactional
    public MESMaterial uploadImage(Integer id, MultipartHttpServletRequest request) {
        MESMaterial material = materialRepository.findOne(id);
        if (material != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    material.setImage(file.getBytes());
                    material = materialRepository.save(material);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return material;
    }

    public void downloadImage(Integer toolId, HttpServletResponse response) {
        MESMaterial material = materialRepository.findOne(toolId);
        if (material != null) {
            InputStream is = new ByteArrayInputStream(material.getImage());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESMaterial> getObjectsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESMaterialType type = materialTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return materialRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESMaterialType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESMaterialType> children = materialTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESMaterialType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<MESMaterial> getAllFilteredMaterials(Pageable pageable, MaterialCriteria materialCriteria) {
        Predicate predicate = materialPredicateBuilder.build(materialCriteria, QMESMaterial.mESMaterial);
        Page<MESMaterial> materials = materialRepository.findAll(predicate, pageable);

        materials.getContent().forEach(material -> {
            material.setQomName(measurementRepository.findOne(material.getQom()).getName());
            if (material.getUom() != null) {
                material.setUomName(measurementUnitRepository.findOne(material.getUom()).getName());
            }
         
        });

        return materials;
    }

}
